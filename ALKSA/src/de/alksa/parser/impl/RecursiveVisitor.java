package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.Token;

/**
 * Processes everything. Can be applied multiple times to different "levels".
 * Must be applied to the different parts of a statement: SelectList, FromList,
 * WhereClause, etc.
 */
public class RecursiveVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		collectColumnList(node);
		collectFromList(node);
		collectJoinList(node);
		// TODO collect WHERE fragments (clauses)

		return node;
	}

	/**
	 * Collects the column lists, i.e. SELECT col1, col2, ABS(col3) FROM ..
	 * WHERE col4, col5 ..
	 */
	private void collectColumnList(Visitable node) throws StandardException {
		List<Token> columnTokens = new ArrayList<>();

		columnTokens.addAll(visitColumnNames(node));
		columnTokens.addAll(visitFunctions(node));
		columnTokens.addAll(visitCalculations(node));
		// TODO add other types like nested expression, ..

		addAllTokens(columnTokens);
	}

	private List<? extends Token> visitColumnNames(Visitable node)
			throws StandardException {

		AbstractVisitor columnNameVisitor = new ColumnNameVisitor();
		node.accept(columnNameVisitor);

		return columnNameVisitor.getTokens();
	}

	private List<? extends Token> visitFunctions(Visitable node)
			throws StandardException {
		AbstractVisitor functionColumnVisitor = new FunctionVisitor();
		node.accept(functionColumnVisitor);

		return functionColumnVisitor.getTokens();
	}

	private List<? extends Token> visitCalculations(Visitable node)
			throws StandardException {
		AbstractVisitor calculationVisitor = new CalculationVisitor();
		node.accept(calculationVisitor);

		return calculationVisitor.getTokens();
	}

	/**
	 * Collects the FROM list: FROM table1, table2, etc.
	 */
	private void collectFromList(Visitable node) throws StandardException {
		List<Token> fromTableTokens = new ArrayList<>();

		fromTableTokens.addAll(visitFromBaseTables(node));

		addAllTokens(fromTableTokens);
	}

	private List<? extends Token> visitFromBaseTables(Visitable node)
			throws StandardException {
		AbstractVisitor fromTableVisitor = new FromBaseTableVisitor();
		node.accept(fromTableVisitor);

		return fromTableVisitor.getTokens();
	}

	/**
	 * Collects the JOIN list: table1 JOIN table2
	 */
	private void collectJoinList(Visitable node) throws StandardException {
		List<Token> joinTokens = new ArrayList<>();

		joinTokens.addAll(visitJoinClauses(node));

		addAllTokens(joinTokens);
	}

	private List<? extends Token> visitJoinClauses(Visitable node)
			throws StandardException {
		AbstractVisitor joinVisitor = new FromJoinVisitor();
		node.accept(joinVisitor);

		return joinVisitor.getTokens();
	}

}

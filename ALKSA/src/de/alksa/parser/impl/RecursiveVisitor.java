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
		// TODO collect FROM, JOIN, WHERE fragments (clauses)

		return node;
	}

	/**
	 * Collects the column lists, i.e. SELECT col1, col2, ABS(col3) FROM .. WHERE
	 * col4, col5 ..
	 */
	private void collectColumnList(Visitable node) throws StandardException {
		List<Token> columnTokens = new ArrayList<>();

		columnTokens.addAll(visitColumnNames(node));
		columnTokens.addAll(visitFunctions(node));
		// TODO add other types like CAlculations, Mixed (nested), ..

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

}

package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;

/**
 * Processes everything. Can be applied multiple times to different "levels".
 * Must be applied to the different parts of a statement: SelectList, FromList,
 * WhereClause, etc.
 */
public class RecursiveVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		visitConstants(node);
		visitColumnNames(node);
		visitFunctions(node);
		visitCalculations(node);
		// TODO add other types like nested expression, ..

		visitFromBaseTables(node);
		visitJoins(node);
		visitFilters(node);

		return node;
	}

	private void visitConstants(Visitable node) throws StandardException {
		AbstractVisitor constantVisitor = new ConstantVisitor();
		node.accept(constantVisitor);

		addAllTokens(constantVisitor.getTokens());
	}

	private void visitColumnNames(Visitable node) throws StandardException {

		AbstractVisitor columnNameVisitor = new ColumnNameVisitor();
		node.accept(columnNameVisitor);

		addAllTokens(columnNameVisitor.getTokens());
	}

	private void visitFunctions(Visitable node) throws StandardException {
		AbstractVisitor functionColumnVisitor = new FunctionVisitor();
		node.accept(functionColumnVisitor);

		addAllTokens(functionColumnVisitor.getTokens());
	}

	private void visitCalculations(Visitable node) throws StandardException {
		AbstractVisitor calculationVisitor = new CalculationVisitor();
		node.accept(calculationVisitor);

		addAllTokens(calculationVisitor.getTokens());
	}

	private void visitFromBaseTables(Visitable node) throws StandardException {
		AbstractVisitor fromTableVisitor = new FromBaseTableVisitor();
		node.accept(fromTableVisitor);

		addAllTokens(fromTableVisitor.getTokens());
	}

	private void visitJoins(Visitable node) throws StandardException {
		AbstractVisitor joinVisitor = new FromJoinVisitor();
		node.accept(joinVisitor);

		addAllTokens(joinVisitor.getTokens());
	}

	private void visitFilters(Visitable node) throws StandardException {
		AbstractVisitor filterVisitor = new FilterVisitor();
		node.accept(filterVisitor);

		addAllTokens(filterVisitor.getTokens());
	}

}

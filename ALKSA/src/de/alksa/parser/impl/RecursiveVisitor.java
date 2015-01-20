package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;

/**
 * Processes everything. Can be applied multiple times to different "levels".
 * Must be applied to the different parts of a statement: SelectList, FromList,
 * WhereClause, etc.
 */

// TODO move to Guice via list for OPEN/CLOSED Principle
public class RecursiveVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		addTokensFromVisitor(node, new ConstantVisitor());
		addTokensFromVisitor(node, new ColumnNameVisitor());
		addTokensFromVisitor(node, new FunctionVisitor());
		addTokensFromVisitor(node, new CalculationVisitor());
		addTokensFromVisitor(node, new FromBaseTableVisitor());
		addTokensFromVisitor(node, new FromJoinVisitor());
		addTokensFromVisitor(node, new FilterVisitor());
		
		// TODO add other types like nested expression, ..

		return node;
	}

	private void addTokensFromVisitor(Visitable node,
			AbstractVisitor visitor) throws StandardException {
		node.accept(visitor);
		addAllTokens(visitor.getTokens());
	}

}

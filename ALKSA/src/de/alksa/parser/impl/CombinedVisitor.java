package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;

/**
 * Processes everything. Can be applied multiple times to different "levels".
 * Must be applied to the different parts of a statement: SelectList, FromList,
 * WhereClause, etc.
 */

class CombinedVisitor extends AbstractVisitor {

	List<AbstractVisitor> visitors;

	public CombinedVisitor() {
		visitors = new ArrayList<>();

		visitors.add(new ConstantVisitor());
		visitors.add(new ColumnNameVisitor());
		visitors.add(new FunctionVisitor());
		visitors.add(new CalculationVisitor());
		visitors.add(new FromBaseTableVisitor());
		visitors.add(new FromJoinVisitor());
		visitors.add(new FilterVisitor());
		visitors.add(new SubqueryVisitor());

		// TODO add other types ..
	}

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		for (AbstractVisitor visitor : visitors) {
			node.accept(visitor);
			addAllTokens(visitor.getTokens());
		}

		return node;
	}

}

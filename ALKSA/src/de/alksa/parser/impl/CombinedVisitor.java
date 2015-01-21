package de.alksa.parser.impl;

import java.util.List;
import java.util.Objects;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.Visitable;
import com.google.inject.Inject;

/**
 * Processes everything. Can be applied multiple times to different "levels".
 * Must be applied to the different parts of a statement: SelectList, FromList,
 * WhereClause, etc.
 */

// TODO move to Guice via list for OPEN/CLOSED Principle
class CombinedVisitor extends AbstractVisitor {

	private List<AbstractVisitor> visitors;

	@Inject
	public CombinedVisitor(List<AbstractVisitor> visitors) {
		Objects.requireNonNull(visitors);
		this.visitors = visitors;
	}

	public CombinedVisitor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		// for (AbstractVisitor visitor : visitors) {
		// addTokensFromVisitor(node, visitor);
		// }

		addTokensFromVisitor(node, new ConstantVisitor());
		addTokensFromVisitor(node, new ColumnNameVisitor());
		addTokensFromVisitor(node, new FunctionVisitor());
		addTokensFromVisitor(node, new CalculationVisitor());
		addTokensFromVisitor(node, new FromBaseTableVisitor());
		addTokensFromVisitor(node, new FromJoinVisitor());
		addTokensFromVisitor(node, new FilterVisitor());
		addTokensFromVisitor(node, new SubqueryVisitor());

		// TODO add other types like nested expression, ..

		return node;
	}

	private void addTokensFromVisitor(Visitable node, AbstractVisitor visitor)
			throws StandardException {
		node.accept(visitor);
		addAllTokens(visitor.getTokens());
	}

}

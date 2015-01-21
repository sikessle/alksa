package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromSubquery;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.SubqueryNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

class SubqueryVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		if (node instanceof ResultColumn) {
			ResultColumn resultColumn = (ResultColumn) node;
			ValueNode expression = resultColumn.getExpression();
			if (expression instanceof SubqueryNode) {
				visitSubquery(((SubqueryNode) expression).getResultSet());
			}
		} else if (node instanceof FromSubquery || node instanceof SubqueryNode) {
			visitSubquery(node);
		}

		return node;
	}

	private void visitSubquery(Visitable subquery) throws StandardException {
		AbstractVisitor visitor = new RootVisitor();
		subquery.accept(visitor);
		addAllTokens(visitor.getTokens());
	}
}

package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.OrderByColumn;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.Token;

class OrderByListVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof OrderByColumn) {
			OrderByColumn orderBy = (OrderByColumn) node;
			AbstractVisitor visitor = new ColumnNameVisitor();
			orderBy.getExpression().accept(visitor);
			Token orderByElement = visitor.getTokens().get(0);
			addToken(orderByElement);
		}
		return node;
	}
}

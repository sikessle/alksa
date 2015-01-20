package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.ConstantValueToken;

public class ConstantVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof ConstantNode) {
			ConstantNode constantNode = (ConstantNode) node;
			addToken(new ConstantValueToken(constantNode.getValue()));
		}
		return node;
	}

}

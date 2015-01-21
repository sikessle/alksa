package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.UnionNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.SelectStatementToken;

class RootVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof UnionNode) {
			visitUnionNode((UnionNode) node);
		} else if (node instanceof SelectNode) {
			visitSelectNode(node);
		}
		return node;
	}

	private void visitUnionNode(UnionNode node) throws StandardException {
		visitSelectNode(node.getLeftResultSet());
		visitSelectNode(node.getRightResultSet());
	}

	private void visitSelectNode(Visitable node) throws StandardException {
		AbstractVisitor visitor = new SelectVisitor();
		node.accept(visitor);
		addToken(new SelectStatementToken(visitor.getTokens()));
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof UnionNode || node instanceof SelectNode) {
			return true;
		}
		return false;
	}

}

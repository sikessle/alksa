package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.SelectListToken;

public class SelectVisitor extends AbstractVisitor {
	
	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			processSelectNode((SelectNode) node);
		}
		return node;
	}

	private void processSelectNode(SelectNode select) throws StandardException {
		SelectListVisitor selectListVisitor = new SelectListVisitor();
		select.accept(selectListVisitor);
		SelectListToken selectToken = new SelectListToken(selectListVisitor.getTokens());
		addToken(selectToken);
	}

}

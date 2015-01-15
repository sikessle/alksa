package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.SelectColumnListToken;

public class SelectVisitor extends AbstractVisitor {
	
	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			processSelectNode((SelectNode) node);
		}
		return node;
	}

	/**
	 * Processes the whole node "SELECT .. FROM .. WHERE .." 
	 */
	private void processSelectNode(SelectNode select) throws StandardException {
		processSelectList(select);
		// TODO process FROM, JOIN, WHERE statements
	}

	/**
	 * Processes the SELECT [SelectList] Node.
	 */
	private void processSelectList(SelectNode select) throws StandardException {
		SelectColumnListVisitor selectListVisitor = new SelectColumnListVisitor();
		select.accept(selectListVisitor);
		SelectColumnListToken selectToken = new SelectColumnListToken(selectListVisitor.getTokens());
		addToken(selectToken);
	}

}

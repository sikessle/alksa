package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.SelectColumnListToken;

public class SelectVisitor extends AbstractVisitor {
	
	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			visitSelectNode((SelectNode) node);
		}
		return node;
	}

	/**
	 * Processes the whole node "SELECT .. FROM .. WHERE .." 
	 */
	private void visitSelectNode(SelectNode select) throws StandardException {
		RootFirstLevelVisitor rootVisitor = new RootFirstLevelVisitor();
		select.accept(rootVisitor);
		SelectColumnListToken selectToken = new SelectColumnListToken(rootVisitor.getTokens());
		addToken(selectToken);
	}

}

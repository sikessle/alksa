package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

public class SelectListVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			SelectNode select = (SelectNode) node;
		}
		return null;
	}

	@Override
	public boolean visitChildrenFirst(Visitable node) {
		return false;
	}

	@Override
	public boolean stopTraversal() {
		return false;
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		return false;
	}

}

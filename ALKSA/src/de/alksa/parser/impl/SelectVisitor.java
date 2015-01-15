package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FromListToken;
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
		ResultColumnList resultColumnList = select.getResultColumns();
		addToken(visitSelectColumnList(resultColumnList));
		
		FromList fromList = select.getFromList();
		addToken(visitSelectFromList(fromList));

		// TODO add select.getWhereClause ..
	}

	

	private SelectColumnListToken visitSelectColumnList(
			ResultColumnList columnList) throws StandardException {

		RecursiveVisitor recursiveVisitor = new RecursiveVisitor();

		for (ResultColumn resultColumn : columnList) {
			resultColumn.accept(recursiveVisitor);
		}

		return new SelectColumnListToken(recursiveVisitor.getTokens());
	}
	
	private FromListToken visitSelectFromList(FromList fromList) throws StandardException {
		RecursiveVisitor recursiveVisitor = new RecursiveVisitor();

		for (FromTable fromTable : fromList) {
			fromTable.accept(recursiveVisitor);
		}

		return new FromListToken(recursiveVisitor.getTokens());
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			return true;
		}
		return false;
	}

}

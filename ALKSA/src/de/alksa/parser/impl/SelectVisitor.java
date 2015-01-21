package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FromListToken;
import de.alksa.token.HavingClauseToken;
import de.alksa.token.SelectColumnListToken;
import de.alksa.token.Token;
import de.alksa.token.WhereClauseToken;

class SelectVisitor extends AbstractVisitor {

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

		ValueNode whereClause = select.getWhereClause();
		addToken(visitWhereClause(whereClause));

		ValueNode havingClause = select.getHavingClause();
		addToken(visitHavingClause(havingClause));
	}

	private SelectColumnListToken visitSelectColumnList(
			ResultColumnList columnList) throws StandardException {

		List<Token> tokens = new ArrayList<>();

		for (ResultColumn resultColumn : columnList) {
			tokens.addAll(visitWithCombinedVisitor(resultColumn));
		}

		return new SelectColumnListToken(tokens);
	}

	private FromListToken visitSelectFromList(FromList fromList)
			throws StandardException {

		List<Token> tokens = new ArrayList<>();

		for (FromTable fromTable : fromList) {
			tokens.addAll(visitWithCombinedVisitor(fromTable));
		}

		return new FromListToken(tokens);
	}

	private WhereClauseToken visitWhereClause(ValueNode whereClause)
			throws StandardException {
		if (whereClause == null) {
			return null;
		}

		return new WhereClauseToken(visitWithCombinedVisitor(whereClause));
	}

	private HavingClauseToken visitHavingClause(ValueNode havingClause)
			throws StandardException {
		if (havingClause == null) {
			return null;
		}

		return new HavingClauseToken(visitWithCombinedVisitor(havingClause));
	}

}

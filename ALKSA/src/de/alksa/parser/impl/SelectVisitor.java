package de.alksa.parser.impl;

import java.util.HashSet;
import java.util.Set;

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

	private SelectColumnListToken columnListToken;
	private FromListToken fromListToken;
	private WhereClauseToken whereClauseToken;
	private HavingClauseToken havingClauseToken;

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			visitSelectNode((SelectNode) node);
		}

		return node;
	}

	public SelectColumnListToken getColumnListToken() {
		return columnListToken;
	}

	public FromListToken getFromListToken() {
		return fromListToken;
	}

	public WhereClauseToken getWhereClauseToken() {
		return whereClauseToken;
	}

	public HavingClauseToken getHavingClauseToken() {
		return havingClauseToken;
	}

	/**
	 * Processes the whole node "SELECT .. FROM .. WHERE .."
	 */
	private void visitSelectNode(SelectNode select) throws StandardException {
		columnListToken = visitSelectColumnList(select.getResultColumns());

		fromListToken = visitSelectFromList(select.getFromList());

		whereClauseToken = visitWhereClause(select.getWhereClause());

		havingClauseToken = visitHavingClause(select.getHavingClause());

		addTokenIfNotNull(columnListToken);
		addTokenIfNotNull(fromListToken);
		addTokenIfNotNull(whereClauseToken);
		addTokenIfNotNull(havingClauseToken);
	}

	private void addTokenIfNotNull(Token token) {
		if (token != null) {
			addToken(token);
		}
	}

	private SelectColumnListToken visitSelectColumnList(
			ResultColumnList columnList) throws StandardException {

		Set<Token> tokens = new HashSet<>();

		for (ResultColumn resultColumn : columnList) {
			tokens.addAll(visitWithCombinedVisitor(resultColumn));
		}

		return new SelectColumnListToken(tokens);
	}

	private FromListToken visitSelectFromList(FromList fromList)
			throws StandardException {

		Set<Token> tokens = new HashSet<>();

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

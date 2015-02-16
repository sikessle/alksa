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

import de.alksa.token.Token;

class SelectVisitor extends AbstractVisitor {

	private Set<Token> columnList;
	private Set<Token> fromList;
	private Set<Token> whereClause;
	private Set<Token> havingClause;

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof SelectNode) {
			visitSelectNode((SelectNode) node);
		}

		return node;
	}

	public Set<Token> getColumnList() {
		return columnList;
	}

	public Set<Token> getFromList() {
		return fromList;
	}

	public Set<Token> getWhereClause() {
		return whereClause;
	}

	public Set<Token> getHavingClause() {
		return havingClause;
	}

	/**
	 * Processes the whole node "SELECT .. FROM .. WHERE .."
	 */
	private void visitSelectNode(SelectNode select) throws StandardException {
		columnList = visitSelectColumnList(select.getResultColumns());

		fromList = visitSelectFromList(select.getFromList());

		whereClause = visitWhereClause(select.getWhereClause());

		havingClause = visitHavingClause(select.getHavingClause());

		addAllTokens(columnList);
		addAllTokens(fromList);
		addAllTokens(whereClause);
		addAllTokens(havingClause);
	}

	private Set<Token> visitSelectColumnList(ResultColumnList columnList)
			throws StandardException {

		Set<Token> tokens = new HashSet<>();

		for (ResultColumn resultColumn : columnList) {
			tokens.addAll(visitWithCombinedVisitor(resultColumn));
		}

		return tokens;
	}

	private Set<Token> visitSelectFromList(FromList fromList)
			throws StandardException {

		Set<Token> tokens = new HashSet<>();

		for (FromTable fromTable : fromList) {
			tokens.addAll(visitWithCombinedVisitor(fromTable));
		}

		return tokens;
	}

	private Set<Token> visitWhereClause(ValueNode whereClause)
			throws StandardException {
		if (whereClause == null) {
			return null;
		}

		return visitWithCombinedVisitor(whereClause);
	}

	private Set<Token> visitHavingClause(ValueNode havingClause)
			throws StandardException {
		if (havingClause == null) {
			return null;
		}

		return visitWithCombinedVisitor(havingClause);
	}

}

package de.alksa.parser.impl;

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

		CombinedVisitor combinedVisitor = new CombinedVisitor();

		for (ResultColumn resultColumn : columnList) {
			resultColumn.accept(combinedVisitor);
		}

		return new SelectColumnListToken(combinedVisitor.getTokens());
	}

	private FromListToken visitSelectFromList(FromList fromList)
			throws StandardException {
		CombinedVisitor combinedVisitor = new CombinedVisitor();

		for (FromTable fromTable : fromList) {
			fromTable.accept(combinedVisitor);
		}

		return new FromListToken(combinedVisitor.getTokens());
	}

	private WhereClauseToken visitWhereClause(ValueNode whereClause)
			throws StandardException {
		if (whereClause == null) {
			return null;
		}

		AbstractVisitor visitor = new CombinedVisitor();
		whereClause.accept(visitor);

		return new WhereClauseToken(visitor.getTokens());
	}

	private HavingClauseToken visitHavingClause(ValueNode havingClause)
			throws StandardException {
		if (havingClause == null) {
			return null;
		}

		AbstractVisitor visitor = new CombinedVisitor();
		havingClause.accept(visitor);

		return new HavingClauseToken(visitor.getTokens());
	}

}

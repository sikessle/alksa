package de.alksa.parser.impl;

import java.util.Set;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FullOuterJoinNode;
import com.foundationdb.sql.parser.HalfOuterJoinNode;
import com.foundationdb.sql.parser.JoinNode;
import com.foundationdb.sql.parser.ResultSetNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FilterToken;
import de.alksa.token.JoinToken;
import de.alksa.token.Token;

class FromJoinVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof JoinNode) {
			JoinNode join = (JoinNode) node;
			JoinToken.Type type = getJoinType(join);
			Token left = getTokenFromJoinPart(join.getLeftResultSet());
			Token right = getTokenFromJoinPart(join.getRightResultSet());
			FilterToken onClause = getOnClause(join);

			JoinToken joinToken = new JoinToken(left, type, right);
			joinToken.setOnClause(onClause);
			addToken(joinToken);
		}
		return node;
	}

	private JoinToken.Type getJoinType(JoinNode join) {
		if (join.isNaturalJoin()) {
			return JoinToken.Type.NATURAL;
		} else if (join instanceof FullOuterJoinNode) {
			return JoinToken.Type.FULL_OUTER;
		} else if (join instanceof HalfOuterJoinNode) {
			if (((HalfOuterJoinNode) join).isRightOuterJoin()) {
				return JoinToken.Type.RIGHT_OUTER;
			}
			return JoinToken.Type.LEFT_OUTER;
		}
		return JoinToken.Type.INNER;
	}

	private Token getTokenFromJoinPart(ResultSetNode resultSet)
			throws StandardException {

		Set<Token> tokens = visitWithCombinedVisitor(resultSet);

		return tokens.iterator().next();
	}

	private FilterToken getOnClause(JoinNode join) throws StandardException {
		ValueNode joinClause = join.getJoinClause();

		if (joinClause == null) {
			return null;
		}

		AbstractVisitor visitor = new FilterVisitor();

		joinClause.accept(visitor);

		for (Token token : visitor.getTokens()) {
			if (token instanceof FilterToken) {
				return (FilterToken) token;
			}
		}

		return null;
	}

}

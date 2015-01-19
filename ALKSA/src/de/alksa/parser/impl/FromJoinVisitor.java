package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromBaseTable;
import com.foundationdb.sql.parser.FullOuterJoinNode;
import com.foundationdb.sql.parser.HalfOuterJoinNode;
import com.foundationdb.sql.parser.JoinNode;
import com.foundationdb.sql.parser.ResultSetNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.JoinToken;
import de.alksa.token.Token;

public class FromJoinVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof JoinNode) {
			JoinNode join = (JoinNode) node;
			String type = getJoinType(join);
			Token left = getTokenFromJoinPart(join.getLeftResultSet());
			Token right = getTokenFromJoinPart(join.getRightResultSet());
			
			addToken(new JoinToken(type, left, right));
		}
		return node;
	}

	private String getJoinType(JoinNode join) {
		if (join.isNaturalJoin()) {
			return "NATURAL";
		} else if (join instanceof FullOuterJoinNode) {
			return "FULL OUTER";
		} else if (join instanceof HalfOuterJoinNode) {
			if (((HalfOuterJoinNode) join).isRightOuterJoin()) {
				return "RIGHT OUTER";
			}
			return "LEFT OUTER";
		}
		return "INNER";
	}

	private Token getTokenFromJoinPart(ResultSetNode resultSet)
			throws StandardException {

		if (resultSet instanceof FromBaseTable) {
			return getFromBaseTableToken(resultSet);
		} else if (resultSet instanceof JoinNode) {
			return getJoinToken(resultSet);
		}

		throw new IllegalStateException(
				"Unknown resultSet. Not a JoinNode or FromBaseTable");
	}

	private Token getFromBaseTableToken(Visitable node)
			throws StandardException {
		AbstractVisitor visitor = new FromBaseTableVisitor();
		node.accept(visitor);

		return visitor.getTokens().get(0);
	}

	private Token getJoinToken(Visitable node) throws StandardException {
		AbstractVisitor visitor = new FromJoinVisitor();

		node.accept(visitor);

		System.out.println("-- BEGIN getJoinToken --");
		for (Token token : visitor.getTokens()) {
			System.out.println(token);
		}
		System.out.println("-- END getJoinToken --");

		return visitor.getTokens().get(0);
	}

}

package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FullOuterJoinNode;
import com.foundationdb.sql.parser.HalfOuterJoinNode;
import com.foundationdb.sql.parser.JoinNode;
import com.foundationdb.sql.parser.ResultSetNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.JoinToken;
import de.alksa.token.TableNameToken;
import de.alksa.token.Token;

public class FromJoinVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof JoinNode) {
			JoinNode join = (JoinNode) node;
			String type = getJoinType(join);
			TableNameToken left = getTableNameToken(join.getLeftResultSet());
			TableNameToken right = getTableNameToken(join.getRightResultSet());
			addToken(new JoinToken(type, left, right));
		}
		return node;
	}

	private TableNameToken getTableNameToken(ResultSetNode resultSet) throws StandardException {
		String tableName = "<unknown>";
		FromBaseTableVisitor visitor = new FromBaseTableVisitor();
		
		resultSet.accept(visitor);
		
		Token token = visitor.getTokens().get(0);
		
		if (token instanceof TableNameToken) {
			TableNameToken tableNameToken = (TableNameToken) token;
			tableName = tableNameToken.getValue();
		}
		
		return new TableNameToken(tableName);
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

}

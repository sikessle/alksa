package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AggregateNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.UnaryOperatorNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FunctionToken;
import de.alksa.token.Token;

public class FunctionVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		List<Token> parameters = new ArrayList<>();

		if (node instanceof UnaryOperatorNode) {

			String operator = null;
			UnaryOperatorNode op = (UnaryOperatorNode) node;
			ValueNode operand = op.getOperand();
			parameters.addAll(getRecursiveAllTokensOfNode(operand));

			if (op.getOperator() == null && op instanceof AggregateNode) {
				AggregateNode ag = (AggregateNode) op;
				operator = ag.getAggregateName();
			} else {
				operator = op.getOperator();
			}

			addToken(new FunctionToken(operator, parameters));

		}

		return node;
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof ResultColumn) {
			return false;
		}
		if (!(node instanceof UnaryOperatorNode)) {
			return true;
		}
		return false;
	}

}

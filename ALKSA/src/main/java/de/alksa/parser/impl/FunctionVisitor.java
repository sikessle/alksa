package de.alksa.parser.impl;

import java.util.HashSet;
import java.util.Set;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AggregateNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.UnaryArithmeticOperatorNode;
import com.foundationdb.sql.parser.UnaryOperatorNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FunctionToken;
import de.alksa.token.Token;

class FunctionVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		Set<Token> parameters = new HashSet<>();

		if (node instanceof UnaryArithmeticOperatorNode
				|| node instanceof AggregateNode) {

			String operator = null;
			UnaryOperatorNode op = (UnaryOperatorNode) node;
			ValueNode operand = op.getOperand();
			if (operand != null) {
				parameters.addAll(visitWithCombinedVisitor(operand));
			}

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

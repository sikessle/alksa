package de.alksa.parser.impl;

import java.util.Set;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryArithmeticOperatorNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.CalculationToken;
import de.alksa.token.Token;

class CalculationVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		if (node instanceof BinaryArithmeticOperatorNode) {

			BinaryArithmeticOperatorNode calc = (BinaryArithmeticOperatorNode) node;

			String left = getOperand(calc.getLeftOperand());
			String right = getOperand(calc.getRightOperand());

			addToken(new CalculationToken(left + calc.getOperator() + right));

		}

		return node;
	}

	private String getOperand(ValueNode node) throws StandardException {

		Set<Token> operand = visitWithCombinedVisitor(node);

		return operand.iterator().next().toString();
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof ResultColumn) {
			return false;
		}
		if (!(node instanceof BinaryArithmeticOperatorNode)) {
			return true;
		}
		return false;
	}

}

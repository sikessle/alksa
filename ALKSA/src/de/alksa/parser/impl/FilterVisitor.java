package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryRelationalOperatorNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.Token;
import de.alksa.token.ComparisonFilterToken.Type;

/**
 * Collects the filters in ON and WHERE Statements.
 */
public class FilterVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof BinaryRelationalOperatorNode) {
			BinaryRelationalOperatorNode opNode = (BinaryRelationalOperatorNode) node;
			addToken(getComparisonToken(opNode));
		}
		// TODO add more types like boolean etc.
		return node;
	}

	private ComparisonFilterToken getComparisonToken(
			BinaryRelationalOperatorNode opNode) throws StandardException {
		ComparisonFilterToken.Type operator = getFilterType(opNode);

		Token leftPart = getComparisonOperandToken(opNode.getLeftOperand());
		Token rightPart = getComparisonOperandToken(opNode.getRightOperand());

		return new ComparisonFilterToken(leftPart, operator, rightPart);
	}

	private Type getFilterType(BinaryRelationalOperatorNode opNode) {
		String operatorString = opNode.getOperator();

		if (operatorString.equals("=")) {
			return ComparisonFilterToken.Type.EQUAL;
		} else if (operatorString.equals(">")) {
			return ComparisonFilterToken.Type.GREATER;
		} else if (operatorString.equals(">=")) {
			return ComparisonFilterToken.Type.GREATER_EQUAL;
		} else if (operatorString.equals("<")) {
			return ComparisonFilterToken.Type.LESS;
		} else if (operatorString.equals("<=")) {
			return ComparisonFilterToken.Type.LESS_EQUAL;
		}

		throw new IllegalStateException("Unknown operator type");
	}

	private Token getComparisonOperandToken(ValueNode operand)
			throws StandardException {
		AbstractVisitor visitor = new RecursiveVisitor();
		operand.accept(visitor);

		return visitor.getTokens().get(0);
	}

}

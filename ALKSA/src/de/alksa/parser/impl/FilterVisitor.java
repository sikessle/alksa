package de.alksa.parser.impl;

import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AndNode;
import com.foundationdb.sql.parser.BinaryLogicalOperatorNode;
import com.foundationdb.sql.parser.BinaryRelationalOperatorNode;
import com.foundationdb.sql.parser.NotNode;
import com.foundationdb.sql.parser.OrNode;
import com.foundationdb.sql.parser.UnaryLogicalOperatorNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.ComparisonFilterToken;
import de.alksa.token.ComparisonFilterToken.Type;
import de.alksa.token.Token;
import de.alksa.token.UnaryLogicalFilterToken;

/**
 * Collects the filters in ON and WHERE Statements.
 */
public class FilterVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof BinaryRelationalOperatorNode) {
			BinaryRelationalOperatorNode relationalNode = (BinaryRelationalOperatorNode) node;
			addToken(getComparisonToken(relationalNode));
		} else if (node instanceof BinaryLogicalOperatorNode
				|| node instanceof UnaryLogicalOperatorNode) {
			addToken(getLogicalToken(node));
		}
		return node;
	}

	private ComparisonFilterToken getComparisonToken(
			BinaryRelationalOperatorNode relationalNode)
			throws StandardException {
		ComparisonFilterToken.Type operator = getFilterType(relationalNode);

		Token leftPart = getRecursiveAllTokensOfNode(
				relationalNode.getLeftOperand()).get(0);
		Token rightPart = getRecursiveAllTokensOfNode(
				relationalNode.getRightOperand()).get(0);

		return new ComparisonFilterToken(leftPart, operator, rightPart);
	}

	private Type getFilterType(BinaryRelationalOperatorNode relationalpNode) {
		String operatorString = relationalpNode.getOperator();

		if (operatorString.equals("=")) {
			return Type.EQUAL;
		} else if (operatorString.equals(">")) {
			return Type.GREATER;
		} else if (operatorString.equals(">=")) {
			return Type.GREATER_EQUAL;
		} else if (operatorString.equals("<")) {
			return Type.LESS;
		} else if (operatorString.equals("<=")) {
			return Type.LESS_EQUAL;
		}

		throw new IllegalStateException("Unknown operator type");
	}

	private Token getLogicalToken(Visitable logicalNode)
			throws StandardException {

		if (logicalNode instanceof AndNode) {

		} else if (logicalNode instanceof OrNode) {

		} else if (logicalNode instanceof NotNode) {
			NotNode not = (NotNode) logicalNode;
			Visitable operand = not.getOperand();
			Token operandToken = getRecursiveAllTokensOfNode(operand).get(0);

			return new UnaryLogicalFilterToken(
					UnaryLogicalFilterToken.Type.NOT, operandToken);
		}

		throw new IllegalStateException("Unknown logical token");
	}

}

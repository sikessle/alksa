package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryOperatorNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.TernaryOperatorNode;
import com.foundationdb.sql.parser.UnaryOperatorNode;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.FunctionToken;
import de.alksa.token.Token;

public class FunctionVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		List<Token> parameters = new ArrayList<>();
		FunctionToken functionToken = null;

		if (node instanceof UnaryOperatorNode) {
			
			UnaryOperatorNode op = (UnaryOperatorNode) node;
			ValueNode operand = op.getOperand();
			RecursiveVisitor recursiveVisitor = new RecursiveVisitor();
			operand.accept(recursiveVisitor);
			parameters.addAll(recursiveVisitor.getTokens());
			functionToken = new FunctionToken(op.getOperator(), parameters);
			
		} else if (node instanceof BinaryOperatorNode) {
			
			BinaryOperatorNode op = (BinaryOperatorNode) node;
			ValueNode operandLeft = op.getLeftOperand();
			ValueNode operandRight = op.getRightOperand();
			
			RecursiveVisitor leftColumnListVisitor = new RecursiveVisitor();
			operandLeft.accept(leftColumnListVisitor );
			RecursiveVisitor rightColumnListVisitor = new RecursiveVisitor();
			operandRight.accept(rightColumnListVisitor );
			
			parameters.addAll(leftColumnListVisitor.getTokens());
			parameters.addAll(rightColumnListVisitor.getTokens());
			functionToken = new FunctionToken(op.getOperator(), parameters);
			
		} else if (node instanceof TernaryOperatorNode) {
			// functionToken = new FunctionToken("func", parameters);
		}

		if (functionToken != null) {
			addToken(functionToken);
		}

		return node;
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof ResultColumn) {
			return false;
		}
		if (!(node instanceof UnaryOperatorNode)
				|| !(node instanceof BinaryOperatorNode)
				|| !(node instanceof TernaryOperatorNode)) {
			return true;
		}
		return false;
	}

}

package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryArithmeticOperatorNode;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.NumericConstantNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ValueNode;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.CalculationToken;

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

	private String getOperand(ValueNode node) {
		if (node instanceof NumericConstantNode) {
			NumericConstantNode constant = (NumericConstantNode) node;
			return constant.getValue().toString();
		} else if (node instanceof ColumnReference) {
			ColumnReference ref = (ColumnReference) node;
			return ref.getSQLColumnName();
		}

		return "<unkown>";
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

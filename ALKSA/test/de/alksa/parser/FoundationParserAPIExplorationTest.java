package de.alksa.parser;

import org.junit.Test;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryComparisonOperatorNode;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.FromBaseTable;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

public class FoundationParserAPIExplorationTest {

	@Test
	public void parseExample() {
		String query = "SELECT SUBSTR(name, 1, 5) AS BERECHNUNG, password FROM users u, emails LEFT OUTER JOIN plz p ON u.plz = p.plz RIGHT OUTER JOIN abt ON c1 = c2 AND c3 = c4 WHERE u.name = 'Simon' AND u.password = 'bar' AND emails.name = 'foo@me.com' OR u.age > 20";

		SQLParser sqlParser = new SQLParser();
		try {
			parseString(query, sqlParser);
		} catch (StandardException e) {
			System.err.println("Failed parsing");
			e.printStackTrace();
		}

	}

	private void parseString(String query, SQLParser sqlParser)
			throws StandardException {
		StatementNode statement = sqlParser.parseStatement(query);
		System.out.println(query);
		System.out.println();
		statement.treePrint();
		statement.accept(new FromStatementVisitor());
		System.out.println("Where Conditions:");
		System.out.println("+++");
		statement.accept(new WhereStatementVisitor());
		System.out.println("+++");
	}

	public class FromStatementVisitor implements Visitor {

		@Override
		public boolean skipChildren(Visitable node) throws StandardException {
			return false;
		}

		@Override
		public boolean stopTraversal() {
			return false;
		}

		@Override
		public Visitable visit(Visitable node) throws StandardException {
			if (node instanceof FromList) {
				FromList tables = (FromList) node;
				System.out.println("All Tables:");
				System.out.println("+++");
				tables.accept(new TableListVisitor());
				System.out.println("+++");
			}
			return node;
		}

		@Override
		public boolean visitChildrenFirst(Visitable node) {
			return false;
		}

	}

	public class TableListVisitor implements Visitor {

		@Override
		public boolean skipChildren(Visitable node) throws StandardException {
			return false;
		}

		@Override
		public boolean stopTraversal() {
			return false;
		}

		@Override
		public Visitable visit(Visitable node) throws StandardException {
			if (node instanceof FromBaseTable) {
				FromBaseTable table = (FromBaseTable) node;
				System.out.println(table.getOrigTableName() + " (Alias: "
						+ table.getCorrelationName() + ")");
			}
			return node;
		}

		@Override
		public boolean visitChildrenFirst(Visitable node) {
			return false;
		}

	}

	public class WhereStatementVisitor implements Visitor {

		@Override
		public boolean skipChildren(Visitable node) throws StandardException {
			return false;
		}

		@Override
		public boolean stopTraversal() {
			return false;
		}

		@Override
		public Visitable visit(Visitable node) throws StandardException {
			if (node instanceof BinaryComparisonOperatorNode) {
				BinaryComparisonOperatorNode where = (BinaryComparisonOperatorNode) node;

				if (where.getLeftOperand() instanceof ColumnReference) {
					System.out.print(where.getLeftOperand().getTableName()
							+ "." + where.getLeftOperand().getColumnName());
				} else if (where.getLeftOperand() instanceof ConstantNode) {
					ConstantNode leftValue = (ConstantNode) where
							.getLeftOperand();
					System.out.print(leftValue.getValue());
				}

				System.out.print(" " + where.getOperator() + " ");

				if (where.getRightOperand() instanceof ColumnReference) {
					System.out.print(where.getRightOperand().getTableName()
							+ "." + where.getRightOperand().getColumnName());
				} else if (where.getRightOperand() instanceof ConstantNode) {
					ConstantNode rightValue = (ConstantNode) where
							.getRightOperand();
					System.out.print(rightValue.getValue());
				}

				System.out.println();
			}
			// if Logical Operator (AND, OR, ..) check recursively each operand
			return node;
		}

		@Override
		public boolean visitChildrenFirst(Visitable node) {
			return false;
		}

	}
}

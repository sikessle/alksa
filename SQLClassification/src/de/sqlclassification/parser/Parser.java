package de.sqlclassification.parser;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

public class Parser {

	public static void main(String[] args) {
		new Parser();
	}

	public Parser() {
		String query = "SELECT name, password FROM users u, emails LEFT OUTER JOIN plz p ON u.plz = plz.plz WHERE u.name = 'Simon' AND user.password='bar' AND emails.name = 'foo@me.com'";

		SQLParser sqlParser = new SQLParser();
		try {
			parseString(query, sqlParser);
		} catch (StandardException e) {
			System.err.println("Failed parsing");
			e.printStackTrace();
		}

	}

	private  void parseString(String query, SQLParser sqlParser)
			throws StandardException {
		StatementNode statement = sqlParser.parseStatement(query);
		System.out.println(query);
		System.out.println();
		//statement.treePrint();
		statement.accept(new FromStatementVisitor());
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
			if (node instanceof FromTable) {
				FromTable table = (FromTable) node;
				System.out.println(table.getOrigTableName() + " " + table.getCorrelationName());
			}
			return node;
		}

		@Override
		public boolean visitChildrenFirst(Visitable node) {
			return false;
		}

	}
}

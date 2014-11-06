package de.sqlclassification.parser;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;

public class Parser {
	
	public static void main(String[] args) {
		String query = "SELECT name, password FROM users WHERE name = 'Simon'";
		
		SQLParser sqlParser = new SQLParser();
		try {
			StatementNode parseStatement = sqlParser.parseStatement(query);
			parseStatement.treePrint();
		} catch (StandardException e) {
			e.printStackTrace();
		}
	}
}

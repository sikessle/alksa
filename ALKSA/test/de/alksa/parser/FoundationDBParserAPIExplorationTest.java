package de.alksa.parser;

import org.junit.Test;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;

public class FoundationDBParserAPIExplorationTest {

	@Test
	public void parseExample() {
		String query = "SELECT c1 FROM x WHERE a AND b AND NOT c";

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
	}
}

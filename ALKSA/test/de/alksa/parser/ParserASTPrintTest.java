package de.alksa.parser;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;

@RunWith(Parameterized.class)
public class ParserASTPrintTest {

	private String query;
	private static final String DELIMITER = "++++++++++++++++++++++++++++++++++++++++++++++++++++";

	public ParserASTPrintTest(String query) {
		this.query = query;
	}

	@Parameters
	public static Collection<Object[]> generateData() {
		return Arrays
				.asList(new Object[][] {
						{ "SELECT ABS(x), AVG(y) FROM y WHERE a AND b OR NOT c OR col = 'b'" },
						{ "SELECT x FROM b ORDER BY x ASC" },
						{ "SELECT (SELECT x FROM b) AS sub FROM tab" },
						{ "SELECT a FROM b UNION SELECT c FROM d" },
						{ "SELECT a FROM (SELECT x FROM b) subAlias" },
						{ "SELECT a AS d FROM b WHERE a = (SELECT x FROM b)" } });
	}

	@Test
	public void parseExample() {
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
		statement.treePrint();
		System.out.println(DELIMITER);
	}
}

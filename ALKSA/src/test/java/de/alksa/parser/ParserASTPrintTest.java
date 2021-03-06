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
						{ "SELECT a AS d FROM b WHERE a = (SELECT x FROM b)" },
						{ "SELECT a FROM tab1, tab2" },
						{ "SELECT t2.rate FROM x as t1 LEFT OUTER JOIN (SELECT rate FROM rates) as t2 ON t1.id=t2.id" },
						{ "SELECT COUNT(*) FROM wp_bwg_image WHERE published=1  AND gallery_id='1'" },
						{ "SELECT * FROM wp_bwg_image WHERE filename LIKE '%%'" } });
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

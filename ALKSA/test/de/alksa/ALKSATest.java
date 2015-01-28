package de.alksa;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ALKSATest {

	private ALKSA alksa;
	private String storagePath;

	@Before
	public void setUp() throws Exception {
		storagePath = "/tmp/alksa-test-complete";
		new File(storagePath).delete();

		alksa = new ALKSA(storagePath);
	}

	@After
	public void tearDown() throws Exception {
		new File(storagePath).delete();
	}

	@Test(expected = ALKSAInvalidQueryException.class)
	public void testInvalidSQL() throws ALKSAInvalidQueryException {
		alksa.accept("sfddsfds", "local", "test");
	}

	@Test
	public void testLoggingAndAcceptanceSQL() {
		String learnedQuery = "SELECT * FROM tab";
		alksa.setLearning(true);
		assertTrue(alksa.isLearning());
		assertTrue(exceptionSafeAccept(learnedQuery, "local", "test"));
		// learning triggers no loig
		assertEquals(0, alksa.getLogEntries().size());

		alksa.setLearning(false);
		try {
			assertFalse(alksa.accept("sfddsfds", "local", "test"));
		} catch (ALKSAInvalidQueryException e) {
			// exception is expected
		}
		// illegal statements should trigger no log
		assertEquals(0, alksa.getLogEntries().size());

		assertFalse(exceptionSafeAccept(
				"SELECT * FROM tab LEFT OUTER JOIN tab2 ON c1=c2", "local",
				"test"));
		assertEquals(1, alksa.getLogEntries().size());

		assertTrue(exceptionSafeAccept(learnedQuery, "local", "test"));
	}

	private boolean exceptionSafeAccept(String sql, String db, String user) {
		try {
			return alksa.accept(sql, db, user);
		} catch (ALKSAInvalidQueryException e) {
			fail("exception should not occur");
			return false;
		}
	}

}

package de.alksa;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ALKSATest {

	private ALKSA alksa;

	@Before
	public void setUp() throws Exception {
		String storagePath = "/tmp/alksa-test-complete";
		new File(storagePath).delete();

		alksa = new ALKSA(storagePath);
	}

	@Test
	public void testDelegateMethods() {
		String learnedQuery = "SELECT * FROM tab";
		alksa.setLearning(true);
		assertTrue(alksa.isLearning());
		assertTrue(alksa.accept(learnedQuery, "local", "test"));

		alksa.setLearning(false);
		assertFalse(alksa.accept("sfddsfds", "local", "test"));
		// illegal statements should trigger no log
		assertEquals(0, alksa.getLogEntries().size());

		assertFalse(alksa.accept(
				"SELECT * FROM tab LEFT OUTER JOIN tab2 ON c1=c2", "local",
				"test"));
		assertEquals(1, alksa.getLogEntries().size());

		assertTrue(alksa.accept(learnedQuery, "local", "test"));
	}

}

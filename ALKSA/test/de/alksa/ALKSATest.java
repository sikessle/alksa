package de.alksa;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ALKSATest {

	private ALKSA alksa;

	@Before
	public void setUp() throws Exception {
		String storagePath = "/tmp/alksa-test-complete";
		new File(storagePath).delete();

		alksa = new ALKSA(storagePath);
	}

	// TODO remove Ignore, when final testing starts
	@Ignore
	public void testDelegateMethods() {
		String learnedQuery = "SELECT * FROM tab";
		alksa.setLearning(true);
		assertTrue(alksa.isLearning());
		assertTrue(alksa.accept(learnedQuery, "local", "test"));

		alksa.setLearning(false);
		assertFalse(alksa.accept("sfddsfds", "local", "test"));
		assertTrue(alksa.accept(learnedQuery, "local", "test"));
	}

}

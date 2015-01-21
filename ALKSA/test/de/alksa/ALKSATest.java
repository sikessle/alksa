package de.alksa;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
		assertTrue(alksa.accept("SELECT * FROM tab", "local", "test"));
	}

}

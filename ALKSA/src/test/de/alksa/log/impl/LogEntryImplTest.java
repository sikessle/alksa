package de.alksa.log.impl;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;

public class LogEntryImplTest {

	@Test
	public void testGettersNonNull() {
		String query = "SELECT x FROM y;";
		String database = "testDatabase";
		String databaseUser = "root";
		String violation = "Columnlist violated";
		Instant timestamp = Instant.now();
		LogEntryImpl attack = new LogEntryImpl(query, database,
				databaseUser, violation, timestamp);

		assertEquals(query, attack.getQuery());
		assertEquals(database, attack.getDatabase());
		assertEquals(databaseUser, attack.getDatabaseUser());
		assertEquals(violation, attack.getViolation());
		assertEquals(timestamp, attack.getTimestamp());
	}

	@Test(expected = NullPointerException.class)
	public void testGettersNull() {
		new LogEntryImpl(null, null, null, null, null);
	}

}

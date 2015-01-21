package de.alksa.log.impl;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;

public class AttackLogEntryTest {

	@Test
	public void testGettersNonNull() {
		String query = "SELECT x FROM y;";
		String database = "testDatabase";
		String databaseUser = "root";
		String violation = "Columnlist violated";
		AttackLogEntry attack = new AttackLogEntry(query, database,
				databaseUser, violation, Instant.now());

		assertEquals(query, attack.getQuery());
		assertEquals(database, attack.getDatabase());
		assertEquals(databaseUser, attack.getDatabaseUser());
		assertEquals(violation, attack.getViolation());
	}

	@Test(expected = NullPointerException.class)
	public void testGettersNull() {
		new AttackLogEntry(null, null, null, null, null);
	}

}

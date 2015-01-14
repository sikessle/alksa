package de.alksa.log.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.alksa.log.impl.AttackLogEntry;

public class AttackLogEntryTest {

	@Test
	public void testGettersNonNull() {
		String query = "SELECT x FROM y;";
		String database = "testDatabase";
		String databaseUser = "root";
		String violation = "Columnlist violated";
		AttackLogEntry attack = new AttackLogEntry(query, database,
				databaseUser, violation);

		assertEquals(query, attack.getQuery());
		assertEquals(database, attack.getDatabase());
		assertEquals(databaseUser, attack.getDatabaseUser());
		assertEquals(violation, attack.getViolation());
	}

	@Test
	public void testGettersNull() {
		AttackLogEntry attack = new AttackLogEntry(null, null, null, null);

		assertEquals("", attack.getQuery());
		assertEquals("", attack.getDatabase());
		assertEquals("", attack.getDatabaseUser());
		assertEquals("", attack.getViolation());
	}

}

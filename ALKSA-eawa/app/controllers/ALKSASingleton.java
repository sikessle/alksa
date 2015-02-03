package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import play.db.DB;
import de.alksa.ALKSA;

public final class ALKSASingleton {

	private final static String PATH = "/tmp/eawa";
	private static ALKSA instance;
	private static Connection connection;

	private ALKSASingleton() {
		new File(PATH).delete();

		DataSource ds = DB.getDataSource();
		try {
			connection = ds.getConnection("sa", "");
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static ALKSA getInstance() {
		if (instance == null) {
			instance = new ALKSA(PATH);
		}
		return instance;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void closeDatabase() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

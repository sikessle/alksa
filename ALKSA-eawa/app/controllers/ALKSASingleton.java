package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import play.db.DB;
import de.alksa.ALKSA;

public final class ALKSASingleton {

	private final static String PATH = "/tmp/eawa";
	private static ALKSASingleton instance;
	private Connection connection;
	private ALKSA alksa;

	private ALKSASingleton() {
		new File(PATH).delete();

		alksa = new ALKSA(PATH);

		DataSource ds = DB.getDataSource();
		try {
			connection = ds.getConnection("sa", "");
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static ALKSASingleton getInstance() {
		if (instance == null) {
			instance = new ALKSASingleton();
		}
		return instance;
	}

	public ALKSA getALKSA() {
		return alksa;
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeDatabase() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

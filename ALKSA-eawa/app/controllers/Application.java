package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.alksa.ALKSA;
import de.alksa.ALKSAInvalidQueryException;

public class Application extends Controller {

	private static ObjectMapper mapper = new ObjectMapper();

	private static ALKSA alksa;
	private static boolean enableALKSA;

	private static List<String> learnedQueries;
	private static List<String> productiveQueries;

	private static ArrayNode columnsFromQuery;
	private static ArrayNode resultFromQuery;

	static {
		init();
	}

	private static void init() {
		alksa = ALKSASingleton.getInstance().getALKSA();
		enableALKSA = false;
		learnedQueries = new LinkedList<>();
		productiveQueries = new LinkedList<>();
	}

	public static Result reset() {
		ALKSASingleton.getInstance().reset();
		init();
		return ok();
	}

	public static Result index() {
		boolean isLearning = alksa == null ? false : alksa.isLearning();
		return ok(index.render(isLearning, enableALKSA));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result runQuery() {
		JsonNode request = request().body().asJson();

		if (request == null) {
			return badRequest("expecting application/json");
		}

		String columns = request.get("columns").asText();
		String where = request.get("where").asText();

		ObjectNode result = createResultFromQuery(buildQuery(columns, where));

		return ok(result);
	}

	private static String buildQuery(String columns, String where) {
		String query = "SELECT " + ("".equals(columns) ? "City.Name" : columns);
		query += " FROM City";
		query += " LEFT OUTER JOIN Country ON City.CountryCode = Country.Code";
		query += " LEFT OUTER JOIN CountryLanguage ON CountryLanguage.CountryCode = Country.Code";

		if (!"".equals(where)) {
			query += " WHERE " + where;
		}

		query += " LIMIT 500";

		return query;
	}

	private static ObjectNode createResultFromQuery(String query) {
		ObjectNode resultNode = mapper.createObjectNode();

		boolean accepted = processQuery(query);

		resultNode.put("learnedQueries", getListAsJSONArray(learnedQueries));
		resultNode.put("productiveQueries",
				getListAsJSONArray(productiveQueries));
		resultNode.put("accepted", accepted);
		resultNode.put("resultHead", columnsFromQuery);
		resultNode.put("resultBody", resultFromQuery);

		return resultNode;
	}

	private static boolean processQuery(String query) {
		boolean accepted = true;

		if (enableALKSA) {
			accepted = processALKSA(query);
		}

		if (accepted) {
			try {
				setResultFromDatabase(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return accepted;
	}

	private static void setResultFromDatabase(String query) throws SQLException {
		columnsFromQuery = mapper.createArrayNode();
		resultFromQuery = mapper.createArrayNode();

		Connection conn = ALKSASingleton.getInstance().getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		ResultSetMetaData meta = rs.getMetaData();

		for (int i = 1; i <= meta.getColumnCount(); i++) {
			columnsFromQuery.add(meta.getColumnLabel(i));
		}

		while (rs.next()) {
			ArrayNode row = mapper.createArrayNode();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				row.add(rs.getString(i));
			}
			resultFromQuery.add(row);
		}
	}

	private static boolean processALKSA(String query) {
		boolean queryError = false;
		boolean queryAccepted = false;

		try {
			queryAccepted = alksa.accept(query, "eawa", "tester");
		} catch (ALKSAInvalidQueryException e) {
			queryError = true;
		}

		String prefix = queryError ? "ERROR: " : (queryAccepted ? "ACCEPTED: "
				: "REJECTED: ");

		if (alksa.isLearning()) {
			learnedQueries.add(prefix + query);
		} else {
			productiveQueries.add(prefix + query);
		}

		return !queryError && queryAccepted;
	}

	private static ArrayNode getListAsJSONArray(List<String> queries) {
		ArrayNode result = mapper.createArrayNode();

		ListIterator<String> iterator = queries.listIterator(queries.size());

		while (iterator.hasPrevious()) {
			result.add(iterator.previous());
		}

		return result;
	}

	public static Result setLearning(boolean learning) {
		alksa.setLearning(learning);
		return ok();
	}

	public static Result enableALKSA(boolean enable) {
		enableALKSA = enable;
		return ok();
	}
}

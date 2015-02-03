package controllers;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import play.Logger;
import de.alksa.ALKSA;
import de.alksa.ALKSAInvalidQueryException;
import de.alksa.querystorage.Query;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import play.mvc.Http.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import views.html.index;

public class Application extends Controller {

	private static ObjectMapper mapper = new ObjectMapper();

	private final static String PATH = "/tmp/eawa";

	private static ALKSA alksa;
	private static boolean enableALKSA;

	private static Set<String> learnedQueries;
	private static Set<String> productiveQueries;

	{
		reset();
	}

	public static Result index() {
		return ok(index.render());
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
		String query = "SELECT " + columns;
		query += " FROM City";
		query += " LEFT OUTER JOIN Country ON City.CountryCode = Country.Code";
		query += " LEFT OUTER JOIN CountryLanguage ON CountryLanguage.CountryCode = Country.Code";
		query += " WHERE " + where;

		return query;
	}

	private static ObjectNode createResultFromQuery(String query) {
		ObjectNode result = mapper.createObjectNode();
		ArrayNode resultHead = mapper.createArrayNode();
		ArrayNode resultBody = mapper.createArrayNode();

		// TODO
		boolean accepted = processQuery(query);

		result.put("learnedQueries", getQueriesAsJSON(learnedQueries));
		result.put("productiveQueries", getQueriesAsJSON(productiveQueries));
		result.put("accepted", accepted);
		result.put("resultHead", resultHead);
		result.put("resultBody", resultBody);
		return result;
	}

	private static boolean processQuery(String query) {
		if (enableALKSA) {
			return processALKSA(query);
		}

		return true;
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

	private static ArrayNode getQueriesAsJSON(Set<String> queries) {
		ArrayNode result = mapper.createArrayNode();

		for (String query : queries) {
			result.add(query);
		}

		return result;
	}

	public static Result reset() {
		if (alksa != null) {
			alksa.shutdown();
		}
		new File(PATH).delete();
		alksa = new ALKSA(PATH);
		enableALKSA = false;
		learnedQueries = new HashSet<>();
		productiveQueries = new HashSet<>();
		return ok();
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

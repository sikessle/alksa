package controllers;

import play.Logger;
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
	private static ALKSA alksa;

	{

	}

	private static boolean enableALKSA = false;

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
		ArrayNode learnedQueries = mapper.createArrayNode();
		ArrayNode productiveQueries = mapper.createArrayNode();
		ArrayNode resultHead = mapper.createArrayNode();
		ArrayNode resultBody = mapper.createArrayNode();

		// TODO

		result.put("accepted", false);
		result.put("learnedQueries", learnedQueries);
		result.put("productiveQueries", productiveQueries);
		result.put("resultHead", resultHead);
		result.put("resultBody", resultBody);
		return result;
	}

	public static Result reset() {
		// TODO
		return ok();
	}

	public static Result setLearning(boolean learning) {
		// TODO
		return ok();
	}

	public static Result enableALKSA(boolean enable) {
		enableALKSA = enable;
		return ok();
	}
}

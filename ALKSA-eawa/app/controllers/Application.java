package controllers;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import views.html.index;

public class Application extends Controller {

	private static ObjectMapper mapper = new ObjectMapper();

	public static Result index() {
		return ok(index.render());
	}

	@BodyParser.Of(play.mvc.BodyParser.Json.class)
	public static Result runQuery() {
		/**
		 *
		 { accepted: true|false, learnedQueries: [], productiveQueries: [],
		 * resultHead: [], resultBody: [] }
		 */

		JsonNode request = request().body().asJson();
		ObjectNode result = createResultFromQuery();

		return ok(result);
	}

	private static ObjectNode createResultFromQuery() {
		ObjectNode result = mapper.createObjectNode();
		ArrayNode learnedQueries = mapper.createArrayNode();
		ArrayNode productiveQueries = mapper.createArrayNode();
		ArrayNode resultHead = mapper.createArrayNode();
		ArrayNode resultBody = mapper.createArrayNode();

		result.put("accepted", false);
		result.put("learnedQueries", learnedQueries);
		result.put("productiveQueries", productiveQueries);
		result.put("resultHead", resultHead);
		result.put("resultBody", resultBody);
		return result;
	}

	public static Result reset() {
		return ok();
	}

	public static Result setLearning(boolean learning) {
		return ok();
	}

	public static Result enableALKSA(boolean enable) {
		return ok();
	}
}

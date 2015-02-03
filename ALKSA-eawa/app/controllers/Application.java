package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result runQuery() {
		return ok(index.render());
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

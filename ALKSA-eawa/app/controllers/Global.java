package controllers;

import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

	@Override
	public void onStop(Application app) {
		ALKSASingleton.getInstance().shutdownAll();
	}

}

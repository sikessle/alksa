package controllers;

import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

	@Override
	public void onStop(Application app) {
		ALKSASingleton singleton = ALKSASingleton.getInstance();
		singleton.getALKSA().shutdown();
		singleton.closeDatabase();
	}

}

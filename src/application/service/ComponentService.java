package application.service;

import org.json.JSONObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ComponentService extends Service<JSONObject> {

	@Override
	protected Task<JSONObject> createTask() {
		return null;
	}

}

package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.javafx.collections.MappingChange.Map;

import application.controller.LoginController;
import application.service.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

public class Main extends Application {
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/betfair/fxml/Main.fxml"));
			BorderPane root = (BorderPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean isLogged = false;
	private LoginController loginController;

	private boolean login() {
		// Sign In dialog
		Dialog<HashMap<String, String>> dialog = new Dialog();
		dialog.initOwner(primaryStage);
		dialog.setTitle("Login to BetFair");
		dialog.setHeaderText("Login To BetFair Account");
		ButtonType signInButtonType = new ButtonType("Sign In", ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, signInButtonType);
		GridPane gridPane = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/betfair/fxml/login.fxml"));
			gridPane = (GridPane) fxmlLoader.load();
			loginController = fxmlLoader.getController();
			System.out.println(loginController);
			dialog.getDialogPane().setContent(gridPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(dialog.getDialogPane().getChildren());
		dialog.setResultConverter(new Callback<ButtonType, HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call(ButtonType param) {
				HashMap<String, String> map = new HashMap();
				if (param.getButtonData() == ButtonData.CANCEL_CLOSE) {
					primaryStage.close();
				} else {
					List<NameValuePair> nameValuePair = new ArrayList<>();
					loginController.setStatusConnecting();
					HashMap<String, String> data = loginController.getData();
					nameValuePair.add(new BasicNameValuePair("username", data.get("username")));
					nameValuePair.add(new BasicNameValuePair("password", data.get("password")));
					JSONObject json = new Connection().request("https://identitysso.betfair.com/api/login",
							nameValuePair);
					try {
						if (json.getString("status").equals("SUCCESS")) {
							isLogged = true;
							map.put("token", json.getString("token"));
							map.put("product", json.getString("product"));
						} else {
							loginController.setStatusError(json.getString("status") + ": " + json.getString("error"));
							isLogged = false;
						}
					} catch (JSONException e) {
						isLogged = false;
						e.printStackTrace();
					}
					// isLogged = true;
					// return map;
				}
				return map;
			}
		});
		dialog.setOnCloseRequest(event -> {
			System.out.println(event);
			if (!isLogged)
				event.consume();
		});
		dialog.showAndWait().ifPresent(pair -> {
			// TODO
		});

		// End Sign in dialog

		return isLogged;
	}
}

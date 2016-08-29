package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.mybetfair.model.User;
import com.sun.glass.ui.Application;

import application.service.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable{
	private User user;
	@FXML private Button logoutBtn;
	@FXML private Text usernameLabel;
	@FXML Stage primaryStage;
	
	

	public void setUser(User user) {
		this.user = user;
		usernameLabel.textProperty().bind(user.getUsernameProperty());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Application.GetApplication().cl
		login();
	}
	
	@FXML private void logout(){
		System.out.println("Log out");
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
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
		ButtonType cancelButtonType = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);
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
					JSONObject json = new Connection().request("https://identitysso.betfair.com/api/login", nameValuePair);
					try {
						if (json.getString("status").equals("SUCCESS")) {
							isLogged = true;
 							map.put("token", json.getString("token"));
							map.put("product", json.getString("product"));
							map.put("username", data.get("username"));
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
			if(pair != null){
				user = User.getSingleton();
				user.setUsername(pair.get("username"));
				user.setToken(pair.get("token"));
				user.setProduct(pair.get("product"));
			}
			System.out.println("Data: ");
			System.out.println(pair);
		});

		// End Sign in dialog

		return isLogged;
	}

}

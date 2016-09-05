package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mybetfair.model.User;

import application.service.Connection;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable {
	private User user = User.getSingleton();
	@FXML
	private Button logoutBtn;
	@FXML
	private Text usernameLabel;
	@FXML
	private Stage primaryStage;
	@FXML
	private TreeView<JSONObject> navigationTreeView;
	@FXML
	ProgressIndicator navigationTreeViewProgressIndicator;
	// private TreeItem<JSONObject> navigationTreeViewRootItem = new
	// TreeItem<JSONObject>();
	private SimpleBooleanProperty isSignedIn = new SimpleBooleanProperty(false);

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usernameLabel.textProperty().bind(user.getUsernameProperty());
		// navigationTreeView.setRoot(navigationTreeViewRootItem);
		navigationTreeView.setShowRoot(false);
		isSignedIn.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue)
				loadData();
		});

		// Format Navigation Tree View Cell
		navigationTreeView.setCellFactory(param -> new TreeCell<JSONObject>() {

			@Override
			protected void updateItem(JSONObject item, boolean empty) {
				if (!empty)
					try {
						setText(item.getString("name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				super.updateItem(item, empty);
			}

		});
		// End Format Navigation Tree View Cell
		navigationTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<JSONObject>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<JSONObject>> observable,
					TreeItem<JSONObject> oldValue, TreeItem<JSONObject> newValue) {
				System.out.println(newValue.getValue());
			}
		});
	}

	@FXML
	private void logout() {
		System.out.println("Log out");
	}

	private void loadData() {
		new Thread(new Task<JSONObject>() {
			@Override
			protected JSONObject call() throws Exception {
				navigationTreeViewProgressIndicator.setVisible(true);
				// navigationTreeViewProgressIndicator.setProgress(20.0);
				JSONObject jsonObject = new Connection()
						.requestWithToken("https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json");
				// navigationTreeViewProgressIndicator.setProgress(80);
				return jsonObject;
			}

			@Override
			protected void succeeded() {
				System.out.println("On Succeeded");
				JSONObject jsonObject = getValue();
				loadNavigationData(null, jsonObject);
				navigationTreeViewProgressIndicator.setVisible(false);
				super.succeeded();
			}
		}).start();
	}

	private void loadNavigationData(TreeItem<JSONObject> treeItem, JSONObject jsonObject) {
		try {
			if (!jsonObject.isNull("children")) {
				JSONArray jsonArray = jsonObject.getJSONArray("children");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					TreeItem<JSONObject> childTreeItem = new TreeItem<JSONObject>(json);

					if (treeItem != null) {
						treeItem.getChildren().add(childTreeItem);
					} else {
						navigationTreeView.setRoot(childTreeItem);
					}
					loadNavigationData(childTreeItem, json);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		if (user == null) {
			System.out.println("user object null");
			primaryStage.close();
		} else {
			System.out.println("user object isn't null");
		}
	}

	private boolean isLogged = false;
	private LoginController loginController;

	public boolean login() {
		// Sign In dialog
		Dialog<HashMap<String, String>> dialog = new Dialog<HashMap<String, String>>();
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
			dialog.getDialogPane().setContent(gridPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(dialog.getDialogPane().getChildren());
		dialog.setResultConverter(new Callback<ButtonType, HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call(ButtonType param) {
				HashMap<String, String> map = new HashMap<String, String>();
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
			if (!isLogged) {
				event.consume();
				// primaryStage.close();
			}
		});
		dialog.showAndWait().ifPresent(pair -> {
			if (pair != null && !pair.isEmpty()) {
				user.setUsername(pair.get("username"));
				user.setToken(pair.get("token"));
				user.setProduct(pair.get("product"));
			}
			System.out.println("Data: ");
			System.out.println(pair);
		});

		// End Sign in dialog
		isSignedIn.set(isLogged);
		return isLogged;
	}

}

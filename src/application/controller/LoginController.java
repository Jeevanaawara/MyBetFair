package application.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController implements Initializable{

	@FXML private TextField username_textField;
	@FXML private TextField password_textField;
	@FXML private Text status_text;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		TODO
	}

	public HashMap<String, String> getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", username_textField.getText().trim());
		map.put("password", password_textField.getText().trim());
		return map;
	}
	
	public void setStatusConnecting(){
		System.out.println("Connecting");
		status_text.setText("Connecting");
		status_text.setStyle("-fx-fill:#0f0");
	}

	public void setStatusError(String string){
		status_text.setText(string);
		status_text.setStyle("-fx-fill:#f00");
	}
	
	public void resetStatus(){
		status_text.setText("");
	}
	
}

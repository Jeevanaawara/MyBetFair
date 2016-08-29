package application.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController implements Initializable, EventHandler<Event>, ChangeListener<Boolean>{

	@FXML private TextField username_textField;
	@FXML private TextField password_textField;
	@FXML private Text status_text;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username_textField.focusedProperty().addListener(this);
		password_textField.focusedProperty().addListener(this);
		username_textField.setOnInputMethodTextChanged(this);
		password_textField.setOnInputMethodTextChanged(this);

	}

	public HashMap<String, String> getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", username_textField.getText().trim());
		map.put("password", password_textField.getText().trim());
		return map;
	}
	
	public void setStatusConnecting(){
		System.out.println("Connecting");
		status_text.setText("CONNECTING...");
		status_text.setStyle("-fx-fill:#0f0");
	}

	public void setStatusError(String string){
		status_text.setText(string);
		status_text.setStyle("-fx-fill:#f00");
	}
	
	public void resetStatus(){
		status_text.setText("");
	}

	@Override
	public void handle(Event event) {
		System.out.println("Input Method Change");
		System.out.println(event.getSource() + ": "+ event.getEventType());
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if(newValue){
			if(!status_text.getText().isEmpty())
				status_text.setText("");			
		}
		
	}
	
}

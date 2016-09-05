package application.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mybetfair.model.User;

public class Connection {

	private static final String BETFAIR_HOST_URL = "https://identitysso.betfair.com/api/login";
	private static final String REQUEST_METHOD = "POST";
	private static final String API_KEY = "7qgehESE204U2m8F";

	public JSONObject requestWithToken(String stringURL) {
		JSONObject json = null;
		HttpGet httpGet = new HttpGet(stringURL);
		try {
			httpGet.setHeader("Connection", "keep-alive");
			httpGet.setHeader("X-Application", API_KEY);
			httpGet.setHeader("X-Authentication",User.getSingleton().getToken());
//			httpGet.setHeader("X-Authentication","QzWI4CfOJwagv0XkIiHlL5WgNc7R5n3745lmvBTu2EU=");
			httpGet.setHeader("Accept-Encoding","gzip,deflate");
			httpGet.setHeader("accept", "application/json");
			httpGet.setHeader("content-type", "application/x-www-form-urlencoded");
//			httpGet.setEntity(new UrlEncodedFormEntity(nameValuePair));
//			System.out.println(nameValuePair);
			System.out.println(httpGet);
			System.out.println("Headers:");
			for (Header header : httpGet.getAllHeaders()) {
				System.out.println(header);
			}
			json = sendHttpRequest(httpGet);
		} finally {

		}
		return json;
	}


	public JSONObject request(String stringURL, List<NameValuePair> nameValuePair) {
		JSONObject json = null;
		HttpPost httpPost = new HttpPost(stringURL);
		try {
			httpPost.setHeader("accept", "application/json");
			httpPost.setHeader("X-Application", API_KEY);
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			System.out.println(nameValuePair);
			System.out.println(httpPost);
			System.out.println("Headers:");
			for (Header header : httpPost.getAllHeaders()) {
				System.out.println(header);
			}
			json = sendHttpRequest(httpPost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return json;
	}

	private JSONObject sendHttpRequest(HttpPost httpPost) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpPost);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			return new JSONObject(getResponseInString(bufferedReader));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject sendHttpRequest(HttpGet httpGet) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			
			return new JSONObject(getResponseInString(bufferedReader));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getResponseInString(BufferedReader bufferedReader) {
		String string = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((string = bufferedReader.readLine()) != null) {
				stringBuilder.append(string);
			}
//			System.out.println("Response: " + stringBuilder.toString());
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
//		 List<NameValuePair> nameValuePair = new ArrayList<>();
//		 nameValuePair.add(new BasicNameValuePair("username","jeevanaawara33@gmail.com"));
//		 nameValuePair.add(new BasicNameValuePair("password", "iamArtist33"));
//		 new Connection().request(BETFAIR_HOST_URL, nameValuePair);
//		new Connection().requestWithToken("https://api.betfair.com/exchange/betting/rest/v1/en/navigation/menu.json");
		// System.out.println(jsonObject);
	}

}

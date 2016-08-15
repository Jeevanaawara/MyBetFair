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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Connection {

	private static final String BETFAIR_HOST_URL = "https://identitysso.betfair.com/api/login";
	private static final String REQUEST_METHOD = "POST";
	private static final String API_KEY = "7qgehESE204U2m8F";
	private HttpClient httpClient;
	private HttpPost httpPost;

	public JSONObject request(String stringURL, List<NameValuePair> nameValuePair) {
		JSONObject json = null;
		httpClient = HttpClientBuilder.create().build();
		httpPost = new HttpPost(stringURL);
		try {
			httpPost.setHeader("accept", "application/json");
			httpPost.setHeader("X-Application", API_KEY);
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			try {
				System.out.println(nameValuePair);
				System.out.println(httpPost);
				System.out.println("Headers:");
				for(Header header : httpPost.getAllHeaders()){
					System.out.println(header);
				}
				HttpResponse httpResponse = httpClient.execute(httpPost);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				String string = null;
				StringBuilder stringBuilder = new StringBuilder();
				while ((string = bufferedReader.readLine()) != null) {
					stringBuilder.append(string);
				}
				System.out.println("Response: "+stringBuilder.toString());
				json = new JSONObject(stringBuilder.toString());
				bufferedReader.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
		return json;
	}

	public static void main(String[] args) {
//		List<NameValuePair> nameValuePair = new ArrayList<>();
//		nameValuePair.add(new BasicNameValuePair("username", "jeevanaawara33@gmail.com"));
//		nameValuePair.add(new BasicNameValuePair("password", "iamArtist33"));
//		new Connection().request(BETFAIR_HOST_URL, nameValuePair);
////		System.out.println(jsonObject);
	}

}

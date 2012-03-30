package p2v.web.signin;

import java.util.Scanner;

import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
	
public class SignInTest {

	private static final String apiKey = "352328731485414";
	private static final String secretKey = "b06de1cba9f5220a427a4bff384199c2";
	private static final String redirectUri =
			"https://www.facebook.com/connect/login_success.html";

	 @Test public void facebook() {
		 FacebookConnectionFactory connectionFactory = 
				 new FacebookConnectionFactory(apiKey, secretKey);
		 OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
		 OAuth2Parameters params = new OAuth2Parameters();
		 params.setRedirectUri(
				 "https://www.facebook.com/dialog/oauth?client_id=" + 
						 apiKey + "&redirect_uri=" + redirectUri);
		 params.setScope("user_about_me,user_birthday,user_likes,user_status,publish_stream");
		 String authorizeUrl = 
				 oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);

		 java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		 if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
			 System.out.println("No browsing allowed...");
			 System.exit(1);
		 }
		 try {
			 java.net.URI uri = new java.net.URI(authorizeUrl);
			 desktop.browse(uri);
		 } catch (Exception e) {
			 System.err.println(e.getMessage());
		 }
		 System.out.println("Token : ");
		 Scanner scanner = new Scanner(System.in);
		 String input = scanner.next();
		 input = input.replace("https://www.facebook.com/connect/login_success.html?code=", "");
		 System.out.println(input);
		 AccessGrant accessGrant = oauthOperations.exchangeForAccess(input, redirectUri, null);
		 Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
		 Facebook facebook = connection.getApi();
		 facebook.feedOperations().updateStatus("Hello World");
	 }

}

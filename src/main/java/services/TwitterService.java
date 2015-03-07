package services;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import commands.DB;

@Path("/twitter")
public class TwitterService {
	String consumerKey = "xejdBtlktzwiQuzbcrlPqd7IT";
	String consumerSecret = "BmVeTI4JKkP3id5bxtWo9NpD3I53r7VAHoXOVfJlj9V5n0oGjQ";

	@GET
	@Path("/request")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAuthentication(@Context HttpServletResponse response,
			@Context HttpServletRequest request, @QueryParam("user") String user) {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
		} catch (Exception e) {
			System.out.println("The OAuthConsumer has likely already been set");
		}

		try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
			
			request.getSession().setAttribute("requestToken", requestToken);
			request.getSession().setAttribute("username", user);
			response.sendRedirect(requestToken.getAuthorizationURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@GET
	@Path("/success")
	@Produces(MediaType.APPLICATION_JSON)
	public String success(@QueryParam("oauth_token") String otoken,
			@QueryParam("oauth_verifier") String oauth_verifier,
			@Context HttpServletRequest request) {
		Twitter twitter = new TwitterFactory().getInstance();
		Status tweetStatus = null;
		AccessToken accessToken = null;
		RequestToken requestToken = null;
		String user = null;
		try {
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
		} catch (Exception e) {
			System.out
					.println("The OAuthConsumer has likely already been set, ignore");
		}
		try {
			requestToken = (RequestToken) request.getSession().getAttribute(
					"requestToken");
			if (requestToken == null)
				throw new Exception();
		} catch (Exception e1) {
			return "Could not find valid Request Token";
		}
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken,
					oauth_verifier);
		} catch (TwitterException e1) {
			return "Could not find valid token";
		}
		try {
			DB db = new DB();
			user = (String) request.getSession().getAttribute("username");
			db.saveOAuthToken(accessToken.getToken(), user, "twitter",
					accessToken.getTokenSecret());
		} catch (Exception e) {
			System.out.println("Could not store access token to DB");
		}
		
		try {
			tweetStatus = twitter.updateStatus("Test From Heroku"
					+ System.currentTimeMillis());
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		if (tweetStatus != null)
			return "Check your Twitter, your tweet has been posted:"
					+ tweetStatus.getText();
		else
			return "BOO! didn't work";
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public String success(@QueryParam("user") String user) {
		Twitter twitter = new TwitterFactory().getInstance();
		Status tweetStatus = null;
		AccessToken accessToken = null;
		try {
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
		} catch (Exception e) {
			System.out.println("The OAuthConsumer has likely already been set");
		}
		try {
			DB db = new DB();
			accessToken = db.getOAuthToken(user, "twitter");
			twitter.setOAuthAccessToken(accessToken);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			tweetStatus = twitter.updateStatus("When you do what you fear most, then you can do anything."+"\n"+"― Stephen Richards");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		if (tweetStatus != null)
			return "Check your Twitter, your tweet has been posted:";
		else
			return "BOO! didn't work";
	}
	
	@GET
	@Path("/postmotiv")
	@Produces(MediaType.APPLICATION_JSON)
	public String msgpost() 
	{
		Twitter twitter = new TwitterFactory().getInstance();
		Status tweetStatus = null;
		AccessToken accessToken = null;
		ArrayList<String> users = new ArrayList<>();
		DB db = new DB();
		users = db.getusers();
		
		try 
		{
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
		} 
		catch (Exception e) 
		{
			System.out.println("The OAuthConsumer has likely already been set");
		}
		
		for(String uname : users)
		{
			try 
			{
				accessToken = db.getOAuthToken(uname, "twitter");
				twitter.setOAuthAccessToken(accessToken);
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			try 
			{
				tweetStatus = twitter.updateStatus("When you do what you fear most, then you can do anything."+"\n"+"― Stephen Richards");
			} 
			catch (TwitterException e) 
			{
				e.printStackTrace();
			}
		}
		
		if (tweetStatus != null)
			return "Check your Twitter, your tweet has been posted:";
		else
			return "BOO! didn't work";
	
		
	}
	
	@GET
	@Path("/postlove")
	@Produces(MediaType.APPLICATION_JSON)
	public String msgpostmotiv() 
	{
		
		Twitter twitter = new TwitterFactory().getInstance();
		Status tweetStatus = null;
		AccessToken accessToken = null;
		ArrayList<String> users = new ArrayList<>();
		ArrayList<String> quotes = new ArrayList<>();
		DB db = new DB();
		users = db.getusers();
		quotes = db.loveQuotes();
		Random rand = new Random();
		int r = rand.nextInt(quotes.size());
		
		
		try 
		{
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
		} 
		catch (Exception e) 
		{
			System.out.println("The OAuthConsumer has likely already been set");
		}
		
		for(String uname : users)
		{
			try 
			{
				accessToken = db.getOAuthToken(uname, "twitter");
				twitter.setOAuthAccessToken(accessToken);
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			try 
			{
							
				{
				tweetStatus = twitter.updateStatus(quotes.get(r));
			
				} 
			}
			catch (TwitterException e) 
			{
				e.printStackTrace();
			}
		}
		
		if (tweetStatus != null)
			return "Check your Twitter, your tweet has been posted:";
		else
			return "BOO! didn't work";
	
		
	}
}

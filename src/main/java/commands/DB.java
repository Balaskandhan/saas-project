package commands;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import twitter4j.auth.AccessToken;
import connectionprovider.ConnectionProvider;

public class DB {
	public static void main(String args[])
	{
		DB db = new DB();
		db.getMotivQuoteUsers();
	}

	public AccessToken getOAuthToken(String user, String application) {
		AccessToken accessToken = null;
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT oauth, secret FROM TOKENS WHERE username = ? AND application=?");
			stmt.setString(1, user);
			stmt.setString(2, application);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				accessToken = new AccessToken(rs.getString("oauth"),
						rs.getString("secret"));

			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	

	public void saveOAuthToken(String otoken, String user, String app,
			String secret,String love_quote,String inspire_quote,String motiv_quote) {

		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO TOKENS(oauth, username, application, secret,love_quote,inspire_quote,motiv_quote)"
							+ " VALUES(?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, otoken);
			stmt.setString(2, user);
			stmt.setString(3, app);
			stmt.setString(4, secret);
			stmt.setString(5, love_quote);
			stmt.setString(6, inspire_quote);
			stmt.setString(7, motiv_quote);
			stmt.executeUpdate();
			} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean addUser(String user, String love_quote, String inspire_quote, String motiv_quote) {
		boolean result = false;
		if(isUsernameAvailable(user)) {
			try {
				Connection connection = ConnectionProvider.getConnection();
				PreparedStatement stmt = connection
					.prepareStatement("INSERT INTO tokens(username,love_quote,inspire_quote,motiv_quote ) VALUES(?, ?, ?, ?, ?)");
				stmt.setString(1, user);
				stmt.setString(2, love_quote);
				stmt.setString(3, inspire_quote);
				stmt.setString(4, motiv_quote);
				stmt.executeUpdate();
				result = true;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return result;
	}
	
	public boolean isUsernameAvailable(String username) {
		boolean result = true;
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tokens WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) 
				result = false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<String> getLoveQuoteUsers()
	{
		ArrayList<String> userslist = new ArrayList<String>();
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select username from tokens where love_quote like 'yes'");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				userslist.add(rs.getString("username"));
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(userslist.toString());
		return userslist;		
	}
	
	public ArrayList<String> getMotivQuoteUsers()
	{
		ArrayList<String> userslist = new ArrayList<String>();
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select username from tokens where motiv_quote like 'yes'");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				userslist.add(rs.getString("username"));
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(userslist.toString());
		return userslist;		
	}
	
	public ArrayList<String> getInspireQuoteUsers()
	{
		ArrayList<String> userslist = new ArrayList<String>();
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select username from tokens where Inspire_quote like 'yes'");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				userslist.add(rs.getString("username"));
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(userslist.toString());
		return userslist;		
	}
	
	public ArrayList<String> loveQuotes()
	{
		
		ArrayList<String> quoteslist = new ArrayList<String>();
		String quote;
		try {
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select quotes,author from love");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				quote = (rs.getString("quotes")) +"\n"+"-"+ (rs.getString("author"));
				quoteslist.add(quote);
			}
						
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		//System.out.println(quoteslist.toString());
		return quoteslist;		
		
	}
	
	public ArrayList<String> motivQuotes()
	{
		
		ArrayList<String> mquoteslist = new ArrayList<String>();
		String quote;
		try 
		{
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select quotes,author from motiv");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				quote = (rs.getString("quotes")) +"\n"+"-"+ (rs.getString("author"));
				mquoteslist.add(quote);
			}
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		System.out.println(mquoteslist.toString());
		return mquoteslist;		
		
	}
	
	public ArrayList<String> inspirationQuotes()
	{
		
		ArrayList<String> iquoteslist = new ArrayList<String>();
		String quote;
		try 
		{
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("select quotes,author from inspiration");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				quote = (rs.getString("quotes")) +"\n"+"-"+ (rs.getString("author"));
				iquoteslist.add(quote);
			}
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		System.out.println(iquoteslist.toString());
		return iquoteslist;		
		
	}
	
	public void rowcount() 
	{
		try
		{
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
				.prepareStatement("select count(*) from love");
			ResultSet rs = stmt.executeQuery();
			int count =0;
			while(rs.next())
				{
					count = rs.getInt(1);
					System.out.println("number of quotes "+count);
				}
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		try
		{
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement stmt = connection
				.prepareStatement("select count(*) from motiv");
			ResultSet rs = stmt.executeQuery();
			int count =0;
			while(rs.next())
				{
					count = rs.getInt(1);
					System.out.println("number of quotes "+count);
				}
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		
	}

}

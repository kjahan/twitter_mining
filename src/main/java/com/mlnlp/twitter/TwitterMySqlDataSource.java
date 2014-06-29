package com.mlnlp.twitter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mlnlp.utilities.TimeZone;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class TwitterMySqlDataSource implements TwitterDataSource {
	private MysqlDataSource dataSource = null;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private TweetDate tweetDate;
	
	public TwitterMySqlDataSource(String start, String end, TimeZone tz){
		setupMySqlDB();
		tweetDate = new TweetDate(start, end, tz);
	}
	
	private void setupMySqlDB(){
		dataSource = new MysqlDataSource();
		dataSource.setServerName(TweetsConstants.TWEET_MYSQL_SERVER_NAME); 
		dataSource.setUser(TweetsConstants.TWEET_MYSQL_USER_NAME); 
		dataSource.setPassword(TweetsConstants.TWEET_MYSQL_PSWD);
		dataSource.setDatabaseName(TweetsConstants.TWEET_MYSQL_DB_NAME);
		
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("getConnection() exception");
		} 
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("createStatement() exception");
		} 
	}
	
	public void cleanUP(){
		try {
			rs.close();
			stmt.close(); 
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlnlp.twitter.TwitterDataSource#totalTweetsNumber(java.lang.String, java.lang.String)
	 * find number of tweets in the given date range
	 * dates are in gmt
	 */

	public long totalTweetsNumber() {
		long count = 0;
		try {
			String query = new String("select count(*) from politics where created_at <= '" + tweetDate.end + "' and created_at >= '" + tweetDate.start + "';");
			//System.out.println(query);
			rs = stmt.executeQuery(query);
			//extract results
			while(rs.next()){
				count = rs.getInt("count(*)");
			}
			//cleanUP();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	public long totalTweetsNumber(String keyword) {
		long count = 0;
		try {
			String query = new String("select count(*) from politics where created_at <= '" + tweetDate.end + "' and created_at >= '" + tweetDate.start + "' and `text` COLLATE UTF8_GENERAL_CI LIKE '%" + keyword + "%';");
			//System.out.println(query);
			rs = stmt.executeQuery(query);
			//extract results
			while(rs.next()){
				count = rs.getInt("count(*)");
			}
			//cleanUP();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	public List<Tweet> randomSampleTweets(int sampleNo) {
		List<Tweet> tweets = new ArrayList<Tweet>();

		try {
			String query = new String("select id, timestamp, text, X(location) as X, Y(location) as Y, source, author from politics where created_at <= '" + tweetDate.end + "' and created_at >= '" + tweetDate.start + "' order by rand() limit " + sampleNo + ";");
			//System.out.println(query);
			rs = stmt.executeQuery(query);
			//extract results
			while(rs.next()){
				Long id = rs.getLong("id");
				//Date date_ = rs.getDate("created_at");
				long ts_ = rs.getLong("timestamp");
				String src_ = rs.getString("source");
				String auth_ = rs.getString("author");
				double lat_ = rs.getDouble("X");
				double lon_ = rs.getDouble("Y");
				String text_ = rs.getString("text");
				Tweet tweet = new Tweet(id, ts_, src_, auth_, lat_, lon_, text_);
				//System.out.println(id + "," + date + "," + text);
				tweets.add(tweet);					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}
	
	public List<Tweet> sampleGeoTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			String query = new String("select id, created_at, text, X(location) as X, Y(location) as Y, source, author from politics where created_at <= '" + tweetDate.end + "' and created_at >= '" + tweetDate.start + "';");
			//System.out.println(query);
			rs = stmt.executeQuery(query);
			//extract results
			while(rs.next()){
				double lat_ = rs.getDouble("X");
				double lon_ = rs.getDouble("Y");
				String text_ = rs.getString("text");
				String content = text_.toLowerCase();
				if(lat_ != 0 && lon_ != 0 && ((content.contains("romney") && !content.contains("obama")) || (!content.contains("romney") && content.contains("obama")))){
					Long id = rs.getLong("id");
					//Date date_ = rs.getDate("created_at");
					long ts_ = rs.getLong("timestamp");
					String src_ = rs.getString("source");
					String auth_ = rs.getString("author");
					Tweet tweet = new Tweet(id, ts_, src_, auth_, lat_, lon_, text_);
					//System.out.println(id + "," + date + "," + text);
					tweets.add(tweet);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}
}

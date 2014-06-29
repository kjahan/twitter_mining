// Constant utility class
package com.mlnlp.twitter;

public class TweetsConstants {
	private TweetsConstants() { }  // Prevents instantiation
	 
	public static final String FIRST_TWEET_TIME = new String("2012-09-29 21:20:18");	//in GMT
	public static final String LAST_TWEET_TIME = new String("2012-11-16 14:32:29");		//in GMT
	
	public static final String TWEET_FILE_NAME = new String("/home/kazem/Documents/ElectionAnalysis/election_data.txt");
	public static final String STOP_WORDS_FILE_NAME = new String("resources/stopwords.txt");
	
	public static final String TWEET_MYSQL_SERVER_NAME = "localhost";
	public static final String TWEET_MYSQL_USER_NAME = "root";
	public static final String TWEET_MYSQL_PSWD = "mysql";
	public static final String TWEET_MYSQL_DB_NAME = "politic";
	public static final int TOTAL_TWEETS_NO = 38937804;
}

package com.mlnlp.twitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mlnlp.utilities.TimeZone;
import com.mlnlp.utilities.TweetUtils;

public class TwitterFileDataSource implements TwitterDataSource {
	private BufferedReader bufferReader;
	private TweetDate tweetDate;
	
	private void initilizeBasics(String fileName){
		try {
			bufferReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println( "File Not Found!" );
		}
	}
	
	public TwitterFileDataSource(String fileName){
		initilizeBasics(fileName);
		tweetDate = new TweetDate();
	}
	
	public TwitterFileDataSource(String fileName, String start, String end, TimeZone tz){
		initilizeBasics(fileName);
		tweetDate = new TweetDate(start, end, tz);
	}
	
	private String[] readNextLine(){
		String line;
		String[] result = null;
		
		try {
			line = bufferReader.readLine();
			result = line.split(";@;");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public BufferedReader getBufferReader(){
		return bufferReader;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mlnlp.twitter.TwitterDataSource#totalTweetsNumber(java.lang.String, java.lang.String)
	 * find number of tweets in the given date range
	 * dates are in gmt
	 */
	public long totalTweetsNumber(){
    	long cnt = 0;
		try {
			TweetUtils tweetUtil = new TweetUtils(false, false, false);
			while (bufferReader.ready()) {
				String[] result = readNextLine();
				if(tweetUtil.isValidTweet(result[0])){
					if(tweetDate.fallsInTimeInterval(result[0])){
						//System.out.println(result[0]);
						cnt++;
					}else if(tweetDate.isDateGreaterThan(result[0])){
						break;
					}
				}
			}
			//bufferReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cnt;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlnlp.twitter.TwitterDataSource#totalTweetsNumber(java.lang.String, java.lang.String, java.lang.String)
	 */
	public long totalTweetsNumber(String keyword) {		
    	long cnt = 0;
		try {
			TweetUtils tweetUtil = new TweetUtils(false, false, false);
			while (bufferReader.ready()) {
				String[] result = readNextLine();
				if(tweetUtil.isValidTweet(result[0])){
					if(tweetDate.fallsInTimeInterval(result[0]) && tweetUtil.cleanTweet(result[result.length - 1]).contains(keyword)){
						//System.out.println(result[0]);
						cnt++;
					}else if(tweetDate.isDateGreaterThan(result[0])){
						break;
					}
				}
			}
			//bufferReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cnt;	
	}

	/*
	 * (non-Javadoc)
	 * @see com.mlnlp.twitter.TwitterDataSource#randomSapleTweets(int)
	 * this method returns sampleNo of tweets randomly selcted from all tweets
	 */
	public List<Tweet> randomSampleTweets(int sampleNo) {
		// TODO Auto-generated method stub
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		return tweets;
	}

	public void cleanUP() {
		// TODO Auto-generated method stub
		try {
			bufferReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Tweet> sampleGeoTweets() {
		// TODO Auto-generated method stub
		return null;
	}

}

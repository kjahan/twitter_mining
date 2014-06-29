package com.mlnlp.twitter;

import java.util.List;

public interface TwitterDataSource {
	long totalTweetsNumber();	//no of tweets in [start,end] time interval
	long totalTweetsNumber(String keyword);	//no of tweets in [start, end] which tweeted about keyword
	List<Tweet> randomSampleTweets(int sampleNo);	//sample tweets with a given probability
	List<Tweet> sampleGeoTweets();	//sample geo-tagged tweets
	public void cleanUP();
}
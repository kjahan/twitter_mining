package com.mlnlp.runanalysis;

import java.util.List;

import com.mlnlp.algorithms.TweetsStatistics;
import com.mlnlp.twitter.Tweet;
import com.mlnlp.twitter.TwitterDataSource;
import com.mlnlp.twitter.TwitterHibernateDataSource;
import com.mlnlp.utilities.MapUtil;
import com.mlnlp.utilities.Pair;
import com.mlnlp.utilities.TimeZone;

public class WorkerThread implements Runnable {
	private Pair<String, String> datePair;
	
	 public WorkerThread(Pair<String, String> date){
		 this.datePair = date;
	 }
	 
	 public void run() {
		 int sampleSize = 1000;		
		 TwitterDataSource tds;
		 System.out.println(datePair.getFirst());
		 System.out.println(datePair.getSecond());
		 tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
		 List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
		 TweetsStatistics stats = new TweetsStatistics();
		 stats.setTweets(tweets);
		 stats.extractHashTags();
		 MapUtil.printFrequentItems(stats.getSortedHahstagMap(), 2);
		 System.out.println("no of unique hashtags = " + MapUtil.getNumberOfUniqueItems(stats.getSortedHahstagMap()));
	 }
	 
	 public String toString(){
		 return (datePair.getFirst() + " " + datePair.getSecond());
	 }
}

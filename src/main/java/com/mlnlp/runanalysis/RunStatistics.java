package com.mlnlp.runanalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mlnlp.algorithms.TweetsStatistics;
import com.mlnlp.twitter.Tweet;
import com.mlnlp.twitter.TwitterDataSource;
import com.mlnlp.twitter.TwitterHibernateDataSource;
import com.mlnlp.utilities.DayIntervals;
import com.mlnlp.utilities.GenerateCsv;
import com.mlnlp.utilities.MapUtil;
import com.mlnlp.utilities.Pair;
import com.mlnlp.utilities.TimeZone;
import com.mlnlp.utilities.TweetUtils;

public class RunStatistics {
	public void generateTweetsFreq(List<Pair<String, String>> dayIntervals){
		List<String> tweetsFreqs = new ArrayList<String>();
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			long cnt = tds.totalTweetsNumber();
			System.out.println("cnt=" + cnt);
			String data = new String(datePair.getFirst() + "," + cnt);
			tweetsFreqs.add(data);
		}
		GenerateCsv.generateCsvFile("tweets_freqs.csv", tweetsFreqs);
	}
	
	public void generateSourceStats(List<Pair<String, String>> dayIntervals){
		int sampleSize = 20000;
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			stats.extractSources();
			MapUtil.printFrequentItems(stats.getSortedSourceMap(),100);
			System.out.println("no of unique sources = " + MapUtil.getNumberOfUniqueItems(stats.getSortedSourceMap()));
		}
	}
	
	public void generateAuthorStats(List<Pair<String, String>> dayIntervals){
		int sampleSize = 100000;		
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			stats.extractAuthors();
			System.out.println(stats.getSortedSourceMap().size());
			MapUtil.printFrequentItems(stats.getSortedAuthorMap(), 50);
			System.out.println("no of unique authors = " + MapUtil.getNumberOfUniqueItems(stats.getSortedAuthorMap()));
		}
	}
	
	public void getRTStats(List<Pair<String, String>> dayIntervals){
		int sampleSize = 10;		
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			int RTNo = stats.computeRTFreq();
			System.out.println("no of RTs = " + RTNo);
		}
	}
	
	public void getReplyStats(List<Pair<String, String>> dayIntervals){
		int sampleSize = 1000;		
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			int replyNo = stats.computeReplyFreq();
			System.out.println("no of Replies = " + replyNo);
		}
	}
	
	public void getHashTagStats(List<Pair<String, String>> dayIntervals) throws IOException{
		int sampleSize = 10000;		
		TwitterDataSource tds;
		List<String> tweetsHashTags = new ArrayList<String>();
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			stats.extractHashTags();
			int threshold = 10;
			MapUtil.printFrequentItems(stats.getSortedHahstagMap(), threshold);
			System.out.println("no of unique hashtags = " + MapUtil.getNumberOfUniqueItems(stats.getSortedHahstagMap()));
			TreeMap<String,Integer> aTreeMap = stats.getSortedHahstagMap();
			for (Map.Entry<String, Integer> entry : aTreeMap.entrySet()) {
			    String hashtag = entry.getKey();
			    Integer counter = entry.getValue();
			    if(counter > threshold){
			    	double percentage = 100.0*(double)counter/(double)sampleSize;
			    	TweetUtils tweetUtil = new TweetUtils(true, true, true);
			    	String data = new String(datePair.getFirst() + "," + tweetUtil.removePuncs(hashtag).replaceAll("\\s+","") + "," + String.valueOf(percentage));
			    	tweetsHashTags.add(data);
			    }
			}
		}
		GenerateCsv.generateCsvFile("tweets_hashtags.csv", tweetsHashTags);
	}
	
	public void getGeoStats(List<Pair<String, String>> dayIntervals){
		int sampleSize = 1000000;		
		TwitterDataSource tds;
		Date date = new Date();
		System.out.println(date.toString());
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			System.out.println("no of tweets=" + tweets.size() + " & %geo tweets=" + 100*(double)stats.countGeoTweets()/tweets.size());
		}
		date = new Date();
		System.out.println(date.toString());
	}

	public void getKewordStats(List<Pair<String, String>> dayIntervals, String keyword){
		int sampleSize = 10000;		
		TwitterDataSource tds;
		Date date = new Date();
		System.out.println(date.toString());
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			TweetsStatistics stats = new TweetsStatistics();
			stats.setTweets(tweets);
			System.out.println("no of tweets=" + tweets.size() + " & #mentions=" + 100*(double)stats.countKeywordMentions(keyword)/tweets.size());
		}
		date = new Date();
		System.out.println(date.toString());
	}
	
	public static void main( String[] args ) throws IOException, ParseException
    {		
		RunStatistics stats = new RunStatistics();
		List<Pair<String, String>> dayIntervals = DayIntervals.populateDates("resources/days_.txt");
		//stats.generateTweetsFreq(dayIntervals);	//test hibernate
		//test source distribution		
		stats.generateSourceStats(dayIntervals);
		//test author distribution		
		//stats.generateAuthorStats(dayIntervals);
		//count no of RT per day
		//stats.getRTStats(dayIntervals);
		//count no of replies per day
		//stats.getReplyStats(dayIntervals);
		//count no of # per day
		//stats.getHashTagStats(dayIntervals);
		//stats.getGeoStats(dayIntervals);
		//stats.getKewordStats(dayIntervals, "romney");
		//stats.generateTweetsFreq(dayIntervals);
    }
}

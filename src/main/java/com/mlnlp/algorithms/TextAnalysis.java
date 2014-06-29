package com.mlnlp.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mlnlp.twitter.Tweet;
import com.mlnlp.utilities.TweetUtils;
import com.mlnlp.utilities.ValueComparator;

public class TextAnalysis {
	private List<Tweet> tweets = new ArrayList<Tweet>();
	private Map<String, Integer> wordCounter = new Hashtable<String, Integer >();
	private ValueComparator bvc =  new ValueComparator(wordCounter);
	private TreeMap<String,Integer> sortedWordCounter = new TreeMap<String,Integer>(bvc);
	private TweetUtils tweetUtil;
	
	public TextAnalysis(boolean puncs, boolean stopWords) throws IOException{
		tweetUtil = new TweetUtils(puncs, false, stopWords);
	}
	
	public void setTweets(List<Tweet> tweets_){
		tweets = tweets_;
	}
	
	public void extractFrequencies(){
		for(Tweet tweet: tweets){
			String text_ = tweet.getText();
			//System.out.println("tweet=" + text_);
			//let's compute word frequency
			String cleanTweet = tweetUtil.cleanTweet(text_);
			//System.out.println("clean tweet=" + cleanTweet);
			String [] words = cleanTweet.split(" ");
			for(String word:words){
				if(!word.isEmpty()){
					if(wordCounter.containsKey(word)){
						int value = wordCounter.get(word);
						wordCounter.put(word, value + 1);
					}else{
						wordCounter.put(word, 1);
					}
				}
			}
		}
		sortedWordCounter.putAll(wordCounter);
	}
	
	public TreeMap<String,Integer> getSortedWordCountMap(){
		return sortedWordCounter;
	}
}

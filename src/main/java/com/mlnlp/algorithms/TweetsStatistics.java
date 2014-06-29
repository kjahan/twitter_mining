package com.mlnlp.algorithms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mlnlp.twitter.Tweet;
import com.mlnlp.utilities.ValueComparator;

public class TweetsStatistics {
	private List<Tweet> tweets = new ArrayList<Tweet>();
	private Map<String, Integer> sourceCounter = new Hashtable<String, Integer >();
	private ValueComparator srcBVC =  new ValueComparator(sourceCounter);
	private TreeMap<String,Integer> sortedSourceCounter = new TreeMap<String,Integer>(srcBVC);
	private Map<String, Integer> authorCounter = new Hashtable<String, Integer >();
	private ValueComparator authBVC =  new ValueComparator(authorCounter);
	private TreeMap<String,Integer> sortedAuthorCounter = new TreeMap<String,Integer>(authBVC);
	private Map<String, Integer> hashtagCounter = new Hashtable<String, Integer >();
	private ValueComparator hashtagBVC =  new ValueComparator(hashtagCounter);
	private TreeMap<String,Integer> sortedHashtagCounter = new TreeMap<String,Integer>(hashtagBVC);
	
	public void setTweets(List<Tweet> tweets_){
		tweets = tweets_;
	}
	
	public void extractSources(){
		for(Tweet tweet: tweets){
			String src = tweet.getSource();
			//System.out.println("src=" + src);
			//let's store the source into its map
			if(!src.isEmpty()){
				if(sourceCounter.containsKey(src)){
					int value = sourceCounter.get(src);
					sourceCounter.put(src, value + 1);
				}else{
					sourceCounter.put(src, 1);
				}
			}
		}
		sortedSourceCounter.putAll(sourceCounter);
	}
	
	public TreeMap<String,Integer> getSortedSourceMap(){
		return sortedSourceCounter;
	}
	
	public void extractAuthors(){
		for(Tweet tweet: tweets){
			String author = tweet.getAuthor();
			//System.out.println("author=" + author);
			//let's store the source into its map
			if(!author.isEmpty()){
				if(authorCounter.containsKey(author)){
					int value = authorCounter.get(author);
					authorCounter.put(author, value + 1);
				}else{
					authorCounter.put(author, 1);
				}
			}	
		}
		sortedAuthorCounter.putAll(authorCounter);
	}
	
	public TreeMap<String,Integer> getSortedAuthorMap(){
		return sortedAuthorCounter;
	}
	
	public int computeRTFreq(){
		int RTNo = 0;
		for(Tweet tweet: tweets){
			String text_ = tweet.getText();
			//let's compute RT count
			if(text_.contains("RT")){
				RTNo++;
			}
		}
		return RTNo;
	}
	
	public int computeReplyFreq(){
		int replyNo = 0;
		for(Tweet tweet: tweets){
			String text_ = tweet.getText();
			//let's compute reply count
			if(text_.contains("@")){
				//System.out.println(text_);
				replyNo++;
			}
		}
		return replyNo;
	}
	
	public void extractHashTags(){
		for(Tweet tweet: tweets){
			String text_ = tweet.getText();
			//let's compute # count. Now we just check the first hit.
			Pattern pattern = Pattern.compile("#(\\S+)");
			Matcher matcher = pattern.matcher(text_);
			//System.out.println(text_);
			while (matcher.find())
			{	
			    //System.out.println(matcher.group());
			    if(hashtagCounter.containsKey(matcher.group().toLowerCase())){
			    	hashtagCounter.put(matcher.group().toLowerCase(), hashtagCounter.get(matcher.group().toLowerCase()) + 1);
				}else{
					hashtagCounter.put(matcher.group().toLowerCase(), 1);
				}
			}
		}
		sortedHashtagCounter.putAll(hashtagCounter);
	}
	
	public TreeMap<String,Integer> getSortedHahstagMap(){
		return sortedHashtagCounter;
	}
	
	public int countGeoTweets(){
		int geoTweets = 0;
		for(Tweet tweet: tweets){
			if((tweet.getLat() != 0) && (tweet.getLon() != 0)){
				//System.out.println("location=(" + tweet.getLat() + "," + tweet.getLon() + ")");
				geoTweets++;
			}
		}
		return geoTweets;
	}
	
	public int countKeywordMentions(String keyword){
		int mentions = 0;
		for(Tweet tweet: tweets){
			if(tweet.getText().toLowerCase().contains(keyword.toLowerCase())){
				mentions++;
			}
		}
		return mentions;
	}
}
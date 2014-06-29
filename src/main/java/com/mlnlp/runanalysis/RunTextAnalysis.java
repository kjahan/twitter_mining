package com.mlnlp.runanalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.mlnlp.algorithms.TextAnalysis;
import com.mlnlp.twitter.Tweet;
import com.mlnlp.twitter.TwitterDataSource;
import com.mlnlp.twitter.TwitterHibernateDataSource;
import com.mlnlp.utilities.DayIntervals;
import com.mlnlp.utilities.MapUtil;
import com.mlnlp.utilities.Pair;
import com.mlnlp.utilities.TimeZone;

public class RunTextAnalysis {
	private void extractFrequentWordsOverTime(List<Pair<String, String>> dayIntervals) throws IOException{
		TwitterDataSource tds;
		int sampleSize = 10000;
		Date date = new Date();
		System.out.println(date.toString());
		//System.out.println(anal.dayIntervals.size());		
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			//System.out.println(tds.totalTweetsNumber());
			List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			//remove stop words and also get rid of punctuations
	    	TextAnalysis txtanalysis = new TextAnalysis(true, true);
	    	txtanalysis.setTweets(tweets);
	    	txtanalysis.extractFrequencies();
	    	MapUtil.printFrequentItems(txtanalysis.getSortedWordCountMap(), 300);
		}
		date = new Date();
		System.out.println(date.toString());
	}
	
	public void extractFrequentWordsForPeriod(String start, String end) throws IOException{
		TwitterDataSource tds;
		int sampleSize = 1000;
		tds = new TwitterHibernateDataSource(start, end, TimeZone.PST);
		//System.out.println(tds.totalTweetsNumber());
		List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
		//remove stop words and also get rid of punctuations
    	TextAnalysis txtanalysis = new TextAnalysis(true, true);
    	txtanalysis.setTweets(tweets);
    	txtanalysis.extractFrequencies();
    	MapUtil.printFrequentItems(txtanalysis.getSortedWordCountMap(), 10);
	}
	
	public static void main( String[] args ) throws IOException, ParseException
    {
		RunTextAnalysis anal = new RunTextAnalysis();
		List<Pair<String, String>> dayIntervals = DayIntervals.populateDates("resources/days.txt");
		anal.extractFrequentWordsOverTime(dayIntervals);
		//anal.extractFrequentWordsForPeriod("2012-11-02 00:00:00", "2012-11-02 23:59:59");
    }

}

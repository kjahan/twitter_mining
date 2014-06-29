package com.mlnlp.twitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mlnlp.utilities.DayIntervals;
import com.mlnlp.utilities.GenerateCsv;
import com.mlnlp.utilities.Pair;
import com.mlnlp.utilities.TimeZone;

/**
 * Hello world!
 *
 */
public class Run 
{
	/*
	 * Count percentage of tweet for a given keyword
	 */
	public void getKeywordPercentages(List<Pair<String, String>> dayIntervals, String keyword){
		TwitterDataSource tds;
		List<String> tweetsPerc = new ArrayList<String>();
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println("[" + datePair.getFirst() + "," + datePair.getSecond() + "]");
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			long interesting_tweets = tds.totalTweetsNumber(keyword);
			long tot_tweets = tds.totalTweetsNumber();
			double percentage = 0.0;
			if(tot_tweets > 0){
				percentage = (double)interesting_tweets/(double)tot_tweets;
			}
			System.out.println("Percentage of tweets for " + keyword + ":" + percentage);
			tds.cleanUP();
			String data = new String(datePair.getFirst() + "," + percentage);
			tweetsPerc.add(data);
		}
		GenerateCsv.generateCsvFile("R/data/tweets_perc_"+ keyword + ".csv", tweetsPerc);
	}
	
    public static void main( String[] args ) throws IOException, ParseException
    {   
    	Run run = new Run();
    	String query = new String("obama");
    	List<Pair<String, String>> dayIntervals = DayIntervals.populateDates("resources/days.txt");
    	run.getKeywordPercentages(dayIntervals, query);
    }
}

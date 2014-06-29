package com.mlnlp.runanalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mlnlp.algorithms.NaiveBayes;
import com.mlnlp.twitter.Tweet;
import com.mlnlp.twitter.TwitterDataSource;
import com.mlnlp.twitter.TwitterHibernateDataSource;
import com.mlnlp.utilities.DayIntervals;
import com.mlnlp.utilities.Pair;
import com.mlnlp.utilities.SentimentLabel;
import com.mlnlp.utilities.TimeZone;

public class RunBayes {
	/*
	 * This method randomly samples from all tweets and comute sentiments for selected tweets
	 */
	public void runSentimentAnalysisOnTweets(List<Pair<String, String>> dayIntervals, String trainingFile) throws IOException, ParseException{
		int sampleSize = 10000;		
		TwitterDataSource tds;
		Date date = new Date();
		System.out.println(date.toString());
		NaiveBayes nb = new NaiveBayes();
		nb.trainClassifier(trainingFile);
		String obama_pos_out = new String("");
		String obama_neg_out = new String("");
		String obama_tot_out = new String("");
		String romney_pos_out = new String("");
		String romney_neg_out = new String("");
		String romney_tot_out = new String("");
		String day_intervals_out = new String("");
		boolean flag = false;
		String comma = new String("");
		for(Pair<String, String> datePair : dayIntervals){
			int obama_pos= 0;
			int obama_neg= 0;
			int obama_net= 0;
			int romney_pos= 0;
			int romney_neg= 0;
			int romney_net= 0;
			//System.out.println(datePair.getFirst());
			//System.out.println(datePair.getSecond());
			Date date_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(datePair.getFirst());
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date_);
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
		    List<Tweet> tweets = tds.randomSampleTweets(sampleSize);
			for(Tweet tweet: tweets){
				String raw_content = tweet.getText();
				String content = raw_content.toLowerCase();
				if((content.contains("romney") && !content.contains("obama")) || (!content.contains("romney") && content.contains("obama"))){
					//System.out.println(content);
					//System.out.println("location=(" + tweet.getLat() + "," + tweet.getLon() + ")");
					SentimentLabel sentim = nb.classify(content);
					if(sentim == SentimentLabel.POSITIVE){
						//System.out.println("pos");
						if(content.contains("romney") && !content.contains("obama")){
							romney_pos++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "romney" + "=+1");
						}
						if(!content.contains("romney") && content.contains("obama")){
							obama_pos++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "obama" + "=+1");
						}
					}
					else if(sentim == SentimentLabel.NEGATIVE){
						//System.out.println("neg");
						if(content.contains("romney") && !content.contains("obama")){
							romney_neg++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "romney" + "=-1");
						}
						if(!content.contains("romney") && content.contains("obama")){
							obama_neg++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "obama" + "=-1");
						}
					}
					else if(sentim == SentimentLabel.NETURAL){
						//System.out.println("net");
						if(content.contains("romney") && !content.contains("obama")){
							romney_net++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "romney" + ",0");
						}
						if(!content.contains("romney") && content.contains("obama")){
							obama_net++;
							//System.out.println(tweet.getDate() + "," + tweet.getText() + "," + "obama" + ",0");
						}
					}	
				}
			}
			if (!flag){
				flag = true;
			}else{
				comma = ",";
			}
			System.out.println(new SimpleDateFormat("MMM-dd").format(cal.getTime()));
			System.out.println("obama_pos=" + obama_pos + ", obama_neg=" + obama_neg + ", obama_net=" + obama_net);
			System.out.println("romney_pos=" + romney_pos + ", romney_neg=" + romney_neg + ", romney_net=" + romney_net);
			day_intervals_out += comma + new SimpleDateFormat("MMM-dd").format(cal.getTime());
			obama_pos_out += comma + Integer.toString(obama_pos);
			obama_neg_out += comma + Integer.toString(obama_neg);
			obama_tot_out += comma + Integer.toString(obama_pos + obama_neg + obama_net);
			romney_pos_out += comma + Integer.toString(romney_pos);
			romney_neg_out += comma + Integer.toString(romney_neg);
			romney_tot_out += comma + Integer.toString(romney_pos + romney_neg + romney_net);
		}
		try{
			// Create file 
			FileWriter fstream = new FileWriter("obama_pos.csv");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(obama_pos_out + "\n");
			out.close();
			fstream = new FileWriter("obama_neg.csv");
			out = new BufferedWriter(fstream);
			out.write(obama_neg_out + "\n");
			out.close();
			fstream = new FileWriter("obama_tot.csv");
			out = new BufferedWriter(fstream);
			out.write(obama_tot_out + "\n");
			out.close();
			fstream = new FileWriter("romney_pos.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_pos_out + "\n");
			out.close();
			fstream = new FileWriter("romney_neg.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_neg_out + "\n");
			out.close();
			fstream = new FileWriter("romney_tot.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_tot_out + "\n");
			out.close();
			fstream = new FileWriter("days.csv");
			out = new BufferedWriter(fstream);
			out.write(day_intervals_out + "\n");
			out.close();
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		date = new Date();
		System.out.println(date.toString());
	}
	
	/*
	 * This method samples all geo-tagged tweets and compute sentiment for them
	 */
	public void runSentimentAnalysisOnGeoTweets(List<Pair<String, String>> dayIntervals, String trainingFile) throws IOException, ParseException{
		TwitterDataSource tds;
		Date date = new Date();
		System.out.println(date.toString());
		NaiveBayes nb = new NaiveBayes();
		nb.trainClassifier(trainingFile);
		String obama_pos_out = new String("");
		String obama_neg_out = new String("");
		String obama_tot_out = new String("");
		String romney_pos_out = new String("");
		String romney_neg_out = new String("");
		String romney_tot_out = new String("");
		String day_intervals_out = new String("");
		boolean flag = false;
		String comma = new String("");
		FileWriter geo_fstream = new FileWriter("sentiment_data_w_coordinates.csv");
		BufferedWriter geo_out = new BufferedWriter(geo_fstream);
		for(Pair<String, String> datePair : dayIntervals){
			int obama_pos= 0;
			int obama_neg= 0;
			int obama_net= 0;
			int romney_pos= 0;
			int romney_neg= 0;
			int romney_net= 0;
					
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			Date date_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(datePair.getFirst());
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date_);
		    tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
		    List<Tweet> tweets = tds.sampleGeoTweets();
			System.out.println("no of geotagged tweets=" + tweets.size());
			for(Tweet tweet: tweets){
				String raw_content = tweet.getText();
				String content = raw_content.toLowerCase();
				//System.out.println(content);
				//System.out.println("location=(" + tweet.getLat() + "," + tweet.getLon() + ")");
				SentimentLabel sentim = nb.classify(content);
				if(sentim == SentimentLabel.POSITIVE){
					//System.out.println("pos");
					if(content.contains("romney") && !content.contains("obama")){
						romney_pos++;
						geo_out.write(tweet.getDate() + "," + "romney," + tweet.getLat() + "," + tweet.getLon() + ",1" + "\n");
					}
					if(!content.contains("romney") && content.contains("obama")){
						obama_pos++;
						geo_out.write(tweet.getDate() + "," + "obama," + tweet.getLat() + "," + tweet.getLon() + ",1" + "\n");

					}
				}
				else if(sentim == SentimentLabel.NEGATIVE){
					//System.out.println("neg");
					if(content.contains("romney") && !content.contains("obama")){
						romney_neg++;
						geo_out.write(tweet.getDate() + "," + "romney," + tweet.getLat() + "," + tweet.getLon() + ",-1" + "\n");
					}
					if(!content.contains("romney") && content.contains("obama")){
						obama_neg++;
						geo_out.write(tweet.getDate() + "," + "obama," + tweet.getLat() + "," + tweet.getLon() + ",-1" + "\n");
					}
				}
				else if(sentim == SentimentLabel.NETURAL){
					//System.out.println("net");
					if(content.contains("romney") && !content.contains("obama")){
						romney_net++;
					}
					if(!content.contains("romney") && content.contains("obama")){
						obama_net++;
					}
				}	
			}
			if (!flag){
				flag = true;
			}else{
				comma = ",";
			}
			System.out.println(new SimpleDateFormat("MMM-dd").format(cal.getTime()));
			System.out.println("obama_pos=" + obama_pos + ", obama_neg=" + obama_neg + ", obama_net=" + obama_net);
			System.out.println("romney_pos=" + romney_pos + ", romney_neg=" + romney_neg + ", romney_net=" + romney_net);
			day_intervals_out += comma + new SimpleDateFormat("MMM-dd").format(cal.getTime());
			obama_pos_out += comma + Integer.toString(obama_pos);
			obama_neg_out += comma + Integer.toString(obama_neg);
			obama_tot_out += comma + Integer.toString(obama_pos + obama_neg + obama_net);
			romney_pos_out += comma + Integer.toString(romney_pos);
			romney_neg_out += comma + Integer.toString(romney_neg);
			romney_tot_out += comma + Integer.toString(romney_pos + romney_neg + romney_net);
		}
		geo_out.close();
		try{
			// Create file 
			FileWriter fstream = new FileWriter("geo_obama_pos.csv");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(obama_pos_out + "\n");
			out.close();
			fstream = new FileWriter("geo_obama_neg.csv");
			out = new BufferedWriter(fstream);
			out.write(obama_neg_out + "\n");
			out.close();
			fstream = new FileWriter("geo_obama_tot.csv");
			out = new BufferedWriter(fstream);
			out.write(obama_tot_out + "\n");
			out.close();
			fstream = new FileWriter("geo_romney_pos.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_pos_out + "\n");
			out.close();
			fstream = new FileWriter("geo_romney_neg.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_neg_out + "\n");
			out.close();
			fstream = new FileWriter("geo_romney_tot.csv");
			out = new BufferedWriter(fstream);
			out.write(romney_tot_out + "\n");
			out.close();
			fstream = new FileWriter("geo_days.csv");
			out = new BufferedWriter(fstream);
			out.write(day_intervals_out + "\n");
			out.close();
		}catch (Exception e){
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		date = new Date();
		System.out.println(date.toString());
	}

	public void generateGeoTweets(List<Pair<String, String>> dayIntervals){
		TwitterDataSource tds;
		for(Pair<String, String> datePair : dayIntervals){
			System.out.println(datePair.getFirst());
			System.out.println(datePair.getSecond());
			tds = new TwitterHibernateDataSource(datePair.getFirst(), datePair.getSecond(), TimeZone.PST);
			List<Tweet> tweets = tds.sampleGeoTweets();
			System.out.println("no of geotagged tweets=" + tweets.size());
		}
	}
	
	public static void main( String[] args ) throws IOException, ParseException{
		/*
		long unixSeconds = 1348978818;
		Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("PST"));
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);
		System.out.println(date.getTime()/1000);
		*/
		//NaiveBayes nb = new NaiveBayes();
		String trainingFile = new String("resources/cleantweets.txt");
		//nb.trainClassifier(trainingFile);
		//SentimentLabel sentim = nb.classify("If you don't make over a year an you vote for Romney, then you are basically committing suicide");
		//SentimentLabel sentim = nb.classify("RT @ReaganWorld: If we want Ambassador Rice to resign we need to FIRE OBAMA. ");
		//SentimentLabel sentim = nb.classify("If obama passes marchel law im moving to europe. That kind of Shit makes me no longer want to be a us citizen"); 
		//SentimentLabel sentim = nb.classify("RT @JohnGGalt: Retweet if you think Obama has failed as President.");
		//SentimentLabel sentim = nb.classify("RT @OhMrWonka: Obama probably called Romney and said I don't see how you can hate from outside the White House, you can't even get in.");
		//String tweet_ = "Growing voter confidence in economy lifts Obam";
		//String tweet_= "@eiwizabeff then vote for romney #romneyryan2012";
		//String tweet_ = "RT @kksheld: Americans do not want the dreams from Obama's father, we want the dreams from our founding fathers and dreams for our child ...";
		//String tweet_ = "I feel like the presidential election has become an episode of SNL. Both candidates are selling comedy to a nation, and we're buying it.";
		//String tweet_ = "romney";
		/*
		SentimentLabel sentim = nb.classify(tweet_);
		if(sentim == SentimentLabel.POSITIVE){
			System.out.println("pos!");
		}
		else if(sentim == SentimentLabel.NEGATIVE){
			System.out.println("neg!");
		}
		else if(sentim == SentimentLabel.NETURAL){
			System.out.println("net!");
		}
		*/
		
		Date date_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2012-09-29 00:00:00");
	    Date newDate = new Date(date_.getTime() + (8L * 60L * 60L * 1000L));
	    System.out.println(newDate.toString());
	    System.out.println(newDate.getTime()/1000);
	    
		RunBayes nb = new RunBayes();
		List<Pair<String, String>> dayIntervals = DayIntervals.populateDates("resources/days.txt");
		//run sentiment analysis
		long prev = System.currentTimeMillis();
		nb.runSentimentAnalysisOnTweets(dayIntervals, trainingFile);
		System.out.println(System.currentTimeMillis() - prev);
		
		//nb.generateGeoTweets(dayIntervals);
		//nb.runSentimentAnalysisOnGeoTweets(dayIntervals, trainingFile);
	}
}

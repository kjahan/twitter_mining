package com.mlnlp.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mlnlp.utilities.TimeZone;

public class TweetDate {
	protected Date startDate = null;
	protected Date endDate = null;
	protected String start;
	protected String end;
	protected long startTs;
	protected long endTs;
	private String newFormat;
	private SimpleDateFormat sdf;
	private TimeZone timeZone;
	private final long hoursInMillis = 60L * 60L * 1000L;
	
	public TweetDate(){
		newFormat = new String("yyyy-MM-dd HH:mm:ss");
		sdf = new SimpleDateFormat(newFormat);
		//start and end are in GMT time zone
		timeZone = TimeZone.GMT;
		try {
			startDate = convertToDate(TweetsConstants.FIRST_TWEET_TIME);
			endDate = convertToDate(TweetsConstants.LAST_TWEET_TIME);
			start = new String(TweetsConstants.FIRST_TWEET_TIME);
			end = new String(TweetsConstants.LAST_TWEET_TIME);
			startTs = startDate.getTime()/1000;
			endTs = endDate.getTime()/1000;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	public TweetDate(String start_, String end_, TimeZone tz){
		newFormat = new String("yyyy-MM-dd HH:mm:ss");
		sdf = new SimpleDateFormat(newFormat);
		timeZone = tz;
		try {
			//start = new String(start_);
			//end = new String(end_);
			startDate = convertToDate(start_);
			endDate = convertToDate(end_);
			start = new String(sdf.format(startDate));
			end = new String(sdf.format(endDate));
			startTs = startDate.getTime()/1000;
			endTs = endDate.getTime()/1000;
			//System.out.println(start);
			//System.out.println(end);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	protected boolean fallsInTimeInterval(String str){
		Date twDate;
		boolean result = false;
		try {
			twDate = convertToDate(str);
			if((startDate.compareTo(twDate) <= 0) && (twDate.compareTo(endDate) <= 0)){
				result = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	protected boolean isDateGreaterThan(String str){
		Date twDate;
		boolean result = false;
		try {
			twDate = convertToDate(str);
			if(twDate.compareTo(endDate) > 0){
				result = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	private Date convertToDate(String input) throws ParseException{
		Date date = sdf.parse(input);
		Date newDate;
		if(timeZone == TimeZone.EST){
			newDate = new Date(date.getTime() + (5L * hoursInMillis)); // Adds 5 hours
		}else if(timeZone == TimeZone.PST){
			newDate = new Date(date.getTime() + (8L * hoursInMillis)); // Adds 8 hours
		}else{
			//GMT
			newDate = new Date(date.getTime() + (0L * hoursInMillis)); // Adds 8 hours
		}
		return newDate; 
	}
}

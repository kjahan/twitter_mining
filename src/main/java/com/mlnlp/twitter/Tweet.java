package com.mlnlp.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet {
    private long id;
    private long ts;
    private String source;
    private String author;
    private double lat;
    private double lon;
    private String text;

    public Tweet() {}

    public Tweet(long id_, long ts_, String src_, String author_, double lat_, double long_, String text_){
    	id = id_;
    	ts = ts_;
    	source = src_;
    	author = author_;
    	lat = lat_;
    	lon = long_;
    	text = text_;
    	}
    
    public long getId() {
        return this.id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public long getTs() {
        return this.ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public double getLat() {
    	return this.lat;
    }
    
    public void setLat(double lat) {
    	this.lat = lat;
    }
    
    public double getLon() {
    	return this.lon;
    }
    
    public void setLon(double lon) {
    	this.lon = lon;
    }
    
    public String getDate(){
		Date date = new Date(this.ts*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("PST"));
		String formattedDate = sdf.format(date);
		return formattedDate;
    }
}
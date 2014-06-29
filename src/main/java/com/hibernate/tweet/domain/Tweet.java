package com.hibernate.tweet.domain;

import java.util.Date;

public class Tweet {
    private Long id;
    private Date date;
    private String source;
    private String author;
    private double lat;
    private double lon;
    private String text;

    public Tweet() {}

    public Long getId() {
        return this.id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}


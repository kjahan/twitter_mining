package com.mlnlp.twitter;

import org.hibernate.Session;

import java.util.*;

import com.mlnlp.twitter.Tweet;
import com.mlnlp.twitter.HibernateUtil;

public class TweetManager {

	public static void main(String[] args) {
		TweetManager mgr = new TweetManager();

		if (args[0].equals("store")) {
			mgr.createAndStoreTweet("Heidi Wu", new Date());
		}
		else if (args[0].equals("list")) {
			List tweets = mgr.listTweets();
            for (int i = 0; i < tweets.size(); i++) {
                
            	Tweet theEvent = (Tweet) tweets.get(i);
                System.out.println(
                        "Text: " + theEvent.getText() + " Created at: " + theEvent.getDate()
                );
            }
        }

		HibernateUtil.getSessionFactory().close();
	}

	private void createAndStoreTweet(String author, Date created_at) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Tweet theTweet = new Tweet();
		theTweet.setAuthor(author);
		theTweet.setTs(created_at.getTime()/1000);
		//theTweet.setLat((double)100);
		//theTweet.setLon((double)200);
		theTweet.setSource("facebook");
		theTweet.setText("WTF...");

	    session.save(theTweet);
		session.getTransaction().commit();
	}
	    
    private List listTweets() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Tweet where id < 10").list();
        session.getTransaction().commit();
        return result;
    }
}

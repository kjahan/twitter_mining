package com.mlnlp.twitter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import com.hibernate.tweet.util.HibernateUtil;
import com.mlnlp.utilities.TimeZone;

public class TwitterHibernateDataSource implements TwitterDataSource{
	private TweetDate tweetDate;
	
	//@see com.mlnlp.twitter.TwitterDataSource#totalTweetsNumber(java.lang.String, java.lang.String)
	//The constructor(start time, end time, timezone)
	//dates are in GMT
	public TwitterHibernateDataSource(String start, String end, TimeZone tz){
		tweetDate = new TweetDate(start, end, tz);
	}

	//find number of tweets in the given date range
	public long totalTweetsNumber() {
		long count = 0;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String hql = new String("select count(*) from Tweet where date <= '" + tweetDate.end + "' and date >= '" + tweetDate.start + "'");
		count = ((Long)session.createQuery(hql).iterate().next()).longValue();
		session.getTransaction().commit();
		
		return count;
	}
	
	public long totalTweetsNumber(String keyword) {
		long count = 0;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		//String query = new String("select count(*) from politics where created_at <= '" + tweetDate.end + "' and created_at >= '" + tweetDate.start + "' and `text` COLLATE UTF8_GENERAL_CI LIKE '%" + keyword + "%';");
		//we should take into account collate in hql later!!!
		String hql = new String("select count(*) from Tweet where date <= '" + tweetDate.end + "' and date >= '" + tweetDate.start + "' and text LIKE '%" + keyword + "%'");
		count = ((Long)session.createQuery(hql).iterate().next()).longValue();
		session.getTransaction().commit();

		return count;
	}	
	
	public List<Tweet> randomSampleTweets(int sampleNo){
		long count = 0, min_id = 0, max_id = 0;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        System.out.println("start date=" + tweetDate.start + ", end date=" + tweetDate.end);
        System.out.println("start ts=" + tweetDate.startTs + ", end ts=" + tweetDate.endTs);
        String hql = new String("select count(*) from Tweet where timestamp <= '" + tweetDate.endTs + "' and timestamp >= '" + tweetDate.startTs + "'");
		count = ((Long)session.createQuery(hql).iterate().next()).longValue();
		if (count == 0){
			session.getTransaction().commit();
			System.out.println("count:" + count);
			return Collections.emptyList();
		}
		hql = new String("select min(id) from Tweet where timestamp <= '" + tweetDate.endTs + "' and timestamp >= '" + tweetDate.startTs + "'");
		min_id = ((Long)session.createQuery(hql).iterate().next()).longValue();
		hql = new String("select max(id) from Tweet where timestamp <= '" + tweetDate.endTs + "' and timestamp >= '" + tweetDate.startTs + "'");
		max_id = ((Long)session.createQuery(hql).iterate().next()).longValue();
        System.out.println("min:" + min_id + ", max id:" + max_id);
        long range = max_id - min_id;
		hql = new String("from Tweet where id in (");// + randomNumber_1 + "','" + randomNumber_2 + "'");
		for(int i = 1; i < sampleNo; i++){
        	Random random = new Random();
        	long randomNumber = (long)(random.nextDouble()*range) + min_id;
        	hql += randomNumber + ",";
        }
        Random random = new Random();
    	long randomNumber = (long)(random.nextDouble()*range) + min_id;
    	hql += randomNumber + ")";
        List tweets = session.createQuery(hql).list();
        //List tweets = session.createQuery("from Tweet where date <= '" + tweetDate.end + "' and date >= '" + tweetDate.start + "' order by rand()").setMaxResults(sampleNo).list();
        session.getTransaction().commit();
		
		return tweets;
	}
	
	//sample tweetslat with a given probability
	public List<Tweet> sampleGeoTweets() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List tweets = session.createQuery("from Tweet where timestamp <= '" + tweetDate.endTs + "' and timestamp >= '" + tweetDate.startTs + "' and lat > 0 and lon > 0 and ((text like '%obama%' and text not like '%romney%') or (text not like '%obama%' and text like '%romney%'))").list();
        session.getTransaction().commit();
        
        return tweets;
	}
	
	public void cleanUP() {
		return;
	}
}

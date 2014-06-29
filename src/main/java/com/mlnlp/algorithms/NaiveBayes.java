package com.mlnlp.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.mlnlp.utilities.SentimentLabel;
import com.mlnlp.utilities.TweetUtils;

public class NaiveBayes {
	private TweetUtils tweetUtil;
	private SentimentLabel label;	//sentiment class
	private Hashtable<SentimentLabel, Integer> labelCounter = new Hashtable<SentimentLabel, Integer>();
	private Hashtable<String, Integer> posCounter = new Hashtable<String, Integer >();
	private Hashtable<String, Integer> negCounter = new Hashtable<String, Integer >();
	private Hashtable<String, Integer> netCounter = new Hashtable<String, Integer >();
	private List<String> vocabulary = new ArrayList<String>();
	private int vocabSize;
	private double posPrior;
	private double negPrior;
	private double netPrior;
	
	private int totalPosCounter;
	private int totalNegCounter;
	private int totalNetCounter;
		
	public NaiveBayes() throws IOException{
		posPrior = 1.0/3.0;
		negPrior = 1.0/3.0;
		netPrior = 1.0/3.0;		
		totalPosCounter = 0;
		totalNegCounter = 0;
		totalNetCounter = 0;
		vocabSize = 0;
		tweetUtil = new TweetUtils(true, true, true);
	}
	
	public void getLabel(){
		switch (label) {
	        case NEGATIVE:
	            System.out.println("class=-1");
	            break;
	                
	        case NETURAL:
	            System.out.println("class=0");
	            break;
	                     
	        case POSITIVE:
	            System.out.println("class=+1");
	            break;
	                    
	        default:
	            System.out.println("No WAY!!!");
	            break;
		}
	}
	
	public void trainClassifier(String trainingFile) throws IOException{
		extractFeatures(trainingFile);
		cleanCounterMaps();
		printCounterMaps(10);
		printLabelMaps();
		computePriors();
		computeTotalCounters();
	}
	
	/*
	 * Read the training file
	 */	
	public void extractFeatures(String trainingFile) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(trainingFile));
		String line;
		int no = 0;
		int cnt = 0;
		int validLines = 0;
		int obamaCnt = 0;
		int romneyCnt = 0;
		String [] words = null;		
		
		while ((line = in.readLine()) != null) {			
			if((cnt % 2) == 0){
				String cleanTweet = tweetUtil.cleanTweet(line);				
				words = cleanTweet.split(" ");
				if(cleanTweet.contains("obama")){
					obamaCnt++;
				}else if(cleanTweet.contains("romney")){
					romneyCnt++;
				}
				//System.out.println(cleanTweet);
			}else{
				String label = line;
				//System.out.println(label);
				if(label.contains("obama=+1,romney=na") || label.contains("obama=na,romney=+1")){
					validLines++;
					//positive for obama/romney
					for(String word:words){
						if(!word.isEmpty()){
							vocabulary.add(word);
							vocabSize += 1;
							if(posCounter.containsKey(word)){
								int value = posCounter.get(word);
								posCounter.put(word, value + 1);
							}else{
								posCounter.put(word, 1);
							}
						}
					}
					//let's add a counter to labelCounter
					if(labelCounter.containsKey(SentimentLabel.POSITIVE)){
						int posCnt = labelCounter.get(SentimentLabel.POSITIVE);
						labelCounter.put(SentimentLabel.POSITIVE, posCnt + 1);
					}else{
						labelCounter.put(SentimentLabel.POSITIVE, 1);
					}
				}else if(label.contains("obama=-1,romney=na") || label.contains("obama=na,romney=-1")){
					validLines++;
					//negative for obama/romney
					for(String word:words){
						if(!word.isEmpty()){
							vocabulary.add(word);
							vocabSize += 1;
							if(negCounter.containsKey(word)){
								int value = negCounter.get(word);
								negCounter.put(word, value + 1);
							}else{
								negCounter.put(word, 1);
							}
						}
					}
					//let's add a counter to labelCounter
					if(labelCounter.containsKey(SentimentLabel.NEGATIVE)){
						int negCnt = labelCounter.get(SentimentLabel.NEGATIVE);
						labelCounter.put(SentimentLabel.NEGATIVE, negCnt + 1);
					}else{
						labelCounter.put(SentimentLabel.NEGATIVE, 1);
					}
				}else if(label.contains("obama=0,romney=na") || label.contains("obama=na,romney=0") || label.contains("obama=0,romney=0")){
					validLines++;
					//neutral for obama/romney
					for(String word:words){
						if(!word.isEmpty()){
							vocabulary.add(word);
							vocabSize += 1;
							if(netCounter.containsKey(word)){
								int value = netCounter.get(word);
								netCounter.put(word, value + 1);
							}else{
								netCounter.put(word, 1);
							}
						}
					}
					//let's add a counter to labelCounter
					if(labelCounter.containsKey(SentimentLabel.NETURAL)){
						int netCnt = labelCounter.get(SentimentLabel.NETURAL);
						labelCounter.put(SentimentLabel.NETURAL, netCnt + 1);
					}else{
						labelCounter.put(SentimentLabel.NETURAL, 1);
					}
				}
			}
	        cnt++;
	        no++;
	    }
		System.out.println("vocabulary size=" + vocabSize);
		System.out.println("no of tweets=" + no/2);
		System.out.println("no of tweets mentioned obama=" + obamaCnt);
		System.out.println("no of tweets mentioned romney=" + romneyCnt);
		System.out.println("valid lines=" + validLines);
		in.close();
	}
	
	private void computePriors(){
		int posCnt = labelCounter.get(SentimentLabel.POSITIVE);
		int negCnt = labelCounter.get(SentimentLabel.NEGATIVE);
		int netCnt = labelCounter.get(SentimentLabel.NETURAL);
		
		int sum = posCnt + negCnt + netCnt;
		
		posPrior = (double)posCnt/(double)sum;
		negPrior = (double)negCnt/(double)sum;
		netPrior = (double)netCnt/(double)sum;
		
		
		System.out.println("pos prior=" + posPrior + ", neg prior=" + negPrior + ", net prior=" + netPrior);
	}
	
	private void computeTotalCounters(){
		for (int counter : posCounter.values()) {
			totalPosCounter += counter;		    
		}
		for (int counter : negCounter.values()) {
			totalNegCounter += counter;		    
		}		
		for (int counter : netCounter.values()) {
			totalNetCounter += counter;		    
		}

		System.out.println("tot pos counter=" + totalPosCounter + ", tot neg counter=" + totalNegCounter + ", tot net counter=" + totalNetCounter);
	}
	
	public SentimentLabel classify(String text){
		String cleanTweet = tweetUtil.cleanTweet(text);
		String [] words = cleanTweet.split(" ");
		double posProb = posPrior;
		double negProb = negPrior;
		double netProb = netPrior;
		
		double logPosProb = Math.log(posPrior);
		double logNegProb = Math.log(negPrior);
		double logNetProb = Math.log(netPrior);
				
		for(String word: words){
			if(!word.isEmpty()){
				//System.out.println("word = " + word);
				
				if(!posCounter.containsKey(word) && !negCounter.containsKey(word) && !netCounter.containsKey(word)){
					continue;
				}
								
				int posCnt = 0, negCnt = 0, netCnt = 0;
				
				if(posCounter.containsKey(word)){
					//System.out.println("found word " + word + " in pos counter=" + (double)posCounter.get(word));
					posCnt = posCounter.get(word);
				}
				posProb *= (double)(posCnt + 1)/(double)(totalPosCounter + vocabSize);
				logPosProb += Math.log((double)(posCnt + 1)/(double)(totalPosCounter + vocabSize));
				
				if(negCounter.containsKey(word)){
					//System.out.println("found word " + word + " in neg counter=" + (double)negCounter.get(word));
					negCnt = negCounter.get(word);
				}
				negProb *= (double)(negCnt + 1)/(double)(totalNegCounter + vocabSize);
				logNegProb += Math.log((double)(negCnt + 1)/(double)(totalNegCounter + vocabSize));
				
				if(netCounter.containsKey(word)){
					//System.out.println("found word " + word + " in net counter=" + (double)netCounter.get(word));
					netCnt = netCounter.get(word);
				}
				netProb *= (double)(netCnt + 1)/(double)(totalNetCounter + vocabSize);
				logNetProb += Math.log((double)(netCnt + 1)/(double)(totalNetCounter + vocabSize));
				//System.out.println("pos=" + posProb + ", neg=" + negProb + ", net=" + netProb);
				//System.out.println("log pos=" + logPosProb + ", log neg=" + logNegProb + ", log net=" + logNetProb);
			}
		}
		
		//System.out.println("pos=" + posProb + ", neg=" + negProb + ", net=" + netProb);
		//System.out.println("log pos=" + logPosProb + ", log neg=" + logNegProb + ", log net=" + logNetProb);
		if(logPosProb > logNegProb && logPosProb > logNetProb){
			return SentimentLabel.POSITIVE;
		}
		else if(logNegProb > logPosProb && logNegProb > logNetProb){
			return SentimentLabel.NEGATIVE;
		}
		
		return SentimentLabel.NETURAL;
	}
	
	/*
	 * remove forbidden words
	 */
	private int cleanCounterMaps(){
		String[] forbiddenWords = {"obama", "romney", "m", "mitt", "barack", "re"};
		
		for(String word: forbiddenWords){
			posCounter.remove(word);
			negCounter.remove(word);
			netCounter.remove(word);
		}
		
		return 0;
	}
	
	private void printCounterMaps(int threshold){
		System.out.println("positive words:");
		for (Map.Entry<String, Integer> entry : posCounter.entrySet()) {
		    String word = entry.getKey();
		    Integer counter = entry.getValue();
		    if(counter > threshold){
		    	System.out.println("word=" + word + ",counter=" + counter );
		    }
		}
		
		System.out.println("negative words:");
		for (Map.Entry<String, Integer> entry : negCounter.entrySet()) {
		    String word = entry.getKey();
		    Integer counter = entry.getValue();
		    if(counter > threshold){
		    	System.out.println("word=" + word + ",counter=" + counter );
		    }
		}
		
		System.out.println("neutral words:");
		for (Map.Entry<String, Integer> entry : netCounter.entrySet()) {
		    String word = entry.getKey();
		    Integer counter = entry.getValue();
		    if(counter > threshold){
		    	System.out.println("word=" + word + ",counter=" + counter );
		    }
		}
	}
	
	private void printLabelMaps(){
		for(Map.Entry<SentimentLabel, Integer> entry : labelCounter.entrySet()){
			if(entry.getKey() == SentimentLabel.POSITIVE){
				System.out.println("postive count=" + entry.getValue());
			}
			else if(entry.getKey() == SentimentLabel.NEGATIVE){
				System.out.println("negative count=" + entry.getValue());
			}
			else if(entry.getKey() == SentimentLabel.NETURAL){
				System.out.println("neutral count=" + entry.getValue());
			}
		}
	}
}

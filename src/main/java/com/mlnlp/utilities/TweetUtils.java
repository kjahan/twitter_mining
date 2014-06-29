package com.mlnlp.utilities;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class TweetUtils {
	public boolean removePunc = false;
	public boolean negations = false;
	public boolean removeStopWords = false;
	private Hashtable<String, Integer> negators = new Hashtable<String, Integer>();
	private Hashtable<String, String> negationMap = new Hashtable<String, String>();
	private Hashtable<String, String> puncMap = new Hashtable<String, String>();
	private Hashtable<String, Integer> puncs = new Hashtable<String, Integer>();
	private StopWords stopWords;

	public TweetUtils(boolean puncs, boolean negs, boolean stopW) throws IOException{
		stopWords = new StopWords();	
		buildNegationMap();
		buildPuncMap();
		removePunc = puncs;
		negations = negs;
		removeStopWords = stopW;
	}
	
	private void buildNegationMap(){
		String doNot = "donot";
		negationMap.put("don't", doNot);
		negationMap.put("do not", doNot);
		negationMap.put("dont", doNot);
		negators.put(doNot, 1);
		
		String doesNot = "doesnot";
		negationMap.put("doesn't", doesNot);
		negationMap.put("does not", doesNot);
		negationMap.put("doesnt", doesNot);
		negators.put(doesNot, 1);
		
		String didNot = "didnot";
		negationMap.put("didn't", didNot);
		negationMap.put("did not", didNot);
		negationMap.put("didnt", didNot);
		negators.put(didNot, 1);
		
		String amNot = "amnot";
		negationMap.put("am not", amNot);
		negationMap.put("amn't", amNot);
		negationMap.put("amnt", amNot);
		negators.put(amNot, 1);
		
		String isNot = "isnot";
		negationMap.put("is not", isNot);
		negationMap.put("isn't", isNot);
		negationMap.put("isnt", isNot);
		negators.put(isNot, 1);
		
		String areNot = "arenot";
		negationMap.put("are not", areNot);
		negationMap.put("aren't", areNot);
		negationMap.put("arent", areNot);
		negators.put(areNot, 1);
		
		String wasNot = "wasnot";
		negationMap.put("was not", wasNot);
		negationMap.put("wasn't", wasNot);
		negationMap.put("wasnt", wasNot);
		negators.put(wasNot, 1);
		
		String wereNot = "werenot";
		negationMap.put("were not", wereNot);
		negationMap.put("weren't", wereNot);
		negationMap.put("werent", wereNot);
		negators.put(wereNot, 1);
		
		String hasNot = "hasnot";
		negationMap.put("has not", hasNot);
		negationMap.put("hasn't", hasNot);
		negationMap.put("hasnt", hasNot);
		negators.put(hasNot, 1);
		
		String haveNot = "havenot";
		negationMap.put("have not", haveNot);
		negationMap.put("haven't", haveNot);
		negationMap.put("havent", haveNot);
		negators.put(haveNot, 1);
		
		String hadNot = "hadnot";
		negationMap.put("had not", hadNot);
		negationMap.put("hadn't", hadNot);
		negationMap.put("hadnt", hadNot);
		negators.put(hadNot, 1);
		
		String willNot = "willnot";
		negationMap.put("will not", willNot);
		negationMap.put("willn't", willNot);
		negationMap.put("willnt", willNot);
		negators.put(willNot, 1);
		
		String wouldNot = "wouldnot";
		negationMap.put("would not", wouldNot);
		negationMap.put("wouldn't", wouldNot);
		negationMap.put("wouldnt", wouldNot);
		negators.put(wouldNot, 1);
		
		String canNot = "cannot";
		negationMap.put("can not", canNot);
		negationMap.put("cann't", canNot);
		negationMap.put("cannt", canNot);
		negators.put(canNot, 1);
		
		String shouldNot = "shouldnot";
		negationMap.put("should not", shouldNot);
		negationMap.put("shouldn't", shouldNot);
		negationMap.put("shouldnt", shouldNot);
		negators.put(shouldNot, 1);
		
		String couldNot = "couldnot";
		negationMap.put("could not", couldNot);
		negationMap.put("couldn't", couldNot);
		negationMap.put("couldnt", couldNot);
		negators.put(couldNot, 1);
		
		String never = "never";
		negators.put(never, 1);
		String not = "not";
		negators.put(not, 1);
		String nothing = "nothing";
		negators.put(nothing, 1);
	}
	
	private void buildPuncMap(){
		puncMap.put("\\.", " . ");
		puncMap.put("\\,", " , ");
		puncMap.put("\\;", " ; ");
		puncMap.put("\\?", " ? ");
		puncMap.put("\\!", " ! ");
		puncs.put(".", 1);
		puncs.put(",", 1);
		puncs.put(";", 1);
		puncs.put("?", 1);
		puncs.put("!", 1);
	}
	
	public boolean isValidTweet(String tweetDate_){
		boolean result = false;
		if(tweetDate_.matches("^\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}")){
			result = true;
		}
		
		return result;
	}
	
	public String cleanTweet(String rawTweet){
		//remove web link
	    String cleanTweet = rawTweet.replaceAll("\\s*http://\\S+\\s*", " ");
	    //remove @Somebody
	    cleanTweet = cleanTweet.replaceAll("@\\S+", "");
	    //remove RT
	    cleanTweet = cleanTweet.replaceAll("RT", "");
	    //remove hashcode
	    cleanTweet = cleanTweet.replaceAll("#\\S+", "");
	    //remove numbers
	    cleanTweet = cleanTweet.replaceAll("\\d+", " ");
	    cleanTweet = cleanTweet.toLowerCase();
	    if(negations){
	    	cleanTweet = convertNegations(cleanTweet);
	    }
	    if(removeStopWords){
	    	cleanTweet = removeStopWords(cleanTweet);
	    }
	    if(negations){
	    	cleanTweet = extractNegations(cleanTweet);
	    }
		if(removePunc){
			cleanTweet = removePuncs(cleanTweet);
		}
	    if(removeStopWords){
	    	cleanTweet = removeStopWords(cleanTweet);
	    }
	    
		return cleanTweet;
	}
	
	public String removePuncs(String text){
	    //remove punctuations
		text = text.replaceAll("\\p{Punct}", " ");
	    return text;
	}
	
	public String convertNegations(String text){
		//clean up negations
		for(Map.Entry<String, String> entry : negationMap.entrySet()){
			text = text.replaceAll(entry.getKey(), entry.getValue());
		}
		//clean up punctuations
		for(Map.Entry<String, String> entry : puncMap.entrySet()){
			text = text.replaceAll(entry.getKey(), entry.getValue());
		}
		
		return text;
	}
	
	public String extractNegations(String text){				
		String [] words = text.split(" ");
		int state = 0;
		String newText = new String("");
		boolean begin = true;
		for(String word : words){
			//System.out.println(word);
			
			if(puncs.containsKey(word)){
				newText += " " + word;
			}
			else if(state == 1){
				newText += " " + "not" + word;
			}else if(state == 0 && begin){
				newText += word;
				begin = false;
			}else if(state == 0 && !puncs.containsKey(word)){
				newText += " " + word;
			}
			
			if(negators.containsKey(word) && state == 0){
				//entered negation state
				state = 1;
				//System.out.println("entered neg state!");
			}
			else if(puncs.containsKey(word) && state == 1){
				//exiting negation state
				state = 0;
				//System.out.println("exiting neg state!!");
			}
		}
		
		return newText;
	}
	
	public String removeStopWords(String text){
		String [] words = text.split(" ");
		boolean begin = true;
		String cleanText = new String("");
		for(String word:words){
			if(!stopWords.containsKey(word) && !word.isEmpty()){
				//System.out.println(word);
				if(begin){
					cleanText += word;
					begin = false;
				}
				else{
					cleanText += (" " + word);
				}
			}
		}
		return cleanText;
	}
}

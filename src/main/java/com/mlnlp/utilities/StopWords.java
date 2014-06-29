package com.mlnlp.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import com.mlnlp.twitter.TweetsConstants;

public class StopWords {
	private Hashtable<String, Integer> stopWords = new Hashtable<String, Integer >();
	
	public StopWords() throws IOException{
		parseStopWords();
	}
	
	private int parseStopWords() throws IOException{
		String word;
		BufferedReader in = new BufferedReader(new FileReader(TweetsConstants.STOP_WORDS_FILE_NAME));
		while ((word = in.readLine()) != null) {
			stopWords.put(word, 1);
		}
		in.close();
		return 0;
	}
	
	public boolean containsKey(String word){
		if(!stopWords.containsKey(word)){
			return false;
		}
		return true;
	}
}

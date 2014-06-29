package com.mlnlp.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.mahout.math.SparseMatrix;
import org.apache.mahout.math.Vector;

import com.mlnlp.utilities.TweetUtils;

public class LDA {
	private double alpha = 0.01;
	private double beta = 0.01;
	private int topicsNum;
	private SparseMatrix docTopicMatrix;
	private SparseMatrix wordTopicMatrix;
	private Map<Integer, Double> NK = new HashMap<Integer, Double>();
	private List<ArrayList<String>> documentMatrix = new ArrayList<ArrayList<String>>();
	private List<ArrayList<Integer>> currentState = new ArrayList<ArrayList<Integer>>();
	private Map<String, Integer> wordIndexMap = new HashMap<String, Integer>();
	private Map<Integer, String> wordIndexInverseMap = new HashMap<Integer, String>();	
	private int vocabSize = 0;
	private TweetUtils tweetUtil;
	
	public LDA(int topicsNum, List<String> documents) throws IOException{
		this.topicsNum = topicsNum;
		tweetUtil = new TweetUtils(true, false, true);
		//build document matrix: assuming we receive a clean version of tweets
		for(String doc : documents){
			ArrayList<String> document = new ArrayList<String>();
			ArrayList<Integer> state = new ArrayList<Integer>();
			//System.out.println(doc);
			String docWoSW = tweetUtil.cleanTweet(doc);	//remove stop words
			String tokens[] = docWoSW.split(" ");
			for(String token : tokens){
				document.add(token);
				state.add(-1);
				if(!wordIndexMap.containsKey(token)){					
					wordIndexMap.put(token, vocabSize);
					wordIndexInverseMap.put(vocabSize, token);
					vocabSize++;
				}
			}
			documentMatrix.add(document);
			currentState.add(state);
		}
		int documentMatrixCardinality[] = {topicsNum, documentMatrix.size()};
		docTopicMatrix = new SparseMatrix(documentMatrixCardinality);
		int wordTopicMatrixCardinality[] = {vocabSize, topicsNum};
		wordTopicMatrix = new SparseMatrix(wordTopicMatrixCardinality);
		//init NK
		for(int index = 0; index < topicsNum; index++){
			NK.put(index, 0.0);
		}
	}
	
	//debug purpose
	public void printtMatrix(){
		System.out.println("docTopicMatrix:");
		for(int row = 0; row < topicsNum; row++){
			for(int col = 0; col < documentMatrix.size(); col++){
				System.out.println(docTopicMatrix.get(row, col));
			}
		}
		System.out.println("wordTopicMatrix:");
		for(int row = 0; row < vocabSize; row++){
			for(int col = 0; col < topicsNum; col++){
				System.out.println(wordTopicMatrix.get(row, col));
			}
		}
	}
	
	public void resetMatrix(){
		for(int row = 0; row < topicsNum; row++){
			for(int col = 0; col < documentMatrix.size(); col++){
				docTopicMatrix.set(row, col, 0.0);
			}
		}
		for(int row = 0; row < vocabSize; row++){
			for(int col = 0; col < topicsNum; col++){
				wordTopicMatrix.set(row, col, 0.0);
			}
		}
	}
	
	//Gibbs Sampler
	private void runGibbsSampler(int docIndex){
		//printtMatrix();
		//System.out.println("doc index=" + docIndex);
		Random rand = new Random();
		ArrayList<String> doc = documentMatrix.get(docIndex);
		ArrayList<Integer> state = currentState.get(docIndex);
		int index = 0;
		for(String word : doc){
			int zij = state.get(index);	//current assignment for this word
			//System.out.println("current state of word " + word + " --> zij=" + zij);
			//System.out.println("word=" + word);
			int wordIndex = wordIndexMap.get(word);
			//System.out.println(word + "'s index = " + wordIndex);
			double u = rand.nextDouble();
			double[] Prob = new double[topicsNum + 1];
			Prob[0] = 0.0;
			for(int topicIndex = 1; topicIndex <= topicsNum; topicIndex++){
				//System.out.println("topic index=" + (topicIndex - 1));
				double Nkj = docTopicMatrix.get(topicIndex - 1, docIndex);
				//System.out.println("Nkj = " + Nkj);				
				double Nwk = wordTopicMatrix.get(wordIndex, topicIndex - 1);
				//System.out.println("Nwk = " + Nwk);
				//the following two lines have been changed to address performance issue (by introducing NK ds)
				//Vector vec = wordTopicMatrix.getColumn(topicIndex - 1);
				//double Nk = vec.zSum();
				double Nk = NK.get(topicIndex - 1);	//performance issue
				//System.out.println("Nk = " + Nk + ", new NK=" + Nk_new);
				if(zij == (topicIndex - 1)){
					//we need adjustments
					Nkj -= 1.0;
					Nwk -= 1.0;
					Nk -= 1.0;
				}
				Prob[topicIndex] = Prob[topicIndex - 1] + (Nkj + alpha)*(Nwk + beta)/(Nk + vocabSize*beta);
			}
			/*
			System.out.println("u=" + u);
			System.out.println("probability:");
			for(int topicIndex = 1; topicIndex <= topicsNum; topicIndex++){
				System.out.println(Prob[topicIndex]/Prob[topicsNum]);
			}
			*/
			for(int topicIndex = 1; topicIndex <= topicsNum; topicIndex++){
				if(u <= Prob[topicIndex]/Prob[topicsNum]){
					state.set(index, topicIndex - 1);
					//update Nkj, Nwk, and Nk
					double newNkj = docTopicMatrix.get(topicIndex - 1, docIndex);
					double newNwk = wordTopicMatrix.get(wordIndex, topicIndex - 1);
					double newNk = NK.get(topicIndex - 1);
					if(zij != (topicIndex - 1)){
						docTopicMatrix.set(topicIndex - 1, docIndex, (newNkj + 1.0));
						wordTopicMatrix.set(wordIndex, topicIndex - 1, (newNwk + 1.0));
						NK.put(topicIndex - 1, (newNk + 1.0));
						if(zij >= 0){
							double oldNkj = docTopicMatrix.get(zij, docIndex);
							double oldNwk = wordTopicMatrix.get(wordIndex, zij);
							double oldNk = NK.get(zij);
							docTopicMatrix.set(zij, docIndex, (oldNkj - 1.0));
							wordTopicMatrix.set(wordIndex, zij, (oldNwk - 1.0));
							NK.put(zij, (oldNk - 1.0));
						}
					}					
					break;	//done with this word
				}
			}
			index++;
		}
		/*
		System.out.println("state");
		for(int s : state){
			System.out.println(s);
		}
		*/
		currentState.set(docIndex, state);
	}
	
	public double getTopicWordProbability(String word, int topicIndex){
		int wordIndex = wordIndexMap.get(word);
		double Nwk = wordTopicMatrix.get(wordIndex, topicIndex);
		Vector vec = wordTopicMatrix.getColumn(topicIndex);
		double Nk = vec.zSum();
		
		return (Nwk + beta)/(Nk + vocabSize*beta);
	}
	
	public double getTopicDocProbability(int docIndex, int topicIndex){
		double Nkj = docTopicMatrix.get(topicIndex, docIndex);
		Vector vec = docTopicMatrix.getColumn(docIndex);
		double Nj = vec.zSum();
		
		System.out.println("Nkj=" + Nkj);
		System.out.println("Nj=" + Nj);
		
		return (Nkj + alpha)/(Nj + topicsNum*alpha);
	}
	
	public void printTopicTopWords(int topicIndex, int top){
		Vector vec = wordTopicMatrix.getColumn(topicIndex);
		int indexes[] = new int[top];
		Hashtable<Integer, Integer> indexMap = new Hashtable<Integer, Integer>();
		for(int index = 0; index < top; index++){
			int maxInx = -1;
			double max = 0.0;
			//System.out.println("max=" + max + ", maxInx=" + maxInx);
			for(int wordInx = 0; wordInx < vec.size(); wordInx++){
				//System.out.println("wordInx=" + wordInx + ", count=" + vec.get(wordInx));
				if(vec.get(wordInx) > max && !indexMap.containsKey(wordInx)){
					maxInx = wordInx;
					max = vec.get(wordInx);
				}
			}
			//System.out.println("top word#" + index);
			//System.out.println("top word index=" + maxInx);
			if(maxInx >= 0){
				indexMap.put(maxInx, 0);
				indexes[index] = maxInx;
			}
		}
		/*
		System.out.println("word index map:");
		for(int index = 0; index < vocabSize; index++){
			System.out.println(index + ":" + wordIndexInverseMap.get(index));
		}
		*/
		System.out.println("Top words for topic=" + topicIndex);
		for(int index = 0; index < top; index++){
			if(vec.get(indexes[index]) > 0){
				System.out.println(wordIndexInverseMap.get(indexes[index]) + ":" + getTopicWordProbability(wordIndexInverseMap.get(indexes[index]), topicIndex));
			}
		}
	}
	
	public void runLDA(int times){
		resetMatrix();
		for(int iteration = 1; iteration <= times; iteration++){
			for(int docIndex = 0; docIndex < documentMatrix.size(); docIndex++){
				runGibbsSampler(docIndex);
			}
			//System.out.println("iteration #" + iteration);
			//printtMatrix();
		}
	}
}

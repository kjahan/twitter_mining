package com.mlnlp.utilities;

import java.util.Map;
import java.util.TreeMap;

public class MapUtil {
	public static void printFrequentItems(TreeMap<String,Integer> aTreeMap, int threshold){
		System.out.println("frequency:");
		for (Map.Entry<String, Integer> entry : aTreeMap.entrySet()) {
		    String source = entry.getKey();
		    Integer counter = entry.getValue();
		    if(counter > threshold){
		    	System.out.println("item=" + source + ",counter=" + counter );
		    }
		}
	}
	
	public static int getNumberOfUniqueItems(TreeMap<String,Integer> aTreeMap){
		return aTreeMap.size();
	}
}

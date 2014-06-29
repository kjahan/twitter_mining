package com.mlnlp.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DayIntervals {
	public static List<Pair<String, String>> populateDates(String filename) throws IOException, ParseException{
		List<Pair<String, String>> dayIntervals = new ArrayList<Pair<String, String>>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line;
		String [] dates;
		String start = null, end = null;
		while ((line = in.readLine()) != null){
			dates = line.split(",");
			int cnt = 0;			
			for(String date : dates){
				if(cnt == 0){
					start = new String(date);
					//System.out.println(start);
					cnt++;
				}else{
					end = new String(date);
					//System.out.println(end);
				}				
			}
			Pair<String, String> datePair = new Pair<String, String>(start, end);
			dayIntervals.add(datePair);
		}
		in.close();
		return dayIntervals;
	}
}

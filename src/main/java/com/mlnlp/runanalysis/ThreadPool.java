package com.mlnlp.runanalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mlnlp.utilities.DayIntervals;
import com.mlnlp.utilities.Pair;

public class ThreadPool {
	public static void main(String[] args) throws IOException, ParseException{
		Date date = new Date();
		System.out.println(date.toString());
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Pair<String, String>> dayIntervals = DayIntervals.populateDates("resources/days.txt");
		for(Pair<String, String> datePair : dayIntervals){
			Runnable worker = new WorkerThread(datePair);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
		date = new Date();
		System.out.println(date.toString());
	}
}

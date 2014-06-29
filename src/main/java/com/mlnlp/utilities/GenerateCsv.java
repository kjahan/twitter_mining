package com.mlnlp.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GenerateCsv {
	public static void generateCsvFile(String FileName, List<String> data){
		try {
			FileWriter writer = new FileWriter(FileName);
			for(String element : data) {
				String[] parts = element.split(",");
				Date date_;
				date_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(parts[0]);
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date_);
				writer.append(new SimpleDateFormat("MMM-dd").format(cal.getTime()));
				for(int i = 1; i < parts.length; i++){
					Object value = parts[i];
				    writer.append(',');
				    writer.append(value.toString());
				}
				writer.append('\n');	
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

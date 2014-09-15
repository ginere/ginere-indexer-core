package eu.ginere.indexer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DateUtils {

	static final Logger log = Logger.getLogger(DateUtils.class);
	
	private static ThreadLocal<SimpleDateFormat> sdfTl=new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
    public static final Pattern DATE_PATER=Pattern.compile("^\\D*([0-9]{1,2})\\D*([0-9]{1,2})\\D*([0-9]{4})\\D*$");

	private static final int MAX_DAY_NUMBER = 31;
	private static final int MAX_MONTH_NUMBER = 12;
	private static final int MAX_YEAR_NUMBER = 3000;
	private static final int MIN_YEAR_NUMBER = 1800;
    
    public static Date get(String string){
		if (string==null){
			return null;
		}

		Matcher matcher=DATE_PATER.matcher(string);

		if (matcher.matches()){
			String dayValue=matcher.group(1);
			String monthValue=matcher.group(2);
			String yearVale=matcher.group(3);
			
			
			int day = Integer.valueOf(dayValue);
			if (day > MAX_DAY_NUMBER){
				return null;
			}

			int month = Integer.valueOf(monthValue);
			if (month > MAX_MONTH_NUMBER){
				return null;
			}

			int year = Integer.valueOf(yearVale);
			if (year > MAX_YEAR_NUMBER || year<MIN_YEAR_NUMBER){
				return null;
			}

			return get(day,month,year);
		} else {
			return null;			
		}
	}

	private static Date get(int day, int month, int year) {
		try {
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(year);
			buffer.append('-');
			buffer.append(month);
			buffer.append('-');
			buffer.append(day);
			
			return sdfTl.get().parse(buffer.toString());
		}catch (Exception e) {
			log.error("day:"+day+
					  " month:"+month+
					  " year:"+year
					  ,e);
			return null;
		}
	}
}

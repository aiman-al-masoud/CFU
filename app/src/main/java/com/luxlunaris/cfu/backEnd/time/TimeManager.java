package com.luxlunaris.cfu.backEnd.time;

import java.util.Date;

public class TimeManager {


	
	     //compare time strings in this format: hh:mm
		//+ve; time 1 greater (later)
		public static int compareTimes(String t1, String t2) {
			
			//get hour 1 and minute 1
			int h1 = Integer.parseInt(t1.split(":")[0].trim());
			int m1 = Integer.parseInt(t1.split(":")[1].trim());
			
			//get hour 2 and minute 2
			int h2 = Integer.parseInt(t2.split(":")[0].trim());
			int m2 = Integer.parseInt(t2.split(":")[1].trim());
			
			//compare hours
			if(h1-h2 !=0) {
				return h1-h2;
			}
			
			//compare minutes
			return m1-m2;
		}
		
		
		//check if a given time lies within a time-bracket 
		//time-bracket format: hh:mm-hh:mm
		public static boolean isTimeInInterval(String time, String interval) {
			
			String lowerBound = interval.split("-")[0].trim();
			String upperBound = interval.split("-")[1].trim();
			
			//if time is greater than lower bound and less than 
			//upper bound it's within the interval
			if(compareTimes(time, lowerBound)>=0 && compareTimes(time, upperBound)<=0) {
				return true;
			}
			
			return false;
		}
		
		
		//get current time in military format hh:mm
		public static String getCurrentTime() {
			Date date = new Date(System.currentTimeMillis());
			return date.getHours()+":"+date.getMinutes();
		}
		
		
		//convert name of dayOfTheWeek to int 
		public static int dayNameToInt(String dayOfTheWeekName) {
			return WESTERN_WEEK.valueOf(dayOfTheWeekName.toUpperCase()).ordinal();
		}
		
		//convert an integer to a name of the week
		public static String intToDayName(int dayOrdinal) {
			return WESTERN_WEEK.values()[dayOrdinal].toString();
		}
		
		
		
		
		
		
		
	
	
}

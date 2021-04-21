package com.luxlunaris.cfu.backEnd.dataModel;

import android.util.Log;

import com.luxlunaris.cfu.backEnd.fileIO.FileIO;
import com.luxlunaris.cfu.backEnd.time.TimeManager;
import com.luxlunaris.cfu.frontEnd.activities.TablesListActivity;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;



//CONSTRUCTOR: given a string of cells separated by newlines,
//the constructor calculates the dimensions of the time table, 
//and initializes the time table matrix with the input values.


//NB: day of the week ordinals start from 0

public class TimeTable {
	
	
	
	//this table's name/ID, corresponds to the faculty's ID
	public String name;
	
	//matrix that contains the period names as its cells
	//number of periods = rows of the matrix
	//number of days = columns of the matrix
	String[][] stringMatrix;
	
	//period time-brackets. .size() is the number of periods
	ArrayList<String> periodTimeBrackets;
	//total number of lessons .size()/num_of_periods is number of days of the week
	ArrayList<String> lessonsInAWeek;  
	
	//cells of this time table saved in the form of a string,
	//cells separated by newline chars
	String cells;
	
	
	public TimeTable(String name, String cells) {
		
		//set the cells string for eventual later use in a save
		this.cells = cells;
		
		
		//give this table a name
		this.name = name;
		
		//initialize lists
		periodTimeBrackets = new ArrayList<String>();
		lessonsInAWeek = new ArrayList<String>();
	
		
		//fill up the list of timebrackets and the list 
		//of lessons in a week
		for(String cell : cells.split("\n")) {
			
			//if this is a time-bracket, it means there's a new period
			if(cell.matches("\\d+:\\d+-\\d+:\\d+")) {
				periodTimeBrackets.add(cell);
			}else {
				//if it's not a time bracket it's a lesson, there's a new column (hence day)
				if(cell.equals("&nbsp;")){
					//if lesson name is html whitespace, add empty string in its place
					lessonsInAWeek.add("");
					continue;
				}
				lessonsInAWeek.add(cell);
			}
		}
		
		
		//initialize the string matrix:
		stringMatrix = new String[getNumPeriods()][getNumDays()];
		
		//fill up the matrix
		int periodsCounter = 0;
		int daysOfTheWeekCounter = 0;
		
		for(String lesson : lessonsInAWeek){
			
			if(periodsCounter>=getNumPeriods()) {
				//you're DONE if (by chance) the periodCounter exceeds 
				//the maximum computed number of periods for this table.
				break; 
			}
			
			//put an entry 
			stringMatrix[periodsCounter][daysOfTheWeekCounter] = lesson;
			//increment the days (columns)
			daysOfTheWeekCounter++;
			//if days reaches max days, reset it and increment periods (rows)
			if(daysOfTheWeekCounter>=getNumDays()) {
				daysOfTheWeekCounter = 0;
				periodsCounter++;
			}	
		}
		
	}
	

	
	//get the number of days (columns)
	public int getNumDays() {
		try{
			return lessonsInAWeek.size()/periodTimeBrackets.size();
		}catch (ArithmeticException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	
	//get the number of periods (rows)
	public int getNumPeriods() {
		return periodTimeBrackets.size();
	}
	
	
	//get the lesson in this table at a given period number
	//and day of the week
	public String getLessonAt(int periodNum, int dayOfTheWeekOrdinal) {
		try {
			return stringMatrix[periodNum][dayOfTheWeekOrdinal];
		}catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	//OVERLOAD
	//get the lesson in this table at a given time of the day and 
	//day of the week (case-insensitive name of the day as a string)
	public String getLessonAt(String timeOfTheDay, String dayOfTheWeek) {
		return getLessonAt(timeToPeriodNum(timeOfTheDay), TimeManager.dayNameToInt(dayOfTheWeek));
	}
	
	//given a time, find the time-bracket/interval it is in, 
	//and convert that into the corresponding periodNum.
	//Military time in this format: hh:mm
	public int timeToPeriodNum(String time) {
		
		for(int i =0; i< periodTimeBrackets.size(); i++) {
			if(TimeManager.isTimeInInterval(time, periodTimeBrackets.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	//get occupied classrooms at a given time of the day
	//and day of the week
	//Military time in format: hh:mm
	public ArrayList<String> getOccupiedClassroomsAt(String timeOfTheDay, String dayOfTheWeek){
		//System.out.println(getLessonAt(timeOfTheDay, dayOfTheWeek));
		return ClassroomManager.parseClassroomIDs(getLessonAt(timeOfTheDay, dayOfTheWeek));
	}
	
	//is a classroom busy by this faculty at a given time?
	public boolean isClassroomOccupiedAt(String classroomID, String timeOfTheDay, String dayOfTheWeek) {
		for(String occupiedClassroom : getOccupiedClassroomsAt(timeOfTheDay, dayOfTheWeek)) {
			if(occupiedClassroom.equals(classroomID)) {
				return true;
			}
		}
		return false;
	}
	
	
	//is a given period taught to people on this TimeTable?
	//doesn't require an exact match of the names
	public String getLesson(String lessonName) {
		for(String lesson : lessonsInAWeek) {
			if(lesson.toUpperCase().contains(lessonName.toUpperCase())) {
				return lesson;
			}
		}
		return null;
	}
	
	//get timings (time-brackets and daysOfTheWeek) for a given lesson
	public ArrayList<String> getTimingsFor(String lessonName){
		//timings the queried lesson is hosted at
		ArrayList<String> timings = new ArrayList<String>();
		//check if the lesson is contained in this TimeTable
		String lesson = getLesson(lessonName);
		//just return null if the lesson isn't hosted at this TimeTable
		if(lesson==null) {
			return null;
		}
		
		//
		for(int i = 0; i<getNumPeriods(); i++) {
			for(int j =0; j<getNumDays(); j++) {
				String lektion = getLessonAt(i, j);
				if(lektion.equals(lesson)) {
					timings.add(periodTimeBrackets.get(i)+" "+TimeManager.intToDayName(j));
				}
			}
		}
		
		return timings;
	}
	
	
	//save this TimeTable's cells in the timeTables directory of this project
	public void save() {
		FileIO.writeToFile(new File(FileIO.timeTablesDir+File.separator+name), cells);
	}
	
	//delete this TimeTable's cells file from the timeTables directory
	public void delete() {

		try{
			File toBeDeleted = new File(FileIO.timeTablesDir+File.separator+name);

			Log.d("TIMETABLE", toBeDeleted.getPath());



			boolean t = toBeDeleted.delete();
			Log.d("TIMETABLE", "time table got deleted? "+t);

		}catch (Exception e){
			e.printStackTrace();
		}


	}
	

	//get period interval
	public String getPeriodInterval(int periodNum){
		return periodTimeBrackets.get(periodNum);
	}

	
	//get this table's name
	public String getName() {
		return name;
	}
	
	//set this table's name
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}

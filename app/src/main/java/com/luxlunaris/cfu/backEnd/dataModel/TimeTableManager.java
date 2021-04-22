package com.luxlunaris.cfu.backEnd.dataModel;

import com.luxlunaris.cfu.backEnd.fileIO.FileIO;
import com.luxlunaris.cfu.backEnd.strings.MyStringUtils;

import org.jsoup.internal.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeTableManager {
	
	
	//each time table corresponds to a different faculty
	static ArrayList<TimeTable> listOfTimeTables = new ArrayList<TimeTable>();
	
	//this is a reference to the directory that contains a bunch of files
	//with the cells. One file for each faculty.
	static File timeTablesDir = FileIO.timeTablesDir;
	
	
	//make and load a new time table into the object
	//the cells are in a string, they're separated by a newline char
	public static TimeTable loadTimeTable(String tableName, String cells) {
		
		//make a new time table, feeding it its cells
		TimeTable newTimeTable = new TimeTable(tableName, cells);
		
		//put the newly generated time table into the list
		listOfTimeTables.add(newTimeTable);
		
		//return the newly created time table
		return newTimeTable;
	}


	
	//load all of the locally available time tables from the time tables directory
	public static void loadAllTimeTables() {


		try{
			//for each time table file in memory...
			for(File timeTableFile : timeTablesDir.listFiles()) {

				//get the text it contains (its cells)
				String cells = FileIO.readFile(timeTableFile);

				//create a new time table from the retrieved text
				loadTimeTable(timeTableFile.getName().split("\\.")[0], cells);
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}

		
	}
	
	//get a time table in the list
	public static TimeTable getTimeTable(String name) {
		//search for a time table with a given name
		for(TimeTable timeTable : listOfTimeTables) {
			if(timeTable.getName().equals(name)) {
				return timeTable;
			}
		}
		return null;
	}

	//get time tables whose name contains a bunch of keywords
	public static ArrayList<TimeTable> getRelevantTables(String keywords){
		ArrayList<TimeTable> results = new  ArrayList<TimeTable>();

		for(TimeTable timeTable : listOfTimeTables){
			if(MyStringUtils.containsKeywords(timeTable.getName(), keywords)){
				results.add(timeTable);
			}
		}
		return results;
	}





	//get all of the busy classrooms (and TimeTables that occupy them) at a given time on a given day
	//each class can be paired to MORE THAN ONE TABLE at a given instant of time.
	public static HashMap<String, ArrayList<TimeTable>> getBusyClassroomsAndTimeTablesAt(String time, String day){

		//key: classroomID; value: list of TimeTables
		HashMap<String, ArrayList<TimeTable>> busyClassrooms = new HashMap<String, ArrayList<TimeTable>>();

		//for each time table...
		for(TimeTable timeTable : listOfTimeTables) {

			//get the time table's busy classrooms...
			ArrayList<String> tablesBusyClassrooms = timeTable.getOccupiedClassroomsAt(time, day);

			//for each classroom you find in a table...
			for(String busyClassroom : tablesBusyClassrooms) {

				//if this classroom isn't already in the map, create a new tables list for that classroom
				if(!busyClassrooms.containsKey(busyClassroom)){
					busyClassrooms.put(busyClassroom, new ArrayList<TimeTable>());
				}

				//in any case, add the time table to the list of a classroom
				//but not twice!!
				boolean flag = true;
				for(TimeTable timeTable1 : busyClassrooms.get(busyClassroom)){
					if(timeTable1.getName().equals(timeTable.getName())){
						flag = false; //don't add duplicate table
						break;
					}
				}

				//add time table to list of a classroom
				if(flag){
					busyClassrooms.get(busyClassroom).add(timeTable);
				}

			}
		}

		return busyClassrooms;
	}



		//get all of the busy classrooms at a given time on a given day
	public static ArrayList<String> getBusyClassroomsAt(String time, String day){

		return new ArrayList<String>(getBusyClassroomsAndTimeTablesAt(time, day).keySet());

	}
	
	
	
	//get all of the free classrooms at a given time
	//free classrooms = all classrooms - busy classrooms
	public static ArrayList<String> getFreeClassroomsAt(String time, String day) {
		
		//free classrooms
		ArrayList<String> freeClassrooms = new ArrayList<String>();
		
		//busy classrooms
		ArrayList<String> busyClassrooms = getBusyClassroomsAt(time, day);
		
		//if it's not busy, it's (hopefully) free
		for(String classroom : ClassroomManager.getAllClassrooms()) {
			if(!busyClassrooms.contains(classroom)) {
				freeClassrooms.add(classroom);
			}
		}
		
		return freeClassrooms;
	}
	
	
	//get all of the timings for a lesson
	public static HashMap<TimeTable, ArrayList<String>> getLessonTimings(String lessonName) {
		
		//key: timetable, value: timings for that lesson in that table
		HashMap<TimeTable, ArrayList<String>> tablesAndTimings = new HashMap<TimeTable, ArrayList<String>>();
		
		//get all of the tables and for each one get the timings of lessonName
		for(TimeTable timeTable : listOfTimeTables) {
			ArrayList<String> timings = timeTable.getTimingsFor(lessonName);
			if(timings!=null) {
				tablesAndTimings.put(timeTable, timings);
			}
		}
		
		return tablesAndTimings;
	}
	
	
	//make a time table, and save its cells to storage
	public static TimeTable makeAndSaveTable(String tableName, String cells) {
		TimeTable timeTable = loadTimeTable(tableName, cells);
		timeTable.save();
		return timeTable;
	}

	//OVERLOAD
	//make and save and LOAD table
	public static TimeTable makeAndSaveTable(String tableName, String cells, boolean load) {

		TimeTable timeTable;
		if(load){
			timeTable  = loadTimeTable(tableName, cells);
		}else{
			timeTable = makeAndSaveTable(tableName, cells);
		}

		return timeTable;
	}


		//OVERLOAD
	//delete a time table given its name
	public static void deleteTable(String tableName) {		
		//get the table to be removed
		TimeTable tableToBeRemoved = getTimeTable(tableName);
		try {
			deleteTable(tableToBeRemoved);
		}catch(NullPointerException e) {
			//table does not exist
		}
	}


	//delete time table
	public static void deleteTable(TimeTable timeTable) {
		timeTable.delete();
		listOfTimeTables.remove(timeTable);
	}


	

	//get all of the available time tables
	public static ArrayList<TimeTable> getAllTimeTables(){
		if(listOfTimeTables.size()==0) {
			loadAllTimeTables();
		}
		return listOfTimeTables;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

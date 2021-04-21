package com.luxlunaris.cfu.backEnd.dataModel;

import android.util.Log;

import com.luxlunaris.cfu.backEnd.fileIO.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;



public class ClassroomManager {
	
	
	//By default a valid classroomID has: 
	//letters first, numbers second, with no spaces in between.
	public static String classroomIDFormat = "[a-zA-Z]+\\d+";
	
	//list of ALL classrooms that got mentioned in the cells of all of the tables
	public static HashMap<String, String> allClassroomIDs = new HashMap<String, String>();
	
	

	//finds alphanumeric classroom IDs in a string 
	public static ArrayList<String> parseClassroomIDs(String string) {
		
		//if the input string to be parsed is null, return an empty list
		if(string==null) {
			return new ArrayList<>();
		}
		
		
		//results: classroomIDs found. Tmply stored 
		//in a HashMap to avoid duplicate values
		HashMap<String, String> results = new HashMap<String, String>();
		
		//buffer that stores a single classroomID in the making
		String buf = "";
		
		//for each character...
		for(int i =0; i<string.length(); i++) {
			
			//keep building a perceived classroomID
			buf+=string.charAt(i); 
			
			
			//add to the results list if the resulting 
			//classroomID is in the (valid) form of:
			if(buf.matches(classroomIDFormat)) {
				
				//...if it's not there yet, add it
				results.put(buf, buf);
			}
			
			
			//when you find a space: re-initialize the buffer
			if(string.charAt(i)==' ') {
				buf = "";
			}
			
		}
		
		return new ArrayList<String>(results.keySet());
	}
	
	
	

	public static void loadSavedClassroomIDs() {
		//load all of the classroom IDs from the classrooms.txt file
		for(String classroom : FileIO.readFile(FileIO.classroomsFile).split("\n")) {
			addIDs(classroom);
		}
	}



	//load all of the classroomIDs
	//NB: this operation is meant to be done ONCE,
	//to avoid performance issues. Frequent parsing of these files
	//to find new classes isn't called for, since MANY new classes are seldom
	//built during runtime.
	//parse all of the tables for classroom IDs and save them to a file for later
	public static void parseAllTablesForClassroomIDs(){
		//parse and load all of the classroom IDs from the tables
		for(File timeTableFile : FileIO.timeTablesDir.listFiles()) {

			//get the text of the files (cells)
			String cells = FileIO.readFile(timeTableFile);

			//add the newly parsed IDs to the list (actually map)
			addIDs(cells);
		}


		//save the parsed IDs
		saveClassroomIDs();

	}

	
	//save currently loaded classroom IDs to a designated text file
	public static void saveClassroomIDs() {
		ArrayList<String> classroomsList = getAllClassrooms();
		FileIO.writeToFile(FileIO.classroomsFile, classroomsList);
	}



	
	
	
	
	
	//parse and add IDs to the overall set of classroomIDs
	public static void addIDs(String cells) {
		//parse the text for new classroom IDs
		ArrayList<String> newClassroomIDs = parseClassroomIDs(cells);
		
		//add each element to the global allClassroomIDs list 
		//only if it doesn't appear there already (no duplicates).
		for(String newClassroomID : newClassroomIDs) {
			allClassroomIDs.put(newClassroomID, newClassroomID);
		}
	}
	
	
	//check if classrooms have been loaded
	private static boolean areClassroomsLoaded() {
		//check if classrooms have been loaded 
		if(allClassroomIDs.size()==0) {
		     //if not load them:
			loadSavedClassroomIDs();
			return false;
		}
		
		return true;
	}
	
	
	
	//get a list of all of the classrooms
	public static ArrayList<String> getAllClassrooms(){
		
		//check if classrooms are loaded
		areClassroomsLoaded();
		
		//return a list without duplicates
		return new ArrayList<String>(allClassroomIDs.keySet());
	}
	
	
	//get number of classrooms
	public static int getNumClassrooms() {
		
		//check if classrooms are loaded
		areClassroomsLoaded();
		
		return getAllClassrooms().size();
	}
	
	
	
	
	
}

package com.luxlunaris.cfu.backEnd.fileIO;

import com.luxlunaris.cfu.frontEnd.activities.TablesListActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileIO {

	//this is the project's rootDir
	public static File rootDir = TablesListActivity.rootDir;
	
	//this folder contains all of the resources of this project
	public static File resDir = new File(rootDir.getPath()+File.separator+"res");
		
	//this folder contains all of the time table resources 
	public static File timeTablesDir = new File(resDir.getPath()+File.separator+"timeTables");

	//this folder contains the classrooms file
	public static File classroomsDir = new File(resDir.getPath()+File.separator+"classrooms");

	//this file contains a list of all of the saved classroom IDs
	public static File classroomsFile = new File(classroomsDir.getPath()+File.separator+"classrooms.txt");

	//this folder contains a bunch of global settings files
	public static File settingsDir = new File(rootDir.getPath()+File.separator+"settings");

	//this file contains the default link to download the tables from
	public static File homepageLinkFile = new File(settingsDir.getPath()+File.separator+"homepageLink.txt");

	//this file contains the default filters to filter out irrelevant links
	public static File linkFilterFile = new File(settingsDir.getPath()+File.separator+"linkFilter.txt");
	
	//read string from file
	public static String readFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buf=null;
			String result ="";
			while((buf= reader.readLine())!= null) {
				result+=buf+"\n";
			}
			reader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	//write string to file
	public static void writeToFile(File file, String string) {
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(string);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//write a list of strings to a file. Separate items of list by a newline.
	public static void writeToFile(File file, ArrayList<String> stringsList) {
		
		//convert the list of strings to a string. Each item on a line.
		String buf = "";
		for(String string : stringsList) {
			buf+=string+"\n";
		}
		
		//write the buf to the file
		writeToFile(file, buf);
	}


	//create files and folders if they don't exist yet
	public static void createAllFiles(){

		resDir.mkdir();
		timeTablesDir.mkdir();
		classroomsDir.mkdir();
		settingsDir.mkdir();

		try{
			classroomsDir.mkdir();classroomsFile.createNewFile();
			homepageLinkFile.createNewFile();
			linkFilterFile.createNewFile();
		}catch (IOException e){

		}


	}

	//create file/folder if it doesn't exist
	public static void createIfInexistant(File file){
		if(file.exists()){
			return;
		}

		if(file.isDirectory()){
			file.mkdir();
			return;
		}

		try {
			file.createNewFile();
			FileIO.writeToFile(file, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	

}

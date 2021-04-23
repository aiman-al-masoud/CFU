package com.luxlunaris.cfu.backEnd.dataFetcher;

import android.os.AsyncTask;
import android.util.Log;

import com.luxlunaris.cfu.backEnd.dataModel.TimeTableManager;
import com.luxlunaris.cfu.backEnd.fileIO.FileIO;
import com.luxlunaris.cfu.frontEnd.activities.TablesListActivity;
import com.luxlunaris.cfu.frontEnd.fragments.MessageFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlTableParser {



	//this returns a list of cells. Each group is headed by a time-bracket,
	//which is followed by a list of subjects, then another time-bracket,
	//then another list of subjects... 
	public static ArrayList<String> getCellsOfTable(String webpage){
		
		//this array list will contain the relevant cells of the parsed table
		ArrayList<String> results = new ArrayList<String>();
		
		//this tells me when to start adding the cells, and when to stop
		boolean doAdd = false;
		
		try {


			//get ALL of the cells from the table
			Document doc = Jsoup.connect(webpage).get();

			//get table's title
			try{
				Elements elements = doc.select("h1");
				for(Element element : elements){


					String string = element.toString();
					Log.d("HEADER",string);


					Pattern pattern = Pattern.compile(">(.*?)<");
					Matcher matcher = pattern.matcher(string);
					matcher.find();
					string = matcher.group(1);
					string = string.replace("&nbsp;","-");
					string = string.replace("'","");

					Log.d("HEADER",string);
                    //add it as the title
					results.add("title:"+string);

				}

			}catch (Exception e){
				e.printStackTrace();
			}




			//for each cell...
			for(Element cell : doc.select("td")) {

				//turn cell Element into a string
				String cellString = cell.toString();

				//get the text content (non-html stuff)
				Pattern pattern = Pattern.compile("\">(.*?)</td");
				Matcher matcher = pattern.matcher(cellString);
				matcher.find();
				String usefulPartOfCell = matcher.group(1).trim();
				
				//only start adding the cells once you reach the first timing
				if(usefulPartOfCell.matches("\\d+:\\d+-\\d+:\\d+")) {
					doAdd = true;
				}
				
				
				//only add if doAdd is true, and if it's non blank
				if(doAdd && !usefulPartOfCell.trim().isEmpty()) {
					results.add(usefulPartOfCell);
				}
				
			}


			
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//if any problem occurs, return a null list
		return null;
	}
	
	
	
	//download all of the time tables from a mainpage and save them to 
	//this project's dedicated directory
	public static void downloadTimeTables() {

		new AsyncTask(){

			@Override
			protected Object doInBackground(Object[] objects) {

				//get homepage
				String mainPage = LinkGetter.getHomepage();

				//get all of the links
				ArrayList<String> timeTableLinks = LinkGetter.getLinksSelect(mainPage, LinkGetter.getLinkFilters());

				//for each link, download and store the time table.
				for(String link :timeTableLinks) {

					//tell UI what link you got to
					try{
						publishProgress(link);
					}catch (Exception e){

					}


					//clean the link (get rid of the surrounding Html)
					String cleanedLink = LinkGetter.cleanLink(link);

					//get the cells of this table
					ArrayList<String> cellsOfTable = getCellsOfTable(cleanedLink);

					//convert cells of table list to a string
					String cellsBuf = "";
					for (String cell : cellsOfTable) {
						cellsBuf += cell + "\n";
					}

					//make a new table name:

					//get the final part after the slashes
					String tableName = cleanedLink.split("/")[cleanedLink.split("/").length - 1];

					//get rid of the extension
					try {
						tableName = tableName.split("\\.")[0];
					} catch (Exception e) {
						e.printStackTrace();
					}


					//save the time table to memory
					TimeTableManager.makeAndSaveTable(tableName, cellsBuf);
				}


				return null;
			}

			@Override
			protected void onProgressUpdate(Object[] values) {


				Log.d("PROGRESS_UPDATE", values[0].toString());
				TablesListActivity.tablesListActivity.displayMessageDialog("fetching table at: "+values[0].toString());
				super.onProgressUpdate(values);
			}


		}.execute();

	}






	
	
	
	
	
	
	
	
	
	

}

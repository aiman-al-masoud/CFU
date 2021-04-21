package com.luxlunaris.cfu.backEnd.dataFetcher;

import com.luxlunaris.cfu.backEnd.fileIO.FileIO;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LinkGetter {
	
	
	//default keyword that filters out useless links
	public static String defaultFilterKeyword = "sem/classi";

	//default homepage to fetch links from
	public static String defaultHomepage = "http://webing.unipv.eu/didattica/orario-lezioni/orario-lezioni-2-semestre/";



	//get all of the links on a page
	public static ArrayList<String> getLinksFrom(String mainPage){
		ArrayList<String> links = new ArrayList<String>();

		try {

			//get the page's doc
			Document doc = Jsoup.connect(mainPage).get();

			//select links, and save each one of them
			for(Element e : doc.select("a")) {
				links.add(e.toString());
			}
			return links;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	//filter the links by a keyword
	public static ArrayList<String> getLinksSelect(String mainPage, String filterKeyword){
		
		//get all of the links on the page
		ArrayList<String> allLinks = getLinksFrom(mainPage);
		
		//relevant links
		ArrayList<String> relevantLinks = new ArrayList<String>();
		
		//pick only the relevant links: ie only the
		//links that contain all of the keywords enumerated
		//in the filter string
		String keywords = getLinkFilters();
		for(String link : allLinks) {

			boolean containsAll = true;
			for(String keyword : keywords.split("\\s+")){
				if(!link.contains(keyword)){
					containsAll = false;
					break;
				}
			}

			if(containsAll == true) {
				relevantLinks.add(link);
			}

		}
		return relevantLinks;
	}
	
	
	//clean a link (remove part that isn't part of the link)
	public static String cleanLink(String link) {
		int start = link.indexOf("<a href=\"")+"<a href=\"".length();
		int end = link.indexOf("\">");
		
		return link.substring(start, end);
	}
	
	//get link label
	public static String getLinkLabel(String link) {
		int start = link.indexOf("\">")+2;
		int end = link.indexOf("</a>");
		
		return link.substring(start, end);
	}





	//set homepage to fetch links from
	public static void setHomepage(String link){
		FileIO.writeToFile(FileIO.homepageLinkFile, link);
	}

	//get homepage to fetch links from
	public static String getHomepage(){


		String homepage = FileIO.readFile(FileIO.homepageLinkFile);
		if(homepage.trim().isEmpty()){
			//if it's empty, return a hardcoded default homepage
			return defaultHomepage;
		}
		return homepage;
	}

	//set link filter
	public static void setLinkFilters(String linkFilters){
		FileIO.writeToFile(FileIO.linkFilterFile, linkFilters);
	}

	//get link filter
	public static String getLinkFilters(){
		String linkFilter = FileIO.readFile(FileIO.linkFilterFile);
		if(linkFilter.trim().isEmpty()){
			return defaultFilterKeyword;
		}
		return linkFilter;
	}
	
	
	
	
	

}

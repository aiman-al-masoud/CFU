package com.luxlunaris.cfu.backEnd.strings;

public class MyStringUtils {


    public static boolean containsKeyword(String string, String keyword){
        //convert both to upper case
        keyword = keyword.toUpperCase();
        string = string.toUpperCase();

        if(string.contains(keyword)){
            return true;
        }
        return false;
    }

    //should contain ALL keywords (ANDed)
    //keywords separated by whitespace
    public static boolean containsKeywords(String string, String keywords){
        //convert both to upper case
        keywords = keywords.toUpperCase();
        string = string.toUpperCase();

        for(String keyword : keywords.split("\\s+")){
            if(!containsKeyword(string, keyword)){
                return false;
            }
        }
        return true;
    }





}

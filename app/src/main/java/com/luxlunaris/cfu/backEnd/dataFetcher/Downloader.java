package com.luxlunaris.cfu.backEnd.dataFetcher;

import android.content.Intent;
import android.os.AsyncTask;

import com.luxlunaris.cfu.backEnd.dataModel.ClassroomManager;
import com.luxlunaris.cfu.frontEnd.activities.TablesListActivity;
import com.luxlunaris.cfu.frontEnd.fragments.MessageFragment;

public class Downloader {

    public static void downloadAndAnalyze(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                //download all of the tables from the specified homepage
                HtmlTableParser.downloadTimeTables();
                //parse all of the downloaded tables for classroomIDs
                ClassroomManager.parseAllTablesForClassroomIDs();
                //TODO: put a progress bar or something


                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                TablesListActivity.tablesListActivity.startActivity(new Intent(TablesListActivity.tablesListActivity, TablesListActivity.class));
                super.onPostExecute(o);
                new MessageFragment("done").show(TablesListActivity.tablesListActivity.getSupportFragmentManager(), "show message");

            }
        }.execute();

    }



}

package com.luxlunaris.cfu.frontEnd.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataFetcher.HtmlTableParser;
import com.luxlunaris.cfu.backEnd.dataModel.ClassroomManager;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTable;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTableManager;
import com.luxlunaris.cfu.backEnd.fileIO.FileIO;
import com.luxlunaris.cfu.frontEnd.fragments.ListItemFragment;
import com.luxlunaris.cfu.frontEnd.fragments.MessageFragment;
import com.luxlunaris.cfu.frontEnd.fragments.YesOrNoPrompt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

//this is the main activity, it displays a
//list of all of the available tables
public class TablesListActivity extends AppCompatActivity implements YesOrNoPrompt.YesOrNoPromptListener {

    //refrence to this Activity
    public static TablesListActivity tablesListActivity;

    //root dir
    public static File rootDir;

    //map of currently displayed items (fragments). Each fragment maps to a TimeTable.
    HashMap<TimeTable, ListItemFragment> itemsOnDisplay = new HashMap<TimeTable, ListItemFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set autoreference
        tablesListActivity = this;
        //get root directory
        rootDir = getFilesDir();
        //create files if they don't exist yet
        FileIO.createAllFiles();


        //set the classrooms FAB's action
        FloatingActionButton analyzeFAB = findViewById(R.id.analyzeFAB);
        analyzeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the classrooms activity
                startActivity(new Intent(TablesListActivity.tablesListActivity, TablesAnalyzerActivity.class));
            }
        });

        //set the setting's FAB's action
        FloatingActionButton settingsFAB = findViewById(R.id.settingsFAB);
        settingsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the classrooms activity
                startActivity(new Intent(TablesListActivity.tablesListActivity, SettingsActivity.class));
            }
        });

        //set the download and analyze fab's action
        FloatingActionButton downloadAndAnalyzeFAB = findViewById(R.id.downloadAndAnalyzeFAB);
        downloadAndAnalyzeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask user if they reeeeally wanna do this (again)
                YesOrNoPrompt yesOrNoPrompt = new YesOrNoPrompt("  re-download and\n  re-analyze time tables?", "DOWNLOAD_TABLES", TablesListActivity.tablesListActivity);
                yesOrNoPrompt.show(getSupportFragmentManager(), "to redownload or not to redownload...");
            }
        });

        //set the options menu fab's action
        FloatingActionButton optionsMenuFAB = findViewById(R.id.optionsMenuFAB);
        optionsMenuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu optionsMenu = new PopupMenu(TablesListActivity.tablesListActivity, optionsMenuFAB);
                optionsMenu.getMenuInflater().inflate(R.menu.list_item_menu, optionsMenu.getMenu());

                //set menu items' actions
                optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.deleteSelection:
                                for(ListItemFragment selectedItem : getSelectedItems()){
                                    remove(selectedItem.getTimeTable());
                                }
                                break;


                        }


                        return true;
                    }
                });


                optionsMenu.show();
            }
        });









        //add all of the available time tables
        addAll();


    }

    //add a table to the screen and to the list of currently displayed items
    public ListItemFragment add(TimeTable timeTable){
        //make a new list item
        ListItemFragment newListItem = new ListItemFragment(timeTable);

        //add the new list item to the list of currently stored items
        itemsOnDisplay.put(timeTable, newListItem);

        //add the new list item to the screen
        getSupportFragmentManager().beginTransaction().add(R.id.tablesListLinearLayout, newListItem, null).commit();

        //add it to the set of saved tables if it's not there yet
        timeTable.save();

        //return the new list item
        return newListItem;
    }

    //remove a time table (ie: the corresponding list item) from the display. And delete the TimeTable from memory.
    public void remove(TimeTable timeTable){


        //get the list item
        ListItemFragment listItemFragment = itemsOnDisplay.get(timeTable);
        //remove it
        getSupportFragmentManager().beginTransaction().remove(listItemFragment).commit();
        //remove it from list
        itemsOnDisplay.remove(timeTable);
        //delete the time table from storage
        TimeTableManager.deleteTable(timeTable);


    }

    //add all of the available time tables
    public void addAll(){
        //get all of the time tables from the TimeTableManager
        ArrayList<TimeTable> timeTables = TimeTableManager.getAllTimeTables();
        //add an item for each time table
        for(TimeTable timeTable : timeTables){
            add(timeTable);
        }
    }

    //get all of the currently selected items
    public ArrayList<ListItemFragment> getSelectedItems(){
        ArrayList<ListItemFragment> selectedItems = new ArrayList<>();
        for(TimeTable timeTable : itemsOnDisplay.keySet()){
            if(itemsOnDisplay.get(timeTable).isSelected()){
                selectedItems.add(itemsOnDisplay.get(timeTable));
            }
        }
        return selectedItems;
    }



    //implements the YesOrNoPrompt listener interface
    @Override
    public void onDecisionTaken(boolean yesOrNo, String tag) {
        switch (tag){
            case "DOWNLOAD_TABLES":
                if(yesOrNo){
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

                            startActivity(new Intent(TablesListActivity.tablesListActivity, TablesListActivity.class));
                            super.onPostExecute(o);
                            new MessageFragment("done").show(getSupportFragmentManager(), "show message");

                        }
                    }.execute();
                }
                break;


        }


    }






}
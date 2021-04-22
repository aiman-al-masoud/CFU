package com.luxlunaris.cfu.frontEnd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataFetcher.Downloader;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTable;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTableManager;
import com.luxlunaris.cfu.backEnd.fileIO.FileIO;
import com.luxlunaris.cfu.frontEnd.fragments.ListItemFragment;
import com.luxlunaris.cfu.frontEnd.fragments.TextEditorFragment;
import com.luxlunaris.cfu.frontEnd.fragments.YesOrNoPrompt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

//this is the main activity, it displays a
//list of all of the available tables
public class TablesListActivity extends AppCompatActivity implements YesOrNoPrompt.YesOrNoPromptListener, TextEditorFragment.TextEditorListener {

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
    public void delete(TimeTable timeTable){

        //remove time table's item from display
        removeFromDisplay(timeTable);
        //delete the time table from storage
        TimeTableManager.deleteTable(timeTable);

    }

    //remove item from display
    public void removeFromDisplay(TimeTable timeTable){
        //get the list item
        ListItemFragment listItemFragment = itemsOnDisplay.get(timeTable);
        //remove it
        getSupportFragmentManager().beginTransaction().remove(listItemFragment).commit();
        //remove it from list
        itemsOnDisplay.remove(timeTable);
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


    //remove irrelevant time tables
    public void removeIrrelevantTables(ArrayList<TimeTable> relevantTables){

        //get all of the tables on display
        ArrayList<TimeTable> timeTablesOnDisplay = new ArrayList<TimeTable>(itemsOnDisplay.keySet());

        //for each table on display...
        for(TimeTable timeTable : timeTablesOnDisplay){

            //check if it's relevant...
            boolean relevant = false;
            for(TimeTable relevantTable : relevantTables){

                if(relevantTable.getName().equals(timeTable.getName())){
                    relevant = true;
                    break;
                }
            }

            //if it's not, remove it from display
            if(!relevant){
                removeFromDisplay(timeTable);
            }

        }

    }



    //implements the YesOrNoPrompt listener interface
    @Override
    public void onDecisionTaken(boolean yesOrNo, String tag) {
        switch (tag){
            case "DOWNLOAD_TABLES":
                if(yesOrNo){
                    Downloader.downloadAndAnalyze();
                }
                break;
        }


    }


    @Override
    public void onTextEntered(String text, String tag) {
        switch (tag){
            case "CREATE_NEW_TABLE":
                TimeTable timeTable = TimeTableManager.makeAndSaveTable(text.split("\n")[0], text);
                add(timeTable);
                break;
            case "SEARCH_FOR_TABLES":
                ArrayList<TimeTable> relevantTables = TimeTableManager.getRelevantTables(text);
                removeIrrelevantTables(relevantTables);
                break;


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.goToSettingsToolbarButton:
                startActivity(new Intent(TablesListActivity.tablesListActivity, SettingsActivity.class));
                break;
            case R.id.optionsToolbarButton:
                PopupMenu optionsMenu = new PopupMenu(TablesListActivity.tablesListActivity, findViewById(R.id.optionsToolbarButton));
                optionsMenu.getMenuInflater().inflate(R.menu.list_item_menu, optionsMenu.getMenu());

                //set menu items' actions
                optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.deleteSelection:
                                for(ListItemFragment selectedItem : getSelectedItems()){
                                    delete(selectedItem.getTimeTable());
                                }
                                break;
                            case R.id.newUserDefinedTimeTable:
                                TextEditorFragment textEditorFrag = new TextEditorFragment(TablesListActivity.this, "CREATE_NEW_TABLE");
                                textEditorFrag.setPredefinedText("title:myTitle\n9:30-10:50\nmondaysPeriod\ntuesdaysPeriod\nwednesdaysPeriod\nthursdaysPeriod\nfridaysPeriod\n10:50-12:00\nmondaysPeriod2\ntuesdaysPeriod2\nwednesdaysPeriod2\nthursdaysPeriod2\nfridaysPeriod2");
                                textEditorFrag.show(getSupportFragmentManager(), "textEditorFrag create new table");
                                break;
                        }

                        return true;
                    }
                });

                optionsMenu.show();
                break;
            case R.id.downloadTablesToolbarButton:
                //ask user if they reeeeally wanna do this (again)
                YesOrNoPrompt yesOrNoPrompt = new YesOrNoPrompt("  re-download and\n  re-analyze time tables?", "DOWNLOAD_TABLES", TablesListActivity.tablesListActivity);
                yesOrNoPrompt.show(getSupportFragmentManager(), "to redownload or not to redownload...");
                break;
            case R.id.searchForTablesToolbarButton:
                TextEditorFragment textEditorFrag = new TextEditorFragment(TablesListActivity.this, "SEARCH_FOR_TABLES");
                textEditorFrag.show(getSupportFragmentManager(), "textEditorFrag search for tables");
                break;

        }

        return true;
    }




}
package com.luxlunaris.cfu.frontEnd.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataModel.ClassroomManager;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTable;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTableManager;
import com.luxlunaris.cfu.frontEnd.fragments.ListItemFragment;
import com.luxlunaris.cfu.frontEnd.fragments.MessageFragment;

import java.util.ArrayList;
import java.util.HashMap;

//this activity represents more of the analytical part of the utility.

public class TablesAnalyzerActivity extends AppCompatActivity {


    public static TablesAnalyzerActivity tablesAnalyzerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables_analyzer);

        tablesAnalyzerActivity =this;

        //get time input field
        EditText timeInputFiled = findViewById(R.id.timeInputField);
        //get day input field
        EditText dayInputFiled = findViewById(R.id.dayOfTheWeekInputField);
        //get course name input field
        EditText courseNameFiled = findViewById(R.id.courseNameField);


        //set the get free/busy classrooms action
        Button getFreeClassroomsButton = findViewById(R.id.getFreeClassroomsButton);
        getFreeClassroomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get time of the day and day of the week
                String time = timeInputFiled.getText().toString();
                String day = dayInputFiled.getText().toString();


                //list of sub-fragments to be displayed on the root dialog fragment
                ArrayList<Fragment> subFragments = new ArrayList<Fragment>();

                //add a "free classrooms" title
                subFragments.add(new MessageFragment("FREE CLASSROOMS:"));

                //get the free classrooms
                ArrayList<String> freeClassrooms =  TimeTableManager.getFreeClassroomsAt(time , day);

                //add the free classroom fragments
                for(String freeClassroom : freeClassrooms){
                    subFragments.add(new MessageFragment(freeClassroom));
                }

                //add a "busy classrooms" title
                subFragments.add(new MessageFragment("BUSY CLASSROOMS:"));

                //get map of busy classroom-to-listOfTimeTables
                HashMap<String, ArrayList<TimeTable>> classroom_ListOfTables = TimeTableManager.getBusyClassroomsAndTimeTablesAt(time, day);

                //for each busy classroom...
                for(String classroom : classroom_ListOfTables.keySet()){

                    //add a classroom "label" frag
                    subFragments.add( new MessageFragment(classroom));

                    //add a fragment for each table in a classroom's tables list
                    for(TimeTable timeTable : classroom_ListOfTables.get(classroom)){
                        subFragments.add(new ListItemFragment(timeTable));
                    }

                }


                MessageFragment rootFrag = new MessageFragment(subFragments);

                rootFrag.show(getSupportFragmentManager(), "show it!");

            }
        });

        //set the get course timings action
        Button getCourseTimingsButton = findViewById(R.id.getCourseTimingsButton);
        getCourseTimingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the inputted course name
                String courseName = courseNameFiled.getText().toString();
                //call the TimeTableManager to get a map of TimeTables to timingsList
                HashMap<TimeTable, ArrayList<String>> tablesAndTimings = TimeTableManager.getLessonTimings(courseName);

                //for each time table prepare a list item fragment
                ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                for(TimeTable timeTable : tablesAndTimings.keySet()){

                    //prepare timings string
                    String buf = "";
                    for(String timing : tablesAndTimings.get(timeTable)) {
                        buf += timing + "\n";
                    }

                    //prepare list item fragment
                    fragments.add(new ListItemFragment(timeTable, buf));

                }

                //display all of the fragments on a message fragment
                new MessageFragment(fragments).show(getSupportFragmentManager(), "show message");




            }
        });



    }


}


package com.luxlunaris.cfu.frontEnd.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTable;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTableManager;
import com.luxlunaris.cfu.backEnd.time.TimeManager;

public class TableViewActivity extends AppCompatActivity {


    //global reference to this activity
    public static TableViewActivity tableViewActivity;


    //the view that is gonna display the table in html form
    static TableLayout tableLayout;

    //currently displayed table
    static TimeTable currentTimeTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        //set global reference
        tableViewActivity = this;

        //get the view that is gonna display the table in html form
        tableLayout =  findViewById(R.id.tableLayout);

        tableLayout.setLayoutParams( new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }





    @Override
    protected void onPostResume() {
        super.onPostResume();

        //the width of each column in pixels
        int columnWidth = 400;

        //color coding with alternating colors for alternating rows
        int alternatingColors[] = {Color.BLACK, Color.BLUE};


        //initialize days' row
        TableRow daysRow = new TableRow(TableViewActivity.tableViewActivity);

        //empty space above to the left
        TextView textView = new TextView(TableViewActivity.tableViewActivity);
        textView.setWidth(columnWidth);

        //add empty space
        daysRow.addView(textView);

        //fill up days' row with days
        for(int i=0; i<currentTimeTable.getNumDays(); i++){
            TextView text = new TextView(TableViewActivity.tableViewActivity);
            text.setWidth(columnWidth);
            text.setTextColor(Color.RED);
            text.setText(TimeManager.intToDayName(i));
            daysRow.addView(text);
        }
        //add days' row as the initial row
        tableLayout.addView(daysRow);


        //add all of the periods' rows (all starting with the period interval cell)
        for(int i =0; i<currentTimeTable.getNumPeriods(); i++){

            //make a periods row
            TableRow tableRow = new TableRow(TableViewActivity.tableViewActivity);

            //pick the color of the row alternatingly
            int rowColor = alternatingColors[i%2];

            //add the first cell of the period row (period interval)
            TextView periodInterval = new TextView(TableViewActivity.tableViewActivity);
            periodInterval.setWidth(columnWidth);
            periodInterval.setText(currentTimeTable.getPeriodInterval(i));

            periodInterval.setTextColor(rowColor);

            tableRow.addView(periodInterval);

            //add all of the lessons for this period (row)
            for(int j=0; j<currentTimeTable.getNumDays(); j++){
                TextView text = new TextView(TableViewActivity.tableViewActivity);
                text.setWidth(columnWidth);
                //text.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 20));

                text.setText(currentTimeTable.getLessonAt(i, j));
                text.setTextColor(rowColor);


                tableRow.addView(text);

                }
                tableLayout.addView(tableRow);
            }

    }

    //set displayed table
    public static void setTableToDisplay(TimeTable timeTable){
        currentTimeTable = timeTable;
    }


}

package com.luxlunaris.cfu.frontEnd.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataModel.TimeTable;
import com.luxlunaris.cfu.frontEnd.activities.SettingsActivity;
import com.luxlunaris.cfu.frontEnd.activities.TableViewActivity;
import com.luxlunaris.cfu.frontEnd.activities.TablesListActivity;

import java.time.LocalDateTime;

public class ListItemFragment extends Fragment {

    //time table referenced by this item of the list
    TimeTable timeTable;

    //button to be pressed
    Button openTableButton;

    //(optional message to be displayed on the fragment)
    TextView listItemLabel;
    String message;
    int labelVisibility = View.GONE;

    //this item turns this color when it gets selected (long press)
    int colorWhenSelected = Color.RED;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item_fragment, container, false);

        //get label and display it if a message was set in this fragment's constructor
        listItemLabel = view.findViewById(R.id.listItemLabel);
        listItemLabel.setVisibility(labelVisibility);
        listItemLabel.setText(message);


        //get button
        openTableButton = (Button)view.findViewById(R.id.button);
        //set button's text
        openTableButton.setText(timeTable.getName());
        //set button's action (launch table view)
        openTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableViewActivity.setTableToDisplay(timeTable);
                Intent intent = new Intent(TablesListActivity.tablesListActivity, TableViewActivity.class);
                startActivity(intent);
            }
        });


        //on long click, this element gets selected
        openTableButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(openTableButton.getCurrentTextColor()!=colorWhenSelected){
                    //if unselected, select
                    openTableButton.setTextColor(Color.RED);
                }else{
                    //if selected, unselect
                    openTableButton.setTextColor(getResources().getColor(R.color.myYellow));
                }

                return true;
            }
        });


        return view;
    }

    public ListItemFragment(TimeTable timeTable){
        this.timeTable = timeTable;
    }


    public ListItemFragment(TimeTable timeTable, String message){
        this.timeTable = timeTable;
        labelVisibility = View.VISIBLE;
        this.message = message;
    }


    //is this item currently selected?
    public boolean isSelected(){
        if(openTableButton.getCurrentTextColor()==colorWhenSelected){
            return true;
        }
        return false;
    }

    //get this item's time table
    public TimeTable getTimeTable() {
        return timeTable;
    }


}

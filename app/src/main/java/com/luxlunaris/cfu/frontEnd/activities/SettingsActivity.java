package com.luxlunaris.cfu.frontEnd.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataFetcher.LinkGetter;

public class SettingsActivity extends AppCompatActivity {


    public static SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get autorefrerence
        settingsActivity = this;

        //get homepage input field
        EditText homepageField = findViewById(R.id.homepageField);
        //load the current homepage from memory
        homepageField.setText(LinkGetter.getHomepage());

        //get filters input field
        EditText filtersField = findViewById(R.id.filtersField);
        //load the current filters from memory
        filtersField.setText(LinkGetter.getLinkFilters());



        //get confirm changes  button and set its action
        Button confirmChangesButton = findViewById(R.id.confirmChangesButton);
        confirmChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkGetter.setHomepage(homepageField.getText().toString());
                LinkGetter.setLinkFilters(filtersField.getText().toString());
            }
        });






    }


}

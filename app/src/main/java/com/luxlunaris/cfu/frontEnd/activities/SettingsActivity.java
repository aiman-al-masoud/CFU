package com.luxlunaris.cfu.frontEnd.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.luxlunaris.cfu.R;
import com.luxlunaris.cfu.backEnd.dataFetcher.LinkGetter;
import com.luxlunaris.cfu.frontEnd.fragments.TextEditorFragment;

public class SettingsActivity extends AppCompatActivity implements TextEditorFragment.TextEditorListener {


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


    //inflate "toolbar" menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //set "toolbar's" menu's actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.downloadTablesToolbarButton:
                TextEditorFragment displayInfo = new TextEditorFragment(this, "no tag");
                displayInfo.setPredefinedText(getResources().getString(R.string.info_about_app));
                displayInfo.setEditable(false);
                displayInfo.setDoneButtonsVisibility(View.GONE);
                displayInfo.show(getSupportFragmentManager(), "no tag");
                break;
        }

        return true;
    }


    //callback feature not used yet in this activity
    @Override
    public void onTextEntered(String text, String tag) {
    }


}

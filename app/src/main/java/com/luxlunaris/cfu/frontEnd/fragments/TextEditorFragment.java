package com.luxlunaris.cfu.frontEnd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.luxlunaris.cfu.R;


public class TextEditorFragment extends DialogFragment {


    //listens to this fragment, and performs action
    //with user-entered text when doneButton is hit
    TextEditorListener listener;

    //tag
    String tag;

    //done editing text button
    Button doneButton;
    //(optional) done button's visibility
    private int doneButtonsVisibility = View.VISIBLE;

    //text area
    EditText textArea;

    //(optional) predefined text
    String predefinedText = null;

    //(optional) set editable
    boolean editable =  true;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_editor, container, false);

        //get text editing area
        textArea = view.findViewById(R.id.textEditorView);
        //set predefined text if not null
        if(predefinedText!=null){
            textArea.setText(predefinedText);
        }
        //set editable
        textArea.setFocusable(editable);


        //get done editing text button and set its action
        doneButton = view.findViewById(R.id.doneEditingTextButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTextEntered(textArea.getText().toString(), tag);
                dismiss();
            }
        });

        //set button's visibility
        doneButton.setVisibility(doneButtonsVisibility);

        return view;
    }


    //this interface should be implemented by an
    //activity/fragment that calls this Editor
    //and gets the user-entered text
    public interface TextEditorListener{
        public void onTextEntered(String text, String tag);
    }

    //construct fragment
    public TextEditorFragment(TextEditorListener listener, String tag){
        this.listener = listener;
        this.tag = tag;
    }

    //optional set predefined text
    public void setPredefinedText(String predefinedText){
        this.predefinedText =predefinedText;
    }

    //optional set editable
    public void setEditable(boolean editable){
        this.editable = editable;
    }

    //optional hide done button
    public void setDoneButtonsVisibility(int doneButtonsVisiblity){
        this.doneButtonsVisibility = doneButtonsVisiblity;
    }


}

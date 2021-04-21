package com.luxlunaris.cfu.frontEnd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.luxlunaris.cfu.R;

public class YesOrNoPrompt extends DialogFragment {


    //confirm button
    Button confirmButton;

    //cancel button
    Button cancelButton;

    //yesNoQuestion field
    TextView yesOrNoQuestionField;
    String yesOrNoQuestionText;

    //listener
    YesOrNoPromptListener listener;

    //the tag serves to recycle the prompt interface
    //for more than one question in the same implementing activity
    String tag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yes_or_no_prompt, container, false);

        //get the confirm button and set its action
        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDecisionTaken(true, tag);
                dismiss();
            }
        });


        //get the cancel button and set its action
        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDecisionTaken(false, tag);
                dismiss();
            }
        });


        //get the yesOnNoQuestionField and set its question text
        yesOrNoQuestionField = view.findViewById(R.id.yesNoQuestion);
        yesOrNoQuestionField.setText(yesOrNoQuestionText);

        return view;
    }


    //to create this object you must provide the text of the question,
    //and an object that implements the YesOrNoPromptListener interface
    public YesOrNoPrompt(String yesOrNoQuestionText,String tag, YesOrNoPromptListener listener){
        this.yesOrNoQuestionText = yesOrNoQuestionText;
        this.listener = listener;
        this.tag = tag;
    }


    //interface to communicate with this Prompt
    public interface YesOrNoPromptListener{
        public void onDecisionTaken(boolean yesOrNo, String tag);
    }




}

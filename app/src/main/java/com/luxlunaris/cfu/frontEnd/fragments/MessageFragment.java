package com.luxlunaris.cfu.frontEnd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.luxlunaris.cfu.R;

import java.util.ArrayList;

public class MessageFragment extends DialogFragment {


    //holds message to be displayed
    String message;
    EditText messageBox;
    int messageVisibility = View.VISIBLE;

    //(optional) display a list of fragments instead of the simple message
    ArrayList<Fragment> fragmentsToBeDisplayed = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);

        messageBox = view.findViewById(R.id.messageBox);
        messageBox.setText(message);
        messageBox.setVisibility(messageVisibility); //invisible if there's fragments to be displayed instead of the message


        //if there are (sub-)fragments to be displayed do it
        if(fragmentsToBeDisplayed!=null){
            for(Fragment fragment : fragmentsToBeDisplayed){
                //add a sub-fragment to this fragment
                getChildFragmentManager().beginTransaction().add(R.id.messageFragLinLayout,fragment).commit();
            }
        }



        return view;
    }

    //initialize message fragment with a simple message
    public MessageFragment(String message){
        this.message = message;
    }

    //initialize message fragment with a list of sub-fragments to be displayed
    public MessageFragment(ArrayList<Fragment> fragmentsToBeDisplayed){
        this.fragmentsToBeDisplayed = fragmentsToBeDisplayed;
        messageVisibility = View.GONE;
    }


}

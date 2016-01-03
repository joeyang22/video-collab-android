package me.joeyang.musiccollab.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import me.joeyang.musiccollab.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class JoinRoomFragment extends Fragment {

    EditText mNameField;
    EditText mJoinRoomField;
    Button mCreateRoomButton;


    public JoinRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_join_room, container, false);
        mNameField = (EditText) rootView.findViewById(R.id.nameEditText);
        mJoinRoomField = (EditText) rootView.findViewById(R.id.enterRoomEditText);

        return rootView;
    }

    public String getRoomFieldText(){
        if (mJoinRoomField!= null){
            return mJoinRoomField.getText().toString();
        }else{
            return null;
        }
    }


    public String getNameFieldText() {
        if (mNameField!=null){
            return mNameField.getText().toString();
        }else{
            return null;
        }
    }
}

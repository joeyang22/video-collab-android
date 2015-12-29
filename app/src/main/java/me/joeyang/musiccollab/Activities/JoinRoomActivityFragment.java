package me.joeyang.musiccollab.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.joeyang.musiccollab.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class JoinRoomActivityFragment extends Fragment {

    EditText mCreateRoomField;
    EditText mJoinRoomField;


    public JoinRoomActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_join_room, container, false);
        mCreateRoomField = (EditText) rootView.findViewById(R.id.createRoomEditText);
        mJoinRoomField = (EditText) rootView.findViewById(R.id.enterRoomEditText);
        mCreateRoomField.addTextChangedListener(new RemoveOtherFieldTextWatcher(mJoinRoomField));

        return rootView;
    }

    class RemoveOtherFieldTextWatcher implements TextWatcher{
            EditText mOtherText;
            public RemoveOtherFieldTextWatcher(EditText editText){
                this.mOtherText = editText;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOtherText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
    }
}

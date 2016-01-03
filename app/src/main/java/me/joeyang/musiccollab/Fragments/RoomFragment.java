package me.joeyang.musiccollab.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.joeyang.musiccollab.R;

public class RoomFragment extends android.support.v4.app.Fragment {
    private static final String KEY_ROOM_ID = "room_id";
    TextView mRoomText;
    String roomId;
    public static RoomFragment newInstance(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ROOM_ID, roomId);
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public RoomFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = getArguments().getString(KEY_ROOM_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_room, container, false);
        mRoomText = (TextView) rootView.findViewById(R.id.roomNameTextView);
        mRoomText.setText(String.format(getString(R.string.room_name), roomId));
        return rootView;
    }

}

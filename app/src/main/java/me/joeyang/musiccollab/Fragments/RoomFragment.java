package me.joeyang.musiccollab.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import me.joeyang.musiccollab.DeveloperKey;
import me.joeyang.musiccollab.R;

public class RoomFragment extends Fragment {
    private static final String KEY_ROOM_ID = "room_id";
    private static final String LOG_TAG = RoomFragment.class.getSimpleName();
    TextView mRoomText;
    String roomId;
    YouTubePlayerSupportFragment mYouTubePlayerFragment;

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

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("2zNSgSzhBfM");
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtubeSupportFragment, youTubePlayerFragment).commit();
        return rootView;
    }


}

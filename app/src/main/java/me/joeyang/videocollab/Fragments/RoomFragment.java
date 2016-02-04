package me.joeyang.videocollab.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.HashMap;

import me.joeyang.videocollab.DeveloperKey;
import me.joeyang.videocollab.Models.Video;
import me.joeyang.videocollab.R;

public class RoomFragment extends Fragment {

    private static final String LOG_TAG = RoomFragment.class.getSimpleName();

    TextView mRoomText;
    String roomId;
    EditText mVideoIdText;

    HashMap<String, Integer> mVideoVotes;
    YouTubePlayerSupportFragment mYouTubePlayerFragment;

    public static RoomFragment newInstance() {
        RoomFragment fragment = new RoomFragment();
        return fragment;
    }

    public RoomFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = getArguments().getString(getString(R.string.room_id));
        mVideoVotes = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_room, container, false);
        mRoomText = (TextView) rootView.findViewById(R.id.roomNameTextView);
        mRoomText.setText(String.format(getString(R.string.room_name), roomId));
        mVideoIdText = (EditText) rootView.findViewById(R.id.videoIdEditText);

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

    public String getVideoIdText(){
        if (mVideoIdText!= null){
            return mVideoIdText.getText().toString();
        }
        return "";
    }

    public void voteVideo(Video video){
        mVideoVotes.put(video.videoId, video.votes);
    }


}

package me.joeyang.videocollab.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.joeyang.videocollab.Adapters.SearchResultAdapter;
import me.joeyang.videocollab.DeveloperKey;
import me.joeyang.videocollab.Models.Video;
import me.joeyang.videocollab.R;
import me.joeyang.videocollab.RecyclerItemClickListener;

public class VideoSearchFragment extends Fragment {

    private static final String LOG_TAG = VideoSearchFragment.class.getSimpleName();
    private EditText mSearchText;
    private RecyclerView mVideoRecyclerView;
    private SearchResultAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Video> mVideoList;

    private YouTube youtube;


    public static VideoSearchFragment newInstance() {
        VideoSearchFragment fragment = new VideoSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VideoSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mVideoList = new ArrayList<Video>();
        mAdapter = new SearchResultAdapter(mVideoList, getContext());

        SearchYouTubeTask task = new SearchYouTubeTask();
        task.execute();



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_search, container, false);
        mSearchText = (EditText) rootView.findViewById(R.id.searchEditText);
        mVideoRecyclerView = (RecyclerView) rootView.findViewById(R.id.videoRecyclerView);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mVideoRecyclerView.setLayoutManager(mLayoutManager);
        mVideoRecyclerView.setAdapter(mAdapter);
        mVideoRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }
        }));

        return rootView;
    }



    class SearchYouTubeTask extends AsyncTask<String, Object, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setApplicationName("Video-Collab").build();

                String queryTerm = "Taylor Swift";
                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(DeveloperKey.DEVELOPER_KEY);
                search.setQ(queryTerm);
                search.setType("video");
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/medium/url)");
                search.setMaxResults(10l);
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();

                if (searchResultList != null) {
                    addItemsToList(searchResultList.iterator());
                    prettyPrint(searchResultList.iterator(), queryTerm);
                }
            } catch (GoogleJsonResponseException e) {
                Log.e(LOG_TAG,"There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                Log.e(LOG_TAG,"There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mAdapter.notifyDataSetChanged();
        }
    }
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        Log.i(LOG_TAG,"\n=============================================================");
        Log.i(LOG_TAG,
                "   First " + 10 + " videos for search on \"" + query + "\".");
        Log.i(LOG_TAG, "=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            Log.i(LOG_TAG, " There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                Log.i(LOG_TAG, " Video Id" + rId.getVideoId());
                Log.i(LOG_TAG, " Title: " + singleVideo.getSnippet().getTitle());
                Log.i(LOG_TAG, " Thumbnail: " + thumbnail.getUrl());
                Log.i(LOG_TAG, "\n-------------------------------------------------------------\n");
            }
        }
    }

    private void addItemsToList(Iterator<SearchResult> iteratorSearchResults) {
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getMedium();

                mVideoList.add(new Video(singleVideo.getSnippet().getTitle(),
                                        rId.getVideoId(),
                                        thumbnail.getUrl()));
                Log.i(LOG_TAG, " Video Id" + rId.getVideoId());
                Log.i(LOG_TAG, " Title: " + singleVideo.getSnippet().getTitle());
                Log.i(LOG_TAG, " Thumbnail: " + thumbnail.getUrl());
                Log.i(LOG_TAG, "\n-------------------------------------------------------------\n");
            }
        }
    }


}

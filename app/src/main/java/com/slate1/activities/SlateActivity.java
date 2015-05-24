package com.slate1.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.slate1.R;
import com.slate1.adapters.SlateListAdapter;
import com.slate1.adapters.YoutubeSongListAdapter;
import com.slate1.asynctask.AddSongAsyncTask;
import com.slate1.asynctask.GetSongsAsyncTask;
import com.slate1.asynctask.GetSuggestionsAsyncTask;
import com.slate1.asynctask.SlateListAsyncTask;
import com.slate1.entities.Song;
import com.slate1.entities.YoutubeSong;
import com.slate1.interfaces.SlateListAsyncResponse;
import com.slate1.interfaces.SuggestionAsyncResponse;
import com.slate1.video_test.VideoFragment;

import java.util.ArrayList;

public class SlateActivity extends AppCompatActivity implements SlateListAsyncResponse,
        SearchView.OnQueryTextListener, SuggestionAsyncResponse {
    ListView slateListView;
    ArrayList<Song> songArrayList = new ArrayList();
    public static final String PREFS_NAME = "MyPrefsFile";
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public SlateListAdapter mSlateListAdapter;
    public String userId;

    boolean isAddSongLinearLayoutVisible =  false;
    LinearLayout addSongLinearLayout;

    SearchView searchView;
    ListView suggestionListView;
    public ArrayList<String> suggestionArrayList = new ArrayList();
    public ArrayAdapter<String> suggestionArrayAdapter;

    boolean showSuggestionListView = true;

    ListView songsListView;
    public ArrayList<YoutubeSong> youtubeSongArrayList = new ArrayList();
    public YoutubeSongListAdapter youtubeSongListAdapter;



    boolean isYoutubePlayerLinearLayoutVisible =  false;

    @Override
    public void onBackPressed() {
        if(isYoutubePlayerLinearLayoutVisible){
            mSlateListAdapter.prevButtonPosition=-1;
            mSlateListAdapter.notifyDataSetChanged();
            closeYoutubeVideo();
            return;
        }
        if(isAddSongLinearLayoutVisible){
            if(addSongLinearLayout!=null)closeAddSongLinearLayout(addSongLinearLayout);
        }
        else{
            super.onBackPressed();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slate);
        setTitle("Slate");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_slate_swipe_refresh_layout);

        slateListView = (ListView) findViewById(R.id.slate_list_list_view);

        mSlateListAdapter= new SlateListAdapter(songArrayList,this);
        slateListView.setAdapter(mSlateListAdapter);

        // Fetch userId from SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("userId","1"); // Set to 1 if value not present

        SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSlateListAdapter ,userId,mSwipeRefreshLayout);
        mSlateListAsyncTask.execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSlateListAdapter, userId, mSwipeRefreshLayout);
                mSlateListAsyncTask.execute();
                if(isYoutubePlayerLinearLayoutVisible){
                    mSlateListAdapter.prevButtonPosition=-1;
                    mSlateListAdapter.notifyDataSetChanged();
                    closeYoutubeVideo();
                }
            }

        });

        addSongLinearLayout = (LinearLayout)findViewById(R.id.addSongLinearLayout);

        searchView = (SearchView) findViewById(R.id.suggestionSearchView);
        searchView.setOnQueryTextListener(SlateActivity.this);

        suggestionArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.suggestion_list_item, R.id.suggestiontextView, suggestionArrayList);
        suggestionListView = (ListView)findViewById(R.id.suggestionListView);
        suggestionListView.setAdapter(suggestionArrayAdapter);
        suggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String data=(String)parent.getItemAtPosition(position);
                emptySongListView();
                showSuggestionListView=false;
                searchView.setQuery(data, true);
                emptySuggestionListView();
            }
        });

        youtubeSongListAdapter = new YoutubeSongListAdapter(youtubeSongArrayList,this);
        songsListView = (ListView)findViewById(R.id.songsListView);
        songsListView.setAdapter(youtubeSongListAdapter);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String data=(String)parent.getItemAtPosition(position);
                TextView youtubeTitleTextView = (TextView)((ViewGroup) view).getChildAt(1);
                String title = youtubeTitleTextView.getText().toString();
                String youtubeLink = youtubeSongArrayList.get(position).getYoutubeLink();
                AddSongAsyncTask mAddSongAsyncTask = new AddSongAsyncTask(title, userId ,youtubeLink, songArrayList, mSwipeRefreshLayout, mSlateListAdapter);
                mAddSongAsyncTask.execute();

                //Close the addSong LinearLayout
                closeAddSongLinearLayout(addSongLinearLayout);

                scrollToTop();
            }
        });



        ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(isYoutubePlayerLinearLayoutVisible){
                    mSlateListAdapter.prevButtonPosition=-1;
                    mSlateListAdapter.notifyDataSetChanged();
                    closeYoutubeVideo();
                }
                openAddSongLinearLayout(addSongLinearLayout);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshSlate() {
        mSwipeRefreshLayout.setRefreshing(true);
        SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSlateListAdapter ,userId , mSwipeRefreshLayout);
        mSlateListAsyncTask.execute();
        scrollToTop();
    }

    public void scrollToTop(){
        slateListView.smoothScrollToPosition(0);
    }

    public void openYoutubeVideo(String youtubeLink){
        isYoutubePlayerLinearLayoutVisible = true;
        //"cR7NrE_qEJ4";
        try{
            VideoFragment f = VideoFragment.newInstance(youtubeLink);
            getSupportFragmentManager().beginTransaction().replace(R.id.videoContainer, f).commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void closeYoutubeVideo(){
        isYoutubePlayerLinearLayoutVisible = false;
        try{
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.videoContainer)).commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSlateListAdapter = new SlateListAdapter(songArrayList,this);
        slateListView.setAdapter(mSlateListAdapter );

        GetSongsAsyncTask getSongsAsyncTask = new GetSongsAsyncTask(query,youtubeSongListAdapter,youtubeSongArrayList, this);
        getSongsAsyncTask.delegate = this;

        if(Build.VERSION.SDK_INT >= 11)
            getSongsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            getSongsAsyncTask.execute();
        showSuggestionListView=true;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText==null || newText.equals("")){
            emptySuggestionListView();
            emptySongListView();
        }

        if(showSuggestionListView){
            GetSuggestionsAsyncTask getSuggestionsAsyncTask = new GetSuggestionsAsyncTask(newText,this,suggestionArrayAdapter,suggestionArrayList);
            getSuggestionsAsyncTask.delegate = this;

            if(Build.VERSION.SDK_INT >= 11)
                getSuggestionsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                getSuggestionsAsyncTask.execute();
        }



        return false;
    }



    public void emptySuggestionListView() {
        suggestionArrayList.clear();
        suggestionArrayAdapter.notifyDataSetChanged();
    }

    public void emptySongListView() {
        youtubeSongArrayList.clear();
        youtubeSongListAdapter.notifyDataSetChanged();
    }

    public void openAddSongLinearLayout(LinearLayout addSongLinearLayout){
        isAddSongLinearLayoutVisible=true;
        showSuggestionListView=true;
        RelativeLayout.LayoutParams rlp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        rlp.setMargins(50, 50, 50, 50);
        addSongLinearLayout.setLayoutParams(rlp);
        searchView.setIconified(false);
        searchView.setQuery("",false);
    }

    public void closeAddSongLinearLayout(LinearLayout addSongLinearLayout){
        isAddSongLinearLayoutVisible=false;
        RelativeLayout.LayoutParams rlp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        rlp.setMargins(50, 50, 50, 50);
        addSongLinearLayout.setLayoutParams(rlp);

    }

}

package com.slate.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.slate.R;
import com.slate.asynctask.AddSongAsyncTask;
import com.slate.asynctask.SlateListAsyncTask;
import com.slate.adapters.SlateListAdapter;
import com.slate.entities.Song;
import com.slate.interfaces.SlateListAsyncResponse;

import java.util.ArrayList;

public class SlateActivity extends AppCompatActivity implements SlateListAsyncResponse{
    ListView slateListView;
    ArrayList<Song> songArrayList = new ArrayList();
    public static final String PREFS_NAME = "MyPrefsFile";
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public SlateListAdapter mSongListAdapter;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slate);
        setTitle("Slate");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_slate_swipe_refresh_layout);

        slateListView = (ListView) findViewById(R.id.slate_list_list_view);

        mSongListAdapter = new SlateListAdapter(songArrayList,this);
        slateListView.setAdapter(mSongListAdapter );

        // Fetch userId from SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("userId","1"); // Set to 1 if value not present

        SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSongListAdapter ,userId,mSwipeRefreshLayout);
        mSlateListAsyncTask.execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSongListAdapter, userId, mSwipeRefreshLayout);
                mSlateListAsyncTask.execute();
            }

        });



        ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(SlateActivity.this).create();
                AlertDialog.Builder alert = new AlertDialog.Builder(SlateActivity.this);
                alert.setTitle("Add Song");

                final EditText input = new EditText(SlateActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AddSongAsyncTask mAddSongAsyncTask = new AddSongAsyncTask(input.getText().toString(), userId , songArrayList, mSwipeRefreshLayout, mSongListAdapter);
                        //mAddSongAsyncTask.slateListAsyncResponseDelegate = SlateActivity.this;
                        mAddSongAsyncTask.execute();
                        scrollToTop();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
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
        SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSongListAdapter ,userId , mSwipeRefreshLayout);
        mSlateListAsyncTask.execute();
        scrollToTop();
    }

    public void scrollToTop(){
        slateListView.smoothScrollToPosition(0);
    }
}

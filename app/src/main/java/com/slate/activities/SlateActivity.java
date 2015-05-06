package com.slate.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.slate.R;
import com.slate.SlateListAsyncTask;
import com.slate.adapters.SlateListAdapter;
import com.slate.entities.Song;

import java.util.ArrayList;

public class SlateActivity extends ActionBarActivity {
    ListView slateListView;
    ArrayList<Song> songArrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slate);


        slateListView = (ListView) findViewById(R.id.slate_list_list_view);

        SlateListAdapter mSongListAdapter = new SlateListAdapter(songArrayList,this);
        slateListView.setAdapter(mSongListAdapter );
        String id="1";

        SlateListAsyncTask mSlateListAsyncTask = new SlateListAsyncTask(songArrayList, mSongListAdapter ,id);
        mSlateListAsyncTask.execute();
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
}

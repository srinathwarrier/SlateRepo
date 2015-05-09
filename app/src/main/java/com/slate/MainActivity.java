package com.slate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.slate.activities.SlateActivity;
import com.slate.asynctask.AddUserAsyncTask;
import com.slate.asynctask.SlateListAsyncTask;


public class MainActivity extends AppCompatActivity{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     *
    private NavigationDrawerFragment mNavigationDrawerFragment;*/

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     *  SharedPreferences Variable
     */
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Check if new user or recurring user, by checking if userId in SharedPreferences is null or not.
            If new user,
                get name, email, android_id values
                Call : addNewUser.php(Name, Email, Android_ID)
                get userId -> save it to SharedPreferences
            If existing user,
                get userId from the SharedPreferences whenever needed for APIs

         */

        // Add DeviceID in SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String userId = settings.getString("userId",null);
        if(userId==null)
        {
            // Wait for user to click on Sign Up.
            Button signUpButton = (Button) findViewById(R.id.signUpButton);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // New user
                    createNewUser();
                }
            });
        }
        else{
            //Existing User

            // Open slate for existing user
            Intent intent = new Intent(getApplicationContext(), SlateActivity.class);
            startActivity(intent);

        }

        /*mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/


    }

    public void createNewUser(){
        // Get name, email and android_id
        EditText userNameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText userEmailEditText = (EditText) findViewById(R.id.emailEditText);

        String userName =userNameEditText.getText().toString();
        String userEmail =userEmailEditText.getText().toString();
        String android_id=getAndroidID();

        // AddUserAsyncTask
        AddUserAsyncTask mSlateListAsyncTask = new AddUserAsyncTask(userName, userEmail, android_id, this);
        mSlateListAsyncTask.execute();

    }

    public String getAndroidID(){
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }


    /*@Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }*/

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }*/
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();
        return true;

        //return super.onCreateOptionsMenu(menu);
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

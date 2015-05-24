package com.slate1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.slate1.R;
import com.slate1.activities.SlateActivity;
import com.slate1.asynctask.AddUserAsyncTask;
import com.slate1.asynctask.CheckUserAsyncTask;
import com.slate1.asynctask.UpdateRegistrationIdAsyncTask;
import com.slate1.interfaces.CheckUserAsyncResponse;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements CheckUserAsyncResponse{

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


    /*
            GCM Fields :
     */
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "189085781483";
    static final String TAG = "MyGCMDemo";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;

    String regid;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Check device for Play Services APK.
        if (!checkPlayServices()) {
            Log.i("Slate","Invalid device for GCM");
            return;
        }

        context = getApplicationContext();
        /*
        Check if userId in SharedPreferences is null or not.
            If new,
                get 4 values : name, email, android_id, registrationId
                Call : addNewUser.php(Name, Email, Android_ID)
                get userId -> save it to SharedPreferences
            If existing user,
                get userId from the SharedPreferences whenever needed for APIs

         */


        // Add DeviceID in SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("userId",null);
        if(userId==null)
        {
            // get android_id
            String android_id = getAndroidID();

            // call checkUserAsyncTask(android_id)
            CheckUserAsyncTask checkUserAsyncTask = new CheckUserAsyncTask(android_id,this);
            checkUserAsyncTask.delegate =this;
            checkUserAsyncTask.execute();
        }
        else{
            //Existing User [Assuming regId is also present]
            // Open slate for existing user
            goToSlateScreen();
        }

        /*mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public void createNewUser(){
        // Get name, email, android_id and registrationId
        EditText userNameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText userEmailEditText = (EditText) findViewById(R.id.emailEditText);

        String userName =userNameEditText.getText().toString();
        String userEmail =userEmailEditText.getText().toString();
        String android_id=getAndroidID();

        // get regid
        String registrationId = regid;

        // AddUserAsyncTask
        AddUserAsyncTask mSlateListAsyncTask = new AddUserAsyncTask(userName, userEmail, android_id, registrationId, this);
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


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Slate", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG,msg);
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // get userId
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("userId",null);

        UpdateRegistrationIdAsyncTask updateRegistrationIdAsyncTask = new UpdateRegistrationIdAsyncTask(userId, regid, this);
        updateRegistrationIdAsyncTask.execute();

    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    @Override
    public void goToAddUserScreen() {
        // get RegistrationId
        gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            registerInBackground();
        }
        //TODO: Wait for Async task before clicking on SignUp

        //Load the Sign-Up Form :
        setContentView(R.layout.activity_main);

        // Wait for user to click on Sign Up.
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                // New user
                createNewUser();
            }
        });
    }

    @Override
    public void goToSlateScreen() {
        Intent intent = new Intent(getApplicationContext(), SlateActivity.class);
        startActivity(intent);
    }

    @Override
    public void saveToSharedPreferences(String userId, String regId) {
        //Add userId to SharedPreferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userId", userId);
        editor.commit();

        // Save regID
        storeRegistrationId(getApplicationContext() , regId);
    }

    @Override
    public void updateRegistrationId(String userId) {
        //Add userId to SharedPreferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userId", userId);
        editor.commit();

        this.userId=userId;

        registerInBackground();
    }
}

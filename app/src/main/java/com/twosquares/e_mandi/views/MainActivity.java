package com.twosquares.e_mandi.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pushbots.push.Pushbots;
import com.twosquares.e_mandi.utils.AsyncClass;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.utils.UserLocalStore;
import com.twosquares.e_mandi.utils.customHandler;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static String ip;
    public static RecyclerView mRecyclerView;
    public static List <RowItem> rowItems;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static boolean openSplash = true;
    public static User user;
    public RowItem item;
    ListView lv;
    UserLocalStore userLocalStore;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(MainActivity.this,SellingActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       splash screen definition
        if (openSplash) {
            openSplash = false;
            startActivity(new Intent(this, SplashScreen.class));
        }
//      logged in user data from shared preference
        userLocalStore= new UserLocalStore(this);
        if (authenticate()==true){
            user = userLocalStore.getLoggedinUser();
        }
        else {
            startActivity(new Intent(this, UserLogin.class));
        }

        initPushbots();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ip = getString(R.string.ip);
        rowItems =  new ArrayList<RowItem>();
        rowItems.clear();
        isStoragePermissionGranted();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setScrollBarSize(0);


        AsyncClass asyncClass = new AsyncClass(this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private void initPushbots(){
        Pushbots.sharedInstance().registerForRemoteNotifications();
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);

    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            Log.e("user", String.valueOf(User.age));
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
           /* user = null;
            rowItems.clear();
            User.stars.clear();
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(this, UserLogin.class));*/
        }

        return super.onOptionsItemSelected(item);
    }



    //permissions

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permissions", "Permission is granted");
                return true;
            } else {

                Log.v("Permissions", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permissions", "Permission is granted");
            return true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        rowItems.clear();
        Log.e("resumed","true");
/*        AsyncClass asyncClass = new AsyncClass(this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");*/
    }


    @Override
    public void onRefresh() {
        AsyncClass asyncClass = new AsyncClass(MainActivity.this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");
    }
}
/*
https://192.168.0.150/emandi.php*/

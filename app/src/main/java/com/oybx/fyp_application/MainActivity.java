package com.oybx.fyp_application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import communication_section.ClientCore;
import communication_section.ServerLocation;

public class MainActivity extends AppCompatActivity {
    /* This MainActivity is a Loading Screen */
    private final String TAG = "UI-LoadingScreen";
    private Activity mActivity = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "main activity layout on create called");

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        if(hasCacheServerLocOrNot()){

            SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("serverLoc", MODE_PRIVATE);

            String cacheServerLoc = userAccountSharedPreferences.getString("serverIpAddress", "");
            Log.i(TAG, "Has cache server location : " + cacheServerLoc);

            ServerLocation.setServerLoc(cacheServerLoc, 2888);
        }

//        Intent intent = new Intent(MainActivity.this, LoginPage.class);
//        this.startActivity(intent);



    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "Main Activity layout on start called");
        ApplicationManager.setCurrentAppActivityContext(mActivity);

        Thread t = new Thread(){

            @Override
            public void run(){
                ClientCore.establishConnectionWithServer();
            }

        };
        t.start();
    }
    @Override
    protected void onPause() {
        // call the superclass method first
        super.onPause();

        //remove the reference when application is paused
        ApplicationManager.setCurrentAppActivityContext(null);

    }

    @Override
    protected void onResume() {
        // call the superclass method first
        super.onResume();

        //set the reference when application is paused
        ApplicationManager.setCurrentAppActivityContext(mActivity);

    }

    private boolean hasCacheServerLocOrNot(){
        SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("serverLoc", MODE_PRIVATE);

        String cacheServerLoc = userAccountSharedPreferences.getString("serverIpAddress", "");


        if(!cacheServerLoc.equals("")){
            //if both cache for username and password is not empty
            return true;


        }else {
            return false;
        }

    }




}

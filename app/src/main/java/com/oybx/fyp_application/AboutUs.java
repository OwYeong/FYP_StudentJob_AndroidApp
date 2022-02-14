package com.oybx.fyp_application;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AboutUs extends AppCompatActivity {

    /* This MainActivity is a Loading Screen */
    private final String TAG = "activity-AboutUs";
    private Activity mActivity = AboutUs.this;

    private Button getStartedNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_page);

        Log.i(TAG, "AboutUs activity layout on create called");

        getStartedNow = (Button) findViewById(R.id.activity_aboutus_page_getStarted_btn);

        getStartedNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        ApplicationManager.setCurrentAppActivityContext(mActivity);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

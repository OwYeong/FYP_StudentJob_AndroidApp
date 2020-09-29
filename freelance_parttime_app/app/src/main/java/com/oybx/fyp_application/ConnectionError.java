package com.oybx.fyp_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import communication_section.ClientCore;

public class ConnectionError extends AppCompatActivity {
    private final String TAG = "ConnectionError";
    private Activity mActivity = ConnectionError.this;

    public Button tryAgainBtn;
    public boolean conTryBtnLock = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_error);//display this layout

        Log.i(TAG, "connection error layout on create called");



        tryAgainBtn = (Button) findViewById(R.id.connection_retry_btn);
        tryAgainBtn.setOnClickListener(onClickListener);



    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.connection_retry_btn:


                        tryAgainBtn.setEnabled(false);
                        Log.i(TAG, "Connection retry btn pressed!");
                        ClientCore.establishConnectionWithServer();






            }

        }

    };

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "connection error layout on start called");
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
        Intent intent = new Intent(ApplicationManager.getCurrentAppContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

}

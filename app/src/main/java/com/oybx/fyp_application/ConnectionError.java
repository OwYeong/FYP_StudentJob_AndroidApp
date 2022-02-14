package com.oybx.fyp_application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import communication_section.ClientCore;
import communication_section.ServerLocation;

public class ConnectionError extends AppCompatActivity {
    private final String TAG = "ConnectionError";
    private Activity mActivity = ConnectionError.this;

    public Button tryAgainBtn;
    public ImageView hidden_changeServerLocation_imageView;
    public boolean conTryBtnLock = false;

    private EditText serverLocationPrompt_editText;
    private TextView serverLocationPrompt_alert_textView;
    private Button changeServerLocation_btn;
    private RelativeLayout hiddenDialogChangeServerLoc_section;

    private boolean hiddenChangeLocDialogOpened = false;

    private long lastHitTime = 0;
    private int clickCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_error);//display this layout

        Log.i(TAG, "connection error layout on create called");


        hidden_changeServerLocation_imageView = (ImageView) findViewById(R.id.hidden_changeServerLocationImage);
        tryAgainBtn = (Button) findViewById(R.id.connection_retry_btn);
        tryAgainBtn.setOnClickListener(onClickListener);

        hidden_changeServerLocation_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Hit Count Is "+ clickCount);
                if(openSecretChangeServerLocOrNot()){
                    hiddenDialogChangeServerLoc_section.setVisibility(View.VISIBLE);
                    hiddenChangeLocDialogOpened = true;
                }
            }
        });

        serverLocationPrompt_editText = (EditText) findViewById(R.id.activity_connection_error_serverlocation);
        serverLocationPrompt_alert_textView = (TextView) findViewById(R.id.activity_connection_error_serverlocation_alert);
        changeServerLocation_btn = (Button) findViewById(R.id.activity_connection_error_changeServerlocationBtn);
        hiddenDialogChangeServerLoc_section = (RelativeLayout) findViewById(R.id.activity_connectionerror_hiddenChangeServerLoc_section);

        hiddenDialogChangeServerLoc_section.setVisibility(View.GONE);

        changeServerLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverLocationPrompt_alert_textView.setVisibility(View.GONE);

                String newServerLoc = serverLocationPrompt_editText.getText().toString();

                String serverLocationRegex = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b";

                if(newServerLoc.matches(serverLocationRegex)){

                    SharedPreferences serverLocSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("serverLoc", MODE_PRIVATE);

                    String cacheServerLoc = serverLocSharedPreferences.getString("serverIpAddress", "");
                    ServerLocation.setServerLoc(newServerLoc, 2888);

                    if(cacheServerLoc.equals("")){
                        //means this is the first time


                        SharedPreferences.Editor editor = serverLocSharedPreferences.edit();
                        editor.putString("serverIpAddress", newServerLoc);

                        editor.apply();

                    }else{
                        //means this is not first time

                        if(!newServerLoc.equals(cacheServerLoc)){
                            //if not equal to cache server loc value

                            SharedPreferences.Editor editor = serverLocSharedPreferences.edit();
                            editor.putString("serverIpAddress", newServerLoc);

                            editor.apply();


                        }
                    }

                    hiddenChangeLocDialogOpened = false;
                    hiddenDialogChangeServerLoc_section.setVisibility(View.GONE);



                }else{
                    serverLocationPrompt_alert_textView.setText("Please Insert a valid Ip Address(xxx.xxx.xxx.xxx)");
                    serverLocationPrompt_alert_textView.setVisibility(View.VISIBLE);
                }

            }
        });


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

    public void createCustomAlertDialog( String message){
        final Dialog dialog = new Dialog(ApplicationManager.getCurrentAppActivityContext());


        dialog.setContentView(R.layout.layout_custom_alert_dialog);
        TextView dialogAlertMessage_textView = (TextView) dialog.findViewById(R.id.layout_custom_alert_dialog_alertMessage);
        dialogAlertMessage_textView.setText(message);

        TextView dialogButton_textView = (TextView) dialog.findViewById(R.id.layout_custom_alert_dialog_ok_btn);
        // if button is clicked, close the custom dialog
        dialogButton_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tryAgainBtn.setEnabled(true);
            }
        });

        dialog.show();




    }

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
        if(hiddenChangeLocDialogOpened){
            hiddenChangeLocDialogOpened = false;
            hiddenDialogChangeServerLoc_section.setVisibility(View.GONE);

        }else{
            Intent intent = new Intent(ApplicationManager.getCurrentAppContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }

    }

    private boolean openSecretChangeServerLocOrNot() {
        if(lastHitTime==0){
            lastHitTime=System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastHitTime <= 5000) {
            clickCount++;
            return clickCount>=10;

        }else{
            Log.i(TAG, "resetHitCount");
            lastHitTime = 0;
        }
        clickCount=0;

        return false;
    }

}

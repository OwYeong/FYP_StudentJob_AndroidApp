package com.oybx.fyp_application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oybx.fyp_application.custom_anim.HeightAnim;

import communication_section.ClientCore;

public class LoginPage extends AppCompatActivity {

    private final String TAG = "UI-loginScreen";
    private Activity mActivity = LoginPage.this;


    private boolean transitionLock = false;//used to lock other buttun while transition is occuring.
    private boolean backPressedOnce = false;
    private boolean keyboardShowOrNot = false;

    private int headerSecInitialHeightInDp;// record the initial height of headerSec for transition

    private LinearLayout mainContainer;

    private RelativeLayout headerSec;
    private RelativeLayout loginSection;
    private RelativeLayout loginWithId;
    private RelativeLayout loginWithGst;
    private static RelativeLayout loadingScreen;

    private ImageView bigGear1;
    private ImageView bigGear2;
    private ImageView smallgear;

    private Button loginIdBtn;
    private Button aboutUsBtn;
    private static Button loginBtn;
    private Button registerBtn;

    //Input bar initialize
    //for login with id section
    private EditText idNameInput;
    private EditText idPasswordInput;
    //for login with guest section
    private EditText gstNameInput;

    private TextView idNameInputAlert;
    private TextView idPasswordInputAlert;

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.login_id_btn:
                    Log.i(TAG, "transition lock is " + transitionLock);
                    aboutUsBtn.setEnabled(false);//disable another btn will be enable after transition done
                    if(!transitionLock)// if transition lock is false
                        Log.i(TAG, "login btn");
                        chgLayout((Button) v, "id");// animate the button and change layout after the animation




                    break;
                case R.id.about_us_btn:
                    Log.i(TAG, "transition lock is " + transitionLock);
                    Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.button_anim);
                    aboutUsBtn.startAnimation(anim);


                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Intent intentToAboutUsPage = new Intent(mActivity, AboutUs.class);
                            intentToAboutUsPage.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intentToAboutUsPage);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    break;

                case R.id.login_id:
                    //test_connection.startConnection();
                    //receive_connection.startInputConnection();
                    idNameInputAlert.setVisibility(View.GONE);
                    idPasswordInputAlert.setVisibility(View.GONE);

                    loginBtn.setEnabled(false);
                    String username = idNameInput.getText().toString();
                    String password = idPasswordInput.getText().toString();

                    String numAndAlphabetRegex = "^[a-zA-Z0-9]+$";
                    //Note: ^ means in the begining, $ mean at the end , 1or more+

                    if(username.length() == 0 ){
                        idNameInputAlert.setText("Username Cannot Be Empty. Try Again!");
                        idNameInputAlert.setVisibility(View.VISIBLE);
                    }else if(username.length() >= 25){
                        idNameInputAlert.setText("Username Cannot Longer Than 25 Character. Try Again!");
                        idNameInputAlert.setVisibility(View.VISIBLE);
                    }
                    else if(!username.matches(numAndAlphabetRegex)){
                        // if the username is not(lower case a to z ,uppercase A to Z, 0 to 9) then it contain symbol
                        idNameInputAlert.setText("Username Cannot Contain Symbol. Try Again!");
                        idNameInputAlert.setVisibility(View.VISIBLE);

                    }else {
                        //if username no problem

                        //check Password
                        if(password.length() == 0){
                            idPasswordInputAlert.setText("Password Cannot Be Empty. Try Again!");
                            idPasswordInputAlert.setVisibility(View.VISIBLE);
                        }else if(password.length() >= 25){
                            idPasswordInputAlert.setText("Password Cannot Longer Than 25 Character. Try Again!");
                            idPasswordInputAlert.setVisibility(View.VISIBLE);
                        }else if(password.matches("[ ]" )){
                            idPasswordInputAlert.setText("Password Cannot Contain Space. Try Again!");
                            idPasswordInputAlert.setVisibility(View.VISIBLE);
                        }else{
                            requestForLogin(username, password);
                        }
                    }





                    //createDialog("Username or Password is empty", "Please enter a username!");
                    loginBtn.setEnabled(true);

                    break;
                case R.id.register_button:
                    Intent intentToRegisterPage = new Intent(mActivity, RegisterPage.class);
                    intentToRegisterPage.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intentToRegisterPage);
                    break;
                default:
                    Log.i(TAG, "Button Not Registered in the on click listener");
                    break;

            }

        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Log.i(TAG, "main activity layout on create called");

        loginIdBtn = (Button) findViewById(R.id.login_id_btn);
        aboutUsBtn = (Button) findViewById(R.id.about_us_btn);
        loginBtn = (Button) findViewById(R.id.login_id);
        registerBtn = (Button) findViewById(R.id.register_button);

        loginIdBtn.setOnClickListener(onClickListener);
        aboutUsBtn.setOnClickListener(onClickListener);
        loginBtn.setOnClickListener(onClickListener);
        registerBtn.setOnClickListener(onClickListener);

        bigGear1 = (ImageView) findViewById(R.id.biggear1);
        bigGear2 = (ImageView) findViewById(R.id.biggear2);
        smallgear = (ImageView) findViewById(R.id.smallgear);

        mainContainer = (LinearLayout) findViewById(R.id.main_container);


        headerSec = (RelativeLayout) findViewById(R.id.header_section);
        loginSection = (RelativeLayout) findViewById(R.id.login_section);
        loginWithId = (RelativeLayout) findViewById(R.id.login_with_id);
        loginWithGst = (RelativeLayout) findViewById(R.id.login_with_guest);
        loadingScreen = (RelativeLayout) findViewById(R.id.loadingPanel);

        idNameInput = (EditText) findViewById(R.id.id_name_input);
        idPasswordInput = (EditText) findViewById(R.id.id_password_input);
        gstNameInput = (EditText) findViewById(R.id.gst_name_input);

        idNameInputAlert = (TextView) findViewById(R.id.username_alert);
        idPasswordInputAlert = (TextView) findViewById(R.id.password_alert);




        //make 2 section gone, so that it cant be seen
        loginWithId.setVisibility(View.GONE);
        loginWithGst.setVisibility(View.GONE);

        idNameInputAlert.setVisibility(View.GONE);
        idPasswordInputAlert.setVisibility(View.GONE);
        loadingScreen.setVisibility(View.GONE);

        //make gear invisibly --> GEAR will be visible when user get into LOGIN with ID or Guest PAGE
        bigGear1.setVisibility(View.INVISIBLE);
        bigGear2.setVisibility(View.INVISIBLE);
        smallgear.setVisibility(View.INVISIBLE);

        if(hasCacheUsernameAndPasswordOrNot()){
            SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("userAccount", MODE_PRIVATE);

            String cacheUsername = userAccountSharedPreferences.getString("username", "");
            String cachePassword = userAccountSharedPreferences.getString("password", "");

            requestForLogin(cacheUsername,cachePassword);
        }





    }

    @Override
    protected void onStart(){
        super.onStart();


        Log.i(TAG, "Main Activity layout on start called");
        ApplicationManager.setCurrentAppActivityContext(mActivity);

        //headerSec has dynamic value(Wrap_content) therefore it doesnt get initialize succesful when onStart() is called
        //so, we use post.Means give a function to run when it is initialized
        // Post() will be called when the height is ready, it allow the height to be taken.
        headerSec.post(new Runnable() {
            @Override
            public void run() {
                headerSecInitialHeightInDp = pxToDpi(headerSec.getHeight());//get headerSecHeight, getHeight() return px Value So we have to convert it manually
            }
        });

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

    //customize the onBackPressed Presetted function
    @Override
    public void onBackPressed() {
        //onBackPressed is trigger when user press back btn
        Log.i(TAG, "inside back btn");
        backToHome();// rollback the layout to LOGINPAGE

        //check if back btn has already press once
        if (backPressedOnce) {
            //if yes...


            exitApp();

        }

        backPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        //count 2s and perform code inside
        new Handler().postDelayed(new Runnable() {
            //if back btn doesnt not press 2nd time in 2s

            @Override
            public void run() {
                backPressedOnce=false;// reset the back button variable
            }
        }, 2000);//
    }

    private void backToHome(){

        // reset to home page
        if(!transitionLock) {
            //if no transition is going on then allow BACKToHome Layout

            adjustLayout(false);


            smallgear.clearAnimation();
            smallgear.setVisibility(View.GONE);
            bigGear1.clearAnimation();
            bigGear1.setVisibility(View.GONE);
            bigGear2.clearAnimation();
            bigGear2.setVisibility(View.GONE);


            if (loginWithId.getVisibility() == View.VISIBLE) {
                loginWithId.setVisibility(View.GONE);
            } else {
                loginWithGst.setVisibility(View.GONE);
            }

            loginSection.setVisibility(View.VISIBLE);
        }

    }

    private void exitApp(){

        /* In order to exit the program , It require the First Activity of This App to call the Function -> finish()
            Thefore, we need to navigate to the first page and give a info(Exit:true) to it
            When the page receive the info(Exit:true) it call exit
         */


        Intent intent = new Intent(mActivity, MainActivity.class);



        //for finish() to exit, All other Activity need to be clear
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Help to clear all activity above it. MainActivity.class is at the bottom of activity-> All activity will be clear
        intent.putExtra("EXIT", true);//give info, --> check Mainactivity(it will receive)
        mActivity.startActivity(intent);



    }


    private void startAnimateGear(){

        Animation anim= AnimationUtils.loadAnimation(LoginPage.this, R.anim.gear_rotation_clockwise);
        bigGear2.startAnimation(anim);
        bigGear1.startAnimation(anim);

        anim= AnimationUtils.loadAnimation(LoginPage.this, R.anim.gear_rotation_anticlockwise);

        smallgear.startAnimation(anim);
        
    }


    private int dpiToPx(int dpi){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int dpiInPx = (int) (dpi * scale + 0.5f);// convert dpi to px

        return dpiInPx;
    }

    private int pxToDpi(int px){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int pxInDpi = (int) (px / scale + 0.5f);// convert px to dpi

        return pxInDpi;
    }


    private void chgLayout(Button b, final String layout){



        transitionLock = true;// lock it while performing transition

        adjustLayout(true);

        Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.button_anim);
        b.startAnimation(anim);

        final Animation fadeIn = AnimationUtils.loadAnimation(mActivity, R.anim.fadein);
        Animation fadeOut = AnimationUtils.loadAnimation(mActivity, R.anim.fadeout);
        loginSection.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginSection.setVisibility(View.GONE);

                switch (layout) {
                    case "id":
                        loginWithId.setVisibility(View.VISIBLE);// make login With ID section available
                        loginWithId.startAnimation(fadeIn);
                        fadeIn.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //when the login with id section is revealed

                                transitionLock = false;// unlock it
                                aboutUsBtn.setEnabled(true);

                            }


                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }


                        });


                        break;
                    case "guest":
                        loginWithGst.setVisibility(View.VISIBLE);// make login With Guest section available
                        loginWithGst.startAnimation(fadeIn);
                        fadeIn.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //when the login with guest section is revealed

                                transitionLock = false;// unlock it
                                loginIdBtn.setEnabled(true);


                            }


                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }


                        });

                        break;

                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // when layout succesfully changed then make gear visible



        bigGear1.setVisibility(View.VISIBLE);
        bigGear2.setVisibility(View.VISIBLE);
        smallgear.setVisibility(View.VISIBLE);

        startAnimateGear();











    }


    public static void createDialog(String title, String message){
        AlertDialog.Builder a = new AlertDialog.Builder(ApplicationManager.getCurrentAppActivityContext());

        a.setTitle(title);
        a.setMessage(message);
        a.setCancelable(false);

        a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // NO action needed when OK button pressed

            }

        });

        a.setIcon(android.R.drawable.ic_dialog_alert);
        a.show();




    }

    public static void createCustomAlertDialog( String message){
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
            }
        });

        dialog.show();




    }


    private void adjustLayout(boolean trueOrFalse){
        Log.i(TAG, "adjust layout called hearsec height is :" + headerSecInitialHeightInDp);

        /*  animate header sec  */
        int heightToBeAdjust;
        if(trueOrFalse){
            //if keyboard is show
            heightToBeAdjust = headerSecInitialHeightInDp - 30; //adjust height short To Leave more space
        }else {
            //if keyboard is not show
            heightToBeAdjust = headerSecInitialHeightInDp;//adjust height to original height value
        }


        //HeightAnim(CLASS) is a customize animation parameter (target_Element, element_from_height, element_to_height)
        Animation topHeaderAnim = new HeightAnim(headerSec, headerSec.getHeight(), dpiToPx(heightToBeAdjust) );
        // target : topheader , original height, to 330dp

        // this interpolator is animation time frame
        topHeaderAnim.setInterpolator(new AccelerateInterpolator());// accelerateInterpolator -> slow in , fast out
        topHeaderAnim.setDuration(700);
        headerSec.setAnimation(topHeaderAnim);
        headerSec.startAnimation(topHeaderAnim);

        /*  animate header sec end */


    }

    public static void showLoadingScreen(boolean yesOrNo){


        if(yesOrNo) {
            loadingScreen.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
        }else{
            loadingScreen.setVisibility(View.GONE);
            loginBtn.setEnabled(true);
        }

    }

    public void freezeScreen(boolean trueOrFalse){
        if(trueOrFalse) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void requestForLogin(String username, String password){

        ClientCore.sentLoginRequest( username, password);
        showLoadingScreen(true);
        freezeScreen(true);

    }

    private boolean hasCacheUsernameAndPasswordOrNot(){
        SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("userAccount", MODE_PRIVATE);

        String cacheUsername = userAccountSharedPreferences.getString("username", "");
        String cachePassword = userAccountSharedPreferences.getString("password", "");

        if(!cacheUsername.equals("") && !cachePassword.equals("")){
            //if both cache for username and password is not empty
            return true;


        }else {
            return false;
        }

    }




}

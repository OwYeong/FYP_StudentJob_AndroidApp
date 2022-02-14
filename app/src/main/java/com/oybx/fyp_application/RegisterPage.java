package com.oybx.fyp_application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oybx.fyp_application.student_section.StudentRequestpage;

import communication_section.ClientCore;

public class RegisterPage extends AppCompatActivity {

    private final String TAG = "activity-RegisterPage";
    private Context mContext = ApplicationManager.getCurrentAppContext();
    private Activity mActivity = RegisterPage.this;

    private ScrollView myScrollView;
    private RelativeLayout loadingScreen_relativeLayout;

    private Spinner accountType_spinner;

    private EditText firstName_editText;
    private EditText lastName_editText;
    private EditText username_editText;
    private EditText password_editText;
    private EditText confirmPassword_editText;
    private EditText email_editText;
    private EditText contactNum_editText;

    private LinearLayout studentAccount_section;
    private Spinner generalSkillCat_spinner;
    private EditText collegeName_editText;
    private EditText courseName_editText;

    private TextView firstName_alert_textView;
    private TextView lastName_alert_textView;
    public TextView username_alert_textView;
    private TextView password_alert_textView;
    private TextView confirmPassword_alert_textView;
    private TextView email_alert_textView;
    private TextView contactNum_alert_textView;
    private TextView collegeName_alert_textView;
    private TextView courseName_alert_textView;

    private Button createAcc_button;
    private Button cancel_button;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_register_page_createAcc_btn:
                    firstName_alert_textView.setVisibility(View.GONE);
                    lastName_alert_textView.setVisibility(View.GONE);
                    username_alert_textView.setVisibility(View.GONE);
                    password_alert_textView.setVisibility(View.GONE);
                    confirmPassword_alert_textView.setVisibility(View.GONE);
                    email_alert_textView.setVisibility(View.GONE);
                    contactNum_alert_textView.setVisibility(View.GONE);
                    collegeName_alert_textView.setVisibility(View.GONE);
                    courseName_alert_textView.setVisibility(View.GONE);

                    //get required info
                    String accType = accountType_spinner.getSelectedItem().toString();

                    String firstName = firstName_editText.getText().toString();
                    String lastName = lastName_editText.getText().toString();
                    String username = username_editText.getText().toString();
                    String password = password_editText.getText().toString();
                    String confirmPassword = confirmPassword_editText.getText().toString();
                    String email = email_editText.getText().toString();
                    String contactNum = contactNum_editText.getText().toString();

                    String generalSkillCat = null;
                    String collegeName = null;
                    String courseName = null;
                    if(accType.equals("Student")){
                        generalSkillCat = generalSkillCat_spinner.getSelectedItem().toString();
                        collegeName = collegeName_editText.getText().toString();
                        courseName = courseName_editText.getText().toString();
                    }
                    String regexOnlyAlphabetAndNumber = "^[a-zA-Z0-9]+$";
                    String regexEverythingExceptSpace = "^[\\S]+$";
                    String phoneNumRegex = "^0[0-9][0-9]-[0-9]+$";//this regex allow number start from 0XX-XXXXXXXX
                    String emailRegex = "^(.+)@(.+)$";// this regex allow anything at the first section and last section But must seperate with @

                    if(firstName.length() > 0 && firstName.length() <= 20 && firstName.matches(regexOnlyAlphabetAndNumber)){
                        if(lastName.length() > 0 && lastName.length() <= 20 && lastName.matches(regexOnlyAlphabetAndNumber)){

                            if(username.length()>0 && username.length()<=20 && username.matches(regexOnlyAlphabetAndNumber)){
                                if(password.length() >= 6 && password.length() < 20 && password.matches(regexEverythingExceptSpace)){
                                    if(confirmPassword.equals(password)){
                                        if(email.length() > 0 && email.length() <= 30 && email.matches(emailRegex)){
                                            if(contactNum.length() > 0 && contactNum.length() <= 15 && contactNum.matches(phoneNumRegex)){

                                                if(accType.equals("Student")){
                                                    if(collegeName.length() > 0 && collegeName.length() <= 50){
                                                        if(courseName.length() > 0 && courseName.length() <= 50 ){
                                                            requestForCreateAccount(accType,firstName,lastName,username,password,email,contactNum,generalSkillCat,collegeName,courseName);

                                                        }else {
                                                            courseName_alert_textView.setText("Course Name Cannot be Empty. Maximum 50 Character");
                                                            courseName_alert_textView.setVisibility(View.VISIBLE);
                                                        }

                                                    }else {
                                                        collegeName_alert_textView.setText("College Name Cannot be Empty. Maximum 50 Character");
                                                        collegeName_alert_textView.setVisibility(View.VISIBLE);
                                                    }


                                                }else{
                                                    //not student. It employer, so we does need to check the additional section for the student
                                                    requestForCreateAccount(accType,firstName,lastName,username,password,email,contactNum);
                                                }

                                            }else {
                                                contactNum_alert_textView.setText("Please Follow the Format (0xx-xxxxxxx)! Maximum 15 Character");
                                                contactNum_alert_textView.setVisibility(View.VISIBLE);
                                            }


                                        }else {

                                            email_alert_textView.setText("Please Follow the Format (xxx@xxxxxxx)! Maximum 30 Character");
                                            email_alert_textView.setVisibility(View.VISIBLE);
                                        }

                                    }else {
                                        confirmPassword_alert_textView.setText("Confirm Password Does Not Matches Your Password. Please Check Carefully");
                                        confirmPassword_alert_textView.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    password_alert_textView.setText("Password Must Have Atleast 6 character And Cannot Contain Space. Maximum 20 Characters");
                                    password_alert_textView.setVisibility(View.VISIBLE);

                                }

                            }else {
                                username_alert_textView.setText("Username Be Empty And Cannot Contain Symbol. Maximum 20 Characters");
                                username_alert_textView.setVisibility(View.VISIBLE);
                            }

                        }else {
                            lastName_alert_textView.setText("Last Name Be Empty And Cannot Contain Symbol. Maximum 20 Characters");
                            lastName_alert_textView.setVisibility(View.VISIBLE);
                        }


                    }else{
                        firstName_alert_textView.setText("First Name Be Empty And Cannot Contain Symbol. Maximum 20 Characters");
                        firstName_alert_textView.setVisibility(View.VISIBLE);
                    }




                    break;
                case R.id.activity_register_page_cancel_btn:
                    onBackPressed();
                    break;
            }

        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Log.i(TAG, "on create called");
        loadingScreen_relativeLayout = (RelativeLayout) findViewById(R.id.activity_register_page_loadingPanel);
        loadingScreen_relativeLayout.setVisibility(View.GONE);
        myScrollView = (ScrollView) findViewById(R.id.activity_register_page_scrollview);
        accountType_spinner = (Spinner) findViewById(R.id.activity_register_page_accountType_spinner);
        firstName_editText = (EditText) findViewById(R.id.activity_register_page_firstName);
        lastName_editText = (EditText) findViewById(R.id.activity_register_page_lastName);
        username_editText = (EditText) findViewById(R.id.activity_register_page_username);
        password_editText = (EditText) findViewById(R.id.activity_register_page_password);
        confirmPassword_editText = (EditText) findViewById(R.id.activity_register_page_confirmPassword);
        email_editText = (EditText) findViewById(R.id.activity_register_page_email);
        contactNum_editText = (EditText) findViewById(R.id.activity_register_page_contactNum);

        studentAccount_section = (LinearLayout) findViewById(R.id.activity_register_page_studentAcc_section);
        generalSkillCat_spinner = (Spinner) findViewById(R.id.activity_register_page_generalSkillCat_spinner);
        collegeName_editText = (EditText) findViewById(R.id.activity_register_page_collegeName);
        courseName_editText = (EditText) findViewById(R.id.activity_register_page_courseName);

        firstName_alert_textView = (TextView) findViewById(R.id.activity_register_page_firstName_alert);
        lastName_alert_textView = (TextView) findViewById(R.id.activity_register_page_lastName_alert);
        username_alert_textView = (TextView) findViewById(R.id.activity_register_page_username_alert);
        password_alert_textView = (TextView) findViewById(R.id.activity_register_page_password_alert);
        confirmPassword_alert_textView = (TextView) findViewById(R.id.activity_register_page_confirmPassword_alert);
        email_alert_textView = (TextView) findViewById(R.id.activity_register_page_email_alert);
        contactNum_alert_textView = (TextView) findViewById(R.id.activity_register_page_contactNum_alert);
        collegeName_alert_textView = (TextView) findViewById(R.id.activity_register_page_collegeName_alert);
        courseName_alert_textView = (TextView) findViewById(R.id.activity_register_page_courseName_alert);


        createAcc_button = (Button) findViewById(R.id.activity_register_page_createAcc_btn);
        cancel_button = (Button) findViewById(R.id.activity_register_page_cancel_btn);
        createAcc_button.setOnClickListener(onClickListener);
        cancel_button.setOnClickListener(onClickListener);


        studentAccount_section.setVisibility(View.GONE);

        firstName_alert_textView.setVisibility(View.GONE);
        lastName_alert_textView.setVisibility(View.GONE);
        username_alert_textView.setVisibility(View.GONE);
        password_alert_textView.setVisibility(View.GONE);
        confirmPassword_alert_textView.setVisibility(View.GONE);
        email_alert_textView.setVisibility(View.GONE);
        contactNum_alert_textView.setVisibility(View.GONE);
        collegeName_alert_textView.setVisibility(View.GONE);
        courseName_alert_textView.setVisibility(View.GONE);

        setupAccTypeSpinner();
        setupGeneralSkillCatSpinner();
    }



    private void setupAccTypeSpinner(){


        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.account_type_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        accountType_spinner.setAdapter(adapter);

        accountType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //get the selected item

                switch (selectedItem){
                    case "Student":
                        studentAccount_section.setVisibility(View.VISIBLE);
                        break;
                    case "Employer":
                        studentAccount_section.setVisibility(View.GONE);

                        break;
                    default:
                        Log.i(TAG, "acc type error");

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void setupGeneralSkillCatSpinner(){


        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.profile_skill_category_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        generalSkillCat_spinner.setAdapter(adapter);



    }

    private void requestForCreateAccount(String accType, String firstName, String lastName, String username, String password, String email, String contactNumber){

        showLoadingScreen(true);
        freezeScreen(true);
        hideKeyboard();
        ClientCore.sentCreateAccountRequest(accType,firstName,lastName,username,password,email ,contactNumber);

    }

    private void requestForCreateAccount(String accType, String firstName, String lastName, String username, String password, String email, String contactNumber, String generalSkillCat, String collegeName, String courseName){

        showLoadingScreen(true);
        freezeScreen(true);
        hideKeyboard();
        ClientCore.sentCreateAccountRequest(accType,firstName,lastName,username,password,email ,contactNumber, generalSkillCat, collegeName,courseName);

    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void freezeScreen(boolean trueOrFalse){
        if(trueOrFalse) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void showLoadingScreen(boolean yesOrNo){


        if(yesOrNo) {
            loadingScreen_relativeLayout.setVisibility(View.VISIBLE);

        }else{
            loadingScreen_relativeLayout.setVisibility(View.GONE);

        }

    }
    public void scrollToView(final View v){
        myScrollView.smoothScrollTo(0, v.getBottom());
        myScrollView.post(new Runnable() {
                              @Override
                              public void run() {
                                  v.requestFocus();
                              }
                          }
        );
    }

    public void createCustomAlertDialog(final String message){
        final Dialog dialog = new Dialog(ApplicationManager.getCurrentAppActivityContext());


        dialog.setContentView(R.layout.layout_custom_alert_dialog);
        TextView dialogAlertMessage_textView = (TextView) dialog.findViewById(R.id.layout_custom_alert_dialog_alertMessage);
        dialogAlertMessage_textView.setText(message);

        TextView dialogButton_textView = (TextView) dialog.findViewById(R.id.layout_custom_alert_dialog_ok_btn);
        // if button is clicked, close the custom dialog
        dialogButton_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.equals("Account Create Success, Your Account Has Been Created Successfully. You Can Now Login To Our Service")){
                    //if the  message is Account Create successful, we navigate userBack to LoginPage
                    Intent intentToLoginPage = new Intent(mActivity, LoginPage.class);
                    intentToLoginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intentToLoginPage);

                }else{
                    dialog.dismiss();
                }


            }
        });

        dialog.show();




    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "on start called");
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

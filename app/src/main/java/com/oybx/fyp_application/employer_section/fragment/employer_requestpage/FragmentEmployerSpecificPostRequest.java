package com.oybx.fyp_application.employer_section.fragment.employer_requestpage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.custom_dialog_fragment.YesOrNoDialog;
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.adapter.EmployerMyGeneralRequestRecyclerViewAdapter;
import com.oybx.fyp_application.employer_section.adapter.EmployerMySpecificPostRequestRecyclerViewAdapter;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import communication_section.ClientCore;

public class FragmentEmployerSpecificPostRequest extends Fragment {

    private final String TAG = "nestFragment-empMyPost";
    private final int REJECT_OR_NOT_FRAGMENT_REQUEST_CODE = 1;

    private Context mContext = ApplicationManager.getCurrentAppContext();
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();

    public boolean actionLock = false;//if action lock is on all others action cannot be perform



    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static EmployerMySpecificPostRequestRecyclerViewAdapter recyclerViewAdapter;

    private boolean selectDateTimeSection_displayOrNot = false;

    private static ArrayList<EmployerMyRequestInfo> mySpecificPostRequestArrayList = new ArrayList<>();

    private TextView postJobType_textView, postOffers_textView, postTitle_textView;

    //declare view for promptFor Interview request Section
    private RelativeLayout selectDateTimeSection_relativeLayout;
    private ImageView calendarIcon_imageView;
    private TextView selectDate_textView;
    private Spinner selectTime_spinner;
    private Spinner selectTimeFormat_spinner;
    private EditText interviewLocation_editText;
    private EditText leaveAMessage_editText;
    private Button sentInterviewRequest_button;
    private Button cancel_button;

    private TextView selectDateAlert_textView;
    private TextView interviewLocationAlert_textView;
    private TextView leaveAMessageAlert_textView;

    //declare variable to store info for promptFor Interview request Sectiob
    private String currentHirePostIdInPrompt = null;
    private String currentRequesterUsernameInPrompt = null;
    private int currentPositionInArrayListInPrompt = -1;
    private String currentActionInPrompt;//use to store action For example, Revise interview timetable or accept Student request

    private ArrayAdapter timeFormatAdapter;
    private ArrayAdapter timeAdapter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_sentInterviewRequest_btn:
                    selectDateAlert_textView.setVisibility(View.GONE);
                    interviewLocationAlert_textView.setVisibility(View.GONE);
                    leaveAMessageAlert_textView.setVisibility(View.GONE);

                    String interviewDate = selectDate_textView.getText().toString();
                    String interviewTime = selectTime_spinner.getSelectedItem().toString() + " " + selectTimeFormat_spinner.getSelectedItem().toString();
                    String interviewLocation = interviewLocation_editText.getText().toString();
                    String interviewLeaveAMessage = leaveAMessage_editText.getText().toString();

                    boolean IsInterviewDateValid = false;
                    try {
                        Date interviewDateInDateObject=new SimpleDateFormat("dd-MMM-yyyy").parse(interviewDate);

                        if(interviewDateInDateObject.compareTo(new Date()) > 0) {
                            //if interviewDateInDateObject after today
                            IsInterviewDateValid = true;
                        }
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    if(interviewDate.length() != 0 && IsInterviewDateValid){
                        //if interviewDate not empty

                        if(interviewLocation.length() != 0 && interviewLocation.length() <= 200 ){
                            if (interviewLeaveAMessage.length() != 0 && interviewLeaveAMessage.length() <= 400){
                                //everything is check
                                if(currentActionInPrompt.equals("acceptPostRequest")){
                                    requestForAcceptRequest(currentHirePostIdInPrompt, currentRequesterUsernameInPrompt, interviewDate, interviewTime, interviewLocation, interviewLeaveAMessage, currentPositionInArrayListInPrompt);
                                    ((EmployerRequestpage) getActivity()).showLoadingScreen(true);
                                    ((EmployerRequestpage) getActivity()).freezeScreen(true);
                                    closePromptUserForInterviewRequestInfo();

                                }else if(currentActionInPrompt.equals("reviseInterviewRequest")){
                                    requestForReviseInterviewRequest(currentHirePostIdInPrompt, currentRequesterUsernameInPrompt, interviewDate, interviewTime, interviewLocation, interviewLeaveAMessage, currentPositionInArrayListInPrompt);
                                    ((EmployerRequestpage) getActivity()).showLoadingScreen(true);
                                    ((EmployerRequestpage) getActivity()).freezeScreen(true);
                                    closePromptUserForInterviewRequestInfo();

                                }
                            }else {
                                leaveAMessageAlert_textView.setText("Please Make Sure Leave A Message is Not Empty. Maximum 400 Character ");
                                leaveAMessageAlert_textView.setVisibility(View.VISIBLE);
                            }

                        }else {
                            interviewLocationAlert_textView.setText("Please Make Sure Interview Location is not Empty. Maximum 200 Character");
                            interviewLocationAlert_textView.setVisibility(View.VISIBLE);
                        }

                    }else {
                        //if empty
                        //alert user tell them Must Select
                        selectDateAlert_textView.setText("Please Select a Date. It can only be future date");
                        selectDateAlert_textView.setVisibility(View.VISIBLE);
                    }



                    break;
                case R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_cancel_btn:
                        closePromptUserForInterviewRequestInfo();
                    break;
                case R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_date:
                case R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_calendarIcon:
                    Calendar mcurrentDate=Calendar.getInstance();
                    int year = mcurrentDate.get(Calendar.YEAR);
                    int month = mcurrentDate.get(Calendar.MONTH);
                    int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                    DatePickerDialog mDatePicker=new DatePickerDialog(ApplicationManager.getCurrentAppActivityContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                            Log.i("Date Selected", "Month: " + selectedMonth + " Day: " + selectedDay + " Year: " + selectedYear);
                            selectDate_textView.setText(selectedDay + "-" + MONTH[selectedMonth] + "-" + selectedYear);
                        }
                    },year, month, day);

                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();


                    break;

            }
        }
    };







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employer_requestpage_specific_post_request, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_recyclerView);
        postJobType_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_jobType);
        postOffers_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_offers);
        postTitle_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_postTitle);


        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new EmployerMySpecificPostRequestRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, mySpecificPostRequestArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();



        //declare selected date time section
        selectDateTimeSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container);

        selectDateTimeSection_relativeLayout.setVisibility(View.GONE);

        calendarIcon_imageView = (ImageView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_calendarIcon);
        calendarIcon_imageView.setOnClickListener(onClickListener);
        selectDate_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_date);
        selectDate_textView.setOnClickListener(onClickListener);
        selectTime_spinner = (Spinner) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_time);
        selectTimeFormat_spinner = (Spinner)view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_timeFormat);
        interviewLocation_editText = (EditText) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_interviewLocation);
        leaveAMessage_editText = (EditText) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_leaveAMessage);
        sentInterviewRequest_button = (Button) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_sentInterviewRequest_btn);
        cancel_button = (Button) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_cancel_btn);

        sentInterviewRequest_button.setOnClickListener(onClickListener);
        cancel_button.setOnClickListener(onClickListener);

        selectDateAlert_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_date_alert);
        interviewLocationAlert_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_interviewLocation_alert);
        leaveAMessageAlert_textView = (TextView) view.findViewById(R.id.fragment_employer_requestpage_specific_post_request_selectDateTime_container_leaveAMessage_alert);

        selectDateAlert_textView.setVisibility(View.GONE);
        interviewLocationAlert_textView.setVisibility(View.GONE);
        leaveAMessageAlert_textView.setVisibility(View.GONE);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String currentHirePostId = mySpecificPostRequestArrayList.get(0).getHirePostId();

        postJobType_textView.setText(NestedFragmentEmployerMyPost.getJobTypeWithHirePostId(currentHirePostId));
        postTitle_textView.setText(NestedFragmentEmployerMyPost.getPostTitleWithHirePostId(currentHirePostId));
        postOffers_textView.setText(NestedFragmentEmployerMyPost.getPostOffersWithHirePostId(currentHirePostId));
        setupSelectTimeSpinner();
        setupSelectTimeFormatSpinner();

    }

    public static ArrayList<EmployerMyRequestInfo> getMySpecificPostRequestArrayList(){
        return mySpecificPostRequestArrayList;
    }

    public static void setMySpecificPostRequestArrayList(ArrayList<EmployerMyRequestInfo> mySpecificPostRequestArrayList) {

        try {
            FragmentEmployerSpecificPostRequest.mySpecificPostRequestArrayList = mySpecificPostRequestArrayList;

            if(recyclerViewAdapter != null){
                //mean this page havent setup
                recyclerViewAdapter.setNewArrayList(FragmentEmployerSpecificPostRequest.mySpecificPostRequestArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateMySpecificPostRequestArrayList() {

        try {
            if(mySpecificPostRequestArrayList.get(0)!= null){


                String hirePostId = mySpecificPostRequestArrayList.get(0).getHirePostId();
                mySpecificPostRequestArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListByHirePostId(hirePostId);

                recyclerViewAdapter.setNewArrayList(FragmentEmployerSpecificPostRequest.mySpecificPostRequestArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setupSelectTimeSpinner(){

        //array adapter for spinner
        timeAdapter = ArrayAdapter.createFromResource(mContext, R.array.time_arrays, R.layout.custom_spinner_item);

        timeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        selectTime_spinner.setAdapter(timeAdapter);



    }

    private void setupSelectTimeFormatSpinner(){

        //array adapter for spinner
        timeFormatAdapter = ArrayAdapter.createFromResource(mContext, R.array.time_format_arrays, R.layout.custom_spinner_item);

        timeFormatAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        selectTimeFormat_spinner.setAdapter(timeFormatAdapter);



    }

    public void promptUserForInterviewRequestInfo(String hirePostId, String requesterUsername, int positionInArrayList, String currentAction){

        if(!selectDateTimeSection_displayOrNot) {
            actionLock = true;
            selectDateTimeSection_displayOrNot = true;

            Log.i(TAG, "currentHirePostIdInPrompt is : " + hirePostId);
            Log.i(TAG, "currentRequesterUsernameInPrompt is : " + requesterUsername);
            currentHirePostIdInPrompt = hirePostId;
            currentRequesterUsernameInPrompt = requesterUsername;
            currentPositionInArrayListInPrompt = positionInArrayList;
            currentActionInPrompt = currentAction;


            selectDateTimeSection_relativeLayout.setVisibility(View.VISIBLE);
        }

    }

    public void promptUserForInterviewRequestInfo(String hirePostId, String requesterUsername, int positionInArrayList, String currentAction, EmployerMyRequestInfo selectedEmployerMyRequestInfo){

        if(!selectDateTimeSection_displayOrNot) {
            actionLock = true;
            selectDateTimeSection_displayOrNot = true;

            Log.i(TAG, "currentHirePostIdInPrompt is : " + hirePostId);
            Log.i(TAG, "currentRequesterUsernameInPrompt is : " + requesterUsername);
            currentHirePostIdInPrompt = hirePostId;
            currentRequesterUsernameInPrompt = requesterUsername;
            currentPositionInArrayListInPrompt = positionInArrayList;
            currentActionInPrompt = currentAction;

            selectDate_textView.setText(selectedEmployerMyRequestInfo.getInterviewDate());
            String[] existingTime = selectedEmployerMyRequestInfo.getInterviewTime().split(" ");

            //get the value of time and find spinner position -> then replace the spinner
            int timeSpinnerPosition = timeAdapter.getPosition(existingTime[0]);
            selectTime_spinner.setSelection(timeSpinnerPosition);

            //get the value of time and find spinner position -> then replace the spinner
            int timeFormatSpinnerPosition = timeFormatAdapter.getPosition(existingTime[1]);
            selectTimeFormat_spinner.setSelection(timeFormatSpinnerPosition);

            leaveAMessage_editText.setText(selectedEmployerMyRequestInfo.getInterviewRequestMessage());

            interviewLocation_editText.setText(selectedEmployerMyRequestInfo.getInterviewLocation());

            selectDateTimeSection_relativeLayout.setVisibility(View.VISIBLE);
        }

    }

    public void closePromptUserForInterviewRequestInfo(){
        selectDateTimeSection_displayOrNot = false;
        actionLock = false;

        ((EmployerRequestpage)getActivity()).hideKeyboard();

        //reset when close
        currentHirePostIdInPrompt = null;
        currentRequesterUsernameInPrompt = null;
        currentActionInPrompt = null;
        currentPositionInArrayListInPrompt = -1;
        selectDate_textView.setText("");
        selectTime_spinner.setSelection(0);
        selectTimeFormat_spinner.setSelection(0);
        interviewLocation_editText.setText("");
        leaveAMessage_editText.setText("");


        selectDateTimeSection_relativeLayout.setVisibility(View.GONE);


    }


    public boolean isSelectDateTimeSection_displayOrNot() {
        return selectDateTimeSection_displayOrNot;
    }

    public void promptUserRejectOrNot(String message, String postId, String requesterUsername, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, requesterUsername, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, REJECT_OR_NOT_FRAGMENT_REQUEST_CODE);
        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REJECT_OR_NOT_FRAGMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String requesterUsername = bundle.getString("requesterUsername");
                    String positionInArrayList = bundle.getString("positionInArrayList");

                    requestForRejectRequest(postId, requesterUsername,positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }


                break;
        }
    }


    private void requestForAcceptRequest(String hirePostId, String requesterUsername, String interviewDate, String interviewTime, String interviewLocation, String interviewMessage, int positionInArrayList ){

        Log.i(TAG, "requestForAcceptRequest Called");

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully edit, use this position to replace the record from current Arraylist

        ClientCore.sentAcceptPostRequestRequest(hirePostId,requesterUsername,interviewDate,interviewTime,interviewLocation,interviewMessage,positionInArrayList_string);
    }

    private void requestForRejectRequest(String hirePostId, String requesterUsername, String positionInArrayList_String ){

        Log.i(TAG, "requestForRejectRequest Called");

        String positionInArrayList_string = positionInArrayList_String;//pass this to server
        //if successfully edit, use this position to replace the record from current Arraylist

        ClientCore.sentRejectPostRequestRequest(hirePostId,requesterUsername, positionInArrayList_string);
        ((EmployerRequestpage) getActivity()).showLoadingScreen(true);
        ((EmployerRequestpage) getActivity()).freezeScreen(true);
    }

    private void requestForReviseInterviewRequest(String hirePostId, String requesterUsername, String interviewDate, String interviewTime, String interviewLocation, String interviewMessage, int positionInArrayList ){

        Log.i(TAG, "requestForReviseInterviewRequest Called");

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully edit, use this position to replace the record from current Arraylist

        ClientCore.sentReviseInterviewRequestRequest(hirePostId,requesterUsername,interviewDate,interviewTime,interviewLocation,interviewMessage,positionInArrayList_string);
    }

    public void requestForCheckProfile(String username){
        Log.i(TAG, "Check Profile request for user : " + username);
        ((EmployerRequestpage)getActivity()).showLoadingScreen(true);
        ((EmployerRequestpage)getActivity()).freezeScreen(true);
        ClientCore.sentEmployerCheckProfile(username);
    }
}

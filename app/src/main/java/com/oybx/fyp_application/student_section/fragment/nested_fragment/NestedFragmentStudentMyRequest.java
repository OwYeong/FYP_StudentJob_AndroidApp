package com.oybx.fyp_application.student_section.fragment.nested_fragment;

import android.app.Activity;
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
import android.widget.RelativeLayout;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.custom_dialog_fragment.YesOrNoDialog;
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.adapter.StudentMyInterviewRequestRecyclerViewAdapter;

import java.util.ArrayList;

import communication_section.ClientCore;

public class NestedFragmentStudentMyRequest extends Fragment {
    private static final String TAG = "myrequest-Fragment";

    private final int ACCEPT_INTERVIEW_REQUEST_OR_NOT = 1;
    private final int DECLINE_INTERVIEW_REQUEST_OR_NOT = 2;


    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static StudentMyInterviewRequestRecyclerViewAdapter recyclerViewAdapter;

    private RelativeLayout noInterviewRequestSection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<StudentMyRequestInfo> studentMyInterviewRequestInfoArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_student_requestpage_myrequest, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_student_requestpage_myInterviewRequest_recyclerView);

        noInterviewRequestSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_student_requestpage_myInterviewRequest_noInterviewRequestSection);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new StudentMyInterviewRequestRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, studentMyInterviewRequestInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void promptUserAcceptOrNot(String message, String postId, String username, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, username, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, ACCEPT_INTERVIEW_REQUEST_OR_NOT);
        dialog.show(getFragmentManager().beginTransaction(), "MyAcceptRequestPromptDialog");

    }

    public void promptUserDeclineOrNot(String message, String postId, String username, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, username, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, DECLINE_INTERVIEW_REQUEST_OR_NOT);
        dialog.show(getFragmentManager().beginTransaction(), "MyDeclineRequestPromptDialog");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACCEPT_INTERVIEW_REQUEST_OR_NOT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String requesterUsername = bundle.getString("requesterUsername");
                    String positionInArrayList = bundle.getString("positionInArrayList");
                    Log.i(TAG, "PostId is " + postId + " and positionInArrayList Is" + positionInArrayList + ", yay!");
                    requestForAcceptInterviewRequest(postId, requesterUsername, positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }

                break;
            case DECLINE_INTERVIEW_REQUEST_OR_NOT:

                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String requesterUsername = bundle.getString("requesterUsername");
                    String positionInArrayList = bundle.getString("positionInArrayList");
                    Log.i(TAG, "PostId is " + postId + " and positionInArrayList Is" + positionInArrayList + ", yay!");
                    requestForDeclineInterviewRequest(postId, requesterUsername, positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }

                break;
        }
    }

    public void requestForDeclineInterviewRequest(String postId, String requesterUsername, String positionInArrayList_string){

        ClientCore.sentStudentDeclineInterviewRequest( postId, requesterUsername, positionInArrayList_string );
        ((StudentRequestpage) getActivity()).showLoadingScreen(true);
        ((StudentRequestpage) getActivity()).freezeScreen(true);

    }

    public void requestForAcceptInterviewRequest(String postId, String requesterUsername, String positionInArrayList_string){

        ClientCore.sentStudentAcceptInterviewRequest( postId, requesterUsername, positionInArrayList_string );
        ((StudentRequestpage) getActivity()).showLoadingScreen(true);
        ((StudentRequestpage) getActivity()).freezeScreen(true);

    }


    public void requestForCheckProfile(String username){
        Log.i(TAG, "Check Profile request for user : " + username);
        ((StudentRequestpage)getActivity()).showLoadingScreen(true);
        ((StudentRequestpage)getActivity()).freezeScreen(true);
        ClientCore.sentStudentCheckProfile(username);
    }


    public static void getStudentMyInterviewRequestInfoFromParentList(){
        //this method is used to get Interview request from the full requestlist in EmployerRequestPage

        //interview request mean only get request that postStatus is accepted and interviewstatus is pending


        //Step 1: get the full request list
        ArrayList<StudentMyRequestInfo> fullStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

        //create a StudentMyRequestInfo arraylist to store all filtered arraylist
        ArrayList<StudentMyRequestInfo> studentMyInterviewRequestInfoArrayList = new ArrayList<>();

        //Step 2: filter all pending request, we get only accepted and rejected request
        for(int i = 0; i < fullStudentMyRequestInfoArrayList.size(); i++ ){

            //we get the StudentMyRequestInfo of current looped position
            //every StudentMyRequestInfo belong to 1 hirepostId
            StudentMyRequestInfo currentStudentMyRequestInfo = fullStudentMyRequestInfoArrayList.get(i);

            //new arraylist that store filtered item

            //filter, we get only pending request
            if(currentStudentMyRequestInfo.getPostRequestStatus().equals("accepted")){
                //if post request is pending, Yes this is wat we wanted
                //add into arraylist
                if(currentStudentMyRequestInfo.getInterviewRequestStatus().equals("pending")) {
                    //if my request is pending
                    studentMyInterviewRequestInfoArrayList.add(currentStudentMyRequestInfo);
                }


            }




        }

        //step 3: after filtered all then update the current arraylist
        updateStudentMyOfferPendingRequestInfoArrayList(studentMyInterviewRequestInfoArrayList);



    }


    public static void updateStudentMyOfferPendingRequestInfoArrayList(ArrayList<StudentMyRequestInfo> myStudentMyOfferPendingRequestInfoArrayList) {
        Log.i(TAG,"zzzzzzzzzzz");
        try {
            studentMyInterviewRequestInfoArrayList = myStudentMyOfferPendingRequestInfoArrayList;

            if(recyclerViewAdapter != null) {
                recyclerViewAdapter.setNewArrayList(studentMyInterviewRequestInfoArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayNoInterviewRequestSection(boolean yesOrNo){

        if(yesOrNo){
            noInterviewRequestSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noInterviewRequestSection_relativeLayout.setVisibility(View.GONE);
        }


    }
    public void dicussTimeTableWithHim(ChatRoomUserInfo employerInfo){
        StudentMessagepage.chatRoomUserInfoForTimetableDicuss = employerInfo;

        Intent intentToMessagepage = new Intent(getActivity(), StudentMessagepage.class);
        intentToMessagepage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentToMessagepage);



    }

}

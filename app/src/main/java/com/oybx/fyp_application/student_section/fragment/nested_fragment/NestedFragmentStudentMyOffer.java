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
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.adapter.StudentOfferPendingRecyclerViewAdapter;

import java.util.ArrayList;

import communication_section.ClientCore;

public class NestedFragmentStudentMyOffer extends Fragment {
    private final String TAG = "myofferpending-Fragment";
    private final int CANCEL_REQUEST_OR_NOT_REQUEST_CODE = 1;


    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static StudentOfferPendingRecyclerViewAdapter recyclerViewAdapter;


    private RelativeLayout noOfferPendingSection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<StudentMyRequestInfo> studentMyOfferPendingRequestInfoArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_student_requestpage_myofferpending, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_student_requestpage_myofferpending_recyclerView);

        noOfferPendingSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_student_requestpage_myofferpending_noOfferPendingSection);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new StudentOfferPendingRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, studentMyOfferPendingRequestInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static void getStudentMyOfferPendingRequestInfoFromParentList(){
        //this method is used to get Offer Pending request from the full requestlist in EmployerRequestPage

        //history request mean only get request that currently Pending for (Employer Reply Or Student Reply)


        //Step 1: get the full request list
        ArrayList<StudentMyRequestInfo> fullStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

        //create a StudentMyRequestInfo arraylist to store all filtered arraylist
        ArrayList<StudentMyRequestInfo> studentMyOfferPendingRequestInfoArrayList = new ArrayList<>();

        //Step 2: filter all pending request, we get only accepted and rejected request
        for(int i = 0; i < fullStudentMyRequestInfoArrayList.size(); i++ ){

            //we get the StudentMyRequestInfo of current looped position
            //every StudentMyRequestInfo belong to 1 hirepostId
            StudentMyRequestInfo currentStudentMyRequestInfo = fullStudentMyRequestInfoArrayList.get(i);

            //new arraylist that store filtered item

            //filter, we get only pending request
            if(currentStudentMyRequestInfo.getPostRequestStatus().equals("pending") || currentStudentMyRequestInfo.getPostRequestStatus().equals("accepted")){
                //if post request is pending, Yes this is wat we wanted
                //add into arraylist
                if(currentStudentMyRequestInfo.getPostRequestStatus().equals("pending") && currentStudentMyRequestInfo.getInterviewRequestStatus() == null ) {
                    //if my request is pending
                    studentMyOfferPendingRequestInfoArrayList.add(currentStudentMyRequestInfo);
                }else if(currentStudentMyRequestInfo.getPostRequestStatus().equals("accepted") && currentStudentMyRequestInfo.getInterviewRequestStatus().equals("pending")){
                    //if my request is accepted and interview request is pending for my reply
                    studentMyOfferPendingRequestInfoArrayList.add(currentStudentMyRequestInfo);
                }


            }




        }

        //step 3: after filtered all then update the current arraylist
        updateStudentMyOfferPendingRequestInfoArrayList(studentMyOfferPendingRequestInfoArrayList);



    }


    public static void updateStudentMyOfferPendingRequestInfoArrayList(ArrayList<StudentMyRequestInfo> myStudentMyOfferPendingRequestInfoArrayList) {

        try {
            studentMyOfferPendingRequestInfoArrayList = myStudentMyOfferPendingRequestInfoArrayList;

            if(recyclerViewAdapter != null) {
                recyclerViewAdapter.setNewArrayList(studentMyOfferPendingRequestInfoArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void promptUserDeleteOrNot(String message, String postId, String username, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, username, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, CANCEL_REQUEST_OR_NOT_REQUEST_CODE);
        dialog.show(getFragmentManager().beginTransaction(), "MyCancelRequestPromptDialog");

    }

    public void requestForCancelPostRequest(String postId, String requesterUsername, String positionInArrayList_string){

        ClientCore.sentStudentCancelPostRequestRequest( postId, requesterUsername, positionInArrayList_string );
        ((StudentRequestpage) getActivity()).showLoadingScreen(true);
        ((StudentRequestpage) getActivity()).freezeScreen(true);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CANCEL_REQUEST_OR_NOT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String requesterUsername = bundle.getString("requesterUsername");
                    String positionInArrayList = bundle.getString("positionInArrayList");
                    Log.i(TAG, "PostId is " + postId + " and positionInArrayList Is" + positionInArrayList + ", yay!");
                    requestForCancelPostRequest(postId, requesterUsername, positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }


                break;

        }
    }

    public void displayNoOfferPendingSection(boolean yesOrNo){

        if(yesOrNo){
            noOfferPendingSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noOfferPendingSection_relativeLayout.setVisibility(View.GONE);
        }


    }
}

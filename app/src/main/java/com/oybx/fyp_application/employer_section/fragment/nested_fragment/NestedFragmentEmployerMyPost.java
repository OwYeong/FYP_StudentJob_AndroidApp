package com.oybx.fyp_application.employer_section.fragment.nested_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.custom_dialog_fragment.YesOrNoDialog;
import com.oybx.fyp_application.employer_section.EmployerHomepage;
import com.oybx.fyp_application.employer_section.adapter.EmployerMyPostRecyclerViewAdapter;
import com.oybx.fyp_application.employer_section.fragment.employer_homepage.FragmentEmployerCreateAPostpage;
import com.oybx.fyp_application.student_section.adapter.StudentRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;

import java.util.ArrayList;

import communication_section.ClientCore;

public class NestedFragmentEmployerMyPost extends Fragment {

    private final String TAG = "nestFragment-empMyPost";
    private final int DELETE_OR_NOT_FRAGMENT_REQUEST_CODE = 2;
    private final int DONE_HIRE_POST_OR_NOT_FRAGMENT_REQUEST_CODE = 3;

    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static EmployerMyPostRecyclerViewAdapter recyclerViewAdapter;
    private RelativeLayout noPostSection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<EmployerPostInfo> myPostInfoArrayList = new ArrayList<>();

    private Button createAPost_button;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.nestedFragment_employer_homepage_mypost_createAPost_button:
                    ((EmployerHomepage) getActivity()).changeFragmentPage(new FragmentEmployerCreateAPostpage(), null, null);
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_employer_homepage_mypost, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noPostSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_employer_homepage_mypost_noPostSection);

        createAPost_button = (Button) view.findViewById(R.id.nestedFragment_employer_homepage_mypost_createAPost_button);

        createAPost_button.setOnClickListener(onClickListener);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_employer_homepage_mypost_recyclerView);


        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new EmployerMyPostRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, myPostInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        myRecyclerView.smoothScrollToPosition(0); // I am passing last position here you can pass any existing position
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static ArrayList<EmployerPostInfo> getMyPostInfoArrayList(){
        return myPostInfoArrayList;
    }

    public static void setMyPostInfoArrayList(ArrayList<EmployerPostInfo> myPostInfoArrayList) {

        try {
            NestedFragmentEmployerMyPost.myPostInfoArrayList = myPostInfoArrayList;

            recyclerViewAdapter.setNewArrayList(myPostInfoArrayList);
            recyclerViewAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void promptUserDeleteOrNot(String message, String postId, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, DELETE_OR_NOT_FRAGMENT_REQUEST_CODE);
        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");

    }

    public void promptUserDoneHirePostOrNot(String message, String postId, String positionInArrayList){

        YesOrNoDialog dialog = YesOrNoDialog.newInstance(message, postId, positionInArrayList);

        // setup link back to use and display
        dialog.setTargetFragment(this, DONE_HIRE_POST_OR_NOT_FRAGMENT_REQUEST_CODE);
        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");

    }


    public void requestForDeletePost( String postId, String positionInArrayList){


        ClientCore.sentDeletePostRequest( postId, positionInArrayList );
        ((EmployerHomepage) getActivity()).showLoadingScreen(true);
        ((EmployerHomepage) getActivity()).freezeScreen(true);



    }

    public void requestForDoneHiringPost( String postId, String positionInArrayList){


        ClientCore.sentDoneHiringPostRequest( postId, positionInArrayList );
        ((EmployerHomepage) getActivity()).showLoadingScreen(true);
        ((EmployerHomepage) getActivity()).freezeScreen(true);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DELETE_OR_NOT_FRAGMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String positionInArrayList = bundle.getString("positionInArrayList");
                    Log.i(TAG, "PostId is " + postId + " and positionInArrayList Is" + positionInArrayList + ", yay!");
                    requestForDeletePost(postId, positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }


                break;
            case DONE_HIRE_POST_OR_NOT_FRAGMENT_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String postId = bundle.getString("postId");
                    String positionInArrayList = bundle.getString("positionInArrayList");
                    Log.i(TAG, "PostId is " + postId + " and positionInArrayList Is" + positionInArrayList + ", yay!");
                    requestForDoneHiringPost(postId, positionInArrayList);

                } else if (resultCode == Activity.RESULT_CANCELED) {

                }

                break;
        }
    }

    public static String getJobTypeWithHirePostId(String hirePostId){
        String resultJobType = "-1";
        for(int i = 0; i < myPostInfoArrayList.size(); i++){

            if(myPostInfoArrayList.get(i).getHirePostId().equals(hirePostId)){
                resultJobType = myPostInfoArrayList.get(i).getJobType();
                break;
            }
        }

        return resultJobType;
    }

    public static String getPostTitleWithHirePostId(String hirePostId){
        String resultPostTitle = "-1";
        for(int i = 0; i < myPostInfoArrayList.size(); i++){

            if(myPostInfoArrayList.get(i).getHirePostId().equals(hirePostId)){
                resultPostTitle = myPostInfoArrayList.get(i).getPostTitle();
                break;
            }
        }

        return resultPostTitle;
    }

    public static String getPostOffersWithHirePostId(String hirePostId){
        String resultPostOffers = "-1";
        for(int i = 0; i < myPostInfoArrayList.size(); i++){

            if(myPostInfoArrayList.get(i).getHirePostId().equals(hirePostId)){
                if(myPostInfoArrayList.get(i).getJobType().equals("PartTime")){
                    resultPostOffers = myPostInfoArrayList.get(i).getOffers() + " / Hrs";
                }else {
                    resultPostOffers = myPostInfoArrayList.get(i).getOffers() + " / Job";
                }

                break;
            }
        }

        return resultPostOffers;
    }


    public void displayNoPostSection(boolean yesOrNo){

        if(yesOrNo){
            noPostSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noPostSection_relativeLayout.setVisibility(View.GONE);
        }


    }



}

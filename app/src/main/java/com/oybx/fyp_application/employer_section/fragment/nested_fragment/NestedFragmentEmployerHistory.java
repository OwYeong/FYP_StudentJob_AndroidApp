package com.oybx.fyp_application.employer_section.fragment.nested_fragment;

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
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.adapter.EmployerMyPostRequestHistoryRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

import communication_section.ClientCore;

public class NestedFragmentEmployerHistory extends Fragment {

    private final String TAG = "nestFragment-stdHistory";


    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static EmployerMyPostRequestHistoryRecyclerViewAdapter recyclerViewAdapter;

    private RelativeLayout noHistorySection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestHistoryInfoNestedArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_employer_requestpage_history, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_employer_requestpage_history_recyclerView);

        noHistorySection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_employer_requestpage_history_noHistorySection);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new EmployerMyPostRequestHistoryRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, employerMyRequestHistoryInfoNestedArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static void getEmployerMyRequestHistoryInfoFromParentList(){
        //this method is used to get history request from the full requestlist in EmployerRequestPage

        //history request mean only get request that already Accepted or Declined(Not pending)


        //Step 1: get the full request list
        ArrayList<ArrayList<EmployerMyRequestInfo>> fullEmployerMyRequestInfoNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

        //create a history Nested arraylist to store all filtered arraylist
        ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestHistoryInfoNestedArrayList = new ArrayList<>();

        //Step 2: filter all pending request, we get only accepted and rejected request
        for(int i = 0; i < fullEmployerMyRequestInfoNestedArrayList.size(); i++ ){

            //we get the arraylist of current looped position
            //every arraylist belong to 1 hirepostId
            ArrayList<EmployerMyRequestInfo> currentEmployerMyRequestInfo = fullEmployerMyRequestInfoNestedArrayList.get(i);

            //new arraylist that store filtered item
            ArrayList<EmployerMyRequestInfo> filteredEmployerMyRequestInfoArrayList = new ArrayList<>();
            for(int counter = 0; counter < currentEmployerMyRequestInfo.size(); counter++){

                //filter, we get only accepted or rejected request
                if(currentEmployerMyRequestInfo.get(counter).getPostRequestStatus().equals("accepted")){
                    //if post request is accepted, we check the interview request status
                    if(currentEmployerMyRequestInfo.get(counter).getInterviewRequestStatus().equals("accepted")){
                        filteredEmployerMyRequestInfoArrayList.add(currentEmployerMyRequestInfo.get(counter));

                    }else if(currentEmployerMyRequestInfo.get(counter).getInterviewRequestStatus().equals("rejected")){
                        filteredEmployerMyRequestInfoArrayList.add(currentEmployerMyRequestInfo.get(counter));
                    }

                }else if (currentEmployerMyRequestInfo.get(counter).getPostRequestStatus().equals("rejected")){
                    filteredEmployerMyRequestInfoArrayList.add(currentEmployerMyRequestInfo.get(counter));

                }

            }

            if(filteredEmployerMyRequestInfoArrayList.size() != 0){
                employerMyRequestHistoryInfoNestedArrayList.add(filteredEmployerMyRequestInfoArrayList);
            }


        }

        //step 3: after filtered all then update the current arraylist
        updateEmployerMyRequestHistoryInfoNestedArrayList(employerMyRequestHistoryInfoNestedArrayList);



    }


    public static void updateEmployerMyRequestHistoryInfoNestedArrayList(ArrayList<ArrayList<EmployerMyRequestInfo>> myEmployerMyRequestHistoryInfoNestedArrayList) {

        try {
            employerMyRequestHistoryInfoNestedArrayList = myEmployerMyRequestHistoryInfoNestedArrayList;

            if(recyclerViewAdapter != null){
                recyclerViewAdapter.setNewArrayList(employerMyRequestHistoryInfoNestedArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayNoHistorySection(boolean yesOrNo){

        if(yesOrNo){
            noHistorySection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noHistorySection_relativeLayout.setVisibility(View.GONE);
        }


    }

    public void requestForCheckProfile(String username){
        Log.i(TAG, "Check Profile request for user : " + username);
        ((EmployerRequestpage)getActivity()).showLoadingScreen(true);
        ((EmployerRequestpage)getActivity()).freezeScreen(true);
        ClientCore.sentEmployerCheckProfile(username);
    }
}

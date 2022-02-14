package com.oybx.fyp_application.student_section.fragment.nested_fragment;

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
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.adapter.StudentMyRequestHistoryRecyclerViewAdapter;

import java.util.ArrayList;

public class NestedFragmentStudentHistory extends Fragment {

    private final String TAG = "nestFragment-empHistory";


    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static StudentMyRequestHistoryRecyclerViewAdapter recyclerViewAdapter;

    private RelativeLayout noHistorySection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<StudentMyRequestInfo> studentMyRequestHistoryInfoArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_student_requestpage_history, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_student_requestpage_history_recyclerView);

        noHistorySection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_student_requestpage_history_noHistorySection);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new StudentMyRequestHistoryRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, studentMyRequestHistoryInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static void getStudentMyRequestHistoryInfoFromParentList(){
        //this method is used to get history request from the full requestlist in EmployerRequestPage

        //history request mean only get request that already Accepted or Declined(Not pending)


        //Step 1: get the full request list
        ArrayList<StudentMyRequestInfo> fullStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

        //create a history Nested arraylist to store all filtered arraylist
        ArrayList<StudentMyRequestInfo> studentMyRequestHistoryInfoArrayList = new ArrayList<>();

        //Step 2: filter all pending request, we get only accepted and rejected request
        for(int i = 0; i < fullStudentMyRequestInfoArrayList.size(); i++ ){

            //we get the StudentMyRequestInfo of current looped position
            //every StudentMyRequestInfo belong to 1 hirepostId
            StudentMyRequestInfo currentStudentMyRequestInfo = fullStudentMyRequestInfoArrayList.get(i);

            //new arraylist that store filtered item

            //filter, we get only accepted or rejected request
            Log.i("wawawa", currentStudentMyRequestInfo.getPostRequestStatus());
            if(currentStudentMyRequestInfo.getPostRequestStatus().equals("accepted")){
                //if post request is accepted, we check the interview request status
                if(currentStudentMyRequestInfo.getInterviewRequestStatus().equals("accepted")){
                    studentMyRequestHistoryInfoArrayList.add(currentStudentMyRequestInfo);

                }else if(currentStudentMyRequestInfo.getInterviewRequestStatus().equals("rejected")){
                    studentMyRequestHistoryInfoArrayList.add(currentStudentMyRequestInfo);
                }

            }else if (currentStudentMyRequestInfo.getPostRequestStatus().equals("rejected")){
                studentMyRequestHistoryInfoArrayList.add(currentStudentMyRequestInfo);

            }




        }

        //step 3: after filtered all then update the current arraylist
        updateStudentMyRequestHistoryInfoArrayList(studentMyRequestHistoryInfoArrayList);



    }


    public static void updateStudentMyRequestHistoryInfoArrayList(ArrayList<StudentMyRequestInfo> myStudentMyRequestHistoryInfoArrayList) {

        try {
            studentMyRequestHistoryInfoArrayList = myStudentMyRequestHistoryInfoArrayList;

            if(recyclerViewAdapter != null) {
                recyclerViewAdapter.setNewArrayList(studentMyRequestHistoryInfoArrayList);
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

}

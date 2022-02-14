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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.adapter.EmployerMyGeneralRequestRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

public class NestedFragmentEmployerMyRequest extends Fragment {

    private final String TAG = "nestFragment-empReq";


    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static EmployerMyGeneralRequestRecyclerViewAdapter recyclerViewAdapter;

    private RelativeLayout noRequestSection_relativeLayout;



    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestInfoNestedArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_employer_requestpage_myrequest, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noRequestSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_employer_requestpage_myrequest_noRequestSection);
        noRequestSection_relativeLayout.setVisibility(View.VISIBLE);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_employer_requestpage_myrequest_recyclerView);


        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new EmployerMyGeneralRequestRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this,  employerMyRequestInfoNestedArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();









    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static ArrayList<ArrayList<EmployerMyRequestInfo>> getEmployerMyRequestInfoNestedArrayList(){
        return employerMyRequestInfoNestedArrayList;
    }

    public static void setEmployerMyRequestInfoNestedArrayList(ArrayList<ArrayList<EmployerMyRequestInfo>> myEmployerMyRequestInfoNestedArrayList) {

        try {
            employerMyRequestInfoNestedArrayList = myEmployerMyRequestInfoNestedArrayList;


            if(recyclerViewAdapter != null) {
                recyclerViewAdapter.setNewArrayList(employerMyRequestInfoNestedArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayNoRequestSection(boolean yesOrNo){

        if(yesOrNo){
            noRequestSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noRequestSection_relativeLayout.setVisibility(View.GONE);
        }


    }





}

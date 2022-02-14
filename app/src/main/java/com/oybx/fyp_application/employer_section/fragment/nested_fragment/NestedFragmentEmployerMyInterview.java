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
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.adapter.EmployerMyInterviewRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NestedFragmentEmployerMyInterview extends Fragment {
    private final String TAG = "nestFragment-empMyInter";

    private static RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static EmployerMyInterviewRecyclerViewAdapter recyclerViewAdapter;

    private RelativeLayout noInterviewSection_relativeLayout;

    private static boolean recyclerViewSetUpOrNot = false;

    private static ArrayList<EmployerMyRequestInfo> employerMyConfirmedInterviewInfoArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_employer_homepage_myinterview, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.nestedFragment_employer_homepage_myInterview_recyclerView);

        noInterviewSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.nestedFragment_employer_homepage_myInterview_noInterviewSection);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new EmployerMyInterviewRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, employerMyConfirmedInterviewInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();







    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static void getEmployerMyConfirmedInterviewInfoFromParentList(){
        //this method is used to get confirmed Interview from the full requestlist in EmployerRequestPage

        //confirmed interview  mean only get request that postStatus is accepted and interviewstatus is accepted


        //Step 1: get the full request list
        ArrayList<ArrayList<EmployerMyRequestInfo>> fullEmployerMyRequestInfoNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

        //create a StudentMyRequestInfo arraylist to store all filtered arraylist
        ArrayList<EmployerMyRequestInfo> employerMyConfirmedInterviewInfoArrayList = new ArrayList<>();

        //Step 2: filter all pending request, we get only accepted and rejected request
        for(int i = 0; i < fullEmployerMyRequestInfoNestedArrayList.size(); i++ ){

            //we get the Arraylist<EmployerMyRequestInfo> of current looped position
            //every Arraylist<EmployerMyRequestInfo> belong to 1 hirepostId

            ArrayList<EmployerMyRequestInfo> currentEmployerMyRequestInfoArrayList = fullEmployerMyRequestInfoNestedArrayList.get(i);

            //Each hirepost can have many request, so we check one by one
            for (int loop = 0; loop < currentEmployerMyRequestInfoArrayList.size(); loop++){
                EmployerMyRequestInfo currentEmployerMyRequestInfo = currentEmployerMyRequestInfoArrayList.get(loop);

                // NOTE: if PostRequestStatus() is accepted and InterviewRequestStatus() is accepted, then it is a confirmed interview request

                //filter, we get only accepted
                if(currentEmployerMyRequestInfo.getPostRequestStatus().equals("accepted")){
                    //if post request is accepted
                    //check interview request status
                    if(currentEmployerMyRequestInfo.getInterviewRequestStatus().equals("accepted")) {
                        //if my interview request is accept
                        //this is wat we wanted


                        //but We only want to show interview request that is still available

                        try {
                            //step 1: we get the current data time
                            Calendar c = Calendar.getInstance();
                            Date dateTimeNow = c.getTime();

                            String interviewDateTime = currentEmployerMyRequestInfo.getInterviewDate() + " ";

                            String[] splitedInterviewTime = currentEmployerMyRequestInfo.getInterviewTime().toLowerCase().split(" ");

                            for (int num = 0; num < splitedInterviewTime.length; num++){
                                Log.i("date", "splited [" + num +"] is" + splitedInterviewTime[num]);
                            }

                            if(splitedInterviewTime[1].equals("p.m.")){
                                if (splitedInterviewTime[0].equals("12")){
                                    interviewDateTime+="12:00:00";
                                }else {
                                    //if pm then  + 12
                                    int timeIn24Format = Integer.parseInt(splitedInterviewTime[0]) +12;
                                    interviewDateTime= interviewDateTime + Integer.toString(timeIn24Format) + ":00:00";
                                }


                            }else if (splitedInterviewTime[1].equals("a.m.")){
                                if (splitedInterviewTime[0].equals("12")){
                                    interviewDateTime+="00:00:00";
                                }else {
                                    interviewDateTime= interviewDateTime + splitedInterviewTime[0] + ":00:00";
                                }

                            }

                            Log.i("date", "final datetime" + interviewDateTime);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


                            Date interviewDateTime_date = sdf.parse(interviewDateTime);
                            String datetime = sdf.format(interviewDateTime_date);
                            Log.i("date", datetime);

                            String strDate = sdf.format(c.getTime());








                            //step 2: compare current time with the interview time

                            if(interviewDateTime_date.compareTo(dateTimeNow) > 0){
                                //current Date is smaller than interviewDateTime_date

                                //step 3: if the interview is not overdue we add into the arraylist
                                //this is not overdue since current date is smaller
                                Log.i("date", "the inteview request is not over yet");

                                employerMyConfirmedInterviewInfoArrayList.add(currentEmployerMyRequestInfo);


                            }else{
                                Log.i("date", "the inteview request is already over");
                            }




                        } catch (ParseException e) {
                            Log.i("date", "got problem");
                            e.printStackTrace();
                        }

                    }



                }
            }




        }

        //step 3: after filtered all then update the current arraylist
        updateEmployerMyConfirmedInterviewInfoArrayList(employerMyConfirmedInterviewInfoArrayList);



    }


    public static void updateEmployerMyConfirmedInterviewInfoArrayList(ArrayList<EmployerMyRequestInfo> myEmployerMyConfirmedInterviewInfoArrayList) {

        try {
            employerMyConfirmedInterviewInfoArrayList = myEmployerMyConfirmedInterviewInfoArrayList;

            if(recyclerViewAdapter != null) {
                recyclerViewAdapter.setNewArrayList(employerMyConfirmedInterviewInfoArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayNoInterviewSection(boolean yesOrNo){

        if(yesOrNo){
            noInterviewSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noInterviewSection_relativeLayout.setVisibility(View.GONE);
        }


    }
}

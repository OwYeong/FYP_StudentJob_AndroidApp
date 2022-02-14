package com.oybx.fyp_application.student_section.fragment.nested_fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerHomepage;
import com.oybx.fyp_application.student_section.StudentHomepage;
import com.oybx.fyp_application.student_section.adapter.StudentRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;

import java.util.ArrayList;

import communication_section.ClientCore;

public class NestedFragmentStudentNewFeed extends Fragment {
    private static final String TAG ="StdNewFeed-Fragement";

    private RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private static SwipeRefreshLayout mySwipeRefreshLayout;
    private static StudentRecyclerViewAdapter recyclerViewAdapter;
    private static ArrayList<EmployerPostInfo> employerPostInfoArrayList = new ArrayList<>();

    public boolean applyForPostSectionDisplayOrNot = false;//if apply for post section is display already, we ill reject it

    private RelativeLayout applyForPostSection_relativeLayout;
    private EditText applyPostMessage_editText;
    private TextView applyPostMessage_alert_textView;
    private Button applyPost_btn;
    private Button cancel_btn;

    private static boolean postRequestPerformingOrNot = false;

    //declare variable to store info for promptFor Interview request Sectiob
    private String currentHirePostIdInPrompt = null;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.nestedfragment_student_homepage_newfeed_sentRequest_btn:
                    applyPostMessage_alert_textView.setVisibility(View.GONE);

                    String leaveAMessage = applyPostMessage_editText.getText().toString();
                    String myUsername = ApplicationManager.getMyStudentAccountInfo().getUsername();

                    if(leaveAMessage.length()!=0 && leaveAMessage.length() < 250){
                        //if the message is not empty and less than 250 character

                        requestForApplyEmployerPost(currentHirePostIdInPrompt,myUsername, leaveAMessage);


                    }else {
                        applyPostMessage_alert_textView.setText("Please Ensure That The Leave A Message Section Is Not Empty. Maximum 250 Character");
                        applyPostMessage_alert_textView.setVisibility(View.VISIBLE);
                    }

                    break;
                case R.id.nestedfragment_student_homepage_newfeed_cancel_btn:
                        closePromptUserForApplyForPostSection();
                    break;

            }
        }
    };




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestForGetEmployerPost();

        /*
        Bitmap myProPic = convertImageStringToBitmap("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");
        Bitmap myPostPic = convertImageStringToBitmap("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");

        EmployerPostInfo epi = new EmployerPostInfo(myProPic, myPostPic, "1", "Tan Jian Feng", "Hiring Freelance Programmer for a Restaurant Point-of-sale system development", "The system should allow employee to input the item into the system and calculate the total price", "Freelance", "Rm 800", "2019-4-15 12:10:30", "", "" );
        EmployerPostInfo epi2 = new EmployerPostInfo(myProPic, myPostPic, "2", "Tan Jian Feng", "Hiring Freelance Programmer for a Restaurant Point-of-sale system development", "The system should allow employee to input the item into the system and calculate the total price", "Freelance", "Rm 800", "2019-4-15 12:10:30", "", "" );
        EmployerPostInfo epi3 = new EmployerPostInfo(myProPic, myPostPic, "3", "Tan Jian Feng", "Hiring Freelance Programmer for a Restaurant Point-of-sale system development", "The system should allow employee to input the item into the system and calculate the total price", "Freelance", "Rm 800", "2019-4-15 12:10:30", "", "" );

        employerPostInfoArrayList.add(epi);
        employerPostInfoArrayList.add(epi2);
        employerPostInfoArrayList.add(epi3);
        */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nestedfragment_student_homepage_newfeed, container, false);// parameter (fragment layout, container)
        Log.i(TAG, "OnCreateView Called");


        applyForPostSection_relativeLayout = (RelativeLayout) v.findViewById(R.id.nestedfragment_student_homepage_newfeed_applyPostContainer);
        applyForPostSection_relativeLayout.setVisibility(View.GONE);
        applyPostMessage_editText = (EditText) v.findViewById(R.id.nestedfragment_student_homepage_newfeed_leaveAMessage);
        applyPostMessage_alert_textView = (TextView) v.findViewById(R.id.nestedfragment_student_homepage_newfeed_leaveAMessage_alert);
        applyPostMessage_alert_textView.setVisibility(View.GONE);

        applyPost_btn = (Button) v.findViewById(R.id.nestedfragment_student_homepage_newfeed_sentRequest_btn);
        cancel_btn = (Button) v.findViewById(R.id.nestedfragment_student_homepage_newfeed_cancel_btn);
        applyPost_btn.setOnClickListener(onClickListener);
        cancel_btn.setOnClickListener(onClickListener);

        closePromptUserForApplyForPostSection();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        myRecyclerView = view.findViewById(R.id.testRecyclerView);
        mySwipeRefreshLayout = view.findViewById(R.id.mySwipeRefreshLayout);

        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myRecyclerView.setLayoutManager(myLayoutManager);

        recyclerViewAdapter = new StudentRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, employerPostInfoArrayList);

        myRecyclerView.setAdapter(recyclerViewAdapter);


        recyclerViewAdapter.notifyDataSetChanged();

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "The pulled up for refresh");
                requestForRefreshEmployerPost();
                mySwipeRefreshLayout.setRefreshing(true);
            }
        });
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleThreshold = 2;//the number of post must Have, below first Visible item

                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = myLayoutManager.getItemCount();
                int firstVisibleItem = myLayoutManager.findFirstVisibleItemPosition();// this indicate the first visible item on my screen --> return in position of Arraylist



                if (postRequestPerformingOrNot) {
                    //Do nothing


                }else{
                    //not performing any post request

                    //check Whether need to Perform post Request anot

                    //total item - visible item Count as
                    //Ex: if the firstvisibleItem in my screen doenst have 2 post below it. Mean end is about to Reach
                    if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

                        Log.i(TAG, "The Remaining Post until the end is less than " + visibleThreshold );

                        //call loadMorePost Function
                        requestForLoadMoreEmployerPost();


                    }

                }




            }

        });


        // or  (ImageView) view.findViewById(R.id.foo);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(TAG, "on destroy view called");


    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "on deattach called");
    }

    public void promptUserApplyForPostSection(String hirePostId){

        if(!applyForPostSectionDisplayOrNot) {
            applyForPostSectionDisplayOrNot = true;

            Log.i(TAG, "currentHirePostIdInPrompt is : " + hirePostId);

            currentHirePostIdInPrompt = hirePostId;



            applyForPostSection_relativeLayout.setVisibility(View.VISIBLE);
        }

    }

    public void closePromptUserForApplyForPostSection(){
        applyForPostSectionDisplayOrNot = false;

        ((StudentHomepage)getActivity()).hideKeyboard();

        //reset when close
        currentHirePostIdInPrompt = null;
        applyPostMessage_editText.setText("");
        applyPostMessage_alert_textView.setVisibility(View.GONE);

        applyForPostSection_relativeLayout.setVisibility(View.GONE);


    }


    private static Bitmap convertImageStringToBitmap(String myImageString){
        /* Function to convert imageString into Bitmap*/


        //Decode the imageString(Base 64 Encoded String) into Image Bytes
        //Note: Base64.NO_PADDING is very important as the ImageString has no padding -> ex: data:image/png;base64
        byte[] myImageByteArray = Base64.decode(myImageString.getBytes(), Base64.NO_PADDING);

        //Convert ImageByte to Bitmap
        Bitmap myBitmap= BitmapFactory.decodeByteArray(myImageByteArray, 0, myImageByteArray.length);

        return myBitmap;
    }

    private void requestForApplyEmployerPost(String hirepostId, String requesterUsername, String leaveAMessage){

        closePromptUserForApplyForPostSection();
        ((StudentHomepage) getActivity()).showLoadingScreen(true);
        ((StudentHomepage) getActivity()).freezeScreen(true);

        ClientCore.sentApplyEmployerPostRequest(hirepostId, requesterUsername, leaveAMessage);
    }


    private void requestForGetEmployerPost(){
        postRequestPerformingOrNot = true;

        String condition = null;
        ClientCore.sentGetEmployerPostRequest(condition);
    }
    private void requestForRefreshEmployerPost(){
        postRequestPerformingOrNot = true;

        String condition = ">'" + employerPostInfoArrayList.get(0).getDateTimePosted() + "'";
        ClientCore.sentRefreshEmployerPostRequest(condition);
    }
    private void requestForLoadMoreEmployerPost(){
        postRequestPerformingOrNot = true;

        String condition = "<'" + employerPostInfoArrayList.get((employerPostInfoArrayList.size()-1)).getDateTimePosted() + "'";

        Log.i(TAG, "The Request Load more post Condition is : " + condition);
        ClientCore.sentLoadMoreEmployerPostRequest(condition);
    }

    public void requestForCheckProfile(String username){
        Log.i(TAG, "Check Profile request for user : " + username);
        ((StudentHomepage)getActivity()).showLoadingScreen(true);
        ((StudentHomepage)getActivity()).freezeScreen(true);
        ClientCore.sentStudentCheckProfile(username);
    }

    public static void updateEmployerPost(String updateType, ArrayList<EmployerPostInfo> receivedEmployerPostInfoArrayList, boolean refreshOrNot){

        Log.i(TAG, "Update EmployerPost Called");
        switch (updateType){
            case "getEmployerPost":
                employerPostInfoArrayList = receivedEmployerPostInfoArrayList;


                break;

            case "refreshEmployerPost":
                //RefreashEmployerPost mean add new post in front of the currentArraylist

                ArrayList<EmployerPostInfo> newEmployerPostInfoArrayList = new ArrayList<>();

                for(int counter = 0; counter < employerPostInfoArrayList.size(); counter++){
                    EmployerPostInfo currentEmployerPostInfoInLoop = employerPostInfoArrayList.get(counter);

                    for (int num = 0; num < receivedEmployerPostInfoArrayList.size(); num++){
                        if(receivedEmployerPostInfoArrayList.get(num).getHirePostId().equals(currentEmployerPostInfoInLoop.getHirePostId())){
                            //if alreadt exist dun add
                            receivedEmployerPostInfoArrayList.remove(num);
                        }

                    }


                }

                //first: add the new employer post into the new arraylist
                for (int i = 0; i < receivedEmployerPostInfoArrayList.size(); i++){
                    newEmployerPostInfoArrayList.add(receivedEmployerPostInfoArrayList.get(i));
                }

                //secondly: add the Existing employer post into the new arraylist
                for (int i = 0; i < employerPostInfoArrayList.size(); i++){
                    newEmployerPostInfoArrayList.add(employerPostInfoArrayList.get(i));
                }

                //lastly: update the arraylist
                employerPostInfoArrayList = newEmployerPostInfoArrayList;

                mySwipeRefreshLayout.setRefreshing(false);

                break;
            case "loadMoreEmployerPost":

                //loadMoreEmployerPost mean add more post


                //add all receivedEmployerpost behind the current array list
                for(int i = 0; i< receivedEmployerPostInfoArrayList.size(); i++){
                    employerPostInfoArrayList.add(receivedEmployerPostInfoArrayList.get(i));
                }

                break;

        }

        Log.i(TAG, "employer list array size is " + employerPostInfoArrayList.size());


        if(refreshOrNot) {
            recyclerViewAdapter.setNewArrayList(employerPostInfoArrayList);
            recyclerViewAdapter.notifyDataSetChanged();
        }

        postRequestPerformingOrNot = false;// done request: Unlock the postrequest



    }



}

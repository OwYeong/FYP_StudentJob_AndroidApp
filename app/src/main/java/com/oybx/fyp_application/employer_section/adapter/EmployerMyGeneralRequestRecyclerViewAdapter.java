package com.oybx.fyp_application.employer_section.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.fragment.employer_requestpage.FragmentEmployerSpecificPostRequest;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyRequest;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

public class EmployerMyGeneralRequestRecyclerViewAdapter extends RecyclerView.Adapter<EmployerMyGeneralRequestRecyclerViewAdapter.MyViewHolder> {
    //this adapter is created for the employer homepage MyPost RecyclerView

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestInfoNestedArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerMyGeneralRequestRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestInfoNestedArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerMyRequestInfoNestedArrayList = employerMyRequestInfoNestedArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_myrequest, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final ArrayList<EmployerMyRequestInfo> currentEmployerRequestInfoArrayList = employerMyRequestInfoNestedArrayList.get(position);
        String currentEmployerRequestHirePostId = currentEmployerRequestInfoArrayList.get(0).getHirePostId();

        myViewHolder.postRequestNum_textView.setText(Integer.toString(currentEmployerRequestInfoArrayList.size()));
        myViewHolder.jobType_textView.setText(NestedFragmentEmployerMyPost.getJobTypeWithHirePostId(currentEmployerRequestHirePostId) + " Job");

        myViewHolder.postTitle_textView.setText(NestedFragmentEmployerMyPost.getPostTitleWithHirePostId(currentEmployerRequestHirePostId));
        myViewHolder.offers_textView.setText(NestedFragmentEmployerMyPost.getPostOffersWithHirePostId(currentEmployerRequestHirePostId));


        myViewHolder.bigContainer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                currentActivity.runOnUiThread(new Runnable() {
                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    @Override
                    public void run() {
                        if (currentLayoutName.equals("EmployerRequestpage")) {

                            Fragment specificPostRequestFragment = new FragmentEmployerSpecificPostRequest();
                            FragmentEmployerSpecificPostRequest.setMySpecificPostRequestArrayList(currentEmployerRequestInfoArrayList);



                            ((EmployerRequestpage)currentActivity).changeFragmentPage(specificPostRequestFragment, "employerSpecificPostRequestFragment",null);


                            // Stuff that updates the UI
                        }

                    }
                });

            }
        });





    }

    @Override
    public int getItemCount() {

        if (employerMyRequestInfoNestedArrayList.size() == 0) {

            ((NestedFragmentEmployerMyRequest)parentFragment).displayNoRequestSection(true);

        }else {
            ((NestedFragmentEmployerMyRequest)parentFragment).displayNoRequestSection(false);

        }
        //return the item count in list
        return employerMyRequestInfoNestedArrayList.size();
    }

    public void setNewArrayList(ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestInfoNestedArrayList){
        this.employerMyRequestInfoNestedArrayList = employerMyRequestInfoNestedArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout bigContainer_linearLayout;
        public TextView jobType_textView, offers_textView, postTitle_textView, postRequestNum_textView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bigContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_employer_myrequest_bigContainer_linearLayout);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_postTitle);
            postRequestNum_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_postRequestNum);

        }
    }
}

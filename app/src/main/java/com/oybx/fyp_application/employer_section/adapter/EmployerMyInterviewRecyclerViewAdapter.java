package com.oybx.fyp_application.employer_section.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyInterview;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

public class EmployerMyInterviewRecyclerViewAdapter extends RecyclerView.Adapter<EmployerMyInterviewRecyclerViewAdapter.MyViewHolder> {
    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<EmployerMyRequestInfo> employerMyConfirmedInterviewInfoArrayList = new ArrayList<>();//Array list For storing Employer Confirmed Interview info


    public EmployerMyInterviewRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<EmployerMyRequestInfo> employerMyConfirmedInterviewInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerMyConfirmedInterviewInfoArrayList = employerMyConfirmedInterviewInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_my_interview, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final EmployerMyRequestInfo currentEmployerMyInterviewInfo = employerMyConfirmedInterviewInfoArrayList.get(position);

        String currentEmployerInterviewHirePostId = currentEmployerMyInterviewInfo.getHirePostId();


        if(!NestedFragmentEmployerMyPost.getJobTypeWithHirePostId(currentEmployerInterviewHirePostId).equals(("-1"))){
            //mean this post is not deleted


            myViewHolder.jobType_textView.setText(NestedFragmentEmployerMyPost.getJobTypeWithHirePostId(currentEmployerInterviewHirePostId) + " Job");

            myViewHolder.postTitle_textView.setText(NestedFragmentEmployerMyPost.getPostTitleWithHirePostId(currentEmployerInterviewHirePostId));
            myViewHolder.offers_textView.setText(NestedFragmentEmployerMyPost.getPostOffersWithHirePostId(currentEmployerInterviewHirePostId));


            myViewHolder.interviewDate_textView.setText(currentEmployerMyInterviewInfo.getInterviewDate());
            myViewHolder.interviewTime_textView.setText(currentEmployerMyInterviewInfo.getInterviewTime());
            myViewHolder.interviewLocation_textView.setText(currentEmployerMyInterviewInfo.getInterviewLocation());



        }







    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(employerMyConfirmedInterviewInfoArrayList.size() == 0){
            ((NestedFragmentEmployerMyInterview)parentFragment).displayNoInterviewSection(true);
        }else {
            ((NestedFragmentEmployerMyInterview)parentFragment).displayNoInterviewSection(false);

        }
        return employerMyConfirmedInterviewInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<EmployerMyRequestInfo> employerMyConfirmedInterviewInfoArrayList){
        this.employerMyConfirmedInterviewInfoArrayList = employerMyConfirmedInterviewInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView jobType_textView, offers_textView, postTitle_textView;
        public TextView interviewDate_textView, interviewTime_textView, interviewLocation_textView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            jobType_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_postTitle);


            interviewDate_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_date);
            interviewTime_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_time);
            interviewLocation_textView = (TextView) itemView.findViewById(R.id.layout_employer_myInterview_location);




        }
    }


}



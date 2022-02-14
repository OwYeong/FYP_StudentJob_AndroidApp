package com.oybx.fyp_application.student_section.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentHistory;

import java.util.ArrayList;

public class StudentMyRequestHistoryRecyclerViewAdapter extends RecyclerView.Adapter<StudentMyRequestHistoryRecyclerViewAdapter.MyViewHolder> {

    //this adapter is created for the employer homepage MyPost RecyclerView

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<StudentMyRequestInfo> studentMyRequestHistoryInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public StudentMyRequestHistoryRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<StudentMyRequestInfo> studentMyRequestHistoryInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.studentMyRequestHistoryInfoArrayList = studentMyRequestHistoryInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_myrequest_history, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final StudentMyRequestInfo currentStudentMyRequestHistoryInfo = studentMyRequestHistoryInfoArrayList.get(position);




        myViewHolder.jobType_textView.setText(currentStudentMyRequestHistoryInfo.getHirePostInfo().getJobType());

        myViewHolder.postTitle_textView.setText(currentStudentMyRequestHistoryInfo.getHirePostInfo().getPostTitle());
        myViewHolder.offers_textView.setText(currentStudentMyRequestHistoryInfo.getHirePostInfo().getOffers());


        if(currentStudentMyRequestHistoryInfo.getPostRequestStatus().equals("accepted")){

            if(currentStudentMyRequestHistoryInfo.getInterviewRequestStatus().equals("accepted")){
                myViewHolder.acceptedSection_relativeLayout.setVisibility(View.VISIBLE);
                myViewHolder.rejectedSection_relativeLayout.setVisibility(View.GONE);

            }else if(currentStudentMyRequestHistoryInfo.getInterviewRequestStatus().equals("rejected")){
                myViewHolder.acceptedSection_relativeLayout.setVisibility(View.GONE);
                myViewHolder.rejectedSection_relativeLayout.setVisibility(View.VISIBLE);

            }


        }else {
            //rejected

            myViewHolder.acceptedSection_relativeLayout.setVisibility(View.GONE);
            myViewHolder.rejectedSection_relativeLayout.setVisibility(View.VISIBLE);
        }







    }

    @Override
    public int getItemCount() {
        if(studentMyRequestHistoryInfoArrayList.size() == 0){
            ((NestedFragmentStudentHistory)parentFragment).displayNoHistorySection(true);
        }else {
            ((NestedFragmentStudentHistory)parentFragment).displayNoHistorySection(false);
        }

        //return the item count in list
        return studentMyRequestHistoryInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<StudentMyRequestInfo> studentMyRequestHistoryInfoArrayList){
        this.studentMyRequestHistoryInfoArrayList = studentMyRequestHistoryInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout parentContainer_linearLayout;
        public LinearLayout bigContainer_linearLayout;
        public TextView jobType_textView, offers_textView, postTitle_textView;

        public RelativeLayout acceptedSection_relativeLayout, rejectedSection_relativeLayout;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parentContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_student_myrequest_history_parentLinearLayout);
            bigContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_student_myrequest_history_bigContainer_linearLayout);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_student_myrequest_history_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_student_myrequest_history_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_student_myrequest_history_postTitle);

            acceptedSection_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_myrequest_history_accepted_section);
            rejectedSection_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_myrequest_history_rejected_section);



        }
    }
}

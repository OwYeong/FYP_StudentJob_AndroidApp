package com.oybx.fyp_application.student_section.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentInterviewRequest;

import java.util.ArrayList;

public class StudentMyConfirmedInterviewRecyclerViewAdapter extends RecyclerView.Adapter<StudentMyConfirmedInterviewRecyclerViewAdapter.MyViewHolder> {

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<StudentMyRequestInfo> studentMyInterviewInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public StudentMyConfirmedInterviewRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<StudentMyRequestInfo> studentMyInterviewInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.studentMyInterviewInfoArrayList = studentMyInterviewInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_my_interview, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final StudentMyRequestInfo currentStudentMyInterviewInfo = studentMyInterviewInfoArrayList.get(position);



        myViewHolder.jobType_textView.setText(currentStudentMyInterviewInfo.getHirePostInfo().getJobType() + " Job");

        myViewHolder.postTitle_textView.setText(currentStudentMyInterviewInfo.getHirePostInfo().getPostTitle());
        myViewHolder.offers_textView.setText(currentStudentMyInterviewInfo.getHirePostInfo().getOffers());


        myViewHolder.interviewDate_textView.setText(currentStudentMyInterviewInfo.getInterviewDate());
        myViewHolder.interviewTime_textView.setText(currentStudentMyInterviewInfo.getInterviewTime());
        myViewHolder.interviewLocation_textView.setText(currentStudentMyInterviewInfo.getInterviewLocation());








    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(studentMyInterviewInfoArrayList.size() == 0){
            ((NestedFragmentStudentInterviewRequest)parentFragment).displayNoInterviewSection(true);
        }else {
            ((NestedFragmentStudentInterviewRequest)parentFragment).displayNoInterviewSection(false);

        }
        return studentMyInterviewInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<StudentMyRequestInfo> studentMyInterviewInfoArrayList){
        this.studentMyInterviewInfoArrayList = studentMyInterviewInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView jobType_textView, offers_textView, postTitle_textView;
        public TextView interviewDate_textView, interviewTime_textView, interviewLocation_textView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            jobType_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_postTitle);


            interviewDate_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_date);
            interviewTime_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_time);
            interviewLocation_textView = (TextView) itemView.findViewById(R.id.layout_student_myInterview_location);




        }
    }

}


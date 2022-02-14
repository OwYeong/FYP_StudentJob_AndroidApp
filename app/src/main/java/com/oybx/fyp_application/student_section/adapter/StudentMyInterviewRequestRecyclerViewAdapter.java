package com.oybx.fyp_application.student_section.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentMyRequest;

import java.util.ArrayList;

public class StudentMyInterviewRequestRecyclerViewAdapter extends RecyclerView.Adapter<StudentMyInterviewRequestRecyclerViewAdapter.MyViewHolder> {

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public StudentMyInterviewRequestRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.studentMyRequestInfoArrayList = studentMyRequestInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_interview_request, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final StudentMyRequestInfo currentStudentRequestInfo = studentMyRequestInfoArrayList.get(position);



        myViewHolder.jobType_textView.setText(currentStudentRequestInfo.getHirePostInfo().getJobType() + " Job");

        myViewHolder.postTitle_textView.setText(currentStudentRequestInfo.getHirePostInfo().getPostTitle());
        myViewHolder.offers_textView.setText(currentStudentRequestInfo.getHirePostInfo().getOffers());

        myViewHolder.empProPic_imageView.setImageBitmap(currentStudentRequestInfo.getHirePostInfo().getProPic());
        myViewHolder.empName_textView.setText(currentStudentRequestInfo.getHirePostInfo().getName());
        myViewHolder.requestedDateTime_textView.setText(currentStudentRequestInfo.getInterviewRequestDateTime());
        myViewHolder.interviewMessage_textView.setText(currentStudentRequestInfo.getInterviewRequestMessage());
        myViewHolder.interviewDate_textView.setText(currentStudentRequestInfo.getInterviewDate());
        myViewHolder.interviewTime_textView.setText(currentStudentRequestInfo.getInterviewTime());
        myViewHolder.interviewLocation_textView.setText(currentStudentRequestInfo.getInterviewLocation());


        myViewHolder.employerInfoContainer_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NestedFragmentStudentMyRequest)parentFragment).requestForCheckProfile(currentStudentRequestInfo.getHirePostInfo().getEmployerUsername());

            }
        });

        myViewHolder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NestedFragmentStudentMyRequest)parentFragment).promptUserAcceptOrNot("Are You Sure You Want To Accept This Interview Request. After Clicking Yes, Notification Will Be Send To The Employer And Interview With Employer Will Be Confirmed.  Action Cannot Be Undone."
                        , currentStudentRequestInfo.getHirePostId(), currentStudentRequestInfo.getRequesterUsername(), Integer.toString(position));



            }
        });

        myViewHolder.decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NestedFragmentStudentMyRequest)parentFragment).promptUserDeclineOrNot("Are You Sure You Want To Decline This Interview Request. After Clicking Yes, Notification Will Be Send To The Employer And Interview Request Will Be Canceled.  Action Cannot Be Undone."
                        , currentStudentRequestInfo.getHirePostId(), currentStudentRequestInfo.getRequesterUsername(), Integer.toString(position));

            }
        });

        myViewHolder.discussTimeTable_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoomUserInfo chatRoomUserInfo = new ChatRoomUserInfo(currentStudentRequestInfo.getHirePostInfo().getEmployerUsername(),currentStudentRequestInfo.getHirePostInfo().getProPic(),currentStudentRequestInfo.getHirePostInfo().getName(),null, "Employer");
                ((NestedFragmentStudentMyRequest)parentFragment).dicussTimeTableWithHim(chatRoomUserInfo);
            }
        });





    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(studentMyRequestInfoArrayList.size() == 0){
            ((NestedFragmentStudentMyRequest)parentFragment).displayNoInterviewRequestSection(true);
        }else {
            ((NestedFragmentStudentMyRequest)parentFragment).displayNoInterviewRequestSection(false);

        }
        return studentMyRequestInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList){
        this.studentMyRequestInfoArrayList = studentMyRequestInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout buttonSection_relativeLayout;
        public RelativeLayout employerInfoContainer_relativeLayout;

        public TextView jobType_textView, offers_textView, postTitle_textView;
        public ImageView empProPic_imageView;
        public TextView empName_textView, requestedDateTime_textView;
        public TextView interviewMessage_textView, interviewDate_textView, interviewTime_textView, interviewLocation_textView;
        public Button accept_button, decline_button;
        public Button discussTimeTable_button;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            employerInfoContainer_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_interview_request_employerInfo_container);
            buttonSection_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_interview_request_button_section);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_postTitle);

            empProPic_imageView = (ImageView) itemView.findViewById(R.id.layout_student_interview_request_profilePic);
            empName_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_name);
            requestedDateTime_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_requestedDateTime);

            interviewMessage_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_message);
            interviewDate_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_date);
            interviewTime_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_time);
            interviewLocation_textView = (TextView) itemView.findViewById(R.id.layout_student_interview_request_location);



            accept_button = (Button) itemView.findViewById(R.id.layout_student_interview_request_accept_btn);
            decline_button = (Button) itemView.findViewById(R.id.layout_student_interview_request_decline_btn);
            discussTimeTable_button = (Button) itemView.findViewById(R.id.layout_student_interview_request_discussTimetable_btn);


        }
    }
}

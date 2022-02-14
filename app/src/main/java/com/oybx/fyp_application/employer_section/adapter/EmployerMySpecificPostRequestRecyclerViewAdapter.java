package com.oybx.fyp_application.employer_section.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.fragment.employer_profilepage.FragmentEmployerInterviewRequestInfopage;
import com.oybx.fyp_application.employer_section.fragment.employer_requestpage.FragmentEmployerSpecificPostRequest;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

public class EmployerMySpecificPostRequestRecyclerViewAdapter extends RecyclerView.Adapter<EmployerMySpecificPostRequestRecyclerViewAdapter.MyViewHolder> {

    //this adapter is created for the employer homepage MyPost RecyclerView

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<EmployerMyRequestInfo> employerMyRequestInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerMySpecificPostRequestRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<EmployerMyRequestInfo> employerMyRequestInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerMyRequestInfoArrayList = employerMyRequestInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_specific_post_request, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final EmployerMyRequestInfo currentEmployerMyRequestInfo = employerMyRequestInfoArrayList.get(position);



        if(currentEmployerMyRequestInfo.getPostRequestStatus().equals("pending") || currentEmployerMyRequestInfo.getPostRequestStatus().equals("accepted") ){


            myViewHolder.requesterProPic_imageView.setImageBitmap(currentEmployerMyRequestInfo.getRequesterProPic());
            myViewHolder.requesterName_textView.setText(currentEmployerMyRequestInfo.getRequesterName());
            myViewHolder.requesterSkillCat_textView.setText(currentEmployerMyRequestInfo.getRequesterSkillCat());
            myViewHolder.requesterMessage_textView.setText(currentEmployerMyRequestInfo.getPostRequestMessage());
            myViewHolder.requestedDateTime_textView.setText(currentEmployerMyRequestInfo.getPostRequestStatusAddedDateTime());

            myViewHolder.buttonSection_relativeLayout.setVisibility(View.VISIBLE);
            myViewHolder.interviewRequestPending_linearLayout.setVisibility(View.GONE);
            myViewHolder.rejected_relativeLayout.setVisibility(View.GONE);
            myViewHolder.accepted_relativeLayout.setVisibility(View.GONE);


            if(currentEmployerMyRequestInfo.getPostRequestStatus().equals("accepted")) {

                if (currentEmployerMyRequestInfo.getInterviewRequestStatus() == null) {

                    //error cause if the status is accepted. We force the employer to sent a interview reuest



                } else if (currentEmployerMyRequestInfo.getInterviewRequestStatus().equals("pending")) {
                    //is not null
                    myViewHolder.buttonSection_relativeLayout.setVisibility(View.GONE);
                    myViewHolder.rejected_relativeLayout.setVisibility(View.GONE);
                    myViewHolder.accepted_relativeLayout.setVisibility(View.GONE);
                    myViewHolder.interviewRequestPending_linearLayout.setVisibility(View.VISIBLE);

                } else if (currentEmployerMyRequestInfo.getInterviewRequestStatus().equals("accepted")) {
                    myViewHolder.accepted_relativeLayout.setVisibility(View.VISIBLE);
                    myViewHolder.rejected_relativeLayout.setVisibility(View.GONE);
                    myViewHolder.interviewRequestPending_linearLayout.setVisibility(View.GONE);
                    myViewHolder.buttonSection_relativeLayout.setVisibility(View.GONE);

                } else if (currentEmployerMyRequestInfo.getInterviewRequestStatus().equals("rejected")) {
                    myViewHolder.accepted_relativeLayout.setVisibility(View.GONE);
                    myViewHolder.rejected_relativeLayout.setVisibility(View.VISIBLE);
                    myViewHolder.interviewRequestPending_linearLayout.setVisibility(View.GONE);
                    myViewHolder.buttonSection_relativeLayout.setVisibility(View.GONE);
                }
            }


        }else {
            //rejected

            myViewHolder.requesterProPic_imageView.setImageBitmap(currentEmployerMyRequestInfo.getRequesterProPic());
            myViewHolder.requesterName_textView.setText(currentEmployerMyRequestInfo.getRequesterName());
            myViewHolder.requesterSkillCat_textView.setText(currentEmployerMyRequestInfo.getRequesterSkillCat());
            myViewHolder.requesterMessage_textView.setText(currentEmployerMyRequestInfo.getPostRequestMessage());
            myViewHolder.requestedDateTime_textView.setText(currentEmployerMyRequestInfo.getPostRequestStatusAddedDateTime());

            myViewHolder.accepted_relativeLayout.setVisibility(View.GONE);
            myViewHolder.rejected_relativeLayout.setVisibility(View.VISIBLE);
            myViewHolder.interviewRequestPending_linearLayout.setVisibility(View.GONE);
            myViewHolder.buttonSection_relativeLayout.setVisibility(View.GONE);

        }

        myViewHolder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("adapter", "the accept button on hirepost id clicked: " + currentEmployerMyRequestInfo.getHirePostId());


                if(!((FragmentEmployerSpecificPostRequest)parentFragment).actionLock){
                    //if actionlock is false
                    ((FragmentEmployerSpecificPostRequest)parentFragment).promptUserForInterviewRequestInfo(currentEmployerMyRequestInfo.getHirePostId(), currentEmployerMyRequestInfo.getRequestUsername(), position, "acceptPostRequest");
                }






            }




        });

        myViewHolder.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("adapter", "the decline_btn on hirepost id clicked: " + currentEmployerMyRequestInfo.getHirePostId());

                if(!((FragmentEmployerSpecificPostRequest)parentFragment).actionLock) {
                    //if actionlock is false
                    ((FragmentEmployerSpecificPostRequest)parentFragment).promptUserRejectOrNot("Are You Sure That You Want To Reject This Request? After Clicking Yes, Action Cannot Be Undone."
                            , currentEmployerMyRequestInfo.getHirePostId(), currentEmployerMyRequestInfo.getRequestUsername(), Integer.toString(position));


                }



            }
        });


        myViewHolder.reviseInterviewRequest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!((FragmentEmployerSpecificPostRequest)parentFragment).actionLock){
                    //if actionlock is false
                    ((FragmentEmployerSpecificPostRequest)parentFragment).promptUserForInterviewRequestInfo(currentEmployerMyRequestInfo.getHirePostId(), currentEmployerMyRequestInfo.getRequestUsername(), position, "reviseInterviewRequest", currentEmployerMyRequestInfo);
                }

            }
        });
        myViewHolder.interviewRequestPending_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!((FragmentEmployerSpecificPostRequest)parentFragment).actionLock) {
                    //if actionlock is false



                    final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerRequestpage")) {

                                Fragment interviewRequestInfoFragment = new FragmentEmployerInterviewRequestInfopage();
                                ((FragmentEmployerInterviewRequestInfopage) interviewRequestInfoFragment).setSelectedEmployerMyRequestInfo(currentEmployerMyRequestInfo);


                                ((EmployerRequestpage)currentActivity).changeFragmentPage(interviewRequestInfoFragment, null,null);


                                // Stuff that updates the UI
                            }

                        }
                    });
                }

            }
        });

        myViewHolder.empInfoHeaderContainer_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentEmployerSpecificPostRequest)parentFragment).requestForCheckProfile(currentEmployerMyRequestInfo.getRequestUsername());

            }
        });



    }

    @Override
    public int getItemCount() {
        //return the item count in list
        return employerMyRequestInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<EmployerMyRequestInfo> employerMyRequestInfoArrayList){
        this.employerMyRequestInfoArrayList = employerMyRequestInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView requesterProPic_imageView;


        TextView requesterName_textView, requesterSkillCat_textView, requesterMessage_textView;
        TextView requestedDateTime_textView;


        RelativeLayout buttonSection_relativeLayout;
        LinearLayout interviewRequestPending_linearLayout;
        RelativeLayout accepted_relativeLayout, rejected_relativeLayout;
        RelativeLayout empInfoHeaderContainer_relativeLayout;
        Button accept_btn, decline_btn;
        Button reviseInterviewRequest_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            empInfoHeaderContainer_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_employer_specific_post_request_headerContainer);
            buttonSection_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_employer_specific_post_request_button_section);
            interviewRequestPending_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_employer_specific_post_request_interviewRequest_section);
            accepted_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_employer_specific_post_request_accepted_section);
            rejected_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_employer_specific_post_request_rejected_section);

            requestedDateTime_textView = (TextView) itemView.findViewById(R.id.layout_employer_specific_post_request_contentContainer_requestedDateTime);
            requesterProPic_imageView = (ImageView) itemView.findViewById(R.id.layout_employer_specific_post_request_headerContainer_profilePic);
            requesterName_textView = (TextView) itemView.findViewById(R.id.layout_employer_specific_post_request_headerContainer_name);
            requesterSkillCat_textView = (TextView) itemView.findViewById(R.id.layout_employer_specific_post_request_headerContainer_skillCat);
            requesterMessage_textView = (TextView) itemView.findViewById(R.id.layout_employer_specific_post_request_contentContainer_message);

            reviseInterviewRequest_btn = (Button) itemView.findViewById(R.id.layout_employer_specific_post_request_reviseTimetable_btn);

            accept_btn = itemView.findViewById(R.id.layout_employer_specific_post_request_accept_btn);
            decline_btn = itemView.findViewById(R.id.layout_employer_specific_post_request_decline_btn);
        }
    }
}

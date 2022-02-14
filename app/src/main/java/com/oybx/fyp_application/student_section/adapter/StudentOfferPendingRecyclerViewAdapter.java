package com.oybx.fyp_application.student_section.adapter;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentMyOffer;
import com.oybx.fyp_application.student_section.fragment.student_requestpage.FragmentStudentEmployerPostPreviewpage;
import com.oybx.fyp_application.student_section.fragment.student_requestpage.FragmentStudentPostRequestPreviewpage;

import java.util.ArrayList;

public class StudentOfferPendingRecyclerViewAdapter extends RecyclerView.Adapter<StudentOfferPendingRecyclerViewAdapter.MyViewHolder> {

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public StudentOfferPendingRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.studentMyRequestInfoArrayList = studentMyRequestInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_offer_pending, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final StudentMyRequestInfo currentStudentRequestInfo = studentMyRequestInfoArrayList.get(position);



        myViewHolder.jobType_textView.setText(currentStudentRequestInfo.getHirePostInfo().getJobType() + " Job");

        myViewHolder.postTitle_textView.setText(currentStudentRequestInfo.getHirePostInfo().getPostTitle());
        myViewHolder.offers_textView.setText(currentStudentRequestInfo.getHirePostInfo().getOffers());


        if(currentStudentRequestInfo.getPostRequestStatus().equals("pending") || currentStudentRequestInfo.getPostRequestStatus().equals("accepted")){

            if(currentStudentRequestInfo.getInterviewRequestStatus() == null){
                myViewHolder.status_textView.setText("Status: Pending For Employer Reply");
                myViewHolder.buttonSection_relativeLayout.setVisibility(View.VISIBLE);


            }else if(currentStudentRequestInfo.getInterviewRequestStatus().equals("pending")){
                myViewHolder.status_textView.setText("Status: Employer Accepted Your Request");
                myViewHolder.buttonSection_relativeLayout.setVisibility(View.GONE);

            }


        }

        myViewHolder.cancelRequest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NestedFragmentStudentMyOffer)parentFragment).promptUserDeleteOrNot("Are You Sure That You Want To Cancel Your Job Request For This Post? After Clicking Yes, Job Request For This Post Will Be Remove. Action Cannot Be Undone."
                        , currentStudentRequestInfo.getHirePostId(), currentStudentRequestInfo.getRequesterUsername(), Integer.toString(position));



            }
        });

        myViewHolder.checkMyRequestInfo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                currentActivity.runOnUiThread(new Runnable() {
                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    @Override
                    public void run() {
                        if (currentLayoutName.equals("StudentRequestpage")) {

                            Fragment postRequestInfoFragment = new FragmentStudentPostRequestPreviewpage();
                            ((FragmentStudentPostRequestPreviewpage) postRequestInfoFragment).setSelectedStudentMyRequestInfo(currentStudentRequestInfo);


                            ((StudentRequestpage)currentActivity).changeFragmentPage(postRequestInfoFragment, null,null);


                            // Stuff that updates the UI
                        }

                    }
                });
            }
        });

        myViewHolder.bigContainer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                currentActivity.runOnUiThread(new Runnable() {
                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    @Override
                    public void run() {
                        if (currentLayoutName.equals("StudentRequestpage")) {

                            Fragment employerPostInfoFragment = new FragmentStudentEmployerPostPreviewpage();
                            ((FragmentStudentEmployerPostPreviewpage) employerPostInfoFragment).setSelectedEmployerPostInfo(currentStudentRequestInfo.getHirePostInfo());


                            ((StudentRequestpage)currentActivity).changeFragmentPage(employerPostInfoFragment, null,null);


                            // Stuff that updates the UI
                        }

                    }
                });
            }
        });





    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(studentMyRequestInfoArrayList.size() == 0){
            ((NestedFragmentStudentMyOffer)parentFragment).displayNoOfferPendingSection(true);
        }else {
            ((NestedFragmentStudentMyOffer)parentFragment).displayNoOfferPendingSection(false);

        }
        return studentMyRequestInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList){
        this.studentMyRequestInfoArrayList = studentMyRequestInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout buttonSection_relativeLayout;
        public LinearLayout bigContainer_linearLayout;

        public TextView jobType_textView, offers_textView, postTitle_textView;
        public TextView status_textView;
        public Button cancelRequest_button, checkMyRequestInfo_button;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            buttonSection_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_offer_pending_button_section);
            bigContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_student_offer_pending_container_linearLayout);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_student_offer_pending_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_student_offer_pending_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_student_offer_pending_postTitle);

            status_textView = (TextView) itemView.findViewById(R.id.layout_student_offer_pending_status);
            cancelRequest_button = (Button) itemView.findViewById(R.id.layout_student_offer_pending_cancel_btn);
            checkMyRequestInfo_button = (Button) itemView.findViewById(R.id.layout_student_offer_pending_checkMyRequest_btn);




        }
    }

}


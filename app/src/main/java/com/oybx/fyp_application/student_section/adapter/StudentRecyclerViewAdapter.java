package com.oybx.fyp_application.student_section.adapter;

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
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentNewFeed;

import java.util.ArrayList;

public class StudentRecyclerViewAdapter extends RecyclerView.Adapter<StudentRecyclerViewAdapter.MyViewHolder> {

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<EmployerPostInfo> employerPostInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public StudentRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<EmployerPostInfo> employerPostInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerPostInfoArrayList = employerPostInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_newfeed_employer_post, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        final EmployerPostInfo currentEmployerPostInfo = employerPostInfoArrayList.get(position);

        myViewHolder.employerName_textView.setText(currentEmployerPostInfo.getName());
        myViewHolder.postedDateTime_textView.setText(currentEmployerPostInfo.getDateTimePosted());
        myViewHolder.jobType_textView.setText(currentEmployerPostInfo.getJobType() + " Job");

        myViewHolder.postTitle_textView.setText(currentEmployerPostInfo.getPostTitle());
        Log.i("adapter", "post title is " + currentEmployerPostInfo.getPostTitle());
        myViewHolder.postDesc_textView.setText(currentEmployerPostInfo.getPostDesc());

        myViewHolder.proPic_imageView.setImageBitmap(currentEmployerPostInfo.getProPic());
        myViewHolder.postPic_imageView.setImageBitmap(currentEmployerPostInfo.getPostPic());


        if (currentEmployerPostInfo.getJobType().equals("Freelance")){
            myViewHolder.offers_textView.setText(currentEmployerPostInfo.getOffers() + " / Job");

            myViewHolder.partTimeAdditionContainer_linearLayout.setVisibility(View.GONE);
        }else if (currentEmployerPostInfo.getJobType().equals("PartTime")){
            myViewHolder.partTimeAdditionContainer_linearLayout.setVisibility(View.VISIBLE);
            myViewHolder.offers_textView.setText(currentEmployerPostInfo.getOffers() + " / Hrs");
            myViewHolder.workingHours_textView.setText(currentEmployerPostInfo.getWorkingHours());
            myViewHolder.location_textView.setText(currentEmployerPostInfo.getLocation());
        }

        myViewHolder.applyNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("adapter", "the button on profile id clicked: " + currentEmployerPostInfo.getHirePostId());
                ((NestedFragmentStudentNewFeed)parentFragment).promptUserApplyForPostSection(currentEmployerPostInfo.getHirePostId());

            }
        });

        myViewHolder.additionInfoContainer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NestedFragmentStudentNewFeed)parentFragment).requestForCheckProfile(currentEmployerPostInfo.getEmployerUsername());


            }
        });



    }

    @Override
    public int getItemCount() {
        //return the item count in list
        return employerPostInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<EmployerPostInfo> employerPostInfoArrayList){
        this.employerPostInfoArrayList = employerPostInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView employerName_textView, postedDateTime_textView;
        TextView jobType_textView, offers_textView, postTitle_textView, postDesc_textView;
        TextView workingHours_textView, location_textView;
        ImageView postPic_imageView, proPic_imageView;
        LinearLayout partTimeAdditionContainer_linearLayout,additionInfoContainer_linearLayout;



        Button applyNow_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            employerName_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_headerContainer_name);
            postedDateTime_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_dateTime);
            jobType_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_jobType);
            offers_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_offers);
            postTitle_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_postTitle);
            postDesc_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_description);
            workingHours_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_workingHours);
            location_textView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_Location);

            postPic_imageView = itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postPic);
            proPic_imageView = itemView.findViewById(R.id.newfeed_employer_post_layout_headerContainer_profilePic);

            partTimeAdditionContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.newfeed_employer_post_layout_contentContainer_postDetailContainer_partTimeAdditionInfoContainer);
            additionInfoContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.newfeed_employer_post_layout_headerContainer_additionInfo_container);
            applyNow_btn = itemView.findViewById(R.id.newfeed_employer_post_layout_headerContainer_applyNow_btn);

        }
    }

}

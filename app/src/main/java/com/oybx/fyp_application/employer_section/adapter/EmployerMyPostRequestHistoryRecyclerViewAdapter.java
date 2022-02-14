package com.oybx.fyp_application.employer_section.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerHistory;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

import java.util.ArrayList;

public class EmployerMyPostRequestHistoryRecyclerViewAdapter extends RecyclerView.Adapter<EmployerMyPostRequestHistoryRecyclerViewAdapter.MyViewHolder> {

    //this adapter is created for the employer homepage MyPost RecyclerView

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestHistoryInfoNestedArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerMyPostRequestHistoryRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestHistoryInfoNestedArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerMyRequestHistoryInfoNestedArrayList = employerMyRequestHistoryInfoNestedArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_myrequest_history, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        final ArrayList<EmployerMyRequestInfo> currentEmployerMyRequestHistoryInfoArrayList = employerMyRequestHistoryInfoNestedArrayList.get(position);
        String currentEmployerRequestHirePostId = currentEmployerMyRequestHistoryInfoArrayList.get(0).getHirePostId();





        myViewHolder.jobType_textView.setText(NestedFragmentEmployerMyPost.getJobTypeWithHirePostId(currentEmployerRequestHirePostId));

        myViewHolder.postTitle_textView.setText(NestedFragmentEmployerMyPost.getPostTitleWithHirePostId(currentEmployerRequestHirePostId));
        myViewHolder.offers_textView.setText(NestedFragmentEmployerMyPost.getPostOffersWithHirePostId(currentEmployerRequestHirePostId));


        for(int i = 0; i < currentEmployerMyRequestHistoryInfoArrayList.size(); i++ ){

            Bitmap proPic = currentEmployerMyRequestHistoryInfoArrayList.get(i).getRequesterProPic();
            String name = currentEmployerMyRequestHistoryInfoArrayList.get(i).getRequesterName();
            String skillCat = currentEmployerMyRequestHistoryInfoArrayList.get(i).getRequesterSkillCat();
            String statusAddedDateTime = currentEmployerMyRequestHistoryInfoArrayList.get(i).getPostRequestStatusAddedDateTime();
            String status = "-1";
            if(currentEmployerMyRequestHistoryInfoArrayList.get(i).getPostRequestStatus().equals("accepted")){

                if (currentEmployerMyRequestHistoryInfoArrayList.get(i).getInterviewRequestStatus().equals("accepted")){
                    //rejected
                    status = "accepted";

                }else {
                    //rejected
                    status = "rejected";

                }

            }else {
                //rejected
                status = "rejected";
            }

            LinearLayout historyRecord = getCustomHistoryRecord(currentEmployerMyRequestHistoryInfoArrayList.get(i).getRequestUsername(), proPic, name, skillCat, statusAddedDateTime, status);

            if(i == (currentEmployerMyRequestHistoryInfoArrayList.size() - 1)){
                //if this is the last record, add margin bottom
                LinearLayout.LayoutParams historyRecordLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                historyRecordLayoutParams.setMargins(0,dpiToPx(20),0,dpiToPx(30));

                historyRecord.setLayoutParams(historyRecordLayoutParams);
            }
            myViewHolder.parentContainer_linearLayout.addView(historyRecord);

        }





    }

    @Override
    public int getItemCount() {
        if(employerMyRequestHistoryInfoNestedArrayList.size() == 0){
            ((NestedFragmentEmployerHistory)parentFragment).displayNoHistorySection(true);
        }else {
            ((NestedFragmentEmployerHistory)parentFragment).displayNoHistorySection(false);
        }

        //return the item count in list
        return employerMyRequestHistoryInfoNestedArrayList.size();
    }

    public void setNewArrayList(ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyRequestHistoryInfoNestedArrayList){
        this.employerMyRequestHistoryInfoNestedArrayList = employerMyRequestHistoryInfoNestedArrayList;

    }

    public LinearLayout getCustomHistoryRecord(final String studentUsername, Bitmap proPic, String name, String skillCat, String statusAddedDate, String status ){

        //create big container
        LinearLayout bigContainer_linearLayout = new LinearLayout(myContext);
        bigContainer_linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams bigContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bigContainerParams.setMargins(0,dpiToPx(20),0,0);
        bigContainer_linearLayout.setLayoutParams(bigContainerParams);

        RelativeLayout headerContainer_relativeLayout = new RelativeLayout(myContext);
        RelativeLayout.LayoutParams headerContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(70));
        headerContainer_relativeLayout.setLayoutParams(headerContainerParams);

        //create framelayout
        FrameLayout profilePicContainer_frameLayout = new FrameLayout(myContext);
        profilePicContainer_frameLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams profilePicContainerParams = new RelativeLayout.LayoutParams(dpiToPx(70), dpiToPx(70));
        profilePicContainer_frameLayout.setLayoutParams(profilePicContainerParams);

        ImageView profilePic_imageView = new ImageView(myContext);
        FrameLayout.LayoutParams profilePicParams = new FrameLayout.LayoutParams(dpiToPx(65), dpiToPx(65));
        profilePicParams.gravity = Gravity.CENTER;
        profilePic_imageView.setAdjustViewBounds(true);
        profilePic_imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        profilePic_imageView.setLayoutParams(profilePicParams);
        profilePic_imageView.setImageBitmap(proPic);

        ImageView profilePicCover_imageView = new ImageView(myContext);
        FrameLayout.LayoutParams profilePicCoverParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        profilePicCover_imageView.setLayoutParams(profilePicCoverParams);
        profilePicCover_imageView.setImageResource(R.drawable.profile_circle_crop);

        //add these 2 imageview into the container
        profilePicContainer_frameLayout.addView(profilePic_imageView);
        profilePicContainer_frameLayout.addView(profilePicCover_imageView);


        LinearLayout requesterInfoContainer_linearLayout = new LinearLayout(myContext);
        requesterInfoContainer_linearLayout.setOrientation(LinearLayout.VERTICAL);
        requesterInfoContainer_linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams requesterInfoContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        requesterInfoContainerParams.addRule(RelativeLayout.RIGHT_OF, profilePicContainer_frameLayout.getId());
        requesterInfoContainer_linearLayout.setLayoutParams(requesterInfoContainerParams);
        requesterInfoContainer_linearLayout.setPadding(dpiToPx(20),0, 0,0);

        TextView name_textView = new TextView(myContext);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        name_textView.setLayoutParams(nameParams);
        name_textView.setText(name);
        name_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.colorPrimary));
        Typeface tf = Typeface.createFromAsset((ApplicationManager.getCurrentAppActivityContext()).getAssets() ,"font/textmeone_regular.ttf");//get the font
        name_textView.setTypeface(tf);
        name_textView.setTextSize(23);

        TextView skillCat_textView = new TextView(myContext);
        LinearLayout.LayoutParams skillCatParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        skillCat_textView.setText(skillCat);
        skillCat_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.textColorContrast));
        Typeface skillCatTf = Typeface.createFromAsset((ApplicationManager.getCurrentAppActivityContext()).getAssets() ,"font/textmeone_regular.ttf");//get the font
        skillCat_textView.setTypeface(skillCatTf);
        skillCat_textView.setTextSize(20);
        skillCat_textView.setLayoutParams(skillCatParams);

        //add these 2 textView into the requesterInfoContainer_linearLayout
        requesterInfoContainer_linearLayout.addView(name_textView);
        requesterInfoContainer_linearLayout.addView(skillCat_textView);

        //add both into the parent layout
        headerContainer_relativeLayout.addView(profilePicContainer_frameLayout);
        headerContainer_relativeLayout.addView(requesterInfoContainer_linearLayout);

        headerContainer_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NestedFragmentEmployerHistory)parentFragment).requestForCheckProfile(studentUsername);
            }
        });


        //add to big container
        bigContainer_linearLayout.addView(headerContainer_relativeLayout);

        RelativeLayout statusAddedContainer_relativeLayout = new RelativeLayout(myContext);
        statusAddedContainer_relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams statusAddedContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(30));
        statusAddedContainer_relativeLayout.setLayoutParams(statusAddedContainerParams);

        TextView statusAddedTitle_textView = new TextView(myContext);
        statusAddedTitle_textView.setId(View.generateViewId());
        RelativeLayout.LayoutParams statusAddedTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statusAddedTitle_textView.setText("Status Updated on: ");
        statusAddedTitle_textView.setTypeface(tf);
        statusAddedTitle_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.colorPrimary));
        statusAddedTitle_textView.setTextSize(14);
        statusAddedTitle_textView.setLayoutParams(statusAddedTitleParams);

        TextView statusAddedDateTime_textView = new TextView(myContext);
        RelativeLayout.LayoutParams statusAddedDateTimeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statusAddedDateTimeParams.addRule(RelativeLayout.RIGHT_OF, statusAddedTitle_textView.getId());
        statusAddedDateTime_textView.setText(statusAddedDate);
        statusAddedDateTime_textView.setTypeface(tf);
        statusAddedDateTime_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.colorPrimary));
        statusAddedDateTime_textView.setTextSize(14);
        statusAddedDateTime_textView.setLayoutParams(statusAddedDateTimeParams);

        // add both textview into their parentLayout
        statusAddedContainer_relativeLayout.addView(statusAddedTitle_textView);
        statusAddedContainer_relativeLayout.addView(statusAddedDateTime_textView);

        //add to big container
        bigContainer_linearLayout.addView(statusAddedContainer_relativeLayout);

        TextView accepted_textView = new TextView(myContext);
        LinearLayout.LayoutParams acceptedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(40));
        acceptedParams.setMargins(0,dpiToPx(15),0,0);
        accepted_textView.setText("Accepted");
        Typeface monserrat = Typeface.createFromAsset((ApplicationManager.getCurrentAppActivityContext()).getAssets() ,"font/montserrat_regular.ttf");//get the fon
        accepted_textView.setTypeface(monserrat);
        accepted_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.colorPrimary));
        accepted_textView.setTextSize(15);
        accepted_textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        accepted_textView.setGravity(Gravity.CENTER);
        accepted_textView.setBackgroundResource(R.drawable.green_rounded_square_btn);
        accepted_textView.setLayoutParams(acceptedParams);

        TextView rejected_textView = new TextView(myContext);
        LinearLayout.LayoutParams rejectedParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(40));
        rejectedParams.setMargins(0,dpiToPx(15),0,0);
        rejected_textView.setText("Rejected");
        rejected_textView.setTypeface(monserrat);
        rejected_textView.setTextColor((ApplicationManager.getCurrentAppActivityContext()).getResources().getColor(R.color.colorPrimary));
        rejected_textView.setTextSize(15);
        rejected_textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rejected_textView.setGravity(Gravity.CENTER);
        rejected_textView.setBackgroundResource(R.drawable.red_rounded_square_btn);
        rejected_textView.setLayoutParams(rejectedParams);

        //add both into the big container
        bigContainer_linearLayout.addView(accepted_textView);
        bigContainer_linearLayout.addView(rejected_textView);

        if(status.equals("accepted")){
            accepted_textView.setVisibility(View.VISIBLE);
            rejected_textView.setVisibility(View.GONE);
        }else{
            accepted_textView.setVisibility(View.GONE);
            rejected_textView.setVisibility(View.VISIBLE);
        }


        return bigContainer_linearLayout;

    }

    private int dpiToPx(int dpi){
        final float scale = (ApplicationManager.getCurrentAppActivityContext()).getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int dpiInPx = (int) (dpi * scale + 0.5f);// convert dpi to px

        return dpiInPx;
    }

    private int pxToDpi(int px){
        final float scale = (ApplicationManager.getCurrentAppActivityContext()).getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int pxInDpi = (int) (px / scale + 0.5f);// convert px to dpi

        return pxInDpi;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout parentContainer_linearLayout;
        public LinearLayout bigContainer_linearLayout;
        public TextView jobType_textView, offers_textView, postTitle_textView;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parentContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_employer_myrequest_history_parentLinearLayout);
            bigContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_employer_myrequest_bigContainer_linearLayout);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_history_jobType);
            offers_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_history_offers);
            postTitle_textView = (TextView) itemView.findViewById(R.id.layout_employer_myrequest_history_postTitle);



        }
    }


}

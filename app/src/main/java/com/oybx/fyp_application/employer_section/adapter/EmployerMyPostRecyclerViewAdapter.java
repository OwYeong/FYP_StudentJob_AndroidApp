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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerHomepage;
import com.oybx.fyp_application.employer_section.fragment.employer_homepage.FragmentEmployerEditPostpage;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;

import java.util.ArrayList;

public class EmployerMyPostRecyclerViewAdapter extends RecyclerView.Adapter<EmployerMyPostRecyclerViewAdapter.MyViewHolder> {

    //this adapter is created for the employer homepage MyPost RecyclerView

    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<EmployerPostInfo> employerPostInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerMyPostRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<EmployerPostInfo> employerPostInfoArrayList){

        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.employerPostInfoArrayList = employerPostInfoArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_mypost, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {


        final EmployerPostInfo currentEmployerPostInfo = employerPostInfoArrayList.get(position);


        if(currentEmployerPostInfo.getPostStatus().equals("ongoing")){



            myViewHolder.jobType_textView.setText(currentEmployerPostInfo.getJobType() + " Job");

            myViewHolder.postTitle_textView.setText(currentEmployerPostInfo.getPostTitle());


            if (currentEmployerPostInfo.getJobType().equals("Freelance")){
                myViewHolder.offers_textView.setText(currentEmployerPostInfo.getOffers() + " / Job");

            }else if (currentEmployerPostInfo.getJobType().equals("PartTime")){
                myViewHolder.offers_textView.setText(currentEmployerPostInfo.getOffers() + " / Hrs");

            }

            myViewHolder.editPost_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("adapter", "the editPost_btn button on hirepost id clicked: " + currentEmployerPostInfo.getHirePostId());
                    final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                Fragment editPostFragment = new FragmentEmployerEditPostpage();
                                ((FragmentEmployerEditPostpage) editPostFragment).setPositionInArrayList(position);
                                ((FragmentEmployerEditPostpage) editPostFragment).setSelectedEmployerPostInfo(currentEmployerPostInfo);

                                ((EmployerHomepage)currentActivity).changeFragmentPage(editPostFragment, null,null);


                                // Stuff that updates the UI
                            }

                        }
                    });





                }




            });

            myViewHolder.deletePost_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("adapter", "the deletePost_btn on hirepost id clicked: " + currentEmployerPostInfo.getHirePostId());

                    final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                String dialogMsg = "Are You Sure You Want To Delete This Post? After Deleting, All Pending Request Will Be Automatically Rejected And History About This Post Cannot Be Tracked Anymore. " +
                                        "You cannot Undo After Clicking Yes.";
                                ((NestedFragmentEmployerMyPost)parentFragment).promptUserDeleteOrNot(dialogMsg, currentEmployerPostInfo.getHirePostId(), Integer.toString(position));


                                // Stuff that updates the UI
                            }

                        }
                    });


                }
            });

            myViewHolder.doneHirePost_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("adapter", "the doneHirePost_imageView on hirepost id clicked: " + currentEmployerPostInfo.getHirePostId());

                    final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                String dialogMsg = "Are You Sure You already Finish Hiring? After Clicking Yes, All Pending Request For This Post Will Be Automatically Rejected. You Can Check History Of This Post Anytime In The History Section.  " +
                                        "You cannot Undo After Clicking Yes.";
                                ((NestedFragmentEmployerMyPost)parentFragment).promptUserDoneHirePostOrNot(dialogMsg, currentEmployerPostInfo.getHirePostId(), Integer.toString(position));


                                // Stuff that updates the UI
                            }

                        }
                    });

                }
            });

        }else {
            //dun show
            RelativeLayout.LayoutParams myLayoutParams = new RelativeLayout.LayoutParams(0,0);
            myViewHolder.bigContainer_relativeLayout.setLayoutParams(myLayoutParams);
            myViewHolder.bigContainer_relativeLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(employerPostInfoArrayList.size() == 0){
            ((NestedFragmentEmployerMyPost)parentFragment).displayNoPostSection(true);
        }else{
            ((NestedFragmentEmployerMyPost)parentFragment).displayNoPostSection(false);
        }
        return employerPostInfoArrayList.size();
    }

    public void setNewArrayList(ArrayList<EmployerPostInfo> employerPostInfoArrayList){
        this.employerPostInfoArrayList = employerPostInfoArrayList;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout bigContainer_relativeLayout;
        TextView jobType_textView, offers_textView, postTitle_textView;
        ImageView doneHirePost_imageView;


        Button editPost_btn, deletePost_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bigContainer_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_employer_mypost_bigContainer);

            jobType_textView = (TextView) itemView.findViewById(R.id.layout_employer_mypost_jobType);
            offers_textView = itemView.findViewById(R.id.layout_employer_mypost_offers);
            postTitle_textView = itemView.findViewById(R.id.layout_employer_mypost_postTitle);

            editPost_btn = itemView.findViewById(R.id.layout_employer_mypost_editPost_btn);
            deletePost_btn = itemView.findViewById(R.id.layout_employer_mypost_deletePost_btn);
            doneHirePost_imageView = (ImageView) itemView.findViewById(R.id.layout_employer_mypost_doneHiringPost_btn);
        }
    }

}

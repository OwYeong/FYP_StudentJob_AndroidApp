package com.oybx.fyp_application.student_section.fragment.student_requestpage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;
import com.oybx.fyp_application.student_section.StudentRequestpage;

import communication_section.ClientCore;

public class FragmentStudentEmployerPostPreviewpage extends Fragment {

    private final String TAG = "Frag-Std-EmpPostPreview";

    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private EmployerPostInfo selectedEmployerPostInfo;


    private TextView employerName_textView, postedDateTime_textView;
    private TextView jobType_textView, offers_textView, postTitle_textView, postDesc_TextView;
    private TextView workingHours_textView, location_textView;
    private ImageView postPic_imageView, proPic_imageView;
    private LinearLayout partTimeAdditionContainer_LinearLayout;
    private RelativeLayout empInfoHeader_relativeLayout;


    Button goBack_btn;



    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_student_requestpage_employer_post_previewpage_goBack_Btn:
                    //edit profile button clicked
                    Log.i(TAG, "Go Back Btn Clicked");
                    ((StudentRequestpage)getActivity()).onBackPressed();

                    break;
                case R.id.fragment_student_requestpage_employer_post_previewpage_headerContainer:
                    requestForCheckProfile(selectedEmployerPostInfo.getEmployerUsername());

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_student_requestpage_employer_post_previewpage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        empInfoHeader_relativeLayout = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_headerContainer);
        employerName_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_headerContainer_name);
        postedDateTime_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_dateTime);
        jobType_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_jobType);
        offers_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_offers);
        postTitle_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_postTitle);
        postDesc_TextView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_description);
        workingHours_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_workingHours);
        location_textView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_Location);

        postPic_imageView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postPic);
        proPic_imageView = layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_headerContainer_profilePic);

        partTimeAdditionContainer_LinearLayout = (LinearLayout) layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_contentContainer_postDetailContainer_partTimeAdditionInfoContainer);

        goBack_btn = (Button) layout.findViewById(R.id.fragment_student_requestpage_employer_post_previewpage_goBack_Btn);
        goBack_btn.setOnClickListener(onClickListener);
        empInfoHeader_relativeLayout.setOnClickListener(onClickListener);
        return layout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            employerName_textView.setText(selectedEmployerPostInfo.getName());
            postedDateTime_textView.setText(selectedEmployerPostInfo.getDateTimePosted());
            jobType_textView.setText(selectedEmployerPostInfo.getJobType() + " Job");

            postTitle_textView.setText(selectedEmployerPostInfo.getPostTitle());
            postDesc_TextView.setText(selectedEmployerPostInfo.getPostDesc());

            proPic_imageView.setImageBitmap(selectedEmployerPostInfo.getProPic());
            postPic_imageView.setImageBitmap(selectedEmployerPostInfo.getPostPic());


            if (selectedEmployerPostInfo.getJobType().equals("Freelance")){
                offers_textView.setText(selectedEmployerPostInfo.getOffers() + " / Job");

                partTimeAdditionContainer_LinearLayout.setVisibility(View.GONE);
            }else if (selectedEmployerPostInfo.getJobType().equals("PartTime")){
                partTimeAdditionContainer_LinearLayout.setVisibility(View.VISIBLE);
                offers_textView.setText(selectedEmployerPostInfo.getOffers() + " / Hrs");
                workingHours_textView.setText(selectedEmployerPostInfo.getWorkingHours());
                location_textView.setText(selectedEmployerPostInfo.getLocation());
            }





        }catch (Exception e){
            e.printStackTrace();
        }







    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    public EmployerPostInfo getSelectedEmployerPostInfo() {
        return selectedEmployerPostInfo;
    }

    public void setSelectedEmployerPostInfo(EmployerPostInfo selectedEmployerPostInfo) {
        this.selectedEmployerPostInfo = selectedEmployerPostInfo;
    }

    public void requestForCheckProfile(String username){
        Log.i(TAG, "Check Profile request for user : " + username);
        ((StudentRequestpage)getActivity()).showLoadingScreen(true);
        ((StudentRequestpage)getActivity()).freezeScreen(true);
        ClientCore.sentStudentCheckProfile(username);
    }

}

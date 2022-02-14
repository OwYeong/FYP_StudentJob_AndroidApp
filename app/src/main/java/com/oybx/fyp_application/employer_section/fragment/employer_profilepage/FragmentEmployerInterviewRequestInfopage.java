package com.oybx.fyp_application.employer_section.fragment.employer_profilepage;

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
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;

public class FragmentEmployerInterviewRequestInfopage extends Fragment {

    private final String TAG="Frag-InterviewReqInfo";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private EmployerMyRequestInfo selectedEmployerMyRequestInfo;

    private ImageView profilePic_imageView;

    private TextView requestDate_textView;
    private TextView interviewRequestStudentName_textView;
    private TextView interviewRequestStudentSkillCat_textview;
    private TextView interviewRequestMessage_textView;
    private TextView interviewRequestDate_textView;
    private TextView interviewRequestTime_textView;
    private TextView interviewLocation_textView;
    private TextView interviewStatus_textView;

    private Button goBack_btn;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_employer_requestpage_interview_request_infopage_goBack_Btn:
                    //edit profile button clicked
                    Log.i(TAG, "Go Back Btn Clicked");
                    ((EmployerRequestpage)getActivity()).onBackPressed();

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_employer_requestpage_interview_request_infopage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        profilePic_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_profilePic);

        requestDate_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_requestedDate);
        interviewRequestStudentName_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_name);
        interviewRequestStudentSkillCat_textview = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_skillCat);
        interviewRequestMessage_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_message);
        interviewRequestDate_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_date);
        interviewRequestTime_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_time);
        interviewLocation_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_location);
        interviewStatus_textView = (TextView) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_status);



        goBack_btn = (Button) layout.findViewById(R.id.fragment_employer_requestpage_interview_request_infopage_goBack_Btn);

        goBack_btn.setOnClickListener(onClickListener);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            profilePic_imageView.setImageBitmap(selectedEmployerMyRequestInfo.getRequesterProPic());
            requestDate_textView.setText("Requested On : " + selectedEmployerMyRequestInfo.getInterviewRequestDateTime());
            interviewRequestStudentName_textView.setText(selectedEmployerMyRequestInfo.getRequesterName());
            interviewRequestStudentSkillCat_textview.setText(selectedEmployerMyRequestInfo.getRequesterSkillCat());
            interviewRequestMessage_textView.setText(selectedEmployerMyRequestInfo.getInterviewRequestMessage());
            interviewRequestDate_textView.setText(selectedEmployerMyRequestInfo.getInterviewDate());
            interviewRequestTime_textView.setText(selectedEmployerMyRequestInfo.getInterviewTime());
            interviewLocation_textView.setText(selectedEmployerMyRequestInfo.getInterviewLocation());

            if(selectedEmployerMyRequestInfo.getInterviewRequestStatus().equals("pending")) {
                interviewStatus_textView.setText("Pending For Student Reply! ");
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

    public EmployerMyRequestInfo getSelectedEmployerMyRequestInfo() {
        return selectedEmployerMyRequestInfo;
    }

    public void setSelectedEmployerMyRequestInfo(EmployerMyRequestInfo selectedEmployerMyRequestInfo) {
        this.selectedEmployerMyRequestInfo = selectedEmployerMyRequestInfo;
    }
}

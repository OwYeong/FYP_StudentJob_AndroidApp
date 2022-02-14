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
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentRequestpage;

public class FragmentStudentPostRequestPreviewpage extends Fragment {

    private final String TAG="Frag-std-postReqPreview";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private StudentMyRequestInfo selectedStudentMyRequestInfo;

    private ImageView profilePic_imageView;

    private TextView requestDate_textView;
    private TextView postRequestStudentName_textView;
    private TextView postRequestStudentSkillCat_textview;
    private TextView postRequestMessage_textView;


    private Button goBack_btn;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_student_requestpage_post_request_previewpage_goBack_Btn:
                    //edit profile button clicked
                    Log.i(TAG, "Go Back Btn Clicked");
                    ((StudentRequestpage)getActivity()).onBackPressed();

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_student_requestpage_post_request_previewpage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        profilePic_imageView = (ImageView) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_profilePic);

        requestDate_textView = (TextView) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_requestedDate);
        postRequestStudentName_textView = (TextView) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_name);
        postRequestStudentSkillCat_textview = (TextView) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_skillCat);
        postRequestMessage_textView = (TextView) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_message);



        goBack_btn = (Button) layout.findViewById(R.id.fragment_student_requestpage_post_request_previewpage_goBack_Btn);

        goBack_btn.setOnClickListener(onClickListener);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            profilePic_imageView.setImageBitmap(ApplicationManager.getMyStudentAccountInfo().getProfilePic());
            requestDate_textView.setText("Requested On : " + selectedStudentMyRequestInfo.getPostRequestDateTime());
            postRequestStudentName_textView.setText(selectedStudentMyRequestInfo.getRequesterName());
            postRequestStudentSkillCat_textview.setText(ApplicationManager.getMyStudentAccountInfo().getGeneralSkillCategory());
            postRequestMessage_textView.setText(selectedStudentMyRequestInfo.getPostRequestMessage());








        }catch (Exception e){
            e.printStackTrace();
        }







    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    public StudentMyRequestInfo getSelectedStudentMyRequestInfo() {
        return selectedStudentMyRequestInfo;
    }

    public void setSelectedStudentMyRequestInfo(StudentMyRequestInfo selectedEmployerMyRequestInfo) {
        this.selectedStudentMyRequestInfo = selectedEmployerMyRequestInfo;
    }
}

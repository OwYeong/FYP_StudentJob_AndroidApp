package com.oybx.fyp_application.employer_section.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;

public class FragmentEmployerCheckPortfoliopage extends Fragment {
    private final String TAG="Frag-Std-CheckPortfolio";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private StudentPortfolioInfo selectedStudentPortfolioInfo;
    private StudentAccountInfo targetedAccountInfo;

    private Button portfolioNavigateBtn_button;

    private TextView studentName_textView;
    private TextView studentGeneralSkillCategory_textView;
    private TextView studentPortfolioDesc_textView;

    private ImageView studentPortfolioPic_imageView;



    public void setSelectedStudentPortfolioInfo(StudentPortfolioInfo selectedStudentPortfolioInfo){
        this.selectedStudentPortfolioInfo = selectedStudentPortfolioInfo;

    }

    public void setTargetedAccountInfo(StudentAccountInfo targetedAccountInfo) {
        this.targetedAccountInfo = targetedAccountInfo;
    }



    //create a onclicklistener variable to handle all button on click function
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the button Id that has been click
            switch (v.getId()){
                case R.id.fragment_employer_check_portfoliopage_contentContainer_urlNavigate_btn:
                    String portfolioUrl = selectedStudentPortfolioInfo.getPortfolioUrl();
                    Intent intentToPortfolioUrl = new Intent(Intent.ACTION_VIEW);
                    intentToPortfolioUrl.setData(Uri.parse(portfolioUrl));
                    startActivity(intentToPortfolioUrl);

                    break;


            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView Called");

        View layout = inflater.inflate(R.layout.fragment_employer_check_portfoliopage, container, false);

        portfolioNavigateBtn_button = (Button) layout.findViewById(R.id.fragment_employer_check_portfoliopage_contentContainer_urlNavigate_btn);

        studentName_textView = (TextView) layout.findViewById(R.id.fragment_employer_check_portfoliopage_headerContainer_name);
        studentGeneralSkillCategory_textView = (TextView) layout.findViewById(R.id.fragment_employer_check_portfoliopage_headerContainer_generalSkillCategory);
        studentPortfolioDesc_textView = (TextView) layout.findViewById(R.id.fragment_employer_check_portfoliopage_contentContainer_portfolioDesc);

        studentPortfolioPic_imageView =(ImageView) layout.findViewById(R.id.fragment_employer_check_portfoliopage_contentContainer_portfolioPic);


        return layout;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated Called");


        portfolioNavigateBtn_button.setOnClickListener(onClickListener);

        studentName_textView.setText(targetedAccountInfo.getFullName());
        studentGeneralSkillCategory_textView.setText(targetedAccountInfo.getGeneralSkillCategory());

        studentPortfolioPic_imageView.setImageBitmap(selectedStudentPortfolioInfo.getPortfolioPicture());
        studentPortfolioDesc_textView.setText(selectedStudentPortfolioInfo.getPortfolioDescription());




    }
}

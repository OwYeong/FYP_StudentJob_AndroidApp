package com.oybx.fyp_application.student_section.fragment.student_profilepage;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;

import communication_section.ClientCore;

public class FragmentStudentDetailedPortfolioPage extends Fragment {
    private final String TAG="Fragment-D.PortfolioPG";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private StudentPortfolioInfo selectedStudentPortfolioInfo;
    private int positionInArrayList;

    private Button portfolioNavigateBtn_button;
    private ImageButton portfolioMoreOptionBtn_imageButton;

    private TextView studentName_textView;
    private TextView studentGeneralSkillCategory_textView;
    private TextView studentPortfolioDesc_textView;

    private ImageView studentPortfolioPic_imageView;



    public void setSelectedStudentPortfolioInfo(StudentPortfolioInfo selectedStudentPortfolioInfo){
        this.selectedStudentPortfolioInfo = selectedStudentPortfolioInfo;

    }

    public void setPositionInArrayList(int positionInArrayList) {
        this.positionInArrayList = positionInArrayList;
    }





    //create a onclicklistener variable to handle all button on click function
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the button Id that has been click
            switch (v.getId()){
                case R.id.fragment_student_profilepage_detailedportfoliopage_contentContainer_urlNavigate_btn:
                    String portfolioUrl = selectedStudentPortfolioInfo.getPortfolioUrl();
                    Intent intentToPortfolioUrl = new Intent(Intent.ACTION_VIEW);
                    intentToPortfolioUrl.setData(Uri.parse(portfolioUrl));
                    startActivity(intentToPortfolioUrl);

                    break;
                case R.id.fragment_student_profilepage_detailedportfoliopage_headerContainer_more_btn:

                    PopupMenu myOptionMenu = new PopupMenu(mActivity, portfolioMoreOptionBtn_imageButton);
                    myOptionMenu.getMenuInflater().inflate(R.menu.detailed_portfolio_option_menu, myOptionMenu.getMenu());

                    myOptionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.edit_portfolio:

                                    //change to the edit portfolio page
                                    FragmentStudentEditPortfoliopage newFragment = new FragmentStudentEditPortfoliopage();
                                    newFragment.setSelectedStudentPortfolioInfo(selectedStudentPortfolioInfo);//pass the selected portfolio info for the fragment
                                    newFragment.setPositionInArrayList(positionInArrayList);

                                    ((StudentProfilepage)getActivity()).changeFragmentPage(newFragment, null,null);//change the fragment


                                    break;
                                case R.id.delete_portfolio:
                                    requestForDeleteAPortfolio(selectedStudentPortfolioInfo.getPortfolioId(), positionInArrayList);
                                    ((StudentProfilepage) getActivity()).showLoadingScreen(true);
                                    ((StudentProfilepage)getActivity()).freezeScreen(true);
                                    break;
                                default:
                                    Log.i(TAG, "detailed_portfolio_option_menu --> Cant Find Button Clicked");
                            }
                            return true;
                        }



                    });
                    myOptionMenu.show();//show the menu when clicked

                    break;




            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView Called");

        View layout = inflater.inflate(R.layout.fragment_student_profilepage_detailedportfoliopage, container, false);

        portfolioNavigateBtn_button = (Button) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_contentContainer_urlNavigate_btn);
        portfolioMoreOptionBtn_imageButton = (ImageButton) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_headerContainer_more_btn);

        studentName_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_headerContainer_name);
        studentGeneralSkillCategory_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_headerContainer_generalSkillCategory);
        studentPortfolioDesc_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_contentContainer_portfolioDesc);

        studentPortfolioPic_imageView =(ImageView) layout.findViewById(R.id.fragment_student_profilepage_detailedportfoliopage_contentContainer_portfolioPic);


        return layout;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated Called");


        portfolioMoreOptionBtn_imageButton.setOnClickListener(onClickListener);
        portfolioNavigateBtn_button.setOnClickListener(onClickListener);

        studentName_textView.setText(ApplicationManager.getMyStudentAccountInfo().getFullName());
        studentGeneralSkillCategory_textView.setText(ApplicationManager.getMyStudentAccountInfo().getGeneralSkillCategory());

        studentPortfolioPic_imageView.setImageBitmap(selectedStudentPortfolioInfo.getPortfolioPicture());
        studentPortfolioDesc_textView.setText(selectedStudentPortfolioInfo.getPortfolioDescription());




    }

    private void requestForDeleteAPortfolio(String portfolioId, int positionInArrayList) {
        Log.i(TAG, "request For delete a portfolio Called");

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully delete, use this position to remove the record from current StudentPortfolioInfo Arraylist

        ClientCore.sentDeletePortfolioRequest(portfolioId, positionInArrayList_string);


    }




}

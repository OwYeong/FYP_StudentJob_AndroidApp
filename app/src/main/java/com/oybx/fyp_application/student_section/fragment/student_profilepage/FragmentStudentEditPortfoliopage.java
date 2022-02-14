package com.oybx.fyp_application.student_section.fragment.student_profilepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;

import java.io.IOException;

import communication_section.ClientCore;

public class FragmentStudentEditPortfoliopage extends Fragment {
    private final String TAG = "Frag-Std-EditPortfolio";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();
    private final int PICK_FROM_GALLERY_REQUEST = 1;

    private ScrollView myScrollview;

    private ImageView portfolioImage_imageView;
    private Spinner projectType_spinner;
    private EditText projectDesc_editText;
    private EditText projectUrl_editText;
    private static Button addAPortfolio_button;

    private boolean portfolioImageLock = true; // use to prevent spinner to change the portfolio Image on the first time

    private Bitmap selectedPortfolioImage = null;
    private String projectType = null;
    private String projectDesc = null;
    private String projectUrl = null;


    private TextView projectType_alert_textView;
    private TextView projectDesc_alert_textView;
    private TextView projectUrl_alert_textView;

    private StudentPortfolioInfo selectedStudentPortfolioInfo;
    private int positionInArrayList;

    public void setSelectedStudentPortfolioInfo(StudentPortfolioInfo selectedStudentPortfolioInfo) {
        this.selectedStudentPortfolioInfo = selectedStudentPortfolioInfo;
    }

    public void setPositionInArrayList(int positionInArrayList) {
        this.positionInArrayList = positionInArrayList;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_student_profilepage_editPortfoliopage_portfolioImage:

                    Intent pickPhotoFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoFromGalleryIntent.setType("image/*");
                    startActivityForResult(pickPhotoFromGalleryIntent, PICK_FROM_GALLERY_REQUEST);

                    break;
                case R.id.fragment_student_profilepage_editPortfoliopage_editPortfolio_btn:
                    projectDesc_alert_textView.setVisibility(View.GONE);
                    projectType_alert_textView.setVisibility(View.GONE);
                    projectUrl_alert_textView.setVisibility(View.GONE);

                    projectType = projectType_spinner.getSelectedItem().toString();
                    projectDesc = projectDesc_editText.getText().toString();
                    projectUrl = projectUrl_editText.getText().toString();
                    if(!projectType.equals("Please Select a Project Type")){
                        //if it not (please select ...) mean user selected a type


                        if(selectedPortfolioImage != null){
                            if(projectDesc.length() != 0){
                                //mean project Desc is not empty

                                //this pattern simply means MUST startwith http://(anything)
                                //symbol must be scape by double \\
                                String regex = "^http:\\/\\/[a-zA-Z0-9-?\\W]+$";

                                if(projectUrl.length() != 0){
                                    if(projectUrl.matches(regex)){
                                        Log.i(TAG, "matches regex");
                                        Log.i(TAG, "Portfolio Type is: " +projectType);
                                        Log.i(TAG, "Portfolio Desc is: " +projectDesc);
                                        Log.i(TAG, "Portfolio Url is: " + projectUrl);

                                        //force the image to be compress into 400px x 400px photo
                                        //reduce the workload of server
                                        Bitmap scaledBitmap = scaleBitmapWithAspectRatio(selectedPortfolioImage, 400,400);
                                        String portfolioId = selectedStudentPortfolioInfo.getPortfolioId();
                                        requestForEditPortfolio(portfolioId, scaledBitmap, projectType, projectDesc, projectUrl, positionInArrayList);
                                        ((StudentProfilepage)getActivity()).showLoadingScreen(true);
                                        ((StudentProfilepage)getActivity()).freezeScreen(true);
                                    }else{
                                        projectUrl_alert_textView.setText("Url Must start with -> http://");
                                        projectUrl_alert_textView.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    projectUrl_alert_textView.setText("Please enter an Url");
                                    projectUrl_alert_textView.setVisibility(View.VISIBLE);
                                }

                            }else {
                                projectDesc_alert_textView.setText("Please Write Some Description");
                                projectDesc_alert_textView.setVisibility(View.VISIBLE);

                            }


                        }



                    }else {
                        projectType_alert_textView.setText("Please Select One Project Type");
                        projectType_alert_textView.setVisibility(View.VISIBLE);


                    }

                    break;
            }
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            //when any view is on touch
            final Handler handler;
            handler = new Handler();

            final Runnable r = new Runnable() {
                public void run() {
                    scrollToView(v);
                }
            };

            handler.postDelayed(r, 200);//delay 0.2s(Because need wait after keyboard appear)


            switch (v.getId()){
                case R.id.fragment_student_profilepage_editPortfoliopage_description:


                    break;
                case R.id.fragment_student_profilepage_editPortfoliopage_projectUrl:


                    break;

            }
            return false;
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "on create View called");

        View layout = inflater.inflate(R.layout.fragment_student_profilepage_edit_portfoliopage, container, false);

        projectType_spinner = (Spinner) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_projectTypeSpinner);
        portfolioImage_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_portfolioImage);

        portfolioImage_imageView.setOnClickListener(onClickListener);
        addAPortfolio_button = (Button) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_editPortfolio_btn);
        addAPortfolio_button.setOnClickListener(onClickListener);

        projectType_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_projectType_alert);
        projectDesc_alert_textView =(TextView) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_projectDesc_alert);
        projectUrl_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_projectUrl_alert);
        projectDesc_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_description);
        projectUrl_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_projectUrl);

        myScrollview = (ScrollView) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_scrollView);

        return layout;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((StudentProfilepage)getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        projectUrl_alert_textView.setVisibility(View.GONE);
        projectDesc_alert_textView.setVisibility(View.GONE);
        projectType_alert_textView.setVisibility(View.GONE);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.project_type_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        projectType_spinner.setAdapter(adapter);

        projectType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //get the selected item


                if(!portfolioImageLock){
                    //portfolioImageLock is false
                    switch (selectedItem){
                        case "GitHub":
                            Log.i(TAG, "selected item github called");
                            selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_portfolio_github);
                            portfolioImage_imageView.setImageBitmap(selectedPortfolioImage);

                            break;
                        case "Behance":
                            Log.i(TAG, "selected item behance called");
                            selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_portfolio_behance);
                            portfolioImage_imageView.setImageBitmap(selectedPortfolioImage);

                            break;
                        case "Others":
                            Log.i(TAG, "selected item others called");
                            selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_portfolio_others);
                            portfolioImage_imageView.setImageBitmap(selectedPortfolioImage);

                            break;
                    }
                }else {
                    //portfolioImagelock is true

                    portfolioImageLock=false;
                    //set it false

                    //this prevent Spinner to set default projecttype image for the first time
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        projectDesc_editText.setOnTouchListener(onTouchListener);
        projectUrl_editText.setOnTouchListener(onTouchListener);

        //SECTION: Place all portfolio info into Edittext, spinner...
        //get the value of portfolioType and find spinner position -> then replace the spinner
        int portfolioTypeSpinnerPosition = adapter.getPosition(selectedStudentPortfolioInfo.getPortfolioType());
        projectType_spinner.setSelection(portfolioTypeSpinnerPosition);

        //get the value of description and replace into edittext
        projectDesc_editText.setText(selectedStudentPortfolioInfo.getPortfolioDescription());

        //get the value of Url and replace into edittext
        projectUrl_editText.setText(selectedStudentPortfolioInfo.getPortfolioUrl());

        //replace image
        selectedPortfolioImage = selectedStudentPortfolioInfo.getPortfolioPicture();
        portfolioImage_imageView.setImageBitmap(selectedPortfolioImage);

















    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case PICK_FROM_GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        selectedPortfolioImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        portfolioImage_imageView.setImageBitmap(selectedPortfolioImage);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    private void scrollToView(final View v){
        myScrollview.smoothScrollTo(0, v.getBottom());
        myScrollview.post(new Runnable() {
                              @Override
                              public void run() {
                                  v.requestFocus();
                              }
                          }
        );
    }

    private void requestForEditPortfolio(String portfolioId, Bitmap selectedPortfolioImage, String portfolioType, String portfolioDesc, String portfolioUrl, int positionInArrayList) {
        Log.i(TAG, "request For add a portfolio Called");

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully edit, use this position to replace the record from current StudentPortfolioInfo Arraylist

        ClientCore.sentEditPortfolioRequest(portfolioId, selectedPortfolioImage, portfolioType, portfolioDesc, portfolioUrl, positionInArrayList_string);


    }

    private Bitmap scaleBitmapWithAspectRatio(Bitmap mBitmap, int maxWidth, int maxHeight) {
        //if max height and maxwidth is empty(means 0)
        if (maxHeight > 0 && maxWidth > 0) {

            //Section:get image(bitmap) width and height
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();


            //calculate the ratio of bitmap
            float ratioBitmap = (float) width / (float) height;// if width is 100px and heiht is 50px
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            mBitmap = Bitmap.createScaledBitmap(mBitmap, finalWidth, finalHeight, true);
            return mBitmap;
        } else {
            return mBitmap;
        }
    }



}

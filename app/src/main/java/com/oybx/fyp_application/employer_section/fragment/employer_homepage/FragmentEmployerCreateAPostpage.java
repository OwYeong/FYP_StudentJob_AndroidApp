package com.oybx.fyp_application.employer_section.fragment.employer_homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerHomepage;

import java.io.IOException;

import communication_section.ClientCore;

public class FragmentEmployerCreateAPostpage extends Fragment {
    private final String TAG = "Fragment-empCreateAPost";
    private Context mContext = ApplicationManager.getCurrentAppContext();
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();

    private final int PICK_FROM_GALLERY_REQUEST = 1;

    private RelativeLayout partTimeAdditionInfoSection_relativeLayout;

    private Bitmap selectedPortfolioImage = null;
    private ImageView postImage_imageView;

    private EditText postTitle_editText;
    private EditText postDesc_editText;

    private Spinner jobSkillCat_spinner;
    private EditText postOffers_editText;

    private EditText postLocation_editText;
    private EditText postWorkingHours_editText;

    private TextView offersWord_textView;
    private Spinner jobType_spinner;

    private TextView postTitle_alert_textView;
    private TextView postDesc_alert_textView;
    private TextView postOffers_alert_textView;
    private TextView postLocation_alert_textView;
    private TextView postWorkingHours_alert_textView;


    private Button createAPost_btn;
    private Button cancel_btn;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_employer_homepage_createAPostpage_postImage_imageView:

                    Intent pickPhotoFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoFromGalleryIntent.setType("image/*");
                    startActivityForResult(pickPhotoFromGalleryIntent, PICK_FROM_GALLERY_REQUEST);

                    break;
                case R.id.fragment_employer_homepage_createAPostpage_createPost_btn:

                    postTitle_alert_textView.setVisibility(View.GONE);
                    postDesc_alert_textView.setVisibility(View.GONE);
                    postOffers_alert_textView.setVisibility(View.GONE);
                    postLocation_alert_textView.setVisibility(View.GONE);
                    postWorkingHours_alert_textView.setVisibility(View.GONE);

                    //extract data from editText
                    Bitmap scaledBitmap = scaleBitmapWithAspectRatio(selectedPortfolioImage, 400,400);
                    String postTitle = postTitle_editText.getText().toString();
                    String postDesc = postDesc_editText.getText().toString();
                    String postOffers = postOffers_editText.getText().toString();
                    String postLocation = postLocation_editText.getText().toString();
                    String postWorkingHours = postWorkingHours_editText.getText().toString();

                    String jobSkillCat = jobSkillCat_spinner.getSelectedItem().toString();
                    String jobType = jobType_spinner.getSelectedItem().toString();

                    String offersRegex = "^[0-9]+$";//this regex ensure only number is entered
                    String workHoursRegex = "^[1]?[0-9](am|pm|Am|Pm)-[1]?[0-9](am|pm|Am|Pm)$";//this regex ensure only time format is entered
                    if(postTitle.length() != 0 && postTitle.length() <= 250){
                        //if title is not empty and less than 250 char

                        if(postDesc.length() != 0){
                            //if description is not empty
                            if(postOffers.length() != 0 && postOffers.matches(offersRegex) && postOffers.length() <= 10){

                                if(jobType.equals("PartTime")){
                                    //if parttime check the location and working hours
                                    if(postLocation.length() != 0){
                                        if(postWorkingHours.length() != 0 && postWorkingHours.matches(workHoursRegex)){

                                            //mean everyThis is done
                                            requestForCreateAPost(postTitle,postDesc,jobType,postOffers,jobSkillCat,postLocation,postWorkingHours,scaledBitmap);
                                            ((EmployerHomepage) getActivity()).showLoadingScreen(true);
                                            ((EmployerHomepage) getActivity()).freezeScreen(true);

                                        }else {
                                            postWorkingHours_alert_textView.setText("Please Ensure Working Hours is entered in following format(XXam-XXpm) and not empty");
                                            postWorkingHours_alert_textView.setVisibility(View.VISIBLE);
                                        }

                                    }else {
                                        postLocation_alert_textView.setText("Please ensure Job Location is entered! ");
                                        postLocation_alert_textView.setVisibility(View.VISIBLE);
                                    }

                                }else{
                                    //this is a Freelance JOb no working location and working hours is entered
                                    postLocation = null;
                                    postWorkingHours = null;
                                    requestForCreateAPost(postTitle,postDesc,jobType,postOffers,jobSkillCat,postLocation,postWorkingHours,scaledBitmap);
                                    ((EmployerHomepage) getActivity()).showLoadingScreen(true);
                                    ((EmployerHomepage) getActivity()).freezeScreen(true);
                                }
                            }else {
                                postOffers_alert_textView.setText("Please ensure only number entered and not empty! Maximum 10 characters");
                                postOffers_alert_textView.setVisibility(View.VISIBLE);
                            }

                        }else {
                            postDesc_alert_textView.setText("Please ensure the description is not empty");
                            postDesc_alert_textView.setVisibility(View.VISIBLE);
                        }


                    }else {
                        postTitle_alert_textView.setText("Please ensure the title is not empty. Maximum 250 character");
                        postTitle_alert_textView.setVisibility(View.VISIBLE);
                    }





                    break;
                case R.id.fragment_employer_homepage_createAPostpage_cancel_btn:
                    ((EmployerHomepage) getActivity()).popBackStack();
                    ((EmployerHomepage) getActivity()).hideKeyboard();

                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employer_homepage_createapostpage, container, false);
        Log.i(TAG, "onCreateView called");


        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        partTimeAdditionInfoSection_relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_partTime_additionInfo_section);

        postImage_imageView = (ImageView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postImage_imageView);

        postImage_imageView.setOnClickListener(onClickListener);
        postTitle_editText = (EditText) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postTitle);
        postDesc_editText = (EditText) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_description);

        jobSkillCat_spinner = (Spinner) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_jobSkillCategory_spinner);
        postOffers_editText = (EditText) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_offers);

        postLocation_editText = (EditText) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postWorkLocation);
        postWorkingHours_editText = (EditText) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postWorkHours);
        offersWord_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_offersWord_textview);
        jobType_spinner = (Spinner) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_jobType_spinner);


        postTitle_alert_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postTitle_alert);
        postDesc_alert_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_description_alert);
        postOffers_alert_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_offers_alert);
        postLocation_alert_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postWorkLocation_alert);
        postWorkingHours_alert_textView = (TextView) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_postWorkHours_alert);

        postTitle_alert_textView.setVisibility(View.GONE);
        postDesc_alert_textView.setVisibility(View.GONE);
        postOffers_alert_textView.setVisibility(View.GONE);
        postLocation_alert_textView.setVisibility(View.GONE);
        postWorkingHours_alert_textView.setVisibility(View.GONE);


        createAPost_btn = (Button) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_createPost_btn);
        cancel_btn = (Button) view.findViewById(R.id.fragment_employer_homepage_createAPostpage_cancel_btn);

        createAPost_btn.setOnClickListener(onClickListener);
        cancel_btn.setOnClickListener(onClickListener);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupJobSkillCatSpinner();
        setupJobTypeSpinner();

    }

    private void setupJobSkillCatSpinner(){
        //array adapter for spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.post_job_skill_category_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        jobSkillCat_spinner.setAdapter(adapter);

        jobSkillCat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //get the selected item

                switch (selectedItem){
                    case "Designer":
                        selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_post_designer_pic);
                        postImage_imageView.setImageBitmap(selectedPortfolioImage);
                        break;
                    case "Programmer":
                        selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_post_programmer_pic);
                        postImage_imageView.setImageBitmap(selectedPortfolioImage);

                        break;
                    case "Others":
                        selectedPortfolioImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_post_others_pic);
                        postImage_imageView.setImageBitmap(selectedPortfolioImage);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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
                        postImage_imageView.setImageBitmap(selectedPortfolioImage);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }


    private void setupJobTypeSpinner(){
        //array adapter for spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.post_job_type_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        jobType_spinner.setAdapter(adapter);

        jobType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //get the selected item

                switch (selectedItem){
                    case "PartTime":
                        offersWord_textView.setText("Offers(RM per Hours)");
                        partTimeAdditionInfoSection_relativeLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Freelance":
                        offersWord_textView.setText("Offers(RM per Job)");
                        partTimeAdditionInfoSection_relativeLayout.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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

    private void requestForCreateAPost(String postTitle, String postDesc, String jobType, String offers, String post_skillCategory, String location, String workHours, Bitmap scaledBitmap){

        offers = "RM " + offers;//add Rm for the offers because user will only input numbers

        ClientCore.sentCreateAPostRequest(postTitle, postDesc, jobType, offers, post_skillCategory, location, workHours, scaledBitmap );


    }




}

package com.oybx.fyp_application.employer_section.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerProfilepage;
import com.oybx.fyp_application.employer_section.fragment.employer_profilepage.FragmentEmployerEditProfilepage;
import com.oybx.fyp_application.infomation_classes.EmployerAccountInfo;

import java.util.ArrayList;

public class FragmentEmployerProfilepage extends Fragment {

    private static final String TAG="Fragment-ProfilePage";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private static EmployerAccountInfo myEmployerAccountInfo = ApplicationManager.getMyEmployerAccountInfo();

    private ImageView profilePic_imageView;
    private ImageView fbUrl_imageView;
    private ImageView twitterUrl_imageView;
    private ImageView whatsappUrl_imageView;

    private TextView name_textView;
    private TextView generalSkillCategory_textView;
    private TextView aboutMe_textview;
    private TextView phoneNum_textView;
    private TextView email_textView;

    private Button editProfile_btn;




    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_employer_profilepage_facebookUrl_btn:

                    /*NOTE:
                        using intent for redirecting user to a browser
                        must provide a Url
                        With http:// or https://
                     */
                    String httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";


                    String fbUrl = ApplicationManager.getMyEmployerAccountInfo().getFacebookUrl();

                    if(!fbUrl.matches(httpRegex)){
                        fbUrl = "http://" + fbUrl;
                    }

                    Log.i(TAG, "fb url" + fbUrl);
                    Intent intentToFb = new Intent(Intent.ACTION_VIEW);
                    intentToFb.setData(Uri.parse(fbUrl));
                    startActivity(intentToFb);


                    break;
                case R.id.fragment_employer_profilepage_twitterUrl_btn:
                    String twitterUrl = ApplicationManager.getMyEmployerAccountInfo().getTwitterUrl();

                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!twitterUrl.matches(httpRegex)){
                        twitterUrl = "http://" + twitterUrl;
                    }

                    Intent intentToTwitter = new Intent(Intent.ACTION_VIEW);
                    intentToTwitter.setData(Uri.parse(twitterUrl));
                    startActivity(intentToTwitter);
                    break;
                case R.id.fragment_employer_profilepage_whatsappUrl_btn:
                    String whatsappUrl = ApplicationManager.getMyEmployerAccountInfo().getWhatsappUrl();

                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!whatsappUrl.matches(httpRegex)){
                        whatsappUrl = "http://" + whatsappUrl;
                    }

                    Intent intentToWhatsapp = new Intent(Intent.ACTION_VIEW);
                    intentToWhatsapp.setData(Uri.parse(whatsappUrl));
                    startActivity(intentToWhatsapp);
                    break;

                case R.id.fragment_employer_profilepage_editProfileBtn:
                    //edit profile button clicked
                    Log.i(TAG, "Edit Profile Btn Clicked");
                    ((EmployerProfilepage)getActivity()).changeFragmentPage(new FragmentEmployerEditProfilepage(),null,null);

                    break;
            }
        }
    };







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_employer_profilepage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        profilePic_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_profilepage_profilePicContainer_profilePic);
        fbUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_profilepage_facebookUrl_btn);
        twitterUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_profilepage_twitterUrl_btn);
        whatsappUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_profilepage_whatsappUrl_btn);

        name_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_name);
        generalSkillCategory_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_generalSkillCategory);
        aboutMe_textview = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_aboutMe);
        phoneNum_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_phoneNum);
        email_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_email);

        editProfile_btn = (Button) layout.findViewById(R.id.fragment_employer_profilepage_editProfileBtn);

        editProfile_btn.setOnClickListener(onClickListener);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupInfo();








    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }



    private int dpiToPx(int dpi){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int dpiInPx = (int) (dpi * scale + 0.5f);// convert dpi to px

        return dpiInPx;
    }

    private int pxToDpi(int px){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int pxInDpi = (int) (px / scale + 0.5f);// convert px to dpi

        return pxInDpi;
    }

    private Bitmap convertImageStringToBitmap(String myImageString){
        /* Function to convert imageString into Bitmap*/


        //Decode the imageString(Base 64 Encoded String) into Image Bytes
        //Note: Base64.NO_PADDING is very important as the ImageString has no padding -> ex: data:image/png;base64
        byte[] myImageByteArray = Base64.decode(myImageString.getBytes(), Base64.NO_PADDING);

        //Convert ImageByte to Bitmap
        Bitmap myBitmap= BitmapFactory.decodeByteArray(myImageByteArray, 0, myImageByteArray.length);

        return myBitmap;
    }

    public void setupInfo(){
        try {
            myEmployerAccountInfo = ApplicationManager.getMyEmployerAccountInfo();
            if(myEmployerAccountInfo == null) {
                Log.i(TAG, "SHYT null");
            }

            if(myEmployerAccountInfo != null) {
                profilePic_imageView.setImageBitmap(myEmployerAccountInfo.getProfilePic());
                name_textView.setText(myEmployerAccountInfo.getFullName());
                generalSkillCategory_textView.setText(myEmployerAccountInfo.getGeneralSkillCategory());
                aboutMe_textview.setText(myEmployerAccountInfo.getAboutMe());
                phoneNum_textView.setText(myEmployerAccountInfo.getContactNum());
                email_textView.setText(myEmployerAccountInfo.getEmail());

                if (myEmployerAccountInfo.getFacebookUrl() != null || myEmployerAccountInfo.getWhatsappUrl() != null || myEmployerAccountInfo.getTwitterUrl() != null) {
                    //if anyone of (facebookurl,whatsappurl,twitterurl) is contain value

                    //if ContainValue Then show icon
                    if (myEmployerAccountInfo.getFacebookUrl() != null) {
                        fbUrl_imageView.setVisibility(View.VISIBLE);
                        fbUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "facebook is null");
                        fbUrl_imageView.setVisibility(View.GONE);
                    }

                    if (myEmployerAccountInfo.getWhatsappUrl() != null) {
                        whatsappUrl_imageView.setVisibility(View.VISIBLE);
                        whatsappUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "whatsapp is null");
                        whatsappUrl_imageView.setVisibility(View.GONE);
                    }


                    if (myEmployerAccountInfo.getTwitterUrl() != null) {
                        twitterUrl_imageView.setVisibility(View.VISIBLE);
                        twitterUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        twitterUrl_imageView.setVisibility(View.GONE);
                    }


                } else {
                    //if all (facebookurl,whatsappurl,twitterurl) is empty
                    Log.i(TAG, "all empty");
                    //made all icon gone
                    fbUrl_imageView.setVisibility(View.GONE);
                    whatsappUrl_imageView.setVisibility(View.GONE);
                    twitterUrl_imageView.setVisibility(View.GONE);
                }
                ((EmployerProfilepage)getActivity()).showLoadingScreen(false);
                editProfile_btn.setEnabled(true);

            }else{
                ((EmployerProfilepage)getActivity()).showLoadingScreen(true);
                editProfile_btn.setEnabled(false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}

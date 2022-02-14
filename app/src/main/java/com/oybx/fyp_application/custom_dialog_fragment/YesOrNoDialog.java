package com.oybx.fyp_application.custom_dialog_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.fragment.employer_requestpage.FragmentEmployerSpecificPostRequest;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentMyOffer;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentMyRequest;

public class YesOrNoDialog extends DialogFragment implements View.OnClickListener{

    private TextView dialogTitle_textView;
    private Button yesButton;
    private Button noButton;

    public YesOrNoDialog()
    {
        // Empty constructor required for DialogFragment
    }

    public static YesOrNoDialog newInstance(String title, String relatedInfo1, String relatedInfo2) {
        YesOrNoDialog newDialog = new YesOrNoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("relatedInfo1", relatedInfo1);
        args.putString("relatedInfo2", relatedInfo2);
        newDialog.setArguments(args);
        return newDialog;
    }

    public static YesOrNoDialog newInstance(String title, String relatedInfo1, String relatedInfo2, String relatedInfo3) {
        YesOrNoDialog newDialog = new YesOrNoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("relatedInfo1", relatedInfo1);
        args.putString("relatedInfo2", relatedInfo2);
        args.putString("relatedInfo3", relatedInfo3);
        newDialog.setArguments(args);
        return newDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_dialog_fragment_yes_no, container);
        dialogTitle_textView = (TextView) view.findViewById(R.id.layout_dialog_fragment_yes_no_title);
        noButton = (Button) view.findViewById(R.id.layout_dialog_fragment_yes_no_noBtn);
        yesButton = (Button) view.findViewById(R.id.layout_dialog_fragment_yes_no_yesBtn);
        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);

        dialogTitle_textView.setText(getArguments().getString("title"));


        return view;
    }

    @Override
    public void onClick(View v)
    {

        if (v == yesButton)
        {

            if(getTargetFragment() instanceof NestedFragmentEmployerMyPost) {
                Intent i = new Intent()
                        .putExtra("postId", getArguments().getString("relatedInfo1"))
                        .putExtra("positionInArrayList", getArguments().getString("relatedInfo2"));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

            }else if(getTargetFragment() instanceof FragmentEmployerSpecificPostRequest){
                Intent i = new Intent()
                        .putExtra("postId", getArguments().getString("relatedInfo1"))
                        .putExtra("requesterUsername", getArguments().getString("relatedInfo2"))
                        .putExtra("positionInArrayList", getArguments().getString("relatedInfo3"));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

            }else if(getTargetFragment() instanceof NestedFragmentStudentMyOffer){
                Intent i = new Intent()
                        .putExtra("postId", getArguments().getString("relatedInfo1"))
                        .putExtra("requesterUsername", getArguments().getString("relatedInfo2"))
                        .putExtra("positionInArrayList", getArguments().getString("relatedInfo3"));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

            }else if(getTargetFragment() instanceof NestedFragmentStudentMyRequest){
                Intent i = new Intent()
                        .putExtra("postId", getArguments().getString("relatedInfo1"))
                        .putExtra("requesterUsername", getArguments().getString("relatedInfo2"))
                        .putExtra("positionInArrayList", getArguments().getString("relatedInfo3"));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

            }



            this.dismiss();
        }
        else if (v == noButton)
        {


            this.dismiss();
        }

    }
}

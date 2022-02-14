package com.oybx.fyp_application.employer_section.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerMessagepage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerMessagepage;
import com.oybx.fyp_application.employer_section.fragment.employer_messagepage.FragmentEmployerChatroompage;
import com.oybx.fyp_application.employer_section.fragment.employer_requestpage.FragmentEmployerSpecificPostRequest;
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.adapter.StudentChatRoomRecyclerViewAdapter;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentMessagepage;
import com.oybx.fyp_application.student_section.fragment.student_message.FragmentStudentChatroompage;

import java.util.ArrayList;

public class EmployerChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<EmployerChatRoomRecyclerViewAdapter.MyViewHolder> {
    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<ChatRoomInfo> chatRoomInfoArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerChatRoomRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<ChatRoomInfo> chatRoomInfoArrayList) {
        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.chatRoomInfoArrayList = chatRoomInfoArrayList;
    }

    @NonNull
    @Override
    public EmployerChatRoomRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_room_list, parent, false);

        EmployerChatRoomRecyclerViewAdapter.MyViewHolder vh = new EmployerChatRoomRecyclerViewAdapter.MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerChatRoomRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
        final ChatRoomInfo currentChatRoomInfo = chatRoomInfoArrayList.get(position);

        myViewHolder.oppenentProfilePic_imageView.setImageBitmap(currentChatRoomInfo.getOppenentInfo().getProfilepic());
        myViewHolder.oppenentName_textView.setText(currentChatRoomInfo.getOppenentInfo().getFullName());
        myViewHolder.oppenentSkillCategory_textView.setText(currentChatRoomInfo.getOppenentInfo().getGeneralSkill_Category());

        myViewHolder.bigContainer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                currentActivity.runOnUiThread(new Runnable() {
                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    @Override
                    public void run() {
                        if (currentLayoutName.equals("EmployerMessagepage")) {

                            Fragment chatRoomPageFragment = new FragmentEmployerChatroompage();
                            FragmentEmployerChatroompage.setSelectedChatRoomInfo(currentChatRoomInfo);



                            ((EmployerMessagepage)currentActivity).changeFragmentPage(chatRoomPageFragment, "chatRoomPageFragment",null);


                            // Stuff that updates the UI
                        }

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        //return the item count in list
        if(chatRoomInfoArrayList.size() == 0){
            ((FragmentEmployerMessagepage)parentFragment).displayNoMessageSection(true);
        }else {
            ((FragmentEmployerMessagepage)parentFragment).displayNoMessageSection(false);
        }
        return chatRoomInfoArrayList.size();
    }


    public void setNewArrayList( ArrayList<ChatRoomInfo> chatRoomInfoArrayList){
        this.chatRoomInfoArrayList = chatRoomInfoArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView oppenentProfilePic_imageView;
        public TextView oppenentName_textView;
        public TextView oppenentSkillCategory_textView;
        public LinearLayout bigContainer_linearLayout;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppenentProfilePic_imageView = (ImageView) itemView.findViewById(R.id.layout_chat_room_list_headerContainer_profilePic);
            oppenentName_textView = (TextView) itemView.findViewById(R.id.layout_chat_room_list_chatUsername);
            oppenentSkillCategory_textView = (TextView) itemView.findViewById(R.id.layout_chat_room_list_chatUser_generalSkillCategory);
            bigContainer_linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_chat_room_list_bigContainer);



        }
    }
}

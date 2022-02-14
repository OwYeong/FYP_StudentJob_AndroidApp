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
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.adapter.StudentChatSearchRecylerViewAdapter;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentMessagepage;
import com.oybx.fyp_application.student_section.fragment.student_message.FragmentStudentChatroompage;

import java.util.ArrayList;

public class EmployerChatSearchRecyclerViewAdapter extends RecyclerView.Adapter<EmployerChatSearchRecyclerViewAdapter.MyViewHolder> {
    private Context myContext;
    private Fragment parentFragment;
    private ArrayList<ChatRoomUserInfo> chatRoomSearchedArrayList = new ArrayList<>();//Array list For storing Employer Post info


    public EmployerChatSearchRecyclerViewAdapter(Context myContext, Fragment parentFragment, ArrayList<ChatRoomUserInfo> chatRoomSearchedArrayList) {
        this.myContext = myContext;
        this.parentFragment = parentFragment;
        this.chatRoomSearchedArrayList = chatRoomSearchedArrayList;
    }

    @NonNull
    @Override
    public EmployerChatSearchRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_room_list, parent, false);

        EmployerChatSearchRecyclerViewAdapter.MyViewHolder vh = new EmployerChatSearchRecyclerViewAdapter.MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerChatSearchRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
        final ChatRoomUserInfo currentSearchedChatUserInfo = chatRoomSearchedArrayList.get(position);

        myViewHolder.oppenentProfilePic_imageView.setImageBitmap(currentSearchedChatUserInfo.getProfilepic());
        myViewHolder.oppenentName_textView.setText(currentSearchedChatUserInfo.getFullName());
        myViewHolder.oppenentSkillCategory_textView.setText(currentSearchedChatUserInfo.getGeneralSkill_Category());

        myViewHolder.bigContainer_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentEmployerMessagepage)parentFragment).emptySearchBarText();
                final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                currentActivity.runOnUiThread(new Runnable() {
                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    @Override
                    public void run() {
                        if (currentLayoutName.equals("EmployerMessagepage")) {

                            Fragment chatRoomPageFragment = new FragmentEmployerChatroompage();

                            if(EmployerMessagepage.getChatRoomInfoWithOpponentUsername(currentSearchedChatUserInfo.getUsername()) != null){
                                FragmentEmployerChatroompage.setSelectedChatRoomInfo(EmployerMessagepage.getChatRoomInfoWithOpponentUsername(currentSearchedChatUserInfo.getUsername()));
                            }else{
                                //new chat room
                                ChatRoomInfo selectedChatRoomInfo = new ChatRoomInfo("-1", currentSearchedChatUserInfo);
                                FragmentEmployerChatroompage.setSelectedChatRoomInfo(selectedChatRoomInfo);

                            }


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
        return chatRoomSearchedArrayList.size();
    }


    public void setNewArrayList( ArrayList<ChatRoomUserInfo> chatRoomSearchedArrayList){
        this.chatRoomSearchedArrayList = chatRoomSearchedArrayList;
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

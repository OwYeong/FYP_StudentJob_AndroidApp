package com.oybx.fyp_application.employer_section.fragment.employer_messagepage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerMessagepage;
import com.oybx.fyp_application.employer_section.adapter.EmployerChatMessageRecyclerViewAdapter;
import com.oybx.fyp_application.infomation_classes.ChatMessage;
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.adapter.StudentChatMessageRecyclerViewAdapter;
import com.oybx.fyp_application.student_section.fragment.student_message.FragmentStudentChatroompage;

import java.util.ArrayList;

import communication_section.ClientCore;

public class FragmentEmployerChatroompage extends Fragment {
    private static final String TAG = "Frag-Emp-chatRoom";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private static RecyclerView myRecyclerView_recyclerView;
    private EditText messageBox_editText;
    private ImageView send_btn_imageView;

    private LinearLayoutManager myLayoutManager;
    private static EmployerChatMessageRecyclerViewAdapter employerChatMessageRecyclerViewAdapter;

    private TextView chatroomTitle_textView;
    private static ChatRoomInfo selectedChatRoomInfo;
    private static ArrayList<ChatMessage> chatMessageArrayList;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.fragment_employer_chatroompage_sent_icon:
                    String message = messageBox_editText.getText().toString().trim();
                    if(message.length() != 0){
                        requestForSentMessage(message);
                        messageBox_editText.setText("");
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_employer_chatroompage, container, false);
        Log.i(TAG, "on create view called");


        myRecyclerView_recyclerView = (RecyclerView) layout.findViewById(R.id.fragment_employer_chatroompage_recyclerView);
        messageBox_editText = (EditText) layout.findViewById(R.id.fragment_employer_chatroompage_messageBox);
        chatroomTitle_textView = (TextView) layout.findViewById(R.id.fragment_employer_chatroompage_header);

        send_btn_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_chatroompage_sent_icon);
        send_btn_imageView.setOnClickListener(onClickListener);


        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        myLayoutManager.setStackFromEnd(true);
        myRecyclerView_recyclerView.setLayoutManager(myLayoutManager);
        chatroomTitle_textView.setText(selectedChatRoomInfo.getOppenentInfo().getFullName());

        employerChatMessageRecyclerViewAdapter = new EmployerChatMessageRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, chatMessageArrayList, selectedChatRoomInfo.getOppenentInfo().getProfilepic());

        myRecyclerView_recyclerView.setAdapter(employerChatMessageRecyclerViewAdapter);
        employerChatMessageRecyclerViewAdapter.notifyDataSetChanged();
    }

    public static void setSelectedChatRoomInfo(ChatRoomInfo selectedChatRoomInfo) {
        FragmentEmployerChatroompage.selectedChatRoomInfo = selectedChatRoomInfo;
        chatMessageArrayList = selectedChatRoomInfo.getAllChatMessageArrayList();

    }

    public static void updateSelectedChatRoomInfo() {

        if(selectedChatRoomInfo != null) {
            String chatRoomOpponentUsername = selectedChatRoomInfo.getOppenentInfo().getUsername();
            selectedChatRoomInfo = EmployerMessagepage.getChatRoomInfoWithOpponentUsername(chatRoomOpponentUsername);
            chatMessageArrayList = selectedChatRoomInfo.getAllChatMessageArrayList();
            employerChatMessageRecyclerViewAdapter.setNewArrayList(chatMessageArrayList);
            employerChatMessageRecyclerViewAdapter.notifyDataSetChanged();
            myRecyclerView_recyclerView.scrollToPosition(myRecyclerView_recyclerView.getAdapter().getItemCount() - 1);
        }
    }

    public void requestForSentMessage(String message){
        String from = ApplicationManager.getMyEmployerAccountInfo().getUsername();
        String to = selectedChatRoomInfo.getOppenentInfo().getUsername();
        String chatRoomId = selectedChatRoomInfo.getChatRoomId();

        ClientCore.sentMessageChatRoom(message, from, to, chatRoomId);

    }
}

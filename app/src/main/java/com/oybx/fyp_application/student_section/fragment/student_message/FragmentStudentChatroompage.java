package com.oybx.fyp_application.student_section.fragment.student_message;

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
import com.oybx.fyp_application.infomation_classes.ChatMessage;
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.adapter.StudentChatMessageRecyclerViewAdapter;
import com.oybx.fyp_application.student_section.adapter.StudentChatRoomRecyclerViewAdapter;

import java.util.ArrayList;

import communication_section.ClientCore;

public class FragmentStudentChatroompage extends Fragment {

    private static final String TAG = "Frag-Std-chatRoom";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private static RecyclerView myRecyclerView_recyclerView;
    private EditText messageBox_editText;
    private ImageView send_btn_imageView;


    private LinearLayoutManager myLayoutManager;
    private static StudentChatMessageRecyclerViewAdapter studentChatMessageRecyclerViewAdapter;

    private TextView chatroomTitle_textView;
    private static ChatRoomInfo selectedChatRoomInfo;
    private static ArrayList<ChatMessage> chatMessageArrayList;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.fragment_student_chatroompage_sent_icon:
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

        View layout = inflater.inflate(R.layout.fragment_student_chatroompage, container, false);
        Log.i(TAG, "on create view called");


        myRecyclerView_recyclerView = (RecyclerView) layout.findViewById(R.id.fragment_student_chatroompage_recyclerView);
        messageBox_editText = (EditText) layout.findViewById(R.id.fragment_student_chatroompage_messageBox);
        chatroomTitle_textView = (TextView) layout.findViewById(R.id.fragment_student_chatroompage_header);

        send_btn_imageView = (ImageView) layout.findViewById(R.id.fragment_student_chatroompage_sent_icon);
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

        studentChatMessageRecyclerViewAdapter = new StudentChatMessageRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, chatMessageArrayList, selectedChatRoomInfo.getOppenentInfo().getProfilepic());

        myRecyclerView_recyclerView.setAdapter(studentChatMessageRecyclerViewAdapter);
        studentChatMessageRecyclerViewAdapter.notifyDataSetChanged();
    }

    public static void setSelectedChatRoomInfo(ChatRoomInfo selectedChatRoomInfo) {
        FragmentStudentChatroompage.selectedChatRoomInfo = selectedChatRoomInfo;
        chatMessageArrayList = selectedChatRoomInfo.getAllChatMessageArrayList();

    }

    public static void updateSelectedChatRoomInfo() {

        if(selectedChatRoomInfo != null) {
            String chatRoomOpponentUsername = selectedChatRoomInfo.getOppenentInfo().getUsername();
            selectedChatRoomInfo = StudentMessagepage.getChatRoomInfoWithOpponentUsername(chatRoomOpponentUsername);
            chatMessageArrayList = selectedChatRoomInfo.getAllChatMessageArrayList();
            studentChatMessageRecyclerViewAdapter.setNewArrayList(chatMessageArrayList);
            studentChatMessageRecyclerViewAdapter.notifyDataSetChanged();
            myRecyclerView_recyclerView.scrollToPosition(myRecyclerView_recyclerView.getAdapter().getItemCount() - 1);
        }
    }

    public void requestForSentMessage(String message){
        String from = ApplicationManager.getMyStudentAccountInfo().getUsername();
        String to = selectedChatRoomInfo.getOppenentInfo().getUsername();
        String chatRoomId = selectedChatRoomInfo.getChatRoomId();

        ClientCore.sentMessageChatRoom(message, from, to, chatRoomId);

    }




}

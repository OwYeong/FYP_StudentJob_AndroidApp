package com.oybx.fyp_application.student_section.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.adapter.StudentChatRoomRecyclerViewAdapter;
import com.oybx.fyp_application.student_section.adapter.StudentChatSearchRecylerViewAdapter;

import java.util.ArrayList;

import communication_section.ClientCore;

public class FragmentStudentMessagepage extends Fragment {
    private static final String TAG="Fragment-messagePage";
    private EditText searchbar_editText;
    private String currentTextInSearchBar;
    private static RecyclerView chatRoom_recyclerView;
    private static RecyclerView searchList_recyclerView;

    private static StudentChatRoomRecyclerViewAdapter chatRoomRecyclerViewAdapter;
    private static StudentChatSearchRecylerViewAdapter chatRoomSearchRecyclerViewAdapter;
    private LinearLayoutManager myLayoutManager;

    private RelativeLayout noMessageSection_relativeLayout;

    private static ArrayList<ChatRoomInfo> chatRoomInfoArrayList = new ArrayList<>();
    private static ArrayList<ChatRoomUserInfo> chatRoomSearchedArrayList = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_student_messagepage, container, false);
        Log.i(TAG, "on create view called");

        searchbar_editText = (EditText) layout.findViewById(R.id.fragment_search_messagepage_searchbar);
        chatRoom_recyclerView = (RecyclerView) layout.findViewById(R.id.fragment_std_messagepage_chatRoom_recyclerview);
        searchList_recyclerView = (RecyclerView) layout.findViewById(R.id.fragment_std_messagepage_searchList_recyclerview);
        noMessageSection_relativeLayout = (RelativeLayout) layout.findViewById(R.id.fragment_student_messagepage_noMessageSection);
        //search will only show when the search bar is focused
        searchList_recyclerView.setVisibility(View.GONE);

        searchbar_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentTextInSearchBar = searchbar_editText.getText().toString();

                if(currentTextInSearchBar.equals("")){
                    searchList_recyclerView.setVisibility(View.GONE);
                }else{
                    requestForSearchChatRoom(currentTextInSearchBar);
                    searchList_recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        LinearLayoutManager searchListLinearLayoutManager = new LinearLayoutManager(ApplicationManager.getCurrentAppContext());
        chatRoom_recyclerView.setLayoutManager(myLayoutManager);
        searchList_recyclerView.setLayoutManager(searchListLinearLayoutManager);


        chatRoomRecyclerViewAdapter = new StudentChatRoomRecyclerViewAdapter(ApplicationManager.getCurrentAppContext(), this, chatRoomInfoArrayList);

        chatRoom_recyclerView.setAdapter(chatRoomRecyclerViewAdapter);
        chatRoomRecyclerViewAdapter.notifyDataSetChanged();

        chatRoomSearchRecyclerViewAdapter = new StudentChatSearchRecylerViewAdapter(ApplicationManager.getCurrentAppContext(), this, chatRoomSearchedArrayList);
        searchList_recyclerView.setAdapter(chatRoomSearchRecyclerViewAdapter);
        chatRoomSearchRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "on activity create view called");


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    public static void getFullChatRoomInfoArrayList(){

        //get arraylist from the parent
        chatRoomInfoArrayList = StudentMessagepage.getFullChatRoomInfoArrayList();

        updateChatRoomInfoArrayList(chatRoomInfoArrayList);


    }

    public static void setSearchedChatRoomInfoArrayList( ArrayList<ChatRoomUserInfo> chatRoomResultSearchedArrayList ){

        chatRoomSearchedArrayList = chatRoomResultSearchedArrayList;

        updateRecyclerViewSearchedChatRoomInfoArrayList(chatRoomSearchedArrayList);


    }

    public static void updateRecyclerViewSearchedChatRoomInfoArrayList( ArrayList<ChatRoomUserInfo> chatRoomResultSearchedArrayList){

        Log.i(TAG,"asdaasdasdasds" + chatRoomResultSearchedArrayList.size());
        try {

            if(searchList_recyclerView != null) {
                chatRoomSearchRecyclerViewAdapter.setNewArrayList(chatRoomResultSearchedArrayList);
                chatRoomSearchRecyclerViewAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updateChatRoomInfoArrayList( ArrayList<ChatRoomInfo> newChatRoomInfoArrayList){

        try {
            chatRoomInfoArrayList = newChatRoomInfoArrayList;

            if(chatRoom_recyclerView != null) {
                chatRoomRecyclerViewAdapter.setNewArrayList(chatRoomInfoArrayList);
                chatRoomRecyclerViewAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void requestForSearchChatRoom(String keyword){
        ClientCore.sentSearchChatRoom(keyword, ApplicationManager.getMyStudentAccountInfo().getUsername());

    }

    public void emptySearchBarText(){
        searchbar_editText.setText("");
    }

    public void displayNoMessageSection(boolean yesOrNo){

        if(yesOrNo){
            noMessageSection_relativeLayout.setVisibility(View.VISIBLE);
        }else {
            //if false
            noMessageSection_relativeLayout.setVisibility(View.GONE);
        }


    }





}


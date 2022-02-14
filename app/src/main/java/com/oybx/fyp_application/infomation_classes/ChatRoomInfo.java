package com.oybx.fyp_application.infomation_classes;

import java.util.ArrayList;

public class ChatRoomInfo {


    private String chatRoomId;

    private String user1;//username who involved in this chat room
    private String user2;//username who involved in this chat room

    private String user1OrUser2;//this indicate i m user 1 or user 2

    private ChatRoomUserInfo oppenentInfo;//the another info

    private ArrayList<ChatMessage> allChatMessageArrayList = new ArrayList<>();

    public ChatRoomInfo(String chatRoomId, String user1, String user2, String user1OrUser2, ChatRoomUserInfo oppenentInfo, ArrayList<ChatMessage> allChatMessageArrayList) {
        this.chatRoomId = chatRoomId;
        this.user1 = user1;
        this.user2 = user2;
        this.user1OrUser2 = user1OrUser2;
        this.oppenentInfo = oppenentInfo;
        this.allChatMessageArrayList = allChatMessageArrayList;
    }

    public ChatRoomInfo(String chatRoomId, ChatRoomUserInfo oppenentInfo) {
        this.chatRoomId = chatRoomId;
        this.oppenentInfo = oppenentInfo;

    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public ChatRoomUserInfo getOppenentInfo() {
        return oppenentInfo;
    }

    public void setOppenentInfo(ChatRoomUserInfo oppenentInfo) {
        this.oppenentInfo = oppenentInfo;
    }

    public ArrayList<ChatMessage> getAllChatMessageArrayList() {
        return allChatMessageArrayList;
    }

    public void setAllChatMessageArrayList(ArrayList<ChatMessage> allChatMessageArrayList) {
        this.allChatMessageArrayList = allChatMessageArrayList;
    }

    public String getUser1OrUser2() {
        return user1OrUser2;
    }
}

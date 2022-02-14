package com.oybx.fyp_application.infomation_classes;

import android.graphics.Bitmap;

public class ChatRoomUserInfo {

    private String username;
    private Bitmap profilepic;
    private String firstName;
    private String lastName;
    private String generalSkill_Category;
    private String fullName;

    public ChatRoomUserInfo(String username, Bitmap profilepic, String firstName, String lastName, String generalSkill_Category) {
        this.username = username;
        this.profilepic = profilepic;
        this.firstName = firstName;
        this.lastName = lastName;
        this.generalSkill_Category = generalSkill_Category;
        if(lastName != null) {
            fullName = this.firstName + " " + this.lastName;
        }else{
            fullName = firstName;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(Bitmap profilepic) {
        this.profilepic = profilepic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGeneralSkill_Category() {
        return generalSkill_Category;
    }

    public void setGeneralSkill_Category(String generalSkill_Category) {
        this.generalSkill_Category = generalSkill_Category;
    }

    public String getFullName() {
        return fullName;
    }
}

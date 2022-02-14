package com.oybx.fyp_application.infomation_classes;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class StudentAccountInfo {
    private String username, accountType, profileId;
    private Bitmap profilePic;
    private String fName,lName, generalSkillCategory;
    private String aboutMe, collegeName, courseName, email, contactNum, whatsappUrl, twitterUrl, facebookUrl;

    private String fullName;

    private ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList;
    private ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList;


    public StudentAccountInfo(String username, String accountType, String profileId, Bitmap profilePic, String fName, String lName, String generalSkillCategory, String aboutMe, String collegeName, String courseName, String email, String contactNum, String whatsappUrl, String twitterUrl, String facebookUrl, ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList, ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList) {
        setUsername(username);
        setAccountType(accountType);
        setProfileId(profileId);
        setProfilePic(profilePic);
        setfName(fName);
        setlName(lName);
        setGeneralSkillCategory(generalSkillCategory);
        setAboutMe(aboutMe);
        setCollegeName(collegeName);
        setCourseName(courseName);
        setEmail(email);
        setContactNum(contactNum);
        setWhatsappUrl(whatsappUrl);
        setTwitterUrl(twitterUrl);
        setFacebookUrl(facebookUrl);
        setStudentPortfolioInfoArrayList(studentPortfolioInfoArrayList);
        setStudentSoftwareSkillInfoArrayList(studentSoftwareSkillInfoArrayList);
    }





    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;

        fullName = fName + " " + lName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;

        fullName = fName + " " + lName;
    }

    public String getGeneralSkillCategory() {
        return generalSkillCategory;
    }

    public void setGeneralSkillCategory(String generalSkillCategory) {
        this.generalSkillCategory = generalSkillCategory;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getWhatsappUrl() {
        return whatsappUrl;
    }

    public void setWhatsappUrl(String whatsappUrl) {
        this.whatsappUrl = whatsappUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public ArrayList<StudentPortfolioInfo> getStudentPortfolioInfoArrayList() {
        return studentPortfolioInfoArrayList;
    }

    public void setStudentPortfolioInfoArrayList(ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList) {
        this.studentPortfolioInfoArrayList = studentPortfolioInfoArrayList;
    }

    public ArrayList<StudentSoftwareSkillInfo> getStudentSoftwareSkillInfoArrayList() {
        return studentSoftwareSkillInfoArrayList;
    }

    public void setStudentSoftwareSkillInfoArrayList(ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList) {
        this.studentSoftwareSkillInfoArrayList = studentSoftwareSkillInfoArrayList;
    }
}

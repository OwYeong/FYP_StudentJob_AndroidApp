package com.oybx.fyp_application.infomation_classes;

import android.graphics.Bitmap;

public class EmployerPostInfo {

    private Bitmap proPic,postPic;
    private String employerUsername;
    private String hirePostId, name, postTitle, postDesc, jobType, offers, dateTimePosted;

    private String location, workingHours;

    private String postSkillCategory;
    private String postStatus;


    public EmployerPostInfo(String employerUsername, Bitmap proPic, Bitmap postPic, String hirePostId, String name , String postTitle, String postDesc,
                            String jobType, String offers, String dateTimePosted, String location, String workingHours, String postSkillCategory, String postStatus ){
        this.employerUsername = employerUsername;
        this.proPic = proPic;
        this.postPic = postPic;
        this.hirePostId = hirePostId;
        this.name = name;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.jobType = jobType;
        this.offers = offers;
        this.dateTimePosted = dateTimePosted;
        this.location = location;
        this.workingHours = workingHours;
        this.postSkillCategory = postSkillCategory;
        this.postStatus = postStatus;

    }

    public EmployerPostInfo(String employerUsername,Bitmap proPic, Bitmap postPic, String hirePostId, String name , String postTitle, String postDesc,
                            String jobType, String offers, String dateTimePosted, String location, String workingHours, String postSkillCategory){
        this.employerUsername = employerUsername;
        this.proPic = proPic;
        this.postPic = postPic;
        this.hirePostId = hirePostId;
        this.name = name;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.jobType = jobType;
        this.offers = offers;
        this.dateTimePosted = dateTimePosted;
        this.location = location;
        this.workingHours = workingHours;
        this.postSkillCategory = postSkillCategory;
        this.postStatus = postStatus;

    }

    public String getHirePostId() {
        return hirePostId;
    }

    public String getName() {
        return name;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public String getJobType() {
        return jobType;
    }

    public String getOffers() {
        return offers;
    }



    public String getLocation() {
        return location;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public Bitmap getPostPic() {
        return postPic;
    }

    public String getDateTimePosted() {
        return dateTimePosted;
    }

    public Bitmap getProPic() {
        return proPic;
    }

    public String getPostSkillCategory() {
        return postSkillCategory;
    }

    public void setPostSkillCategory(String postSkillCategory) {
        this.postSkillCategory = postSkillCategory;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getEmployerUsername() {
        return employerUsername;
    }

}

package com.oybx.fyp_application.infomation_classes;

import android.graphics.Bitmap;

public class EmployerMyRequestInfo {

    private String hirePostId;
    private String employerName;
    private String requesterUsername;
    private Bitmap requesterProPic;
    private String requesterSkillCat;
    private String requesterName;


    //(Variables)request for post
    private String postRequestDateTime;
    private String postRequestMessage;
    private String postRequestStatus;
    private String postRequestStatusAddedDateTime;

    //(Variable) request for Interview
    private String interviewRequestDateTime;
    private String interviewRequestMessage;
    private String interviewDate;
    private String interviewTime;
    private String interviewLocation;
    private String interviewRequestStatus;

    public EmployerMyRequestInfo(String hirePostId, String employerName, String requesterUsername, Bitmap requesterProPic, String requesterSkillCat, String requesterName, String postRequestDateTime, String postRequestMessage, String postRequestStatus, String postRequestStatusAddedDateTime, String interviewRequestDateTime, String interviewRequestMessage, String interviewDate, String interviewTime, String interviewLocation, String interviewRequestStatus) {
        this.hirePostId = hirePostId;
        this.employerName = employerName;
        this.requesterUsername = requesterUsername;
        this.requesterProPic = requesterProPic;
        this.requesterSkillCat = requesterSkillCat;
        this.requesterName = requesterName;
        this.postRequestDateTime = postRequestDateTime;
        this.postRequestMessage = postRequestMessage;
        this.postRequestStatus = postRequestStatus;
        this.postRequestStatusAddedDateTime = postRequestStatusAddedDateTime;
        this.interviewRequestDateTime = interviewRequestDateTime;
        this.interviewRequestMessage = interviewRequestMessage;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewLocation = interviewLocation;
        this.interviewRequestStatus = interviewRequestStatus;
    }

    public String getHirePostId() {
        return hirePostId;
    }

    public void setHirePostId(String hirePostId) {
        this.hirePostId = hirePostId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getRequestUsername() {
        return requesterUsername;
    }

    public void setRequestUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getPostRequestDateTime() {
        return postRequestDateTime;
    }

    public void setPostRequestDateTime(String postRequestDateTime) {
        this.postRequestDateTime = postRequestDateTime;
    }

    public String getPostRequestMessage() {
        return postRequestMessage;
    }

    public void setPostRequestMessage(String postRequestMessage) {
        this.postRequestMessage = postRequestMessage;
    }

    public String getPostRequestStatus() {
        return postRequestStatus;
    }

    public void setPostRequestStatus(String postRequestStatus) {
        this.postRequestStatus = postRequestStatus;
    }

    public String getInterviewRequestDateTime() {
        return interviewRequestDateTime;
    }

    public void setInterviewRequestDateTime(String interviewRequestDateTime) {
        this.interviewRequestDateTime = interviewRequestDateTime;
    }

    public String getInterviewRequestMessage() {
        return interviewRequestMessage;
    }

    public void setInterviewRequestMessage(String interviewRequestMessage) {
        this.interviewRequestMessage = interviewRequestMessage;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewLocation() {
        return interviewLocation;
    }

    public void setInterviewLocation(String interviewLocation) {
        this.interviewLocation = interviewLocation;
    }

    public String getInterviewRequestStatus() {
        return interviewRequestStatus;
    }

    public void setInterviewRequestStatus(String interviewRequestStatus) {
        this.interviewRequestStatus = interviewRequestStatus;
    }

    public Bitmap getRequesterProPic() {
        return requesterProPic;
    }

    public void setRequesterProPic(Bitmap requesterProPic) {
        this.requesterProPic = requesterProPic;
    }

    public String getRequesterSkillCat() {
        return requesterSkillCat;
    }

    public void setRequesterSkillCat(String requesterSkillCat) {
        this.requesterSkillCat = requesterSkillCat;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getPostRequestStatusAddedDateTime() {
        return postRequestStatusAddedDateTime;
    }

    public void setPostRequestStatusAddedDateTime(String postRequestStatusAddedDateTime) {
        this.postRequestStatusAddedDateTime = postRequestStatusAddedDateTime;
    }
}

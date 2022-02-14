package com.oybx.fyp_application.infomation_classes;

public class StudentMyRequestInfo {

    private String hirePostId;
    private String requesterUsername;
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

    //HirepostInfo
    private EmployerPostInfo hirePostInfo;


    public StudentMyRequestInfo(String hirePostId, String requesterUsername, String requesterName, String postRequestDateTime, String postRequestMessage, String postRequestStatus, String postRequestStatusAddedDateTime, String interviewRequestDateTime, String interviewRequestMessage, String interviewDate, String interviewTime, String interviewLocation, String interviewRequestStatus, EmployerPostInfo hirePostInfo) {
        this.hirePostId = hirePostId;
        this.requesterUsername = requesterUsername;
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
        this.hirePostInfo = hirePostInfo;
    }

    public String getHirePostId() {
        return hirePostId;
    }

    public void setHirePostId(String hirePostId) {
        this.hirePostId = hirePostId;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getPostRequestDateTime() {
        return postRequestDateTime;
    }

    public void setPostRequestDateTime(String postRequestDateTime) {
        this.postRequestDateTime = postRequestDateTime;
    }

    public String getPostRequestStatus() {
        return postRequestStatus;
    }

    public void setPostRequestStatus(String postRequestStatus) {
        this.postRequestStatus = postRequestStatus;
    }

    public String getPostRequestMessage() {
        return postRequestMessage;
    }

    public void setPostRequestMessage(String postRequestMessage) {
        this.postRequestMessage = postRequestMessage;
    }

    public String getPostRequestStatusAddedDateTime() {
        return postRequestStatusAddedDateTime;
    }

    public void setPostRequestStatusAddedDateTime(String postRequestStatusAddedDateTime) {
        this.postRequestStatusAddedDateTime = postRequestStatusAddedDateTime;
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

    public EmployerPostInfo getHirePostInfo() {
        return hirePostInfo;
    }

    public void setHirePostInfo(EmployerPostInfo hirePostInfo) {
        this.hirePostInfo = hirePostInfo;
    }
}

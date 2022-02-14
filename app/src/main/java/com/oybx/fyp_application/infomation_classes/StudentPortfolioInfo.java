package com.oybx.fyp_application.infomation_classes;

import android.graphics.Bitmap;

public class StudentPortfolioInfo {

    private String portfolioId;
    private String portfolioType;
    private Bitmap portfolioPicture;
    private String portfolioDescription;
    private String portfolioUrl;

    public StudentPortfolioInfo(String portfolioId, String portfolioType, Bitmap portfolioPicture, String portfolioDescription, String portfolioUrl){
        this.portfolioId = portfolioId;
        this.portfolioType = portfolioType;
        this.portfolioPicture = portfolioPicture;
        this.portfolioDescription = portfolioDescription;
        this.portfolioUrl = portfolioUrl;
    }


    public String getPortfolioId() {
        return portfolioId;
    }

    public String getPortfolioType() {
        return portfolioType;
    }

    public Bitmap getPortfolioPicture() {
        return portfolioPicture;
    }

    public String getPortfolioDescription() {
        return portfolioDescription;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }
}

package com.oybx.fyp_application.infomation_classes;

public class StudentSoftwareSkillInfo {
    private String softwareSkillId;
    private String softwareName;
    private int skillLevel;


    public StudentSoftwareSkillInfo( String softwareSkillId, String softwareName, int skillLevel) {
        this.softwareSkillId = softwareSkillId;
        this.softwareName = softwareName;
        this.skillLevel = skillLevel;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public String getSoftwareSkillId() {
        return softwareSkillId;
    }
}

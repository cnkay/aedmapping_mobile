package com.app.aedmapping.Retrofit.Report;

public class CreateReportRequest {
    private String type;
    private String comment;
    private String mail;
    private Integer defibrillator_id;

    public CreateReportRequest(String type, String comment, String mail, Integer defibrillator_id) {
        this.type = type;
        this.comment = comment;
        this.mail = mail;
        this.defibrillator_id = defibrillator_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getDefibrillator_id() {
        return defibrillator_id;
    }

    public void setDefibrillator_id(Integer defibrillator_id) {
        this.defibrillator_id = defibrillator_id;
    }
}

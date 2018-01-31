package com.example.kimjaeseung.cultureseoul2.domain;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class CultureEvent implements Serializable {

    @SerializedName("CULTCODE")
    private long cultCode;
    @SerializedName("SUBJCODE")
    private long subjCode;
    @SerializedName("CODENAME")
    private String codeName;
    @SerializedName("TITLE")
    private String title;
    @SerializedName("STRTDATE")
    private Date startDate;
    @SerializedName("END_DATE")
    private Date endDate;
    @SerializedName("TIME")
    private String time;
    @SerializedName("PLACE")
    private String place;
    @SerializedName("ORG_LINK")
    private String orgLink;
    @SerializedName("MAIN_IMG")
    private String mainImg;
    @SerializedName("HOMEPAGE")
    private String homePage;
    @SerializedName("USE_TRGT")
    private String useTrgt;
    @SerializedName("USE_FEE")
    private String useFee;
    @SerializedName("SPONSOR")
    private String sponsor;
    @SerializedName("INQUIRY")
    private String inquiry;
    @SerializedName("SUPPORT")
    private String support;
    @SerializedName("ETC_DESC")
    private String etcDesc;
    @SerializedName("AGELIMIT")
    private String ageLimit;
    @SerializedName("IS_FREE")
    private String isFree;
    @SerializedName("TICKET")
    private String ticket;
    @SerializedName("PROGRAM")
    private String program;
    @SerializedName("PLAYER")
    private String player;
    @SerializedName("CONTENTS")
    private String contents;
    @SerializedName("GCODE")
    private String gCode;

    public CultureEvent() {
    }

    public CultureEvent(long cultCode, long subjCode, String codeName, String title, Date startDate, Date endDate, String time, String place, String orgLink, String mainImg, String homePage, String useTrgt, String useFee, String sponsor, String inquiry, String support, String etcDesc, String ageLimit, String isFree, String ticket, String program) {
        this.cultCode = cultCode;
        this.subjCode = subjCode;
        this.codeName = codeName;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.place = place;
        this.orgLink = orgLink;
        this.mainImg = mainImg;
        this.homePage = homePage;
        this.useTrgt = useTrgt;
        this.useFee = useFee;
        this.sponsor = sponsor;
        this.inquiry = inquiry;
        this.support = support;
        this.etcDesc = etcDesc;
        this.ageLimit = ageLimit;
        this.isFree = isFree;
        this.ticket = ticket;
        this.program = program;
    }

    public long getCultCode() {
        return cultCode;
    }

    public void setCultCode(long cultCode) {
        this.cultCode = cultCode;
    }

    public long getSubjCode() {
        return subjCode;
    }

    public void setSubjCode(long subjCode) {
        this.subjCode = subjCode;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getTitle() {
        title = title.replaceAll("&#39;", "\'");
        title = title.replaceAll("&quot;", "\"");
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOrgLink() {
        return orgLink;
    }

    public void setOrgLink(String orgLink) {
        this.orgLink = orgLink;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getUseTrgt() {
        return useTrgt;
    }

    public void setUseTrgt(String useTrgt) {
        this.useTrgt = useTrgt;
    }

    public String getUseFee() {
        useFee = useFee.replaceAll("&#39;", "\'");
        useFee = useFee.replaceAll("&quot;", "\"");
        return useFee;
    }

    public void setUseFee(String useFee) {
        this.useFee = useFee;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getEtcDesc() {
        return etcDesc;
    }

    public void setEtcDesc(String etcDesc) {
        this.etcDesc = etcDesc;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}

package com.example.kimjaeseung.cultureseoul2.domain;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CultureEventWrapper {

    @SerializedName("list_total_count")
    private int totalCount;

    @SerializedName("RESULT")
    private CultureResult cultureResult;

    @SerializedName("row")
    private List<CultureEvent> cultureEventList;


    public CultureEventWrapper() {
    }

    public CultureEventWrapper(int totalCount, CultureResult cultureResult, List<CultureEvent> cultureEventList) {
        this.totalCount = totalCount;
        this.cultureResult = cultureResult;
        this.cultureEventList = cultureEventList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public CultureResult getCultureResult() {
        return cultureResult;
    }

    public void setCultureResult(CultureResult cultureResult) {
        this.cultureResult = cultureResult;
    }

    public List<CultureEvent> getCultureEventList() {
        return cultureEventList;
    }

    public void setCultureEventList(List<CultureEvent> cultureEventList) {
        this.cultureEventList = cultureEventList;
    }
}

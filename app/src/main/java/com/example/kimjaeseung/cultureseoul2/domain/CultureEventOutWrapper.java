package com.example.kimjaeseung.cultureseoul2.domain;


import com.google.gson.annotations.SerializedName;

public class CultureEventOutWrapper {

    @SerializedName("SearchConcertDetailService")
    private CultureEventWrapper cultureEventWrapper;

    public CultureEventOutWrapper() {

    }

    public CultureEventOutWrapper(CultureEventWrapper cultureEventWrapper) {
        this.cultureEventWrapper = cultureEventWrapper;
    }

    public CultureEventWrapper getCultureEventWrapper() {
        return cultureEventWrapper;
    }

    public void setCultureEventWrapper(CultureEventWrapper cultureEventWrapper) {
        this.cultureEventWrapper = cultureEventWrapper;
    }
}

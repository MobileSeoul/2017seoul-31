package com.example.kimjaeseung.cultureseoul2.network;


import com.example.kimjaeseung.cultureseoul2.domain.CultureEventOutWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CultureService {

    // http://openapi.seoul.go.kr:8088/sample/json/SearchConcertDetailService/1/5/23075/
    @GET("/{key}/{type}/{service}/{startIdx}/{endIdx}/{cultureCode}")
    Call<CultureEventOutWrapper> getCultureEvents(
            @Path("key") String key,
            @Path("type") String type,
            @Path("service") String service,
            @Path("startIdx") long startIdx,
            @Path("endIdx") long endIdx,
            @Path("cultureCode") String cultureCode
    );

}

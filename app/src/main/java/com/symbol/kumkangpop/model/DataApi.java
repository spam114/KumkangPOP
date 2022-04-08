package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.model.object.LoginInfo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface DataApi {
    // @Headers({"Content-Type: application/json"})
    //@FormUrlEncoded
    @POST("getDongProgressFloorReturn")
    Single<List<Dong>> getCountries(@Body SearchCondition searchCondition);

    @POST("CheckAppVersion")
    Single<AppVersion> CheckAppVersion(@Body SearchCondition searchCondition);

    @POST("checkAppProgramsPowerAndLoginHistory")
    Single<List<AppVersion>> checkAppProgramsPowerAndLoginHistory(@Body SearchCondition searchCondition);

    @POST("InsertAppLoginHistory")
    Single<AppVersion> InsertAppLoginHistory(@Body SearchCondition searchCondition);

    @POST("GetLoginInfoData")
    Single<LoginInfo> GetLoginInfoData(@Body SearchCondition searchCondition);

    //GetNoticeData
    @POST("GetNoticeData2")
    Single<Object> GetNoticeData2(@Body SearchCondition searchCondition);
}


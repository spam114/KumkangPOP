package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.BarcodeConvertPrint;
import com.symbol.kumkangpop.model.object.CodeData;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.model.object.LoginInfo;
import com.symbol.kumkangpop.model.object.Part;
import com.symbol.kumkangpop.model.object.PartSpec;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Scan;

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

    @POST("GetLoginInfoByPhoneNumber")
    Single<LoginInfo> GetLoginInfoByPhoneNumber(@Body SearchCondition searchCondition);

    @POST("GetPrintPCData")
    Single<LoginInfo> GetPrintPCData(@Body SearchCondition searchCondition);

    @POST("GetNoticeData2")
    Single<Object> GetNoticeData2(@Body SearchCondition searchCondition);

    @POST("GetUserImage")
    Single<String> GetUserImage(@Body SearchCondition searchCondition);

    //사업장 정보를 가져온다.
    @POST("GetBusinessClassData")
    Single<List<Object>> GetBusinessClassData(@Body SearchCondition searchCondition);

    @POST("GetSalesOrderData")
    Single<List<Object>> GetSalesOrderData(@Body SearchCondition searchCondition);

    @POST("GetStockInDataPOP")
    Single<List<Object>> GetStockInDataPOP(@Body SearchCondition searchCondition);

    @POST("CheckAWaitingQR")
    Single<Object> CheckAWaitingQR(@Body SearchCondition searchCondition);

    @POST("GetAWaitingDetail")
    Single<Object> GetAWaitingDetail(@Body SearchCondition searchCondition);

    @POST("GetADetail")
    Single<Object> GetADetail(@Body SearchCondition searchCondition);

    @POST("GetAWaitingPartForEdit")
    Single<List<Part>> GetAWaitingPartForEdit(@Body SearchCondition searchCondition);

    @POST("GetAWaitingPartSpecForEdit")
    Single<List<PartSpec>> GetAWaitingPartSpecForEdit(@Body SearchCondition searchCondition);

    @POST("UpdateShortData")
    Single<String> UpdateShortData(@Body SearchCondition searchCondition);

    //A급 Detail을 Update한다.
    @POST("UpdateADetail")
    Single<String> UpdateADetail(@Body SearchCondition searchCondition);


    @POST("GetAMaster")
    Single<List<Object>> GetAMaster(@Body SearchCondition searchCondition);

    @POST("CheckAQR")
    Single<Object> CheckAQR(@Body SearchCondition searchCondition);

    @POST("GetReport1Data")
    Single<List<Report>> GetReport1Data(@Body SearchCondition searchCondition);

    @POST("GetReport2Data")
    Single<List<Report>> GetReport2Data(@Body SearchCondition searchCondition);

    @POST("GetReport3Data")
    Single<List<Report>> GetReport3Data(@Body SearchCondition searchCondition);

    @POST("GetReport4Data")
    Single<List<Report>> GetReport4Data(@Body SearchCondition searchCondition);

    @POST("GetReport5Data")
    Single<List<Report>> GetReport5Data(@Body SearchCondition searchCondition);

    @POST("GetReport6Data")
    Single<List<Report>> GetReport6Data(@Body SearchCondition searchCondition);

    @POST("GetReport7Data")
    Single<List<Report>> GetReport7Data(@Body SearchCondition searchCondition);

    @POST("GetReport5Data2")
    Single<List<Report>> GetReport5Data2(@Body SearchCondition searchCondition);

    @POST("GetSaleTypeByCodeType")
    Single<List<CodeData>> GetSaleTypeByCodeType(@Body SearchCondition searchCondition);

    @POST("FNBarcodeConvertPrint")
    Single<BarcodeConvertPrint> FNBarcodeConvertPrint(@Body SearchCondition searchCondition);

    @POST("GetTest")
    Single<Scan> GetTest(@Body SearchCondition searchCondition);
}


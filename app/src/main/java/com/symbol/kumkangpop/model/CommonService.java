package com.symbol.kumkangpop.model;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.Common;
import com.symbol.kumkangpop.model.object.SData;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonService {

    // 통신 설정 (우리 서버와 연결하는 내용)
    private static final String BASE_URL = ApplicationClass.getResourses().getString(R.string.service_address); // 서버 주소 설정
    private static CommonService instance;//todo
    public static DataApi api = new Retrofit.Builder() // 서버와 연결
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class); // 서버와 연결하여 하나의 객체 형태로 통신 객체를 생성 -> 해당 api 객체를 통해 서버의 메소드를 호출 (DataApi 라는 인터페이스에 우리가 호출하는 메소드와 서버에서 작동하는 메소드를 기록)
    // 해당 코드를 통해 api가 서버와 연결되어 통신객체로 존재하게된다. api객체만 호출하면 서버와 연결되어있으므로 서버 함수 호출

    public static CommonService getInstance() { // getInstance 호출 시 본인 객체를 반환하며 api 서버 객체를 생성한다.
        if (instance == null) {
            instance = new CommonService();
            //통신 에러로그 확인을 위한 코드
            /*OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(DataApi.class);*/
        }
        return instance;
    }

    /*public Single<Common> GetTest() {// 객체의 사용//todo
        SearchCondition sc = new SearchCondition();
        sc.UserID = Users.UserID;
        return api.GetTest(sc);//todo
    }*/

    public Single<Common> Get(String apiName, SearchCondition sc) {// 객체의 사용//todo
        if (apiName.equals("GetNumConvertData")) {
            return api.GetNumConvertData(sc);
        }
        else if (apiName.equals("GetSalesOrderData")) {
            return api.GetSalesOrderData(sc);
        }
        else if (apiName.equals("GetStockInDataPOP")) {
            return api.GetStockInDataPOP(sc);
        }
        else if (apiName.equals("GetAMaster")) {
            return api.GetAMaster(sc);
        }
        else if(apiName.equals("GetItemTagCheck")){
            return api.GetItemTagCheck(sc);
        }
        else if(apiName.equals("GetSalesOrderDataHo")){
            return api.GetSalesOrderDataHo(sc);
        }
        else if(apiName.equals("PrintPacking")){
            return api.PrintPacking(sc);
        }
        else if(apiName.equals("GetPackingData")){
            return api.GetPackingData(sc);
        }
        else if(apiName.equals("GetSalesOrderDataCnt")){
            return api.GetSalesOrderDataCnt(sc);
        }
        else if(apiName.equals("ScanPacking")){
            return api.ScanPacking(sc);
        }
        else if(apiName.equals("GetPackingDataExists")){
            return api.GetPackingDataExists(sc);
        }
        else if(apiName.equals("GetPackingDetailDataSum")){
            return api.GetPackingDetailDataSum(sc);
        }
        else if(apiName.equals("GetPackingDetailData")){
            return api.GetPackingDetailData(sc);
        }
        else if(apiName.equals("GetItemTagData")){
            return api.GetItemTagData(sc);
        }
        else if(apiName.equals("UpdatePackingData")){
            return api.UpdatePackingData(sc);
        }
        else if(apiName.equals("GetBusinessClassDataAll")){
            return api.GetBusinessClassDataAll(sc);
        }
        else if(apiName.equals("GetLocationDataBusinessClass")){
            return api.GetLocationDataBusinessClass(sc);
        }
        else if(apiName.equals("GetContainerData")){
            return api.GetContainerData(sc);
        }
        else if(apiName.equals("SetContainerData")){
            return api.SetContainerData(sc);
        }
        else if(apiName.equals("InsertContainerPictureData")){
            return api.InsertContainerPictureData(sc);
        }
        else if(apiName.equals("GetStockOutDataCnt")){
            return api.GetStockOutDataCnt(sc);
        }
        else if(apiName.equals("ScanStockOut")){
            return api.ScanStockOut(sc);
        }
        else if(apiName.equals("GetStockInFlag")){
            return api.GetStockInFlag(sc);
        }
        else if(apiName.equals("InsertStockOutPictureData")){
            return api.InsertStockOutPictureData(sc);
        }
        else if(apiName.equals("GetStockOutData")){
            return api.GetStockOutData(sc);
        }
        else if(apiName.equals("UpdateStockOutData")){
            return api.UpdateStockOutData(sc);
        }
        else if(apiName.equals("GetDeptDataBusiness")){
            return api.GetDeptDataBusiness(sc);
        }
        else if(apiName.equals("GetCustomersDataClass")){
            return api.GetCustomersDataClass(sc);
        }
        else if(apiName.equals("GetRegionData")){
            return api.GetRegionData(sc);
        }
        else if(apiName.equals("GetStockOutDataExists")){
            return api.GetStockOutDataExists(sc);
        }
        else if(apiName.equals("GetStockOutDetailData")){
            return api.GetStockOutDetailData(sc);
        }
        else if(apiName.equals("EditStockOut")){
            return api.EditStockOut(sc);
        }
        else if(apiName.equals("GetSalesOrderDataLocation")){
            return api.GetSalesOrderDataLocation(sc);
        }
        else if(apiName.equals("ScanStockOutTag")){
            return api.ScanStockOutTag(sc);
        }
        else if(apiName.equals("ScanBundle")){
            return api.ScanBundle(sc);
        }
        else if(apiName.equals("GetBundleDataBundleNo")){
            return api.GetBundleDataBundleNo(sc);
        }
        else if(apiName.equals("GetBundleDataPackingNo")){
            return api.GetBundleDataPackingNo(sc);
        }
        else if(apiName.equals("EditBundle")){
            return api.EditBundle(sc);
        }
        else if(apiName.equals("ScanTransfer")){
            return api.ScanTransfer(sc);
        }
        else if(apiName.equals("EditTransfer")){
            return api.EditTransfer(sc);
        }
        else if(apiName.equals("ScanBring")){
            return api.ScanBring(sc);
        }
        else if(apiName.equals("UpdateStockOutDetailData")){
            return api.UpdateStockOutDetailData(sc);
        }
        else {
            return null;
        }
    }

    public Single<SData> GetStr(String apiName, SearchCondition sc) {// 객체의 사용//todo
        if (apiName.equals("GetPackingNo")) {
            return api.GetPackingNo();
        }
        else {
            return null;
        }
    }
}

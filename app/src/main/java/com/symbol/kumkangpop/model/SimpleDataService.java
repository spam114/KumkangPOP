package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.CodeData;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimpleDataService {
    private static final String BASE_URL = ApplicationClass.getResourses().getString(R.string.service_address);
    private static SimpleDataService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);

    public static SimpleDataService getInstance() {
        if (instance == null) {
            instance = new SimpleDataService();
        }
        return instance;
    }

    public Single<Object> GetSimpleData(String apiName) {
        SearchCondition sc = new SearchCondition();
        if (apiName.equals("GetNoticeData2")) {
            sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
            return api.GetNoticeData2(sc);
        }
        else {
            return null;
        }
    }



    public Single<Object> GetSimpleData(String apiName, SearchCondition sc) {
        if (apiName.equals("CheckAWaitingQR")) {
            return api.CheckAWaitingQR(sc);
        }
        else if (apiName.equals("GetAWaitingDetail")) {
            return api.GetAWaitingDetail(sc);
        }
        else if (apiName.equals("GetADetail")) {
            return api.GetADetail(sc);
        }
        else if (apiName.equals("CheckAQR")) {
            return api.CheckAQR(sc);
        }
        else {
            return null;
        }
    }

    public Single<List<Object>> GetSimpleDataList(String apiName) {
        SearchCondition sc = new SearchCondition();
        if (apiName.equals("GetBusinessClassData")) {
            sc.UserID = Users.UserID;
            return api.GetBusinessClassData(sc);
        } else {
            return null;
        }
    }

    public Single<List<CodeData>> GetCodeData(String apiName, SearchCondition sc) {
        if (apiName.equals("GetSaleTypeByCodeType")) {
            return api.GetSaleTypeByCodeType(sc);
        } else {
            return null;
        }
    }
}

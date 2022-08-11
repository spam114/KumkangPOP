package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AEditService {
    private static final String BASE_URL = ApplicationClass.getResourses().getString(R.string.service_address);
    private static AEditService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);

    public static AEditService getInstance() {
        if (instance == null) {
            instance = new AEditService();
        }
        return instance;
    }

    public Single<String> UpdateADetail(SearchCondition sc) {
        return api.UpdateADetail(sc);
    }


    /*public Single<List<Object>> GetSimpleDataList(String apiName) {
        SearchCondition sc = new SearchCondition();
        if (apiName.equals("GetBusinessClassData")) {
            sc.UserID = Users.UserID;
            return api.GetBusinessClassData(sc);
        } else {
            return null;
        }
    }*/
}

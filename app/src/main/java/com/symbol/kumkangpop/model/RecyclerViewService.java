package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewService {
    private static final String BASE_URL = ApplicationClass.getResourses().getString(R.string.service_address);
    private static RecyclerViewService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);

    public static RecyclerViewService getInstance() {
        if (instance == null) {
            instance = new RecyclerViewService();
        }
        return instance;
    }

    public Single<List<Object>> Get(String apiName) {
        SearchCondition sc = new SearchCondition();
        if (apiName.equals("GetSalesOrderData")) {
            sc.BusinessClassCode = Users.BusinessClassCode;
            sc.CustomerCode = Users.CustomerCode;
            sc.SaleOrderNo ="";
            return api.GetSalesOrderData(sc);
        }

        else {
            return null;
        }
    }

    public Single<List<Object>> Get(String apiName, SearchCondition sc) {
        if (apiName.equals("GetStockInDataPOP")) {
            return api.GetStockInDataPOP(sc);
        }
        else if (apiName.equals("GetAMaster")) {
            return api.GetAMaster(sc);
        }
        else {
            return null;
        }
    }
}

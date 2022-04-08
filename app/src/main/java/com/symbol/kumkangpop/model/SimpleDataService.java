package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(DataApi.class);
        }
        return instance;
    }

    public Single<Object> GetSimpleData(String apiName) {
        SearchCondition sc = new SearchCondition();
        if (apiName.equals("GetNoticeData2")) {
            sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
            return api.GetNoticeData2(sc);
        } else {
            return null;
        }
    }
}

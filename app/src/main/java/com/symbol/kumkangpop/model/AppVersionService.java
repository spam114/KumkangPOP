package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppVersionService {
    private static final String BASE_URL = ApplicationClass.getResourses().getString(R.string.service_address);
    private static AppVersionService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);


    public static AppVersionService getInstance() {
        if (instance == null) {
            instance = new AppVersionService();
              OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(DataApi.class);
        }
        return instance;
    }

    public Single<List<AppVersion>> checkAppVersion(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.checkAppVersion(searchCondition);//todo
    }

    public Single<List<AppVersion>> checkAppProgramsPowerAndLoginHistory(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.checkAppProgramsPowerAndLoginHistory(searchCondition);//todo
    }

    //insertAppLoginHistory
    public Single<List<AppVersion>> insertAppLoginHistory(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.insertAppLoginHistory(searchCondition);//todo
    }
}

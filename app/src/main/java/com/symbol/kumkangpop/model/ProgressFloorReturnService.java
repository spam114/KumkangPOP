package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgressFloorReturnService {
    private static final String BASE_URL = Users.ServiceAddress;
    private static ProgressFloorReturnService instance;
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);


    public static ProgressFloorReturnService getInstance(){
        if (instance ==null){
            instance = new ProgressFloorReturnService();
          /*  OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);*/
            /*api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(CountriesApi.class);*/
        }
        return instance;
    }

    public Single<List<Dong>> getData(SearchCondition searchCondition){// 객체의 사용
        return api.getCountries(searchCondition);
    }
}


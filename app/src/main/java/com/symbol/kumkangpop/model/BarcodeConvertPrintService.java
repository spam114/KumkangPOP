package com.symbol.kumkangpop.model;


import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.BarcodeConvertPrint;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeConvertPrintService {

    // 통신 설정 (우리 서버와 연결하는 내용)
    private static final String BASE_URL = Users.ServiceAddress;
    private static BarcodeConvertPrintService instance;//todo
    public static DataApi api = new Retrofit.Builder() // 서버와 연결
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // json 데이터를 java object로 변형해줌
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class); // 서버와 연결하여 하나의 객체 형태로 통신 객체를 생성 -> 해당 api 객체를 통해 서버의 메소드를 호출 (DataApi 라는 인터페이스에 우리가 호출하는 메소드와 서버에서 작동하는 메소드를 기록)
    // 해당 코드를 통해 api가 서버와 연결되어 통신객체로 존재하게된다. api객체만 호출하면 서버와 연결되어있으므로 서버 함수 호출

    public static BarcodeConvertPrintService getInstance() { // getInstance 호출 시 본인 객체를 반환하며 api 서버 객체를 생성한다.
        if (instance == null) {
            instance = new BarcodeConvertPrintService();
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


    // api가 인터페이스로 존재하는데 해당 인터페이스를 통해 서버와 연결하여 checkAppVersion 으로 연결되어있는 서버 함수를 호출한다.
    // POP 로그인 정보를 확인한다.
    public Single<BarcodeConvertPrint> FNBarcodeConvertPrint(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.FNBarcodeConvertPrint(searchCondition);//todo
    }

    public Single<BarcodeConvertPrint> FNSetPrintOrderData(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.FNSetPrintOrderData(searchCondition);//todo
    }

    public Single<BarcodeConvertPrint> FNSetPackingPDAData(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.FNSetPackingPDAData(searchCondition);//todo
    }

    public Single<BarcodeConvertPrint> FNSetBundleData(SearchCondition searchCondition) {// 객체의 사용//todo
        return api.FNSetBundleData(searchCondition);//todo
    }

}

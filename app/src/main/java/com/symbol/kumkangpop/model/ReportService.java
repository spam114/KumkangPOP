package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportService {
    private static final String BASE_URL = Users.ServiceAddress;
    private static ReportService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);

    public static ReportService getInstance() {
        /*if (instance == null) {
            instance = new ReportService();
        }
        return instance;*/


        if (instance == null) {
            instance = new ReportService();

            //통신에러 로그 확인을 위한 코드
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.connectTimeout(100, TimeUnit.SECONDS);//데이터 로드가 오래걸릴시, 이 코드를 넣어줘야 한다, 기본 설정값은 10초
            clientBuilder.readTimeout(100, TimeUnit.SECONDS);//데이터 로드가 오래걸릴시, 이 코드를 넣어줘야 한다, 기본 설정값은 10초
            clientBuilder.writeTimeout(100, TimeUnit.SECONDS);//데이터 로드가 오래걸릴시, 이 코드를 넣어줘야 한다, 기본 설정값은 10초


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

    /*public Single<List<Report>> Get(String apiName) {
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
    }*/

    public Single<List<Report>> Get(String apiName, SearchCondition sc) {
        if (apiName.equals("GetReport1Data")) {
            return api.GetReport1Data(sc);
        }
        else if (apiName.equals("GetReport2Data")) {
            return api.GetReport2Data(sc);
        }
        else if (apiName.equals("GetReport3Data")) {
            return api.GetReport3Data(sc);
        }
        else if (apiName.equals("GetReport4Data")) {
            return api.GetReport4Data(sc);
        }
        else if (apiName.equals("GetReport5Data")) {
            return api.GetReport5Data(sc);
        }
        else if (apiName.equals("GetReport5Data2")) {
            return api.GetReport5Data2(sc);
        }
        else if (apiName.equals("GetReport6Data")) {
            return api.GetReport6Data(sc);
        }
        else if (apiName.equals("GetReport7Data")) {
            return api.GetReport7Data(sc);
        }
        else {
            return null;
        }
    }
}

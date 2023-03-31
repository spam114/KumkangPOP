package com.symbol.kumkangpop.model;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.object.Part;
import com.symbol.kumkangpop.model.object.PartSpec;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AWaitingEditService {
    private static final String BASE_URL = Users.ServiceAddress;
    private static AWaitingEditService instance;//todo
    public static DataApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DataApi.class);

    public static AWaitingEditService getInstance() {
        if (instance == null) {
            instance = new AWaitingEditService();
        }
        return instance;
    }

    public Single<List<Part>> GetPart(SearchCondition sc) {
        return api.GetAWaitingPartForEdit(sc);
    }

    public Single<List<PartSpec>> GetPartSpec(SearchCondition sc) {
        return api.GetAWaitingPartSpecForEdit(sc);
    }

    public Single<String> UpdateShortData(SearchCondition sc) {
        return api.UpdateShortData(sc);
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

package com.symbol.kumkangpop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.kumkangpop.model.ReportService;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 이 클래스는 간단한 데이터를 전송할때 쓰인다.
 * (api 메소드 이름으로 호출한다.)
 */
public class ReportViewModel extends ViewModel {
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    // 에러메시지
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public ReportService service = ReportService.getInstance();
    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<List<Report>> dataList = new MutableLiveData<>();//데이터 리스트
    public MutableLiveData<List<Report>> dataList2 = new MutableLiveData<>();//데이터 리스트
    public MutableLiveData<Report> list = new MutableLiveData<>();//Live Data 갱신을 위해 데이터목록을 따로 관리
    public MutableLiveData<Integer> count = new MutableLiveData<>();//아이템 갯수


    /*public void Get(String apiName) {
        loading.setValue(true);
        disposable.add(service.Get(apiName)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<List<Report>>() {
                    @Override
                    public void onSuccess(@NonNull List<Report> models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        *//*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*//*
                        dataList.setValue(models);
                        loadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        errorMsg.setValue("서버 오류 발생");
                        loadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }*/

    public void Get(String apiName, SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.Get(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<List<Report>>() {
                    @Override
                    public void onSuccess(@NonNull List<Report> models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        /*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*/
                        if(apiName.equals("GetReport5Data2")){
                            dataList2.setValue(models);
                        }
                        else{
                            dataList.setValue(models);
                        }
                        loadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        errorMsg.setValue(Users.Language==0 ? "서버 오류 발생": "Server error occurred");
                        loadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }

    /*public void Delete(String apiName) {
        loading.setValue(true);
        disposable.add(service.Select(apiName)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<List<Object>>() {
                    @Override
                    public void onSuccess(@NonNull List<Object> models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        *//*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*//*
                        dataList.setValue(models);
                        loadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        errorMsg.setValue("서버 오류 발생");
                        loadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }*/
}

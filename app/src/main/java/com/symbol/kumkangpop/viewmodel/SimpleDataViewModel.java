package com.symbol.kumkangpop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.SimpleDataService;
import com.symbol.kumkangpop.model.object.CodeData;
import com.symbol.kumkangpop.model.object.Dong;

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
public class SimpleDataViewModel extends ViewModel {
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    // 에러메시지
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public SimpleDataService service = SimpleDataService.getInstance();
    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Object> data = new MutableLiveData<>();
    public MutableLiveData<List<CodeData>> codeDataList = new MutableLiveData<>();
    public MutableLiveData<List<Object>> list = new MutableLiveData<>();

    public void GetSimpleData(String apiName) {

        loading.setValue(true);
        disposable.add(service.GetSimpleData(apiName)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Object>() {
                    @Override
                    public void onSuccess(@NonNull Object models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        /*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*/
                        data.setValue(models);
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
    }



    public void GetSimpleData(String apiName, SearchCondition sc) {

        loading.setValue(true);
        disposable.add(service.GetSimpleData(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Object>() {
                    @Override
                    public void onSuccess(@NonNull Object models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        /*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*/
                        data.setValue(models);
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
    }

    public void GetSimpleDataList(String apiName) {
        loading.setValue(true);
        disposable.add(service.GetSimpleDataList(apiName)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<List<Object>>() {
                    @Override
                    public void onSuccess(@NonNull List<Object> models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        /*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*/
                        list.setValue(models);
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
    }



    public void GetCodeData(String apiName, SearchCondition sc) {

        loading.setValue(true);
        disposable.add(service.GetCodeData(apiName, sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<List<CodeData>>() {
                    @Override
                    public void onSuccess(@NonNull List<CodeData> models) {
                        // SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                        /*if (models.ErrorCheck != null) {
                            errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            return;
                        }*/
                        codeDataList.setValue(models);
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
    }
}

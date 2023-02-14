package com.symbol.kumkangpop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.kumkangpop.model.ScanService;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Scan;
import com.symbol.kumkangpop.model.object.Users;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ScanViewModel extends ViewModel {
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();//false면 성공, true면 에러
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public ScanService service = ScanService.getInstance();//todo

    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<Scan> data = new MutableLiveData<>();//todo

    /*public void GetTest() {
        loading.setValue(true);
        disposable.add(service.GetTest()
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Scan>() {
                    @Override
                    public void onSuccess(@NonNull Scan models) {
                        data.setValue(models);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }*/

    public void GetScanMain(SearchCondition sc) {
        loading.setValue(true);
        disposable.add(service.GetScanMain(sc)
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<Scan>() {
                    @Override
                    public void onSuccess(@NonNull Scan models) {
                        if (models.ErrorCheck != null) {
                            //errorMsg.setValue(models.ErrorCheck);
                            loadError.setValue(true);
                            loading.setValue(false);
                            Users.SoundManager.playSound(0, 2, 3);//에러
                            return;
                        }
                        data.setValue(models);
                        loadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                        Users.SoundManager.playSound(0, 2, 3);//에러
                        e.printStackTrace();
                    }
                })
        );
    }
}

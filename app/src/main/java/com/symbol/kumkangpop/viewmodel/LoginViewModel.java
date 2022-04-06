package com.symbol.kumkangpop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.kumkangpop.model.LoginService;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.LoginInfo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public LoginService service = LoginService.getInstance();//todo
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();

    // 로그인 확인 후 에러메시지를 출력하기 위한 변수로 사용
    public MutableLiveData<List<LoginInfo>> loginInfoList = new MutableLiveData<>();//todo

    // 로그인 정보 호출
    public void GetLoginInfoData(SearchCondition sc) {

        loading.setValue(true);

        disposable.add(
                service.GetLoginInfoData(sc) // POP 로그인 정보를 확인한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<List<LoginInfo>>() {//todo

                            @Override
                            public void onSuccess(@NonNull List<LoginInfo> models) {//todo
                                loginInfoList.setValue(models);
                                loadError.setValue(false);
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
    }
}

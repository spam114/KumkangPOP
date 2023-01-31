package com.symbol.kumkangpop.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.symbol.kumkangpop.model.LoginService;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Authority;
import com.symbol.kumkangpop.model.object.LoginInfo;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.PreferenceManager;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    // 데이터를 가져오는 것에 성공했는지를 알려주는 데이터(앱버전)
    public MutableLiveData<Boolean> loadError = new MutableLiveData<>();//false면 성공, true면 에러
    // 로딩 중인지를 나타내는 데이터
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    //PhoneNumber로 성공했는지 확인: true성공, false실패
    public MutableLiveData<Boolean> phoneNumberFlag = new MutableLiveData<>();
    // 에러메시지
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    // 프린트 에러메시지
    public MutableLiveData<String> printErrorMsg = new MutableLiveData<>();
    // 서버 객체 호출 : api를 통해 서버와 연결된다.
    public LoginService service = LoginService.getInstance();//todo

    // Single 객체를 가로채기 위함
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<LoginInfo> loginInfo = new MutableLiveData<>();//todo
    public MutableLiveData<String> userImage = new MutableLiveData<>();

    // 로그인 정보 호출
    public void GetLoginInfoData(SearchCondition sc) {
        loading.setValue(true);
        disposable.add(
                service.GetLoginInfoData(sc) // POP 로그인 정보를 확인한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<LoginInfo>() {//todo

                            @Override
                            public void onSuccess(@NonNull LoginInfo models) {//todo
                                if (models.ErrorCheck != null) {
                                    errorMsg.setValue(models.ErrorCheck);
                                    loadError.setValue(true);
                                    loading.setValue(false);
                                    return;
                                }
                                Users.UserID = sc.UserID;
                                Users.UserName = models.UserName;
                                Users.DeptCode = models.DeptCode;
                                Users.CustomerCode = models.CustomerCode;
                                Users.BusinessClassCode = models.BusinessClassCode;
                                Users.GboutSourcing = models.GboutSourcing;
                                Users.Language = sc.Language;
                                Users.AuthorityList = models.AuthorityList;
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

    public void GetUserImage() {
        loading.setValue(true);
        disposable.add(service.GetUserImage()
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String models) {
                        userImage.setValue(models);
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

    // 로그인 정보 호출
    public void GetLoginInfoByPhoneNumber(SearchCondition sc) {
        loading.setValue(true);
        disposable.add(
                service.GetLoginInfoByPhoneNumber(sc) // POP 로그인 정보를 확인한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<LoginInfo>() {//todo

                            @Override
                            public void onSuccess(@NonNull LoginInfo models) {//todo
                                if (models.ErrorCheck != null) {
                                    errorMsg.setValue(models.ErrorCheck);
                                    loadError.setValue(true);
                                    phoneNumberFlag.setValue(false);
                                    loading.setValue(false);
                                    return;
                                }
                                Users.UserID = sc.UserID;
                                Users.UserName = models.UserName;
                                Users.DeptCode = models.DeptCode;
                                Users.CustomerCode = models.CustomerCode;
                                Users.BusinessClassCode = models.BusinessClassCode;
                                Users.GboutSourcing = models.GboutSourcing;
                                Users.Language = sc.Language;
                                Users.AuthorityList = models.AuthorityList;
                                phoneNumberFlag.setValue(true);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                phoneNumberFlag.setValue(false);
                                errorMsg.setValue(Users.Language==0 ? "서버 오류 발생": "Server error occurred");
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }


    public void GetPrintPCData(SearchCondition sc, Context context) {
        loading.setValue(true);
        disposable.add(
                service.GetPrintPCData(sc) // POP 로그인 정보를 확인한다.
                        .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                        .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                        .subscribeWith(new DisposableSingleObserver<LoginInfo>() {//todo

                            @Override
                            public void onSuccess(@NonNull LoginInfo models) {//todo
                                loading.setValue(false);
                                GetUserImage();
                                if (models.ErrorCheck != null) {
                                    //errorMsg.setValue(models.ErrorCheck);
                                    printErrorMsg.setValue(models.ErrorCheck);
                                    //loadError.setValue(true);
                                    //loading.setValue(false);
                                    return;
                                }
                                //loadError.setValue(false);
                                Users.PCCode = models.PCCode;
                                Users.PCName = models.PCName;
                                PreferenceManager.setString(context, "PCCode", models.PCCode);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                errorMsg.setValue(Users.Language==0 ? "서버 오류 발생": "Server error occurred");
                                //loadError.setValue(true);
                                //loading.setValue(false);
                                loading.setValue(false);
                                e.printStackTrace();
                                GetUserImage();
                            }
                        })
        );
    }


}

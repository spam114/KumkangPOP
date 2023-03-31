package com.symbol.kumkangpop.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityLoginBinding;
import com.symbol.kumkangpop.model.LoginService;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.BackPressControl;
import com.symbol.kumkangpop.view.PreferenceManager;
import com.symbol.kumkangpop.view.application.ApplicationClass;
import com.symbol.kumkangpop.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    BackPressControl backpressed;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setServiceAddress();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.tlID.setErrorEnabled(true);
        binding.tlPW.setErrorEnabled(true);
        backpressed = new BackPressControl(this);
        setView();
        initEvent();
        observerViewModel();
        if (getIntent().getBooleanExtra("FirstFlag", true)) {
            StartLogin();
        }
    }

    private void setView() {
        int langInt = PreferenceManager.getInt(this, "Language");
        if (langInt == 0) {
            binding.rbKor.setChecked(true);
        } else {
            binding.rbEng.setChecked(true);
        }
    }

    private void setServiceAddress(){
        if(PreferenceManager.getInt(this, "ServiceType")==0){//금강
            Users.ServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address);
        }
        else if(PreferenceManager.getInt(this, "ServiceType")==1){//KKM
            Users.ServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address_kkm);
        }
        else if(PreferenceManager.getInt(this, "ServiceType")==2){//KKV
            Users.ServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address_kkv);
        }
        else{//TEST
            Users.ServiceAddress = ApplicationClass.getResourses().getString(R.string.service_address_test);
        }
    }

    //로그인을 성공하면 무조건 이 메소드를 쓴다.
    private void GetPrintPCData() {
        SearchCondition sc = new SearchCondition();

        sc.PCCode = PreferenceManager.getString(this, "PCCode");
        if (sc.PCCode.equals(""))//저장되어 있는 PCCode가 없다면,
            sc.PCCode = binding.edtPC.getText().toString();
        loginViewModel.GetPrintPCData(sc, this);
        sc.Language = PreferenceManager.getInt(this, "Language");
        if (binding.checkAuto.isChecked()) {
            PreferenceManager.setBoolean(this, "AutoLogin", true);
            PreferenceManager.setString(this, "ID", binding.edtID.getText().toString());
            PreferenceManager.setString(this, "PW", binding.edtPW.getText().toString());
        }
    }


    private void initEvent() {
        // 키보드 엔터 키 이벤트 생성
        binding.edtID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    binding.edtPW.requestFocus();
                }
                return false;
            }
        });
        binding.edtPW.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    if (binding.rbKor.isChecked())
                        PreferenceManager.setInt(LoginActivity.this, "Language", 0);
                    else
                        PreferenceManager.setInt(LoginActivity.this, "Language", 1);
                    binding.btnLogin.performClick();
                }
                return false;*/
                /*if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    HideKeyBoard();
                }*/
                return false;

            }
        });

        // 포커스 이벤트
        binding.edtID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!binding.edtID.getText().toString().isEmpty()) {
                        binding.tlID.setErrorEnabled(false);
                        binding.tlID.setError(null);
                    }
                }
            }
        });

        binding.edtPW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!binding.edtPW.getText().toString().isEmpty()) {
                        binding.tlPW.setErrorEnabled(false);
                        binding.tlPW.setError(null);
                    }
                }
            }
        });

        binding.btnConnectionString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                final View dialogView = inflater.inflate(R.layout.dialog_service, null);
                AlertDialog.Builder buider = new AlertDialog.Builder(LoginActivity.this); //AlertDialog.Builder 객체 생성
                //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                //Dialog 보이기
                dialog.show();
                TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                tvTitle.setText("지역 변경 (Change Region)");
                RadioButton rbKor = dialogView.findViewById(R.id.rbKor);
                RadioButton rbKKM = dialogView.findViewById(R.id.rbKKM);
                RadioButton rbKKV = dialogView.findViewById(R.id.rbKKV);
                RadioButton rbTEST = dialogView.findViewById(R.id.rbTEST);
                Button btnOK = dialogView.findViewById(R.id.btnOK);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);

                if(PreferenceManager.getInt(LoginActivity.this, "ServiceType")==0){//금강
                    rbKor.setChecked(true);
                }
                else if(PreferenceManager.getInt(LoginActivity.this, "ServiceType")==1){//KKM
                    rbKKM.setChecked(true);
                }
                else if(PreferenceManager.getInt(LoginActivity.this, "ServiceType")==2){//KKV
                    rbKKV.setChecked(true);
                }
                else{//TEST
                    rbTEST.setChecked(true);
                }

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rbKor.isChecked()) {
                            PreferenceManager.setInt(LoginActivity.this, "ServiceType", 0);
                            PreferenceManager.setInt(LoginActivity.this, "Language", 0);
                            //Users.ServiceType = 0;
                        }
                        else if(rbKKM.isChecked()){
                            PreferenceManager.setInt(LoginActivity.this, "ServiceType", 1);
                            PreferenceManager.setInt(LoginActivity.this, "Language", 1);
                            //Users.ServiceType = 1;
                        }
                        else if(rbKKV.isChecked()){
                            PreferenceManager.setInt(LoginActivity.this, "ServiceType", 2);
                            PreferenceManager.setInt(LoginActivity.this, "Language", 1);
                            //Users.ServiceType = 2;
                        }
                        else{//TEST
                            PreferenceManager.setInt(LoginActivity.this, "ServiceType", 3);
                            PreferenceManager.setInt(LoginActivity.this, "Language", 0);
                            //Users.ServiceType = 3;
                        }
                        dialog.dismiss();

                        PreferenceManager.setBoolean(LoginActivity.this, "AutoLogin", false);
                        PreferenceManager.setString(LoginActivity.this, "ID", "");
                        PreferenceManager.setString(LoginActivity.this, "PW", "");
                        PreferenceManager.setString(LoginActivity.this, "PCCode", "");
                        restart(LoginActivity.this);//여기문제
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }


    /**
     * 로그인 버튼 클릭
     *
     * @param v
     */
    public void btnLoginClick(View v) {
        if (binding.rbKor.isChecked())
            PreferenceManager.setInt(this, "Language", 0);
        else
            PreferenceManager.setInt(this, "Language", 1);
        StartLogin();
    }

    private void StartLogin() {
        SearchCondition sc = new SearchCondition();
        boolean autoLoginFlag = PreferenceManager.getBoolean(this, "AutoLogin");
        if (autoLoginFlag) {
            sc.UserID = PreferenceManager.getString(this, "ID");
            sc.PassWord = PreferenceManager.getString(this, "PW");
            sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
            sc.Language = PreferenceManager.getInt(this, "Language");
        } else {
            if (getIntent().getBooleanExtra("FirstFlag", true)) {
                GetLoginInfoByPhoneNumber();
                return;
            } else {
                sc.UserID = CheckInputLoginUserID(); // 아이디 공백 확인
                sc.PassWord = CheckInputLoginPassword(); // 패스워드 공백 확인
                sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
                if (binding.rbKor.isChecked())
                    sc.Language = 0;
                else
                    sc.Language = 1;
                if (sc.UserID.equals("") || sc.PassWord.equals("")) return;
            }
        }
        loginViewModel.GetLoginInfoData(sc);
    }

    private void GetLoginInfoByPhoneNumber() {
        SearchCondition sc = new SearchCondition();
        sc.UserID = Users.PhoneNumber;
        sc.Language = PreferenceManager.getInt(this, "Language");
        sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
        loginViewModel.GetLoginInfoByPhoneNumber(sc);
    }

    // 로그인 시 ID와 Password가 공백인지 확인한다.
    private String CheckInputLoginUserID() {
        String userID = binding.edtID.getText().toString();
        if (userID.isEmpty()) {
            binding.tlID.setErrorEnabled(true);
            //binding.tlID.setError("아이디를 입력해주세요.");
            binding.tlID.setError(" ");
        } else {
            binding.tlID.setError(null);
            binding.tlID.setErrorEnabled(false);
        }
        return userID;
    }

    private String CheckInputLoginPassword() {
        String passWord = binding.edtPW.getText().toString();
        if (passWord.isEmpty()) {
            binding.tlPW.setErrorEnabled(true);
            //binding.tlPW.setError("패스워드를 입력해주세요.");
            binding.tlPW.setError(" ");
        } else {
            binding.tlPW.setError(null);
            binding.tlPW.setErrorEnabled(false);
        }
        return passWord;
    }

    public void observerViewModel() {
        loginViewModel.userImage.observe(this, models -> {
            if (models != null) {
                Users.UserImage = models;
            } else
                Users.UserImage = "fail";
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        loginViewModel.phoneNumberFlag.observe(this, phoneNumberFlag -> {
            if (phoneNumberFlag != null) {
                if (phoneNumberFlag) {//성공
                    GetPrintPCData();
                } else {
                    SearchCondition sc = new SearchCondition();
                    sc.UserID = CheckInputLoginUserID(); // 아이디 공백 확인
                    sc.PassWord = CheckInputLoginPassword(); // 패스워드 공백 확인
                    sc.AppCode = ApplicationClass.getResourses().getString(R.string.app_code);
                    if (sc.UserID.equals("") || sc.PassWord.equals("")) return;

                    if (binding.rbKor.isChecked())
                        sc.Language = 0;
                    else
                        sc.Language = 1;
                    loginViewModel.GetLoginInfoData(sc);
                }
                /*else{//PhoneNumber 가 없으니 자동로그인 or 직접로그인
                    LoginByAutoFlag();
                }*/
            }
        });

        //성공, 실패 여부
        loginViewModel.loadError.observe(this, models -> {
            if (models != null) {
                if (!models) {//성공
                    GetPrintPCData();
                }
            }
        });

        //에러메시지
        loginViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                if(!models.equals(""))
                    Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                PreferenceManager.setBoolean(this, "AutoLogin", false);
                PreferenceManager.setString(this, "ID", "");
                PreferenceManager.setString(this, "PW", "");
                PreferenceManager.setString(this, "PCCode", "");
                //PreferenceManager.setInt(this, "Language", 0);
                progressOFF2();
            }
        });

        loginViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }

    private void startProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF2();
            }
        }, 5000);
        progressON("Loading...", handler);
    }

    @Override
    public void onBackPressed() {
        backpressed.onBackPressed();
    }
}

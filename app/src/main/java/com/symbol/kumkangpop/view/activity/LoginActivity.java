package com.symbol.kumkangpop.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityLoginBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.view.BackPressControl;
import com.symbol.kumkangpop.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    BackPressControl backpressed;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.tlID.setErrorEnabled(true);
        binding.tlPW.setErrorEnabled(true);
        backpressed = new BackPressControl(this);
        initEvent();
        observerViewModel();
    }

    private void initEvent(){
        // 키보드 엔터 키 이벤트 생성
        binding.edtID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){ // IME_ACTION_SEARCH , IME_ACTION_GO
                    binding.edtPW.requestFocus();
                }
                return false;
            }
        });
        binding.edtPW.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){ // IME_ACTION_SEARCH , IME_ACTION_GO
                    binding.btnLogin.performClick();
                }
                return false;
            }
        });

        // 포커스 이벤트
        binding.edtID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!binding.edtID.getText().toString().isEmpty()){
                        binding.tlID.setErrorEnabled(false);
                        binding.tlID.setError(null);
                    }
                }
            }
        });

        binding.edtPW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!binding.edtPW.getText().toString().isEmpty()){
                        binding.tlPW.setErrorEnabled(false);
                        binding.tlPW.setError(null);
                    }
                }
            }
        });
    }

    /**
     * 로그인 버튼 클릭
     * @param v
     */
    public void btnLoginClick(View v) {
        SearchCondition sc= new SearchCondition();
        sc.UserID = CheckInputLoginUserID(); // 아이디 공백 확인
        sc.PassWord = CheckInputLoginPassword(); // 패스워드 공백 확인
        if(sc.UserID.equals("")||sc.PassWord.equals(""))return;
        // 자동로그인 체크 여부
        //users.AutoLogin = loginBinding.checkAuto.isChecked();
        loginViewModel.GetLoginInfoData(sc);
        // 자동로그인 체크 여부
        //users.AutoLogin = loginBinding.checkAuto.isChecked();

        // ViewModel 호출
        // 로그인 실시
        //loginViewModel.loginInfo();
        //observerViewModel();
    }

    // 로그인 시 ID와 Password가 공백인지 확인한다.
    private String CheckInputLoginUserID(){
        String userID = binding.edtID.getText().toString();
        if(userID.isEmpty()) {
            binding.tlID.setErrorEnabled(true);
            //binding.tlID.setError("아이디를 입력해주세요.");
            binding.tlID.setError(" ");
        }
        else {
            binding.tlID.setError(null);
            binding.tlID.setErrorEnabled(false);
        }
        return userID;
    }

    private String CheckInputLoginPassword(){
        String passWord = binding.edtPW.getText().toString();
        if(passWord.isEmpty()){
            binding.tlPW.setErrorEnabled(true);
            //binding.tlPW.setError("패스워드를 입력해주세요.");
            binding.tlPW.setError(" ");
        }
        else {
            binding.tlPW.setError(null);
            binding.tlPW.setErrorEnabled(false);
        }
        return passWord;
    }

    public void observerViewModel(){
        loginViewModel.loginInfoList.observe(this, models-> {
            if(models != null){
                String errorCheck = models.get(0).ErrorCheck;
                if(errorCheck != null) { // 에러 발생 (1.유저정보x, 2.비밀번호 불일치, 3.server Error)
                    Toast.makeText(this, errorCheck, Toast.LENGTH_LONG).show();
                    finish();
                }
                else { // POP 계정 존재
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
            else{
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        backpressed.onBackPressed();
    }
}

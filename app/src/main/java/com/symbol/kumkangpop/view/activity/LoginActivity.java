package com.symbol.kumkangpop.view.activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityLoginBinding;
import com.symbol.kumkangpop.view.BackPressControl;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    BackPressControl backpressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        backpressed = new BackPressControl(this);
    }
    /**
     * 버튼 클릭
     */
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        backpressed.onBackPressed();
    }
}

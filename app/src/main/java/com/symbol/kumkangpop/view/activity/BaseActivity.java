package com.symbol.kumkangpop.view.activity;


import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.symbol.kumkangpop.view.application.ApplicationClass;


public class BaseActivity extends AppCompatActivity {


    public void progressON() {
        ApplicationClass.getInstance().progressON(this, null);
    }

    public void progressON(String message) {
        ApplicationClass.getInstance().progressON(this, message);
    }

    public void progressON(String message, Handler handler) {
        ApplicationClass.getInstance().progressON(this, message, handler);
    }

    public void progressOFF2(String className) {
        ApplicationClass.getInstance().progressOFF2(className);
    }

    public void progressOFF() {
        ApplicationClass.getInstance().progressOFF();
    }

}
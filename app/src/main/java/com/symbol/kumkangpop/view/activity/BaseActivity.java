package com.symbol.kumkangpop.view.activity;


import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.symbol.kumkangpop.model.object.MainMenuItem;
import com.symbol.kumkangpop.view.application.ApplicationClass;

import java.util.ArrayList;


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

    public void progressOFF2() {
        ApplicationClass.getInstance().progressOFF2();
    }

    public void getKeyInResult(String result){}

    public void showFloatingNavigationView(){}

    public ArrayList<MainMenuItem> getMainMenuItem(){return ApplicationClass.getInstance().getMainMenuItem();}
}
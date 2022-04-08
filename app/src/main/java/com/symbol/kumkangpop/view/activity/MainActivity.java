package com.symbol.kumkangpop.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.internal.LinkedTreeMap;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityMainBinding;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.CVariable;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SimpleDataViewModel simpleDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        simpleDataViewModel.GetSimpleData("GetNoticeData2");
        observerViewModel();
        //GoProgressFloorReturnActivity();
    }

    public void observerViewModel() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) data;
                Object errorCheck = linkedTreeMap.get("ErrorCheck");
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Object remark = linkedTreeMap.get("Remark");

                    //viewNotice();
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
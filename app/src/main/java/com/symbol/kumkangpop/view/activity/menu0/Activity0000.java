package com.symbol.kumkangpop.view.activity.menu0;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0000Binding;
import com.symbol.kumkangpop.view.activity.BaseActivity;

/**
 * 제품포장
 */
public class Activity0000 extends BaseActivity {
    Activity0000Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity0000);
        //simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        //simpleDataViewModel.GetSimpleData("GetNoticeData2");

        //setListener();
        //observerViewModel();
        //GoProgressFloorReturnActivity();
    }
}

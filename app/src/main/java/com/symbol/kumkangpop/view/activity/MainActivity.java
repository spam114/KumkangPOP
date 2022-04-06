package com.symbol.kumkangpop.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //GoProgressFloorReturnActivity();
    }




    private void GoProgressFloorReturnActivity() {
        Intent i = new Intent(this, ProgressFloorReturnActivity.class);
        i.putExtra("customerLocation", "광평건설(주)" + "-" + "아산 탕정 2A-15BL");
        i.putExtra("contractNo", "C-9-37412-01");
        //i.putExtra("dongHashMap", dongHashMap);
        startActivity(i);
    }
}
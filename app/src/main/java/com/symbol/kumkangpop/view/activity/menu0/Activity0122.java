package com.symbol.kumkangpop.view.activity.menu0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0122Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.MC3300X;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter0122;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

/**
 * 제품포장
 */
public class Activity0122 extends BaseActivity {
    Activity0122Binding binding;
    Adapter0122 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;
    MC3300X mc3300X;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0122);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0122) : getString(R.string.detail_menu_0122_eng));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter0122(new ArrayList<>(), this, resultLauncher, commonViewModel, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textViewWorkDate4.setText("PackingNo");
            binding.textViewWorkDate.setText("Block");
            binding.textViewWorkType.setText("Zone");
            binding.textViewWorkType4.setText("NO");
            binding.textInputLayout.setHint("PackingNo");
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SaleOrderNo = saleOrderNo;
        sc.LocationNo = Users.LocationNo;
        sc.PackingNo = "";
        commonViewModel.Get("GetPackingData", sc);
    }

    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;

                SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 2;
                sc.Barcode = barcode.Barcode;
                //recyclerViewModel.cData = sc.Barcode;
                commonViewModel.Get2("GetNumConvertData", sc);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        barcodeConvertPrintViewModel.data2.observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, result.Result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        //에러메시지
        barcodeConvertPrintViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        barcodeConvertPrintViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(data.PackingList);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size() == 0) {
                    Toast.makeText(this, Users.Language == 0 ? "해당 TAG의 포장정보를 찾을 수 없습니다." : "TAG's packaging information that could not be found.", Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    return;
                } else {
                    CommonMethod.FNSetPrintOrderData(this, barcodeConvertPrintViewModel, 1, data.NumConvertDataList.get(0).DestNum);
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {


            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });


        //에러메시지
        commonViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        commonViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }

    private void setListener() {
        /**
         * forfilter
         */
        binding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 공통 시작
     */
    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return CommonMethod.onCreateOptionsMenu2(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 2);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //result.getResultCode()를 통하여 결과값 확인
                if (result.getResultCode() == RESULT_OK) {
                    //ToDo
                }
                if (result.getResultCode() == RESULT_CANCELED) {
                    //ToDo
                }
            }
    );

    /**
     * 스캔 인식
     */
    private void setResultLauncher() {
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);
    }

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
                //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
                intentIntegrator.setPrompt(this.getString(R.string.qr_state_common));
                intentIntegrator.setOrientationLocked(true);
                // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
                //intentIntegrator.initiateScan();
                intentIntegrator.setRequestCode(7);
                resultLauncher.launch(intentIntegrator.createScanIntent());
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SetMC3300X() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        this.mc3300X = new MC3300X(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        mc3300X.registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = "";
            if(intent.getAction().equals("mycustombroadcast")){
                result = bundle.getString("mcx3300result");
            }
            else if(intent.getAction().equals("scan.rcv.message")){
                result = bundle.getString("barcodeData");
            }
            if (result.equals(""))
                return;
            getKeyInResult(result);
        }
    };

    /**
     * 공통 끝
     */
}

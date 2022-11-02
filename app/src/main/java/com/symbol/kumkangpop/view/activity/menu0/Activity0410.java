package com.symbol.kumkangpop.view.activity.menu0;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0410Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ScanListViewItem;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter0410;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity0410 extends BaseActivity {
    Activity0410Binding binding;
    Adapter0410 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;
    String ho;
    String packingNo;
    List<ScanListViewItem> scanListViewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0410);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(getString(R.string.detail_menu_0410));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        ho = getIntent().getStringExtra("ho");
        packingNo = getIntent().getStringExtra("packingNo");
        binding.tvPackingNo.setText(packingNo);
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter0410(new ArrayList<>(), this, resultLauncher);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.PackingNo = packingNo;
        commonViewModel.Get("GetPackingDetailDataSum", sc);
    }

    public void observerViewModel() {
        //바코드 스캔 후 동작
        /*barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;
                *//*SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 2;
                sc.Barcode = barcode.Barcode;
                //recyclerViewModel.cData = sc.Barcode;
                commonViewModel.Get2("GetNumSeekData", sc);*//*



            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });*/

        /*commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size() == 0) {
                    *//*Toast.makeText(this, "해당 TAG의 포장정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    return;*//*
                }
                else{
                    //CommonMethod.FNSetPrintOrderData(this, barcodeConvertPrintViewModel, 1, data.NumConvertDataList.get(0).DestNum);
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });*/

        /*barcodeConvertPrintViewModel.data2.observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, result.Result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });*/

        /*//에러메시지
        barcodeConvertPrintViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
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
        });*/

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                int num1 = 0, num2 = 0;
                for(int i=0;i<data.PackingList.size();i++){
                    num1 += data.PackingList.get(i).StockOutOrderQty;
                    num2 += data.PackingList.get(i).StockOutQty;
                }
                DecimalFormat numFormatter = new DecimalFormat("###,###");
                binding.tvStockOutOrderQty.setText(numFormatter.format(num1));
                binding.tvStockOutQty.setText(numFormatter.format(num2));
                adapter.updateAdapter(data.PackingList);
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /*
        commonViewModel.data3.observe(this, data -> {
            if (data != null) {



            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });*/


        //에러메시지
        commonViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
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
        //제품추가
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0411.class);
                intent.putExtra("packingNo", packingNo);
                intent.putExtra("saleOrderNo", saleOrderNo);
                intent.putExtra("ho", ho);
                activityResultLauncher.launch(intent);
            }
        });
        //제품제외
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0412.class);
                intent.putExtra("packingNo", packingNo);
                activityResultLauncher.launch(intent);
            }
        });
        //포장출력
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //번들출력
        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                GetMainData();
                //result.getResultCode() 기본값은 0
                /*if (result.getResultCode() == RESULT_OK) {//-1
                }
                if (result.getResultCode() == RESULT_CANCELED) {//0
                }*/
            }
    );

    /**
     * 스캔 인식 (이 액티비티는 별도로 작업한다.)
     */
    private void setResultLauncher() {
        resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        /**
                         * QR코드 시작
                         */
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            //Scanning(scanResult);
                            return;
                        }
                        /**
                         * QR코드 끝
                         */
                        if (result.getResultCode() == 100) {

                        }
                    }
                });
    }


    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        //Scanning(result);
    }

    /**
     * 공통 끝
     */
}

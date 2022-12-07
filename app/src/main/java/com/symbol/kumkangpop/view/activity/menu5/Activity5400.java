package com.symbol.kumkangpop.view.activity.menu5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity5400Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter5400;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class Activity5400 extends BaseActivity {
    Activity5400Binding binding;
    Adapter5400 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    int locationNo;
    int stockOutType = 0;
    //String saleOrderNo;
    //String ho;
    //ArrayList<ItemTag> tempItemTagArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity5400);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_5400):getString(R.string.detail_menu_5400_eng));
        locationNo = getIntent().getIntExtra("locationNo", -1);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter5400(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView5.setText("Invty-OutNo");
            binding.textView1.setText("Customer");
            binding.textView2.setText("Jobsite");
            binding.textInputLayout.setHint("Invty-OutNo");
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.StockOutNo = "";
        commonViewModel.Get("GetStockOutDataExists", sc);
    }


    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                //txtContainerNo.Text = barcode;
                //DataSet nDataSet = npopControl.GetNumConvertData(iConvetDivision, barcode);

                SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 5;//출고번호 5
                sc.Barcode = barcode.Barcode;
                commonViewModel.Get2("GetNumConvertData", sc);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });


        //에러메시지
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
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                /*DecimalFormat numFormatter = new DecimalFormat("###,###");
                binding.tvStockOutOrderQty.setText(numFormatter.format(num1));
                binding.tvStockOutQty.setText(numFormatter.format(num2));*/
                adapter.updateAdapter(data.StockOutDetailList);
                adapter.getFilter().filter(binding.edtInput.getText().toString());
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size() == 0){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "해당 바코드의 출고정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "That the factory could not find any information of a barcode.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                String stockOutNo = data.NumConvertDataList.get(0).DestNum;


                for (int i = 0; i < adapter.getItemList().size(); i++)
                {
                    if (stockOutNo.equals(adapter.getItemList().get(i).StockOutNo))
                    {
                        Intent intent = new Intent(getBaseContext(), Activity5410.class);
                        intent.putExtra("stockOutNo", stockOutNo);
                        intent.putExtra("locationNo", locationNo);
                        activityResultLauncher.launch(intent);

                        /*RMwrt5410P nForm = new RMwrt5410P(txtStockOutNo.Text, gLocationNo);
                        nForm.ShowDialog();
                        // 프로그램 시작시 바코드 모듈 ON
                        MBBarcode.Power(true);

                        // 바코드 모듈 제어권 시작
                        MBBarcode.ActiveForm = this;

                        DataTable nDataTable = npopControl.GetStockOutDataExists(gLocationNo, StockOutType, "").Tables[0];
                        mclFunction.fnSetListView(listView1, nDataTable);
                        return;*/
                    }
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });

        /*commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                if (data.ContainerList.size()==0)
                {
                    ////nScaner.fnBeepError();
                    if (Users.Language == 0) {
                        Toast.makeText(this, "해당 바코드의 컨테이너정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Container of the corresponding bar code information can not be found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getBaseContext(), Activity5110.class);
                    intent.putExtra("containerNo", data.ContainerList.get(0).ContainerNo);
                    intent.putExtra("locationNo", locationNo);
                    activityResultLauncher.launch(intent);

                }

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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
        //이것은 서버TAG인식 로직
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);

        //이것은 인식한 TAG 그대로
        /*resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        *//**
         * QR코드 시작
         *//*
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            Scanning(scanResult);
                            return;
                        }
                        *//**
         * QR코드 끝
         *//*
                        if (result.getResultCode() == 100) {

                        }
                    }
                });*/
    }


    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
    }

    /**
     * 공통 끝
     */
}

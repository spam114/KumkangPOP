package com.symbol.kumkangpop.view.activity.menu5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity5310Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.StockOut;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.dialog.Dialog5320;
import com.symbol.kumkangpop.view.dialog.Dialog5330;
import com.symbol.kumkangpop.view.dialog.Dialog5340;
import com.symbol.kumkangpop.view.dialog.Dialog5350;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

/**
 * 제품포장
 */
public class Activity5310 extends BaseActivity {
    Activity5310Binding binding;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String stockOutNo;
    int locationNo;
    int stockOutType=0;
    //File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity5310);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_5310):getString(R.string.detail_menu_5310_eng));
        stockOutNo = getIntent().getStringExtra("stockOutNo");
        locationNo = getIntent().getIntExtra("locationNo", -1);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        GetMainData(Users.LocationNo, stockOutNo);
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView1.setText("ContainerNo");

            binding.textView2.setText("OutDept");
            binding.tvBlank2.setText("AAA");
            binding.textView3.setText("CarryCom");
            binding.tvBlank3.setText("AA");
            binding.textView4.setText("R-Weight");
            binding.tvBlank4.setText("AAA");
            binding.textView5.setText("CarryExp");
            binding.tvBlank5.setText("AAA");
            binding.textView6.setText("Area");
            binding.tvBlank6.setText("AAAAAA");
            binding.textView7.setText("CarTon");
            binding.tvBlank7.setText("A AAA");
            binding.textView8.setText("CarryAmt");
            binding.tvBlank8.setText("AAA");
            binding.textView9.setText("CarNo");
            binding.tvBlank9.setText("AAAAA");
            binding.textView10.setText("Remark1");
            binding.tvBlank10.setText("AAA ");
            binding.textView11.setText("Remark2");
            binding.tvBlank11.setText("AAA ");

            binding.radioButton.setText("Arrival");
            binding.radioButton2.setText("Carry");

            binding.btnSave.setText("Save");
        }
    }

    private void GetMainData(int locationNo, String stockOutNo) {

        SearchCondition sc = new SearchCondition();
        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.StockOutNo = stockOutNo;
        sc.StockInFlag = -1;
        commonViewModel.Get("GetStockOutData", sc);

    }

    public void observerViewModel() {
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });


        commonViewModel.data.observe(this, data -> {
            if (data != null) {

                ArrayList<StockOut> tempArrayList = data.StockOutList;
                if (tempArrayList.size() != 0) {
                    binding.edt1.setText(tempArrayList.get(0).ContainerNo);
                    binding.edt2.setText(tempArrayList.get(0).DeptCode);

                    binding.edt3.setText(tempArrayList.get(0).TransportCust);
                    binding.edt4.setText(Double.toString(tempArrayList.get(0).ActualWeight));

                    if ((int) tempArrayList.get(0).TransportDivision == 1) {
                        binding.radioButton.setChecked(true);
                    } else {
                        binding.radioButton2.setChecked(true);
                    }
                    binding.edt6.setText(tempArrayList.get(0).AreaCode);
                    binding.edt7.setText(Double.toString(tempArrayList.get(0).CarTon));
                    binding.edt8.setText(Integer.toString((int)tempArrayList.get(0).TransportAmt));
                    binding.edt9.setText(tempArrayList.get(0).CarNumber);
                    binding.edt10.setText(tempArrayList.get(0).Remark1);
                    binding.edt11.setText(tempArrayList.get(0).Remark2);
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if(data.BoolResult){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Normal has been registered.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                }

            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        /*commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                boolean bbool = data.BoolResult;
                if (bbool) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Normal has been registered.", Toast.LENGTH_SHORT).show();
                    }

                    GetMainData(Users.LocationNo, binding.edtContainerNo.getText().toString());
                    *//*if (nDataSet.Tables[0].Rows.Count != 0)
                    {
                        txtContainerNo.Text = nDataSet.Tables[0].Rows[0]["ContainerNo"].ToString();
                        txtContainerNo2.Text = nDataSet.Tables[0].Rows[0]["ContainerNo"].ToString();
                        txtOutDate.Text = nDataSet.Tables[0].Rows[0]["OutDate"].ToString();
                        txtSilNo.Text = nDataSet.Tables[0].Rows[0]["SilNo"].ToString();
                        if (decimal.Parse(nDataSet.Tables[0].Rows[0]["ContainerTon"].ToString()) == 40)
                        {
                            rdoCheck1.Checked = true;
                        }
                        else
                        {
                            rdoCheck2.Checked = true;
                        }
                        txtCarNumber.Text = nDataSet.Tables[0].Rows[0]["CarNumber"].ToString();
                    }*//*
                } else {
                    ////nScaner.fnBeepError();
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

        /*commonViewModel.data4.observe(this, data -> {
            if (data != null) {
                if (data.BoolResult) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photos have been saved.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

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

    private void saveStockOut() {
    }

    private void setListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                sc.StockOutNo = stockOutNo;

                sc.ContainerNo = binding.edt1.getText().toString();//컨테이너번호
                sc.DeptCode = binding.edt2.getText().toString();//출고부서
                sc.CustomerCode = binding.edt3.getText().toString();//운송사

                if(binding.edt4.getText().toString().substring(binding.edt4.getText().toString().length()-1).equals(".")){
                    binding.edt4.setText(binding.edt4.getText().toString().replace(".",""));
                }
                if(binding.edt4.getText().toString().equals("")){
                    sc.ActualWeight = 0;//실중량
                }
                else{
                    sc.ActualWeight = Double.parseDouble(binding.edt4.getText().toString());//실중량
                }


                if(binding.radioButton.isChecked()){//운반비구분
                    sc.TransportDivision = 1;
                }
                else
                    sc.TransportDivision=2;

                sc.AreaCode = binding.edt6.getText().toString();//지역
                if(binding.edt7.getText().toString().substring(binding.edt7.getText().toString().length()-1).equals(".")){
                    binding.edt7.setText(binding.edt7.getText().toString().replace(".",""));
                }
                if(binding.edt7.getText().toString().equals("")){
                    sc.CarTon = 0;//차톤수
                }
                else{
                    sc.CarTon = Double.parseDouble(binding.edt7.getText().toString());//차톤수
                }
                if(binding.edt8.getText().toString().substring(binding.edt8.getText().toString().length()-1).equals(".")){
                    binding.edt8.setText(binding.edt8.getText().toString().replace(".",""));
                }
                if(binding.edt8.getText().toString().equals("")){
                    sc.TransportAmt = 0;//운반비
                }
                else{
                    sc.TransportAmt = Double.parseDouble(binding.edt8.getText().toString());//운반비
                }

                sc.CarNumber = binding.edt9.getText().toString();//차량번호
                sc.Remark1 = binding.edt10.getText().toString();//비고1
                sc.Remark2 = binding.edt11.getText().toString();//비고2
                sc.UserID = Users.UserID;

                commonViewModel.Get2("UpdateStockOutData", sc);
            }
        });

        binding.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialog5320 custom = new Dialog5320(locationNo);
                custom.show(fm, "");
                fm.executePendingTransactions();//Dialog 가 활성화 되지 않은 상태였기 때문에 getDialog() 를 호출할때 Null 에러가 발생 하기떄문에 이것을 넣어준다.
                custom.setOnDismissListener(dialog -> {
                    binding.edt1.setText(custom.containerNo);
                });
            }
        });
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialog5330 custom = new Dialog5330();
                custom.show(fm, "");
                fm.executePendingTransactions();//Dialog 가 활성화 되지 않은 상태였기 때문에 getDialog() 를 호출할때 Null 에러가 발생 하기떄문에 이것을 넣어준다.
                custom.setOnDismissListener(dialog -> {
                    binding.edt2.setText(Integer.toString(custom.deptCode));
                });
            }
        });
        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialog5340 custom = new Dialog5340();
                custom.show(fm, "");
                fm.executePendingTransactions();//Dialog 가 활성화 되지 않은 상태였기 때문에 getDialog() 를 호출할때 Null 에러가 발생 하기떄문에 이것을 넣어준다.
                custom.setOnDismissListener(dialog -> {
                    binding.edt3.setText(custom.customerCode);
                });
            }
        });
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialog5350 custom = new Dialog5350();
                custom.show(fm, "");
                fm.executePendingTransactions();//Dialog 가 활성화 되지 않은 상태였기 때문에 getDialog() 를 호출할때 Null 에러가 발생 하기떄문에 이것을 넣어준다.
                custom.setOnDismissListener(dialog -> {
                    binding.edt6.setText(custom.areaCode);
                });
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
        //QR런처
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);

    }

    @Override
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
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

    /**
     * 공통 끝
     */
}

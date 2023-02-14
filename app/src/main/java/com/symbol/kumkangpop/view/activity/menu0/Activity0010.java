package com.symbol.kumkangpop.view.activity.menu0;

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
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0010Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.SalesOrder;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

/**
 * 제품포장
 */
public class Activity0010 extends BaseActivity {
    Activity0010Binding binding;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0010);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0010):getString(R.string.detail_menu_0010_eng));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        GetMainData(saleOrderNo);
    }

    private void setView() {
        if(Users.Language == 1){
            binding.textView6.setText("SaleOrderNo");//주문번호
            binding.textInputLayout1.setHint("SaleOrderNo");

            binding.textView45.setText(" aa");
            binding.textView7.setText("Customer");//거래처명
            binding.textInputLayout2.setHint("CustomerName");

            binding.textView11.setText("Aaaa");
            binding.textView8.setText("Jobsite");//현장명
            binding.textInputLayout3.setHint("Jobsite");

            binding.textView12.setText("aaaaaa");
            binding.textView10.setText("Block");//동
            binding.textInputLayout4.setHint("Block");

            binding.btn1.setText("Inspection");//제품검수
            binding.btn2.setText("Print Zone");//세대출력
            binding.btn3.setText("Packing");//제품포장
            binding.btn4.setText("Edit Packing");//포장수정
            binding.btn5.setText("Weigh Packing");//포장계근
            binding.btn6.setText("Exit");//취소
        }
    }

    private void GetMainData(String tSaleOrderNo) {
        SearchCondition sc = new SearchCondition();
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.CustomerCode = Users.CustomerCode;
        sc.SaleOrderNo = tSaleOrderNo;
        commonViewModel.Get("GetSalesOrderData", sc);
    }

    public void observerViewModel() {
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;

                SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 1;
                sc.Barcode = barcode.Barcode;
                commonViewModel.Get2("GetNumConvertData", sc);
            }
            else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {

                ArrayList<SalesOrder> tempArrayList = data.SalesOrderList;
                binding.textInputLayout1.setHint("S2-"+tempArrayList.get(0).SaleOrderNo);
                binding.textInputLayout2.setHint(tempArrayList.get(0).CustomerName);
                binding.textInputLayout3.setHint(tempArrayList.get(0).LocationName);
                binding.textInputLayout4.setHint(tempArrayList.get(0).Dong);
                binding.edtInput1.setEnabled(false);
                binding.edtInput2.setEnabled(false);
                binding.edtInput3.setEnabled(false);
                binding.edtInput4.setEnabled(false);
                this.saleOrderNo = "S2-"+tempArrayList.get(0).SaleOrderNo;

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size()==0) {
                    Toast.makeText(this, Users.Language==0 ? "해당 TAG의 주문정보를 찾을 수 없습니다.": "Ordering information for the appropriate TAG can not be found.", Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    return;
                }
                //SearchCondition sc = new SearchCondition();
                //주문번호 및 화면 재셋팅
                GetMainData(data.NumConvertDataList.get(0).DestNum);
            }
            else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
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

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }



    private void setListener() {

        //제품검수
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0100.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
            }
        });
        //세대출력
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0120.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
            }
        });
        //제품포장
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0300.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
            }
        });
        //포장수정
        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0400.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
            }
        });
        //포장계근
        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0500.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
            }
        });
        //취소: 뒤로가기
        binding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
    }

    private void setResultLauncher() {
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);
    }

    /**
     * 공통 시작
     */
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
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher,2);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //result.getResultCode()를 통하여 결과값 확인
                if(result.getResultCode() == RESULT_OK) {
                    //ToDo
                }
                if(result.getResultCode() == RESULT_CANCELED){
                    //ToDo
                }
            }
    );

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

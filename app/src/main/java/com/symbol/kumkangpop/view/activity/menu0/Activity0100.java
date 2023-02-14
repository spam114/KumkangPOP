package com.symbol.kumkangpop.view.activity.menu0;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.CheckTag;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 제품포장
 */
public class Activity0100 extends BaseActivity {
    Activity0100Binding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity0100);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0100):getString(R.string.detail_menu_0100_eng));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        //GetMainData(saleOrderNo);
    }

    private void setView() {
        binding.edtInput2.setEnabled(false);
        binding.edtInput3.setEnabled(false);
        binding.edtInput4.setEnabled(false);
        binding.edtInput5.setEnabled(false);
        binding.edtInput6.setEnabled(false);
        binding.edtInput7.setEnabled(false);
        binding.edtInput8.setEnabled(false);
        binding.edtInput9.setEnabled(false);


        if(Users.Language == 1){
            binding.textView47.setText("  ");
            binding.textView6.setText("TAGNO");//태그번호
            binding.textInputLayout1.setHint("TAGNO");

            binding.textView46.setText("");
            binding.textView7.setText("OrderNo");//주문번호
            binding.textInputLayout2.setHint("OrderNo");

            binding.textView11.setText("      ");
            binding.textView8.setText("Code");//코드
            binding.textInputLayout3.setHint("Code");

            binding.textView12.setText("       ");
            binding.textView13.setText("Item");//품명
            binding.textInputLayout4.setHint("Item");

            binding.textView14.setText("       ");
            binding.textView15.setText("Size");//규격
            binding.textInputLayout5.setHint("Size");

            binding.textView16.setText("      ");
            binding.textView17.setText("Spec");//제작사양
            binding.textInputLayout6.setHint("Spec");

            binding.textView18.setText("      ");
            binding.textView19.setText("DWG");//도면번호
            binding.textInputLayout7.setHint("DWG");

            binding.textView20.setText("     ");
            binding.textView21.setText("R-Qty");//지시량
            binding.textInputLayout8.setHint("R-Qty");

            binding.textView22.setText("     ");
            binding.textView23.setText("C-Qty");//검수량
            binding.textInputLayout9.setHint("C-Qty");
        }
    }

    private void GetMainData(String barCode) {
        CommonMethod.FNBarcodeConvertPrint(barCode, barcodeConvertPrintViewModel);
    }

    public void observerViewModel() {
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                binding.edtInput1.setText(barcode.Barcode);
                SearchCondition sc = new SearchCondition();
                sc.CustomerCode = Users.CustomerCode;
                sc.SaleOrderNo = this.saleOrderNo;
                sc.Barcode = barcode.Barcode;
                commonViewModel.Get("GetItemTagCheck", sc);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                ArrayList<CheckTag> tempArrayList = data.CheckTagList;

                if (tempArrayList.get(0).ErrorCheck != null) {
                    Toast.makeText(this, tempArrayList.get(0).ErrorCheck, Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                } else {
                    DecimalFormat numFormatter = new DecimalFormat("###,###");
                    binding.edtInput1.setText(tempArrayList.get(0).ItemTag);
                    binding.textInputLayout2.setHint(tempArrayList.get(0).SaleOrderNo);
                    binding.textInputLayout3.setHint(tempArrayList.get(0).PartCode);
                    binding.textInputLayout4.setHint(tempArrayList.get(0).PartName);
                    binding.textInputLayout5.setHint(tempArrayList.get(0).PartSpec);
                    binding.textInputLayout6.setHint(tempArrayList.get(0).MSpec);
                    binding.textInputLayout7.setHint(tempArrayList.get(0).DrawingNo);

                    binding.textInputLayout8.setHint(numFormatter.format(tempArrayList.get(0).StockOutOrderQty));
                    binding.textInputLayout9.setHint(numFormatter.format(tempArrayList.get(0).StockOutCheckQty));
                    Toast.makeText(this, Users.Language==0 ? "검수 완료되었습니다.": "The inspection is complete.", Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                }
                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
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
        binding.edtInput1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    GetMainData(binding.edtInput1.getText().toString());
                }
                return false;
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

    @Override
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        GetMainData(result);
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

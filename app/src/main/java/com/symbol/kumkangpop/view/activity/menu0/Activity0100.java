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
        binding.txtTitle.setText(getString(R.string.detail_menu_0100));
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
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                ArrayList<CheckTag> tempArrayList = data.CheckTagList;

                if (tempArrayList.get(0).ErrorCheck != null) {
                    Toast.makeText(this, tempArrayList.get(0).ErrorCheck, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "검수 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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

    /**
     * 공통 끝
     */
}

package com.symbol.kumkangpop.view.activity.menu0;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0510Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Packing;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

/**
 * 제품포장
 */
public class Activity0510 extends BaseActivity {
    Activity0510Binding binding;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;
    String packingNo;
    String[] items = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0510);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0510):getString(R.string.detail_menu_0510_eng));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        packingNo = getIntent().getStringExtra("packingNo");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        GetMainData(saleOrderNo, packingNo);
    }

    private void setView() {
        binding.edtInput1.setEnabled(false);

        items = new String[3];
        items[0]="알폼";
        items[1]="액세서리";
        items[2]="스틸";

        if(Users.Language==1){
            items[0]="aluminium";
            items[1]="accessory";
            items[2]="steel";

            binding.radioButton.setText("Large");
            binding.radioButton2.setText("Medium");
            binding.radioButton3.setText("Small");


            binding.textView6.setText("PackingNo");//포장번호
            binding.textInputLayout1.setHint("PackingNo");

            binding.textView50.setText("  ");
            binding.textView14.setText("ItemType");//제품유형
            binding.textInputLayout5.setHint("ItemType");

            binding.textView16.setText("      ");
            binding.textView17.setText("Weight");//실중량
            binding.textInputLayout6.setHint("Weight");

            binding.textView9.setText("       ");
            binding.textView7.setText("Palette");//팔레트

            binding.textView11.setText("             ");
            binding.textView8.setText("Bin");//빈
            binding.textInputLayout3.setHint("Bin");

            binding.textView12.setText("     ");
            binding.textView13.setText("Remark");//비고
            binding.textInputLayout4.setHint("Remark");

            binding.btnSave.setText("SAVE");
        }



        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, R.layout.item_list, items);
        binding.comboPartType.setAdapter(itemAdapter);
        binding.comboPartType.setInputType(0);
        binding.comboPartType.setText(items[0], false);
    }

    private void GetMainData(String saleOrderNo, String packingNo) {
        SearchCondition sc = new SearchCondition();
        sc.SaleOrderNo = saleOrderNo;
        sc.LocationNo = Users.LocationNo;
        sc.PackingNo = packingNo;
        commonViewModel.Get("GetPackingData", sc);
    }

    public void observerViewModel() {
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });

        //GetPackingData 후
        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                ArrayList<Packing> tempArrayList = data.PackingList;

                binding.textInputLayout1.setHint(tempArrayList.get(0).PackingNo);//포장번호
                //binding.textInputLayout5.setHint(tempArrayList.get(0).LocationName);//제품유형
                binding.edtInput6.setText(Double.toString(tempArrayList.get(0).Weight));//실중량
                binding.edtInput3.setText(tempArrayList.get(0).Bin);//빈
                binding.edtInput4.setText(tempArrayList.get(0).Remark);//비고
                String pType = tempArrayList.get(0).PalletType;
                if (pType.equals("대")) {
                    binding.radioButton.setChecked(true);
                } else if (pType.equals("중")) {
                    binding.radioButton2.setChecked(true);
                } else {
                    binding.radioButton3.setChecked(true);
                }
                binding.comboPartType.setInputType(0);
                binding.comboPartType.setText(items[(int)tempArrayList.get(0).PackingDivision-1],false);

                /*SearchCondition sc = new SearchCondition();
                sc.CustomerCode = Users.CustomerCode;
                saleOrderNo = sc.SaleOrderNo;
                commonViewModel.Get2("GetSalesOrderDataHo", sc);*/

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
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
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });

        /*commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
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
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                int packingDivision=0;
                String palletType="";
                if(binding.comboPartType.getText().toString().equals("알폼") || binding.comboPartType.getText().toString().equals("aluminium")){
                    packingDivision=1;
                }
                else if(binding.comboPartType.getText().toString().equals("액세서리") || binding.comboPartType.getText().toString().equals("accessory")){
                    packingDivision=2;
                }
                else{
                    packingDivision=3;
                }
                if(binding.radioButton.isChecked()){
                    palletType = "대";
                }
                else if(binding.radioButton2.isChecked()){
                    palletType = "중";
                }
                else{
                    palletType = "소";
                }
                sc.PackingNo = packingNo;
                sc.PackingDivision = packingDivision;
                sc.Weight = Double.parseDouble(binding.edtInput6.getText().toString());
                sc.PalletType = palletType;
                sc.Bin = binding.edtInput3.getText().toString();
                sc.Remark = binding.edtInput4.getText().toString();
                commonViewModel.Get2("UpdatePackingData", sc);
            }
        });
        /*binding.edtInput1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // IME_ACTION_SEARCH , IME_ACTION_GO
                    GetMainData(binding.edtInput1.getText().toString());
                }
                return false;
            }
        });*/
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

    @Override
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
    }

    /**
     * 공통 끝
     */
}

package com.symbol.kumkangpop.view.activity.menu4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity4000Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter4000;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity4000 extends BaseActivity {
    Activity4000Binding binding;
    Adapter4000 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    List<ScanListViewItem2> scanListViewItemList2 = new ArrayList<>();
    List<ScanListViewItem2> scanListViewItemTemp = new ArrayList<>();
    int action = 0;//0:scan, 1:입력, 2:삭제
    //File filePath;

    //int stockOutType = 3;
   /* int gLocationNo = 0;
    String gPackingNo = "";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity4000);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_4000) : getString(R.string.detail_menu_4000_eng));
        //binding.tvStockOutNo.setText(getIntent().getStringExtra("stockOutNo"));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter4000(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    /*private void GetStockInFlag() {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        commonViewModel.Get3("GetStockInFlag", sc);
    }*/

    private void setView() {

        if (Users.Language == 1) {
            //binding.textInputLayout1.setHint("In Store");
            binding.textView32.setText("Invty-OutNo");
            binding.textView34.setText("PackingNo");//포장번호
            binding.textView35.setText("Customer(Jobsite)");//거래처
            binding.textView37.setText("Block");//동
            binding.textView38.setText("Zone");//세대
            binding.textView39.setText("No");//No
            binding.textView40.setText("OrderNo");//주문번호
            binding.textView42.setText("ViewType");//처리구분
            binding.rbAdd.setText("Stock Not");
            binding.rbExcept.setText("Stock In");
            binding.txtInfo.setText("\"Please Scan Packing Order TAG\"");

            binding.btnDeleteAll.setText("Not All");
            binding.btnInputAll.setText("In All");
        }
    }

    /*private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SearchDivision = 0;
        sc.LocationNo = locationNo;
        sc.ContainerNo = "";
        commonViewModel.Get("GetContainerData", sc);
    }*/

    /*public void ScanStockOut(String barcode) {
        SearchCondition sc = new SearchCondition();
        sc.ScanList2 = scanListViewItemList2;
        sc.IConvetDivision = 5;
        sc.Barcode = barcode;
        sc.PackingNo = binding.tvPackingNo.getText().toString();
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        sc.GPackingNo = packingNo;
        sc.ALQty = Double.parseDouble(binding.tvALQty.getText().toString());
        sc.ACQty = Double.parseDouble(binding.tvACQty.getText().toString());
        sc.STQty = Double.parseDouble(binding.tvSTQty.getText().toString());

        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;


        commonViewModel.Get2("ScanStockOut", sc);
    }*/

    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                ScanBring(barcode.Barcode);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        /*//프린트 후 메시지 처리
        barcodeConvertPrintViewModel.data2.observe(this, barcode -> {
            if (barcode != null) {
                Toast.makeText(this, barcode.Result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
            }
        });*/

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

                if (data.StrResult != null) {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }

                if (data.ScanResult2 != null) {
                    if (data.ScanResult2.ListView != null) {
                        scanListViewItemList2 = data.ScanResult2.ListView;
                        scanListViewItemTemp = data.ScanResult2.ListViewTemp;
                        adapter.updateAdapter((ArrayList<ScanListViewItem2>) scanListViewItemList2, binding.rbAdd.isChecked());
                    }
                    if (data.ScanResult2.StockOutNo != null) {
                        binding.tvStockOutNo.setText(data.ScanResult2.StockOutNo);
                    }
                    action = (int) data.ScanResult2.Action;
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                if(data.StrResult!=null){
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                scanListViewItemList2.clear();

                /*boolean b1 = binding.rbAdd.isChecked();
                boolean b2 = binding.rbExcept.isChecked();
                String str = scanListViewItemTemp.get(0).StockOutStatus;*/

                for (int i = 0; i < scanListViewItemTemp.size(); i++) {
                    if (binding.rbAdd.isChecked() && scanListViewItemTemp.get(i).StockOutStatus.equals("N") ||
                            binding.rbExcept.isChecked() && scanListViewItemTemp.get(i).StockOutStatus.equals("Y")) {
                        AddListViewItem(i);
                    }
                }

                adapter.updateAdapter((ArrayList<ScanListViewItem2>) scanListViewItemList2, binding.rbAdd.isChecked());

                DecimalFormat numFormatter = new DecimalFormat("###");
                /*if (data.StrResult != null) {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                if (data.ScanResult2 != null) {
                    if (data.ScanResult2.ListView != null) {
                        adapter.updateAdapter(data.ScanResult2.ListView);
                        scanListViewItemList2 = data.ScanResult2.ListView;
                    }
                    if (data.ScanResult2.StockOutNo != null) {
                        binding.tvStockOutNo.setText(data.ScanResult2.StockOutNo);
                    }
                }*/


            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        /*commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                int stockInFlag = data.IntResult;
                if (stockInFlag == 1) {//출고완료
                    binding.tvState.setText("완료");
                    binding.tvState.setTextColor(Color.BLUE);
                } else {//미완료
                    binding.tvState.setText("미완료");
                    binding.tvState.setTextColor(Color.BLACK);
                }

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
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

    public void ScanBring(String barcode) {
        SearchCondition sc = new SearchCondition();
        //Data유지
        sc.ScanList2 = scanListViewItemList2;
        sc.ScanListTemp = scanListViewItemTemp;
        sc.Action = action;
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();

        sc.Barcode = barcode;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;
        sc.LocationNo = Users.LocationNo;

        if (binding.rbAdd.isChecked()) {
            sc.BoolRadio1 = true;
            sc.BoolRadio2 = false;
        } else {
            sc.BoolRadio1 = false;
            sc.BoolRadio2 = true;
        }
        commonViewModel.Get("ScanBring", sc);
    }

    /*public void RemoveStockOut(String barcode) {
        SearchCondition sc = new SearchCondition();
        sc.ScanList2 = scanListViewItemList2;
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        sc.Barcode = barcode;
        sc.PCCode = Users.PCCode;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;
        sc.LocationNo = Users.LocationNo;
        sc.BoolRadio1 = false;
        sc.BoolRadio2 = true;

        commonViewModel.Get("ScanBring", sc);
    }*/

    private void AddListViewItem(int ViewIndex) {
        /*ListViewItem lv = new ListViewItem();
        lv.Text = listView2.Items[ViewIndex].Text;
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[1].Text);
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[2].Text);
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[3].Text);
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[5].Text);
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[6].Text);
        lv.SubItems.Add(listView2.Items[ViewIndex].SubItems[7].Text);
        listView1.Items.Insert(0, lv);*/

        ScanListViewItem2 scanListViewItem2 = new ScanListViewItem2();
        scanListViewItem2.PackingNo = scanListViewItemTemp.get(ViewIndex).PackingNo;
        scanListViewItem2.Dong = scanListViewItemTemp.get(ViewIndex).Dong;
        scanListViewItem2.Ho = scanListViewItemTemp.get(ViewIndex).Ho;
        scanListViewItem2.HoSeqNo = scanListViewItemTemp.get(ViewIndex).HoSeqNo;
        scanListViewItem2.CustomerName = scanListViewItemTemp.get(ViewIndex).CustomerName;
        scanListViewItem2.LocationName = scanListViewItemTemp.get(ViewIndex).LocationName;
        scanListViewItem2.SaleOrderNo = scanListViewItemTemp.get(ViewIndex).SaleOrderNo;
        scanListViewItemList2.add(scanListViewItem2);
    }

    private void setListener() {
        binding.rbAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*listView1.Items.Clear();

                for (int i = 0; i < listView2.Items.Count; i++)
                {
                    if (rdoCheck1.Checked && listView2.Items[i].SubItems[4].Text == "N" ||
                            rdoCheck2.Checked && listView2.Items[i].SubItems[4].Text == "Y")
                    {
                        AddListViewItem(i);
                    }
                }*/

                scanListViewItemList2.clear();
                for (int i = 0; i < scanListViewItemTemp.size(); i++) {
                    if (isChecked && scanListViewItemTemp.get(i).StockOutStatus.equals("N") ||
                            !isChecked && scanListViewItemTemp.get(i).StockOutStatus.equals("Y")) {
                        AddListViewItem(i);
                    }
                }
                adapter.updateAdapter((ArrayList<ScanListViewItem2>) scanListViewItemList2, binding.rbAdd.isChecked());
            }
        });

        /*binding.rbExcept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scanListViewItemList2.clear();
                for (int i = 0; i < scanListViewItemTemp.size(); i++) {
                    if (!isChecked && scanListViewItemTemp.get(i).StockOutStatus.equals("N") ||
                            isChecked && scanListViewItemTemp.get(i).StockOutStatus.equals("Y")) {
                        AddListViewItem(i);
                    }
                }
                //adapter.updateAdapter((ArrayList<ScanListViewItem2>) scanListViewItemList2);
            }
        });*/
        /**
         * forfilter
         */
        /*binding.edtInput.addTextChangedListener(new TextWatcher() {
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

        binding.btnCreateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5110.class);
                intent.putExtra("containerNo", "");
                intent.putExtra("locationNo", locationNo);
                activityResultLauncher.launch(intent);
            }
        });*/

        /*binding.tvStockOutNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {//출고번호가 있으면 기타정보 버튼 활성화
                    binding.btnEtc.setVisibility(View.VISIBLE);
                    binding.textView36.setVisibility(View.VISIBLE);
                    binding.tvState.setVisibility(View.VISIBLE);
                    GetStockInFlag();
                } else {
                    binding.btnEtc.setVisibility(View.GONE);
                    binding.textView36.setVisibility(View.GONE);
                    binding.tvState.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        /*binding.btnEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5310.class);
                intent.putExtra("stockOutNo", binding.tvStockOutNo.getText().toString());
                intent.putExtra("locationNo", locationNo);
                activityResultLauncher.launch(intent);
            }
        });*/

        binding.btnInputAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CommonMethod.FNSetPrintOrderData(Activity4000.this, barcodeConvertPrintViewModel, 5, binding.tvStockOutNo.getText().toString());
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(Activity4000.this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                alertBuilder.setTitle(Users.Language == 0 ? "일괄이입" : "Input All");
                alertBuilder.setMessage(Users.Language == 0 ? "일괄이입 하시겠습니까?" : "Do you want to register in batches?");
                alertBuilder.setPositiveButton(Users.Language == 0 ? "확인" : "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PackingBatchImport("Y");
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton(Users.Language == 0 ? "취소" : "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
            }
        });

        binding.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(Activity4000.this, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                alertBuilder.setTitle(Users.Language == 0 ? "일괄제외" : "Exclude All");
                alertBuilder.setMessage(Users.Language == 0 ? "일괄제외 하시겠습니까?" : "Do you want to exclude in batches?");
                alertBuilder.setPositiveButton(Users.Language == 0 ? "확인" : "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PackingBatchImport("N");
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton(Users.Language == 0 ? "취소" : "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
            }
        });

        /*binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CommonMethod.FNSetPrintOrderData(Activity4000.this, barcodeConvertPrintViewModel, 5, binding.tvStockOutNo.getText().toString());
            }
        });*/

        /*binding.btnEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5310.class);
                intent.putExtra("stockOutNo", binding.tvStockOutNo.getText().toString());
                intent.putExtra("locationNo", gLocationNo);
                activityResultLauncher.launch(intent);
            }
        });*/
    }

    private void PackingBatchImport(String SetYN)
    {
        for (int i = 0; i < scanListViewItemTemp.size(); i++)
        {
            if (SetYN.equals("Y")  && scanListViewItemTemp.get(i).StockOutStatus.equals("N") ||
                    SetYN.equals("N")  && scanListViewItemTemp.get(i).StockOutStatus.equals("Y"))
            {
                scanListViewItemTemp.get(i).StockOutStatus=SetYN;
            }
        }

        SearchCondition sc = new SearchCondition();
        sc.WorkDivision = SetYN.equals("Y") ? 3 : 4;
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        sc.PackingNo = "";
        sc.LocationNo = Users.LocationNo;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;
        commonViewModel.Get2("UpdateStockOutDetailData", sc);
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

    /*ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                GetMainData();
                //result.getResultCode() 기본값은 0
                *//*if (result.getResultCode() == RESULT_OK) {//-1
                }
                if (result.getResultCode() == RESULT_CANCELED) {//0
                }*//*
            }
    );*/

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

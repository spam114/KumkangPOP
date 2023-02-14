package com.symbol.kumkangpop.view.activity.menu0;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0120Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.SalesOrder;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 제품포장
 */
public class Activity0120 extends BaseActivity {
    Activity0120Binding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity0120);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0120):getString(R.string.detail_menu_0120_eng));
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
        binding.edtInput1.setEnabled(false);
        binding.edtInput2.setEnabled(false);
        binding.edtInput3.setEnabled(false);
        binding.edtInput4.setEnabled(false);

        if(Users.Language == 1){
            binding.textView49.setText("  ");
            binding.textView6.setText("OrderNo");//주문번호
            binding.textInputLayout1.setHint("OrderNo");

            binding.textView48.setText("");
            binding.textView7.setText("Customer");//거래처명
            binding.textInputLayout2.setHint("Customer");

            binding.textView11.setText("    ");
            binding.textView8.setText("Jobsite");//현장명
            binding.textInputLayout3.setHint("Jobsite");

            binding.textView12.setText("       ");
            binding.textView13.setText("Block");//동
            binding.textInputLayout4.setHint("Block");

            binding.textView14.setText("        ");
            binding.textView15.setText("Zone");//세대
            binding.textInputLayout5.setHint("Zone");

            binding.textView16.setText("   ");
            binding.textView17.setText("PRT Qty");//개수
            binding.textInputLayout6.setHint("PRT Qty");

            binding.btnPrint.setText("PRINT TAG");
            binding.btnRePrint.setText("REPRINT TAG");
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
                SearchCondition sc = new SearchCondition();
                sc.BusinessClassCode = Users.BusinessClassCode;
                sc.CustomerCode = Users.CustomerCode;
                sc.SaleOrderNo = barcode.Barcode;
                commonViewModel.Get("GetSalesOrderData", sc);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                ArrayList<SalesOrder> tempArrayList = data.SalesOrderList;

                if (tempArrayList.get(0).ErrorCheck != null) {
                    Toast.makeText(this, tempArrayList.get(0).ErrorCheck, Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                } else {
                    binding.textInputLayout1.setHint("S2-" + tempArrayList.get(0).SaleOrderNo);
                    binding.textInputLayout2.setHint(tempArrayList.get(0).CustomerName);
                    binding.textInputLayout3.setHint(tempArrayList.get(0).LocationName);
                    binding.textInputLayout4.setHint(tempArrayList.get(0).Dong);

                    SearchCondition sc = new SearchCondition();
                    sc.CustomerCode = Users.CustomerCode;
                    sc.SaleOrderNo = "S2-" + tempArrayList.get(0).SaleOrderNo;
                    saleOrderNo = sc.SaleOrderNo;
                    commonViewModel.Get2("GetSalesOrderDataHo", sc);
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

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                String[] items = new String[data.HoList.size()];
                for (int i = 0; i < data.HoList.size(); i++) {
                    items[i] = data.HoList.get(i).Ho;
                }

                ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, R.layout.item_list, items);
                binding.comboHo.setAdapter(itemAdapter);
                binding.comboHo.setInputType(0);
                binding.comboHo.setText(items[0], false);


                //binding.comboHo.setText(items[0]);

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                Toast.makeText(this, Users.Language==0 ? "완료 되었습니다.": "The operation is complete.", Toast.LENGTH_SHORT).show();
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

        //출력
        binding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleKor="작업TAG(포장) 출력";
                String titleEng="TAG (Packing) Print";
                String messageKor="출력(TAG생성)작업을 진행하시겠습니까?";
                String messageEng="Are you sure you want to proceed with the output (create TAG)?";
                String poButtonKor="확인";
                String poButtonEng="OK";
                String negaButtonKor="취소";
                String negaButtonEng="Cancel";

                String strTitle;
                String strMessage;
                String strPoButton;
                String strNegaButton;

                if(Users.Language ==0){
                    strTitle=titleKor;
                    strMessage=messageKor;
                    strPoButton=poButtonKor;
                    strNegaButton=negaButtonKor;
                }
                else{
                    strTitle=titleEng;
                    strMessage=messageEng;
                    strPoButton=poButtonEng;
                    strNegaButton=negaButtonEng;
                }


                new MaterialAlertDialogBuilder(Activity0120.this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                if (Users.PCCode.equals("")) {
                                    Toast.makeText(Activity0120.this, Users.Language==0 ? "출력 PC가 연결되어 있지 않습니다.": "There is no PC connected.", Toast.LENGTH_SHORT).show();
                                    Users.SoundManager.playSound(0, 2, 3);//에러
                                    return;
                                }
                                ListAdapter adapter = binding.comboHo.getAdapter();
                                List<String> tempList =new ArrayList<>();

                                for (int i = 0; i < adapter.getCount(); i++) {
                                    tempList.add(adapter.getItem(i).toString());
                                }

                                int printNum=0;
                                try{
                                    printNum = Integer.parseInt(binding.edtInput6.getText().toString());
                                }
                                catch (Exception et){
                                    Users.SoundManager.playSound(0, 2, 3);
                                    return;
                                }
                                //String sPackingNo = CommonMethod.GetPackingNo();
                                SearchCondition sc = new SearchCondition();
                                sc.SaleOrderNo = saleOrderNo;
                                sc.Ho = binding.comboHo.getText().toString();
                                sc.LocationNo = Users.LocationNo;
                                sc.PCCode = Users.PCCode;
                                sc.UserID = Users.UserID;
                                sc.PrintNum = printNum;
                                sc.HoList=tempList;
                                commonViewModel.Get3("PrintPacking", sc);
                            }
                        })
                        .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        //재출력
        binding.btnRePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity0122.class);
                intent.putExtra("saleOrderNo", saleOrderNo);
                activityResultLauncher.launch(intent);
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

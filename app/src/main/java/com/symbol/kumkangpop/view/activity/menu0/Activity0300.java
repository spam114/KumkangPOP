package com.symbol.kumkangpop.view.activity.menu0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0300Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ScanListViewItem;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.MC3300X;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter0300;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 제품포장
 */
public class Activity0300 extends BaseActivity {
    Activity0300Binding binding;
    Adapter0300 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;
    String nType = "Ready";
    List<ScanListViewItem> scanListViewItemList = new ArrayList<>();
    MC3300X mc3300X;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0300);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0300) : getString(R.string.detail_menu_0300_eng));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        binding.tvSaleOrderNo.setText(saleOrderNo);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter0300(new ArrayList<>(), this, resultLauncher);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView32.setText("OrderNo");
            binding.textView33.setText("PackingNo");
            binding.tv1.setText("Order-Qty");
            binding.tv2.setText("Packing-Qty");
            binding.tv3.setText("Scan-Qty");

            binding.tvName.setText("ITEM/SIZE");
            binding.textViewWorkDate.setText("SPEC");//사양
            binding.textViewWorkType.setText("ZONE");//세대
            binding.textViewWorkType4.setText("DWG");//도면
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.UserID = Users.UserID;
        sc.SaleOrderNo = saleOrderNo;
        commonViewModel.Get("GetSalesOrderDataCnt", sc);
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
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size() == 0) {
                    *//*Toast.makeText(this, "해당 TAG의 포장정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;*//*
                }
                else{
                    //CommonMethod.FNSetPrintOrderData(this, barcodeConvertPrintViewModel, 1, data.NumConvertDataList.get(0).DestNum);
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*barcodeConvertPrintViewModel.data2.observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, result.Result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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
                if (data.SalesOrderList.size() == 0) {
                    binding.tvSaleOrderQty.setText(0);
                    binding.tvPackingQty.setText(0);

                    //binding.tvSaleOrderNo.setText("");
                    //binding.tvPackingNo.setText("");
                } else {
                    DecimalFormat numFormatter = new DecimalFormat("###,###");
                    binding.tvSaleOrderQty.setText(numFormatter.format(data.SalesOrderList.get(0).StockOutOrderQty));
                    binding.tvPackingQty.setText(numFormatter.format(data.SalesOrderList.get(0).PackingCnt));
                }
                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(data.PackingList);*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                DecimalFormat numFormatter = new DecimalFormat("###,###");
                if (data.StrResult != null) {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                if (data.ScanResult != null) {
                    if (data.ScanResult.ItemTag != null) {

                    }
                    if (data.ScanResult.ListView != null) {
                        adapter.updateAdapter(data.ScanResult.ListView);
                        scanListViewItemList = data.ScanResult.ListView;
                    }
                    if (data.ScanResult.NType != null) {
                        nType = data.ScanResult.NType;
                    }
                    if (data.ScanResult.PackingNo != null) {
                        binding.tvPackingNo.setText(data.ScanResult.PackingNo);
                    }
                    //PackingQty
                    binding.tvPackingQty.setText(numFormatter.format(data.ScanResult.PackingQty));
                    if (data.ScanResult.SaleOrderNo != null) {
                        binding.tvSaleOrderNo.setText(data.ScanResult.SaleOrderNo);
                    }
                    //SaleOrderQty
                    binding.tvSaleOrderQty.setText(numFormatter.format(data.ScanResult.SaleOrderQty));
                    //ScanQty
                    binding.tvScanQty.setText(numFormatter.format(data.ScanResult.ScanQty));
                    if (data.ScanResult.StrColumn0 != null) {
                        binding.tvName.setText(data.ScanResult.StrColumn0 + "/" + data.ScanResult.StrColumn1);
                    }
                }

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(data.PackingList);*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        /*
        commonViewModel.data3.observe(this, data -> {
            if (data != null) {



            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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

    private void setListener() {
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
     * 스캔 인식 (이 액티비티는 별도로 작업한다.)
     */
    private void setResultLauncher() {
        resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Users.SoundManager.playSound(0, 0, 3);
                        /**
                         * QR코드 시작
                         */
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            Scanning(scanResult);
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

    private void Scanning(String barcode) {
        String titleKor = "작업완료";
        String titleEng = "Operation completed";
        String messageKor = "출력작업을 진행하시겠습니까?";
        String messageEng = "Are you sure you want to proceed with the output?";
        String poButtonKor = "확인";
        String poButtonEng = "OK";
        String negaButtonKor = "취소";
        String negaButtonEng = "Cancel";

        String strTitle;
        String strMessage;
        String strPoButton;
        String strNegaButton;

        if (Users.Language == 0) {
            strTitle = titleKor;
            strMessage = messageKor;
            strPoButton = poButtonKor;
            strNegaButton = negaButtonKor;
        } else {
            strTitle = titleEng;
            strMessage = messageEng;
            strPoButton = poButtonEng;
            strNegaButton = negaButtonEng;
        }

        String[] saction = barcode.split("-");
        if (saction.length == 3) {
            if (saction[2].equals("03")) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                if (Users.PCCode.equals("")) {
                                    Toast.makeText(Activity0300.this, Users.Language == 0 ? "출력 PC가 연결되어 있지 않습니다." : "There is no PC connected.", Toast.LENGTH_SHORT).show();
                                    Users.SoundManager.playSound(0, 2, 3);//에러
                                    return;
                                }
                                ScanPacking(barcode);
                            }
                        })
                        .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
            } else {
                ScanPacking(barcode);
            }
        } else {
            ScanPacking(barcode);
        }
    }

    private void ScanPacking(String barcode) {

        SearchCondition sc = new SearchCondition();
        sc.ScanList = scanListViewItemList;//연결
        sc.Barcode = barcode;
        sc.NType = nType;//연결
        sc.SaleOrderNo = saleOrderNo;//바뀔일없음
        sc.PackingNo = binding.tvPackingNo.getText().toString();//연결
        sc.LocationNo = Users.LocationNo;
        sc.UserID = Users.UserID;
        sc.PCCode = Users.PCCode;
        sc.Language = Users.Language;
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.CustomerCode = Users.CustomerCode;

        sc.SaleOrderQty = Double.parseDouble(binding.tvSaleOrderQty.getText().toString().replace(",", ""));
        sc.PackingQty = Double.parseDouble(binding.tvPackingQty.getText().toString().replace(",", ""));
        sc.ScanQty = Double.parseDouble(binding.tvScanQty.getText().toString().replace(",", ""));

        commonViewModel.Get2("ScanPacking", sc);
    }

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        Scanning(result);
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
    protected void onPause()
    {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver =  new BroadcastReceiver() {
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

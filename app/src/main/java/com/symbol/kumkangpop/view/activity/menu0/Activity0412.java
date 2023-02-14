package com.symbol.kumkangpop.view.activity.menu0;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0412Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter0412;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class Activity0412 extends BaseActivity {
    Activity0412Binding binding;
    Adapter0412 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String packingNo;
    String dFlag = "개별";//일괄, 개별 삭제 구분
    //String saleOrderNo;
    //String ho;
    //ArrayList<ItemTag> tempItemTagArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0412);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_0412) : getString(R.string.detail_menu_0412_eng));
        packingNo = getIntent().getStringExtra("packingNo");
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter0412(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textViewWorkDate1.setText("ITEM TAG");//제품TAG
            binding.textViewWorkType42.setText("SPEC");//사양
            binding.textViewWorkType5.setText("DWG");//도면
            binding.textViewWorkDate.setText("ITEM");//품명
            binding.textViewWorkType4.setText("SIZE");//규격

            binding.btnDeleteAll.setText("DELETE ALL");
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.PackingNo = packingNo;
        commonViewModel.Get("GetPackingDetailData", sc);
    }

    private void scanning(String barcode) {
        String titleKor=getString(R.string.detail_menu_0412);
        String titleEng=getString(R.string.detail_menu_0412_eng);
        String messageKor="제품TAG \"" + barcode + "\"\n를 제외하겠습니까?";
        String messageEng="Do you want to exclude ITEMTAG"+"("+barcode+")?";
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

        new MaterialAlertDialogBuilder(this)
                .setTitle(strTitle)
                .setMessage(strMessage)
                .setCancelable(true)
                .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        fnRemoveList(barcode);
                    }
                })
                .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void fnRemoveList(String barcode) {
        //tempItemTagArrayList = itemTagList;
        CommonMethod.FNSetPackingPDAData(barcodeConvertPrintViewModel, 4, packingNo, barcode, 0);
    }

    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                scanning(barcode.Barcode);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        //FNSetPackingPDAData 반환 데이터
        barcodeConvertPrintViewModel.data3.observe(this, data -> {
            if (data != null) {
                if (data.Result.equals("True")) {

                    /*Packing packing = new Packing();
                    packing.ItemTag = tempItemTagArrayList.get(0).TagNo;
                    packing.PartName = tempItemTagArrayList.get(0).PartName;
                    packing.PartSpec = tempItemTagArrayList.get(0).PartSpec;
                    packing.MSpec = tempItemTagArrayList.get(0).MSpec;
                    packing.DrawingNo = tempItemTagArrayList.get(0).DrawingNo;
                    packing.PartCode = tempItemTagArrayList.get(0).PartCode;*/

                    GetMainData();
                    if (dFlag.equals("개별")) {
                        Toast.makeText(this, Users.Language==0 ? "제외 되었습니다.": "It's excluded", Toast.LENGTH_SHORT).show();
                    }
                     /*packing.ItemTag = tempItemTagArrayList.get(0).

                     adapter.addItem();

                     ListViewItem lvi = new ListViewItem();
                     lvi.Text = nDataTable.Rows[0]["ItemTag"].ToString();
                     lvi.SubItems.Add(nDataTable.Rows[0]["PartName"].ToString());
                     lvi.SubItems.Add(nDataTable.Rows[0]["PartSpec"].ToString());
                     lvi.SubItems.Add(nDataTable.Rows[0]["MSpec"].ToString());
                     lvi.SubItems.Add(nDataTable.Rows[0]["DrawingNo"].ToString());
                     lvi.SubItems.Add(nDataTable.Rows[0]["PartCode"].ToString());
                     listView1.Items.Insert(0, lvi);*/
                } else {

                }

            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

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
                /*DecimalFormat numFormatter = new DecimalFormat("###,###");
                binding.tvStockOutOrderQty.setText(numFormatter.format(num1));
                binding.tvStockOutQty.setText(numFormatter.format(num2));*/
                adapter.updateAdapter(data.PackingList);
                adapter.getFilter().filter(binding.edtInput.getText().toString());
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
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

        binding.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleKor="제품 일괄 제외";
                String titleEng="ITEM Exclude ALL";
                String messageKor="\"모든\" 제품TAG를 제외하겠습니까?";
                String messageEng="Exclude \"all\" TAGs?";
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
                try {
                    new MaterialAlertDialogBuilder(Activity0412.this)
                            .setTitle(strTitle)
                            .setMessage(strMessage)
                            .setCancelable(true)
                            .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dFlag = "일괄";
                                    for (int i = 0; i < adapter.getItemList().size(); i++) {
                                        fnRemoveList(adapter.getItemList().get(i).ItemTag);
                                    }
                                    Toast.makeText(getBaseContext(), Users.Language==0 ? "제외 되었습니다.": "It's excluded", Toast.LENGTH_SHORT).show();
                                    GetMainData();
                                }
                            })
                            .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } catch (Exception et) {

                } finally {
                    dFlag = "개별";
                }

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

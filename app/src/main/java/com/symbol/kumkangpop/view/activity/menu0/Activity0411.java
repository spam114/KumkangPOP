package com.symbol.kumkangpop.view.activity.menu0;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity0411Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ItemTag;
import com.symbol.kumkangpop.model.object.Packing;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter0411;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class Activity0411 extends BaseActivity {
    Activity0411Binding binding;
    Adapter0411 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String saleOrderNo;
    String ho;
    String packingNo;
    ArrayList<ItemTag> tempItemTagArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity0411);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(getString(R.string.detail_menu_0411));
        saleOrderNo = getIntent().getStringExtra("saleOrderNo");
        ho = getIntent().getStringExtra("ho");
        packingNo = getIntent().getStringExtra("packingNo");
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter0411(new ArrayList<>(), this, resultLauncher);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.PackingNo = packingNo;
        commonViewModel.Get("GetPackingDetailData", sc);
    }

    private void scanning(String barcode){
        SearchCondition sc = new SearchCondition();
        sc.CustomerCode = Users.CustomerCode;
        sc.Barcode = barcode;
        commonViewModel.Get2("GetItemTagData", sc);
    }

    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                scanning(barcode.Barcode);
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.ItemTagList.size() == 0) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "품목Tag 정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Tag information items can not be found.", Toast.LENGTH_LONG).show();
                    }
                }
                else if (!data.ItemTagList.get(0).PackingNo.equals(""))
                {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "이미 포장된 제품TAG 입니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "TAG product is already paved.", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                else if (!data.ItemTagList.get(0).SaleOrderNo.equals(saleOrderNo))
                {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "주문번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Order numbers do not match.", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                else if (!data.ItemTagList.get(0).Ho.equals(ho))
                {
                    if (data.ItemTagList.get(0).PartCombine==1)
                    {
                        if (Users.Language == 0) {
                            Toast.makeText(this, "기존 세대와 현재 입력된 세대가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Zone previous generations, and currently entered do not match.", Toast.LENGTH_LONG).show();
                        }
                        /*if (mclMsgBox.Show(mclVariable.Language == 0 ? "기존 세대와 현재 입력된 세대가 일치하지 않습니다. 제품포장을 진행하시겠습니까?(Yes:F3/No:F4)" : "zone previous generations, and currently entered do not match. Do you wish to proceed with the product packaging? (Yes: F3/No: F4)", "", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button1) == DialogResult.OK)
                        {
                            fnAddList(nDataTable);
                            txtItemTag.Text = "";
                        }*/
                    }
                    else
                    {
                        if (Users.Language == 0) {
                            Toast.makeText(this, "기존 세대와 현재 입력된 세대가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Zone previous generations, and currently entered do not match.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    fnAddList(data.ItemTagList);
                    /*SearchCondition sc = new SearchCondition();
                    sc.CustomerCode = Users.CustomerCode;
                    sc.Barcode = barcode.Barcode;
                    commonViewModel.Get3("GetItemTagData", sc);*/
                    //txtItemTag.Text = "";
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });

        //FNSetPackingPDAData 반환 데이터
        barcodeConvertPrintViewModel.data3.observe(this, data -> {
            if (data != null) {
                 if(data.Result.equals("True")){

                     /*Packing packing = new Packing();
                     packing.ItemTag = tempItemTagArrayList.get(0).TagNo;
                     packing.PartName = tempItemTagArrayList.get(0).PartName;
                     packing.PartSpec = tempItemTagArrayList.get(0).PartSpec;
                     packing.MSpec = tempItemTagArrayList.get(0).MSpec;
                     packing.DrawingNo = tempItemTagArrayList.get(0).DrawingNo;
                     packing.PartCode = tempItemTagArrayList.get(0).PartCode;

                     adapter.addItem(packing);*/

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
                     GetMainData();
                 }
                 else{

                 }

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
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
                adapter.updateAdapter(data.PackingList);
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /*
        commonViewModel.data3.observe(this, data -> {
            if (data != null) {



            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
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

    private void fnAddList(ArrayList<ItemTag> itemTagList) {
        tempItemTagArrayList = itemTagList;
        CommonMethod.FNSetPackingPDAData(barcodeConvertPrintViewModel,3, packingNo, itemTagList.get(0).TagNo, 0);
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

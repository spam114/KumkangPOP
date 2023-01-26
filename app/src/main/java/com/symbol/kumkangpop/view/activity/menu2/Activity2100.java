package com.symbol.kumkangpop.view.activity.menu2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.symbol.kumkangpop.databinding.Activity2100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.menu0.Activity0300;
import com.symbol.kumkangpop.view.adapter.Adapter2100;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity2100 extends BaseActivity {
    Activity2100Binding binding;
    Adapter2100 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    List<ScanListViewItem2> scanListViewItemList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity2100);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_2100):getString(R.string.detail_menu_2100_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter2100(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        //GetMainData();
    }

    private void GetMainData() {
        /*SearchCondition sc = new SearchCondition();
        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        commonViewModel.Get("GetStockOutDataCnt", sc);*/
    }


    private void setView() {
        if (Users.Language == 1) {
            //binding.textInputLayout1.setHint("In Store");
            binding.textView32.setText("BundleNo");

            binding.textView34.setText("PackingNo");//포장번호
            binding.textView35.setText("Customer(Jobsite)");//거래처
            binding.textView37.setText("Block");//동
            binding.textView38.setText("Zone");//세대
            binding.textView39.setText("No");//No
            binding.textView40.setText("OrderNo");//주문번호

            binding.btn1.setText("Ins PK");
            binding.btn2.setText("Del PK");
            binding.btn3.setText("Prn BN");
            binding.btn4.setText("Exit");
        }
    }

    /*private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SearchDivision = 0;
        sc.LocationNo = locationNo;
        sc.ContainerNo = "";
        commonViewModel.Get("GetContainerData", sc);
    }*/

    public void ScanBundle(String barcode) {

        for (int i = 0; i < adapter.getItemList().size(); i++) {
            if (adapter.getItem(i).PackingNo.equalsIgnoreCase(barcode)) {
                String title = "작업목록 제외";
                String message = "작업목록에서 제외하시겠습니까?\n" + barcode;
                String okString = "확인";
                String noString = "취소";

                if (Users.Language != 0) {
                    title = "Exclude Task List";
                    message = "Are you sure you want to exclude it from the task list?\n" + barcode;
                    okString = "Confirm";
                    noString = "Cancel";
                }

                new MaterialAlertDialogBuilder(this)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(okString, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SearchCondition sc = new SearchCondition();
                                sc.ScanList2 = scanListViewItemList2;
                                sc.BundleNo = binding.tvBundleNo.getText().toString();
                                sc.Barcode = barcode;
                                sc.UserID = Users.UserID;
                                sc.Language = Users.Language;
                                sc.PCCode = Users.PCCode;
                                sc.LocationNo = Users.LocationNo;

                                commonViewModel.Get2("ScanBundle", sc);
                            }
                        })
                        .setNegativeButton(noString, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return;
            }
        }

        SearchCondition sc = new SearchCondition();
        sc.ScanList2 = scanListViewItemList2;
        sc.BundleNo = binding.tvBundleNo.getText().toString();
        sc.Barcode = barcode;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;
        sc.PCCode = Users.PCCode;
        sc.LocationNo = Users.LocationNo;

        commonViewModel.Get2("ScanBundle", sc);
    }

    public void observerViewModel() {

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
                DecimalFormat numFormatter = new DecimalFormat("###");

                /*binding.tvALQty.setText(numFormatter.format(data.ScanResult2.ALQty));
                binding.tvACQty.setText(numFormatter.format(data.ScanResult2.ACQty));
                binding.tvSTQty.setText(numFormatter.format(data.ScanResult2.STQty));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                DecimalFormat numFormatter = new DecimalFormat("###");
                if (data.StrResult != null) {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                if (data.ScanResult2 != null) {
                    if (data.ScanResult2.ListView != null) {
                        adapter.updateAdapter(data.ScanResult2.ListView);
                        scanListViewItemList2 = data.ScanResult2.ListView;
                    }
                    if (data.ScanResult2.BundleNo != null) {
                        binding.tvBundleNo.setText(data.ScanResult2.BundleNo);
                    }
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
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
                if(data.BoolResult){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photos have been saved.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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

        binding.btn1.setOnClickListener(new View.OnClickListener() {//포장추가
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == 0)
                    return;
                ScanBundle("KP-0000000000-01");
            }
        });
        binding.btn2.setOnClickListener(new View.OnClickListener() {//포장제외
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == 0)
                    return;
                ScanBundle("KP-0000000000-02");
            }
        });
        binding.btn3.setOnClickListener(new View.OnClickListener() {//번들출력
            @Override
            public void onClick(View v) {

                String titleKor="출력";
                String titleEng="Print";
                String messageKor="출력작업을 진행하시겠습니까?";
                String messageEng="Are you sure you want to proceed with the output?";
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

                new MaterialAlertDialogBuilder(Activity2100.this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                if (Users.PCCode.equals("")) {
                                    Toast.makeText(Activity2100.this, Users.Language==0 ? "출력 PC가 연결되어 있지 않습니다.": "There is no PC connected.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ScanBundle("KP-0000000000-03");
                                return;
                            }
                        })
                        .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    /**
     * 스캔 인식 (이 액티비티는 별도로 작업한다.)
     */
    private void setResultLauncher() {
        //이것은 서버TAG인식 로직
        //resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);
        //이것은 인식한 TAG 그대로
        resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            ScanBundle(scanResult);
                            return;
                        }

                        if (result.getResultCode() == 100) {

                        }
                    }
                });
    }


    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        ScanBundle(result);
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
     * 공통 끝
     */
}

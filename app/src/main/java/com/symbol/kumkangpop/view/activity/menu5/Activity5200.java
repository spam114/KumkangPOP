package com.symbol.kumkangpop.view.activity.menu5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity5200Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.menu2.Activity2100;
import com.symbol.kumkangpop.view.adapter.Adapter5200;
import com.symbol.kumkangpop.view.dialog.Dialog5320;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class Activity5200 extends BaseActivity {
    Activity5200Binding binding;
    Adapter5200 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    int StockOutType = 0;
    int gLocationNo = 0;
    //String saleOrderNo;
    //String ho;
    //ArrayList<ItemTag> tempItemTagArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity5200);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_5200):getString(R.string.detail_menu_5200_eng));
        gLocationNo = getIntent().getIntExtra("locationNo", -1);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter5200(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView1.setText("Customer");
            binding.textView2.setText("Jobsite");
            binding.textView3.setText("CustomerCode");
            binding.textView4.setText("JobsiteCode");
            binding.textView41.setText("Container");
            binding.textView44.setText("P-Qty");
            binding.textInputLayout.setHint("Customer or Jobsite");

            //binding.btnPrint.setText("TAG Prn");
            binding.btnRePrint.setText("RePrint TAG");
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.LocationNo = gLocationNo;
        sc.LocationName = "";
        sc.StockOutType = StockOutType;
        commonViewModel.Get("GetSalesOrderDataLocation", sc);
    }


    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
                //txtContainerNo.Text = barcode;
                //DataSet nDataSet = npopControl.GetNumConvertData(iConvetDivision, barcode);

                SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 8;//출고번호 5
                sc.Barcode = barcode.Barcode;
                commonViewModel.Get2("GetNumConvertData", sc);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
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
                adapter.updateAdapter(data.CustomerList);
                adapter.getFilter().filter(binding.edtInput.getText().toString());
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if (data.NumConvertDataList.size() == 0) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "해당 입력Tag의 현장정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Enter the Tag field information that can not be found.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                String locationNo = data.NumConvertDataList.get(0).DestNum;
                int StockOrderLocationNo = (int)Double.parseDouble(locationNo);
                ScanStockOutTag(StockOrderLocationNo);

            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                if (data.StrResult!=null)
                {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                else {
                    /*Intent intent = new Intent(getBaseContext(), Activity5110.class);
                    intent.putExtra("containerNo", data.ContainerList.get(0).ContainerNo);
                    intent.putExtra("locationNo", locationNo);
                    activityResultLauncher.launch(intent);*/
                }

            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
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

    public void ScanStockOutTag(int stockOrderLocationNo) {
        boolean bReturn = true;
        int numPrint = 1;
        if(!binding.edt2.getText().toString().equals("")){
            numPrint = Integer.parseInt(binding.edt2.getText().toString());
        }

        String titleKor="출고상차TAG 출력";
        String titleEng="Reprint OUT TAG";
        String messageKor="출력작업을 진행하시겠습니까?\n(출고번호가 생성됩니다.)\n"+"수량: "+numPrint+"\n컨테이너: "+binding.edt1.getText().toString();
        String messageEng="Are you sure you want to proceed with the output?\n(The Release number is generated.)\nQTY: "+numPrint+"\nContainer: "+binding.edt1.getText().toString();
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



        int finalNumPrint = numPrint;
        new MaterialAlertDialogBuilder(this)
                .setTitle(strTitle)
                .setMessage(strMessage)
                .setCancelable(true)
                .setPositiveButton(strPoButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        if (Users.PCCode.equals("")) {
                            Toast.makeText(Activity5200.this, Users.Language==0 ? "출력 PC가 연결되어 있지 않습니다.": "There is no PC connected.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SearchCondition sc = new SearchCondition();
                        sc.NumPrint = finalNumPrint;
                        sc.BReturn = bReturn;
                        sc.StockOutType = StockOutType;
                        sc.GLocationNo = gLocationNo;
                        sc.StockOutLocationNo = gLocationNo;
                        sc.StockOrderLocationNo = stockOrderLocationNo;
                        sc.ContainerNo = binding.edt1.getText().toString();
                        sc.DeptCode = Integer.toString(Users.DeptCode);
                        sc.BusinessClassCode = Users.BusinessClassCode;
                        sc.PCCode = Users.PCCode;
                        sc.UserID = Users.UserID;
                        sc.Language = Users.Language;
                        commonViewModel.Get3("ScanStockOutTag", sc);
                        return;
                    }
                })
                .setNegativeButton(strNegaButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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

        binding.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialog5320 custom = new Dialog5320(gLocationNo);
                custom.show(fm, "");
                fm.executePendingTransactions();//Dialog 가 활성화 되지 않은 상태였기 때문에 getDialog() 를 호출할때 Null 에러가 발생 하기떄문에 이것을 넣어준다.
                custom.setOnDismissListener(dialog -> {
                    binding.edt1.setText(custom.containerNo);
                });
            }
        });

        binding.btnRePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5210.class);
                intent.putExtra("locationNo", gLocationNo);
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

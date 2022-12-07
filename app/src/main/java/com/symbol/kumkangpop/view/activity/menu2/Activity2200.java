package com.symbol.kumkangpop.view.activity.menu2;

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
import com.symbol.kumkangpop.databinding.Activity2200Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.menu0.Activity0120;
import com.symbol.kumkangpop.view.adapter.Adapter2200;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity2200 extends BaseActivity {
    Activity2200Binding binding;
    Adapter2200 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity2200);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_2200):getString(R.string.detail_menu_2200_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter2200(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.LocationNo = Users.LocationNo;
        sc.SaleOrderLocationNo = -1;
        sc.BundleNo = "";
        commonViewModel.Get("GetBundleDataBundleNo", sc);
    }


    private void setView() {
        if (Users.Language == 1) {
            //binding.textInputLayout1.setHint("In Store");
            binding.textView34.setText("BundleNo");//번들번호
            binding.textView35.setText("Customer(Jobsite)");//거래처
            binding.textView37.setText("Block");//동
            binding.textView39.setText("No");//No
            binding.textView40.setText("OrderNo");//주문번호
            binding.textInputLayout.setHint("Customer or Jobsite or BundleNo");
        }
    }

    /*private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SearchDivision = 0;
        sc.LocationNo = locationNo;
        sc.ContainerNo = "";
        commonViewModel.Get("GetContainerData", sc);
    }*/

    public void observerViewModel() {

        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;

                SearchCondition sc = new SearchCondition();
                sc.IConvetDivision = 3;//번들번호
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
                adapter.updateAdapter(data.BundleList);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                if(data.NumConvertDataList.size()==0){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "해당 바코드의 번들정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bundles of bar code information that can not be found.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                String bundleNo = data.NumConvertDataList.get(0).DestNum;
                for (int i = 0; i < adapter.getItemList().size(); i++)
                {
                    if (bundleNo.equals(adapter.getItemList().get(i).BundleNo))
                    {
                        Intent intent = new Intent(getBaseContext(), Activity2300.class);
                        intent.putExtra("bundleNo", bundleNo);
                        activityResultLauncher.launch(intent);
                        return;
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

        /*binding.btn1.setOnClickListener(new View.OnClickListener() {//포장추가
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == 0)
                    return;
                ScanBundle("KP-0000000000-01");
            }
        });*/
        /*binding.btn2.setOnClickListener(new View.OnClickListener() {//포장제외
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == 0)
                    return;
                ScanBundle("KP-0000000000-02");
            }
        });*/
        /*binding.btn3.setOnClickListener(new View.OnClickListener() {//번들출력
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(Activity2200.this)
                        .setTitle("출력")
                        .setMessage("출력작업을 진행하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                if (Users.PCCode.equals("")) {
                                    Toast.makeText(Activity2200.this, "출력 PC가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ScanBundle("KP-0000000000-03");
                                return;
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });*/
        /*binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            ScanBundle(scanResult);
                            return;
                        }

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

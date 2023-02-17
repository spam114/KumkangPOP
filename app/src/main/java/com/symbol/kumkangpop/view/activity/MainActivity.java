package com.symbol.kumkangpop.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityMainBinding;
import com.symbol.kumkangpop.databinding.DialogNoticeBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.BusinessClass;
import com.symbol.kumkangpop.model.object.MainMenuItem;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.BackPressControl;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.PreferenceManager;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.menu0.Activity0010;
import com.symbol.kumkangpop.view.activity.menu2.Activity2300;
import com.symbol.kumkangpop.view.adapter.MainAdapter;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.ScanViewModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    SimpleDataViewModel simpleDataViewModel;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    ScanViewModel scanViewModel;
    BackPressControl backpressed;
    ArrayAdapter businessAdapter;
    MainAdapter mainAdapter;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;
    ArrayList<BusinessClass> businessClassList;

    //SoundManager soundManager; // 효과음 관리 객체

    // 전원버튼 컨트롤러
    //private BroadcastReceiver receiver; // 전원버튼 눌러 기기의 화면이 OFF, ON 되는 것을 확인한다.
    //private IntentFilter intentFilter; // ON,OFF를 등록하는 변수
    //PowerManager powerManager;
    //PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // 화면 꺼짐 방지
        init();

        //test
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        simpleDataViewModel.GetSimpleData("GetNoticeData2");
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        setBar();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        ArrayList<MainMenuItem> menuItemArrayList = getMainMenuItem();
        mainAdapter = new MainAdapter(menuItemArrayList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mainAdapter);
        backpressed = new BackPressControl(this);

        /*if (Users.GboutSourcing) {//외주처의 경우 출고검수 사용금지
            mainAdapter.removeItem(17);
            //mainAdapter.notifyDataSetChanged();
        }*/
    }

    private void setListener() {

        /*binding.button9.setOnClickListener(new View.OnClickListener() {//A급대기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity9000.class);
                startActivity(intent);
            }
        });

        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity1000.class);
                startActivity(intent);
            }
        });*/
    }

    public void observerViewModel() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                //LinkedTreeMap linkedTreeMap = (LinkedTreeMap) data;
                //Object errorCheck = linkedTreeMap.get("ErrorCheck");
                AppVersion appVersion = TypeChanger.changeTypeAppVersion(data);
                Object errorCheck = appVersion.ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                } else {
                    boolean noticeFlag = PreferenceManager.getBoolean(this, "NoShowNotice");
                    //Object remark = linkedTreeMap.get("Remark");
                    Object remark = appVersion.Remark;
                    if (!noticeFlag)
                        viewNotice(remark.toString());
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        simpleDataViewModel.list.observe(this, list -> {
            if (list != null) {
                businessClassList = TypeChanger.changeTypeBusinessClassList(list);
                Object errorCheck = businessClassList.get(0).ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_SHORT).show();
                    Users.SoundManager.playSound(0, 2, 3);//에러
                } else {
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    for (int i = 0; i < businessClassList.size(); i++) {
                        stringArrayList.add((int) businessClassList.get(i).BusinessClassCode + "-" + businessClassList.get(i).CompanyName.replace("금강공업(주)", ""));
                    }
                    final ArrayAdapter adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, stringArrayList);
                    businessAdapter = adapter;

                    MenuItem searchItem = binding.toolbar.getMenu().findItem(R.id.itemBusiness);
                    Spinner yourSpinnerName = (Spinner) searchItem.getActionView();
                    //Log.i("스피너순서", "실행");
                    yourSpinnerName.setAdapter(adapter);

                    yourSpinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//사업장이 변경될때마다, CustomerCode, LocationNo를 바꿔준다.
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Users.BusinessClassCode = (int) businessClassList.get(position).BusinessClassCode;
                            if (!Users.GboutSourcing) {
                                Users.CustomerCode = businessClassList.get(position).CustomerCode;
                            }
                            Users.LocationNo = (int) businessClassList.get(position).LocationNo;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //Log.i("스피너순서", "구성");
                }
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        //에러메시지
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                PreferenceManager.setBoolean(this, "AutoLogin", false);
                PreferenceManager.setString(this, "ID", "");
                PreferenceManager.setString(this, "PW", "");
                progressOFF2();
            }
        });

        simpleDataViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });

        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals("")) return;

                SearchCondition sc = new SearchCondition();
                sc.Barcode = barcode.Barcode;
                sc.BusinessClassCode = Users.BusinessClassCode;
                sc.CustomerCode = Users.CustomerCode;
                scanViewModel.GetScanMain(sc);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        scanViewModel.data.observe(this, data -> {
            if (data != null) {
                int activityFlag = data.ActivityFlag;//1: Activity2300실행, 2: 종료, 3: Activity0010실행 + Get한 주문서 데이터 처리
                if (activityFlag == 1) {//1: Activity2300실행 + barcode
                    String barCode = data.Barcode;
                    Intent intent = new Intent(MainActivity.this, Activity2300.class);
                    intent.putExtra("barCode", barCode);
                    startActivity(intent);
                } else if (activityFlag == 2) {//2: 종료
                    return;
                } else if (activityFlag == 3) {//3: Activity0010실행 + Get한 주문서 데이터 처리(SalesOrderList)

                    Intent intent = new Intent(this, Activity0010.class);
                    intent.putExtra("saleOrderNo", data.SalesOrderList.get(0).SaleOrderNo);
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void viewNotice(String remark) {
        DialogNoticeBinding dialogNoticeBinding = DataBindingUtil.inflate(LayoutInflater.from(getBaseContext()), R.layout.dialog_notice, null, false);
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
        buider.setView(dialogNoticeBinding.getRoot());
        try {
            dialogNoticeBinding.tvTitle.setText("변경사항(version " + getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName + ")");
        } catch (PackageManager.NameNotFoundException e) {
            dialogNoticeBinding.tvTitle.setText("변경사항");
        }
        dialogNoticeBinding.tvContent.setText(remark);
        final AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);////Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.show();
        dialogNoticeBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogNoticeBinding.chkNoView.isChecked()) {
                    PreferenceManager.setBoolean(MainActivity.this, "NoShowNotice", true);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 공용부분
     */

    @Override
    public ArrayList<MainMenuItem> getMainMenuItem() {
        return super.getMainMenuItem();
    }

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setResultLauncher() {

        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);

        /*resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        *//**
         * QR코드 시작
         *//*
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String barcode = intentResult.getContents();
                            Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
                            SearchCondition sc = new SearchCondition();
                            sc.Barcode = barcode;
                            sc.LocationNo = Users.LocationNo;
                            sc.PCCode = Users.PCCode;
                            sc.UserID = Users.UserID;
                            barcodeConvertPrintViewModel.FNBarcodeConvertPrint(sc);
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

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
    }

    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        simpleDataViewModel.GetSimpleDataList("GetBusinessClassData");
        return CommonMethod.onCreateOptionsMenu4(this, menu);
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {//qr코드 리딩 결과
            if (result.getContents() == null) {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                //showErrorDialog(this, "취소 되었습니다.",1);
            } else {
                String scanResult;
                scanResult = result.getContents();
                Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;

        SearchCondition sc = new SearchCondition();
        sc.Barcode = result;
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.CustomerCode = Users.CustomerCode;
        scanViewModel.GetScanMain(sc);
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
    public void onBackPressed() {
        backpressed.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                //intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
                intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
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
     * 공용부분 END
     */
}
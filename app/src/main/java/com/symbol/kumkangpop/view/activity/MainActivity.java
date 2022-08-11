package com.symbol.kumkangpop.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
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
        mainAdapter = new MainAdapter(menuItemArrayList,this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mainAdapter);
        backpressed = new BackPressControl(this);
        if (Users.GboutSourcing) {//외주처의 경우 출고검수 사용금지
            mainAdapter.removeItem(17);
            //mainAdapter.notifyDataSetChanged();
        }
    }


    private void setListener() {
        /*binding.button.setOnClickListener(new View.OnClickListener() {//제품포장
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity0000.class);
                startActivity(intent);
            }
        });

        binding.button9.setOnClickListener(new View.OnClickListener() {//A급대기
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
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_LONG).show();
                } else {
                    boolean noticeFlag = PreferenceManager.getBoolean(this, "NoShowNotice");
                    //Object remark = linkedTreeMap.get("Remark");
                    Object remark = appVersion.Remark;
                    if (!noticeFlag)
                        viewNotice(remark.toString());
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        simpleDataViewModel.list.observe(this, list -> {
            if (list != null) {
                businessClassList = TypeChanger.changeTypeBusinessClassList(list);
                Object errorCheck = businessClassList.get(0).ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel 은 에러처리를 각각의 View에서 처리한다.(각각 다르므로)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_LONG).show();
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
                            Users.BusinessClassCode = (int)businessClassList.get(position).BusinessClassCode;
                            if(!Users.GboutSourcing){
                                Users.CustomerCode = businessClassList.get(position).CustomerCode;
                            }
                            Users.LocationNo = (int)businessClassList.get(position).LocationNo;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //Log.i("스피너순서", "구성");
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //에러메시지
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_LONG).show();
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

        /*barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.equals("")) return;

                int iConvetDivision = 1;
                //주문번호 1
                //포장번호 2
                //번들번호 3
                //이송번호 4
                //출고번호 5
                //컨테이너번호 6
                if (npopControl.GetNumSeekData(3, barcode))
                {
                    //RMwrt2300P nForm = new RMwrt2300P(barcode);
                    //nForm.ShowDialog();
                    // 프로그램 시작시 바코드 모듈 ON
                    //MBBarcode.Power(true);
                    // 바코드 모듈 제어권 시작
                    //MBBarcode.ActiveForm = this;
                    return;
                }

                DataSet nDataSet = npopControl.GetNumConvertData(iConvetDivision, barcode);
                if (nDataSet.Tables[0].Rows.Count == 0)
                {
                    ////nScaner.fnBeepError();
                    return;
                }

                DataTable nDataTable = npopControl.GetSalesOrderData(mclVariable.BusinessClassCode, mclVariable.CustomerCode, nDataSet.Tables[0].Rows[0][0].ToString()).Tables[0];
                if (nDataTable.Rows.Count != 0)
                {
                    //MBBarcode.ActiveForm = null;
                    //RMwrt0010P nForm = new RMwrt0010P(nDataTable.Rows[0]);
                    //nForm.ShowDialog();
                    // 프로그램 시작시 바코드 모듈 ON
                    //MBBarcode.Power(true);
                    // 바코드 모듈 제어권 시작
                    //MBBarcode.ActiveForm = this;

                }

            }
            else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_LONG).show();
            }
        });*/

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
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        /**
                         * QR코드 시작
                         */
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {

                            scanViewModel.GetTest();

                            /*String barcode = intentResult.getContents();
                            Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
                            SearchCondition sc = new SearchCondition();
                            sc.Barcode = barcode;
                            sc.LocationNo = Users.LocationNo;
                            sc.PCCode = Users.PCCode;
                            sc.UserID = Users.UserID;
                            barcodeConvertPrintViewModel.FNBarcodeConvertPrint(sc);*/
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
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher);
    }

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

    /**
     * 공용부분 END
     */
}
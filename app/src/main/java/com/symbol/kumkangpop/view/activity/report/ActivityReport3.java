package com.symbol.kumkangpop.view.activity.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport3Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport3;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReport3 extends BaseActivity {
    ActivityReport3Binding binding;
    AdapterReport3 adapter;
    ReportViewModel reportViewModel;
    //SimpleDataViewModel simpleDataViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;


    //int businessClassCode = 9;
    //String deptCode = "92410";
    //int stockInType = 63;

    //달력 날짜
    public int tyear;
    public int tmonth;
    public int tdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report3);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu7) : getString(R.string.menu7_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new AdapterReport3(new ArrayList<>(), this, resultLauncher);
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        //GetRecyclerViewData(inDate);
        setSpinnerData();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.txtFromDate2.setText("Date");
            binding.txtFromDate3.setText("Type");
            binding.textViewWorkDate4.setText("Item/Size");
            binding.textViewWorkDate.setText("Qty");
            binding.textViewWorkType.setText("Weight");
            binding.textViewWorkType2.setText("ActualWeight");
            binding.textView3.setText("Total");
            binding.txtTotal1.setText("Total");
            binding.txtTotal2.setText("Total2");
            binding.txtTotal3.setText("Total3");
        }
    }

    private void setSpinnerData() {
        ArrayList<String> partFlagList = new ArrayList<>();
        partFlagList.add(Users.Language == 0 ? "규격" : "Spec");
        partFlagList.add(Users.Language == 0 ? "비규격" : "NonSpec");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, partFlagList);

        binding.spinnerSpecFlag.setAdapter(adapter);
        //spinnerLocation.setMinimumWidth(150);
        //spinnerLocation.setDropDownWidth(150);
        binding.spinnerSpecFlag.setSelection(0);

        binding.spinnerSpecFlag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetRecyclerViewData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void GetRecyclerViewData() {
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        SearchCondition sc = new SearchCondition();
        sc.FromDate = inDate;
        sc.ToDate = inDate;
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.SpecFlag = binding.spinnerSpecFlag.getSelectedItemPosition() + 1;
        reportViewModel.Get("GetReport3Data", sc);
    }

    private void setListener() {
        /*binding.rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    adapter.setConfirmFlag(false);
                else
                    adapter.setConfirmFlag(true);
                String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                GetRecyclerViewData(inDate, isChecked);
            }
        });*/
        binding.txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(tyear, tmonth, tdate);
            }
        });
    }

    private void showDateTimePicker(int year, int month, int date) {
        DatePickerDialog dpd = new DatePickerDialog
                (this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view,
                                                  int year, int monthOfYear, int dayOfMonth) {
                                binding.txtFromDate.setText("[ " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " ]");
                                tyear = year;
                                tmonth = monthOfYear;
                                tdate = dayOfMonth;
                                GetRecyclerViewData();
                            }
                        }
                        , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                        //    호출할 리스너 등록
                        year, month, date); // 기본값 연월일
        dpd.show();
    }

    public void observerReport() {
        reportViewModel.dataList.observe(this, dataList -> {
            if (dataList != null) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.

                binding.txtTotal1.setText("0");
                binding.txtTotal2.setText("0");
                binding.txtTotal3.setText("0");
                DecimalFormat numFormatter = new DecimalFormat("###,###");

                double totalInQty = 0;
                totalInQty = dataList.get(dataList.size() - 1).InQty;
                double totalLogicalWeight = 0;
                totalLogicalWeight = dataList.get(dataList.size() - 1).LogicalWeight;
                double totalActualWeight = 0;
                totalActualWeight = dataList.get(dataList.size() - 1).ActualWeight;

                binding.txtTotal1.setText(numFormatter.format(totalInQty));
                binding.txtTotal2.setText(numFormatter.format(totalLogicalWeight));
                binding.txtTotal3.setText(numFormatter.format(totalActualWeight));

                dataList.remove(dataList.size() - 1);
                adapter.updateAdapter((ArrayList<Report>) dataList);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        //에러메시지
        reportViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        reportViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }

    /**
     * 공용부분
     */

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setResultLauncher() {
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
    }

    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    public void getKeyInResult(String result) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return CommonMethod.onCreateOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 1);
    }


    /*private void CheckAQR(String scanResult) {
        SearchCondition sc = new SearchCondition();
        sc.WorderLot = scanResult;
        sc.SearchYear = tyear;
        sc.SearchMonth = tmonth + 1;
        sc.SearchDay = tdate;
        sc.UserID = Users.UserID;
        simpleDataViewModel.GetSimpleData("CheckAQR", sc);
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
     * 공용부분 END
     */

}
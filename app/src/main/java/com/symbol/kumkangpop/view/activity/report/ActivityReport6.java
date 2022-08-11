package com.symbol.kumkangpop.view.activity.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport6Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport6;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReport6 extends BaseActivity {
    ActivityReport6Binding binding;
    AdapterReport6 adapter;
    ReportViewModel reportViewModel;
    //SimpleDataViewModel simpleDataViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report6);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText(getString(R.string.menu16));
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new AdapterReport6(new ArrayList<>(), this, resultLauncher);
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetRecyclerViewData();
    }


    private void GetRecyclerViewData() {
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        SearchCondition sc = new SearchCondition();
        sc.FromDate = inDate;
        sc.ToDate = inDate;
        sc.BusinessClassCode = Users.BusinessClassCode;
        reportViewModel.Get("GetReport6Data", sc);
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

                double totalQty = 0;
                totalQty = dataList.get(dataList.size() - 1).Qty;
                double totalM2 = 0;
                totalM2 = dataList.get(dataList.size() - 1).M2;
                double totalWeight = 0;
                totalWeight = dataList.get(dataList.size() - 1).Weight;
                binding.txtTotal1.setText(numFormatter.format(totalQty));
                binding.txtTotal2.setText(numFormatter.format(totalM2));
                binding.txtTotal3.setText(numFormatter.format(totalWeight));
                dataList.remove(dataList.size() - 1);
                String locationNo = "";
                boolean colorFlag=false;
                try{
                    for(int i=0;i<dataList.size();i++){
                        if (!locationNo.equals(dataList.get(i).LocationNo)){
                            Report report= new Report();
                            report.CustomerName=dataList.get(i).CustomerName;
                            report.LocationName=dataList.get(i).LocationName;
                            locationNo = dataList.get(i).LocationNo;
                            dataList.add(i, report);
                            colorFlag=!colorFlag;
                            dataList.get(i).ColorFlag=colorFlag;
                            continue;
                        }
                        dataList.get(i).ColorFlag=colorFlag;
                    }
                }
                catch (Exception et){
                    String test="4";
                }
                adapter.updateAdapter((ArrayList<Report>) dataList);
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //에러메시지
        reportViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
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
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher);
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

    /**
     * 공용부분 END
     */

}
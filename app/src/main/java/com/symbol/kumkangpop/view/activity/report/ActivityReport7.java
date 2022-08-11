package com.symbol.kumkangpop.view.activity.report;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport7Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport7;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReport7 extends BaseActivity {
    ActivityReport7Binding binding;
    AdapterReport7 adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report7);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        setListener();
        setResultLauncher();
        adapter = new AdapterReport7(new ArrayList<>(), this, resultLauncher);
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        setRadioButton();
        setCheckBox();
        GetRecyclerViewData();
    }

    private void setRadioButton() {
        binding.rbQty.setChecked(true);
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                GetRecyclerViewData();
            }
        });
    }

    private void setCheckBox() {
        binding.chkViewAlForm.setChecked(true);
        binding.chkViewAlForm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetRecyclerViewData();
            }
        });
    }

    private void GetRecyclerViewData() {
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        SearchCondition sc = new SearchCondition();
        sc.FromDate = inDate;
        sc.ToDate = inDate;
        sc.BusinessClassCode = Users.BusinessClassCode;
        boolean weightPrint = false;
        String commodityCode = "-1";
        if (binding.rbWeight.isChecked()) {
            weightPrint = true;
        }
        if (binding.chkViewAlForm.isChecked()) {
            commodityCode = "456071";
        }
        sc.WeightPrint = weightPrint;
        sc.CommodityCode = commodityCode;
        reportViewModel.Get("GetReport7Data", sc);
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
    }

    public void observerReport() {
        reportViewModel.dataList.observe(this, dataList -> {
            if (dataList != null) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                binding.txtTotal1.setText("0");
                binding.txtTotal2.setText("0");
                binding.txtTotal3.setText("0");
                binding.txtTotal4.setText("0");
                binding.txtTotal5.setText("0");
                binding.txtTotal6.setText("0");
                binding.txtTotal7.setText("0");
                DecimalFormat numFormatter = new DecimalFormat("###,###");

                double totalTotalQty = 0;
                double totalCurrentCQty = 0;
                double totalCurrentAwaitQty = 0;
                double totalCurrentASaleQty = 0;
                double totalCurrentAPlanQty = 0;
                double totalCurrentNSaleQty = 0;
                double totalCurrentNPlanQty = 0;

                totalTotalQty = dataList.get(dataList.size() - 1).totalQty;
                totalCurrentCQty = dataList.get(dataList.size() - 1).currentCQty;
                totalCurrentAwaitQty = dataList.get(dataList.size() - 1).currentAwaitQty;
                totalCurrentASaleQty = dataList.get(dataList.size() - 1).currentASaleQty;
                totalCurrentAPlanQty = dataList.get(dataList.size() - 1).currentAPlanQty;
                totalCurrentNSaleQty = dataList.get(dataList.size() - 1).currentNSaleQty;
                totalCurrentNPlanQty = dataList.get(dataList.size() - 1).currentNPlanQty;

                binding.txtTotal1.setText(numFormatter.format(totalTotalQty));
                binding.txtTotal2.setText(numFormatter.format(totalCurrentCQty));
                binding.txtTotal3.setText(numFormatter.format(totalCurrentAwaitQty));
                binding.txtTotal4.setText(numFormatter.format(totalCurrentASaleQty));
                binding.txtTotal5.setText(numFormatter.format(totalCurrentAPlanQty));
                binding.txtTotal6.setText(numFormatter.format(totalCurrentNSaleQty));
                binding.txtTotal7.setText(numFormatter.format(totalCurrentNPlanQty));

                dataList.remove(dataList.size() - 1);
                String partCode = "";
                boolean colorFlag = false;
                try {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (!partCode.equals(dataList.get(i).PartCode)) {
                            Report report = new Report();
                            report.PartCode = dataList.get(i).PartCode;
                            report.PartName = dataList.get(i).PartName;
                            partCode = dataList.get(i).PartCode;
                            dataList.add(i, report);
                            colorFlag = !colorFlag;
                            dataList.get(i).ColorFlag = colorFlag;
                            continue;
                        }
                        dataList.get(i).ColorFlag = colorFlag;
                    }
                } catch (Exception et) {
                    String test = "4";
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
        }, 100000);
        progressON("Loading...", handler);
    }

    /**
     * 공용부분 END
     */

}
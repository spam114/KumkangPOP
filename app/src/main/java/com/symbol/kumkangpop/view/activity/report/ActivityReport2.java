package com.symbol.kumkangpop.view.activity.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport2Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport2;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReport2 extends BaseActivity {
    ActivityReport2Binding binding;
    AdapterReport2 adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report2);
        binding.rbQty.setChecked(true);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu6):getString(R.string.menu6_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new AdapterReport2(new ArrayList<>(), this, resultLauncher);
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        //GetRecyclerViewData(inDate);
        setSpinnerData();
        setRadioButton();
        setFieldState(R.id.rbQty);
    }

    private void setView() {
        if(Users.Language==1){
            binding.txtFromDate2.setText("Date");
            binding.txtFromDate3.setText("Type");
            binding.txtFromDate4.setText("Print");
            binding.rbQty.setText("Qty");
            binding.rbVolumn.setText("Volumn");
            binding.rbWeight.setText("Weight");
            binding.textViewWorkDate4.setText("Item/Size");
            binding.textViewWorkDate.setText("Input");
            binding.textViewWorkType.setText("Repair");
            binding.textViewWorkType2.setText("Damaged");

            binding.textView3.setText("Total");
            binding.txtTotal1.setText("Total");
            binding.txtTotal2.setText("Total2");
            binding.txtTotal3.setText("Total3");
            binding.txtTotal4.setText("Total");
            binding.txtTotal5.setText("Total2");
            binding.txtTotal6.setText("Total3");
            binding.txtTotal7.setText("Total");
            binding.txtTotal8.setText("Total2");
            binding.txtTotal9.setText("Total3");

        }
    }

    private void setRadioButton() {
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFieldState(checkedId);
            }
        });

    }

    private void setFieldState(int index) {
        binding.txtTotal1.setVisibility(View.GONE);
        binding.txtTotal2.setVisibility(View.GONE);
        binding.txtTotal3.setVisibility(View.GONE);
        binding.txtTotal4.setVisibility(View.GONE);
        binding.txtTotal5.setVisibility(View.GONE);
        binding.txtTotal6.setVisibility(View.GONE);
        binding.txtTotal7.setVisibility(View.GONE);
        binding.txtTotal8.setVisibility(View.GONE);
        binding.txtTotal9.setVisibility(View.GONE);

        if (index == R.id.rbQty) {
            binding.txtTotal1.setVisibility(View.VISIBLE);
            binding.txtTotal2.setVisibility(View.VISIBLE);
            binding.txtTotal3.setVisibility(View.VISIBLE);
        } else if (index == R.id.rbWeight) {
            binding.txtTotal4.setVisibility(View.VISIBLE);
            binding.txtTotal5.setVisibility(View.VISIBLE);
            binding.txtTotal6.setVisibility(View.VISIBLE);
        } else if (index == R.id.rbVolumn) {
            binding.txtTotal7.setVisibility(View.VISIBLE);
            binding.txtTotal8.setVisibility(View.VISIBLE);
            binding.txtTotal9.setVisibility(View.VISIBLE);
        }
        adapter.setChekedRadioIndex(index);

        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

    }

    private void setSpinnerData() {
        ArrayList<String> partFlagList = new ArrayList<>();
        partFlagList.add(Users.Language==0 ? "규격": "Spec");
        partFlagList.add(Users.Language==0 ? "비규격": "NonSpec");
        partFlagList.add(Users.Language==0 ? "규격전환": "Spec Change");

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
        reportViewModel.Get("GetReport2Data", sc);
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
                binding.txtTotal4.setText("0");
                binding.txtTotal5.setText("0");
                binding.txtTotal6.setText("0");
                binding.txtTotal7.setText("0");
                binding.txtTotal8.setText("0");
                binding.txtTotal9.setText("0");
                DecimalFormat numFormatter = new DecimalFormat("###,###");

                double totalInQty = 0;
                totalInQty = dataList.get(dataList.size() - 1).InQty;
                double totalInWeight = 0;
                totalInWeight = dataList.get(dataList.size() - 1).InWeight;
                double totalInVolumn = 0;
                totalInVolumn = dataList.get(dataList.size() - 1).InVolumn;
                double totalScrapQty = 0;
                totalScrapQty = dataList.get(dataList.size() - 1).ScrapQty;
                double totalScrapWeight = 0;
                totalScrapWeight = dataList.get(dataList.size() - 1).ScrapWeight;
                double totalScrapVolumn = 0;
                totalScrapVolumn = dataList.get(dataList.size() - 1).ScrapVolumn;
                double totalSettlementQty = 0;
                totalSettlementQty = dataList.get(dataList.size() - 1).SettlementQty;
                double totalSettlementWeight = 0;
                totalSettlementWeight = dataList.get(dataList.size() - 1).SettlementWeight;
                double totalSettlementVolumn = 0;
                totalSettlementVolumn = dataList.get(dataList.size() - 1).SettlementVolumn;

                binding.txtTotal1.setText(numFormatter.format(totalInQty));
                binding.txtTotal2.setText(numFormatter.format(totalScrapQty));
                binding.txtTotal3.setText(numFormatter.format(totalSettlementQty));

                binding.txtTotal4.setText(numFormatter.format(totalInWeight));
                binding.txtTotal5.setText(numFormatter.format(totalScrapWeight));
                binding.txtTotal6.setText(numFormatter.format(totalSettlementWeight));

                binding.txtTotal7.setText(numFormatter.format(totalInVolumn));
                binding.txtTotal8.setText(numFormatter.format(totalScrapVolumn));
                binding.txtTotal9.setText(numFormatter.format(totalSettlementVolumn));

                dataList.remove(dataList.size() - 1);
                adapter.updateAdapter((ArrayList<Report>) dataList);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
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
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher,1);
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
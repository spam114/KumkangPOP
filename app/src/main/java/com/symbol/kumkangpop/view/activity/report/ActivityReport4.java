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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport4Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport4;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReport4 extends BaseActivity {
    ActivityReport4Binding binding;
    AdapterReport4 adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report4);
        binding.rbQty.setChecked(true);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu8):getString(R.string.menu8_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new AdapterReport4(new ArrayList<>(), this, resultLauncher);
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetRecyclerViewData();
        setRadioButton();
        setFieldState(R.id.rbQty);
    }

    private void setView() {
        if(Users.Language==1){
            binding.txtFromDate2.setText("Date");
            binding.txtFromDate4.setText("Print");
            binding.rbQty.setText("Qty");
            binding.rbVolumn.setText("Volumn");
            binding.rbWeight.setText("Weight");
            binding.textViewWorkDate4.setText("Item/Size");
            binding.textView4.setText("NewItem");
            binding.textView5.setText("Repair");

            binding.textViewWorkDate.setText("Original");//순수
            binding.textViewWorkType.setText("Processing");//가공
            binding.textViewWorkType2.setText("Repair");//보수
            binding.textViewWorkType3.setText("Processing/Repair");//가공보수
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
        binding.txtTotal10.setVisibility(View.GONE);
        binding.txtTotal11.setVisibility(View.GONE);
        binding.txtTotal12.setVisibility(View.GONE);

        if (index == R.id.rbQty) {
            binding.txtTotal1.setVisibility(View.VISIBLE);
            binding.txtTotal2.setVisibility(View.VISIBLE);
            binding.txtTotal3.setVisibility(View.VISIBLE);
            binding.txtTotal4.setVisibility(View.VISIBLE);
        } else if (index == R.id.rbWeight) {
            binding.txtTotal5.setVisibility(View.VISIBLE);
            binding.txtTotal6.setVisibility(View.VISIBLE);
            binding.txtTotal7.setVisibility(View.VISIBLE);
            binding.txtTotal8.setVisibility(View.VISIBLE);
        } else if (index == R.id.rbVolumn) {
            binding.txtTotal9.setVisibility(View.VISIBLE);
            binding.txtTotal10.setVisibility(View.VISIBLE);
            binding.txtTotal11.setVisibility(View.VISIBLE);
            binding.txtTotal12.setVisibility(View.VISIBLE);
        }
        adapter.setChekedRadioIndex(index);

        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

    }

    private void GetRecyclerViewData() {
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        SearchCondition sc = new SearchCondition();
        sc.FromDate = inDate;
        sc.ToDate = inDate;
        sc.BusinessClassCode = Users.BusinessClassCode;
        reportViewModel.Get("GetReport4Data", sc);
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
                binding.txtTotal10.setText("0");
                binding.txtTotal11.setText("0");
                binding.txtTotal12.setText("0");
                DecimalFormat numFormatter = new DecimalFormat("###,###");

                double totalQuantity1 = 0;
                totalQuantity1 = dataList.get(dataList.size() - 1).Quantity1;
                double totalQuantity2 = 0;
                totalQuantity2 = dataList.get(dataList.size() - 1).Quantity2;
                double totalQuantity3 = 0;
                totalQuantity3 = dataList.get(dataList.size() - 1).Quantity3;
                double totalQuantity4 = 0;
                totalQuantity4 = dataList.get(dataList.size() - 1).Quantity4;

                double totalLogicalWeight1 = 0;
                totalLogicalWeight1 = dataList.get(dataList.size() - 1).LogicalWeight1;
                double totalLogicalWeight2 = 0;
                totalLogicalWeight2 = dataList.get(dataList.size() - 1).LogicalWeight2;
                double totalLogicalWeight3 = 0;
                totalLogicalWeight3 = dataList.get(dataList.size() - 1).LogicalWeight3;
                double totalLogicalWeight4 = 0;
                totalLogicalWeight4 = dataList.get(dataList.size() - 1).LogicalWeight4;


                double totalVolumn1 = 0;
                totalVolumn1 = dataList.get(dataList.size() - 1).Volumn1;
                double totalVolumn2 = 0;
                totalVolumn2 = dataList.get(dataList.size() - 1).Volumn2;
                double totalVolumn3 = 0;
                totalVolumn3 = dataList.get(dataList.size() - 1).Volumn3;
                double totalVolumn4 = 0;
                totalVolumn4 = dataList.get(dataList.size() - 1).Volumn4;


                binding.txtTotal1.setText(numFormatter.format(totalQuantity1));
                binding.txtTotal2.setText(numFormatter.format(totalQuantity2));
                binding.txtTotal3.setText(numFormatter.format(totalQuantity3));
                binding.txtTotal4.setText(numFormatter.format(totalQuantity4));

                binding.txtTotal5.setText(numFormatter.format(totalLogicalWeight1));
                binding.txtTotal6.setText(numFormatter.format(totalLogicalWeight2));
                binding.txtTotal7.setText(numFormatter.format(totalLogicalWeight3));
                binding.txtTotal8.setText(numFormatter.format(totalLogicalWeight4));

                binding.txtTotal9.setText(numFormatter.format(totalVolumn1));
                binding.txtTotal10.setText(numFormatter.format(totalVolumn2));
                binding.txtTotal11.setText(numFormatter.format(totalVolumn3));
                binding.txtTotal12.setText(numFormatter.format(totalVolumn4));

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
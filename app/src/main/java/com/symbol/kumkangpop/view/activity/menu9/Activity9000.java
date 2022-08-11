package com.symbol.kumkangpop.view.activity.menu9;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity9000Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter9000;
import com.symbol.kumkangpop.viewmodel.RecyclerViewModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A급대기
 */
public class Activity9000 extends BaseActivity {
    Activity9000Binding binding;
    Adapter9000 adapter;
    RecyclerViewModel recyclerViewModel;
    SimpleDataViewModel simpleDataViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;


    //int businessClassCode = 9;
    String deptCode = "92410";
    int stockInType = 63;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity9000);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        recyclerViewModel = new ViewModelProvider(this).get(RecyclerViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText("쇼트생산 등록(A급대기)");
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new Adapter9000(new ArrayList<>(), this, resultLauncher);
        observerRecycler();
        observerSimpleData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        GetRecyclerViewData(inDate, binding.rbNo.isChecked());
    }


    private void GetRecyclerViewData(String inDate, boolean isChecked) {
        SearchCondition sc = new SearchCondition();
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.InDate = inDate;
        sc.DeptCode = deptCode;
        sc.StockInType = stockInType;
        sc.CheckType = (isChecked) ? 0 : 1;
        recyclerViewModel.Get("GetStockInDataPOP", sc);
    }

    private void setListener() {
        binding.rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    adapter.setConfirmFlag(false);
                else
                    adapter.setConfirmFlag(true);
                String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                GetRecyclerViewData(inDate, isChecked);
            }
        });
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

                                String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                                GetRecyclerViewData(inDate, binding.rbNo.isChecked());
                            }
                        }
                        , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                        //    호출할 리스너 등록
                        year, month, date); // 기본값 연월일
        dpd.show();
    }

    public void observerRecycler() {
        recyclerViewModel.dataList.observe(this, dataList -> {
            if (dataList != null) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeStockInList(dataList));
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //에러메시지
        recyclerViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                progressOFF2();
            }
        });

        recyclerViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }


    public void observerSimpleData() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                if (data.toString().equals("success")) {
                    //다시조회
                    String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                    GetRecyclerViewData(inDate, binding.rbNo.isChecked());
                    Toast.makeText(this, "확인 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //에러메시지
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 공용부분
     */

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
                            String scanResult = intentResult.getContents();
                            //scanResult = result.getContents();
                            if (binding.rbNo.isChecked())//미확인 탭에 위치
                                CheckAWaitingQR(scanResult);
                            else {//확인 탭에 위치
                                GoActivity9100(scanResult);
                            }
                            return;
                        }
                        /**
                         * QR코드 끝
                         */


                        if (result.getResultCode() == 100){
                            String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                            GetRecyclerViewData(inDate, binding.rbNo.isChecked());
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

    public void getKeyInResult(String result) {
        if (binding.rbNo.isChecked())//미확인 탭에 위치
            CheckAWaitingQR(result);
        else//확인 탭에 위치
            GoActivity9100(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return CommonMethod.onCreateOptionsMenu2(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher);
    }

    private void GoActivity9100(String result) {
        Intent intent = new Intent(this, Activity9100.class);
        intent.putExtra("result", result);
        resultLauncher.launch(intent);
    }

    private void CheckAWaitingQR(String scanResult) {
        SearchCondition sc = new SearchCondition();
        sc.ShortNo = scanResult;
        sc.SearchYear = tyear;
        sc.SearchMonth = tmonth + 1;
        sc.SearchDay = tdate;
        sc.UserID = Users.UserID;
        simpleDataViewModel.GetSimpleData("CheckAWaitingQR", sc);
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

    /**
     * 공용부분 END
     */

}
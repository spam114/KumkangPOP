package com.symbol.kumkangpop.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityProgressFloorReturnBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Dong;
import com.symbol.kumkangpop.view.adapter.ProgressFloorReturnViewAdapter;
import com.symbol.kumkangpop.viewmodel.ProgressFloorReturnViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

public class ProgressFloorReturnActivity extends BaseActivity {

    ActivityProgressFloorReturnBinding binding;
    ProgressFloorReturnViewModel viewModel;
    ProgressFloorReturnViewAdapter adapter = new ProgressFloorReturnViewAdapter(new ArrayList<>());

    String contractNo = "";
    String customerLocation = "";
    TreeMap<String, Dong> dongTreeMap;
    ArrayList<Dong> dongArrayList;

    public int tyear;
    public int tmonth;
    public int tdate;


    private void startProgress() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF2(this.getClass().getName());
            }
        }, 5000);
        progressON("Loading...", handler);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress_floor_return);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);

        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(tyear, tmonth, tdate);
            }
        });
        String fromDate = tyear + "-" + (tmonth + 1) + "-" + tdate;

        contractNo = getIntent().getStringExtra("contractNo");
        customerLocation = getIntent().getStringExtra("customerLocation");
        binding.tvCustomerLocation.setText(customerLocation);
        viewModel = new ViewModelProvider(this).get(ProgressFloorReturnViewModel.class);
        //viewModel = ViewModelProviders.of(this).get(ProgressFloorReturnViewModel.class);
        SearchCondition searchCondition =  new SearchCondition();
        searchCondition.FromDate = "2022-03-02";
        searchCondition.ContractNo = "C-9-37412-01";
        viewModel.refresh(searchCondition);

        // 리사이클러뷰에 어뎁터를 설정한다.
        /*LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);*/
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        observerViewModel();
    }

    private void observerViewModel() {
        /**
         *  뷰(메인 화면)에 라이브 데이터를 붙인다.
         *  메인 화면에서 관찰할 데이터를 설정한다.
         *  라이브 데이터가 변경됐을 때 변경된 데이터를 가지고 UI를 변경한다.
         */

        viewModel.list.observe(this, models -> {

            // 데이터 값이 변할 때마다 호출된다.
            if (models != null){
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter((ArrayList<Dong>) models);
            }
        });

        viewModel.loadError.observe(this, isError -> {
            // 에러 메세지를 보여준다.
            if (isError != null){
                //Toast.makeText(this, isError, Toast.LENGTH_SHORT).show();
                //listError.setVisibility(isError? View.VISIBLE: View.GONE);
            }
        });

        viewModel.loading.observe(this, isLoading -> {
            if (isLoading!= null){
                // 로딩 중이라는 것을 보여준다.
                // 로딩중일 때 에러 메세지, 국가 리스트는 안 보여준다.
                if (isLoading){
                    startProgress();
                    //listError.setVisibility(View.GONE);
                   // countriesList.setVisibility(View.GONE);
                }
                else{
                    progressOFF();
                }
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
                                String fromDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                                //DATA가져오기
                                //getViewSaleOrderData(fromDate);
                                //getDongProgressFloorReturn(fromDate);
                            }
                        }
                        , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                        //    호출할 리스너 등록
                        year, month, date); // 기본값 연월일
        dpd.show();
    }
}


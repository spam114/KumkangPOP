package com.symbol.kumkangpop.view.activity.report;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityReport5Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Report;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.AdapterReport5;
import com.symbol.kumkangpop.view.adapter.AdapterReport5_2;
import com.symbol.kumkangpop.viewmodel.ReportViewModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SortedMap;
import java.util.TreeMap;

public class ActivityReport5 extends BaseActivity {
    ActivityReport5Binding binding;
    AdapterReport5 adapter;
    AdapterReport5_2 adapter2;
    ReportViewModel reportViewModel;
    SimpleDataViewModel simpleDataViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;
    SortedMap<Integer, String> codeDataList;


    //int businessClassCode = 9;
    //String deptCode = "92410";
    //int stockInType = 63;

    //달력 날짜
    public int tyear;
    public int tmonth;
    public int tdate;
    boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report5);
        binding.rbQty.setChecked(true);
        binding.chkCustomer.setChecked(true);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        tyear = calendar.get(Calendar.YEAR);
        tmonth = calendar.get(Calendar.MONTH);
        tdate = calendar.get(Calendar.DATE);
        binding.txtFromDate.setText("[ " + tyear + "-" + (tmonth + 1) + "-" + tdate + " ]");
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu9) : getString(R.string.menu9_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new AdapterReport5(new ArrayList<>(), this, resultLauncher);
        adapter2 = new AdapterReport5_2(new ArrayList<>(), this, resultLauncher);
        observerSimpleData();
        observerReport();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetSaleType();
        setRadioButton();
        setFieldState(R.id.rbQty);
    }

    private void setView() {
        if(Users.Language==1){
            binding.txtFromDate2.setText("Date");
            binding.chkCustomer.setText("By Customer");
            binding.txtFromDate77.setText("Print");
            binding.rbQty.setText("Qty");
            binding.rbVolumn.setText("Volumn");
            binding.rbWeight.setText("Weight");
            binding.txtFromDate4.setText("Sale Type");
            binding.txtSaleTypeName.setText("Select");
            binding.textViewField1.setText("InvoiceNo");
            binding.textViewField2.setText("ItemType");
            binding.textViewField3.setText("Qty");
            binding.textViewField4.setText("TotalQty");
            binding.textView3.setText("Total");
        }
    }

    private void GetSaleType() {
        SearchCondition sc = new SearchCondition();
        sc.Type = 2;
        simpleDataViewModel.GetCodeData("GetSaleTypeByCodeType", sc);
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
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        binding.txtTotal1.setVisibility(View.GONE);
        binding.txtTotal2.setVisibility(View.GONE);
        binding.txtTotal3.setVisibility(View.GONE);
        binding.txtTotal4.setVisibility(View.GONE);
        binding.txtTotal5.setVisibility(View.GONE);
        binding.txtTotal6.setVisibility(View.GONE);
        binding.txtTotal7.setVisibility(View.GONE);
        binding.txtTotal8.setVisibility(View.GONE);
        binding.txtTotal9.setVisibility(View.GONE);
        if (!binding.chkCustomer.isChecked()) {
            param.weight = 3.5f;
            param2.weight = 2f;
            binding.txtTotal1.setLayoutParams(param2);
            binding.txtTotal2.setLayoutParams(param2);
            binding.txtTotal3.setLayoutParams(param2);
            binding.textView3.setLayoutParams(param);

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
        } else {
            param.weight = 6.5f;
            param2.weight = 3f;
            binding.txtTotal1.setLayoutParams(param2);
            binding.txtTotal2.setLayoutParams(param2);
            binding.txtTotal3.setLayoutParams(param2);
            binding.textView3.setLayoutParams(param);

            if (index == R.id.rbQty) {
                binding.txtTotal1.setVisibility(View.VISIBLE);
                binding.textViewField3.setText(binding.rbQty.getText().toString());
            } else if (index == R.id.rbWeight) {
                binding.txtTotal2.setVisibility(View.VISIBLE);
                binding.textViewField3.setText(binding.rbWeight.getText().toString());
            } else if (index == R.id.rbVolumn) {
                binding.txtTotal3.setVisibility(View.VISIBLE);
                binding.textViewField3.setText(binding.rbVolumn.getText().toString());
            }
            adapter2.setChekedRadioIndex(index);
            //binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setAdapter(adapter2);
        }


    }


    private void GetRecyclerViewData() {

        String saleType = "";

        for (int key : codeDataList.keySet()) {
            saleType += simpleDataViewModel.codeDataList.getValue().get(key).Code + ",";
        }
        saleType = saleType.substring(0, saleType.length() - 1);
        String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
        SearchCondition sc = new SearchCondition();
        sc.FromDate = inDate;
        sc.ToDate = inDate;
        sc.BusinessClassCode = Users.BusinessClassCode;
        sc.SaleType = saleType;

        String apiName = "";
        if (!binding.chkCustomer.isChecked()) {
            apiName = "GetReport5Data";
        } else {
            apiName = "GetReport5Data2";//거래처별보기
        }
        reportViewModel.Get(apiName, sc);
    }

    private void setListener() {
        binding.txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(tyear, tmonth, tdate);
            }
        });

        binding.chkCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //1. 상단 필드명 변경
                //2. RecycleView, Adapter 변경
                //3. 하단 Total 변경
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                if (isChecked) {//거래처별
                    param.weight = 3;
                    binding.textViewField4.setVisibility(View.GONE);
                    binding.textViewField1.setText(Users.Language==0 ? "송장번호": "InvoiceNo");
                    binding.textViewField2.setText(Users.Language==0 ? "품목분류": "ItemType");
                    binding.textViewField3.setText(Users.Language==0 ? "중량": "Weight");
                } else {
                    param.weight = 2;
                    binding.textViewField4.setVisibility(View.VISIBLE);
                    binding.textViewField1.setText(Users.Language==0 ? "품목/규격": "Item/Size");
                    binding.textViewField2.setText(Users.Language==0 ? "보수출고": "Output(Repair)");
                    binding.textViewField3.setText(Users.Language==0 ? "신제출고": "Output(New)");
                }
                param.rightMargin = 6;
                binding.textViewField2.setLayoutParams(param);
                binding.textViewField3.setLayoutParams(param);
                setFieldState(binding.radioGroup.getCheckedRadioButtonId());
                GetRecyclerViewData();
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

                double total_repair_Quantity = 0;
                total_repair_Quantity = dataList.get(dataList.size() - 1).repair_Quantity;
                double total_repair_M2 = 0;
                total_repair_M2 = dataList.get(dataList.size() - 1).repair_M2;
                double total_repair_Kg = 0;
                total_repair_Kg = dataList.get(dataList.size() - 1).repair_Kg;
                double total_new_Quantity = 0;
                total_new_Quantity = dataList.get(dataList.size() - 1).new_Quantity;
                double total_new_M2 = 0;
                total_new_M2 = dataList.get(dataList.size() - 1).new_M2;
                double total_new_Kg = 0;
                total_new_Kg = dataList.get(dataList.size() - 1).new_Kg;
                double total_sum_Quantity = 0;
                total_sum_Quantity = dataList.get(dataList.size() - 1).sum_Quantity;
                double total_sum_M2 = 0;
                total_sum_M2 = dataList.get(dataList.size() - 1).sum_M2;
                double total_sum_Kg = 0;
                total_sum_Kg = dataList.get(dataList.size() - 1).sum_Kg;

                binding.txtTotal1.setText(numFormatter.format(total_repair_Quantity));
                binding.txtTotal2.setText(numFormatter.format(total_new_Quantity));
                binding.txtTotal3.setText(numFormatter.format(total_sum_Quantity));

                binding.txtTotal4.setText(numFormatter.format(total_repair_Kg));
                binding.txtTotal5.setText(numFormatter.format(total_new_Kg));
                binding.txtTotal6.setText(numFormatter.format(total_sum_Kg));

                binding.txtTotal7.setText(numFormatter.format(total_repair_M2));
                binding.txtTotal8.setText(numFormatter.format(total_new_M2));
                binding.txtTotal9.setText(numFormatter.format(total_sum_M2));

                dataList.remove(dataList.size() - 1);
                adapter.updateAdapter((ArrayList<Report>) dataList);
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
            }
        });


        reportViewModel.dataList2.observe(this, dataList -> {
            if (dataList != null) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.

                binding.txtTotal1.setText("0");
                binding.txtTotal2.setText("0");
                binding.txtTotal3.setText("0");
                DecimalFormat numFormatter = new DecimalFormat("###,###");

                double totalQty = 0;
                totalQty = dataList.get(dataList.size() - 1).Qty;
                double totalWeight = 0;
                totalWeight = dataList.get(dataList.size() - 1).Weight;
                double totalM2 = 0;
                totalM2 = dataList.get(dataList.size() - 1).M2;
                binding.txtTotal1.setText(numFormatter.format(totalQty));
                binding.txtTotal2.setText(numFormatter.format(totalWeight));
                binding.txtTotal3.setText(numFormatter.format(totalM2));
                dataList.remove(dataList.size() - 1);
                String locationNo = "";
                boolean colorFlag = false;
                try {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (!locationNo.equals(dataList.get(i).LocationNo)) {
                            Report report = new Report();
                            report.CustomerName = dataList.get(i).CustomerName;
                            report.LocationName = dataList.get(i).LocationName;
                            locationNo = dataList.get(i).LocationNo;
                            dataList.add(i, report);
                            colorFlag = !colorFlag;
                            dataList.get(i).ColorFlag = colorFlag;
                            continue;
                        }
                        locationNo = dataList.get(i).LocationNo;
                        dataList.get(i).ColorFlag = colorFlag;
                    }
                } catch (Exception et) {
                    String test = "4";
                }
                adapter2.updateAdapter((ArrayList<Report>) dataList);
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


    public void observerSimpleData() {
        simpleDataViewModel.codeDataList.observe(this, models -> {
            if (models != null) {
                checkedItems = new boolean[models.size()];
                if (models.size() >= 3) {
                    checkedItems[0] = true;//최초 셋팅
                    checkedItems[1] = true;
                    checkedItems[2] = true;

                    binding.txtSaleTypeName.setText(models.get(0).Name + "," + models.get(1).Name + "," + models.get(2).Name);
                    codeDataList = new TreeMap<>();
                    codeDataList.put(0, models.get(0).Name);
                    codeDataList.put(1, models.get(1).Name);
                    codeDataList.put(2, models.get(2).Name);
                }
                GetRecyclerViewData();
                binding.linearLayout6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean tempItems[] = checkedItems.clone();

                        final AlertDialog.Builder build = new AlertDialog.Builder(ActivityReport5.this);
                        build.create();
                        build.setTitle(Users.Language==0 ? "판매구분 선택": "Select Sale Type");

                        final String[] items = new String[models.size()];
                        for (int i = 0; i < models.size(); i++)
                            items[i] = models.get(i).Name;

                        /*for (int i = 0; i < items.length; i++) {
                            if (btnList.contains(items[i]))
                                checkedItems[i] = true;
                            else
                                checkedItems[i] = false;
                        }*/


                        build.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
                                int cnt = 0;
                                for (int i = 0; i < checkedItems.length; i++) {
                                    if (checkedItems[i])
                                        cnt++;
                                }
                               /* if (cnt == 0) {
                                    Toast.makeText(build.getContext(), "하나이상의 조건을 선택하여야 합니다.", Toast.LENGTH_SHORT);
                                    checkedItems[which] = true;
                                    ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                                }*/
                            }
                        });

                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == DialogInterface.BUTTON_POSITIVE) {

                                } else {
                                    checkedItems = tempItems;
                                    Toast.makeText(ActivityReport5.this, Users.Language==0 ? "취소 되었습니다.": "It's been canceled.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        build.setPositiveButton(Users.Language==0 ? "선택": "OK", listener);
                        build.setNegativeButton(Users.Language==0 ? "취소": "Cancel", listener);
                        AlertDialog dialog = build.create();
                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectCount = 0;
                                for (int i = 0; i < items.length; i++) {
                                    if (checkedItems[i]) {
                                        selectCount++;
                                    }
                                }

                                if (selectCount == 0) {
                                    Toast.makeText(ActivityReport5.this, Users.Language==0 ? "하나 이상의 조건을 선택하여야 합니다.": "At least one condition must be selected.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                codeDataList = new TreeMap<>();
                                String strCodeName = "";

                                for (int i = 0; i < items.length; i++) {
                                    if (checkedItems[i]) {
                                        strCodeName += items[i] + ",";
                                        codeDataList.put(i, items[i]);
                                    }
                                }

                                strCodeName = strCodeName.substring(0, strCodeName.length() - 1);
                                binding.txtSaleTypeName.setText(strCodeName);
                                GetRecyclerViewData();
                                dialog.dismiss();

                                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                            }
                        });

                    }
                });
            }
        });

        /*//에러메시지
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
        });*/
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

    /**
     * 공용부분 END
     */

}
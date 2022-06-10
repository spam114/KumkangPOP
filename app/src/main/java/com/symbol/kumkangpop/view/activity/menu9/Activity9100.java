package com.symbol.kumkangpop.view.activity.menu9;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity9100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.StockIn;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.AWaitingEditModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.text.DecimalFormat;

/**
 * A급대기 Detail
 */
public class Activity9100 extends BaseActivity {
    Activity9100Binding binding;
    SimpleDataViewModel simpleDataViewModel;
    AWaitingEditModel aWaitingEditModel;
    String tagNo;
    StockIn stockIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity9100);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        aWaitingEditModel = new ViewModelProvider(this).get(AWaitingEditModel.class);
        tagNo = getIntent().getStringExtra("result");
        createQRcode(binding.imvQR, tagNo);
        setListener();
        observerSimpleData();
        observerAWaiting();
        GetAWaitingDetail(tagNo);
    }

    private void GetAWaitingDetail(String tagNo) {
        SearchCondition sc = new SearchCondition();
        sc.ShortNo = tagNo;
        simpleDataViewModel.GetSimpleData("GetAWaitingDetail", sc);
    }

    public void createQRcode(ImageView img, String text) {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
        }
    }

    private void setListener() {
        binding.layoutPartName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aWaitingEditModel.GetPart();
                //ShowEditDialog();
            }
        });
        binding.layoutPartSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                sc.PartCode = stockIn.PartCode;
                aWaitingEditModel.GetPartSpec(sc);
                //ShowEditDialog();
            }
        });
        binding.layoutShortNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowEditDialog();
            }
        });
        binding.layoutQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowEditDialog();
            }
        });
        binding.layoutWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowEditDialog();
            }
        });

        /*binding.rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String inDate = tyear + "-" + (tmonth + 1) + "-" + tdate;
                GetRecyclerViewData(inDate, isChecked);
            }
        });
        binding.txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(tyear, tmonth, tdate);
            }
        });*/
    }

    public void observerAWaiting() {
        aWaitingEditModel.partList.observe(this, data -> {
            if (data != null) {
                //데이터 불러오기까지 완료 다이얼로그에찍기
                ShowPartEditDialog();

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        aWaitingEditModel.partSpecList.observe(this, data -> {
            if (data != null) {
                //데이터 불러오기까지 완료 다이얼로그에찍기
                ShowPartSpecEditDialog();

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //에러메시지
        aWaitingEditModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                progressOFF2();
            }
        });

        aWaitingEditModel.loading.observe(this, isLoading -> {
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
                stockIn = TypeChanger.chageTypeStockIn(data);

                if (stockIn.ErrorCheck != null) {
                    Toast.makeText(this, stockIn.ErrorCheck, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    DrawTag(stockIn);
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

    private void DrawTag(StockIn stockIn) {
        DecimalFormat numFormatter = new DecimalFormat("###,###");
        binding.txtPartName.setText(stockIn.PartName);
        binding.txtPartSpec.setText(stockIn.PartSpec);
        binding.txtShortNo.setText(stockIn.ShortNo);
        binding.txtQty.setText(numFormatter.format(stockIn.InQty) + " EA");
        binding.txtWeight.setText(numFormatter.format(stockIn.ActualWeight) + " KG");
        int workingGroup = (int) stockIn.WorkingGroup;
        int workingMachine = (int) stockIn.WorkingMachine;
        String groupName;
        String machineName;
        if (workingGroup == 1)
            groupName = "주간";
        else if (workingGroup == 2)
            groupName = "야간";
        else
            groupName = "";
        if (workingMachine == 1)
            machineName = "1호기";
        else if (workingMachine == 2)
            machineName = "2호기";
        else if (workingMachine == 3)
            machineName = "3호기";
        else
            machineName = "";
        binding.txtWork.setText(groupName + " " + machineName);
    }

    /**
     * 공용부분
     */

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
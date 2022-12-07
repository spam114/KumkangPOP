package com.symbol.kumkangpop.view.activity.menu1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity1100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.model.object.WoPartHist;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.menu9.Activity9100;
import com.symbol.kumkangpop.viewmodel.AEditModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.text.DecimalFormat;

/**
 * A급
 */
public class Activity1100 extends BaseActivity {
    Activity1100Binding binding;
    SimpleDataViewModel simpleDataViewModel;
    AEditModel aEditModel;
    String tagNo;
    WoPartHist woPartHist;
    int selectedPartIndex = 0;
    int selectedPartSpecIndex = 0;
    //List<Part> part

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setResult(100);
        binding = DataBindingUtil.setContentView(this, R.layout.activity1100);
        woPartHist = new WoPartHist();
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        aEditModel = new ViewModelProvider(this).get(AEditModel.class);
        tagNo = getIntent().getStringExtra("result");
        createQRcode(binding.imvQR, tagNo);
        setListener();
        observerSimpleData();
        observerA();
        GetADetail(tagNo);
    }

    private void GetADetail(String tagNo) {
        SearchCondition sc = new SearchCondition();
        sc.WorderLot = tagNo;
        simpleDataViewModel.GetSimpleData("GetADetail", sc);
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
        binding.layoutQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog(1);
            }
        });
        binding.layoutBundle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog(2);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                sc.WorderLot = woPartHist.WorderLot;
                sc.UserID = Users.UserID;
                sc.WorksOrderNo = woPartHist.WorksOrderNo;
                sc.WorderRequestNo = woPartHist.WorderRequestNo;
                sc.PartCode = woPartHist.PartCode;
                sc.PartSpec= woPartHist.PartSpec;
                sc.MSpec = woPartHist.MSpec;
                sc.ProductionType = (int)woPartHist.ProductionType;
                sc.ReceivedQty = woPartHist.ReceivedQty;
                sc.AssetsType = (int)woPartHist.AssetsType;
                sc.WorkDate = woPartHist.WorkDate;
                sc.BundelQty = woPartHist.BundelQty;

                aEditModel.UpdateADetail(sc);
            }
        });
    }

    private void ShowEditDialog(int type) {
        String titleName;
        String msg;
        String hint;
        if (type == 1) {//번들순번
            titleName = "포장수량 입력";
            msg = "포장수량을 입력해주세요.";
            hint = "수량";
        } else {//중량
            titleName = "번들순번 입력";
            msg = "번들순번을 입력해주세요.";
            hint = "순번";
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_key_in, null);
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        final AlertDialog dialog = buider.create();
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        //Dialog 보이기
        dialog.show();
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(titleName);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        TextInputLayout textInputLayout = dialogView.findViewById(R.id.textInputLayout);
        textInputLayout.setHint(hint);
        TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
        edtTagNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String output = edtTagNo.getText().toString();

                if (output.equals("")) {
                    Toast.makeText(Activity1100.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 1) {//포장수량 확인 버튼
                    double num = Double.parseDouble(output);

                    if(num<=0){
                        Toast.makeText(Activity1100.this, "수량은 0보다 커야합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(woPartHist.ReceivedQty-num >10 || woPartHist.ReceivedQty-num < -10){
                        Toast.makeText(Activity1100.this, "포장수량은 ±10개까지 변경 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    woPartHist.ReceivedQty = num;
                    binding.txtQty.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                } else {//번들순번 확인 버튼
                    woPartHist.BundelQty = Double.parseDouble(output);
                    binding.txtWorkDate.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                }
                DrawTag(woPartHist);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void observerA() {

        aEditModel.resultMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                ChangeColorWhite();
                progressOFF2();
            }
        });

        //에러메시지
        aEditModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                progressOFF2();
            }
        });

        aEditModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }

    private void ChangeColorWhite() {
        binding.txtQty.setBackgroundColor(Color.WHITE);
        binding.txtWorkDate.setBackgroundColor(Color.WHITE);
    }

    public void observerSimpleData() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                woPartHist = TypeChanger.changeTypeWoPartHist(data);
                if (woPartHist.ErrorCheck != null) {
                    Toast.makeText(this, woPartHist.ErrorCheck, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    DrawTag(woPartHist);
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();                finish();
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

    private void DrawTag(WoPartHist woPartHist) {
        binding.setWopartHist(woPartHist);
        DecimalFormat numFormatter = new DecimalFormat("###,###");
        binding.txtQty.setText(numFormatter.format(woPartHist.ReceivedQty) + " EA");
        String workDate;
        try{
            workDate = woPartHist.WorkDate.split("-")[1]+"/"+woPartHist.WorkDate.split("-")[2];
        }
        catch (Exception e){
            workDate="";
        }
        binding.txtWorkDate.setText(workDate+" ("+numFormatter.format(woPartHist.BundelQty)+")");
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
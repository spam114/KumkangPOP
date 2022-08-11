package com.symbol.kumkangpop.view.activity.menu9;

import android.content.DialogInterface;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity9100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Part;
import com.symbol.kumkangpop.model.object.PartSpec;
import com.symbol.kumkangpop.model.object.StockIn;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.AWaitingEditModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A급대기 Detail
 */
public class Activity9100 extends BaseActivity {
    Activity9100Binding binding;
    SimpleDataViewModel simpleDataViewModel;
    AWaitingEditModel aWaitingEditModel;
    String tagNo;
    StockIn stockIn;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity9100);
        stockIn = new StockIn();
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
            }
        });
        binding.layoutPartSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                sc.PartCode = stockIn.PartCode;
                aWaitingEditModel.GetPartSpec(sc);
            }
        });
        binding.layoutShortNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.layoutQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog(1);
            }
        });
        binding.layoutWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog(2);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCondition sc = new SearchCondition();
                sc.StockInNo = stockIn.StockInNo;
                sc.PartCode = stockIn.PartCode;
                sc.PartSpec = stockIn.PartSpec;
                sc.InQty = stockIn.InQty;
                sc.ActualWeight = stockIn.ActualWeight;
                sc.ShortNo = stockIn.ShortNo;
                sc.WorkingGroup = stockIn.WorkingGroup;
                sc.WorkingMachine = stockIn.WorkingMachine;
                aWaitingEditModel.UpdateShortData(sc);
            }
        });
    }

    private void ShowEditDialog(int type) {
        String titleName;
        String msg;
        String hint;
        if (type == 1) {//수량
            titleName = "수량 입력";
            msg = "수량을 입력해주세요.";
            hint = "수량";
        } else {//중량
            titleName = "중량 입력";
            msg = "중량을 입력해주세요.";
            hint = "중량";
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
                    Toast.makeText(Activity9100.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 1) {//수량 확인 버튼
                    if(Double.parseDouble(output)<=0){
                        Toast.makeText(Activity9100.this, "수량은 0보다 커야합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    stockIn.InQty = Double.parseDouble(output);
                    binding.txtQty.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                } else {//중량 확인 버튼
                    stockIn.ActualWeight = Double.parseDouble(output);
                    binding.txtWeight.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                }
                DrawTag(stockIn);
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

    public void observerAWaiting() {
        aWaitingEditModel.partList.observe(this, data -> {
            if (data != null) {
                //데이터 불러오기까지 완료 다이얼로그에찍기
                ShowPartEditDialog(data);

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        aWaitingEditModel.partSpecList.observe(this, data -> {
            if (data != null) {
                //데이터 불러오기까지 완료 다이얼로그에찍기
                ShowPartSpecEditDialog(data);

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        aWaitingEditModel.resultMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                ChangeColorWhite();
                progressOFF2();
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

    private void ChangeColorWhite(){
        binding.txtQty.setBackgroundColor(Color.WHITE);
        binding.txtWeight.setBackgroundColor(Color.WHITE);
        binding.txtPartSpec.setBackgroundColor(Color.WHITE);
        binding.txtPartName.setBackgroundColor(Color.WHITE);
    }

    private void ShowPartEditDialog(List<Part> list) {

        CharSequence[] partNameSequences;

        partNameSequences = new CharSequence[list.size()];
        for (int i = 0; i < list.size(); i++) {
            partNameSequences[i] = list.get(i).PartName;
        }

        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("품명을 선택하세요")
                .setSingleChoiceItems(partNameSequences, selectedPartIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPartIndex = which;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        //listview.setFilterText("SPP BPE");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String partCode = list.get(selectedPartIndex).PartCode;
                        String partName = list.get(selectedPartIndex).PartName;

                        stockIn.PartCode = partCode;
                        stockIn.PartName = partName;

                        SearchCondition sc = new SearchCondition();
                        sc.PartCode = partCode;
                        binding.txtPartName.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                        aWaitingEditModel.GetPartSpec(sc);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void ShowPartSpecEditDialog(List<PartSpec> list) {

        CharSequence[] partSpecNameSequences;

        partSpecNameSequences = new CharSequence[list.size()];
        for (int i = 0; i < list.size(); i++) {
            partSpecNameSequences[i] = list.get(i).PartSpec;
        }

        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("규격을 선택하세요")
                .setSingleChoiceItems(partSpecNameSequences, selectedPartSpecIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPartSpecIndex = which;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        //listview.setFilterText("SPP BPE");
                        dialog.dismiss();
                    }
                })
                /*.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })*/
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String partSpec = list.get(selectedPartSpecIndex).PartSpec;
                        stockIn.PartSpec = partSpec;
                        binding.txtPartSpec.setBackgroundColor(Color.parseColor("#FFF5F5DC"));
                        DrawTag(stockIn);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void observerSimpleData() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                stockIn = TypeChanger.changeTypeStockIn(data);

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
        binding.setStockIn(stockIn);
        DecimalFormat numFormatter = new DecimalFormat("###,###");
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
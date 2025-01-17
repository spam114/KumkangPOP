package com.symbol.kumkangpop.view.activity.menu9;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity9100Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Part;
import com.symbol.kumkangpop.model.object.PartSpec;
import com.symbol.kumkangpop.model.object.StockIn;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.AWaitingEditModel;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.text.DecimalFormat;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import com.symbol.kumkangpop.view.MC3300X;

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
    MC3300X mc3300X;
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
        SetMC3300X();
        tagNo = getIntent().getStringExtra("result");
        createQRcode(binding.imvQR, tagNo);
        setView();
        setListener();
        observerSimpleData();
        observerAWaiting();
        GetAWaitingDetail(tagNo);
    }

    private void setView() {
        if(Users.Language==1){
            binding.btnSave.setText("SAVE");
        }
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
        binding.layoutWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog2();
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
            titleName = Users.Language==0 ? "수량 입력": "Enter quantity";
            msg = Users.Language==0 ? "수량을 입력해 주세요": "Please enter quantity";
            hint = Users.Language==0 ? "수량": "Quantity";
        } else{//중량
            titleName = Users.Language==0 ? "중량 입력": "Enter weight";
            msg = Users.Language==0 ? "중량을 입력해 주세요": "Please enter weight";
            hint = Users.Language==0 ? "중량": "Weight";
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
                        Toast.makeText(Activity9100.this, Users.Language==0 ? "수량은 0보다 커야합니다.": "Quantity must be greater than zero.", Toast.LENGTH_SHORT).show();
                        Users.SoundManager.playSound(0, 2, 3);//에러
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

    private void ShowEditDialog2() {
        String titleName= Users.Language==0 ? "주/야간 입력": "Enter Day/Night";
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_day_night, null);
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
        RadioButton rbDay = dialogView.findViewById(R.id.rbDay);
        rbDay.setText(Users.Language==0 ? "주간": "Day");
        RadioButton rbNight = dialogView.findViewById(R.id.rbNight);
        rbNight.setText(Users.Language==0 ? "야간": "Night");

        if(stockIn.WorkingGroup == 1)//주간
            rbDay.setChecked(true);
        else//야간
            rbNight.setChecked(true);

        Button btnOK = dialogView.findViewById(R.id.btnOK);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbDay.isChecked()){
                    stockIn.WorkingGroup = 1;
                }
                else{
                    stockIn.WorkingGroup = 2;
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
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        aWaitingEditModel.partSpecList.observe(this, data -> {
            if (data != null) {
                //데이터 불러오기까지 완료 다이얼로그에찍기
                ShowPartSpecEditDialog(data);

            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
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
                Users.SoundManager.playSound(0, 2, 3);//에러
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
                .setTitle(Users.Language==0 ? "품명을 선택하세요.": "Please select a item.")
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
                .setNegativeButton(Users.Language==0 ? "취소": "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(Users.Language==0 ? "확인": "OK", new DialogInterface.OnClickListener() {
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
                .setTitle(Users.Language==0 ? "규격을 선택하세요.": "Please select size.")
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
                .setPositiveButton(Users.Language==0 ? "확인": "OK", new DialogInterface.OnClickListener() {
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
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    finish();
                } else {
                    DrawTag(stockIn);
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        //에러메시지
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
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
            groupName = Users.Language==0 ? "주간": "daytime";
        else if (workingGroup == 2)
            groupName = Users.Language==0 ? "야간": "night time";
        else
            groupName = "";
        if (workingMachine == 1)
            machineName = Users.Language==0 ? "1호기": "Unit1";
        else if (workingMachine == 2)
            machineName = Users.Language==0 ? "2호기": "Unit2";
        else if (workingMachine == 3)
            machineName = Users.Language==0 ? "3호기": "Unit3";
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
                //resultLauncher.launch(intentIntegrator.createScanIntent());
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SetMC3300X() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        this.mc3300X = new MC3300X(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"), RECEIVER_EXPORTED);
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"), RECEIVER_EXPORTED);
        } else {
            registerReceiver(mc3300GetReceiver, new IntentFilter("mycustombroadcast"));
            registerReceiver(mc3300GetReceiver, new IntentFilter("scan.rcv.message"));
        }
        mc3300X.registerReceivers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = "";
            if(intent.getAction().equals("mycustombroadcast")){
                result = bundle.getString("mcx3300result");
            }
            else if(intent.getAction().equals("scan.rcv.message")){
                result = bundle.getString("barcodeData");
            }
            if (result.equals(""))
                return;
            getKeyInResult(result);
        }
    };

    /**
     * 공용부분 END
     */

}
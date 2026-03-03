package com.symbol.kumkangpop.view.activity.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityProcessBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ProdProcess;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.MC3300X;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.MainActivity;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

public class ProcessActivity extends BaseActivity {
    ActivityProcessBinding binding;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    MC3300X mc3300X;
    String SaleOrderNo;
    String CustomerCode;
    String CustomerName;
    int LocationNo;
    String LocationName;
    String Dong;
    String CommodityName;
    String DeliveryDate;
    private ActivityResultLauncher<Intent> directQRResultLauncher;//QR 직접 호출을 위한 ResultLauncher

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_process);
        //barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu25) : getString(R.string.menu25_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
    }

    private void observerViewModel() {
        commonViewModel.data.observe(this, data -> {

            ProdProcess prodProcess1 = data.ProdProcessList.get(0);
            ProdProcess prodProcess2 = data.ProdProcessList.get(1);
            ProdProcess prodProcess3 = data.ProdProcessList.get(2);

            binding.txt12.setText(getStateText(prodProcess1.State));
            binding.btn1.setTag(prodProcess1);

            binding.txt13.setText(getStateText(prodProcess2.State));
            binding.btn2.setTag(prodProcess2);

            binding.txt14.setText(getStateText(prodProcess3.State));
            binding.btn3.setTag(prodProcess3);

            if (prodProcess1.State.equals("미진행")) {
                binding.btn1.setText(Users.Language == 0 ? "시작" : "Start");
                binding.btn1.setAlpha(1f);
                //binding.btn1.setAlpha(0.4f);
                //binding.btn1.setEnabled(true);
            } else if (prodProcess1.State.equals("진행중")) {
                binding.btn1.setText(Users.Language == 0 ? "종료" : "Finish");
                binding.btn1.setAlpha(1f);
            } else if (prodProcess1.State.equals("완료")) {
                binding.btn1.setText(Users.Language == 0 ? "재시작" : "Restart");
                binding.btn1.setAlpha(0.4f);
            }

            if (prodProcess2.State.equals("미진행")) {
                binding.btn2.setText(Users.Language == 0 ? "시작" : "Start");
                binding.btn2.setAlpha(1f);
                //binding.btn1.setEnabled(true);
            } else if (prodProcess2.State.equals("진행중")) {
                binding.btn2.setText(Users.Language == 0 ? "종료" : "Finish");
                binding.btn2.setAlpha(1f);
            } else if (prodProcess2.State.equals("완료")) {
                binding.btn2.setText(Users.Language == 0 ? "재시작" : "Restart");
                binding.btn2.setAlpha(0.4f);
                //binding.btn1.setAlpha(0.4f);
            }

            if (prodProcess3.State.equals("미진행")) {
                binding.btn3.setText(Users.Language == 0 ? "시작" : "Start");
                binding.btn3.setAlpha(1f);
                //binding.btn1.setEnabled(true);
            } else if (prodProcess3.State.equals("진행중")) {
                binding.btn3.setText(Users.Language == 0 ? "종료" : "Finish");
                binding.btn3.setAlpha(1f);
            } else if (prodProcess3.State.equals("완료")) {
                binding.btn3.setText(Users.Language == 0 ? "재시작" : "Restart");
                binding.btn3.setAlpha(0.4f);
                //binding.btn1.setAlpha(0.4f);
            }
        });

        commonViewModel.data2.observe(this, data -> {
            String str = "";

            // 공정명 선택 (기존 로직 유지)
            if (data.IntResult == 1)
                str += binding.txt7.getText().toString();
            else if (data.IntResult == 2)
                str += binding.txt8.getText().toString();
            else if (data.IntResult == 3)
                str += binding.txt9.getText().toString();

            // ---- 언어별 처리 ----
            String action = data.StrResult;   // 시작 / 종료 / 재시작

            if (Users.Language == 1) {
                // 영어 변환
                if ("시작".equals(action)) action = "started";
                else if ("종료".equals(action)) action = "finished";
                else if ("재시작".equals(action)) action = "restarted";

                str += " process " + action + ".";
            } else {
                // 한국어
                str += " 공정이 " + action + "되었습니다.";
            }

            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            // 성공 시 데이터 갱신
            SearchCondition sc = new SearchCondition();
            sc.SaleOrderNo = SaleOrderNo;
            commonViewModel.Get("GetProdProcess", sc);
        });


        commonViewModel.data3.observe(this, data -> {
            if (data != null) {

                setView2(data.WorderList.get(0).SaleOrderNo,
                        data.WorderList.get(0).CustomerCode,
                        data.WorderList.get(0).CustomerName,
                        data.WorderList.get(0).LocationNo,
                        data.WorderList.get(0).LocationName,
                        data.WorderList.get(0).Dong,
                        data.WorderList.get(0).CommodityName,
                        data.WorderList.get(0).DeliveryDate
                        );

                //startActivity(intent);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        commonViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });
    }

    private String getStateText(String state) {
        if (Users.Language == 0) return state; // 한국어면 원본 그대로 표시

        // 영어 표시용(원본 state 값은 절대 변경하지 않음)
        if ("미진행".equals(state)) return "Not started";
        if ("진행중".equals(state)) return "In progress";
        if ("완료".equals(state))   return "Completed";

        return state; // 예외/알 수 없는 값은 그대로
    }

    private void setView() {
        SaleOrderNo = getIntent().getStringExtra("saleOrderNo");
        CustomerCode = getIntent().getStringExtra("customerCode");
        CustomerName = getIntent().getStringExtra("customerName");
        LocationNo = getIntent().getIntExtra("locationNo", -1);
        LocationName = getIntent().getStringExtra("locationName");
        Dong = getIntent().getStringExtra("dong");
        CommodityName = getIntent().getStringExtra("commodityName");
        DeliveryDate = getIntent().getStringExtra("deliveryDate");

        binding.tv1.setText(SaleOrderNo);
        binding.tv2.setText(CustomerName);
        binding.tv3.setText(LocationName);
        binding.tv4.setText(Dong);
        binding.tv5.setText(DeliveryDate);
        binding.tvCommodity.setText(CommodityName);

        if (Users.Language == 1) {
            binding.txt1.setText("Order No");
            binding.txt2.setText("Supplier");
            binding.txt3.setText("Site");
            binding.txt4.setText("Block");
            binding.txtCommodity.setText("Type");
            binding.txt5.setText("Finish Date");
            binding.txt6.setText("Step");
            binding.txt7.setText("Cutting");
            binding.txt8.setText("Welding");
            binding.txt9.setText("Packing");
            binding.txt10.setText("Start/Finish");
            binding.txt11.setText("Status");
            binding.txt12.setText("Status");
            binding.txt13.setText("Status");
            binding.txt14.setText("Status");
            binding.btn1.setText("Start");
            binding.btn2.setText("Start");
            binding.btn3.setText("Start");
        }

        SearchCondition sc = new SearchCondition();
        sc.SaleOrderNo = SaleOrderNo;
        commonViewModel.Get("GetProdProcess", sc);
    }

    /**
     * Activity 내에서 QR스캔, 입력으로 데이터를 다시 받는다.
     */
    private void setView2(String SaleOrderNo, String CustomerCode, String CustomerName,
                          int LocationNo, String LocationName, String Dong, String CommodityName, String DeliveryDate) {

        this.SaleOrderNo = SaleOrderNo;
        this.CustomerCode = CustomerCode;
        this.CustomerName = CustomerName;
        this.LocationNo = LocationNo;
        this.LocationName = LocationName;
        this.Dong = Dong;
        this.CommodityName = CommodityName;
        this.DeliveryDate = DeliveryDate;

        binding.tv1.setText(SaleOrderNo);
        binding.tv2.setText(CustomerName);
        binding.tv3.setText(LocationName);
        binding.tv4.setText(Dong);
        binding.tv5.setText(DeliveryDate);
        binding.tvCommodity.setText(CommodityName);

        if (Users.Language == 1) {
            binding.txt1.setText("Order No");
            binding.txt2.setText("Supplier");
            binding.txt3.setText("Site");
            binding.txt4.setText("Block");
            binding.txtCommodity.setText("Type");
            binding.txt5.setText("Finish Date");
            binding.txt6.setText("Step");
            binding.txt7.setText("Cutting");
            binding.txt8.setText("Welding");
            binding.txt9.setText("Packing");
            binding.txt10.setText("Start/Finish");
            binding.txt11.setText("Status");
            binding.txt12.setText("Status");
            binding.txt13.setText("Status");
            binding.txt14.setText("Status");
            binding.btn1.setText("Start");
            binding.btn2.setText("Start");
            binding.btn3.setText("Start");
        }

        SearchCondition sc = new SearchCondition();
        sc.SaleOrderNo = SaleOrderNo;
        commonViewModel.Get("GetProdProcess", sc);
    }

    private void setListener() {
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdProcess prodProcess = (ProdProcess) binding.btn1.getTag();
                String state = prodProcess.State; // "미진행" / "진행중" / "완료"
                boolean isKor = (Users.Language == 0);

// ---- 상태별 분기 ----
                boolean isNotStarted = "미진행".equals(state);
                boolean isWorking = "진행중".equals(state);
                boolean isDone = "완료".equals(state);

// ---- 문구 정의 ----
                String titleKor, titleEng;
                String msgKor, msgEng;

// 버튼 텍스트 기본
                String okKor = "확인";
                String okEng = "OK";
                String cancelKor = "취소";
                String cancelEng = "Cancel";

// 완료 상태에서만 Positive 버튼 텍스트 변경
                String restartKor = "재시작";
                String restartEng = "Restart";

// 상태별 타이틀/메시지
                if (isNotStarted) {
                    titleKor = "시작";
                    titleEng = "Start";

                    msgKor = "작업을 시작하시겠습니까?";
                    msgEng = "Do you want to start the work?";

                } else if (isWorking) {
                    titleKor = "종료";
                    titleEng = "Finish";

                    msgKor = "작업을 종료하시겠습니까?";
                    msgEng = "Do you want to finish the work?";

                } else if (isDone) {
                    titleKor = "재시작";
                    titleEng = "Restart";

                    msgKor = "작업이 완료 상태입니다.\n재시작 하시겠습니까?\n(기존 작업 정보는 삭제됩니다)";
                    msgEng = "This work is already completed.\nDo you want to restart?\n(Existing work data will be deleted.)";

                } else {
                    titleKor = "알림";
                    titleEng = "Notice";

                    msgKor = "알 수 없는 상태값입니다: " + state;
                    msgEng = "Unknown state: " + state;
                }

                // ---- 최종 선택 ----
                String strTitle = isKor ? titleKor : titleEng;
                String strMessage = isKor ? msgKor : msgEng;

                // Positive 버튼: 완료 상태면 재시작/Restart, 아니면 확인/OK
                String strPoButton = isDone
                        ? (isKor ? restartKor : restartEng)
                        : (isKor ? okKor : okEng);

                String strNegaButton = isKor ? cancelKor : cancelEng;

                // ---- 다이얼로그 ----
                new MaterialAlertDialogBuilder(ProcessActivity.this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, (dialog, which) -> {
                            SearchCondition sc = new SearchCondition();
                            sc.SaleOrderNo = SaleOrderNo;
                            sc.InsertType = state;
                            sc.Code = prodProcess.Code;
                            sc.UserID = Users.UserID;
                            sc.DeviceType = 2;//안드로이드 Type 2
                            commonViewModel.Get2("InsertProdProcess", sc);
                            /*if (isNotStarted) {//미진행
                            } else if (isWorking) {//진행중
                            } else if (isDone) {//완료
                            } else {
                                // unknown state -> no-op or log
                            }*/
                        })
                        .setNegativeButton(strNegaButton, (dialog, which) -> {
                            // 취소 처리
                        })
                        .show();
            }
        });
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdProcess prodProcess = (ProdProcess) binding.btn2.getTag();
                String state = prodProcess.State; // "미진행" / "진행중" / "완료"
                boolean isKor = (Users.Language == 0);

// ---- 상태별 분기 ----
                boolean isNotStarted = "미진행".equals(state);
                boolean isWorking = "진행중".equals(state);
                boolean isDone = "완료".equals(state);

// ---- 문구 정의 ----
                String titleKor, titleEng;
                String msgKor, msgEng;

// 버튼 텍스트 기본
                String okKor = "확인";
                String okEng = "OK";
                String cancelKor = "취소";
                String cancelEng = "Cancel";

// 완료 상태에서만 Positive 버튼 텍스트 변경
                String restartKor = "재시작";
                String restartEng = "Restart";

// 상태별 타이틀/메시지
                if (isNotStarted) {
                    titleKor = "시작";
                    titleEng = "Start";

                    msgKor = "작업을 시작하시겠습니까?";
                    msgEng = "Do you want to start the work?";

                } else if (isWorking) {
                    titleKor = "종료";
                    titleEng = "Finish";

                    msgKor = "작업을 종료하시겠습니까?";
                    msgEng = "Do you want to finish the work?";

                } else if (isDone) {
                    titleKor = "완료";
                    titleEng = "Completed";

                    msgKor = "작업이 완료 상태입니다.\n재시작 하시겠습니까?\n(기존 작업 정보는 삭제됩니다)";
                    msgEng = "This work is already completed.\nDo you want to restart?\n(Existing work data will be deleted.)";

                } else {
                    titleKor = "알림";
                    titleEng = "Notice";

                    msgKor = "알 수 없는 상태값입니다: " + state;
                    msgEng = "Unknown state: " + state;
                }

                // ---- 최종 선택 ----
                String strTitle = isKor ? titleKor : titleEng;
                String strMessage = isKor ? msgKor : msgEng;

                // Positive 버튼: 완료 상태면 재시작/Restart, 아니면 확인/OK
                String strPoButton = isDone
                        ? (isKor ? restartKor : restartEng)
                        : (isKor ? okKor : okEng);

                String strNegaButton = isKor ? cancelKor : cancelEng;

                // ---- 다이얼로그 ----
                new MaterialAlertDialogBuilder(ProcessActivity.this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, (dialog, which) -> {
                            SearchCondition sc = new SearchCondition();
                            sc.SaleOrderNo = SaleOrderNo;
                            sc.InsertType = state;
                            sc.Code = prodProcess.Code;
                            sc.UserID = Users.UserID;
                            sc.DeviceType = 2;//안드로이드 Type 2
                            commonViewModel.Get2("InsertProdProcess", sc);
                            /*if (isNotStarted) {//미진행
                            } else if (isWorking) {//진행중
                            } else if (isDone) {//완료
                            } else {
                                // unknown state -> no-op or log
                            }*/
                        })
                        .setNegativeButton(strNegaButton, (dialog, which) -> {
                            // 취소 처리
                        })
                        .show();
            }
        });
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdProcess prodProcess = (ProdProcess) binding.btn3.getTag();
                String state = prodProcess.State; // "미진행" / "진행중" / "완료"
                boolean isKor = (Users.Language == 0);

// ---- 상태별 분기 ----
                boolean isNotStarted = "미진행".equals(state);
                boolean isWorking = "진행중".equals(state);
                boolean isDone = "완료".equals(state);

// ---- 문구 정의 ----
                String titleKor, titleEng;
                String msgKor, msgEng;

// 버튼 텍스트 기본
                String okKor = "확인";
                String okEng = "OK";
                String cancelKor = "취소";
                String cancelEng = "Cancel";

// 완료 상태에서만 Positive 버튼 텍스트 변경
                String restartKor = "재시작";
                String restartEng = "Restart";

// 상태별 타이틀/메시지
                if (isNotStarted) {
                    titleKor = "시작";
                    titleEng = "Start";

                    msgKor = "작업을 시작하시겠습니까?";
                    msgEng = "Do you want to start the work?";

                } else if (isWorking) {
                    titleKor = "종료";
                    titleEng = "Finish";

                    msgKor = "작업을 종료하시겠습니까?";
                    msgEng = "Do you want to finish the work?";

                } else if (isDone) {
                    titleKor = "완료";
                    titleEng = "Completed";

                    msgKor = "작업이 완료 상태입니다.\n재시작 하시겠습니까?\n(기존 작업 정보는 삭제됩니다)";
                    msgEng = "This work is already completed.\nDo you want to restart?\n(Existing work data will be deleted.)";

                } else {
                    titleKor = "알림";
                    titleEng = "Notice";

                    msgKor = "알 수 없는 상태값입니다: " + state;
                    msgEng = "Unknown state: " + state;
                }

                // ---- 최종 선택 ----
                String strTitle = isKor ? titleKor : titleEng;
                String strMessage = isKor ? msgKor : msgEng;

                // Positive 버튼: 완료 상태면 재시작/Restart, 아니면 확인/OK
                String strPoButton = isDone
                        ? (isKor ? restartKor : restartEng)
                        : (isKor ? okKor : okEng);

                String strNegaButton = isKor ? cancelKor : cancelEng;

                // ---- 다이얼로그 ----
                new MaterialAlertDialogBuilder(ProcessActivity.this)
                        .setTitle(strTitle)
                        .setMessage(strMessage)
                        .setCancelable(true)
                        .setPositiveButton(strPoButton, (dialog, which) -> {
                            SearchCondition sc = new SearchCondition();
                            sc.SaleOrderNo = SaleOrderNo;
                            sc.InsertType = state;
                            sc.Code = prodProcess.Code;
                            sc.UserID = Users.UserID;
                            sc.DeviceType = 2;//안드로이드 Type 2
                            commonViewModel.Get2("InsertProdProcess", sc);
                            /*if (isNotStarted) {//미진행
                            } else if (isWorking) {//진행중
                            } else if (isDone) {//완료
                            } else {
                                // unknown state -> no-op or log
                            }*/
                        })
                        .setNegativeButton(strNegaButton, (dialog, which) -> {
                            // 취소 처리
                        })
                        .show();
            }
        });
    }

    /**
     * 공통 시작
     */
    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return CommonMethod.onCreateOptionsMenu2(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 2);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //result.getResultCode()를 통하여 결과값 확인
                if (result.getResultCode() == RESULT_OK) {
                    //ToDo
                }
                if (result.getResultCode() == RESULT_CANCELED) {
                    //ToDo
                }
            }
    );

    /**
     * 스캔 인식
     */
    private void setResultLauncher() {
        resultLauncher =  registerForActivityResult(
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
                            getWordersData(scanResult);
                            return;
                        }
                        /**
                         * QR코드 끝
                         */
                        if (result.getResultCode() == 100) {
                            //GetMainData("");
                        }
                    }
                });

        directQRResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String barcode = intentResult.getContents();
                            getWordersData(barcode);
                            return;
                        }
                        if (result.getResultCode() == 100) {

                        }
                    }
                });
    }

    private void getWordersData(String result){
        String barcode = result;
        SearchCondition sc = new SearchCondition();
        sc.SaleOrderNo = barcode;
        commonViewModel.Get3("GetWordersData", sc);
    }

    @Override
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        getWordersData(result);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리 off
                //intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리 on
                intentIntegrator.setPrompt(this.getString(R.string.qr_state_common));
                intentIntegrator.setOrientationLocked(true);
                //intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
                //intentIntegrator.initiateScan();
                intentIntegrator.setRequestCode(7);
                directQRResultLauncher.launch(intentIntegrator.createScanIntent());
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
    protected void onPause() {
        super.onPause();
        mc3300X.unRegisterReceivers();
        unregisterReceiver(mc3300GetReceiver);
    }

    BroadcastReceiver mc3300GetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = "";
            if (intent.getAction().equals("mycustombroadcast")) {
                result = bundle.getString("mcx3300result");
            } else if (intent.getAction().equals("scan.rcv.message")) {
                result = bundle.getString("barcodeData");
            }
            if (result.equals(""))
                return;
            //CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
        }
    };

    /**
     * 공통 끝
     */
}
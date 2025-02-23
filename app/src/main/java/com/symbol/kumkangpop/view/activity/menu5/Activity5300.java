package com.symbol.kumkangpop.view.activity.menu5;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity5300Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.Adapter5300;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import com.symbol.kumkangpop.view.MC3300X;

public class Activity5300 extends BaseActivity {
    Activity5300Binding binding;
    Adapter5300 adapter;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    List<ScanListViewItem2> scanListViewItemList2 = new ArrayList<>();
    File filePath;

    int stockOutType = 0;
    int locationNo = 0;
    String packingNo = "";

    public String tempPackingNo = "";
    MC3300X mc3300X;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity5300);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_5300) : getString(R.string.detail_menu_5300_eng));
        locationNo = getIntent().getIntExtra("locationNo", -1);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        setFilePath();
        adapter = new Adapter5300(new ArrayList<>(), this, resultLauncher, barcodeConvertPrintViewModel, cameraResultLauncher, filePath);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetMainData();
    }

    private void setFilePath() {
        try {
            //저장소 사용시
            //String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            //File dir = new File(dirPath);

            //캐쉬(임시) 저장소 사용시
            File tempDir = getCacheDir();

            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            filePath = File.createTempFile("IMG", ".jpg", tempDir);
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
        } catch (Exception et) {
            et.getMessage();
        }
    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        commonViewModel.Get("GetStockOutDataCnt", sc);
    }

    private void GetStockInFlag() {
        SearchCondition sc = new SearchCondition();
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        commonViewModel.Get3("GetStockInFlag", sc);
    }

    private void setView() {

        if (Users.Language == 1) {
            //binding.textInputLayout1.setHint("In Store");
            binding.textView32.setText("Invty-OutNo");
            binding.tv1.setText("AL");
            binding.tv2.setText("AC");
            binding.tv3.setText("ST");

            binding.textView34.setText("PackingNo");//포장번호
            binding.textView35.setText("Customer(Jobsite)");//거래처
            binding.textView37.setText("Block");//동
            binding.textView38.setText("Zone");//세대
            binding.textView39.setText("No");//No
            binding.textView40.setText("OrderNo");//주문번호
        }
    }

    /*private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SearchDivision = 0;
        sc.LocationNo = locationNo;
        sc.ContainerNo = "";
        commonViewModel.Get("GetContainerData", sc);
    }*/

    public void ScanStockOut(String barcode) {
        SearchCondition sc = new SearchCondition();
        sc.ScanList2 = scanListViewItemList2;
        sc.IConvetDivision = 5;
        sc.Barcode = barcode;
        sc.PackingNo = binding.tvPackingNo.getText().toString();
        sc.StockOutNo = binding.tvStockOutNo.getText().toString();
        sc.GPackingNo = packingNo;
        sc.ALQty = Double.parseDouble(binding.tvALQty.getText().toString());
        sc.ACQty = Double.parseDouble(binding.tvACQty.getText().toString());
        sc.STQty = Double.parseDouble(binding.tvSTQty.getText().toString());

        sc.LocationNo = locationNo;
        sc.StockOutType = stockOutType;
        sc.UserID = Users.UserID;
        sc.Language = Users.Language;


        commonViewModel.Get2("ScanStockOut", sc);
    }

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (filePath != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        try {
                            InputStream in = new FileInputStream(filePath);
                            BitmapFactory.decodeStream(in, null, options);
                            in.close();
                            in = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //final int width = options.outWidth;
                        //final int height = options.outHeight;
                        // width, height 값에 따라 inSaampleSize 값 계산

                        BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());


                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(filePath.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap bmRotated = rotateBitmap(bitmap, orientation);

                        try {
                            byte[] byteArray = null;
                            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
                            byteArray = bStream.toByteArray();

                            SearchCondition sc = new SearchCondition();
                            sc.PackingNo = tempPackingNo;
                            sc.Image = compressImage2(Base64.encodeToString(byteArray, Base64.DEFAULT));
                            //sc.ImageName = sc.ContainerNo+"_"+
                            sc.UserID = Users.UserID;
                            commonViewModel.Get4("InsertStockOutPictureData", sc);

                        } catch (Exception et) {
                            et.printStackTrace();
                        }


                    }
                }
            });

    private String compressImage2(String jsonString) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1; // 1/4배율로 읽어오게 하는 방법

        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);

        int targetWidth = 1000; // your arbitrary fixed limit
        int targetHeight = (int) (decodedByte.getHeight() * targetWidth / (double) decodedByte.getWidth());
        /*
        options = new BitmapFactory.Options();
        options.outHeight = targetHeight;
        options.outWidth = 480;
        */
        //decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
        Bitmap resized = Bitmap.createScaledBitmap(decodedByte, targetWidth, targetHeight, true);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 80, bStream);
        byte[] byteArray = bStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void observerViewModel() {
        //바코드 스캔 후 동작
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode.Result != null) {//제품출고 작업완료 혹은 작업완료 취소
                Toast.makeText(this, barcode.Result, Toast.LENGTH_SHORT).show();
                binding.tvStockOutNo.setText(barcode.Barcode);//출고번호
                return;

            }

            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;

                //찍은것을 또찍으면 삭제할것인지 물어본다.
                for (int i = 0; i < adapter.getItemList().size(); i++) {
                    if (adapter.getItem(i).PackingNo.equalsIgnoreCase(barcode.Barcode)) {
                        String title = "작업목록 제외";
                        String message = "작업목록에서 제외하시겠습니까?\n" + barcode.Barcode;
                        String okString = "확인";
                        String noString = "취소";

                        if (Users.Language != 0) {
                            title = "Exclude Task List";
                            message = "Are you sure you want to exclude it from the task list?\n" + barcode.Barcode;
                            okString = "Confirm";
                            noString = "Cancel";
                        }

                        new MaterialAlertDialogBuilder(this)
                                .setTitle(title)
                                .setMessage(message)
                                .setCancelable(true)
                                .setPositiveButton(okString, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ScanStockOut(barcode.Barcode);
                                    }
                                })
                                .setNegativeButton(noString, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        return;
                    }
                }

                ScanStockOut(barcode.Barcode);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        //에러메시지
        barcodeConvertPrintViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                progressOFF2();
            }
        });

        barcodeConvertPrintViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중
                    startProgress();
                } else {//로딩끝
                    progressOFF2();
                }
            }
        });

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                DecimalFormat numFormatter = new DecimalFormat("###");

                binding.tvALQty.setText(numFormatter.format(data.ScanResult2.ALQty));
                binding.tvACQty.setText(numFormatter.format(data.ScanResult2.ACQty));
                binding.tvSTQty.setText(numFormatter.format(data.ScanResult2.STQty));
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                DecimalFormat numFormatter = new DecimalFormat("###");
                if (data.StrResult != null) {
                    Toast.makeText(this, data.StrResult, Toast.LENGTH_SHORT).show();
                }
                if (data.ScanResult2 != null) {
                    if (data.ScanResult2.PackingNo != null) {
                        binding.tvPackingNo.setText(data.ScanResult2.PackingNo);
                    }
                    if (data.ScanResult2.ListView != null) {
                        adapter.updateAdapter(data.ScanResult2.ListView);
                        scanListViewItemList2 = data.ScanResult2.ListView;
                    }
                    if (data.ScanResult2.StockOutNo != null) {
                        binding.tvStockOutNo.setText(data.ScanResult2.StockOutNo);
                    }
                    if (data.ScanResult2.GPackingNo != null) {
                        packingNo = data.ScanResult2.GPackingNo;
                    }
                    binding.tvALQty.setText(numFormatter.format(data.ScanResult2.ALQty));
                    binding.tvACQty.setText(numFormatter.format(data.ScanResult2.ACQty));
                    binding.tvSTQty.setText(numFormatter.format(data.ScanResult2.STQty));
                }


            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                int stockInFlag = data.IntResult;
                if (stockInFlag == 1) {//출고완료
                    if (Users.Language == 1) {
                        binding.tvState.setText("completed");
                    } else {
                        binding.tvState.setText("완료");
                    }
                    binding.tvState.setTextColor(Color.BLUE);
                } else {//미완료
                    if (Users.Language == 1) {
                        binding.tvState.setText("uncompleted");
                    } else {
                        binding.tvState.setText("미완료");
                    }
                    binding.tvState.setTextColor(Color.BLACK);
                }

            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data4.observe(this, data -> {
            if (data != null) {
                if (data.BoolResult) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photos have been saved.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                }

            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });


        //에러메시지
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

    private void setListener() {
        /**
         * forfilter
         */
        /*binding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnCreateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5110.class);
                intent.putExtra("containerNo", "");
                intent.putExtra("locationNo", locationNo);
                activityResultLauncher.launch(intent);
            }
        });*/

        binding.tvStockOutNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {//출고번호가 있으면 기타정보 버튼 활성화
                    binding.btnEtc.setVisibility(View.VISIBLE);
                    binding.textView36.setVisibility(View.VISIBLE);
                    binding.tvState.setVisibility(View.VISIBLE);
                    GetStockInFlag();
                } else {
                    binding.btnEtc.setVisibility(View.GONE);
                    binding.textView36.setVisibility(View.GONE);
                    binding.tvState.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity5310.class);
                intent.putExtra("stockOutNo", binding.tvStockOutNo.getText().toString());
                intent.putExtra("locationNo", locationNo);
                activityResultLauncher.launch(intent);
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

    /**
     * 스캔 인식 (이 액티비티는 별도로 작업한다.)
     */
    private void setResultLauncher() {
        //이것은 서버TAG인식 로직
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);
        //이것은 인식한 TAG 그대로
        /*resultLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        *//**
         * QR코드 시작
         *//*
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            Scanning(scanResult);
                            return;
                        }
                        *//**
         * QR코드 끝
         *//*
                        if (result.getResultCode() == 100) {

                        }
                    }
                });*/
    }


    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                GetMainData();
                //result.getResultCode() 기본값은 0
                /*if (result.getResultCode() == RESULT_OK) {//-1
                }
                if (result.getResultCode() == RESULT_CANCELED) {//0
                }*/
            }
    );

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

    BroadcastReceiver mc3300GetReceiver = new BroadcastReceiver() {
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
            CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
        }
    };

    /**
     * 공통 끝
     */
}

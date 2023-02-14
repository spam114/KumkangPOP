package com.symbol.kumkangpop.view.activity.menu5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Activity5110Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Container;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 제품포장
 */
public class Activity5110 extends BaseActivity {
    Activity5110Binding binding;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    String containerNo;
    int locationNo;
    File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity5110);
        barcodeConvertPrintViewModel = new ViewModelProvider(this).get(BarcodeConvertPrintViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.detail_menu_5110):getString(R.string.detail_menu_5110_eng));
        containerNo = getIntent().getStringExtra("containerNo");
        locationNo = getIntent().getIntExtra("locationNo", -1);
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        setFilePath();
        GetMainData(Users.LocationNo, containerNo);
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

    private void setView() {
        if (Users.Language == 1) {
            binding.textView.setText("ContainerNo");

            binding.textView2.setText("SilNo");
            binding.tvBlank.setText("AAAAA");

            binding.textView3.setText("TonWeight");
            binding.tvBlank2.setText("AA");

            binding.textView4.setText("CarNo");
            binding.tvBlank3.setText("AAAA");

            binding.btnImage.setText("Camera");
            binding.btnSave.setText("Save");
        }

        binding.edtContainerNo.setText(containerNo);
        binding.edtContainerNo2.setText(containerNo);
    }

    private void GetMainData(int locationNo, String containerNo) {

        if (!containerNo.equals("")) {
            SearchCondition sc = new SearchCondition();
            sc.SearchDivision = 0;
            sc.LocationNo = locationNo;
            sc.ContainerNo = containerNo;
            commonViewModel.Get("GetContainerData", sc);
        } else {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String getTime = sdf.format(date);
            binding.edtOutDate.setText(getTime);
            binding.radioButton.setChecked(true);
        }
    }

    public void observerViewModel() {
        barcodeConvertPrintViewModel.data.observe(this, barcode -> {
            if (barcode != null) {
                if (barcode.Barcode.equals(""))
                    return;
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });


        commonViewModel.data.observe(this, data -> {
            if (data != null) {

                ArrayList<Container> tempArrayList = data.ContainerList;
                if (tempArrayList.size() != 0) {
                    binding.edtOutDate.setText(tempArrayList.get(0).OutDate);
                    binding.edtSealNo.setText(tempArrayList.get(0).SilNo);

                    binding.edtContainerNo.setText(tempArrayList.get(0).ContainerNo);
                    binding.edtContainerNo2.setText(tempArrayList.get(0).ContainerNo);

                    if ((int) tempArrayList.get(0).ContainerTon == 40) {
                        binding.radioButton.setChecked(true);
                    } else {
                        binding.radioButton2.setChecked(true);
                    }
                    binding.edtCarNumber.setText(tempArrayList.get(0).CarNumber);

                    if (!tempArrayList.get(0).StockOutNo.equals("")) {
                        binding.edtContainerNo.setEnabled(false);
                        binding.edtContainerNo.setClickable(false);
                    }
                }

                /*SearchCondition sc = new SearchCondition();
                sc.CustomerCode = Users.CustomerCode;
                saleOrderNo = sc.SaleOrderNo;
                commonViewModel.Get2("GetSalesOrderDataHo", sc);*/

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(TypeChanger.changeTypeSalesOrderList(dataList));*/
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {

                if (data.ContainerList.size() > 0) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "이미 등록된 컨테이너번호입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Is the number of containers that have already been registered.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    return;
                }
                saveContainer();
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                boolean bbool = data.BoolResult;
                if (bbool) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Normal has been registered.", Toast.LENGTH_SHORT).show();
                    }

                    GetMainData(Users.LocationNo, binding.edtContainerNo.getText().toString());
                    /*if (nDataSet.Tables[0].Rows.Count != 0)
                    {
                        txtContainerNo.Text = nDataSet.Tables[0].Rows[0]["ContainerNo"].ToString();
                        txtContainerNo2.Text = nDataSet.Tables[0].Rows[0]["ContainerNo"].ToString();
                        txtOutDate.Text = nDataSet.Tables[0].Rows[0]["OutDate"].ToString();
                        txtSilNo.Text = nDataSet.Tables[0].Rows[0]["SilNo"].ToString();
                        if (decimal.Parse(nDataSet.Tables[0].Rows[0]["ContainerTon"].ToString()) == 40)
                        {
                            rdoCheck1.Checked = true;
                        }
                        else
                        {
                            rdoCheck2.Checked = true;
                        }
                        txtCarNumber.Text = nDataSet.Tables[0].Rows[0]["CarNumber"].ToString();
                    }*/
                } else {
                    ////nScaner.fnBeepError();
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                }
                //Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data4.observe(this, data -> {
            if (data != null) {
                if(data.BoolResult){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Photos have been saved.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                }
            } else {
                Toast.makeText(this, Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
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

    private void saveContainer() {
        double ContainerTon;

        if (binding.radioButton.isChecked())
            ContainerTon = 40;
        else
            ContainerTon = 20;

        SearchCondition sc = new SearchCondition();
        sc.ContainerNo = binding.edtContainerNo.getText().toString();
        sc.OutDate = binding.edtOutDate.getText().toString();
        sc.SilNo = binding.edtSealNo.getText().toString();
        sc.ContainerTon = ContainerTon;
        sc.CarNumber = binding.edtCarNumber.getText().toString();
        sc.UserID = Users.UserID;
        sc.ContainerNo2 = binding.edtContainerNo2.getText().toString();
        commonViewModel.Get3("SetContainerData", sc);
    }

    private void setListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtContainerNo.getText().toString().equals("")) {
                    if (Users.Language == 0) {
                        Toast.makeText(getBaseContext(), "컨테이너번호를 입력하시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Please enter the containerNo.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    return;
                }

                if (binding.edtContainerNo2.getText().toString().equals("")) {
                    SearchCondition sc = new SearchCondition();
                    sc.SearchDivision = 0;
                    sc.LocationNo = locationNo;
                    sc.ContainerNo = binding.edtContainerNo.getText().toString();
                    commonViewModel.Get2("GetContainerData", sc);
                } else {
                    saveContainer();
                }
            }
        });

        binding.btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtContainerNo2.getText().toString().equals("")){
                    if (Users.Language == 0) {
                        Toast.makeText(getBaseContext(), "컨테이너 정보를 먼저 저장해 주십시요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Please store the container information first.", Toast.LENGTH_SHORT).show();
                    }
                    Users.SoundManager.playSound(0, 2, 3);//에러
                    return;
                }
                startCamera();
            }
        });

    }

    private void startCamera() {
        try {
            Uri photoUri = FileProvider.getUriForFile(getBaseContext(), getApplication().getPackageName() + ".fileprovider" , filePath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));

            cameraResultLauncher.launch(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    //Intent intent = result.getData();
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

                        try{
                            byte[] byteArray = null;
                            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
                            byteArray = bStream.toByteArray();


                            SearchCondition sc = new SearchCondition();
                            sc.ContainerNo = binding.edtContainerNo.getText().toString();
                            sc.OutDate = binding.edtOutDate.getText().toString();
                            sc.Image = compressImage2(Base64.encodeToString(byteArray, Base64.DEFAULT));
                            //sc.ImageName = sc.ContainerNo+"_"+
                            sc.UserID = Users.UserID;
                            commonViewModel.Get4("InsertContainerPictureData", sc);
                        }
                        catch (Exception et){
                            et.printStackTrace();
                        }


                    }
                }
            });

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

    /**
     * 스캔 인식
     */
    private void setResultLauncher() {
        //QR런처
        resultLauncher = CommonMethod.FNBarcodeConvertPrint(this, barcodeConvertPrintViewModel);

        //Camera런처

    }

    @Override
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        CommonMethod.FNBarcodeConvertPrint(result, barcodeConvertPrintViewModel);
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
     * 공통 끝
     */
}

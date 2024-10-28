package com.symbol.kumkangpop.view.activity.rawmaterial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityTransferRawMaterialBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.MC3300X;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.adapter.TransferRawMaterialAdapter;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

/**
 * 제품포장
 */
public class TransferRawMaterialActivity extends BaseActivity {
    ActivityTransferRawMaterialBinding binding;
    TransferRawMaterialAdapter adapter;
    CommonViewModel commonViewModel;
    private ActivityResultLauncher<Intent> resultLauncher;//QR ResultLauncher
    private FloatingNavigationView mFloatingNavigationView;
    MC3300X mc3300X;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_raw_material);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        SetMC3300X();
        binding.txtTitle.setText(Users.Language == 0 ? getString(R.string.menu23) : getString(R.string.menu23_eng));
        setView();
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        adapter = new TransferRawMaterialAdapter(new ArrayList<>(), this, resultLauncher, commonViewModel);
        observerViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        GetBusinessClassDataAll();
    }

    private void setView() {
        if (Users.Language == 1) {
            binding.textView24.setText("Company");//사업장
            binding.textView51.setText("Please scan the raw material TAG.");
            binding.textViewWorkDate.setText("TAG NO");
            binding.textViewWorkDate2.setText("Location");
            binding.textViewWorkDate3.setText("Part");
            binding.textViewWorkDate4.setText("Spec");
            binding.textViewWorkDate5.setText("Qty");
            binding.textViewWorkDate6.setText("StockoutNo");
        }
    }

    private void GetBusinessClassDataAll() {
        SearchCondition sc = new SearchCondition();
        commonViewModel.Get("GetBusinessClassDataAll", sc);
    }

    private void GetMainData(String itemTag) {
        SearchCondition sc = new SearchCondition();
        int businessClassCode = 0;
        try {
            businessClassCode = Integer.parseInt(binding.comboBusiness.getText().toString().split("-")[0]);
        } catch (Exception et) {
            Users.SoundManager.playSound(0, 2, 3);//에러
            return;
        }
        sc.BusinessClassCode = businessClassCode;
        sc.ItemTag = itemTag;
        commonViewModel.Get2("GetTransferData", sc);
    }

    public void observerViewModel() {

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                String[] items = new String[data.BusinessClassList.size()];
                String initBusiness = "";
                for (int i = 0; i < items.length; i++) {
                    items[i] = (int) data.BusinessClassList.get(i).BusinessClassCode + "-" + data.BusinessClassList.get(i).CompanyName.replace("금강공업(주)", "");
                    if (Users.BusinessClassCode == data.BusinessClassList.get(i).BusinessClassCode) {
                        initBusiness = items[i];
                    }
                }

                ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, R.layout.item_list, items);
                binding.comboBusiness.setAdapter(itemAdapter);
                binding.comboBusiness.setInputType(0);
                binding.comboBusiness.setText(initBusiness, false);

                GetMainData("");

                /*binding.recyclerView.setVisibility(View.VISIBLE);
                // 어뎁터가 리스트를 수정한다.
                adapter.updateAdapter(data.PackingList);*/
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
                finish();
            }
        });

        commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                adapter.updateAdapter(data.StockOut3DetailList, data.StrResult);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                GetMainData(data.StrResult);
            } else {
                Toast.makeText(this, Users.Language == 0 ? "서버 연결 오류" : "Server connection error", Toast.LENGTH_SHORT).show();
                Users.SoundManager.playSound(0, 2, 3);//에러
            }
        });

        commonViewModel.data4.observe(this, data -> {//창고 가져온 후
            if (data != null) {
                final int[] locationNo = {-1};
                final String[] locationName = {"-1"};

                final CharSequence[] locationSequences= new CharSequence[data.LocationList.size()];
                for(int i=0;i<data.LocationList.size();i++){
                    locationSequences[i] = data.LocationList.get(i).LocationName;
                }

                String title="창고 선택";
                String okButton = "예";
                String cancelButton = "아니요";

                if(Users.Language != 0){
                    title = "Select Location";
                    okButton = "YES";
                    cancelButton = "NO";
                }

                new MaterialAlertDialogBuilder(TransferRawMaterialActivity.this)
                        .setTitle(title)
                        .setSingleChoiceItems(locationSequences, Users.selectedLocationIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Users.WorkClassCode=workClassArrayList.get(which).WorkClassCode;
                                //Users.WorkClassName=workClassArrayList.get(which).WorkClassName;
                                Users.selectedLocationIndex = which;
                                locationNo[0] = data.LocationList.get(Users.selectedLocationIndex).LocationNo;
                                locationName[0] = data.LocationList.get(Users.selectedLocationIndex).LocationName;
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InsertTransferRawMaterial(data.StrResult, locationNo[0]);
                            }
                        })
                        .setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                //InsertTransferRawMaterial(String itemTag)
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
        });*/

        binding.comboBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetMainData("");
            }
        });
    }

    private void InsertTransferRawMaterial(String itemTag, int locationNo) {
        SearchCondition sc = new SearchCondition();
        int businessClassCode = 0;
        try {
            businessClassCode = Integer.parseInt(binding.comboBusiness.getText().toString().split("-")[0]);
        } catch (Exception et) {
            Users.SoundManager.playSound(0, 2, 3);//에러
            return;
        }
        sc.BusinessClassCode = businessClassCode;
        sc.ItemTag = itemTag;
        sc.StockOutType = 11;//11:창고이동, 12:투입불출
        sc.LocationNo = locationNo;
        sc.ServiceType = Users.ServiceType;
        sc.UserID = Users.UserID;
        commonViewModel.Get3("InsertStockOut3POP", sc);
    }

    private void GetLocation(String itemTag){
        SearchCondition sc = new SearchCondition();
        int businessClassCode = 0;
        try {
            businessClassCode = Integer.parseInt(binding.comboBusiness.getText().toString().split("-")[0]);
        } catch (Exception et) {
            Users.SoundManager.playSound(0, 2, 3);//에러
            return;
        }
        sc.BusinessClassCode = businessClassCode;
        sc.ItemTag = itemTag;
        commonViewModel.Get4("GetLocation", sc);
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
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher, 1);
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
                            GetLocation(scanResult);
                            return;
                        }
                        /**
                         * QR코드 끝
                         */
                        if (result.getResultCode() == 100) {
                            GetMainData("");
                        }
                    }
                });
    }
    public void getKeyInResult(String result) {
        if (result.equals(""))
            return;
        GetLocation("EI-" + result);
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
            GetLocation(result);
        }
    };

    /**
     * 공통 끝
     */
}

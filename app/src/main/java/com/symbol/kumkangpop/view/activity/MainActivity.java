package com.symbol.kumkangpop.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.ActivityMainBinding;
import com.symbol.kumkangpop.databinding.DialogNoticeBinding;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.BusinessClass;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.BackPressControl;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.view.PreferenceManager;
import com.symbol.kumkangpop.view.TypeChanger;
import com.symbol.kumkangpop.view.activity.menu0.Activity0000;
import com.symbol.kumkangpop.view.activity.menu1.Activity1000;
import com.symbol.kumkangpop.view.activity.menu9.Activity9000;
import com.symbol.kumkangpop.viewmodel.SimpleDataViewModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    SimpleDataViewModel simpleDataViewModel;
    BackPressControl backpressed;
    ArrayAdapter businessAdapter;
    private ActivityResultLauncher<Intent> resultLauncher;
    private FloatingNavigationView mFloatingNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        simpleDataViewModel = new ViewModelProvider(this).get(SimpleDataViewModel.class);
        simpleDataViewModel.GetSimpleData("GetNoticeData2");
        setBar();
        setListener();
        setFloatingNavigationView();
        setResultLauncher();
        observerViewModel();
        backpressed = new BackPressControl(this);
        if (Users.Gboutsourcing)
            binding.button6.setVisibility(View.GONE);//???????????? ?????? ???????????? ????????????
        binding.constraintLayout.setVisibility(View.GONE);
        binding.constraintLayout3.setVisibility(View.GONE);
        binding.constraintLayout4.setVisibility(View.GONE);
        binding.constraintLayout5.setVisibility(View.GONE);
    }


    private void setListener() {
        binding.button.setOnClickListener(new View.OnClickListener() {//????????????
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity0000.class);
                startActivity(intent);
            }
        });

        binding.button9.setOnClickListener(new View.OnClickListener() {//A?????????
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity9000.class);
                startActivity(intent);
            }
        });

        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity1000.class);
                startActivity(intent);
            }
        });
    }

    public void observerViewModel() {
        simpleDataViewModel.data.observe(this, data -> {
            if (data != null) {
                //LinkedTreeMap linkedTreeMap = (LinkedTreeMap) data;
                //Object errorCheck = linkedTreeMap.get("ErrorCheck");
                AppVersion appVersion = TypeChanger.chageTypeAppVersion(data);
                Object errorCheck = appVersion.ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel ??? ??????????????? ????????? View?????? ????????????.(?????? ????????????)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_LONG).show();
                } else {
                    boolean noticeFlag = PreferenceManager.getBoolean(this, "NoShowNotice");
                    //Object remark = linkedTreeMap.get("Remark");
                    Object remark = appVersion.Remark;
                    if (!noticeFlag)
                        viewNotice(remark.toString());
                }
            } else {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        simpleDataViewModel.list.observe(this, list -> {
            if (list != null) {
                ArrayList<BusinessClass> businessClassList = TypeChanger.chageTypeBusinessClassList(list);
                Object errorCheck = businessClassList.get(0).ErrorCheck;
                if (errorCheck != null) {// SimpleDataViewModel ??? ??????????????? ????????? View?????? ????????????.(?????? ????????????)
                    Toast.makeText(this, errorCheck.toString(), Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    for (int i = 0; i < businessClassList.size(); i++) {
                        stringArrayList.add((int) businessClassList.get(i).BusinessClassCode + "-" + businessClassList.get(i).CompanyName.replace("????????????(???)", ""));
                    }
                    final ArrayAdapter adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, stringArrayList);
                    businessAdapter = adapter;

                    MenuItem searchItem = binding.toolbar.getMenu().findItem(R.id.itemBusiness);
                    Spinner yourSpinnerName = (Spinner) searchItem.getActionView();
                    //Log.i("???????????????", "??????");
                    yourSpinnerName.setAdapter(adapter);

                    //Log.i("???????????????", "??????");
                }
            } else {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //???????????????
        simpleDataViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(this, models, Toast.LENGTH_LONG).show();
                PreferenceManager.setBoolean(this, "AutoLogin", false);
                PreferenceManager.setString(this, "ID", "");
                PreferenceManager.setString(this, "PW", "");
                progressOFF2();
            }
        });

        simpleDataViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//?????????
                    startProgress();
                } else {//?????????
                    progressOFF2();
                }
            }
        });
    }

    private void viewNotice(String remark) {
        DialogNoticeBinding dialogNoticeBinding = DataBindingUtil.inflate(LayoutInflater.from(getBaseContext()), R.layout.dialog_notice, null, false);
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder ?????? ??????
        //buider.setIcon(android.R.drawable.ic_menu_add); //???????????? ????????? ?????????(????????? ????????? ??????)
        buider.setView(dialogNoticeBinding.getRoot());
        try {
            dialogNoticeBinding.tvTitle.setText("????????????(version " + getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName + ")");
        } catch (PackageManager.NameNotFoundException e) {
            dialogNoticeBinding.tvTitle.setText("????????????");
        }
        dialogNoticeBinding.tvContent.setText(remark);
        final AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);////Dialog??? ???????????? ???????????? ??? Dialog??? ????????? ??????
        dialog.show();
        dialogNoticeBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogNoticeBinding.chkNoView.isChecked()) {
                    PreferenceManager.setBoolean(MainActivity.this, "NoShowNotice", true);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * ????????????
     */

    private void setFloatingNavigationView() {
        mFloatingNavigationView = CommonMethod.setFloatingNavigationView(this);
    }

    private void setResultLauncher() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        /**
                         * QR?????? ??????
                         */
                        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                        if (intentResult.getContents() != null) {
                            String scanResult = intentResult.getContents();
                            Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        /**
                         * QR?????? ???
                         */


                        if (result.getResultCode() == 100){

                        }
                    }
                });
    }

    private void setBar() {
        setSupportActionBar(binding.toolbar);
        CommonMethod.setBar(this);
    }

    public void showFloatingNavigationView() {
        mFloatingNavigationView.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        simpleDataViewModel.GetSimpleDataList("GetBusinessClassData");
        return CommonMethod.onCreateOptionsMenu2(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonMethod.onOptionsItemSelected(this, item, resultLauncher);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {//qr?????? ?????? ??????
            if (result.getContents() == null) {
                Toast.makeText(this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();
                //showErrorDialog(this, "?????? ???????????????.",1);
            } else {
                String scanResult;
                scanResult = result.getContents();
                Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onBackPressed() {
        backpressed.onBackPressed();
    }

    /**
     * ???????????? END
     */
}
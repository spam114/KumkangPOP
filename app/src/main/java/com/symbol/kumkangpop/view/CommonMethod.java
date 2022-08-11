package com.symbol.kumkangpop.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.activity.BaseActivity;
import com.symbol.kumkangpop.view.activity.LoginActivity;
import com.symbol.kumkangpop.viewmodel.LoginViewModel;

public class CommonMethod {
    public static String keyResult;

    public static boolean onCreateOptionsMenu(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        menu.findItem(R.id.itemQR).setVisible(false);
        menu.findItem(R.id.itemKeyboard).setVisible(false);
        return true;
    }

    /**
     * 상단바 사업장 숨기기
     *
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu2(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        return true;
    }

    public static boolean onCreateOptionsMenu3(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        menu.findItem(R.id.itemKeyboard).setVisible(false);
        menu.findItem(R.id.itemQR).setVisible(false);
        return true;
    }

    /**
     * 상단바 전부사용
     *
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu4(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public static boolean onOptionsItemSelected(BaseActivity activity, MenuItem item, ActivityResultLauncher<Intent> resultLauncher) {
        switch (item.getItemId()) {
            case R.id.itemKeyboard:
                LayoutInflater inflater = LayoutInflater.from(activity);
                final View dialogView = inflater.inflate(R.layout.dialog_key_in, null);
                AlertDialog.Builder buider = new AlertDialog.Builder(activity); //AlertDialog.Builder 객체 생성
                //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                final AlertDialog dialog = buider.create();
                //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                //Dialog 보이기
                dialog.show();
                TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                tvTitle.setText("TAG번호 입력");
                Button btnOK = dialogView.findViewById(R.id.btnOK);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagNo = edtTagNo.getText().toString();

                        if (tagNo.equals("")) {
                            Toast.makeText(activity, "TAG번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        activity.getKeyInResult(tagNo);
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            /*case R.id.itemLogout:
                //로그아웃
                MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(activity, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                alertBuilder.setTitle("로그아웃");
                alertBuilder.setMessage("로그아웃 하시겠습니까?");
                alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.setBoolean(activity, "AutoLogin", false);
                        PreferenceManager.setString(activity, "ID", "");
                        PreferenceManager.setString(activity, "PW", "");
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
                //((TextView)findViewById(R.id.textView)).setText("SEARCH") ;
                return true;*/
            case R.id.itemQR:
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리
                intentIntegrator.setPrompt(activity.getString(R.string.qr_state_common));
                intentIntegrator.setOrientationLocked(false);
                // intentIntegrator.setCaptureActivity(QRReaderActivityStockOutMaster.class);
                //intentIntegrator.initiateScan();
                intentIntegrator.setRequestCode(7);
                resultLauncher.launch(intentIntegrator.createScanIntent());
                return true;
            /*case R.id.action_settings :
                //((TextView)findViewById(R.id.textView)).setText("SETTINGS") ;
                return true ;*/
            case android.R.id.home:
                activity.showFloatingNavigationView();

            default:
                return false;
        }
    }

    public static void setBar(BaseActivity activity) {
        Drawable drawable = activity.getResources().getDrawable(R.drawable.baseline_menu_black_36);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, 36, 36, true));
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(newdrawable);
    }


    public static FloatingNavigationView setFloatingNavigationView(BaseActivity activity) {
        View fnvHeader;
        FloatingNavigationView mFloatingNavigationView = (FloatingNavigationView) activity.findViewById(R.id.floating_navigation_view);
        fnvHeader = mFloatingNavigationView.getHeaderView(0);

        /**
         * 글자색 강제변경
         */
        Menu menu = mFloatingNavigationView.getMenu();
        MenuItem mi = menu.findItem(R.id.logOut);
        MenuItem mi2 = menu.findItem(R.id.pcPrinter);
        SpannableString s = new SpannableString(mi.getTitle());
        SpannableString s2 = new SpannableString(mi2.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s2.length(), 0);
        mi.setTitle(s);
        mi2.setTitle(s2);

        TextView txtUserName = fnvHeader.findViewById(R.id.txtUserName);
        txtUserName.setText(Users.UserName);
        TextView txtUserID = fnvHeader.findViewById(R.id.txtUserID);
        txtUserID.setText(Users.UserID);
        ImageView userImage = fnvHeader.findViewById(R.id.userImage);
        TextView txtPCName = fnvHeader.findViewById(R.id.txtPCName);
        if (Users.PCCode.equals(""))
            txtPCName.setText("출력PC 없음");
        else
            txtPCName.setText(Users.PCName + "(" + Users.PCCode + ")");


        LoginViewModel loginViewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
        SearchCondition sc = new SearchCondition();


        loginViewModel.loading.observe(activity, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {//로딩중

                } else {//로딩끝
                    if (Users.PCCode.equals(""))
                        txtPCName.setText("출력PC 없음");
                    else
                        txtPCName.setText(Users.PCName + "(" + Users.PCCode + ")");
                }
            }
        });

        loginViewModel.printErrorMsg.observe(activity, models -> {
            if (models != null) {
                Toast.makeText(activity, models, Toast.LENGTH_SHORT).show();
            }
        });

        if (Users.UserImage.equals("fail")) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            userImage.setLayoutParams(layoutParams);
            userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
        } else {
            try {
                byte[] array5 = Base64.decode(Users.UserImage, Base64.DEFAULT);
                userImage.setBackground(new ShapeDrawable(new OvalShape()));
                userImage.setClipToOutline(true);
                userImage.setImageBitmap(BitmapFactory.decodeByteArray(array5, 0, array5.length));
            } catch (Exception ex) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
                userImage.setLayoutParams(layoutParams);
                userImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.kumkangcircle));
            }
        }

        ImageView imvClose = fnvHeader.findViewById(R.id.imvClose);
        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingNavigationView.close();
            }
        });


        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logOut) {
                    //로그아웃
                    MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(activity, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                    alertBuilder.setTitle("로그아웃");
                    alertBuilder.setMessage("로그아웃 하시겠습니까?");
                    alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreferenceManager.setBoolean(activity, "AutoLogin", false);
                            PreferenceManager.setString(activity, "ID", "");
                            PreferenceManager.setString(activity, "PW", "");
                            PreferenceManager.setString(activity, "PCCode", "");
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("FirstFlag", false);


                            //Users.
                            activity.startActivity(intent);
                            activity.finish();
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                } else if (item.getItemId() == R.id.pcPrinter) {
                    //출력PC 연결
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View dialogView = inflater.inflate(R.layout.dialog_key_in, null);
                    AlertDialog.Builder buider = new AlertDialog.Builder(activity); //AlertDialog.Builder 객체 생성
                    //  buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                    buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                    final AlertDialog dialog = buider.create();
                    //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                    dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                    //Dialog 보이기
                    dialog.show();
                    TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
                    tvTitle.setText("PC번호 입력");
                    com.google.android.material.textfield.TextInputLayout textInputLayout = dialogView.findViewById(R.id.textInputLayout);
                    textInputLayout.setHint("PC번호");
                    Button btnOK = dialogView.findViewById(R.id.btnOK);
                    Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                    TextInputEditText edtTagNo = dialogView.findViewById(R.id.edtTagNo);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pcNo = edtTagNo.getText().toString();

                            if (pcNo.equals("")) {
                                Toast.makeText(activity, "연결할 PC의 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            sc.PCCode = pcNo;
                            loginViewModel.GetPrintPCData(sc, activity);

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

                return false;
            }
        });


        return mFloatingNavigationView;

    }
}
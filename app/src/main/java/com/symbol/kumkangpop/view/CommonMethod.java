package com.symbol.kumkangpop.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.view.activity.BaseActivity;

public class CommonMethod {
    public static String keyResult;

    public static boolean onCreateOptionsMenu(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * 상단바 사업장 숨기기
     * @param activity
     * @param menu
     * @return
     */
    public static boolean onCreateOptionsMenu2(BaseActivity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.itemBusiness).setVisible(false);
        return true;
    }

    public static boolean onOptionsItemSelected(BaseActivity activity, MenuItem item) {
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
                intentIntegrator.initiateScan();
                return true;
            /*case R.id.action_settings :
                //((TextView)findViewById(R.id.textView)).setText("SETTINGS") ;
                return true ;*/
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
}
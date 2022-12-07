package com.symbol.kumkangpop.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.BackPressEditText;
import com.symbol.kumkangpop.view.Interface.BaseActivityInterface;
import com.symbol.kumkangpop.view.adapter.Adapter5320;
import com.symbol.kumkangpop.view.application.ApplicationClass;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.util.ArrayList;

public class Dialog5320 extends DialogFragment implements BaseActivityInterface {

    Adapter5320 adapter;
    int locationNo;
    CommonViewModel commonViewModel;
    BackPressEditText edtInput;
    //Button btnCreateContainer;
    RecyclerView recyclerView;
    public String containerNo;
    private DialogInterface.OnDismissListener onDismissOuvinte;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    public Dialog5320(int locationNo) {
        this.locationNo = locationNo;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissOuvinte!=null)
            onDismissOuvinte.onDismiss(dialog);
    }

    public void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        this.onDismissOuvinte = listener;
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog5320, container, false);
        edtInput = view.findViewById(R.id.edtInput);
        //btnCreateContainer = view.findViewById(R.id.btnCreateContainer);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView5 = view.findViewById(R.id.textView5);
        return view;
    }

    private void init() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        observerViewModel();
        setListener();
        adapter = new Adapter5320(new ArrayList<>(), getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        setLanguage();
        GetMainData();
    }

    private void setLanguage() {
        if(Users.Language!=0){
            textView1.setText("ContainerNo");
            textView2.setText("OutDate");
            textView3.setText("SilNo");
            textView4.setText("CarNo");
            textView5.setText("TonWeight");
        }
    }

    private void setListener() {
        /**
         * forfilter
         */
        edtInput.addTextChangedListener(new TextWatcher() {
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

    }

    private void GetMainData() {
        SearchCondition sc = new SearchCondition();
        sc.SearchDivision = 0;
        sc.LocationNo = locationNo;
        sc.ContainerNo = "";
        commonViewModel.Get("GetContainerData", sc);
    }


    public void observerViewModel() {

        commonViewModel.data.observe(this, data -> {
            if (data != null) {
                /*DecimalFormat numFormatter = new DecimalFormat("###,###");
                binding.tvStockOutOrderQty.setText(numFormatter.format(num1));
                binding.tvStockOutQty.setText(numFormatter.format(num2));*/
                adapter.updateAdapter(data.ContainerList);
                adapter.getFilter().filter(edtInput.getText().toString());
            } else {
                Toast.makeText(getContext(), Users.Language==0 ? "서버 연결 오류": "Server connection error", Toast.LENGTH_SHORT).show();            }
        });

        /*commonViewModel.data2.observe(this, data -> {
            if (data != null) {
                if(data.BoolResult){
                    if (Users.Language == 0) {
                        Toast.makeText(this, "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Normal has been registered.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

        /*commonViewModel.data3.observe(this, data -> {
            if (data != null) {
                boolean bbool = data.BoolResult;
                if (bbool) {
                    if (Users.Language == 0) {
                        Toast.makeText(this, "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Normal has been registered.", Toast.LENGTH_SHORT).show();
                    }

                    GetMainData(Users.LocationNo, binding.edtContainerNo.getText().toString());
                    *//*if (nDataSet.Tables[0].Rows.Count != 0)
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
                    }*//*
                } else {
                    ////nScaner.fnBeepError();
                    if (Users.Language == 0) {
                        Toast.makeText(this, "진행중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "An error occurred during the course.", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

        /*commonViewModel.data4.observe(this, data -> {
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
                }
            } else {
                Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

        //에러메시지
        commonViewModel.errorMsg.observe(this, models -> {
            if (models != null) {
                Toast.makeText(getContext(), models, Toast.LENGTH_SHORT).show();
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



    /*public void addSaleOrderList() {
        ArrayList<SaleOrder> saleOrderArrayList = new ArrayList<>();

        for (int i = 0; i < stockArrayList.size(); i++) {


            Stock stock = stockArrayList.get(i);
            SaleOrder saleOrder = new SaleOrder();
            if (stock.checked) {
                saleOrder.isDBSaved = false;
                saleOrder.saleOrderNo = "";
                saleOrder.index = -1;
                saleOrder.partCode = stock.PartCode;
                saleOrder.partName = stock.PartName;
                saleOrder.partSpec = stock.PartSpec;
                saleOrder.partSpecName = stock.PartSpecName;
                saleOrder.marketPrice = stock.MarketPrice;
                saleOrder.logicalWeight = Double.parseDouble(stock.Weight);
                saleOrder.stockQty = Double.parseDouble(stock.Qty);
                saleOrderArrayList.add(saleOrder);
            }
        }
        for (int i = 0; i < saleOrderArrayList.size(); i++) {
            for (int j = 0; j < exOrderArrayList.size(); j++) {
                if (saleOrderArrayList.get(i).partCode.equals(exOrderArrayList.get(j).partCode) && saleOrderArrayList.get(i).partSpec.equals(exOrderArrayList.get(j).partSpec)) {
                    Toast.makeText(getContext(), "주문서에 존재하는 품목, 규격을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        }

        ((SaleOrderActivity) getContext()).addSaleOrderListByContents(saleOrderArrayList);
        dismiss();
                *//*Intent intent = new Intent(SearchAvailablePartActivity.this, SaleOrderActivity.class);
                intent.putExtra("customerCode",customerCode);
                intent.putExtra("locationNo",locationNo);
                intent.putExtra("saleOrderNo",saleOrderNo);
                intent.putExtra("saleOrderArrayList", saleOrderArrayList);
                //intent.putExtra()
                startActivityResult.launch(intent);
                finish();*//*

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
    public void progressOFF2() {
        ApplicationClass.getInstance().progressOFF2();
    }

    @Override
    public void progressON() {

    }

    @Override
    public void progressON(String message) {

    }

    @Override
    public void progressON(String message, Handler handler) {

    }


    /*View.OnClickListener doneAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG).show();
        }
    };*/

}
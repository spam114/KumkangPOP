package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row5000Binding;
import com.symbol.kumkangpop.model.object.SalesOrder;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5000 extends RecyclerView.Adapter<Adapter5000.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<SalesOrder> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<SalesOrder> unFilteredlist;//for filter
    ArrayList<SalesOrder> filteredList;//for filter
    CommonViewModel commonViewModel;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;

    public Adapter5000(ArrayList<SalesOrder> items, Context context, ActivityResultLauncher<Intent> resultLauncher, CommonViewModel commonViewModel, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.commonViewModel = commonViewModel;
        this.barcodeConvertPrintViewModel = barcodeConvertPrintViewModel;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<SalesOrder> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5000Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5000, viewGroup, false);
        return new Adapter5000.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //SalesOrder item = items.get(position);
        SalesOrder item = filteredList.get(position);//for filter
        viewHolder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        //return items.size();
        return filteredList.size();//for filter

    }

    //for filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<SalesOrder> filteringList = new ArrayList<>();
                    for (SalesOrder salesOrder : unFilteredlist) {
                        if (salesOrder.LocationName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(salesOrder);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<SalesOrder>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5000Binding binding;
        //View row;

        public ViewHolder(Row5000Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(SalesOrder item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");

            binding.tvLocationNo.setText(Integer.toString((int) item.LocationNo));
            binding.tvLocationName.setText(item.LocationName);
            //binding.tvSalesOrderNo.setText(item.SalesOrderNo);
            //binding.tvDong.setText(item.Dong);
            //binding.tvHo.setText(item.Ho);
            //binding.tvNo.setText(numFormatter.format(item.HoSeqNo));
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            /*binding.tvPartSpec.setText(item.PartSpec);
            binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));*/

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethod.FNBarcodeConvertPrint(Integer.toString((int) item.LocationNo), barcodeConvertPrintViewModel);

                    /*if (item.SalesOrderNo.equals("")) return;

                    SearchCondition sc = new SearchCondition();
                    sc.IConvetDivision = 1;
                    sc.Barcode = "S2-" + item.SaleOrderNo;
                    //commonViewModel.cData = sc.Barcode;
                    commonViewModel.Get2("GetNumConvertData", sc);
                    *//*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*//*
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();*/
                }
            });

        }
    }
   /* private void GoActivity1100(String result){
        Intent intent = new Intent(context, Activity1100.class);
        intent.putExtra("result", result);
        resultLauncher.launch(intent);
    }*/

    public void addItem(SalesOrder item) {
        items.add(item);
    }

    public void setItems(ArrayList<SalesOrder> items) {
        this.items = items;
    }

    public SalesOrder getItem(int position) {
        return items.get(position);
    }

    public ArrayList<SalesOrder> getItemList() {
        return items;
    }

    public void setItem(int position, SalesOrder item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}


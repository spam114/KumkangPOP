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
import com.symbol.kumkangpop.databinding.Row2200Binding;
import com.symbol.kumkangpop.model.object.Bundle;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter2200 extends RecyclerView.Adapter<Adapter2200.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Bundle> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Bundle> unFilteredlist;//for filter
    ArrayList<Bundle> filteredList;//for filter

    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    //File filePath;

    public Adapter2200(ArrayList<Bundle> items, Context context, ActivityResultLauncher<Intent> resultLauncher, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.barcodeConvertPrintViewModel = barcodeConvertPrintViewModel;
        this.cameraResultLauncher = cameraResultLauncher;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<Bundle> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row2200Binding binding = DataBindingUtil.inflate(inflater, R.layout.row2200, viewGroup, false);
        return new Adapter2200.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Bundle item = items.get(position);
        Bundle item = filteredList.get(position);//for filter
        viewHolder.setItem(item, position); //왜오류
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
                    ArrayList<Bundle> filteringList = new ArrayList<>();
                    for (Bundle Bundle : unFilteredlist) {
                        if (Bundle.BundleNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Bundle);
                        }
                        if (Bundle.CustomerName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Bundle);
                        }
                        if (Bundle.LocationName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Bundle);
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
                filteredList = (ArrayList<Bundle>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row2200Binding binding;
        //View row;

        public ViewHolder(Row2200Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Bundle item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvBundleNo.setText(item.BundleNo);
            binding.tvCustomerName.setText(item.CustomerName + "(" + item.LocationName + ")");
            binding.tvDong.setText(item.Dong);
            binding.tvNo.setText(numFormatter.format(item.DongSeqNo));
            binding.tvSaleOrderNo.setText(item.SaleOrderNo);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethod.FNBarcodeConvertPrint(item.BundleNo, barcodeConvertPrintViewModel);
                    /*if (item.PackingNo.equals(""))
                        return;
                    //CommonMethod.FNBarcodeConvertPrint(item.BundleNo, barcodeConvertPrintViewModel);
                    *//*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*//*
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();

                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                    materialAlertDialogBuilder.setTitle(item.PackingNo);
                    CharSequence[] sequences = new CharSequence[2];

                    sequences[0] = "사진촬영";
                    sequences[1] = "작업목록 제외";

                    if (Users.Language != 0) {
                        sequences[0] = "Camera";
                        sequences[1] = "Exclude Task List";
                    }

                    materialAlertDialogBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    materialAlertDialogBuilder.setCancelable(true);
                    materialAlertDialogBuilder.show();*/
                }
            });

        }
    }

    public void addItem(Bundle item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Bundle> items) {
        this.items = items;
    }

    public Bundle getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Bundle> getItemList() {
        return items;
    }

    public void setItem(int position, Bundle item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



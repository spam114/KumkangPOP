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
import com.symbol.kumkangpop.databinding.Row3300Binding;
import com.symbol.kumkangpop.model.object.StockOutDetail;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter3300 extends RecyclerView.Adapter<Adapter3300.ViewHolder>  implements Filterable{

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<StockOutDetail> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<StockOutDetail> unFilteredlist;//for filter
    ArrayList<StockOutDetail> filteredList;//for filter

    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;

    public Adapter3300(ArrayList<StockOutDetail> items, Context context, ActivityResultLauncher<Intent> resultLauncher, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.barcodeConvertPrintViewModel = barcodeConvertPrintViewModel;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<StockOutDetail> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row3300Binding binding = DataBindingUtil.inflate(inflater, R.layout.row3300, viewGroup, false);
        return new Adapter3300.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //StockOutDetail item = items.get(position);
        StockOutDetail item = filteredList.get(position);//for filter
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
                    ArrayList<StockOutDetail> filteringList = new ArrayList<>();
                    for (StockOutDetail StockOutDetail : unFilteredlist) {
                        if (StockOutDetail.StockOutNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(StockOutDetail);
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
                filteredList = (ArrayList<StockOutDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row3300Binding binding;
        //View row;

        public ViewHolder(Row3300Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(StockOutDetail item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvStockOutNo.setText(item.StockOutNo);
            binding.tvCustomerName.setText(item.CustomerName);
            binding.tvLocationName.setText(item.LocationName);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.StockOutNo.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.StockOutNo, barcodeConvertPrintViewModel);
                }
            });

        }
    }

    public void addItem(StockOutDetail item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<StockOutDetail> items) {
        this.items = items;
    }

    public StockOutDetail getItem(int position) {
        return items.get(position);
    }

    public ArrayList<StockOutDetail> getItemList() {
        return items;
    }

    public void setItem(int position, StockOutDetail item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



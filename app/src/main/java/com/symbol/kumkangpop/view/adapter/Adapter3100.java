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
import com.symbol.kumkangpop.databinding.Row3100Binding;
import com.symbol.kumkangpop.model.object.Customer;
import com.symbol.kumkangpop.view.activity.menu3.Activity3100;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter3100 extends RecyclerView.Adapter<Adapter3100.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Customer> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Customer> unFilteredlist;//for filter
    ArrayList<Customer> filteredList;//for filter

    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;

    public Adapter3100(ArrayList<Customer> items, Context context, ActivityResultLauncher<Intent> resultLauncher, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
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

    public void updateAdapter(ArrayList<Customer> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row3100Binding binding = DataBindingUtil.inflate(inflater, R.layout.row3100, viewGroup, false);
        return new Adapter3100.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Customer item = items.get(position);
        Customer item = filteredList.get(position);//for filter
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
                    ArrayList<Customer> filteringList = new ArrayList<>();
                    for (Customer Customer : unFilteredlist) {
                        if (Customer.CustomerName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Customer);
                        }
                        if (Customer.LocationName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Customer);
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
                filteredList = (ArrayList<Customer>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row3100Binding binding;
        //View row;

        public ViewHolder(Row3100Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Customer item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvCustomerName.setText(item.CustomerName);
            binding.tvCustomerCode.setText(item.CustomerCode);
            binding.tvLocationName.setText(item.LocationName);
            binding.tvLocationNo.setText(numFormatter.format(item.LocationNo));

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (item.StockOutNo.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.StockOutNo, barcodeConvertPrintViewModel);*/
                    ((Activity3100) context).ScanStockOutTag((int) item.LocationNo);
                }
            });

        }
    }

    public void addItem(Customer item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Customer> items) {
        this.items = items;
    }

    public Customer getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Customer> getItemList() {
        return items;
    }

    public void setItem(int position, Customer item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



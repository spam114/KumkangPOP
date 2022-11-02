package com.symbol.kumkangpop.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row0000Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.SalesOrder;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter0000 extends RecyclerView.Adapter<Adapter0000.ViewHolder> implements Filterable {

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

    public Adapter0000(ArrayList<SalesOrder> items, Context context, ActivityResultLauncher<Intent> resultLauncher, CommonViewModel commonViewModel) {
        this.context=context;
        this.items = items;
        this.resultLauncher=resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.commonViewModel = commonViewModel;
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
        if(newCountries.size()==1 && newCountries.get(0).ErrorCheck!=null){
            Toast.makeText(context, newCountries.get(0).ErrorCheck, Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
            return;
        }
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row0000Binding binding = DataBindingUtil.inflate(inflater, R.layout.row0000, viewGroup, false);
        return new Adapter0000.ViewHolder(binding);
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
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<SalesOrder> filteringList = new ArrayList<>();
                    for(SalesOrder sale : unFilteredlist) {
                        if(sale.SaleOrderNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(sale);
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
                filteredList = (ArrayList<SalesOrder>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row0000Binding binding;
        //View row;

        public ViewHolder(Row0000Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(SalesOrder item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvSaleOrderNo.setText(item.SaleOrderNo);
            binding.tvCustomerLocation.setText(item.CustomerName+"("+item.LocationName+")");
            binding.tvDong.setText(item.Dong);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            /*binding.tvPartSpec.setText(item.PartSpec);
            binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));*/


            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.SaleOrderNo.equals("")) return;

                    SearchCondition sc = new SearchCondition();
                    sc.IConvetDivision = 1;
                    sc.Barcode = "S2-"+item.SaleOrderNo;
                    //commonViewModel.cData = sc.Barcode;
                    commonViewModel.Get2("GetNumConvertData", sc);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
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

    public void setItem(int position, SalesOrder item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag){
        this.confirmFlag=confirmFlag;
    }
}


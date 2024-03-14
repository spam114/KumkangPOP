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
import com.symbol.kumkangpop.databinding.Row0412Binding;
import com.symbol.kumkangpop.model.object.Packing;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter0412 extends RecyclerView.Adapter<Adapter0412.ViewHolder>  implements Filterable{

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Packing> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Packing> unFilteredlist;//for filter
    ArrayList<Packing> filteredList;//for filter

    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;

    public Adapter0412(ArrayList<Packing> items, Context context, ActivityResultLauncher<Intent> resultLauncher, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
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

    public void updateAdapter(ArrayList<Packing> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row0412Binding binding = DataBindingUtil.inflate(inflater, R.layout.row0412, viewGroup, false);
        return new Adapter0412.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Packing item = items.get(position);
        Packing item = filteredList.get(position);//for filter
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
                    ArrayList<Packing> filteringList = new ArrayList<>();
                    for (Packing packing : unFilteredlist) {
                        if (packing.ItemTag.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(packing);
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
                filteredList = (ArrayList<Packing>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row0412Binding binding;
        //View row;

        public ViewHolder(Row0412Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Packing item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvPartName.setText(item.PartName);
            binding.tvPartSpec.setText(item.PartSpec);
            binding.tvMspec.setText(item.MSpec);
            binding.tvDrawingNo.setText(item.DrawingNo);
            binding.tvTagNo.setText(item.ItemTag);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (item.ItemTag.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.ItemTag, barcodeConvertPrintViewModel);


                    //ㄴㅇㄹ

                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(Packing item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Packing> items) {
        this.items = items;
    }

    public Packing getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Packing> getItemList() {
        return items;
    }

    public void setItem(int position, Packing item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



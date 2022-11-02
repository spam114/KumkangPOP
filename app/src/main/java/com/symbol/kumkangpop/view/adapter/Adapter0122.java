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
import com.symbol.kumkangpop.databinding.Row0122Binding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.Packing;
import com.symbol.kumkangpop.view.CommonMethod;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter0122 extends RecyclerView.Adapter<Adapter0122.ViewHolder> implements Filterable {

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
    CommonViewModel commonViewModel;
    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;

    public Adapter0122(ArrayList<Packing> items, Context context, ActivityResultLauncher<Intent> resultLauncher, CommonViewModel commonViewModel, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.commonViewModel = commonViewModel;
        this.barcodeConvertPrintViewModel= barcodeConvertPrintViewModel;
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
        Row0122Binding binding = DataBindingUtil.inflate(inflater, R.layout.row0122, viewGroup, false);
        return new Adapter0122.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Packing item = items.get(position);
        Packing item = filteredList.get(position);//for filter
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
                    ArrayList<Packing> filteringList = new ArrayList<>();
                    for (Packing packing : unFilteredlist) {
                        if (packing.PackingNo.toLowerCase().contains(charString.toLowerCase())) {
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
        Row0122Binding binding;
        //View row;

        public ViewHolder(Row0122Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Packing item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvPackingNo.setText(item.PackingNo);
            binding.tvDong.setText(item.Dong);
            binding.tvHo.setText(item.Ho);
            binding.tvNo.setText(numFormatter.format(item.HoSeqNo));
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            /*binding.tvPartSpec.setText(item.PartSpec);
            binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));*/


            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.PackingNo.equals(""))
                        return;
                    CommonMethod.FNBarcodeConvertPrint(item.PackingNo, barcodeConvertPrintViewModel);

                    /*if (item.PackingNo.equals("")) return;

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

    public void addItem(Packing item) {
        items.add(item);
    }

    public void setItems(ArrayList<Packing> items) {
        this.items = items;
    }

    public Packing getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Packing item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}


package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row5330Binding;
import com.symbol.kumkangpop.model.object.Dept;
import com.symbol.kumkangpop.view.dialog.Dialog5330;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5330 extends RecyclerView.Adapter<Adapter5330.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Dept> items = new ArrayList<>();
    boolean confirmFlag;
    //ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Dept> unFilteredlist;//for filter
    ArrayList<Dept> filteredList;//for filter

    Dialog5330 dialog5330;

    public Adapter5330(ArrayList<Dept> items, Context context, Dialog5330 dialog5330) {
        this.context = context;
        this.items = items;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.dialog5330=dialog5330;
    }

    public void updateAdapter(ArrayList<Dept> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5330Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5330, viewGroup, false);
        return new Adapter5330.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Dept item = items.get(position);
        Dept item = filteredList.get(position);//for filter
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
                    ArrayList<Dept> filteringList = new ArrayList<>();
                    for (Dept Dept : unFilteredlist) {
                        if (Dept.CodeName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Dept);
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
                filteredList = (ArrayList<Dept>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5330Binding binding;
        //View row;

        public ViewHolder(Row5330Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Dept item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvDeptCode.setText(Integer.toString((int)item.DeptCode));
            binding.tvCodeName.setText(item.CodeName);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Double.toString(item.DeptCode).equals(""))
                        return;

                    dialog5330.deptCode = (int)item.DeptCode;
                    dialog5330.dismiss();

                    //CommonMethod.FNBarcodeConvertPrint(item.DeptNo, barcodeConvertPrintViewModel);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(Dept item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Dept> items) {
        this.items = items;
    }

    public Dept getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Dept> getItemList() {
        return items;
    }

    public void setItem(int position, Dept item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



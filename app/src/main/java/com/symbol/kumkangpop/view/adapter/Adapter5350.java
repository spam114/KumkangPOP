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
import com.symbol.kumkangpop.databinding.Row5350Binding;
import com.symbol.kumkangpop.model.object.Area;
import com.symbol.kumkangpop.view.dialog.Dialog5350;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5350 extends RecyclerView.Adapter<Adapter5350.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Area> items = new ArrayList<>();
    boolean confirmFlag;
    //ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Area> unFilteredlist;//for filter
    ArrayList<Area> filteredList;//for filter

    Dialog5350 dialog5350;

    public Adapter5350(ArrayList<Area> items, Context context, Dialog5350 dialog5350) {
        this.context = context;
        this.items = items;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.dialog5350=dialog5350;
    }

    public void updateAdapter(ArrayList<Area> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5350Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5350, viewGroup, false);
        return new Adapter5350.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Area item = items.get(position);
        Area item = filteredList.get(position);//for filter
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
                    ArrayList<Area> filteringList = new ArrayList<>();
                    for (Area Area : unFilteredlist) {
                        if (Area.AreaName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Area);
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
                filteredList = (ArrayList<Area>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5350Binding binding;
        //View row;

        public ViewHolder(Row5350Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Area item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvAreaCode.setText(item.AreaCode);
            binding.tvAreaName.setText(item.AreaName);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.AreaCode.equals(""))
                        return;

                    dialog5350.areaCode = item.AreaCode;
                    dialog5350.dismiss();

                    //CommonMethod.FNBarcodeConvertPrint(item.AreaNo, barcodeConvertPrintViewModel);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(Area item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Area> items) {
        this.items = items;
    }

    public Area getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Area> getItemList() {
        return items;
    }

    public void setItem(int position, Area item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



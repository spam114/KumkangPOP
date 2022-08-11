package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.RowReport6Binding;
import com.symbol.kumkangpop.model.object.Report;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterReport6 extends RecyclerView.Adapter<AdapterReport6.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Report> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;
    int radioIndex;

    public AdapterReport6(ArrayList<Report> items, Context context, ActivityResultLauncher<Intent> resultLauncher) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
    }

    public void updateAdapter(ArrayList<Report> newCountries) {
        items.clear();
        if (newCountries.size() == 1 && newCountries.get(0).ErrorCheck != null) {
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
        RowReport6Binding binding = DataBindingUtil.inflate(inflater, R.layout.row_report6, viewGroup, false);
        return new AdapterReport6.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Report item = items.get(position);
        viewHolder.setItem(item, position); //왜오류
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setChekedRadioIndex(int i) {
        radioIndex=i;
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowReport6Binding binding;
        //View row;

        public ViewHolder(RowReport6Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Report item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvPackingNo.setText(item.PackingNo);
            binding.tvDong.setText(item.Dong);
            binding.tvQty.setText(numFormatter.format(item.Qty));
            binding.tvM2.setText(numFormatter.format(item.M2));
            binding.tvWeight.setText(numFormatter.format(item.Weight));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if(item.LocationNo.equals("")){
                binding.tvPackingNo.setText(item.CustomerName+"("+item.LocationName+")");
                param.weight=9.5f;
            }
            else{
                param.weight=2.7f;
            }
            param.topMargin=15;
            param.bottomMargin=15;
            param.leftMargin=10;
            binding.tvPackingNo.setLayoutParams(param);

            if (!item.ColorFlag)
                binding.getRoot().setBackgroundColor(Color.parseColor("#F1F1F1"));
            else
                binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addItem(Report item) {
        items.add(item);
    }

    public void setItems(ArrayList<Report> items) {
        this.items = items;
    }

    public Report getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Report item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



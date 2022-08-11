package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
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
import com.symbol.kumkangpop.databinding.RowReport7Binding;
import com.symbol.kumkangpop.model.object.Report;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterReport7 extends RecyclerView.Adapter<AdapterReport7.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Report> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;
    int radioIndex;

    public AdapterReport7(ArrayList<Report> items, Context context, ActivityResultLauncher<Intent> resultLauncher) {
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
        RowReport7Binding binding = DataBindingUtil.inflate(inflater, R.layout.row_report7, viewGroup, false);
        return new AdapterReport7.ViewHolder(binding);
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
        RowReport7Binding binding;
        //View row;

        public ViewHolder(RowReport7Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Report item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvPartSpec.setText(item.PartSpec);
            binding.tvTotalQty.setText(numFormatter.format(item.totalQty));
            binding.tvCurrentCQty.setText(numFormatter.format(item.currentCQty));
            binding.tvCurrentAwaitQty.setText(numFormatter.format(item.currentAwaitQty));
            binding.tvCurrentASaleQty.setText(numFormatter.format(item.currentASaleQty));
            binding.tvCurrentAPlanQty.setText(numFormatter.format(item.currentAPlanQty));
            binding.tvCurrentNSaleQty.setText(numFormatter.format(item.currentNSaleQty));
            binding.tvCurrentNPlanQty.setText(numFormatter.format(item.currentNPlanQty));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight=1.3f;

            if(item.PartSpec.equals("")){
                binding.tvPartSpec.setText(item.PartName);
                binding.tvPartSpec.setTextSize(18);
                param.topMargin=15;
                param.weight=8.3f;
            }
            else{
                param.weight=1.3f;
                binding.tvPartSpec.setTextSize(17);
            }
            //param.topMargin=10;
            //param.bottomMargin=15;
            param.leftMargin=12;
            param.gravity= Gravity.CENTER_VERTICAL;
            binding.tvPartSpec.setLayoutParams(param);

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



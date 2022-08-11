package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.RowReport5Binding;
import com.symbol.kumkangpop.model.object.Report;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterReport5 extends RecyclerView.Adapter<AdapterReport5.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Report> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;
    int radioIndex;

    public AdapterReport5(ArrayList<Report> items, Context context, ActivityResultLauncher<Intent> resultLauncher) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

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
        RowReport5Binding binding = DataBindingUtil.inflate(inflater, R.layout.row_report5, viewGroup, false);
        return new AdapterReport5.ViewHolder(binding);
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
        RowReport5Binding binding;
        //View row;

        public ViewHolder(RowReport5Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Report item, int position) {
            setFieldState();
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvPart.setText(item.PartName+"("+item.PartSpec+")");
            binding.tvQty1.setText(numFormatter.format(item.repair_Quantity));
            binding.tvQty2.setText(numFormatter.format(item.new_Quantity));
            binding.tvQty3.setText(numFormatter.format(item.sum_Quantity));

            binding.tvWeight1.setText(numFormatter.format(item.repair_Kg));
            binding.tvWeight2.setText(numFormatter.format(item.new_Kg));
            binding.tvWeight3.setText(numFormatter.format(item.sum_Kg));

            binding.tvVolumn1.setText(numFormatter.format(item.repair_M2));
            binding.tvVolumn2.setText(numFormatter.format(item.new_M2));
            binding.tvVolumn3.setText(numFormatter.format(item.sum_M2));

            if(position%2==1)
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

        private void setFieldState(){

            binding.tvQty1.setVisibility(View.GONE);
            binding.tvQty2.setVisibility(View.GONE);
            binding.tvQty3.setVisibility(View.GONE);
            binding.tvWeight1.setVisibility(View.GONE);
            binding.tvWeight2.setVisibility(View.GONE);
            binding.tvWeight3.setVisibility(View.GONE);
            binding.tvVolumn1.setVisibility(View.GONE);
            binding.tvVolumn2.setVisibility(View.GONE);
            binding.tvVolumn3.setVisibility(View.GONE);

            if(radioIndex==R.id.rbQty){
                binding.tvQty1.setVisibility(View.VISIBLE);
                binding.tvQty2.setVisibility(View.VISIBLE);
                binding.tvQty3.setVisibility(View.VISIBLE);
            }
            else if(radioIndex==R.id.rbWeight){
                binding.tvWeight1.setVisibility(View.VISIBLE);
                binding.tvWeight2.setVisibility(View.VISIBLE);
                binding.tvWeight3.setVisibility(View.VISIBLE);
            }
            else if(radioIndex==R.id.rbVolumn){
                binding.tvVolumn1.setVisibility(View.VISIBLE);
                binding.tvVolumn2.setVisibility(View.VISIBLE);
                binding.tvVolumn3.setVisibility(View.VISIBLE);
            }

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



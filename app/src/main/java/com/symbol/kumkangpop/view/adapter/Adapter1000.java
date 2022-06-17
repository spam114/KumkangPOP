package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row1000Binding;
import com.symbol.kumkangpop.model.object.StockIn;
import com.symbol.kumkangpop.view.activity.menu1.Activity1100;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter1000 extends RecyclerView.Adapter<Adapter1000.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<StockIn> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    public Adapter1000(ArrayList<StockIn> items, Context context, ActivityResultLauncher<Intent> resultLauncher) {
        this.context=context;
        this.items = items;
        this.resultLauncher=resultLauncher;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<StockIn> newCountries) {
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
        Row1000Binding binding = DataBindingUtil.inflate(inflater, R.layout.row1000, viewGroup, false);
        return new Adapter1000.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        StockIn item = items.get(position);
        viewHolder.setItem(item, position); //왜오류
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row1000Binding binding;
        //View row;

        public ViewHolder(Row1000Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(StockIn item, int position) {
            String shortNo;
            try{
                shortNo = item.ShortNo.split("-")[1];
            }
            catch (Exception e){
                shortNo = item.ShortNo;
            }
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            binding.tvShortNo.setText(shortNo);
            binding.tvShortNo.setTag(item);
            binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            binding.tvPartSpec.setText(item.PartSpec);
            binding.tvInQty.setText(numFormatter.format(item.InQty));


            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(confirmFlag)
                        GoActivity1100(item.ShortNo);
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private void GoActivity1100(String result){
        Intent intent = new Intent(context, Activity1100.class);
        intent.putExtra("result", result);
        resultLauncher.launch(intent);
    }

    public void addItem(StockIn item) {
        items.add(item);
    }

    public void setItems(ArrayList<StockIn> items) {
        this.items = items;
    }

    public StockIn getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, StockIn item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag){
        this.confirmFlag=confirmFlag;
    }
}



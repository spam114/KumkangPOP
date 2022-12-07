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
import com.symbol.kumkangpop.databinding.Row5320Binding;
import com.symbol.kumkangpop.model.object.Container;
import com.symbol.kumkangpop.view.dialog.Dialog5320;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5320 extends RecyclerView.Adapter<Adapter5320.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Container> items = new ArrayList<>();
    boolean confirmFlag;
    //ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Container> unFilteredlist;//for filter
    ArrayList<Container> filteredList;//for filter

    Dialog5320 dialog5320;

    public Adapter5320(ArrayList<Container> items, Context context, Dialog5320 dialog5320) {
        this.context = context;
        this.items = items;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.dialog5320=dialog5320;
    }

    public void updateAdapter(ArrayList<Container> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5320Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5320, viewGroup, false);
        return new Adapter5320.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Container item = items.get(position);
        Container item = filteredList.get(position);//for filter
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
                    ArrayList<Container> filteringList = new ArrayList<>();
                    for (Container Container : unFilteredlist) {
                        if (Container.ContainerNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Container);
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
                filteredList = (ArrayList<Container>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5320Binding binding;
        //View row;

        public ViewHolder(Row5320Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Container item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvContainerNo.setText(item.ContainerNo);
            binding.tvOutDate.setText(item.OutDate);
            binding.tvSealNo.setText(item.SilNo);
            binding.tvCarNumber.setText(item.CarNumber);
            binding.tvTon.setText(numFormatter.format(item.ContainerTon));

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.ContainerNo.equals(""))
                        return;

                    dialog5320.containerNo = item.ContainerNo;
                    dialog5320.dismiss();

                    //CommonMethod.FNBarcodeConvertPrint(item.ContainerNo, barcodeConvertPrintViewModel);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(Container item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Container> items) {
        this.items = items;
    }

    public Container getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Container> getItemList() {
        return items;
    }

    public void setItem(int position, Container item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



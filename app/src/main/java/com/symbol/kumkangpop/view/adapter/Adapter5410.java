package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row5410Binding;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.view.activity.menu5.Activity5410;
import com.symbol.kumkangpop.viewmodel.BarcodeConvertPrintViewModel;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5410 extends RecyclerView.Adapter<Adapter5410.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<ScanListViewItem2> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<ScanListViewItem2> unFilteredlist;//for filter
    ArrayList<ScanListViewItem2> filteredList;//for filter

    BarcodeConvertPrintViewModel barcodeConvertPrintViewModel;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    File filePath;

    public Adapter5410(ArrayList<ScanListViewItem2> items, Context context, ActivityResultLauncher<Intent> resultLauncher, BarcodeConvertPrintViewModel barcodeConvertPrintViewModel,
                       ActivityResultLauncher<Intent> cameraResultLauncher, File filePath) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.barcodeConvertPrintViewModel = barcodeConvertPrintViewModel;
        this.cameraResultLauncher = cameraResultLauncher;
        this.filePath = filePath;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<ScanListViewItem2> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5410Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5410, viewGroup, false);
        return new Adapter5410.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //ScanListViewItem2 item = items.get(position);
        ScanListViewItem2 item = filteredList.get(position);//for filter
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
                    ArrayList<ScanListViewItem2> filteringList = new ArrayList<>();
                    for (ScanListViewItem2 ScanListViewItem2 : unFilteredlist) {
                        if (ScanListViewItem2.PackingNo.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(ScanListViewItem2);
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
                filteredList = (ArrayList<ScanListViewItem2>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5410Binding binding;
        //View row;

        public ViewHolder(Row5410Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(ScanListViewItem2 item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvPackingNo.setText(item.PackingNo);
            binding.tvCustomerName.setText(item.CustomerName + "(" + item.LocationName + ")");
            binding.tvDong.setText(item.Dong);
            binding.tvHo.setText(item.Ho);
            binding.tvNo.setText(numFormatter.format(item.HoSeqNo));
            binding.tvSaleOrderNo.setText(item.SaleOrderNo);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.PackingNo.equals(""))
                        return;
                    //CommonMethod.FNBarcodeConvertPrint(item.ScanListViewItem2No, barcodeConvertPrintViewModel);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();

                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                    materialAlertDialogBuilder.setTitle(item.PackingNo);
                    CharSequence[] sequences = new CharSequence[2];

                    sequences[0] = "사진촬영";
                    sequences[1] = "포장제외";

                    if (Users.Language != 0) {
                        sequences[0] = "Camera";
                        sequences[1] = "Exclude Packing";
                    }

                    materialAlertDialogBuilder.setItems(sequences, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {//사진촬영
                                /*FragmentManager fm = getSupportFragmentManager();
                                SearchAvailablePartDialog custom = new SearchAvailablePartDialog(saleOrderArrayList);
                                custom.show(fm, "");*/
                                startCamera(item.PackingNo);
                            } else if (which == 1) {//작업목록 제외

                                String title = "포장제외";
                                String message = "출고항목에서 포장을 제외 하시겠습니까?\n" + item.PackingNo;
                                String okString = "확인";
                                String noString = "취소";

                                if (Users.Language != 0) {
                                    title = "Exclude Packing";
                                    message = "Are you sure you want to exclude packaging from the shipment item?\n" + item.PackingNo;
                                    okString = "Confirm";
                                    noString = "Cancel";
                                }

                                new MaterialAlertDialogBuilder(context)
                                        .setTitle(title)
                                        .setMessage(message)
                                        .setCancelable(true)
                                        .setPositiveButton(okString, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ((Activity5410) (context)).RemoveStockOut(item.PackingNo);
                                            }
                                        })
                                        .setNegativeButton(noString, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                                return;
                            }
                        }
                    });
                    materialAlertDialogBuilder.setCancelable(true);
                    materialAlertDialogBuilder.show();


                }
            });

        }
    }

    private void startCamera(String packingNo) {
        try {
            Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", filePath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
            ((Activity5410) (context)).tempPackingNo = packingNo;

            cameraResultLauncher.launch(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(ScanListViewItem2 item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<ScanListViewItem2> items) {
        this.items = items;
    }

    public ScanListViewItem2 getItem(int position) {
        return items.get(position);
    }

    public ArrayList<ScanListViewItem2> getItemList() {
        return items;
    }

    public void setItem(int position, ScanListViewItem2 item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}



package com.FiveSGroup.TMS.TransferQR.ChuyenMa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PoReturn.Qrcode_PoReturn;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class ChuyenMa_Adapter extends RecyclerView.Adapter<ChuyenMa_Adapter.ViewHolder> {

    final ArrayList<Product_ChuyenMa> listChuyenMa;
    Context context;
    View view;

    public ChuyenMa_Adapter(Context context, ArrayList<Product_ChuyenMa> listChuyenMa) {
        this.context = context;
        this.listChuyenMa = listChuyenMa;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public ChuyenMa_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_transferqr, null, false);
        return new ChuyenMa_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ChuyenMa_Adapter.ViewHolder holder, final int position) {
        final Product_ChuyenMa product = listChuyenMa.get(position);
        holder.setIsRecyclable(false);
//        holder.edt.setText(listChuyenMa.get(position).getQTY());
//
//
//
//        holder.idproduct.setText(product.getPRODUCT_NAME());
//        holder.tvhsd.setText(product.getEXPIRED_DATE());
//        holder.tvbarcode.setText(product.getBATCH_NUMBER());
//
//
//
//
//        holder.idproduct1.setText(product.getPRODUCT_CODE());
//        holder.idproduct2.setText(product.getPRODUCT_CODE());
//        holder.idproduct3.setText(product.getPRODUCT_CODE());
//
//
//
//
//
//
//
//
//        holder.tvUnit.setText(product.getUNIT());
//
//
//        if(!product.getLPN_FROM().equals("")){
//            holder.tvFrom.setText(product.getLPN_FROM());
//        }else {
//            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
//        }
//
//        if(!product.getLPN_TO().equals("")){
//            holder.tvTo.setText(product.getLPN_TO());
//        }else {
//            holder.tvTo.setText(product.getPOSITION_TO_CODE());
//        }
//
//        if(!product.getLPN_CODE().equals("")){
//            holder.edt.setEnabled(false);
//        }
//
////        holder.layoutTo.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));
//
//        holder.tvExpired.setText(product.getEXPIRED_DATE());
//        holder.tvStockin.setText(product.getSTOCKIN_DATE());
//
//
//
//        holder.edt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
////                    DatabaseHelper.getInstance().updateProduct_ChuyenMa(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
//                } else {
//                    DatabaseHelper.getInstance().updateProduct_ChuyenMa(product, product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_TRANSFER_POSTING_CD());
//                }
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return listChuyenMa.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView idsl , idkg , idproduct1 , idproduct2 , idproduct3 , tvnameproduct , tvhsd ,tvbatch;
        EditText idsl1 , idsl2 , idsl3 ,idkg1 , idkg2 , idkg3 ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            idsl = itemView.findViewById(R.id.idsl);
            idkg = itemView.findViewById(R.id.idkg);
            idproduct1 = itemView.findViewById(R.id.idproduct1);
            idproduct2 = itemView.findViewById(R.id.idproduct2);
            idproduct3 = itemView.findViewById(R.id.idproduct3);

            tvnameproduct = itemView.findViewById(R.id.tvnameproduct);
            tvhsd = itemView.findViewById(R.id.tvhsd);

            tvbatch = itemView.findViewById(R.id.tvbatch);
            idsl1 = itemView.findViewById(R.id.idsl1);
            idsl2 = itemView.findViewById(R.id.idsl2);
            idsl3 = itemView.findViewById(R.id.idsl3);
            idkg1 = itemView.findViewById(R.id.idkg1);
            idkg2 = itemView.findViewById(R.id.idkg2);
            idkg3 = itemView.findViewById(R.id.idkg3);
            idsl1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    listChuyenMa.get(getAdapterPosition()).setQTY(idsl1.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

    private void removeItems(int position) {
        listChuyenMa.remove(position);
        notifyItemRemoved(position);
    }
}

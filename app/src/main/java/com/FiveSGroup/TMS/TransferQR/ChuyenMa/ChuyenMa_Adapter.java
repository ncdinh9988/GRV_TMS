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
import android.widget.Toast;

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
        holder.tvhsd.setText(product.getEXPIRED_DATE());
        holder.tvbatch.setText(product.getBATCH_NUMBER());
        holder.tvnameproduct.setText(product.getPRODUCT_NAME_FROM());
        holder.tvqty.setText(product.getQTY_SET_AVAILABLE_ORIGINAL());
        holder.tvunit.setText(product.getUNIT());
//        holder.idsl1.setText(listChuyenMa.get(position).getQTY_SET_AVAILABLE());
//        holder.idsl2.setText(listChuyenMa.get(position).getQTY_SET_AVAILABLE());
//        holder.idsl3.setText(listChuyenMa.get(position).getQTY_SET_AVAILABLE());


        String code = product.getPRODUCT_CODE_FROM();
        String unit = product.getUNIT();
        String chuyenma_Cd = product.getTRANSFER_POSTING_CD();
        String batch = product.getBATCH_NUMBER();


        final ArrayList<Product_ChuyenMa> chuyenma = DatabaseHelper.getInstance().getDataMaterialbyItemBasic(code , unit , chuyenma_Cd, batch);
        for (int i = 0 ; i < chuyenma.size() ; i++){
            if(i == 0){
                holder.idproduct1.setText(chuyenma.get(0).getPRODUCT_NAME_TO());
                holder.idsl1.setText(chuyenma.get(0).getQTY_SET_AVAILABLE());
                holder.idsl1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TransferPosting(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                        } else {
                            DatabaseHelper.getInstance().updateProduct_ChuyenMa(chuyenma.get(0).getPRODUCT_CODE_FROM(), chuyenma.get(0).getPRODUCT_CODE_TO(),s.toString(),product.getBATCH_NUMBER());
                        }
                    }
                });
            }
            if(i==1){
                holder.idproduct2.setText(chuyenma.get(1).getPRODUCT_NAME_TO());
                holder.idsl2.setText(chuyenma.get(1).getQTY_SET_AVAILABLE());
                holder.idsl2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TransferPosting(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                        } else {
                            DatabaseHelper.getInstance().updateProduct_ChuyenMa(chuyenma.get(1).getPRODUCT_CODE_FROM(), chuyenma.get(1).getPRODUCT_CODE_TO(),s.toString(),product.getBATCH_NUMBER());
                        }
                    }
                });
            }
            if(i==2){
                holder.idproduct3.setText(chuyenma.get(2).getPRODUCT_NAME_TO());
                holder.idsl3.setText(chuyenma.get(2).getQTY_SET_AVAILABLE());
                holder.idsl3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TransferPosting(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                        } else {
                            DatabaseHelper.getInstance().updateProduct_ChuyenMa(chuyenma.get(2).getPRODUCT_CODE_FROM(), chuyenma.get(2).getPRODUCT_CODE_TO(),s.toString(),product.getBATCH_NUMBER());
                        }
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return listChuyenMa.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvqty , tvunit , idproduct1 , idproduct2 , idproduct3 , tvnameproduct , tvhsd ,tvbatch;
        EditText idsl1 , idsl2 , idsl3 ,idkg1 , idkg2 , idkg3 ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvqty = itemView.findViewById(R.id.tvqty);
            tvunit = itemView.findViewById(R.id.tvunit);
            idproduct1 = itemView.findViewById(R.id.idproduct1);
            idproduct2 = itemView.findViewById(R.id.idproduct2);
            idproduct3 = itemView.findViewById(R.id.idproduct3);

            tvnameproduct = itemView.findViewById(R.id.tvnameproduct);
            tvhsd = itemView.findViewById(R.id.tvhsd);
            tvbatch = itemView.findViewById(R.id.tvbatch);

            idsl1 = itemView.findViewById(R.id.idsl1);
            idsl2 = itemView.findViewById(R.id.idsl2);
            idsl3 = itemView.findViewById(R.id.idsl3);

//            edt.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    listTransferPosting.get(getAdapterPosition()).setQTY(edt.getText().toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//
//            });
//
//            idkg1 = itemView.findViewById(R.id.idkg1);
//            idkg2 = itemView.findViewById(R.id.idkg2);
//            idkg3 = itemView.findViewById(R.id.idkg3);

//            idsl1.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    listChuyenMa.get(getAdapterPosition()).setUNIT(idsl1.getText().toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//
//            });


        }
    }

    private void removeItems(int position) {
        listChuyenMa.remove(position);
        notifyItemRemoved(position);
    }
}

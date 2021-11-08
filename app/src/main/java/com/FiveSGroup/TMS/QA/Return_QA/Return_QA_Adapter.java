package com.FiveSGroup.TMS.QA.Return_QA;

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
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class Return_QA_Adapter extends RecyclerView.Adapter<Return_QA_Adapter.ViewHolder> {

    final ArrayList<Product_Return_QA> listPickup;
    Context context;
    View view;

    public Return_QA_Adapter(Context context, ArrayList<Product_Return_QA> listPickup) {
        this.context = context;
        this.listPickup = listPickup;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public Return_QA_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_pickup, null, false);
        return new Return_QA_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Return_QA_Adapter.ViewHolder holder, final int position) {
        final Product_Return_QA product = listPickup.get(position);
        holder.setIsRecyclable(false);
//        holder.edt.setText(listPickup.get(position).getQTY());
        holder.edt.setText(product.getQTY());
        holder.edtnote.setText(listPickup.get(position).getNOTE());
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvUnit.setText(product.getUNIT());
        holder.tvbatchnumber.setText(product.getBATCH_NUMBER());
        holder.edt.setEnabled(false);


        holder.tvFrom.setText(product.getPOSITION_FROM_CODE());
        holder.tvTo.setText(product.getPOSITION_TO_CODE());

        holder.layoutTo.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));
        holder.layoutFrom.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());


//        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context, Qrcode_Return_QA.class);
//                intent.putExtra("position", "1");
//                intent.putExtra("product_cd", product.getPRODUCT_CD());
//                intent.putExtra("c", holder.tvExpired.getText());
//                intent.putExtra("ea_unit_position", product.getUNIT());
//                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
//                intent.putExtra("id_unique_SO", product.getAUTOINCREMENT());
//
//                context.startActivity(intent);
//
//                ((Activity) context).finish();
//            }
//        });
//        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Qrcode_Return_QA.class);
//                intent.putExtra("position", "2");
//                intent.putExtra("product_cd", product.getPRODUCT_CD());
//                intent.putExtra("c", holder.tvExpired.getText());
//                intent.putExtra("ea_unit_position", product.getUNIT());
//                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
//                intent.putExtra("id_unique_SO", product.getAUTOINCREMENT());
//
//
//                context.startActivity(intent);
//
//                ((Activity) context).finish();
//            }
//        });


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
////                    DatabaseHelper.getInstance().updateProduct_Return_QA(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
//                } else {
//                    DatabaseHelper.getInstance().updateProduct_Return_QA(product, product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_QA_CD());
//                }
//            }
//        });

        holder.edtnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_Return_QA(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                } else {
                    DatabaseHelper.getInstance().updateNote_Pickup( product.getAUTOINCREMENT(), s.toString());
                }
            }
        });


    }

    private boolean hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return true;
    }

    @Override
    public int getItemCount() {
        return listPickup.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct;
        TextView tvExpired, tvStockin ,tvbatchnumber;
        EditText edt , edtnote;
        LinearLayout layoutTo , layoutFrom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            layoutTo = itemView.findViewById(R.id.layoutTo);
            layoutFrom = itemView.findViewById(R.id.layoutFrom);
            tvbatchnumber = itemView.findViewById(R.id.tvbatchnumber);

            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvStockin = itemView.findViewById(R.id.tvStockin);

            tvExpired = itemView.findViewById(R.id.tvExpired);
            edt = itemView.findViewById(R.id.priceproduct);
            edtnote = itemView.findViewById(R.id.edtnote);
//            edt.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    listPickup.get(getAdapterPosition()).setQTY(edt.getText().toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//
//            });

            edtnote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listPickup.get(getAdapterPosition()).setNOTE(edtnote.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

    private void removeItems(int position) {
        listPickup.remove(position);
        notifyItemRemoved(position);
    }
}

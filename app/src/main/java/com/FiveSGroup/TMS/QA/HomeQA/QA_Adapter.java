package com.FiveSGroup.TMS.QA.HomeQA;

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
import com.FiveSGroup.TMS.QA.HomeQA.Image_QA.TakePhoto_QA;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class QA_Adapter extends RecyclerView.Adapter<QA_Adapter.ViewHolder> {

    final ArrayList<Product_QA> listTransferPosting;
    Context context;
    View view;

    public QA_Adapter(Context context, ArrayList<Product_QA> listTransferPosting) {
        this.context = context;
        this.listTransferPosting = listTransferPosting;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public QA_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_qa, null, false);
        return new QA_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final QA_Adapter.ViewHolder holder, final int position) {
        final Product_QA product = listTransferPosting.get(position);
        holder.setIsRecyclable(false);
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvchecked.setText(product.getCHECKED());
        holder.tvbatchnumber.setText(product.getBATCH_NUMBER());


        if(!product.getLPN_TO().equals("")){
            holder.tvTo.setText(product.getLPN_TO());
        }else {
            holder.tvTo.setText(product.getPOSITION_TO_CODE());
        }



//        holder.layoutTo.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());


        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, List_Criteria.class);
                intent.putExtra("product_code", product.getPRODUCT_CODE());
                intent.putExtra("batch_number",product.getBATCH_NUMBER());
                intent.putExtra("barcode",product.getBARCODE());
                intent.putExtra("cd",product.getSTOCK_QA_CD());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TakePhoto_QA.class);
                intent.putExtra("product_code", product.getPRODUCT_CODE());
                intent.putExtra("batch_number",product.getBATCH_NUMBER());
                intent.putExtra("stockcd",product.getSTOCK_QA_CD());
                intent.putExtra("stockindate",product.getSTOCKIN_DATE());
                intent.putExtra("exp",product.getEXPIRED_DATE());
                intent.putExtra("unit",product.getUNIT());

                context.startActivity(intent);

                ((Activity) context).finish();
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
        return listTransferPosting.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView  tvTo, tvIdProduct, tvNameProduct , tvchecked , tvbatchnumber;
        TextView tvExpired, tvStockin;
        LinearLayout layoutTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvchecked = itemView.findViewById(R.id.tvchecked);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            layoutTo = itemView.findViewById(R.id.layoutTo);
            tvbatchnumber = itemView.findViewById(R.id.tvbatchnumber);

            tvStockin = itemView.findViewById(R.id.tvStockin);

            tvExpired = itemView.findViewById(R.id.tvExpired);


        }
    }

    private void removeItems(int position) {
        listTransferPosting.remove(position);
        notifyItemRemoved(position);
    }
}

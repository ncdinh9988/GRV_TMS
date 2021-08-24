package com.FiveSGroup.TMS.QA.HomeQA;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class Criteria_Adapter extends RecyclerView.Adapter<Criteria_Adapter.ViewHolder> {

    final ArrayList<Product_Criteria> listCriterial;
    Context context;
    View view;

    public Criteria_Adapter(Context context, ArrayList<Product_Criteria> listCriterial) {
        this.context = context;
        this.listCriterial = listCriterial;

    }

    @NonNull
    @Override
    public Criteria_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_criteria, null, false);
        return new Criteria_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Criteria_Adapter.ViewHolder holder, final int position) {
        final Product_Criteria product = listCriterial.get(position);
        holder.setIsRecyclable(false);






        holder.edtunit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_Criteria(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                } else {
//                    DatabaseHelper.getInstance().updateProduct_Criteria(product, product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_TRANSFER_POSTING_CD());
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return listCriterial.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvcont, tvcriteria;
        EditText edtunit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvcont = itemView.findViewById(R.id.tvcont);
            tvcriteria = itemView.findViewById(R.id.tvcriteria);
            edtunit = itemView.findViewById(R.id.edtunit);
            edtunit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    listCriterial.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

    private void removeItems(int position) {
        listCriterial.remove(position);
        notifyItemRemoved(position);
    }
}

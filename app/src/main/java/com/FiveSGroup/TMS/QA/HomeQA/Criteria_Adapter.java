package com.FiveSGroup.TMS.QA.HomeQA;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TransferQR.ChuyenMa.Product_ChuyenMa;

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

                holder.tvcont.setText(product.getBATCH_NUMBER());
                holder.tvcriteria.setText(product.getMIC_DESC());
                holder.edtqty.setText(product.getQTY());
                holder.edtnote.setText(product.getNOTE());
                final String product_code = product.getPRODUCT_CODE();
                final String cd = product.getMATERIA_CD();


                holder.edtqty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((s.toString().equals("")) || (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000")) || (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TransferPosting(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                        } else {
                            DatabaseHelper.getInstance().updateunit_Criteria(product.getMIC_CODE(), product.getBATCH_NUMBER(), s.toString(),cd, product_code);
                        }
                    }
                });

                holder.edtnote.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((s.toString().equals("")) || (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000")) || (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TransferPosting(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getCANCEL_CD());
                        } else {
                            DatabaseHelper.getInstance().updatenote_Criteria(product.getMIC_CODE(), product.getBATCH_NUMBER(), s.toString(),cd , product_code);
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
        EditText edtqty , edtnote;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvcont = itemView.findViewById(R.id.tvcont);
            tvcriteria = itemView.findViewById(R.id.tvcriteria);
            edtqty = itemView.findViewById(R.id.edtqty);
            edtnote = itemView.findViewById(R.id.edtnote);
            edtqty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listCriterial.get(getAdapterPosition()).setQTY(edtqty.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
            edtnote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listCriterial.get(getAdapterPosition()).setNOTE(edtnote.getText().toString());
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

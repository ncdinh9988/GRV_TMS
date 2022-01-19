package com.FiveSGroup.TMS.ListOD;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class PickPositionODAdapter extends RecyclerView.Adapter<PickPositionODAdapter.ViewHolder> {

    final ArrayList<Product_OD> listOD;
    Context context;
    View view;

    public PickPositionODAdapter(Context context, ArrayList<Product_OD> listLetDown) {
        this.context = context;
        this.listOD = listLetDown;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public PickPositionODAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_pick_position, null, false);
        return new PickPositionODAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PickPositionODAdapter.ViewHolder holder, final int position) {
        final Product_OD product = listOD.get(position);
        holder.setIsRecyclable(false);
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvbatch.setText(product.getBATCH_NUMBER());
        holder.tvOD.setText(product.getQTY_OD());
        holder.edt.setText(listOD.get(position).getQTY());
        holder.tvdvt.setText(product.getUNIT());
        holder.tvsuggest.setText(product.getSUGGESTION());


        holder.edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().equals(""))) {
//                    DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE());
                    Toast.makeText(context,"Vui Lòng Không Nhập Rỗng",Toast.LENGTH_SHORT);
                } else {
                    DatabaseHelper.getInstance().updateProduct_OD(product,product.getAUTOINCREMENT(), s.toString());
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
        return listOD.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView  tvIdProduct, tvNameProduct ;
        TextView tvExpired ,tvbatch , tvOD , tvdvt , tvsuggest;
        EditText edt;

        TextView tvcont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            tvExpired = itemView.findViewById(R.id.tvExpired);
            tvbatch = itemView.findViewById(R.id.tvbatch);

            tvOD = itemView.findViewById(R.id.tvOD);



            tvdvt = itemView.findViewById(R.id.tvdvt);
            tvsuggest = itemView.findViewById(R.id.tvsuggest);

            edt = itemView.findViewById(R.id.priceproduct);
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listOD.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

}
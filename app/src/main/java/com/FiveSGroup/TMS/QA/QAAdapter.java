package com.FiveSGroup.TMS.QA;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {

    final ArrayList<QAAdapter> listtransferunit;
    Context context;
    View view;

    public QAAdapter(Context context, ArrayList<QAAdapter> listtransferunit) {
        this.context = context;
        this.listtransferunit = listtransferunit;
        // this.arrCustomerFilter = arrCustomerFilter;
    }


    private boolean hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return true;
    }

    @NonNull
    @Override
    public QAAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_transferqr, null, false);
        return new QAAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listtransferunit.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct;
        TextView tvExpired, tvStockin;
        EditText edt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);

            tvUnit = itemView.findViewById(R.id.tvUnit);

            tvStockin = itemView.findViewById(R.id.tvStockin);

            tvExpired = itemView.findViewById(R.id.tvExpired);
            edt = itemView.findViewById(R.id.priceproduct);

        }
    }

    private void removeItem(int position) {
        listtransferunit.remove(position);
        notifyItemRemoved(position);
    }
}

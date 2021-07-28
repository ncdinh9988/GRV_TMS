package com.FiveSGroup.TMS.TransferQR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TransferUnit.Change_Unit;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitActivity;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitAdapter;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitProduct;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;

import java.util.ArrayList;

public class TransferQRAdapter extends RecyclerView.Adapter<TransferQRAdapter.ViewHolder> {

    final ArrayList<TransferQRAdapter> listtransferunit;
    Context context;
    View view;

    public TransferQRAdapter(Context context, ArrayList<TransferQRAdapter> listtransferunit) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_transferqr, null, false);
        return new TransferQRAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

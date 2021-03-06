package com.FiveSGroup.TMS.TransferUnit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;

import com.FiveSGroup.TMS.LetDown.LetDownQrCodeActivity;

import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;

import java.util.ArrayList;

public class TransferUnitAdapter extends RecyclerView.Adapter<TransferUnitAdapter.ViewHolder> {

    final ArrayList<TransferUnitProduct> listtransferunit;
    Context context;
    View view;

    public TransferUnitAdapter(Context context, ArrayList<TransferUnitProduct> listtransferunit) {
        this.context = context;
        this.listtransferunit = listtransferunit;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public TransferUnitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_masterpick, null, false);
        return new TransferUnitAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TransferUnitAdapter.ViewHolder holder, final int position) {
        final TransferUnitProduct product = listtransferunit.get(position);
        holder.setIsRecyclable(false);
        holder.edt.setText(listtransferunit.get(position).getQTY());
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvcont.setText(product.getBATCH_NUMBER());
        holder.layout_cont.setVisibility(View.VISIBLE);
//        holder.edt.setText(product.getQTY());
        holder.tvUnit.setText(product.getUNIT());

        if(!product.getLPN_FROM().equals("")){
            holder.tvFrom.setText(product.getLPN_FROM());
        }else {
            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
        }


            holder.tvTo.setText(product.getUNIT_CHANGE_TO());


        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());
        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TransferUnitQrcode.class);
                intent.putExtra("position", "1");
                intent.putExtra("product_cd", product.getPRODUCT_CD());
                intent.putExtra("experied_letdown", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                intent.putExtra("id_unique_LD", product.getAUTOINCREMENT());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Change_Unit.class);
                intent.putExtra("position", "2");
                intent.putExtra("product_code", product.getPRODUCT_CODE());
                intent.putExtra("product_cd", product.getPRODUCT_CD());
                intent.putExtra("experied_letdown", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("barcode", product.getBARCODE());
                intent.putExtra("id_unique_TU", product.getAUTOINCREMENT());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        final String oldValue = holder.edt.getText().toString();



        holder.edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().equals(""))|| (s.toString().equals("0")) || (s.toString().equals("00")) || (s.toString().equals("000")) || (s.toString().equals("0000"))|| (s.toString().equals("00000"))) {
//                    DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE());
                } else {
                    DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE());
                }
            }
        });

//        holder.edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    if ((holder.edt.getText().toString().equals("")) || (holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                        // the user is done typing.
//                        Toast.makeText(context, "S??? l?????ng kh??ng ???????c b???ng kh??ng ho???c r???ng", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // the user is done typing.
//                        DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                        Toast.makeText(context, "???? c???p nh???t s??? l?????ng", Toast.LENGTH_SHORT).show();
//                        hideSoftKeyboard(view);
//
//                    }
//                }
//            }
//        });
//
//
//        holder.edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event != null &&
//                                event.getAction() == KeyEvent.ACTION_DOWN &&
//                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (event == null || !event.isShiftPressed()) {
//
//
//                        if (holder.edt.getText().toString().equals("")) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "S??? l?????ng kh??ng ???????c b???ng  r???ng", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "S??? l?????ng kh??ng ???????c b???ng kh??ng", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else {
//
//                            Toast.makeText(context, "???? c???p nh???t s??? l?????ng", Toast.LENGTH_SHORT).show();
//
//
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                            Intent intent = new Intent(context, TransferUnitActivity.class);
//                            context.startActivity(intent);
//                            ((Activity) context).finish();
//                            hideSoftKeyboard(view);
//
//                        }
//                        return true; // consume.
//
//
//                    }
//                }
//                return false;
//            }
//        });
    }

    private boolean hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return true;
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
        TextView tvcont;
        LinearLayout layout_cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            tvcont = itemView.findViewById(R.id.tvcont);
            layout_cont = itemView.findViewById(R.id.layout_cont);

            tvUnit = itemView.findViewById(R.id.tvUnit);

            tvStockin = itemView.findViewById(R.id.tvStockin);

            tvExpired = itemView.findViewById(R.id.tvExpired);
            edt = itemView.findViewById(R.id.priceproduct);
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listtransferunit.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

        }
    }

    private void removeItem(int position) {
        listtransferunit.remove(position);
        notifyItemRemoved(position);
    }
}

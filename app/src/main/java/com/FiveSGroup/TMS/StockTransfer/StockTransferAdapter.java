package com.FiveSGroup.TMS.StockTransfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;

import java.util.ArrayList;

public class StockTransferAdapter extends RecyclerView.Adapter<StockTransferAdapter.ViewHolder> {
    ArrayList<Product_StockTransfer> listStockTransfer;
    Context context;
    View view;

    public StockTransferAdapter(ArrayList<Product_StockTransfer> listStockTransfer, Context context) {
        this.listStockTransfer = listStockTransfer;
        this.context = context;
    }

    @NonNull
    @Override
    public StockTransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_masterpick, null, false);
        return new StockTransferAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final StockTransferAdapter.ViewHolder holder, final int position) {
        final Product_StockTransfer product = listStockTransfer.get(position);
        holder.setIsRecyclable(false);
        holder.edt.setText(listStockTransfer.get(position).getQTY());
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvUnit.setText(product.getUNIT());


        if(!product.getLPN_FROM().equals("")){
            holder.tvFrom.setText(product.getLPN_FROM());
        }else {
            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
        }

        if(!product.getLPN_TO().equals("")){
            holder.tvTo.setText(product.getLPN_TO());
        }else {
            holder.tvTo.setText(product.getPOSITION_TO_CODE());
        }


        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());

        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }

        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Qrcode_StockTransfer.class);
                intent.putExtra("position", "1");
                intent.putExtra("product_cd_position", product.getPRODUCT_CD());
                intent.putExtra("c_position", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date_position", product.getSTOCKIN_DATE());
                intent.putExtra("id_unique_STF", product.getAUTOINCREMENT());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Qrcode_StockTransfer.class);
                intent.putExtra("position", "2");
                intent.putExtra("product_cd_position", product.getPRODUCT_CD());
                intent.putExtra("c_position", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date_position", product.getSTOCKIN_DATE());
                intent.putExtra("id_unique_STF", product.getAUTOINCREMENT());

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
//                    DatabaseHelper.getInstance().updateProduct_StockTransfer(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE());
                } else {
                    DatabaseHelper.getInstance().updateProduct_StockTransfer(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE());
                }
            }
        });


//        holder.edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    if ((holder.edt.getText().toString().equals("")) || (holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                        // the user is done typing.
//                        Toast.makeText(context, "Số lượng không được bằng không hoặc rỗng", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // the user is done typing.
//                        DatabaseHelper.getInstance().updateProduct_StockTransfer(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                        Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(context, "Số lượng không được bằng rỗng", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_StockTransfer(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "Số lượng không được bằng không", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_StockTransfer(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else {
//
//                            Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//
//
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_StockTransfer(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                            Intent intent = new Intent(context, ListStockTransfer.class);
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
        return listStockTransfer.size();
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

            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listStockTransfer.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

        }
    }

    private void removeItem(int position) {
        listStockTransfer.remove(position);
        notifyItemRemoved(position);
    }
}

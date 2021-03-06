package com.FiveSGroup.TMS.ReturnWareHouse;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;

import java.util.ArrayList;

public class Return_Warehouse_Adapter extends RecyclerView.Adapter<Return_Warehouse_Adapter.ViewHolder> {
    final ArrayList<Product_Return_WareHouse> return_wareHouses;
    Context context;
    View view;


    public Return_Warehouse_Adapter(Context context, ArrayList<Product_Return_WareHouse> return_wareHouses) {
        this.context = context;
        this.return_wareHouses = return_wareHouses;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public Return_Warehouse_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_masterpick, null, false);
        return new Return_Warehouse_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Return_Warehouse_Adapter.ViewHolder holder, final int position) {
        final Product_Return_WareHouse product = return_wareHouses.get(position);
        holder.setIsRecyclable(false);
        holder.edt.setText(return_wareHouses.get(position).getQTY());
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvUnit.setText(product.getUNIT());
        holder.tvcont.setText(product.getBATCH_NUMBER());
        holder.layout_cont.setVisibility(View.VISIBLE);


        if(!product.getLPN_FROM().equals("")){
            holder.tvFrom.setText(product.getLPN_FROM());
        }else {
            holder.tvFrom.setText(product.getPOSITION_FROM_CODE());
        }

        if(!product.getLPN_TO().equals("")){
            holder.tvTo.setText(product.getLPN_TO());
        }else {
            holder.tvTo.setText(product.getPOSITION_TO_CODE());
        }

        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }
        holder.btnvtdi.setEnabled(false);
        holder.btnvtden.setEnabled(false);

        holder.layoutTo.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));
        holder.layoutFrom.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());


//        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context, Qrcode_Return_WareHouse.class);
//                intent.putExtra("position", "1");
//                intent.putExtra("product_cd", product.getPRODUCT_CD());
//                intent.putExtra("c", holder.tvExpired.getText());
//                intent.putExtra("ea_unit_position", product.getUNIT());
//                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
//
//                context.startActivity(intent);
//
//                ((Activity) context).finish();
//            }
//        });
//        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Qrcode_Return_WareHouse.class);
//                intent.putExtra("position", "2");
//                intent.putExtra("product_cd", product.getPRODUCT_CD());
//                intent.putExtra("c", holder.tvExpired.getText());
//                intent.putExtra("ea_unit_position", product.getUNIT());
//                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
//
//                context.startActivity(intent);
//
//                ((Activity) context).finish();
//            }
//        });

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
//                    DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE(), product.getRETURN_CD());
                } else {
                    DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getRETURN_CD());
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
//                        DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getRETURN_CD());
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
//                        if (holder.edt.getText().toString().equals("")) {
//                            // the user is done typing.
//                            Toast.makeText(context, "S??? l?????ng kh??ng ???????c b???ng r???ng", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getRETURN_CD());
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "S??? l?????ng kh??ng ???????c b???ng kh??ng", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getRETURN_CD());
//                        } else {
//
//                            Toast.makeText(context, "???? c???p nh???t s??? l?????ng", Toast.LENGTH_SHORT).show();
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_Return_WareHouse(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getRETURN_CD());
//                            Intent intent = new Intent(context, List_Return_WareHouse.class);
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
        return return_wareHouses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct;
        TextView tvExpired, tvStockin;
        EditText edt;
        LinearLayout layoutTo , layoutFrom;
        TextView tvcont;
        LinearLayout layout_cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvcont = itemView.findViewById(R.id.tvcont);
            layout_cont = itemView.findViewById(R.id.layout_cont);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            layoutTo = itemView.findViewById(R.id.layoutTo);
            layoutFrom = itemView.findViewById(R.id.layoutFrom);

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
                    return_wareHouses.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

    private void removeItems(int position) {
        return_wareHouses.remove(position);
        notifyItemRemoved(position);
    }
}

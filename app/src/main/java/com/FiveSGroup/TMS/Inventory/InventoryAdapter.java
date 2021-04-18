package com.FiveSGroup.TMS.Inventory;

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

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{
    final ArrayList<InventoryProduct> inventoryList;
    Context context;
    View view;
    InventoryAdapter myAdapter;

    public InventoryAdapter(ArrayList<InventoryProduct> inventoryList, Context context) {
        this.inventoryList = inventoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view, null, false);
        return new InventoryAdapter.ViewHolder(view);

    }

    public void removeItem(int position) {
        inventoryList.remove(position);
        notifyItemRemoved(position);
        // Add whatever you want to do when removing an Item
    }


    @Override
    public void onBindViewHolder(@NonNull final InventoryAdapter.ViewHolder holder, final int position) {
        final InventoryProduct product = inventoryList.get(position);
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.edt.setText(product.getQTY());
        holder.tvUnit.setText(product.getUNIT());
        String lpnCode = product.getLPN_FROM();
//        holder.tvTo.setText(product.getPOSITION_TO_CODE());
//        if(!lpnCode.equals("")){
//            holder.tvFrom.setText(lpnCode);
//        }else{
//            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
//        }

        if((!product.getLPN_FROM().equals(""))){
            holder.tvFrom.setText(product.getLPN_FROM());
        }else {
            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
        }

//        if((!product.getLPN_TO().equals("")) || (!product.getLPN_TO().equals(null))){
//            holder.tvTo.setText(product.getLPN_TO());
//        }else {
//            holder.tvTo.setText(product.getPOSITION_TO_CODE());
//        }

       // holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());


        holder.layoutTo.setBackground(context.getDrawable(R.drawable.bg_button_barcode_no_choose));

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());

        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }

        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, InventoryScanCode.class);
                intent.putExtra("position", "1");
                intent.putExtra("product_cd", product.getPRODUCT_CD());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        holder.btnvtdi.setEnabled(false);
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InventoryScanCode.class);
                intent.putExtra("position", "2");
                intent.putExtra("product_cd", product.getPRODUCT_CD());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());

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
                if (s.toString().equals("")) {
                    // trong KT khi để sl rỗng thì phải cập nhật "" để phân biệt giữa 0 và ""
                    DatabaseHelper.getInstance().updateProduct_Inventory(product,product.getAUTOINCREMENT(),product.getPRODUCT_CD(), "", product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_TAKE_CD());
                } else {
                    DatabaseHelper.getInstance().updateProduct_Inventory(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_TAKE_CD());
                }
            }
        });


        holder.edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {

//
//                        if (holder.edt.getText().toString().equals("")) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "Số lượng không được bằng rỗng", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_Inventory(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getSTOCK_TAKE_CD());
//                        }
//                        else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))) {
//                            // the user is done typing.
//                            Toast.makeText(context, "Số lượng không được bằng không ", Toast.LENGTH_SHORT).show();
//
////                            Toast.makeText(context, "Số lượng không được bằng 0 ", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_Inventory(product, product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getSTOCK_TAKE_CD());
//                            hideSoftKeyboard(view);
//                        }
//                        else {
                            Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();

                            // the user is done typing.
                            DatabaseHelper.getInstance().updateProduct_Inventory(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getSTOCK_TAKE_CD());

                            hideSoftKeyboard(view);

//                        }
                        return true; // consume.


                    }
                }
                return false;
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
        return inventoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct;
        TextView tvExpired, tvStockin;
        EditText edt;
        LinearLayout layoutTo;


        public void onClick(View view) {
            myAdapter.removeItem(getAdapterPosition());
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            layoutTo = itemView.findViewById(R.id.layoutTo);

            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvStockin = itemView.findViewById(R.id.tvStockin);

            tvExpired = itemView.findViewById(R.id.tvExpired);
            edt = itemView.findViewById(R.id.priceproduct);

        }
    }

    private void removeItems(int position) {
        inventoryList.remove(position);
        notifyItemRemoved(position);
    }
}

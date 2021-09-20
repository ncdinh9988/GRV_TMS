package com.FiveSGroup.TMS.PutAway;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class PutAwayAdapter extends RecyclerView.Adapter<PutAwayAdapter.ViewHolder> {

    ArrayList<Product_PutAway> listPutAway;
    Context context;
    View view;


    public PutAwayAdapter(Context context, ArrayList<Product_PutAway> listPutAway) {
        this.context = context;
        this.listPutAway = listPutAway;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public PutAwayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.new_layout_list, null, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PutAwayAdapter.ViewHolder holder, final int position) {
        final Product_PutAway product = listPutAway.get(position);
        holder.setIsRecyclable(false);
        holder.edt.setText(listPutAway.get(position).getQTY_SET_AVAILABLE());
        holder.tvIdProduct.setText(product.getPRODUCT_CODE_PUTAWAY());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME_PUTAWAY());
//        holder.edt.setText(product.getQTY_SET_AVAILABLE());
        holder.tvUnit.setText(product.getEA_UNIT_PUTAWAY());
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
        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }
        holder.tvcont.setText(product.getBATCH_NUMBER());
//        holder.layout_cont.setVisibility(View.VISIBLE);

        holder.tvExpired.setText(product.getEXPIRED_DATE_PUTAWAY());
        holder.tvStockin.setText(product.getSTOCKIN_DATE_PUTAWAY());
        holder.tvPutAwayPositionSuggest.setText(product.getSUGGESTION_POSITION());
        holder.tvPutAwayPositionSuggest.setTextColor(Color.rgb(255, 51, 0));
        holder.layout__put.setVisibility(View.VISIBLE);
        holder.layout_putaway.setVisibility(View.VISIBLE);
        holder.layout__goiy.setVisibility(View.INVISIBLE);


        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Qrcode_PutAway.class);
                intent.putExtra("position", "1");
                intent.putExtra("product_cd", product.getPRODUCT_CD_PUTAWAY());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getEA_UNIT_PUTAWAY());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE_PUTAWAY());
                intent.putExtra("id_unique_PAW", product.getAUTOINCREMENT());


                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Qrcode_PutAway.class);
                intent.putExtra("position", "2");
                intent.putExtra("product_cd", product.getPRODUCT_CD_PUTAWAY());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getEA_UNIT_PUTAWAY());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE_PUTAWAY());
                intent.putExtra("c", holder.tvExpired.getText());
                intent.putExtra("id_unique_PAW", product.getAUTOINCREMENT());


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
//                    DatabaseHelper.getInstance().updateProduct_PutAway(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD_PUTAWAY(), "0", product.getEA_UNIT_PUTAWAY(), product.getSTOCKIN_DATE_PUTAWAY());
//                    Toast.makeText(context, "Số lượng không được bằng không hoặc rỗng", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper.getInstance().updateProduct_PutAway(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD_PUTAWAY(), s.toString(), product.getEA_UNIT_PUTAWAY(), product.getSTOCKIN_DATE_PUTAWAY());
//                    Toast.makeText(context, "Đã cập nhật số lượng 2", Toast.LENGTH_SHORT).show();
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
//                        DatabaseHelper.getInstance().updateProduct_PutAway(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD_PUTAWAY(), holder.edt.getText().toString(), product.getEA_UNIT_PUTAWAY(), product.getSTOCKIN_DATE_PUTAWAY());
//                        Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//                        hideSoftKeyboard(view);
//
//                    }
//                }
//            }
//        });

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
//                        if (holder.edt.getText().toString().equals("") ) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "Số lượng không được bằng rỗng", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_PutAway(product, product.getPRODUCT_CD_PUTAWAY(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE_PUTAWAY());
//
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "Số lượng không được bằng không", Toast.LENGTH_SHORT).show();
////                            DatabaseHelper.getInstance().updateProduct_PutAway(product, product.getPRODUCT_CD_PUTAWAY(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE_PUTAWAY());
//                        } else {
//
//                            Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//
//
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_PutAway(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD_PUTAWAY(), holder.edt.getText().toString(), product.getEA_UNIT_PUTAWAY(), product.getSTOCKIN_DATE_PUTAWAY());
//                            Intent intent = new Intent(context, List_PutAway.class);
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
    private boolean hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return true;
    }
    @Override
    public int getItemCount() {
        return listPutAway.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct, tvPutAwayPositionSuggest;
        TextView tvExpired, tvStockin;
        EditText edt;
        LinearLayout layout__put;
        View layout_putaway , layout__goiy;
        TextView tvcont;
//        LinearLayout layout_cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvcont = itemView.findViewById(R.id.tvcont);
//            layout_cont = itemView.findViewById(R.id.layout_cont);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            tvPutAwayPositionSuggest = itemView.findViewById(R.id.tvPositionSuggestput);
            layout__put = itemView.findViewById(R.id.layout__put);
            layout_putaway = itemView.findViewById(R.id.layout_putaway);
            layout__goiy = itemView.findViewById(R.id.layout__goiy);


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
                    listPutAway.get(getAdapterPosition()).setQTY_SET_AVAILABLE(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

        }
    }

    private void removeItem(int position){
        listPutAway.remove(position);
        notifyItemRemoved(position);
    }
}

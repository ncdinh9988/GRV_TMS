package com.FiveSGroup.TMS.LetDown;

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
import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class LetDownAdapter extends RecyclerView.Adapter<LetDownAdapter.ViewHolder> {

    final ArrayList<ProductLetDown> listLetDown;
    Context context;
    View view;

    public LetDownAdapter(Context context, ArrayList<ProductLetDown> listLetDown) {
        this.context = context;
        this.listLetDown = listLetDown;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public LetDownAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_masterpick, null, false);
        return new LetDownAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final LetDownAdapter.ViewHolder holder, final int position) {
        final ProductLetDown product = listLetDown.get(position);
        holder.setIsRecyclable(false);
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvcont.setText(product.getBATCH_NUMBER());
        holder.layout_cont.setVisibility(View.VISIBLE);
//        holder.edt.setText(product.getQTY_SET_AVAILABLE());
        holder.edt.setText(listLetDown.get(position).getQTY_SET_AVAILABLE());
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

        if(!product.getLPN_CODE().equals("")){
            holder.edt.setEnabled(false);
        }

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());

        holder.tvLetDownPositionSuggest.setText(product.getSUGGESTION_POSITION());
        holder.tvLetDownPositionSuggest.setTextColor(Color.rgb(255, 51, 0));

        holder.tvLetDownPositionSuggestTo.setText(product.getSUGGESTION_POSITION_TO());
        holder.tvLetDownPositionSuggestTo.setTextColor(Color.rgb(255, 51, 0));

        holder.layout__let.setVisibility(View.VISIBLE);
        holder.layout__goiy.setVisibility(View.VISIBLE);

        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LetDownQrCodeActivity.class);
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
                Intent intent = new Intent(context, LetDownQrCodeActivity.class);
                intent.putExtra("position", "2");
                intent.putExtra("product_cd", product.getPRODUCT_CD());
                intent.putExtra("experied_letdown", holder.tvExpired.getText());
                intent.putExtra("ea_unit_position", product.getUNIT());
                intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                intent.putExtra("id_unique_LD", product.getAUTOINCREMENT());

                context.startActivity(intent);

                ((Activity) context).finish();
            }
        });
        final String oldValue = holder.edt.getText().toString();


//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                try {
//                    LayoutInflater factory = LayoutInflater.from(context);
//                    View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
//                    final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                    InsetDrawable inset = new InsetDrawable(back, 64);
//                    dialog.getWindow().setBackgroundDrawable(inset);
//                    dialog.setView(layout_cus);
//
//                    Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                    Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                    final TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//                    textView.setText("Bạn có chắc chắn muốn xóa sản phẩm " + product.getPRODUCT_NAME() + " không?");
//
//
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//
//                        }
//                    });
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            removeItem(position);
//                            notifyDataSetChanged();
//                            DatabaseHelper.getInstance().deleteProduct_Letdown_Specific(product.getPRODUCT_CODE(), product.getEXPIRED_DATE(), product.getSTOCKIN_DATE(), product.getUNIT());
//                            Toast.makeText(context, "Đã xóa sản phẩm " + product.getPRODUCT_NAME(), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                } catch (Exception e) {
//                    Log.e("Exception", e.getMessage());
//                }
//                return true;
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
//                    DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), "0", product.getUNIT(), product.getSTOCKIN_DATE());
                } else {
                    DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), s.toString(), product.getUNIT(), product.getSTOCKIN_DATE());
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
//                        DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                        Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//                        hideSoftKeyboard(view);
//
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
//                            Toast.makeText(context, "Số lượng không được bằng  rỗng", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//
//                            Toast.makeText(context, "Số lượng không được bằng không", Toast.LENGTH_SHORT).show();
//                            DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE());
//                        } else {
//
//                            Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//
//
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_LetDown(product,product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE());
//                            Intent intent = new Intent(context, LetDownActivity.class);
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
        return listLetDown.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct, tvLetDownPositionSuggest , tvLetDownPositionSuggestTo;
        TextView tvExpired, tvStockin;
        EditText edt;
        LinearLayout layout__let , layout__goiy;
        TextView tvcont;
        LinearLayout layout_cont;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvcont = itemView.findViewById(R.id.tvcont);
            layout_cont = itemView.findViewById(R.id.layout_cont);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            tvLetDownPositionSuggest =itemView.findViewById(R.id.tvPositionSuggestlet);
            tvLetDownPositionSuggestTo =itemView.findViewById(R.id.tvPositionSuggestto);
            layout__let = itemView.findViewById(R.id.layout__let);
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
                    listLetDown.get(getAdapterPosition()).setQTY_SET_AVAILABLE(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });


        }
    }

    private void removeItem(int position) {
        listLetDown.remove(position);
        notifyItemRemoved(position);
    }
}

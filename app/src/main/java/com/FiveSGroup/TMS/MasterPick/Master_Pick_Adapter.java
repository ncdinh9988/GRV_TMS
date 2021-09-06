package com.FiveSGroup.TMS.MasterPick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;

public class Master_Pick_Adapter extends RecyclerView.Adapter<Master_Pick_Adapter.ViewHolder> {

    final ArrayList<Product_Master_Pick> masterPicks;
    Context context;
    View view;

    public Master_Pick_Adapter(Context context, ArrayList<Product_Master_Pick> masterPicks) {
        this.context = context;
        this.masterPicks = masterPicks;
        // this.arrCustomerFilter = arrCustomerFilter;
    }

    @NonNull
    @Override
    public Master_Pick_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_view_masterpick, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final Master_Pick_Adapter.ViewHolder holder, final int position) {
        final Product_Master_Pick product = masterPicks.get(position);
        holder.setIsRecyclable(false);
        holder.tvIdProduct.setText(product.getPRODUCT_CODE());
        holder.tvNameProduct.setText(product.getPRODUCT_NAME());
        holder.tvUnit.setText(product.getUNIT());
//        holder.layout__put.setVisibility(View.VISIBLE);
//        holder.layout_putaway.setVisibility(View.VISIBLE);
//        holder.layout__goiy.setVisibility(View.INVISIBLE);
//        holder.tvvtgy.setText("Vị Trí Lấy Hàng: ");
        holder.edt.setText(masterPicks.get(position).getQTY());
        holder.tvMasterPickPositionSuggest.setText(product.getSUGGESTION_POSITION());
        holder.tvMasterPickPositionSuggest.setTextColor(Color.rgb(255, 51, 0));
        holder.layout_cont.setVisibility(View.VISIBLE);
        holder.tvcont.setText(product.getBATCH_NUMBER());


        if (!product.getLPN_FROM().equals("")) {
            holder.tvFrom.setText(product.getLPN_FROM());
        } else {
            holder.tvFrom.setText(product.getPOSITION_FROM_CODE() + " - " + product.getPOSITION_FROM_DESCRIPTION());
        }

        if (!product.getLPN_TO().equals("")) {
            holder.tvTo.setText(product.getLPN_TO());
        } else {
            holder.tvTo.setText(product.getPOSITION_TO_CODE());
        }

        if (!product.getLPN_CODE().equals("")) {
            holder.edt.setEnabled(false);
        }

        holder.tvExpired.setText(product.getEXPIRED_DATE());
        holder.tvStockin.setText(product.getSTOCKIN_DATE());


        holder.btnvtden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getLPN_CODE().equals("")) {
                    Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("product_cd", product.getPRODUCT_CD());
                    intent.putExtra("c", holder.tvExpired.getText());
                    intent.putExtra("ea_unit_position", product.getUNIT());
                    intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                    intent.putExtra("unique_id", product.getAUTOINCREMENT());

                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    if (!product.getPOSITION_FROM_CODE().equals("---")) {
                        try {
                            LayoutInflater factory = LayoutInflater.from(context);
                            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
                            final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                            InsetDrawable inset = new InsetDrawable(back, 64);
                            dialog.getWindow().setBackgroundDrawable(inset);
                            dialog.setView(layout_cus);

                            Button btnNo = layout_cus.findViewById(R.id.btnNo);
                            Button btnYes = layout_cus.findViewById(R.id.btnYes);
                            final TextView textView = layout_cus.findViewById(R.id.tvTextBack);
                            textView.setText("Nhân đôi sản phẩm " + product.getPRODUCT_NAME() + " không?");
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                                    intent.putExtra("position", "1");
                                    intent.putExtra("product_cd", product.getPRODUCT_CD());
                                    intent.putExtra("c", holder.tvExpired.getText());
                                    intent.putExtra("ea_unit_position", product.getUNIT());
                                    intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                                    intent.putExtra("unique_id", product.getAUTOINCREMENT());
                                    context.startActivity(intent);
                                    ((Activity) context).finish();

                                }
                            });
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    DatabaseHelper.getInstance().CreateMaster_Pick(product);
                                    Intent intent = new Intent(context, List_Master_Pick.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });
                            dialog.show();
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    } else {
                        Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                        intent.putExtra("position", "1");
                        intent.putExtra("product_cd", product.getPRODUCT_CD());
                        intent.putExtra("c", holder.tvExpired.getText());
                        intent.putExtra("ea_unit_position", product.getUNIT());
                        intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                        intent.putExtra("unique_id", product.getAUTOINCREMENT());

                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
            }
        });
        holder.btnvtdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getLPN_CODE().equals("")) {
                    Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                    intent.putExtra("position", "2");
                    intent.putExtra("product_cd", product.getPRODUCT_CD());
                    intent.putExtra("c", holder.tvExpired.getText());
                    intent.putExtra("ea_unit_position", product.getUNIT());
                    intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                    intent.putExtra("unique_id", product.getAUTOINCREMENT());

                    context.startActivity(intent);

                    ((Activity) context).finish();
                } else {
                    if (!product.getPOSITION_TO_CODE().equals("---")) {
                        try {
                            LayoutInflater factory = LayoutInflater.from(context);
                            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
                            final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                            InsetDrawable inset = new InsetDrawable(back, 64);
                            dialog.getWindow().setBackgroundDrawable(inset);
                            dialog.setView(layout_cus);

                            Button btnNo = layout_cus.findViewById(R.id.btnNo);
                            Button btnYes = layout_cus.findViewById(R.id.btnYes);
                            final TextView textView = layout_cus.findViewById(R.id.tvTextBack);
                            textView.setText("Nhân đôi sản phẩm " + product.getPRODUCT_NAME() + " không?");
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                                    intent.putExtra("position", "2");
                                    intent.putExtra("product_cd", product.getPRODUCT_CD());
                                    intent.putExtra("c", holder.tvExpired.getText());
                                    intent.putExtra("ea_unit_position", product.getUNIT());
                                    intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                                    intent.putExtra("unique_id", product.getAUTOINCREMENT());
                                    context.startActivity(intent);
                                    ((Activity) context).finish();

                                }
                            });
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    DatabaseHelper.getInstance().CreateMaster_Pick(product);
                                    Intent intent = new Intent(context, List_Master_Pick.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });
                            dialog.show();
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    } else {
                        Intent intent = new Intent(context, Qrcode_Master_Pick.class);
                        intent.putExtra("position", "2");
                        intent.putExtra("product_cd", product.getPRODUCT_CD());
                        intent.putExtra("c", holder.tvExpired.getText());
                        intent.putExtra("ea_unit_position", product.getUNIT());
                        intent.putExtra("stockin_date", product.getSTOCKIN_DATE());
                        intent.putExtra("unique_id", product.getAUTOINCREMENT());

                        context.startActivity(intent);

                        ((Activity) context).finish();
                    }
                }

            }
        });
        final String oldValue = holder.edt.getText().toString();

//        holder.edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    if ((holder.edt.getText().toString().equals("")) || (holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000"))|| (holder.edt.getText().toString().equals("0000"))|| (holder.edt.getText().toString().equals("00000"))) {
//                        // the user is done typing.
//                    } else {
//                        // the user is done typing.
//                        DatabaseHelper.getInstance().updateProduct_Master_Pick(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getMASTER_PICK_CD());
//                        Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//
//
//
//                    }
//                }
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

                if ((holder.edt.getText().toString().equals("")) || (holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000")) || (holder.edt.getText().toString().equals("0000")) || (holder.edt.getText().toString().equals("00000"))) {
                    // the user is done typing.
                } else {
                    // the user is done typing.
                    DatabaseHelper.getInstance().updateProduct_Master_Pick(product, product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getMASTER_PICK_CD());
//                    Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
                }
            }

        });
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
//                            Toast.makeText(context, "Số lượng không được bằng rỗng", Toast.LENGTH_SHORT).show();
//                            //  DatabaseHelper.getInstance().updateProduct_Master_Pick(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getMASTER_PICK_CD());
//                        } else if ((holder.edt.getText().toString().equals("0")) || (holder.edt.getText().toString().equals("00")) || (holder.edt.getText().toString().equals("000")) || (holder.edt.getText().toString().equals("0000")) || (holder.edt.getText().toString().equals("00000"))) {
//                            // the user is done typing.
//                            Toast.makeText(context, "Số lượng không được bằng không", Toast.LENGTH_SHORT).show();
//                            // DatabaseHelper.getInstance().updateProduct_Master_Pick(product, product.getAUTOINCREMENT(),product.getPRODUCT_CD(), holder.edt.getText().toString(), "0", product.getSTOCKIN_DATE(), product.getMASTER_PICK_CD());
//                        } else {
//                            // the user is done typing.
//                            DatabaseHelper.getInstance().updateProduct_Master_Pick(product, product.getAUTOINCREMENT(), product.getPRODUCT_CD(), holder.edt.getText().toString(), product.getUNIT(), product.getSTOCKIN_DATE(), product.getMASTER_PICK_CD());
//                            //DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());
//                            Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(context, List_Master_Pick.class);
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
        return masterPicks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnvtden, btnvtdi;
        TextView tvFrom, tvUnit, tvTo, tvIdProduct, tvNameProduct, tvMasterPickPositionSuggest;
        TextView tvExpired, tvStockin, tvvtgy , tvcont;
        EditText edt;
        LinearLayout layout__put , layout_cont;
        View layout_putaway, layout__goiy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMasterPickPositionSuggest = itemView.findViewById(R.id.tvPositionSuggestput);
//            layout__put = itemView.findViewById(R.id.layout__put);
//            layout_putaway = itemView.findViewById(R.id.layout_putaway);
//            layout__goiy = itemView.findViewById(R.id.layout__goiy);
//            tvvtgy = itemView.findViewById(R.id.tvvtgy);

            layout_cont = itemView.findViewById(R.id.layout_cont);
            btnvtden = itemView.findViewById(R.id.btnvtden);
            btnvtdi = itemView.findViewById(R.id.btnvtdi);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvIdProduct = itemView.findViewById(R.id.idproduct);
            tvNameProduct = itemView.findViewById(R.id.nameproduct);
            tvcont = itemView.findViewById(R.id.tvcont);

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
                    masterPicks.get(getAdapterPosition()).setQTY(edt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

        }
    }


    private void removeItems(int position) {
        masterPicks.remove(position);
        notifyItemRemoved(position);
    }
}
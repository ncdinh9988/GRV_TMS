package com.FiveSGroup.TMS.Warehouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class ListQrcode extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String stock_in = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";


    String clickShowListCode = "";

    String lpn = "";

    int result;
    String saleCode = CmnFns.readDataAdmin();
    int statusGetCust;
    Product_Qrcode product_qrcode;

    ArrayList<Product_Qrcode> qrcode1;
//    ArrayList<Product_PutAway> putaway;
    CheckEventbus eventbus;

    TextView tvTitle;
    //PutAwayListAdapter putAwayListAdapter;

//    public static PutAwayAdapter putAwayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qrcode);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getDataFromIntent();

        init();
        btnscan_barcode.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);

        prepareData();


    }


    private void prepareData() {
//        if (put_away != null) {
//            tvTitle.setText("Danh Sách SP Put Away");
//            if (positonReceive == null) {
//                if (lpn != null && value1 != null) {
//                    //TODO
//                    alert_show_SP(1);
//                } else if (lpn == null && value1 != null) {
//                    //TODO
//                    alert_show_SP(0);
//                }
//
//            } else {
//                if (lpn != null && value1 != null) {
//                    //TODO
//                    alert_show_position(1);
//                } else if (lpn == null && value1 != null) {
//                    //TODO
//                    alert_show_position(0);
//                }
//
//            }
//
//            putaway = DatabaseHelper.getInstance().getAllProduct_PutAway();
//            //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
//            putAwayListAdapter = new PutAwayAdapter(this, putaway);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//            listViewProduct.setLayoutManager(layoutManager);
//            listViewProduct.setAdapter(putAwayListAdapter);
//            putAwayListAdapter.notifyDataSetChanged();
//
//            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//
//                @Override
//                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                    Toast.makeText(ListQrcode.this, "on Move", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                @Override
//                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                    LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//                    View layout_cus = factory.inflate(R.layout.layout_delete, null);
//                    final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                    InsetDrawable inset = new InsetDrawable(back, 64);
//                    dialog.getWindow().setBackgroundDrawable(inset);
//                    dialog.setView(layout_cus);
//
//                    Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                    Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                    TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
//                            dialog.dismiss();
//                            finish();
//                            Intent i = new Intent(ListQrcode.this,ListQrcode.class);
//                            startActivity(i);
//
//                        }
//                    });
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //Remove swiped item from list and notify the RecyclerView
//                            dialog.dismiss();
//
//                            int position = viewHolder.getAdapterPosition();
//                            Product_PutAway product = putaway.get(position);
//                            putaway.remove(position);
//                            DatabaseHelper.getInstance().deleteProduct_PutAway_Specific(product.getAUTOINCREMENT());
//                            putAwayListAdapter.notifyItemRemoved(position);
//                        }
//                    });
//                    dialog.show();
//
//
//                }
//            };
//            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//            itemTouchHelper.attachToRecyclerView(listViewProduct);
//        } else {
            tvTitle.setText("Danh Sách SP Nhập Kho");
            //Dành cho nhập kho chỉ gửi lên qrcode
            if (clickShowListCode != null) {
                qrcode1 = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());
                productListViewAdapter = new ProductAdapter(this, qrcode1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(productListViewAdapter);
                return;
            }
//            if (positonReceive != null) {
//                alert_show_position_warehouse(0);
//            } else {
//                alert_show_SP_Stock_in();
//            }
        if(stock_in!=null){
            if (positonReceive == null) {
                alert_show_SP_Stock_in();
            }
        }



            qrcode1 = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());
            if (qrcode1 != null) {
                //productListViewAdapter = new ProductListViewAdapter(qrcode1, this);
                productListViewAdapter = new ProductAdapter(this, qrcode1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(productListViewAdapter);
                productListViewAdapter.notifyDataSetChanged();
                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        Toast.makeText(ListQrcode.this, "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
                        View layout_cus = factory.inflate(R.layout.layout_delete, null);
                        final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                        InsetDrawable inset = new InsetDrawable(back, 64);
                        dialog.getWindow().setBackgroundDrawable(inset);
                        dialog.setView(layout_cus);

                        Button btnNo = layout_cus.findViewById(R.id.btnNo);
                        Button btnYes = layout_cus.findViewById(R.id.btnYes);
                        TextView textView = layout_cus.findViewById(R.id.tvTextBack);


                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
                                dialog.dismiss();
                                finish();
                                Intent i = new Intent(ListQrcode.this,ListQrcode.class);
                                startActivity(i);

                            }
                        });
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Remove swiped item from list and notify the RecyclerView
                                dialog.dismiss();

                                int position = viewHolder.getAdapterPosition();
                                Product_Qrcode product = qrcode1.get(position);
                                qrcode1.remove(position);
                                DatabaseHelper.getInstance().deleteProduct_PO_Specific(product.getAUTOINCREMENT());
                                productListViewAdapter.notifyItemRemoved(position);
                            }
                        });
                        dialog.show();


                    }
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(listViewProduct);
            }
//        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        stock_in = intent.getStringExtra("stock_in");
        ea_unit = intent.getStringExtra("ea_unit");

        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        stockinDate = intent.getStringExtra("stockin_date");
        lpn = intent.getStringExtra("lpn");
        clickShowListCode = intent.getStringExtra("clickShowListCode");

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

    }


    private boolean isQuanityZero() {
        boolean check = false;
//        if (put_away != null) {
//            List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
//            for (int i = 0; i < product.size(); i++) {
//                Product_PutAway putAway = product.get(i);
//                String valueQty = putAway.getQTY_SET_AVAILABLE();
//                if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
//                    check = true;
//                }
//            }
//        } else {
            List<Product_Qrcode> product = DatabaseHelper.getInstance().getAllProduct_Qrcode();
            for (int i = 0; i < product.size(); i++) {
                Product_Qrcode putAway = product.get(i);
                String valueQty = putAway.getSL_SET();
                if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
                    check = true;
                }
//            }
        }

        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

//    private boolean isNotScanFromOrTo() {
//        boolean check = false;
//        if (put_away != null) {
//            List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
//
//            for (int i = 0; i < product.size(); i++) {
//                Product_PutAway putAway = product.get(i);
//                String value0 = "---";
//                String valueAm1 = "-1";
//                String valueFromCode = putAway.getPOSITION_FROM_CODE();
//                String valueToCode = putAway.getPOSITION_TO_CODE();
//                String positionCode = putAway.getPOSITION_FROM_CODE();
//                String valueCode = putAway.getLPN_CODE();
//                if (valueFromCode.equals("") || valueFromCode.equals("---")) {
//                    if ((positionCode.equals(value0) || (positionCode.equals(valueAm1)))) {
//                        check = true;
//                    }
//                } else {
//                    if ((valueFromCode.equals("")) || (valueToCode.equals("---"))) {
//                        check = true;
//                    }
//                }
//            }
//            if (check == true) {
//                return check;
//            } else {
//                return false;
//            }
//        }
//        return check;
//    }

    private boolean isNotScanFromOrToWarehouse() {
        boolean check = false;
        List<Product_Qrcode> product = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());

        for (int i = 0; i < product.size(); i++) {
            Product_Qrcode product_qrcode = product.get(i);
            String value0 = "---";
            String valueAm1 = "-1";
            String valueToCode = product_qrcode.getPRODUCT_TO();
            String positionCode = product_qrcode.getPOSITION_CODE();
            if (valueToCode.equals("")) {
                if (positionCode.equals("") || positionCode.equals(value0) || positionCode.equals(valueAm1)) {
                    check = true;
                }
            }
        }
        if (check == true) {
            return check;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void actionBack() {
//        if (put_away == null) {
//            LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
//            final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//            InsetDrawable inset = new InsetDrawable(back, 64);
//            dialog.getWindow().setBackgroundDrawable(inset);
//            dialog.setView(layout_cus);
//
//            Button btnNo = layout_cus.findViewById(R.id.btnNo);
//            Button btnYes = layout_cus.findViewById(R.id.btnYes);
//            TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//
//                }
//            });
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DatabaseHelper.getInstance().deleteAllProduct_Qrcode();
//                    DatabaseHelper.getInstance().deleteallEa_Unit();
//                    dialog.dismiss();
//                    Intent intent = new Intent(ListQrcode.this, HomeQRActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//            dialog.show();
            Intent intent = new Intent(ListQrcode.this, HomeQRActivity.class);
            startActivity(intent);
            finish();
//        } else {
//            try {
//                LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//                View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
//                final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                InsetDrawable inset = new InsetDrawable(back, 64);
//                dialog.getWindow().setBackgroundDrawable(inset);
//                dialog.setView(layout_cus);
//
//                Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//                btnNo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//
//                    }
//                });
//                btnYes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DatabaseHelper.getInstance().deleteProduct_PutAway();
//                        DatabaseHelper.getInstance().deleteallEa_Unit();
//                        dialog.dismiss();
//                        Intent intent = new Intent(ListQrcode.this, MainWareHouseActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                dialog.show();
//            } catch (Exception e) {
//                Log.e("Exception", e.getMessage());
//            }
//
//        }
    }

    private void synchronizeToServer() {

        Dialog dialog = new Dialog(ListQrcode.this);
        try {

            if (qrcode1.size() > 0) {
                if (isNotScanFromOrToWarehouse()) {
                    dialog.showDialog(ListQrcode.this, "Chưa có VT Từ hoặc VT Đến");

                } else if (isQuanityZero()) {
                    dialog.showDialog(ListQrcode.this, "Số lượng SP không được bằng 0");

                } else {

                    result = new CmnFns().synchronizeStockReceiptChecked(ListQrcode.this,saleCode);

                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
                    } else {
                        if (result == -2) {
                            dialog.showDialog(ListQrcode.this, "Số lượng không đủ trong tồn kho");
                        } else if (result == -3) {
                            dialog.showDialog(ListQrcode.this, "Vị trí từ không hợp lệ");
                        } else if (result == -4) {
                            dialog.showDialog(ListQrcode.this, "Trạng thái của phiếu không hợp lệ");
                        } else if (result == -5) {
                            dialog.showDialog(ListQrcode.this, "Vị trí từ trùng vị trí đên");
                        } else if (result == -6) {
                            dialog.showDialog(ListQrcode.this, "Vị trí đến không hợp lệ");
                        } else if (result == -7) {
                            dialog.showDialog(ListQrcode.this, "Cập nhật trạng thái thất bại");
                        } else if (result == -8) {
                            dialog.showDialog(ListQrcode.this, "Sản phẩm không có thông tin trên phiếu ");
                        } else if (result == -13) {
                            dialog.showDialog(ListQrcode.this, "Dữ liệu không hợp lệ");
                        } else if (result == -24) {
                            dialog.showDialog(ListQrcode.this, "Vui Lòng Kiểm Tra Lại Số Lượng");
                        }else if (result == -25) {
                            dialog.showDialog(ListQrcode.this, "Số Lượng Vượt Quá Yêu Cầu Trên PO");
                        }
                        else {
                            dialog.showDialog(ListQrcode.this, "Lưu thất bại");
                        }

                    }
                }
            } else {
                dialog.showDialog(ListQrcode.this, "Không có sản phẩm");

            }
        } catch (Exception e) {

        }

    }
//    private void synchronizeseverputawway(){
//        Dialog dialog = new Dialog(ListQrcode.this);
//        try {
//            if (putaway.size() > 0) {
//                if (isNotScanFromOrTo()) {
//                    dialog.showDialog(ListQrcode.this, "Chưa có VT Từ hoặc VT Đến");
//
//                } else if (isQuanityZero()) {
//                    dialog.showDialog(ListQrcode.this, "Số lượng SP không được bằng 0");
//
//                } else {
//                    if (put_away != null) {
//                        try {
//
//                            result = new CmnFns().synchronizeData(saleCode, "WPA", "");
//
//                            if (result >= 1) {
//                                ShowSuccessMessage("Lưu thành công");
//
//                            } else {
//                                if (result == -2) {
//                                    dialog.showDialog(ListQrcode.this, "Số lượng không đủ trong tồn kho");
//                                } else if (result == -3) {
//                                    dialog.showDialog(ListQrcode.this, "Vị trí từ không hợp lệ");
//                                } else if (result == -4) {
//                                    dialog.showDialog(ListQrcode.this, "Trạng thái của phiếu không hợp lệ");
//                                } else if (result == -5) {
//                                    dialog.showDialog(ListQrcode.this, "Vị trí từ trùng vị trí đên");
//                                } else if (result == -6) {
//                                    dialog.showDialog(ListQrcode.this, "Vị trí đến không hợp lệ");
//                                } else if (result == -7) {
//                                    dialog.showDialog(ListQrcode.this, "Cập nhật trạng thái thất bại");
//                                } else if (result == -8) {
//                                    dialog.showDialog(ListQrcode.this, "Sản phẩm không có thông tin trên phiếu ");
//                                } else if (result == -13) {
//                                    dialog.showDialog(ListQrcode.this, "Dữ liệu không hợp lệ");
//
//                                }else if (result == -24) {
//                                    dialog.showDialog(ListQrcode.this, "Vui Lòng Kiểm Tra Lại Số Lượng");
//
//                                } else {
//                                    dialog.showDialog(ListQrcode.this, "Lưu thất bại");
//                                }
//
//                            }
//                        }catch (Exception e){
//                            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
//                            return ;
//                        }
//
//                    }
//                }
//            } else {
//                dialog.showDialog(ListQrcode.this, "Không có sản phẩm");
//
//            }
//        } catch (Exception e) {
//
//        }
//    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);
        dialog.setCancelable(false);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);
        btnClose.setText("OK");


        textView.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                if (put_away != null) {
//                    DatabaseHelper.getInstance().deleteProduct_PutAway();
//                    putaway.clear();
//                    putAwayListAdapter.notifyDataSetChanged();
//
//                    Intent intentToHomeQRActivity = new Intent(ListQrcode.this, Wv_ShowResultQrode.class);
//                    intentToHomeQRActivity.putExtra("result_WPA", result);
//                    intentToHomeQRActivity.putExtra("type_WPA", "WPA");
//
//                    startActivity(intentToHomeQRActivity);
//
//                    finish();
//                } else {
                    DatabaseHelper.getInstance().deleteProduct_Qrcode(global.getStockReceiptCd());
                    qrcode1.clear();
                    productListViewAdapter.notifyDataSetChanged();
                    Intent intentToHomeQRActivity = new Intent(ListQrcode.this, HomeQRActivity.class);
                    startActivity(intentToHomeQRActivity);

                    finish();
                }
//            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
//                if (put_away != null) {
//                    Intent intent = new Intent(ListQrcode.this, Qrcode_PutAway.class);
//                    intent.putExtra("check_to_finish_at_list", "check");
//                    startActivity(intent);
//                    finish();
//                } else {
                    Intent intent = new Intent(ListQrcode.this, Qrcode.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
//                }
                break;

            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
//                if(put_away!=null){
//                    actionSyn();
//                }else{
                    synchronizeToServer();
//                }
                break;
        }
    }

//    private void actionSyn(){
//        try {
//            LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//            View layout_cus = factory.inflate(R.layout.layout_request, null);
//            final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//            InsetDrawable inset = new InsetDrawable(back, 64);
//            dialog.getWindow().setBackgroundDrawable(inset);
//            dialog.setView(layout_cus);
//
//            Button btnNo = layout_cus.findViewById(R.id.btnNo);
//            Button btnYes = layout_cus.findViewById(R.id.btnYes);
//            TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//
//                }
//            });
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    synchronizeseverputawway();
//                }
//            });
//            dialog.show();
//        } catch (Exception e) {
//            Log.e("Exception", e.getMessage());
//        }
//
//    }


    @Override
    protected void onResume() {

        super.onResume();
    }

//    public void alert_show_position(int isLPN) {
//        String positionTo = "";
//        String positionFrom = "";
//        ArrayList<Product_PutAway> putAways = new ArrayList<>();
//        putAways = DatabaseHelper.getInstance().getAllProduct_PutAway_Sync();
//        for (int i = 0; i < putAways.size(); i++) {
//            Product_PutAway putAway = putAways.get(i);
//            if (productCd.equals(putAway.getPRODUCT_CD_PUTAWAY()) &&
//                    expDate1.equals(putAway.getEXPIRED_DATE_PUTAWAY()) &&
//                    stockinDate.equals(putAway.getSTOCKIN_DATE_PUTAWAY()) &&
//                    ea_unit_position.equals(putAway.getEA_UNIT_PUTAWAY())) {
//                if (!putAway.getLPN_FROM().equals("") || !putAway.getLPN_TO().equals("")) {
//                    positionTo = putAway.getLPN_TO();
//                    positionFrom = putAway.getLPN_FROM();
//                }
//                if (!putAway.getPOSITION_FROM_CODE().equals("") || !putAway.getPOSITION_TO_CODE().equals("")) {
//                    positionTo = putAway.getPOSITION_TO_CODE();
//                    positionFrom = putAway.getPOSITION_FROM_CODE();
//                }
//
//                // if này là để trả lại giá trị from và to nếu người dùng muốn quét lại VTT và VTĐ
//                if (positonReceive.equals("1")) {
//                    if (!positionTo.equals("") && !positionFrom.equals("")) {
//                        positionFrom = "";
//                    }
//                } else {
//                    if (!positionTo.equals("") && !positionFrom.equals("")) {
//                        positionTo = "";
//                    }
//                }
//            }
//        }
//        try {
//            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPA", isLPN);
//
//            Dialog dialog = new Dialog(ListQrcode.this);
//
//            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
//                dialog.showDialog(ListQrcode.this, "Vui Lòng Thử Lại");
//
//            } else if (postitionDes.equals("-9")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí không hợp lệ");
//
//            } else if (postitionDes.equals("-3")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí từ không hợp lệ");
//
//            } else if (postitionDes.equals("-6")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí đến không hợp lệ");
//
//            } else if (postitionDes.equals("-5")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí từ trùng vị trí đến");
//
//            } else if (postitionDes.equals("-14")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí đến trùng vị trí từ");
//
//            } else if (postitionDes.equals("-15")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí từ không có trong hệ thống");
//
//            } else if (postitionDes.equals("-10")) {
//                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong hệ thống");
//
//            } else if (postitionDes.equals("-17")) {
//                dialog.showDialog(ListQrcode.this, "LPN từ trùng LPN đến");
//
//            } else if (postitionDes.equals("-18")) {
//                dialog.showDialog(ListQrcode.this, "LPN đến trùng LPN từ");
//
//            } else if (postitionDes.equals("-19")) {
//                dialog.showDialog(ListQrcode.this, "Vị trí đến không có trong hệ thống");
//
//            } else if (postitionDes.equals("-12")) {
//                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong tồn kho");
//
//            } else {
//                return;
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//    }

    public void alert_show_position_warehouse(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Qrcode> product_qrcodes = new ArrayList<>();
        product_qrcodes = DatabaseHelper.getInstance().getAllProduct_Qrcode_Sync(stock);
        for (int i = 0; i < product_qrcodes.size(); i++) {
            Product_Qrcode product_qrcode = product_qrcodes.get(i);
            if (productCd.equals(product_qrcode.getPRODUCT_CD()) &&
                    expDate.equals(product_qrcode.getEXPIRED_DATE()) &&
                    stockinDate.equals(product_qrcode.getSTOCKIN_DATE()) &&
                    ea_unit.equals(product_qrcode.getEA_UNIT())) {
                if (!product_qrcode.getPOSITION_CODE().equals("")) {
                    positionTo = product_qrcode.getPOSITION_CODE();
                }

                // if này là để trả lại giá trị from và to nếu người dùng muốn quét lại VTT và VTĐ
                if (positonReceive.equals("1")) {
                    if (!positionTo.equals("") && !positionFrom.equals("")) {
                        positionFrom = "";
                    }
                } else {
                    if (!positionTo.equals("") && !positionFrom.equals("")) {
                        positionTo = "";
                    }
                }
            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate, ea_unit, stockinDate, positionFrom, positionTo, "WSI", isLPN);

            Dialog dialog = new Dialog(ListQrcode.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(ListQrcode.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-9")) {
                dialog.showDialog(ListQrcode.this, "Vị trí không hợp lệ");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(ListQrcode.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(ListQrcode.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(ListQrcode.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(ListQrcode.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(ListQrcode.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(ListQrcode.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(ListQrcode.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(ListQrcode.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong tồn kho");

            } else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }

//    public void alert_show_SP(int isLPN) {
//        try {
//            int postitionDes = new CmnFns().synchronizeGETProductByZonePutaway(ListQrcode.this, value1,
//                    CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, isLPN );
//
//            Dialog dialog = new Dialog(ListQrcode.this);
//
//
//            if (postitionDes == 1) {
//                return;
//            } else if (postitionDes == -1) {
//                dialog.showDialog(ListQrcode.this, "Vui Lòng Thử Lại");
//
//            } else if (postitionDes == -8) {
//                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trên phiếu");
//
//
//            } else if (postitionDes == -10) {
//                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong hệ thống");
//
//            } else if (postitionDes == -11) {
//
//                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trong kho");
//
//
//            } else if (postitionDes == -12) {
//
//                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong kho");
//
//            } else if (postitionDes == -16) {
//
//                dialog.showDialog(ListQrcode.this, "Sản phẩm đã quét không nằm trong LPN nào");
//
//            } else if (postitionDes == -20) {
//
//                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trong hệ thống");
//
//            } else if (postitionDes == -21) {
//
//                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trong zone nhập");
//
//            } else if (postitionDes == -22) {
//
//                dialog.showDialog(ListQrcode.this, "Mã LPN không có trong zone nhập");
//
//            }
//        }catch (Exception e){
//            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//
//    }

    public void alert_show_SP_Stock_in() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
            String stockReceipt = sharedPreferences.getString("stock", "");

            int statusGetCust = new CmnFns().synchronizeGETProductInfo(saleCode ,value1, stockReceipt, expDate, stockinDate,
                    ea_unit, positonReceive );

            Dialog dialog = new Dialog(ListQrcode.this);
            if (statusGetCust == 1) {
                return;
            } else if (statusGetCust == -1) {
                dialog.showDialog(ListQrcode.this, "Vui Lòng Thử Lại");
            } else if (statusGetCust == -8) {
                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trên phiếu");
            } else if (statusGetCust == -20) {
                dialog.showDialog(ListQrcode.this, "Mã sản phẩm không có trong hệ thống");
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}

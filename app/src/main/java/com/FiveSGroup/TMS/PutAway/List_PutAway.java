package com.FiveSGroup.TMS.PutAway;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.FiveSGroup.TMS.Warehouse.Wv_ShowResultQrode;

import java.util.ArrayList;
import java.util.List;

public class List_PutAway extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok, buttonBackList;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String put_away = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    TextView tvTitle;
    String lpn = "";
    int result;


    ArrayList<Product_PutAway> putAways;

    PutAwayAdapter putAwayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qrcode);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getValueFromIntent();

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

        init();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prepareData();
    }

    private void prepareData() {

        if (positonReceive == null) {
            if (lpn != null && value1 != null) {
                alert_show_SP(1);
            } else if (lpn == null && value1 != null) {
                alert_show_SP(0);

            }

        } else {
            if (lpn != null && value1 != null) {
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                alert_show_position(0);
            }
        }
        putAways = DatabaseHelper.getInstance().getAllProduct_PutAway();
        putAwayAdapter = new PutAwayAdapter(this, putAways);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(putAwayAdapter);
        putAwayAdapter.notifyDataSetChanged();
        put_away = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_PutAway.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_PutAway.this,List_PutAway.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();
                        //Remove swiped item from list and notify the RecyclerView
                        int position = viewHolder.getAdapterPosition();
                        Product_PutAway product = putAways.get(position);
                        putAways.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_PutAway_Specific(product.getAUTOINCREMENT());
                        putAwayAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listViewProduct);
    }

    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBackList = findViewById(R.id.buttonBackList);
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Put Away");
        btnscan_barcode.setOnClickListener(this);

//        buttonBackList.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(this);
        buttonBackList.setOnClickListener(this);

        btnok.setOnClickListener(this);
    }


    private void getValueFromIntent() {
        Intent intent = getIntent();
        // value1 : giá trị barcode truyền từ PutaWayQrCodeActivity
        value1 = intent.getStringExtra("btn1");
        // xác định vị trí là from hay to
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        // expDate - hiển thị HSD cho người dùng trong list sản phẩm
        expDate = intent.getStringExtra("exp_date");
        //  expdate1 xử lí position from - to
        expDate1 = intent.getStringExtra("expdate");
        put_away = intent.getStringExtra("put_away");
        // ea_unit : đơn vị trả về từ PutaWayQrCodeActivity
        ea_unit = intent.getStringExtra("ea_unit");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
    }


    private void ShowNoti() {
        try {
            LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    dialog.dismiss();

                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper.getInstance().deleteProduct_PutAway();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    DatabaseHelper.getInstance().deleteallExp_date();
                    dialog.dismiss();
                    Intent intent = new Intent(List_PutAway.this, MainWareHouseActivity.class);
                    startActivity(intent);
                    finish();
                    //  }

                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    private void SynchronizeToServer() {

        Dialog dialog = new Dialog(List_PutAway.this);

        if (put_away != null) {
            String saleCode = CmnFns.readDataAdmin();

            if (putAways.size() > 0) {
                if (isNotScanFromOrTo()) {

                    dialog.showDialog(List_PutAway.this, "Chưa có VT Từ hoặc VT Đến");

                } else if (isQuanityZero()) {
                    dialog.showDialog(List_PutAway.this, "Số lượng SP không được bằng 0");

                } else {
                    result = new CmnFns().synchronizeData(saleCode, "WPA", "");
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
                    } else {

                        if (result == -2) {
                            dialog.showDialog(List_PutAway.this, "Số lượng không đủ trong tồn kho");
                        } else if (result == -3) {
                            dialog.showDialog(List_PutAway.this, "Vị trí từ không hợp lệ");
                        } else if (result == -4) {
                            dialog.showDialog(List_PutAway.this, "Trạng thái của phiếu không hợp lệ");
                        } else if (result == -5) {
                            dialog.showDialog(List_PutAway.this, "Vị trí từ trùng vị trí đên");
                        } else if (result == -6) {
                            dialog.showDialog(List_PutAway.this, "Vị trí đến không hợp lệ");
                        } else if (result == -7) {
                            dialog.showDialog(List_PutAway.this, "Cập nhật trạng thái thất bại");
                        } else if (result == -8) {
                            dialog.showDialog(List_PutAway.this, "Sản phẩm không có thông tin trên phiếu ");
                        } else if (result == -13) {
                            dialog.showDialog(List_PutAway.this, "Dữ liệu không hợp lệ");

                        } else if (result == -24) {
                            dialog.showDialog(List_PutAway.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }else {
                            dialog.showDialog(List_PutAway.this, "Lưu thất bại");
                        }

                    }
                }
            } else {
                dialog.showDialog(List_PutAway.this, "Không có sản phẩm");

            }


        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
//                        Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                DatabaseHelper.getInstance().deleteProduct_PutAway();
                putAways.clear();
                putAwayAdapter.notifyDataSetChanged();

                Intent intentToHomeQRActivity = new Intent(List_PutAway.this, Wv_ShowResultQrode.class);
                intentToHomeQRActivity.putExtra("result_WPA", result);
                intentToHomeQRActivity.putExtra("type_WPA", "WPA");
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
        for (int i = 0; i < product.size(); i++) {
            Product_PutAway product_putAway = product.get(i);
            String valueQty = product_putAway.getQTY_SET_AVAILABLE();
            if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
                check = true;
            }
        }


        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();

        for (int i = 0; i < product.size(); i++) {
            Product_PutAway putAway = product.get(i);
            String value0 = "---";
            String valueAm1 = "-1";
            String valueFromCode = putAway.getPOSITION_FROM_CODE();
            String valueToCode = putAway.getPOSITION_TO_CODE();
            String positionCode = putAway.getPOSITION_FROM_CODE();
            String valueCode = putAway.getLPN_CODE();
            if (valueFromCode.equals("") || valueFromCode.equals("---")) {
                if ((positionCode.equals(value0) || (positionCode.equals(valueAm1)))) {
                    check = true;
                }
            } else {
                if ((valueFromCode.equals("")) || (valueToCode.equals("---"))) {
                    check = true;
                }
            }
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
                if (put_away != null) {
                    Intent intent = new Intent(List_PutAway.this, Qrcode_PutAway.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.buttonBack:
                ShowNoti();

                break;
            case R.id.buttonOK:
                actionSyn();
                break;
        }
    }

    private void actionSyn(){
        try {
            LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    dialog.dismiss();

                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SynchronizeToServer();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_PutAway> putAways = new ArrayList<>();
        putAways = DatabaseHelper.getInstance().getAllProduct_PutAway_Sync();
        for (int i = 0; i < putAways.size(); i++) {
            Product_PutAway putAway = putAways.get(i);
            if (productCd.equals(putAway.getPRODUCT_CD_PUTAWAY()) &&
                    expDate1.equals(putAway.getEXPIRED_DATE_PUTAWAY()) &&
                    stockinDate.equals(putAway.getSTOCKIN_DATE_PUTAWAY()) &&
                    ea_unit_position.equals(putAway.getEA_UNIT_PUTAWAY())) {

                if (!putAway.getLPN_FROM().equals("") || !putAway.getLPN_TO().equals("")) {
                    positionTo = putAway.getLPN_TO();
                    positionFrom = putAway.getLPN_FROM();
                }
                if (!putAway.getPOSITION_FROM_CODE().equals("") || !putAway.getPOSITION_TO_CODE().equals("")) {
                    positionTo = putAway.getPOSITION_TO_CODE();
                    positionFrom = putAway.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPA", isLPN);

            Dialog dialog = new Dialog(List_PutAway.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(List_PutAway.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(List_PutAway.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(List_PutAway.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(List_PutAway.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(List_PutAway.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(List_PutAway.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(List_PutAway.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(List_PutAway.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(List_PutAway.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(List_PutAway.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(List_PutAway.this, "Mã LPN không có trong tồn kho");

            } else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public void alert_show_SP(int isLPN) {
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZonePutaway(List_PutAway.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, isLPN);

            Dialog dialog = new Dialog(List_PutAway.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_PutAway.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_PutAway.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_PutAway.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_PutAway.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_PutAway.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_PutAway.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_PutAway.this, "Mã sản phẩm không có trong hệ thống");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_PutAway.this, "Mã sản phẩm không có trong zone reserve");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_PutAway.this, "Mã LPN không có trong zone reserve");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }
}

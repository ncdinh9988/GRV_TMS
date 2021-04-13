package com.FiveSGroup.TMS.MasterPick;

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
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.LoadPallet.Product_LoadPallet;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_Master_Pick extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    TextView nameproduct;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String master_picklist = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "";
    String unique_id = "" ;


    int statusGetCust;
    Product_Master_Pick product_qrcode;

    ArrayList<Product_Master_Pick> masterPicks;
    CheckEventbus eventbus;

    Master_Pick_Adapter MasterPickAdapter;
    TextView tvTitle;

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


    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        master_picklist = intent.getStringExtra("master_picklist");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        unique_id = intent.getStringExtra("unique_id");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData() {
        if (master_picklist != null) {
            if (positonReceive == null) {
                if (lpn != null && master_picklist != null) {
                    //TODO
                    alert_show_SP(1);
                } else if (lpn == null && master_picklist != null) {
                    //TODO
                    alert_show_SP(0);
                }

            } else {
                if (lpn != null) {
                    //TODO
                    alert_show_position(1);
                } else {
                    //TODO
                    alert_show_position(0);
                }

            }
        }
        masterPicks = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());
        MasterPickAdapter = new Master_Pick_Adapter(this, masterPicks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(MasterPickAdapter);
        MasterPickAdapter.notifyDataSetChanged();
        master_picklist = "";

//        nameproduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_Master_Pick.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_Master_Pick.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_Master_Pick.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        MasterPickAdapter.notifyDataSetChanged();

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_Master_Pick product = masterPicks.get(position);
                        masterPicks.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Master_Pick_Specific(product.getAUTOINCREMENT());
                        MasterPickAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listViewProduct);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());

        for (int i = 0; i < product.size(); i++) {
            Product_Master_Pick masterPick = product.get(i);
            String value0 = "---";
            String valueAm1 = "-1";
            String valueFromCode = masterPick.getPOSITION_FROM_CODE();
            String valueToCode = masterPick.getPOSITION_TO_CODE();
            String positionCode = masterPick.getPOSITION_FROM_CODE();
            String valueCode = masterPick.getLPN_CODE();
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


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        nameproduct = findViewById(R.id.nameproduct);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Master Pick");
    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_Master_Pick.this, Qrcode_Master_Pick.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());
        for (int i = 0; i < product.size(); i++) {
            Product_Master_Pick putAway = product.get(i);
            String valueQty = putAway.getQTY();
            if (valueQty.equals("0") || valueQty.equals("") || valueQty.equals("00") || valueQty.equals("000")) {
                check = true;
            }
        }


        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();
        Dialog dialog = new Dialog(List_Master_Pick.this);


        if (masterPicks.size() > 0) {
            if (isNotScanFromOrTo()) {
                dialog.showDialog(List_Master_Pick.this, "Chưa có VT Từ hoặc VT Đến");

            } else if (isQuanityZero()) {
                dialog.showDialog(List_Master_Pick.this, "Số lượng SP không được bằng 0");

            } else {
                try {
                    String postitionDes = new CmnFns().check_Duplicate(unique_id, CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WMP", isLPN);

                    int result = new CmnFns().synchronizeData(saleCode, "WMP", global.getMasterPickCd());
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                    } else {

                        if (result == -1) {
                            dialog.showDialog(List_Master_Pick.this, "Lưu thất bại");
                        } else if (result == -2) {
                            dialog.showDialog(List_Master_Pick.this, "Số lượng không đủ trong tồn kho");

                        } else if (result == -3) {
                            dialog.showDialog(List_Master_Pick.this, "Vị trí từ không hợp lệ");

                        } else if (result == -4) {
                            dialog.showDialog(List_Master_Pick.this, "Trạng thái của phiếu không hợp lệ");

                        } else if (result == -5) {
                            dialog.showDialog(List_Master_Pick.this, "Vị trí từ trùng vị trí đên");

                        } else if (result == -6) {
                            dialog.showDialog(List_Master_Pick.this, "Vị trí đến không hợp lệ");

                        } else if (result == -7) {
                            dialog.showDialog(List_Master_Pick.this, "Cập nhật trạng thái thất bại");

                        } else if (result == -8) {
                            dialog.showDialog(List_Master_Pick.this, "Sản phẩm không có thông tin trên phiếu ");

                        } else if (result == -13) {
                            dialog.showDialog(List_Master_Pick.this, "Dữ liệu không hợp lệ");

                        }else if (result == -24) {
                            dialog.showDialog(List_Master_Pick.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }

                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        } else {
            dialog.showDialog(List_Master_Pick.this, "Không có sản phẩm");

        }


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Master_Pick.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Master_Pick.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_Master_Pick();
                masterPicks.clear();
                MasterPickAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_Master_Pick.this, Home_Master_Pick.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                startScan();
                break;
            case R.id.buttonOK:
                synchronizeToService();
                break;
            case R.id.buttonBack:
                actionBack();
                break;
        }
    }


    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Master_Pick> product_master_picks = new ArrayList<>();
        product_master_picks = DatabaseHelper.getInstance().getAllProduct_Master_Pick_Sync(global.getMasterPickCd());
        for (int i = 0; i < product_master_picks.size(); i++) {
            Product_Master_Pick master_pick = product_master_picks.get(i);
            if (productCd.equals(master_pick.getPRODUCT_CD()) &&
                    expDate1.equals(master_pick.getEXPIRED_DATE()) &&
                    stockinDate.equals(master_pick.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(master_pick.getUNIT())) {

                if (!master_pick.getLPN_FROM().equals("") || !master_pick.getLPN_TO().equals("")) {
                    positionTo = master_pick.getLPN_TO();
                    positionFrom = master_pick.getLPN_FROM();
                }
                if (!master_pick.getPOSITION_FROM_CODE().equals("") || !master_pick.getPOSITION_TO_CODE().equals("")) {
                    positionTo = master_pick.getPOSITION_TO_CODE();
                    positionFrom = master_pick.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(unique_id, CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WMP", isLPN);

            Dialog dialog = new Dialog(List_Master_Pick.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(List_Master_Pick.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(List_Master_Pick.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(List_Master_Pick.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(List_Master_Pick.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(List_Master_Pick.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(List_Master_Pick.this, "Mã LPN không có trong tồn kho");

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
            int postitionDes = new CmnFns().synchronizeGETProductByZoneMasterPick(List_Master_Pick.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, global.getMasterPickCd(), isLPN);

            Dialog dialog = new Dialog(List_Master_Pick.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_Master_Pick.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_Master_Pick.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_Master_Pick.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_Master_Pick.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_Master_Pick.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_Master_Pick.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_Master_Pick.this, "Mã sản phẩm không có trong hệ thống");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_Master_Pick.this, "Mã sản phẩm không có trong zone reserve");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_Master_Pick.this, "Mã LPN không có trong zone reserve");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}

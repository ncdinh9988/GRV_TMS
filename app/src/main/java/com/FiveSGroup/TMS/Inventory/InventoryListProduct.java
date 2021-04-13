package com.FiveSGroup.TMS.Inventory;

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
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.RemoveFromLPN.Product_Remove_LPN;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class InventoryListProduct extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String inventory = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "";


    int statusGetCust;
    Product_Qrcode product_qrcode;
    TextView tvTitle;


    ArrayList<InventoryProduct> inventoryList;
    CheckEventbus eventbus;

    //PutAwayListAdapter putAwayListAdapter;

    InventoryAdapter inventoryAdapter;

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

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

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
        inventory = intent.getStringExtra("inventory");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");


        stockinDate = intent.getStringExtra("stockin_date");
    }

    private void prepareData() {
        if (positonReceive == null) {
            if (lpn != null && value1 != null) {
                //TODO
                alert_show_SP(1);
            } else if (lpn == null && value1 != null) {
                //TODO
                alert_show_SP(0);
            }

        } else {
            if (lpn != null && value1 != null) {
                //TODO
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                //TODO
                alert_show_position(0);
            }

        }

        inventoryList = DatabaseHelper.getInstance().getAllProduct_Inventory_Show(global.getInventoryCD());
        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        inventoryAdapter = new InventoryAdapter(inventoryList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(inventoryAdapter);
        inventoryAdapter.notifyDataSetChanged();
        inventory = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(InventoryListProduct.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        inventoryAdapter.notifyDataSetChanged();

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();
                        //Remove swiped item from list and notify the RecyclerView
                        int position = viewHolder.getAdapterPosition();
                        InventoryProduct product = inventoryList.get(position);
                        inventoryList.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Inventory_Specific(product.getAUTOINCREMENT());
                        inventoryAdapter.notifyItemRemoved(position);
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
        List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory_Show(global.getInventoryCD());

        for (int i = 0; i < product.size(); i++) {
            InventoryProduct inventory = product.get(i);
            String value0 = "---";
            String valueAm1 = "-1";
            String valueFromCode = inventory.getLPN_FROM();
            String positionCode = inventory.getPOSITION_FROM_CODE();
            String valueCode = inventory.getLPN_CODE();
            String valueToCode = inventory.getPOSITION_TO_CODE();
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
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("DANH SÁCH SP KIỂM TỒN");
    }

    private void actionBack() {
        try {
            finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory();
        for (int i = 0; i < product.size(); i++) {
            InventoryProduct inventory = product.get(i);
            String valueQty = inventory.getQTY();
            if (valueQty.equals("")) {
                check = true;
            }
        }

        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    private void synchronizeToServer() {
        int result;
        Dialog dialog = new Dialog(InventoryListProduct.this);
        if (inventory != null) {
            String saleCode = CmnFns.readDataAdmin();
            if (inventoryList.size() > 0) {
                if (isNotScanFromOrTo()) {
                    dialog.showDialog(InventoryListProduct.this, "Chưa có VT Từ");

                } else if (isQuanityZero()) {
                    dialog.showDialog(InventoryListProduct.this, "Số lượng SP không được để trống");

                } else {
                    //TODO
                    try {
                        result = new CmnFns().synchronizeData(saleCode, "WST", global.getInventoryCD());
                        switch (result) {
                            case 1:
                                ShowSuccessMessage("Lưu thành công");
                                // Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                ShowErrorMessage("Lưu thất bại");
                                break;
                            case -2:
                                ShowErrorMessage("Số lượng không đủ trong tồn kho");
                                break;
                            case -3:
                                ShowErrorMessage("Vị trí từ không hợp lệ");
                                break;
                            case -4:
                                ShowErrorMessage("Trạng thái phiếu không hợp lệ");
                                break;
                            case -5:
                                ShowErrorMessage("Vị trí từ trùng vị trí đến");
                                break;
                            case -6:
                                ShowErrorMessage("Vị trí đến không hợp lệ");
                                break;
                            case -7:
                                ShowErrorMessage("Cập nhật trạng thái của phiếu thất bại");
                                break;
                            case -8:
                                ShowErrorMessage("Sản phẩm không có thông tin trên phiếu");
                                break;
                            case -13:
                                ShowErrorMessage("Dữ liệu không hợp lệ");
                                break;
                            default:
                                if (result >= 1) {
                                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    if (inventory != null) {
                                        DatabaseHelper.getInstance().deleteProduct_Inventory_CD(global.getInventoryCD());
                                        inventoryList.clear();
                                        inventoryAdapter.notifyDataSetChanged();
                                        finish();
                                    }
                                } else {
                                    ShowErrorMessage("Lưu thất bại");
                                }
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(this,"Vui Lòng Thử Lại" ,Toast.LENGTH_SHORT).show();
                       finish();
                    }
                }
            } else {
                dialog.showDialog(InventoryListProduct.this, "Không có sản phẩm");

            }


        }

    }

    private void ShowErrorMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);


        textView.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                if (inventory != null) {
                    DatabaseHelper.getInstance().deleteProduct_Inventory_CD(global.getInventoryCD());
                    inventoryList.clear();
                    inventoryAdapter.notifyDataSetChanged();
                    startActivity(new Intent(InventoryListProduct.this, InventoryHome.class));
                    finish();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
                if (inventory != null) {
                    Intent intent = new Intent(InventoryListProduct.this, InventoryScanCode.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
                synchronizeToServer();
                break;
        }
    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<InventoryProduct> inventoryProducts = new ArrayList<>();
        inventoryProducts = DatabaseHelper.getInstance().getAllProduct_Inventory_Sync(global.getInventoryCD());
        for (int i = 0; i < inventoryProducts.size(); i++) {
            InventoryProduct inventoryProduct = inventoryProducts.get(i);
            if (productCd.equals(inventoryProduct.getPRODUCT_CD()) &&
                    expDate1.equals(inventoryProduct.getEXPIRED_DATE()) &&
                    stockinDate.equals(inventoryProduct.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(inventoryProduct.getUNIT())) {
                if (!inventoryProduct.getLPN_FROM().equals("") || !inventoryProduct.getLPN_TO().equals("")) {
                    positionTo = inventoryProduct.getLPN_TO();
                    positionFrom = inventoryProduct.getLPN_FROM();
                } else {
                    positionTo = inventoryProduct.getPOSITION_TO_CODE();
                    positionFrom = inventoryProduct.getPOSITION_FROM_CODE();

                }


            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WST", isLPN);

            Dialog dialog = new Dialog(InventoryListProduct.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(InventoryListProduct.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(InventoryListProduct.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(InventoryListProduct.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(InventoryListProduct.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(InventoryListProduct.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(InventoryListProduct.this, "Mã LPN không có trong tồn kho");

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
            int postitionDes = new CmnFns().synchronizeGETProductByZoneInventory(value1, CmnFns.readDataAdmin(), expDate, ea_unit, "WST", global.getInventoryCD(), stockinDate, isLPN);

            Dialog dialog = new Dialog(InventoryListProduct.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(InventoryListProduct.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(InventoryListProduct.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(InventoryListProduct.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(InventoryListProduct.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(InventoryListProduct.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(InventoryListProduct.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(InventoryListProduct.this, "Mã sản phẩm không có trong hệ thống");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}

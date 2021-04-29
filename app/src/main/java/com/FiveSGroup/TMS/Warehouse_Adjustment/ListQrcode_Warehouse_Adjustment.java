package com.FiveSGroup.TMS.Warehouse_Adjustment;

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
import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class ListQrcode_Warehouse_Adjustment extends AppCompatActivity implements View.OnClickListener {
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
    String warehouse_adjustment = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "";

    int statusGetCust;
    Product_Warehouse_Adjustment product_qrcode;

    ArrayList<Product_Warehouse_Adjustment> Warehouse_Adjustment;
    CheckEventbus eventbus;

    Warehouse_Adjustment_Adapter Warehouse_Adjustment_ListAdapter;
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
        warehouse_adjustment = intent.getStringExtra("warehouse_adjustment");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData() {
        if (positonReceive == null) {
            if (lpn != null && value1 != null) {
                //TODO
                alert_show_SP(1);
            } else if(lpn == null && value1 != null) {
                //TODO
                alert_show_SP(0);
            }

        } else {
            if (lpn != null && value1 != null) {
                //TODO
                alert_show_position(1);
            } else if(lpn == null && value1 != null){
                //TODO
                alert_show_position(0);
            }

        }

        Warehouse_Adjustment = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(global.getWarehouse_AdjustmentCD());
        Warehouse_Adjustment_ListAdapter = new Warehouse_Adjustment_Adapter(this, Warehouse_Adjustment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(Warehouse_Adjustment_ListAdapter);
        Warehouse_Adjustment_ListAdapter.notifyDataSetChanged();
        warehouse_adjustment = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(ListQrcode_Warehouse_Adjustment.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(ListQrcode_Warehouse_Adjustment.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(ListQrcode_Warehouse_Adjustment.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(ListQrcode_Warehouse_Adjustment.this,ListQrcode_Warehouse_Adjustment.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_Warehouse_Adjustment product = Warehouse_Adjustment.get(position);
                        Warehouse_Adjustment.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Warehouse_Adjustment_Specific(product.getAUTOINCREMENT());
                        Warehouse_Adjustment_ListAdapter.notifyItemRemoved(position);
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
        List<Product_Warehouse_Adjustment> product = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(global.getWarehouse_AdjustmentCD());

        for (int i = 0; i < product.size(); i++) {
            Product_Warehouse_Adjustment warehouse_adjustment = product.get(i);
            String value0 = "---";
            String valueFromCode = warehouse_adjustment.getPOSITION_FROM_CODE();
            String valueToCode = warehouse_adjustment.getPOSITION_TO_CODE();
            String lpn_from = warehouse_adjustment.getLPN_FROM();
            String lpn_to = warehouse_adjustment.getLPN_TO();

            if((valueFromCode.equals("") || valueFromCode.equals(value0)) && (lpn_from.equals(""))){
                check = true;
            }
            if((valueToCode.equals("") || valueToCode.equals(value0)) && (lpn_to.equals(""))){
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
    public void onBackPressed() {

    }


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Chỉnh Kho");
    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(ListQrcode_Warehouse_Adjustment.this, Qrcode_Warehouse_Adjustment.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_Warehouse_Adjustment> product = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(global.getWarehouse_AdjustmentCD());
        for (int i = 0; i < product.size(); i++) {
            Product_Warehouse_Adjustment putAway = product.get(i);
            String valueQty = putAway.getQTY();
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

    private void ShowErrorMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(ListQrcode_Warehouse_Adjustment.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(ListQrcode_Warehouse_Adjustment.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();

        Dialog dialog = new Dialog(ListQrcode_Warehouse_Adjustment.this);
        if (Warehouse_Adjustment.size() > 0) {
            if (isNotScanFromOrTo()) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Chưa có VT Từ");

            } else if (isQuanityZero()) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Số lượng SP không được bằng 0");

            } else {
                try {
                    int result = new CmnFns().synchronizeData(saleCode, "WWA", global.getWarehouse_AdjustmentCD());

                    switch (result) {
                        case 1:
                            ShowErrorMessage("Lưu thành công");
//                        Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                            DatabaseHelper.getInstance().deleteProduct_Warehouse_Adjustment();
                            Warehouse_Adjustment.clear();
                            Warehouse_Adjustment_ListAdapter.notifyDataSetChanged();

                            finish();
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
                                DatabaseHelper.getInstance().deleteProduct_Warehouse_Adjustment();
                                Warehouse_Adjustment.clear();
                                Warehouse_Adjustment_ListAdapter.notifyDataSetChanged();

                                finish();
                            } else {
                                ShowErrorMessage("Lưu thất bại");
                            }
                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        } else {
            dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Không có sản phẩm");

        }


    }

    private void actionBack() {
        try {
            ListQrcode_Warehouse_Adjustment.this.finish();
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
        ArrayList<Product_Warehouse_Adjustment> product_warehouse_adjustments = new ArrayList<>();
        product_warehouse_adjustments = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(global.getWarehouse_AdjustmentCD());
        for(int i = 0; i < product_warehouse_adjustments.size(); i++){
            Product_Warehouse_Adjustment product_warehouse_adjustment = product_warehouse_adjustments.get(i);
            if(productCd.equals(product_warehouse_adjustment.getPRODUCT_CD()) &&
                    expDate1.equals(product_warehouse_adjustment.getEXPIRED_DATE()) &&
                    stockinDate.equals(product_warehouse_adjustment.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(product_warehouse_adjustment.getUNIT())){

                if(!product_warehouse_adjustment.getLPN_FROM().equals("") || !product_warehouse_adjustment.getLPN_TO().equals("") ){
                    positionTo = product_warehouse_adjustment.getPOSITION_TO_CODE();
                    positionFrom = product_warehouse_adjustment.getPOSITION_FROM_CODE();
                }
                // không được để else if - vì quét VTT k phải là mã LPN
                if(!product_warehouse_adjustment.getPOSITION_FROM_CODE().equals("") || !product_warehouse_adjustment.getPOSITION_TO_CODE().equals("")) {
                    positionTo = product_warehouse_adjustment.getPOSITION_TO_CODE();
                    positionFrom = product_warehouse_adjustment.getPOSITION_FROM_CODE();
                }

                // if này là để trả lại giá trị from và to nếu người dùng muốn quét lại VTT và VTĐ
                if(positonReceive.equals("1")){
                    if(!positionTo.equals("") && !positionFrom.equals("")){
                        positionFrom = "";
                    }
                }else {
                    if(!positionTo.equals("") && !positionFrom.equals("")){
                        positionTo = "";
                    }
                }

            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo,"WWA", isLPN);


            Dialog dialog = new Dialog(ListQrcode_Warehouse_Adjustment.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí từ không hợp lệ");

            }else if (postitionDes.equals("-6")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí từ không có trong hệ thống");

            }else if (postitionDes.equals("-10")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã LPN không có trong hệ thống");

            }else if (postitionDes.equals("-17")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "LPN từ trùng LPN đến");

            }else if (postitionDes.equals("-18")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí đến không có trong hệ thống");

            }else if (postitionDes.equals("-12")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã LPN không có trong tồn kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vị trí từ chưa có sản phẩm");

            }else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }




    }

    public void alert_show_SP(int isLPN) {
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZoneWarehouse_Adjustment(ListQrcode_Warehouse_Adjustment.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, global.getWarehouse_AdjustmentCD(), isLPN);

            Dialog dialog = new Dialog(ListQrcode_Warehouse_Adjustment.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã LPN không có trong hệ thống");

            }else if (postitionDes == -11) {

                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã LPN không có trong kho");

            }else if (postitionDes == -16) {

                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(ListQrcode_Warehouse_Adjustment.this, "Mã sản phẩm không có trong hệ thống");

            }

        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }

}

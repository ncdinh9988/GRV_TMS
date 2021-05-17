package com.FiveSGroup.TMS.ReturnWareHouse;

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
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.PickList.PickList;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_Return_WareHouse extends AppCompatActivity implements View.OnClickListener {
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
    String return_warehouse = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "";

    int statusGetCust;
    Product_Return_WareHouse product_return_wareHouse;

    ArrayList<Product_Return_WareHouse> return_wareHouses;
    CheckEventbus eventbus;

    Return_Warehouse_Adapter return_warehouse_adapter;
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
        return_warehouse = intent.getStringExtra("return_warehouse");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData(){
        if (positonReceive == null) {
            if (lpn != null && return_warehouse != null) {
                //TODO
                alert_show_SP(1);
            } else if (lpn == null && return_warehouse != null){
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

        return_wareHouses = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse(global.getReturnCD());
        return_warehouse_adapter = new Return_Warehouse_Adapter(this, return_wareHouses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(return_warehouse_adapter);
        return_warehouse_adapter.notifyDataSetChanged();
        return_warehouse = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_Return_WareHouse.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_Return_WareHouse.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_Return_WareHouse.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_Return_WareHouse.this,List_Return_WareHouse.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_Return_WareHouse product = return_wareHouses.get(position);
                        return_wareHouses.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Return_Specific(product.getAUTOINCREMENT());
                        return_warehouse_adapter.notifyItemRemoved(position);
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
        List<Product_Return_WareHouse> product = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse(global.getReturnCD());

        for (int i = 0; i < product.size(); i++) {
            Product_Return_WareHouse return_wareHouse = product.get(i);
            String value0 = "---";
            String valueFromCode = return_wareHouse.getPOSITION_FROM_CODE();
            String valueToCode = return_wareHouse.getPOSITION_TO_CODE();
            String lpn_from = return_wareHouse.getLPN_FROM();
            String lpn_to = return_wareHouse.getLPN_TO();

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
        tvTitle.setText("Danh Sách SP Trả Hàng");
    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_Return_WareHouse.this, Qrcode_Return_WareHouse.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();
    }
    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_Return_WareHouse> product = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse(global.getReturnCD());
        for (int i = 0; i < product.size(); i++) {
            Product_Return_WareHouse return_wareHouse = product.get(i);
            String valueQty = return_wareHouse.getQTY();
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
    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();
        Dialog dialog = new Dialog(List_Return_WareHouse.this);


        if(return_wareHouses.size() > 0){
            if (isNotScanFromOrTo()) {
                dialog.showDialog(List_Return_WareHouse.this,"Chưa có VT Từ hoặc VT Đến");

            } else if(isQuanityZero()){
                dialog.showDialog(List_Return_WareHouse.this,"Số lượng SP không được bằng 0");

            }else{
                try {
                    int result = new CmnFns().synchronizeData(saleCode, "WRW", global.getReturnCD());
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
                    } else {

                        if (result == -1) {
                            dialog.showDialog(List_Return_WareHouse.this,"Lưu thất bại");
                        }else if(result == -2){
                            dialog.showDialog(List_Return_WareHouse.this,"Số lượng không đủ trong tồn kho");

                        }else if(result == -3){
                            dialog.showDialog(List_Return_WareHouse.this,"Vị trí từ không hợp lệ");

                        }else if(result == -4){
                            dialog.showDialog(List_Return_WareHouse.this,"Trạng thái của phiếu không hợp lệ");

                        }else if(result == -5){
                            dialog.showDialog(List_Return_WareHouse.this,"Vị trí từ trùng vị trí đên");

                        }else if(result == -6){
                            dialog.showDialog(List_Return_WareHouse.this,"Vị trí đến không hợp lệ");

                        }else if(result == -7){
                            dialog.showDialog(List_Return_WareHouse.this,"Cập nhật trạng thái thất bại");

                        }else if(result == -8){
                            dialog.showDialog(List_Return_WareHouse.this,"Sản phẩm không có thông tin trên phiếu ");

                        } else if (result == -13) {
                            dialog.showDialog(List_Return_WareHouse.this,"Dữ liệu không hợp lệ");

                        }else if (result == -24) {
                            dialog.showDialog(List_Return_WareHouse.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }else if (result == -26) {
                            dialog.showDialog(List_Return_WareHouse.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }

                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }else{
            dialog.showDialog(List_Return_WareHouse.this,"Không có sản phẩm");
        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Return_WareHouse.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Return_WareHouse.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_Return_WareHouse();
                return_wareHouses.clear();
                return_warehouse_adapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_Return_WareHouse.this, Home_Return_WareHouse.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_Return_WareHouse.this.finish();
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


    public void alert_show_position(int isLPN){
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Return_WareHouse> returns = new ArrayList<>();
        returns = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse_Sync(global.getReturnCD());
        for(int i = 0; i < returns.size(); i++){
            Product_Return_WareHouse return_wareHouse = returns.get(i);
            if(productCd.equals(return_wareHouse.getPRODUCT_CD()) &&
                    expDate1.equals(return_wareHouse.getEXPIRED_DATE()) &&
                    stockinDate.equals(return_wareHouse.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(return_wareHouse.getUNIT())){
                positionTo = return_wareHouse.getPOSITION_TO_CODE();
                positionFrom = return_wareHouse.getPOSITION_FROM_CODE();
            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo("",CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo,"WRW",isLPN);

            Dialog dialog = new Dialog(List_Return_WareHouse.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí từ không có trong hệ thống");

            }else if (postitionDes.equals("-10")) {
                dialog.showDialog(List_Return_WareHouse.this, "Mã LPN không có trong hệ thống");

            }else if (postitionDes.equals("-17")) {
                dialog.showDialog(List_Return_WareHouse.this, "LPN từ trùng LPN đến");

            }else if (postitionDes.equals("-18")) {
                dialog.showDialog(List_Return_WareHouse.this, "LPN đến trùng LPN từ");

            }else if (postitionDes.equals("-19")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí đến không có trong hệ thống");

            }else if (postitionDes.equals("-12")) {
                dialog.showDialog(List_Return_WareHouse.this, "Mã LPN không có trong tồn kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(List_Return_WareHouse.this, "Vị trí từ chưa có sản phẩm");

            }else if (postitionDes.equals("-28")) {
                dialog.showDialog(List_Return_WareHouse.this, "LPN đến có vị trí không hợp lệ");

            }else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public void alert_show_SP(int isLPN){
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZoneReturnWareHouse(List_Return_WareHouse.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate,global.getReturnCD(),isLPN);

            Dialog dialog = new Dialog(List_Return_WareHouse.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_Return_WareHouse.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_Return_WareHouse.this, "Mã sản phẩm không có trên phiếu");


            }else if (postitionDes == -10) {
                dialog.showDialog(List_Return_WareHouse.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_Return_WareHouse.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_Return_WareHouse.this, "Mã LPN không có trong kho");

            }else if (postitionDes == -16) {

                dialog.showDialog(List_Return_WareHouse.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_Return_WareHouse.this, "Mã sản phẩm không có trong hệ thống");

            }else if (postitionDes == -21) {

                dialog.showDialog(List_Return_WareHouse.this, "Mã sản phẩm không có trong trả hàng");

            }else if (postitionDes == -22) {

                dialog.showDialog(List_Return_WareHouse.this, "Mã LPN không có trong trả hàng");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}

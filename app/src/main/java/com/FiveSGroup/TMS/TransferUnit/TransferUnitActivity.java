package com.FiveSGroup.TMS.TransferUnit;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.LetDown.LetDownQrCodeActivity;
import com.FiveSGroup.TMS.LetDown.LetDownSuggestionsActivity;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.Wv_ShowResultQrode;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class TransferUnitActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok, buttonBackList;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    Spinner idUnit;
    String value1 = "" , id_unique_TU = "";
    String positonReceive = "";
    String productCd = "";
    String batch_number = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String key = "";
    String transfer_unit = "" ,transferunit = "";
    String ea_unit = "";
    String pro_code = "";
    String pro_name = "";
    String ea_unit_position = "";
    String stockinDate = "" , id_unique_LD = "";
    TextView tvTitle , VTDen;
    String fromLetDownSuggestionsActivity = "";
    String lpn = "";
    int result;


    ArrayList<TransferUnitProduct> transferUnitarr;

    TransferUnitAdapter TransferUnitAdapter;

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
//        getunit();
    }
//    private void getunit(){
//        Spinner spinner = (Spinner) findViewById(R.id.idUnit);
//        List<String> spinnerList = new ArrayList<>();
//        ArrayList<String> units = new ArrayList<>();
//        units = new CmnFns().getEa_Unit("8935001215516", "2");
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                    String item = parent.getItemAtPosition(position).toString();
//
//                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }

    private void prepareData() {

        if (positonReceive == null) {
            if (key == null || key.equals("")) {
                if (lpn != null && value1 != null) {
                    alert_show_SP(1);
                } else if (lpn == null && value1 != null) {
                    alert_show_SP(0);

                }
            } else {
                Dialog dialog = new Dialog(TransferUnitActivity.this);
                dialog.showDialog(TransferUnitActivity.this, "Mã Sản Phẩm Không Có Trong Kho");
            }


        } else {
            if (lpn != null && value1 != null) {
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                alert_show_position(0);
            }
        }
        try {
            if(!id_unique_TU.equals("")){
                DatabaseHelper.getInstance().updatePositionToTransferUnit(id_unique_TU,transferunit);
            }
        }catch (Exception e){

        }

        transferUnitarr = DatabaseHelper.getInstance().getAllTransferUnitProduct();
        TransferUnitAdapter = new TransferUnitAdapter(this, transferUnitarr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(TransferUnitAdapter);
        TransferUnitAdapter.notifyDataSetChanged();
        transfer_unit = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(TransferUnitActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(TransferUnitActivity.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(TransferUnitActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(TransferUnitActivity.this,TransferUnitActivity.class);
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
                        TransferUnitProduct product = transferUnitarr.get(position);
                        transferUnitarr.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_TRANSFER_UNIT_Specific(product.getAUTOINCREMENT());
                        TransferUnitAdapter.notifyItemRemoved(position);
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
        tvTitle.setText("Chuyển Đơn Vị Tính");
        VTDen = findViewById(R.id.VTDen);
        VTDen.setText("Quy Đổi");
        btnscan_barcode.setOnClickListener(this);

        buttonBackList.setVisibility(View.GONE);
        buttonBack.setOnClickListener(this);
        buttonBackList.setOnClickListener(this);

        btnok.setOnClickListener(this);
    }


    private void getValueFromIntent() {
        Intent intent = getIntent();
        // value1 : giá trị barcode truyền từ LetDownQrCodeActivity
        value1 = intent.getStringExtra("btn1");
        global.setBarcode(value1);
        id_unique_TU = intent.getStringExtra("id_unique_TU");
        // xác định vị trí là from hay to
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        // expDate - hiển thị HSD cho người dùng trong list sản phẩm
        expDate = intent.getStringExtra("exp_date");
        id_unique_LD = intent.getStringExtra("id_unique_LD");
        transferunit = intent.getStringExtra("transferunit");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        //  expdate1 xử lí position from - to
        expDate1 = intent.getStringExtra("expdate");
        transfer_unit = intent.getStringExtra("transfer_unit");
        // ea_unit : đơn vị trả về từ LetDownQRCodeActivity
        ea_unit = intent.getStringExtra("ea_unit");
        lpn = intent.getStringExtra("lpn");
        batch_number = intent.getStringExtra("batch_number");
        try {
            if (batch_number.equals("---")){
                batch_number = "";
            }
            if(batch_number==null){
                batch_number = "";
            }
        }catch (Exception e){

        }

        stockinDate = intent.getStringExtra("stockin_date");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        fromLetDownSuggestionsActivity = intent.getStringExtra("fromLetDownSuggestionsActivity");
        key = intent.getStringExtra("key");
    }


    private void ShowNoti() {
        try {
            LayoutInflater factory = LayoutInflater.from(TransferUnitActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(TransferUnitActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    DatabaseHelper.getInstance().deleteProduct_TRANSFER_UNIT();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    DatabaseHelper.getInstance().deleteallExp_date();
                    dialog.dismiss();
//                    if (fromLetDownSuggestionsActivity!=null){
                    Intent intent = new Intent(TransferUnitActivity.this, MainWareHouseActivity.class);
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

        Dialog dialog = new Dialog(TransferUnitActivity.this);

        if (transfer_unit != null) {
            String saleCode = CmnFns.readDataAdmin();

            if (transferUnitarr.size() > 0) {
                if (checkunit()) {
                    dialog.showDialog(TransferUnitActivity.this, "Đơn Vị Quy Đổi không Được Trùng Với Đơn Vị Sản Phẩm Đã Chọn");

                }else{
                    result = new CmnFns().synchronizeConvert_UOM();

//                            result = new CmnFns().synchronizeData(saleCode, "WLD", "");
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
                    } else {

                        if (result == -2) {
                            dialog.showDialog(TransferUnitActivity.this, "Số lượng không đủ trong tồn kho");
                        } else if (result == -3) {
                            dialog.showDialog(TransferUnitActivity.this, "Vị trí từ không hợp lệ");
                        } else if (result == -4) {
                            dialog.showDialog(TransferUnitActivity.this, "Trạng thái của phiếu không hợp lệ");
                        } else if (result == -5) {
                            dialog.showDialog(TransferUnitActivity.this, "Vị trí từ trùng vị trí đên");
                        } else if (result == -6) {
                            dialog.showDialog(TransferUnitActivity.this, "Vị trí đến không hợp lệ");
                        } else if (result == -7) {
                            dialog.showDialog(TransferUnitActivity.this, "Cập nhật trạng thái thất bại");
                        } else if (result == -8) {
                            dialog.showDialog(TransferUnitActivity.this, "Sản phẩm không có thông tin trên phiếu ");
                        } else if (result == -13) {
                            dialog.showDialog(TransferUnitActivity.this, "Dữ liệu không hợp lệ");

                        } else if (result == -24) {
                            dialog.showDialog(TransferUnitActivity.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }else if (result == -26) {
                            dialog.showDialog(TransferUnitActivity.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                        }else if (result == -32) {
                            dialog.showDialog(TransferUnitActivity.this, "Số Lượng Quy Đổi Không Đủ");

                        }else {
                            dialog.showDialog(TransferUnitActivity.this, "Lưu thất bại");
                        }


                    }
                }


            } else {
                dialog.showDialog(TransferUnitActivity.this, "Không có sản phẩm");

            }


        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(TransferUnitActivity.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(TransferUnitActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_TRANSFER_UNIT();
                transferUnitarr.clear();
                TransferUnitAdapter.notifyDataSetChanged();

//                Intent intentToHomeQRActivity = new Intent(TransferUnitActivity.this, T.class);
//                intentToHomeQRActivity.putExtra("result_WLD", result);
//                intentToHomeQRActivity.putExtra("type_WLD", "WLD");
//                startActivity(intentToHomeQRActivity);
//                finish();
            }
        });
        dialog.show();
    }

    private boolean checkunit(){
        boolean check = false;
        List<TransferUnitProduct> product = DatabaseHelper.getInstance().getAllTransferUnitProduct();
        for (int i = 0; i < product.size(); i++) {
            TransferUnitProduct TransferUnitProduct = product.get(i);
            String valueQty = TransferUnitProduct.getQTY();
            String changeUnit = TransferUnitProduct.getUNIT_CHANGE_TO();
            if(valueQty.equals(changeUnit)){
                check = true;
            }
        }


        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<TransferUnitProduct> product = DatabaseHelper.getInstance().getAllTransferUnitProduct();
        for (int i = 0; i < product.size(); i++) {
            TransferUnitProduct TransferUnitProduct = product.get(i);
            String valueQty = TransferUnitProduct.getQTY();
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
        List<TransferUnitProduct> product = DatabaseHelper.getInstance().getAllTransferUnitProduct();

        for (int i = 0; i < product.size(); i++) {
            TransferUnitProduct letDown = product.get(i);
            String value0 = "---";
            String valueFromCode = letDown.getPOSITION_FROM_CODE();
            String valueToCode = letDown.getPOSITION_TO_CODE();
            String lpn_from = letDown.getLPN_FROM();
            String lpn_to = letDown.getLPN_TO();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
                if (transfer_unit != null) {
                    Intent intent = new Intent(TransferUnitActivity.this, TransferUnitQrcode.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.buttonBack:
                ShowNoti();

                break;
            case R.id.buttonBackList:
                Intent intent = new Intent(this, TransferUnitActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonOK:
                actionSyn();
                break;
        }
    }

    private void actionSyn(){
        try {
            LayoutInflater factory = LayoutInflater.from(TransferUnitActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(TransferUnitActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
        ArrayList<TransferUnitProduct> transfer = new ArrayList<>();
        transfer = DatabaseHelper.getInstance().getAllProduct_TRANSFER_UNIT_Sync();
        for (int i = 0; i < transfer.size(); i++) {
            TransferUnitProduct transferunit = transfer.get(i);
            if (productCd.equals(transferunit.getPRODUCT_CD()) &&
                    expDate1.equals(transferunit.getEXPIRED_DATE()) &&
                    stockinDate.equals(transferunit.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(transferunit.getUNIT())) {

                if (!transferunit.getLPN_FROM().equals("") || !transferunit.getLPN_TO().equals("")) {
                    positionTo = transferunit.getLPN_TO();
                    positionFrom = transferunit.getLPN_FROM();
                }
                if (!transferunit.getPOSITION_FROM_CODE().equals("") || !transferunit.getPOSITION_TO_CODE().equals("")) {
                    positionTo = transferunit.getPOSITION_TO_CODE();
                    positionFrom = transferunit.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfooUnit(id_unique_LD,CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WOI", isLPN);

            Dialog dialog = new Dialog(TransferUnitActivity.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(TransferUnitActivity.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(TransferUnitActivity.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(TransferUnitActivity.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(TransferUnitActivity.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(TransferUnitActivity.this, "Mã LPN không có trong tồn kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(TransferUnitActivity.this, "Vị trí từ chưa có sản phẩm");

            }else if (postitionDes.equals("-28")) {
                dialog.showDialog(TransferUnitActivity.this, "LPN đến có vị trí không hợp lệ");

            }  else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public void alert_show_SP(int isLPN) {
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZoneTransferUnit(TransferUnitActivity.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, isLPN,pro_code , pro_name ,batch_number);

            Dialog dialog = new Dialog(TransferUnitActivity.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(TransferUnitActivity.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(TransferUnitActivity.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(TransferUnitActivity.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(TransferUnitActivity.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(TransferUnitActivity.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(TransferUnitActivity.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(TransferUnitActivity.this, "Mã sản phẩm không có trong hệ thống");

            } else if (postitionDes == -21) {

                dialog.showDialog(TransferUnitActivity.this, "Mã sản phẩm không có trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(TransferUnitActivity.this, "Mã LPN không có trong zone");

            }
            else if (postitionDes == -100) {

                dialog.showDialog(TransferUnitActivity.this, "Không Có Sản Phẩm Với ĐVT Này");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }


}

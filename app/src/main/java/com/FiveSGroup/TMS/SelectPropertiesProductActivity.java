package com.FiveSGroup.TMS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.List_Return_WareHouse;
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;
import com.FiveSGroup.TMS.Warehouse.ListQrcode;
import com.FiveSGroup.TMS.Warehouse_Adjustment.ListQrcode_Warehouse_Adjustment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectPropertiesProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView tvProductSelectName;
    private EditText edtSelectProductExpiredDate, edtSelectProductStockinDate , edtSelectShelfLife , edtSelectShelfLifeDate;
    private Spinner spinnerProductUnit;
    private DatePickerDialog pickerDialog;
    private ArrayList<String> units;
    private ArrayAdapter<String> adapter;
    private Button btnBack, btnConfirm;
    private Date date ,date2;
    private Integer shelfLife1 , shelfLife2 ;
    private String barcode = "",
            returnposition = "",
            returnCD = "",
            returnStock = "",
            selectedUnit = "",
            stockinDate = "",
            shelfLife = "",
            shelfLifeDate = "",
            typeScan = "" ,
            exp_date = "";
    String total_shelf_life = "";
    String shelf_life_type = "";
    String min_rem_shelf_life = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_properties_product);

        getDataFromWareHouseScanCode();
        init();
        getProductName();
    }

    private void getProductName() {
        switch (typeScan) {
            case "scan_from_stock_in":
                setProductName(typeScan);
                break;
            case "scan_from_inventory":
                setProductName(typeScan);
                break;
            case "scan_from_letdown":
                setProductName(typeScan);
                break;
            case "scan_from_load_pallet":
                setProductName(typeScan);
                break;
            case "scan_from_master_picklist":
                setProductName(typeScan);
                break;
            case "scan_from_pick_list":
                setProductName(typeScan);
                break;
            case "scan_from_put_away":
                setProductName(typeScan);
                break;
            case "scan_from_remove_lpn":
                setProductName(typeScan);
                break;
            case "scan_from_return_warehouse":
                setProductName(typeScan);
                break;
            case "scan_from_stock_out":
                setProductName(typeScan);
                break;
            case "scan_from_stock_transfer":
                setProductName(typeScan);
                break;
            case "scan_from_warehouse_adjustment":
                setProductName(typeScan);
                break;
        }

    }

    private void setProductName(String typeScan) {
        String data = new CmnFns().getProductName(typeScan, barcode, returnCD);
        if (isNumeric(data)) {
            tvProductSelectName.setText("SẢN PHẨM KHÔNG HỢP LỆ, VUI LÒNG THỰC HIỆN QUÉT LẠI SẢN PHẨM");
        } else {
            tvProductSelectName.setText(data);
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void getDataFromWareHouseScanCode() {
        Intent intent = getIntent();
        typeScan = intent.getStringExtra("typeScan");
        barcode = intent.getStringExtra("btn1");
        returnposition = intent.getStringExtra("returnposition");
        returnCD = global.getStockReceiptCd();
        returnStock = intent.getStringExtra("returnStock");
        total_shelf_life = intent.getStringExtra("total_shelf_life");
        shelf_life_type = intent.getStringExtra("shelf_life_type");
        min_rem_shelf_life= intent.getStringExtra("min_rem_shelf_life");

    }

    private void init() {
        tvProductSelectName = findViewById(R.id.tvSelectProductName);
        edtSelectProductExpiredDate = findViewById(R.id.edtSelectProductExpiredDate);
        edtSelectProductStockinDate = findViewById(R.id.edtSelectProductStockinDate);
        edtSelectShelfLifeDate = findViewById(R.id.edtSelectShelfLifeDate);
        edtSelectShelfLife = findViewById(R.id.edtSelectShelfLife);
        spinnerProductUnit = findViewById(R.id.spinnerProductUnit);
        edtSelectProductExpiredDate.setOnClickListener(this);
        edtSelectProductStockinDate.setOnClickListener(this);
        edtSelectProductStockinDate.setInputType(InputType.TYPE_NULL);
        edtSelectProductExpiredDate.setInputType(InputType.TYPE_NULL);
        //set unit in adapter
        units = new ArrayList<>();
        units = new CmnFns().getEa_Unit(barcode, "2");
        if (units == null) {
            units = new ArrayList<>();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerProductUnit.setAdapter(adapter);
        spinnerProductUnit.setOnItemSelectedListener(this);
        //set text in adapter
        if(shelf_life_type.equals("MONTH"))
        {
            edtSelectShelfLife.setText(total_shelf_life);
        }else {
            edtSelectShelfLifeDate.setText(total_shelf_life);
        }

        btnBack = findViewById(R.id.btnBackSelectProductProperties);
        btnConfirm = findViewById(R.id.btnConfirmProductProperties);
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtSelectProductExpiredDate:
                selectDate(R.id.edtSelectProductExpiredDate);
                break;
            case R.id.edtSelectProductStockinDate:
                selectDate(R.id.edtSelectProductStockinDate);
                break;
            case R.id.btnBackSelectProductProperties:
                finish();
                break;
            case R.id.btnConfirmProductProperties:
                savePropertiesToProduct();
                break;

        }
    }


    private void savePropertiesToProduct() {
        switch (typeScan) {
            case "scan_from_stock_in":
                createProduct(ListQrcode.class, "stock_in");
                break;
            case "scan_from_inventory":
                createProduct(InventoryListProduct.class, "inventory");
                break;
            case "scan_from_letdown":
                createProduct(LetDownActivity.class, "let_down");
                break;
            case "scan_from_load_pallet":
                createProduct(LoadPalletActivity.class, "load_pallet");
                break;
            case "scan_from_master_picklist":
                createProduct(List_Master_Pick.class, "master_picklist");
                break;
            case "scan_from_pick_list":
                createProduct(ListPickList.class, "pick_list");
                break;
            case "scan_from_put_away":
                createProduct(ListQrcode.class, "put_away");
                break;
            case "scan_from_remove_lpn":
                createProduct(List_Remove_LPN.class, "remove_lpn");
                break;
            case "scan_from_return_warehouse":
                createProduct(List_Return_WareHouse.class, "return_warehouse");
                break;
            case "scan_from_stock_out":
                createProduct(ListQrcode_Stockout.class, "stock_out");
                break;
            case "scan_from_stock_transfer":
                createProduct(ListStockTransfer.class, "stock_transfer");
                break;
            case "scan_from_warehouse_adjustment":
                createProduct(ListQrcode_Warehouse_Adjustment.class, "warehouse_adjustment");
                break;
        }

    }

    private void createProduct(Class activity, String type) {
//        if (edtSelectProductExpiredDate.getText().toString().trim().isEmpty() ||
//                edtSelectProductStockinDate.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Vui lòng chọn hạn sử dụng và ngày nhập", Toast.LENGTH_SHORT).show();
//        } else {
        if(spinnerProductUnit.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đơn vị ", Toast.LENGTH_SHORT).show();
        }else{

            String expiredDate = edtSelectProductExpiredDate.getText().toString().trim();
            stockinDate = edtSelectProductStockinDate.getText().toString().trim();
            shelfLife = edtSelectShelfLife.getText().toString().trim();
            shelfLifeDate = edtSelectShelfLifeDate.getText().toString().trim();
            String unit = spinnerProductUnit.getSelectedItem().toString();
            try {
                date =new SimpleDateFormat("dd/MM/yyyy").parse(stockinDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!expiredDate.equals("")){
                exp_date = expiredDate;
            }else{
                //Nếu có stockindate
                if(!stockinDate.equals("")){
                    //Nếu có ngày ShelfLife
                    if(!shelfLifeDate.equals("")){
                        //Nếu có tháng ShelfLife
                        if(!shelfLife.equals("")){
                            //Bước 1 : Lấy String của exp_date sau khi đã tính dc dựa trên ngày shelflife
                            shelfLife2 = Integer.valueOf(shelfLifeDate);
                            SelectPropertiesProductActivity obj = new SelectPropertiesProductActivity();
                            Calendar calendar = obj.dateToCalendar(date);
                            calendar.add(Calendar.DATE, shelfLife2);
                            Date newDate = obj.calendarToDate(calendar);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            exp_date = dateFormat.format(newDate);
                            //Bước 2 chuyển String exp_date sang date
                            try {
                                date2 =new SimpleDateFormat("dd/MM/yyyy").parse(exp_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            shelfLife1 = Integer.valueOf(shelfLife);
                            Calendar calendar2 = obj.dateToCalendar(date2);
                            calendar2.add(Calendar.MONTH, shelfLife1);
                            Date newDate2 = obj.calendarToDate(calendar2);
                            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            exp_date = dateFormat2.format(newDate2);
                            //Ngược lại không có tháng Shelflife
                        }else {
                            shelfLife2 = Integer.valueOf(shelfLifeDate);
                            SelectPropertiesProductActivity obj = new SelectPropertiesProductActivity();
                            Calendar calendar = obj.dateToCalendar(date);
                            calendar.add(Calendar.DATE, shelfLife2);
                            Date newDate = obj.calendarToDate(calendar);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            exp_date = dateFormat.format(newDate);
                        }
                      //Ngược lại không có ngày ShelfLife
                    }else{
                        //Nếu có tháng ShelfLife
                        if (!shelfLife.equals("")){

                            shelfLife1 = Integer.valueOf(shelfLife);
                            SelectPropertiesProductActivity obj = new SelectPropertiesProductActivity();
                            Calendar calendar = obj.dateToCalendar(date);
                            calendar.add(Calendar.MONTH, shelfLife1);
                            Date newDate = obj.calendarToDate(calendar);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            exp_date = dateFormat.format(newDate);
                        }
                    }
                }

            }

            Intent intent = new Intent(SelectPropertiesProductActivity.this, activity);
            intent.putExtra("btn1", barcode);
            intent.putExtra("returnposition", returnposition);
            intent.putExtra("returnCD", returnCD);
            intent.putExtra("returnStock", returnStock);
            intent.putExtra("exp_date", exp_date);
            intent.putExtra("stockin_date", stockinDate);
            intent.putExtra("ea_unit", unit);
            intent.putExtra(type, type);
            startActivity(intent);
            finish();
        }
//        }
    }

    //Convert Calendar to Date
    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }
    //Convert Date to Calendar
    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }
    private void selectDate(final int viewId) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        pickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthFormat = "";
                String dayFormat = "";
                if (monthOfYear + 1 > 9) {
                    monthFormat = String.valueOf(monthOfYear + 1);
                } else {
                    monthFormat = "0" + (monthOfYear + 1);
                }
                if (dayOfMonth > 9) {
                    dayFormat = String.valueOf(dayOfMonth);
                } else {
                    dayFormat = "0" + dayOfMonth;
                }

                if (viewId == R.id.edtSelectProductExpiredDate) {
                    edtSelectProductExpiredDate.setText(dayFormat + "/" + monthFormat + "/" + year);
                } else {
                    edtSelectProductStockinDate.setText(dayFormat + "/" + monthFormat + "/" + year);
                }

            }
        }, year, month, day);
        pickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
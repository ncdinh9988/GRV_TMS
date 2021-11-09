package com.FiveSGroup.TMS.Warehouse;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.List_Return_WareHouse;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;
import com.FiveSGroup.TMS.Warehouse_Adjustment.ListQrcode_Warehouse_Adjustment;
import com.FiveSGroup.TMS.global;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Layout_PO_Fresh extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView tvProductSelectName;
    private EditText edtSelectProductExpiredDate, edtSelectProductStockinDate, edtSelectShelfLife, edtSelectShelfLifeDate , edtcont;
    private Spinner spinnerProductUnit;
    private DatePickerDialog pickerDialog;
    private ArrayList<String> units;
    private ArrayAdapter<String> adapter;
    private Button btnBack, btnConfirm;
    private Date date, date2, date_exp, date_current, newDate_curent;
    private Integer shelfLife1, shelfLife2;
    private CheckBox checkBoxDate, checkBoxMonth;
    private String barcode = "",
            returnposition = "",
            returnCD = "",
            pro_code = "",
            pro_cd = "",
            returnStock = "",
            selectedUnit = "",
            currentDateandTime = "",
            stockinDate = "",
            cont = "",
            expiredDate = "",
            expired_Date = "",
            shelfLife = "",
            shelfLifeDate = "",
            typeScan = "",
            batch = "",
            stockin = "",
            fromCd = "",
            pro_name = "",
            unit = "",
            exp_date = "";



    String total_shelf_life = "";
    String shelf_life_type = "";
    String min_rem_shelf_life = "";
    int min_to_date =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_properties_product);
        DatabaseHelper.getInstance().deleteallBatch_Number();

        getDataFromWareHouseScanCode();
        init();
        getProductName();
        setCheckBox();
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
            tvProductSelectName.setText(pro_name);
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
        unit = intent.getStringExtra("unit");
        barcode = intent.getStringExtra("btn1");
        returnposition = intent.getStringExtra("returnposition");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        pro_cd = intent.getStringExtra("pro_cd");
        expired_Date = intent.getStringExtra("expired_Date");
        fromCd = intent.getStringExtra("fromCd");
        if(fromCd==null){
            fromCd = "";
        }
        if(pro_name==null){
            pro_name = "";
        }
        batch = intent.getStringExtra("batch");
        if (batch==null){
            batch= "";
        }
        returnCD = global.getStockReceiptCd();
        returnStock = intent.getStringExtra("returnStock");
        stockin = intent.getStringExtra("stockin");
        if(pro_code==null){
            pro_code= "";
        }

        total_shelf_life = intent.getStringExtra("total_shelf_life");
        shelf_life_type = intent.getStringExtra("shelf_life_type");
        min_rem_shelf_life = intent.getStringExtra("min_rem_shelf_life");

        if(min_rem_shelf_life.equals("")){
            min_rem_shelf_life = "0";
        }

    }

    private void init() {
        checkBoxDate = findViewById(R.id.checkBoxDate);
        checkBoxMonth = findViewById(R.id.checkBoxMonth);
        tvProductSelectName = findViewById(R.id.tvSelectProductName);
        edtSelectProductExpiredDate = findViewById(R.id.edtSelectProductExpiredDate);
        edtSelectProductExpiredDate.setText(expired_Date);
        edtSelectProductStockinDate = findViewById(R.id.edtSelectProductStockinDate);
        edtSelectShelfLife = findViewById(R.id.edtSelectShelfLife);
        spinnerProductUnit = findViewById(R.id.spinnerProductUnit);
        edtcont = findViewById(R.id.edtcont);
        if (batch==null || batch==""){
            edtcont.setText(batch);
            //set unit in adapter
            units = new ArrayList<>();
            units = new CmnFns().getEa_Unit(barcode, "2",pro_code);
            if (units == null) {
                units = new ArrayList<>();
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinnerProductUnit.setAdapter(adapter);
            spinnerProductUnit.setOnItemSelectedListener(this);
        }else{

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Integer.parseInt(unit));
            spinnerProductUnit.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(unit);
            spinnerProductUnit.setSelection(spinnerPosition);
            edtcont.setFocusable(false);
            edtcont.setText(batch);
        }


        edtSelectProductExpiredDate.setOnClickListener(this);
        edtSelectProductStockinDate.setOnClickListener(this);
        edtSelectProductStockinDate.setInputType(InputType.TYPE_NULL);
        edtSelectProductExpiredDate.setInputType(InputType.TYPE_NULL);
        edtSelectProductExpiredDate.setVisibility(View.GONE);
        edtSelectProductStockinDate.setVisibility(View.GONE);
        edtSelectShelfLife.setVisibility(View.GONE);
        checkBoxMonth.setVisibility(View.GONE);
        checkBoxDate.setVisibility(View.GONE);

        btnBack = findViewById(R.id.btnBackSelectProductProperties);
        btnConfirm = findViewById(R.id.btnConfirmProductProperties);
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void setCheckBox() {
        checkBoxDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxMonth.setChecked(false);
                }

            }
        });
        checkBoxMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxDate.setChecked(false);
                }
            }
        });
//set text in adapter
        if (shelf_life_type.equals("MONTH")) {
            if (!total_shelf_life.equals("0")) {
                edtSelectShelfLife.setText(total_shelf_life);
                checkBoxMonth.setChecked(true);
                checkBoxDate.setChecked(false);
//                checkBoxDate.setEnabled(false);
//                checkBoxMonth.setEnabled(false);
            }

        } else if (shelf_life_type.equals("DAY")) {
            if (!total_shelf_life.equals("0")) {
                edtSelectShelfLife.setText(total_shelf_life);
                checkBoxDate.setChecked(true);
                checkBoxMonth.setChecked(false);
//                checkBoxDate.setEnabled(false);
//                checkBoxMonth.setEnabled(false);
            }
        }
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

    private void createProduct(final Class activity, final String type) {
        expiredDate = edtSelectProductExpiredDate.getText().toString().trim();
        stockinDate = edtSelectProductStockinDate.getText().toString().trim();
        cont = edtcont.getText().toString();
        shelfLife = edtSelectShelfLife.getText().toString().trim();
//            shelfLifeDate = edtSelectShelfLifeDate.getText().toString().trim();
        final String unit = spinnerProductUnit.getSelectedItem().toString();
//      dành cho kho khô
//        if (spinnerProductUnit.getSelectedItem().toString().isEmpty()) {
//            Toast.makeText(this, "Vui lòng chọn đơn vị ", Toast.LENGTH_SHORT).show();
//        }
//        else if((expiredDate.equals(""))&&(stockinDate.equals(""))){
//            Toast.makeText(this, "Vui lòng chọn HSD hoặc (NSX và shelflife)", Toast.LENGTH_SHORT).show();
//        }
//        else if((expiredDate.equals(""))&&(!stockinDate.equals(""))&&(shelfLife.equals(""))) {
//            Toast.makeText(this, "Vui lòng chọn HSD hoặc (NSX và shelflife)", Toast.LENGTH_SHORT).show();
//        }
        if (batch==null || batch==""){
            if(edtcont.getText().toString().isEmpty()){
                Toast.makeText(this, "Vui Lòng Nhập Batch/Cont ", Toast.LENGTH_SHORT).show();
            }
        }

//        else {
        try {
            min_to_date = Integer.parseInt(min_rem_shelf_life);
            if (!stockinDate.equals("")) {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(stockinDate);
            }
            if (!expiredDate.equals("")) {
                date_exp = new SimpleDateFormat("dd/MM/yyyy").parse(expiredDate);
            }
            //Lấy ngày hiện tại
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            currentDateandTime = sdf.format(Calendar.getInstance().getTime());
            date_current = new SimpleDateFormat("dd/MM/yyyy").parse(currentDateandTime);

            if ((!expiredDate.equals("")) && (!stockinDate.equals(""))) {
                if (date.compareTo(date_exp) > 0) {
                    Toast.makeText(this, "Ngày Sản Xuất Lớn Hơn Hạn Sử Dụng ", Toast.LENGTH_LONG).show();
                    return;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Nếu có HSD
        if (!expiredDate.equals("")) {
            exp_date = expiredDate;
            //Nếu có chọn checkbox date
//                if (!stockinDate.equals("")) {
//
//                }else{
            IntentActivity(activity, type, unit);
//                }
//                if (checkBoxDate.isChecked()) {
//                    SelectPropertiesProductActivity obj = new SelectPropertiesProductActivity();
//                    Calendar calendar = obj.dateToCalendar(date_exp);
//                    calendar.add(Calendar.DATE, (-min_to_date));
//                    newDate_curent = obj.calendarToDate(calendar);
//                    //Lấy ngày nằm trong khoảng cảnh báo bằng hạn sử dụng trừ đi min_to_date so sánh với ngày hiện tại
//                    if (date_current.compareTo(newDate_curent) > 0) {
//                        LayoutInflater factory = LayoutInflater.from(SelectPropertiesProductActivity.this);
//                        View layout_cus = factory.inflate(R.layout.layout_warn, null);
//                        final AlertDialog dialog = new AlertDialog.Builder(SelectPropertiesProductActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                        InsetDrawable inset = new InsetDrawable(back, 64);
//                        dialog.getWindow().setBackgroundDrawable(inset);
//                        dialog.setView(layout_cus);
//
//                        Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                        Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                        btnNo.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
//                                dialog.dismiss();
//                            }
//                        });
//                        btnYes.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //Remove swiped item from list and notify the RecyclerView
//                                dialog.dismiss();
//                                IntentActivity(activity, type, unit);
//                            }
//                        });
//                        dialog.show();
//                    }
//                    else {
//                        IntentActivity(activity, type, unit);
//                    }
//                }
//                //Nếu có chọn checkbox month
//                else if (checkBoxMonth.isChecked()) {
//                    SelectPropertiesProductActivity obj = new SelectPropertiesProductActivity();
//                    Calendar calendar = obj.dateToCalendar(date_exp);
//                    calendar.add(Calendar.MONTH, (-min_to_date));
//                    newDate_curent = obj.calendarToDate(calendar);
//                    //Lấy ngày nằm trong khoảng cảnh báo bằng hạn sử dụng trừ đi min_to_date so sánh với ngày hiện tại
//                    if (date_current.compareTo(newDate_curent) > 0) {
//                        LayoutInflater factory = LayoutInflater.from(SelectPropertiesProductActivity.this);
//                        View layout_cus = factory.inflate(R.layout.layout_warn, null);
//                        final AlertDialog dialog = new AlertDialog.Builder(SelectPropertiesProductActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                        InsetDrawable inset = new InsetDrawable(back, 64);
//                        dialog.getWindow().setBackgroundDrawable(inset);
//                        dialog.setView(layout_cus);
//
//                        Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                        Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                        btnNo.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
//                                dialog.dismiss();
//                            }
//                        });
//                        btnYes.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //Remove swiped item from list and notify the RecyclerView
//                                dialog.dismiss();
//                                IntentActivity(activity, type, unit);
//                            }
//                        });
//                        dialog.show();
//                    }
//                    else {
//                        IntentActivity(activity, type, unit);
//                    }
//                }
//                else{
//                    IntentActivity(activity, type, unit);
//                }

        }
        // Ngược lại không có HSD
        else {
            //Nếu có stockindate
            if (!stockinDate.equals("")) {
                // Nếu có chọn checkbox date
                if (checkBoxDate.isChecked()) {
                    //Nếu có ngày ShelfLife
                    if(!shelfLife.equals("")){
                        shelfLife2 = Integer.valueOf(shelfLife);
                        Layout_PO_Fresh obj = new Layout_PO_Fresh();
                        Calendar calendar = obj.dateToCalendar(date);
                        calendar.add(Calendar.DATE, shelfLife2);
                        Date newDate = obj.calendarToDate(calendar);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        exp_date = dateFormat.format(newDate);
                        try {
                            date_exp = new SimpleDateFormat("dd/MM/yyyy").parse(exp_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Layout_PO_Fresh objvs = new Layout_PO_Fresh();
                        Calendar calendar1 = objvs.dateToCalendar(date_exp);
                        calendar1.add(Calendar.DATE, (-min_to_date));
                        newDate_curent = objvs.calendarToDate(calendar1);

                        if (date_current.compareTo(newDate_curent) > 0) {
                            LayoutInflater factory = LayoutInflater.from(Layout_PO_Fresh.this);
                            View layout_cus = factory.inflate(R.layout.layout_warn, null);
                            final AlertDialog dialog = new AlertDialog.Builder(Layout_PO_Fresh.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                            InsetDrawable inset = new InsetDrawable(back, 64);
                            dialog.getWindow().setBackgroundDrawable(inset);
                            dialog.setView(layout_cus);

                            Button btnNo = layout_cus.findViewById(R.id.btnNo);
                            Button btnYes = layout_cus.findViewById(R.id.btnYes);
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
                                    dialog.dismiss();
                                }
                            });
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Remove swiped item from list and notify the RecyclerView
                                    dialog.dismiss();
                                    IntentActivity(activity, type, unit);

                                }
                            });
                            dialog.show();
                        } else {
                            IntentActivity(activity, type, unit);
                        }
                    }
                    // Ngược lại không có ngày shelfLife
                    else{
                        IntentActivity(activity, type, unit);
                    }
                }
                // Ngược lại chọn checkbox month
                else {
                    if (!shelfLife.equals("")) {
                        shelfLife1 = Integer.valueOf(shelfLife);
                        Layout_PO_Fresh obj = new Layout_PO_Fresh();
                        Calendar calendar = obj.dateToCalendar(date);
                        calendar.add(Calendar.MONTH, shelfLife1);
                        Date newDate = obj.calendarToDate(calendar);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        exp_date = dateFormat.format(newDate);

                        try {
                            date_exp = new SimpleDateFormat("dd/MM/yyyy").parse(exp_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Layout_PO_Fresh objvs = new Layout_PO_Fresh();
                        Calendar calendar1 = objvs.dateToCalendar(date_exp);
                        calendar1.add(Calendar.DATE, (-min_to_date));
                        newDate_curent = objvs.calendarToDate(calendar1);
                        if (date_current.compareTo(newDate_curent) > 0) {

                            LayoutInflater factory = LayoutInflater.from(Layout_PO_Fresh.this);
                            View layout_cus = factory.inflate(R.layout.layout_warn, null);
                            final AlertDialog dialog = new AlertDialog.Builder(Layout_PO_Fresh.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                            InsetDrawable inset = new InsetDrawable(back, 64);
                            dialog.getWindow().setBackgroundDrawable(inset);
                            dialog.setView(layout_cus);

                            Button btnNo = layout_cus.findViewById(R.id.btnNo);
                            Button btnYes = layout_cus.findViewById(R.id.btnYes);
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
                                    dialog.dismiss();
                                }
                            });
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Remove swiped item from list and notify the RecyclerView
                                    dialog.dismiss();
                                    IntentActivity(activity, type, unit);
                                }
                            });
                            dialog.show();
                        } else {
                            IntentActivity(activity, type, unit);
                        }
                    }
                    else{
                        IntentActivity(activity, type, unit);
                    }

                }
            }
            //Ngược lại không có stockindate
            else {
                IntentActivity(activity, type, unit);
            }
        }
//        }
    }

    private void IntentActivity(final Class activity, final String type, final String unit) {
        Intent intent = new Intent(Layout_PO_Fresh.this, activity);
        intent.putExtra("btn1", barcode);
        intent.putExtra("returnposition", returnposition);
        intent.putExtra("returnCD", returnCD);
        intent.putExtra("returnStock", returnStock);
        intent.putExtra("batch_number", cont);
        intent.putExtra("pro_code",pro_code);
        intent.putExtra("pro_name",pro_name);
        intent.putExtra("pro_cd",pro_cd);
        intent.putExtra("exp_date", exp_date);
        intent.putExtra("stockin_date", stockinDate);
        intent.putExtra("cont", cont);
        intent.putExtra("fromCd", fromCd);
        intent.putExtra("ea_unit", unit);
        intent.putExtra(type, type);
        startActivity(intent);
        finish();
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

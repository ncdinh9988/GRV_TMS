package com.FiveSGroup.TMS.Warehouse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitActivity;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
import com.FiveSGroup.TMS.global;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.ArrayList;

public class Qrcode extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView surfaceView;
    private CodeScanner mCodeScanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    Button btnTruyCap;
    String value1 = "";
    String value2 = "";
    boolean check = false;
    Intent intent;
    String position = "";
    String pro_code = "";
    String pro_name = "";
    String product_cd = "" , allow_dry = "" , allow_fresh = "";
    String stock = "";
    String stock_in = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "" , id_unique_SI = "";
    TextView textViewTitle;
    String pro_cd = "";
    String lot_ind = "";
    //bi???n ????? test hi???n th??? dialog ????n v??? t??nh
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;;
    String checkToFinish = "";
    String exp_date;
    String batch_number_t;
    String stockin_date;
    String ea_unit;
    String total_shelf_life = "";
    String shelf_life_type = "";
    String min_rem_shelf_life = "";
    String setting = "";



    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreff = this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        setting = sharedPreff.getString("checked", "");
        try {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
                setContentView(R.layout.layout_qrcode);
                init();
                if (setting.equals("HoneyWell")) {

                } else {
                    if (ContextCompat.checkSelfPermission(Qrcode.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(Qrcode.this, new String[]{Manifest.permission.CAMERA}, 123);
                    } else {
                        startScanning();
                    }
                }

            }else {
                setContentView(R.layout.activity_load_camera);
                init();
                initialiseDetectorsAndSources();

            }
        } catch (Exception e) {

        }
        if (setting.equals("HoneyWell")) {
            edtBarcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    GetData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        edtBarcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    GetData(edtBarcode.getText().toString());
                    return true;
                }
                return false;
            }
        });

        getDataFromIntent();
        check = true;

        buttonBack.setOnClickListener(this);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    CmnFns.hideSoftKeyboard(Qrcode.this);
                }catch (Exception e){

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void startScanning() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetData(result.getText());
                        Toast.makeText(Qrcode.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void init() {
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        btnSend = findViewById(R.id.btnSend);
        edtBarcode = findViewById(R.id.edtBarcode);
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        textViewTitle = findViewById(R.id.tvTitle);
        textViewTitle.setText("QU??T M?? - NH???P KHO");
        buttonBack = findViewById(R.id.buttonQRBack);
    }

    private void getDataFromIntent() {
        intent = getIntent();
        checkToFinish = intent.getStringExtra("check_to_finish_at_list");
        ea_unit_position = intent.getStringExtra("ea_unit_position");
        // position ???????c truy???n qua ????? ?????nh danh n?? ???????c b???m from hay to
        position = intent.getStringExtra("position");
        // ???????c tuy???n qua t??? adapter
        product_cd = intent.getStringExtra("product_cd");
        // ???????c truy???n qua t??? adapter
        stock = intent.getStringExtra("stock");
        id_unique_SI = intent.getStringExtra("id_unique_SI");


        exp_date = intent.getStringExtra("exp_date");
        stockin_date = intent.getStringExtra("stockin_date");
        ea_unit = intent.getStringExtra("ea_unit");

        // expiredDate truy???n t??? adapter ????? x??? l?? from - to
        expiredDate = intent.getStringExtra("c");
        if (!(position == null)) {
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setChecked(false);
            if (position.equals("1")) {
                textViewTitle.setText("QU??T V??? TR?? T???");
            } else if (position.equals("2")) {
                textViewTitle.setText("QU??T V??? TR?? ?????N");
            }
        } else {
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setChecked(false);
            textViewTitle.setText("QU??T M?? - NH???P KHO");
        }
    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .setRequestedFps(30.0f)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Qrcode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Qrcode.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    if (check == true) {
                        check = false;
                        Log.d("double", String.valueOf(barcodes.size()));
                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    barcodeData = barcodes.valueAt(0).displayValue;
                                    Toast.makeText(Qrcode.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                    if (barcodeData != null) {
                                        barcodeData = barcodeData.replace("\n","");
                                        edtBarcode.setText(barcodeData);
                                        GetData(barcodeData);
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(Qrcode.this, "T??nh N??ng N??y ??ang B??? Kh??a", Toast.LENGTH_LONG).show();
                                    allow_dry = DatabaseHelper.getInstance().getParamByKey("STOCK_IN_DRY").getValue();
                                    allow_fresh = DatabaseHelper.getInstance().getParamByKey("STOCK_IN_FRESH").getValue();
                                    Intent intent = null;
                                    if(allow_dry.equals("1")){
                                        intent = new Intent(getApplication(), Layout_PO.class);
                                    }else if (allow_fresh.equals("1")){
                                        intent = new Intent(getApplication(), Layout_PO_Fresh.class);
                                    }

                                    intent.putExtra("barcode", barcodeData);
                                    startActivity(intent);
                                    finish();
                                }


                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        });
                    }


                }
            }
        });
    }
    private void GetData(final String barcodeData){
        if (barcodeData != null) {
            if (position != null) {
                Intent intentt = new Intent(getApplication(), ListQrcode.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("stock_in", "333");
                intentt.putExtra("id_unique_SI", id_unique_SI);


                // truy???n qua cho ListQRCode ????? x??? l?? from - to
                intentt.putExtra("exp_date", exp_date);
                intentt.putExtra("stockin_date", stockin_date);
                intentt.putExtra("ea_unit", ea_unit);

                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nh???n t??? 2 n??t", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();
            } else {
                DatabaseHelper.getInstance().deleteallProduct_S_P();
                int statusGetcode = new CmnFns().getProduct_code(barcodeData);
                final ArrayList<Product_S_P> product_s_ps = DatabaseHelper.getInstance().getallValueSP();
                if (product_s_ps.size() > 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Qrcode.this);
                    builder.setTitle("M?? S???n Ph???m - T??n S???n Ph???m");

                    final ArrayList<String> product_code = new ArrayList<>();
                    for (int i = 0; i < product_s_ps.size(); i++) {
                        product_code.add(product_s_ps.get(i).getPRODUCT_CODE() + " - " + product_s_ps.get(i).getPRODUCT_NAME());
                    }
                    // chuy???n ?????i exp_date th??nh m???ng chu???i String
                    String[] mStringArray = new String[product_code.size()];
                    mStringArray = product_code.toArray(mStringArray);

                    final String[] mString = mStringArray;
                    builder.setItems(mString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String product_name = mString[which];
                            String[] chuoi = product_name.split(" - ");
                            //int vitri = which;
                            pro_cd = product_s_ps.get(which).getPRODUCT_CD();
                            lot_ind = product_s_ps.get(which).getLOT_IND();

                            dialog.dismiss(); // Close Dialog
                            if (product_name != "") {
                                pro_code = chuoi[0];
                                pro_name = chuoi[1];
                                getinformation(barcodeData);
                            }

                            // Do some thing....

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                }else if(product_s_ps.size() == 1){
                    pro_name = product_s_ps.get(0).getPRODUCT_NAME();
                    pro_code = product_s_ps.get(0).getPRODUCT_CODE();
                    pro_cd = product_s_ps.get(0).getPRODUCT_CD();
                    lot_ind = product_s_ps.get(0).getLOT_IND();
                    getinformation(barcodeData);
                }else{
                    Toast.makeText(Qrcode.this, "Vui L??ng Th??? L???i", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Qrcode.this, ListQrcode.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void getinformation(final String barcodeData) {
        int statusGetBatch = new CmnFns().getBatch(global.getAdminCode(),barcodeData , global.getStockReceiptCd(),pro_code);
        if(statusGetBatch == 1){
            // l???y t???t c??? batch trong database ra
            final ArrayList<Batch_number_Tam> batch_number_tams = DatabaseHelper.getInstance().getallBatch();


            if (batch_number_tams.size() > 1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Qrcode.this);
                builder.setTitle("Ch???n Batch Number - ????n V??? T??nh");

                final ArrayList<String> batch_number = new ArrayList<>();
                for (int i = 0; i < batch_number_tams.size(); i++) {
                    batch_number.add(batch_number_tams.get(i).getBATCH_NUMBER() + " - " + batch_number_tams.get(i).getUNIT());

                }

                // chuy???n ?????i exp_date th??nh m???ng chu???i String
                String[] mStringArray = new String[batch_number.size()];
                mStringArray = batch_number.toArray(mStringArray);

                final String[] mString = mStringArray;
                builder.setItems(mString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bat = mString[which];
                        String vitri = String.valueOf(which);
                        String expired_Date = batch_number_tams.get(which).getEXPIRED_DATE();
                        String unit = batch_number_tams.get(which).getUNIT();

                        dialog.dismiss(); // Close Dialog

                        if (bat != "") {
                            try{
                                batch_number_t = bat; //TEST
                                String[] chuoi = batch_number_t.split(" - ");
                                SharedPreferences prefs = getBaseContext().getSharedPreferences("vitriPO", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("vitri",vitri );
                                edit.commit();
                                allow_dry = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_DRY").getValue();
                                allow_fresh = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_FRESH").getValue();
                                Intent intentt = null;
                                if(allow_dry.equals("1")){
                                    intentt = new Intent(getApplication(), Layout_PO.class);
                                }else if (allow_fresh.equals("1")){
                                    intentt = new Intent(getApplication(), Layout_PO_Fresh.class);
                                }

                                intentt.putExtra("typeScan", "scan_from_stock_in");
                                intentt.putExtra("btn1", barcodeData);
                                intentt.putExtra("unit", unit);
                                intentt.putExtra("stockin", "444");
                                intentt.putExtra("stock_in", "333");
                                intentt.putExtra("id_unique_SI", id_unique_SI);
                                intentt.putExtra("total_shelf_life", total_shelf_life);
                                intentt.putExtra("shelf_life_type", shelf_life_type);
                                intentt.putExtra("min_rem_shelf_life", min_rem_shelf_life);
                                intentt.putExtra("returnposition", position);
                                intentt.putExtra("returnCD", product_cd);
                                intentt.putExtra("returnStock", stock);
                                intentt.putExtra("expired_Date", expired_Date);
                                intentt.putExtra("pro_code", pro_code);
                                intentt.putExtra("pro_name", pro_name);
                                intentt.putExtra("pro_cd", pro_cd);
                                intentt.putExtra("lot_ind", lot_ind);
                                intentt.putExtra("batch", chuoi[0]);
                                intentt.putExtra("vitri", vitri);

                                startActivity(intentt);
                                finish();
                                // For example: Call method of MainActivity.
                                Toast.makeText(Qrcode.this, "You select: " + batch_number_t,
                                        Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                Toast.makeText(Qrcode.this, "T??nh N??ng N??y ??ang B??? Kh??a", Toast.LENGTH_LONG).show();
                            }

                        }
                        // Do some thing....

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            } else if (batch_number_tams.size() == 1) {
                String batchTam = "" ,expired_Date = "" , unit = "";
                try {
                    batchTam = batch_number_tams.get(0).getBATCH_NUMBER();
                    expired_Date = batch_number_tams.get(0).getEXPIRED_DATE();
                    unit = batch_number_tams.get(0).getUNIT();
                    SharedPreferences prefs = getBaseContext().getSharedPreferences("vitriPO", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("vitri","0");
                    edit.commit();
                    allow_dry = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_DRY").getValue();
                    allow_fresh = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_FRESH").getValue();
                    Intent intentt = null;
                    if(allow_dry.equals("1")){
                        intentt = new Intent(getApplication(), Layout_PO.class);
                    }else if (allow_fresh.equals("1")){
                        intentt = new Intent(getApplication(), Layout_PO_Fresh.class);
                    }

                    intentt.putExtra("typeScan", "scan_from_stock_in");
                    intentt.putExtra("btn1", barcodeData);
                    intentt.putExtra("unit", unit);
                    intentt.putExtra("stockin", "444");
                    intentt.putExtra("stock_in", "333");
                    intentt.putExtra("id_unique_SI", id_unique_SI);
                    intentt.putExtra("total_shelf_life", total_shelf_life);
                    intentt.putExtra("shelf_life_type", shelf_life_type);
                    intentt.putExtra("min_rem_shelf_life", min_rem_shelf_life);
                    intentt.putExtra("returnposition", position);
                    intentt.putExtra("expired_Date", expired_Date);
                    intentt.putExtra("returnCD", product_cd);
                    intentt.putExtra("returnStock", stock);
                    intentt.putExtra("batch", batchTam);
                    intentt.putExtra("pro_code", pro_code);
                    intentt.putExtra("pro_name", pro_name);
                    intentt.putExtra("pro_cd", pro_cd);
                    intentt.putExtra("lot_ind", lot_ind);
                    startActivity(intentt);

                    finish();
                } catch (Exception e) {
                    Toast.makeText(Qrcode.this, "T??nh N??ng N??y ??ang B??? Kh??a", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Qrcode.this, "Vui L??ng Th??? L???i", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Qrcode.this, ListQrcode.class);
                intent.putExtra("btn1", barcodeData);
                startActivity(intent);
                finish();
            }
        }
        else{
            int statusGetCustt = new CmnFns().getExpDateFromServer(global.getAdminCode(),barcodeData, global.getStockReceiptCd());
            if (statusGetCustt != 1) {
                ReturnPosition(barcodeData);
            } else {
                if (expiredDate != null) {

                    ReturnPosition(barcodeData);

                } else {
                    try {
                        // l???y t???t c??? h???n `s??? d???ng trong database ra
                        final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallValueStockin();
                        for (int i = 0; i < expired_date.size(); i++) {
                            total_shelf_life = expired_date.get(0).getTOTAL_SHELF_LIFE();
                            shelf_life_type = expired_date.get(0).getSHELF_LIFE_TYPE();
                            min_rem_shelf_life = expired_date.get(0).getMIN_REM_SHELF_LIFE();
                        }
                        allow_dry = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_DRY").getValue();
                        allow_fresh = DatabaseHelper.getInstance().getParamByKey_Layout("STOCK_IN_FRESH").getValue();

                        if(allow_dry.equals("1")){
                            intent = new Intent(getApplication(), Layout_PO.class);
                        }else if (allow_fresh.equals("1")){
                            intent = new Intent(getApplication(), Layout_PO_Fresh.class);
                        }

                        intent.putExtra("typeScan", "scan_from_stock_in");
                        intent.putExtra("btn1", barcodeData);
                        intent.putExtra("stockin", "444");
                        intent.putExtra("stock_in", "333");
                        intent.putExtra("id_unique_SI", id_unique_SI);
                        intent.putExtra("total_shelf_life", total_shelf_life);
                        intent.putExtra("shelf_life_type", shelf_life_type);
                        intent.putExtra("min_rem_shelf_life", min_rem_shelf_life);
                        intent.putExtra("returnposition", position);
                        intent.putExtra("returnCD", product_cd);
                        intent.putExtra("pro_code", pro_code);
                        intent.putExtra("pro_name", pro_name);
                        intent.putExtra("pro_cd", pro_cd);
                        intent.putExtra("lot_ind", lot_ind);
                        intent.putExtra("returnStock", stock);
                        DatabaseHelper.getInstance().deleteallExp_date();
                        DatabaseHelper.getInstance().deleteallEa_Unit();
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(Qrcode.this, "T??nh N??ng N??y ??ang B??? Kh??a", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }


    }

    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), ListQrcode.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("stock_in", "333");
        intentt.putExtra("id_unique_SI", id_unique_SI);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);
        intentt.putExtra("pro_cd", pro_cd);
        // truy???n qua cho ListQRCode ????? x??? l?? from - to
//        intentt.putExtra("expdate", expiredDate);
//        intentt.putExtra("stockin_date", stockinDate);
        intentt.putExtra("exp_date", exp_date);
        intentt.putExtra("stockin_date", stockin_date);
        intentt.putExtra("ea_unit", exp_date);

        Log.e("barcodeData1", "" + barcodeData);
        Log.e("nh???n t??? 2 n??t", "" + position);
        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp, String stockinDateShow) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1",pro_code);
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), ListQrcode.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("exp_date", expDatetemp);
        intentt.putExtra("id_unique_SI", id_unique_SI);
        if (stockinDate == null) {
            intentt.putExtra("stockin_date", stockinDateShow);

        } else {
            intentt.putExtra("stockin_date", stockinDate);
        }
        if (ea_unit_tams.size() > 0) {
            intentt.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());
        } else {
            intentt.putExtra("ea_unit", " ");

        }

        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();
    }


    private void ShowDialogUnit(final String barcode, final String expDateTemp2, final String stockinDateShow) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "2",pro_code);

        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();
        String Ea_Unit_temp = "";
        try {
            Ea_Unit_temp = ea_unit_tams.get(0).getEA_UNIT_TAM();
        } catch (Exception e) {

        }

        final ArrayList<String> ea_unit = new ArrayList<>();
        for (int i = 0; i < ea_unit_tams.size(); i++) {
            ea_unit.add(ea_unit_tams.get(i).getEA_UNIT_TAM());
        }

        // chuy???n ?????i exp_date th??nh m???ng chu???i String
        String[] mStringArray = new String[ea_unit.size()];
        mStringArray = ea_unit.toArray(mStringArray);
        final String[] mString = mStringArray;

        AlertDialog.Builder builderDVT = new AlertDialog.Builder(Qrcode.this);
        builderDVT.setTitle("CH???N ????N V??? T??NH");
        builderDVT.setCancelable(false);
        builderDVT.setItems(mString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Qrcode.this, mString[which], Toast.LENGTH_LONG).show();
                Intent intentt = new Intent(getApplication(), ListQrcode.class);
                intentt.putExtra("btn1", barcode);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("id_unique_SI", id_unique_SI);
                // truy???n qua cho ListQRcode ????? add v??o text HSD
                intentt.putExtra("exp_date", expDateTemp2);
                intentt.putExtra("ea_unit", mString[which]);
                if (stockinDate == null) {
                    intentt.putExtra("stockin_date", stockinDateShow);

                } else {
                    intentt.putExtra("stockin_date", stockinDate);
                }
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                finish();
            }
        }).show();

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    protected void onPause() {
        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
            if (mCodeScanner != null) {
                mCodeScanner.releaseResources();
            }
        }else {
            cameraSource.release();

        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
            if (mCodeScanner != null) {
                mCodeScanner.startPreview();
            }

        }else {
            initialiseDetectorsAndSources();
        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonQRBack:
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(Qrcode.this, ListQrcode.class);
                    intent.putExtra("btn1", "");
                    intent.putExtra("id_unique_SI", id_unique_SI);
                    intent.putExtra("clickShowListCode","yes");
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }
}

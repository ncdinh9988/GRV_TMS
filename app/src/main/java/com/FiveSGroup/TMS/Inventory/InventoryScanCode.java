package com.FiveSGroup.TMS.Inventory;

import android.Manifest;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.FiveSGroup.TMS.CancelGood.ListQrcode_CancelGood;
import com.FiveSGroup.TMS.CancelGood.Qrcode_CancelGood;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.Warehouse.Product_S_P;
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

public class InventoryScanCode extends AppCompatActivity {
    private SurfaceView surfaceView;
private CodeScanner mCodeScanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    Button btnTruyCap ;
    String value1 = "";
    String value2 = "";
    boolean check = false;
    Intent intent;
    String position = "";
    String pro_cd = "";
    String product_cd = "";
    String stock = "";
    String pro_code = "";
    String pro_name = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "";
    String fromCd = "";
    String checkToFinish = "" , id_unique_IVT = "";
    TextView textViewTitle , showvitri;
    //bi???n ????? test hi???n th??? dialog ????n v??? t??nh
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;
    String vitritu = "";
    int vitri = 0 ;
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
        SharedPreferences sharedPreferencess = getSharedPreferences("vitrituinventory", Context.MODE_PRIVATE);
        vitritu = sharedPreferencess.getString("vitritu", "");
        SharedPreferences sharedPreff = this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        setting = sharedPreff.getString("checked", "");
        try {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
                setContentView(R.layout.layout_qrcode);
                init();
                getDataFromIntent();
                if (setting.equals("HoneyWell")) {

                } else {
                    if (ContextCompat.checkSelfPermission(InventoryScanCode.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(InventoryScanCode.this, new String[]{Manifest.permission.CAMERA}, 123);
                    } else {
                        startScanning();
                    }
                }

            }else {
                setContentView(R.layout.activity_load_camera);
                init();
                getDataFromIntent();
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


        check = true;
        setCheckBox();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(InventoryScanCode.this, InventoryListProduct.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CmnFns.hideSoftKeyboard(InventoryScanCode.this);
                } catch (Exception e) {

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
                        Toast.makeText(InventoryScanCode.this, result.getText(), Toast.LENGTH_SHORT).show();
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
        checkBoxGetLPN.setVisibility(View.GONE);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        textViewTitle = findViewById(R.id.tvTitle);
        showvitri = findViewById(R.id.vitritu);
        showvitri.setVisibility(View.VISIBLE);
        showvitri.setText(vitritu);
        textViewTitle.setText("QU??T M?? - KI???M T???N");
        buttonBack = findViewById(R.id.buttonQRBack);

    }

    private void setCheckBox() {
        checkBoxGetDVT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxGetLPN.setChecked(false);
                }

            }
        });
        checkBoxGetLPN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxGetDVT.setChecked(false);
                }
            }
        });
    }

    private void getDataFromIntent() {
        intent = getIntent();
        checkToFinish = intent.getStringExtra("check_to_finish_at_list");
        ea_unit_position = intent.getStringExtra("ea_unit_position");
//        vitritu = intent.getStringExtra("vitritu");
        // position ???????c truy???n qua ????? ?????nh danh n?? ???????c b???m from hay to
        position = intent.getStringExtra("position");
        // ???????c tuy???n qua t??? adapter
        product_cd = intent.getStringExtra("product_cd");
        // ???????c truy???n qua t??? adapter
        stock = intent.getStringExtra("stock");
        id_unique_IVT = intent.getStringExtra("id_unique_IVT");

        // expiredDate truy???n t??? adapter ????? x??? l?? from - to
        expiredDate = intent.getStringExtra("c");
        // stockindate truy???n t??? adapter ????? x??? l?? from - to
        stockinDate = intent.getStringExtra("stockin_date");
        if (!(position == null)) {
            checkBoxGetDVT.setVisibility(View.GONE);
            checkBoxGetLPN.setVisibility(View.VISIBLE);
            showvitri.setVisibility(View.GONE);
            checkBoxGetLPN.setChecked(false);
            if (position.equals("1")) {
                textViewTitle.setText("QU??T V??? TR?? T???");
            } else if (position.equals("2")) {
                textViewTitle.setText("QU??T V??? TR?? ?????N");
            }
        } else {
            checkBoxGetDVT.setVisibility(View.VISIBLE);
            checkBoxGetLPN.setVisibility(View.GONE);
            checkBoxGetDVT.setChecked(true);
            checkBoxGetLPN.setChecked(false);
            textViewTitle.setText("QU??T M?? - KI???M T???N");
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
                    if (ActivityCompat.checkSelfPermission(InventoryScanCode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(InventoryScanCode.this, new
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
                                    Toast.makeText(InventoryScanCode.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                    Log.e("barcode2", "" + barcodeData);

                                    if (barcodeData != null) {
                                        barcodeData = barcodeData.replace("\n","");
                                        edtBarcode.setText(barcodeData);
                                        GetData(barcodeData);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(InventoryScanCode.this, "Vui L??ng Th??? L???i", Toast.LENGTH_LONG).show();
                                    Log.d("#777", e.getMessage());
                                    Intent intent = new Intent(InventoryScanCode.this, InventoryListProduct.class);
                                    intent.putExtra("inventory", "333");

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


    private void GetData(final String barcodeData) {
        String texxt = CmnFns.readDataAdmin();

        if (checkBoxGetLPN.isChecked()) {
            if (expiredDate != null) {
                Intent intentt = new Intent(getApplication(), InventoryListProduct.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("id_unique_IVT", id_unique_IVT);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("inventory", "333");

                // truy???n qua cho ListQRCode ????? x??? l?? from - to
                intentt.putExtra("expdate", expiredDate);
                intentt.putExtra("stockin_date", stockinDate);

                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nh???n t??? 2 n??t", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();


            } else {
                Intent intentt = new Intent(getApplication(), InventoryScanCode.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("inventory", "333");
                startActivity(intentt);
                finish();
            }

        } else {
            //TODO
            DatabaseHelper.getInstance().deleteallProduct_S_P();
            if(!(position == null)){
                ReturnPosition(barcodeData);
            }else{
                int statusGetcode = new CmnFns().getProduct_code(barcodeData);
                if (statusGetcode != 1) {
                    ReturnPosition(barcodeData);
                } else {
                    final ArrayList<Product_S_P> product_s_ps = DatabaseHelper.getInstance().getallValueSP();
                    if (product_s_ps.size() > 1) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(InventoryScanCode.this);
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
//                        String product_code = product_s_ps.get(vitri).getPRODUCT_CODE();

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
                        pro_code = product_s_ps.get(0).getPRODUCT_CODE();
                        getinformation(barcodeData);
                    }else{
                        Toast.makeText(InventoryScanCode.this, "M?? Barcode Kh??ng C?? Trong H??? Th???ng", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InventoryScanCode.this, InventoryListProduct.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

        }
    }


    private void getinformation(final String barcodeData) {

        int statusGetCustt = new CmnFns().getDataFromSeverWithBatch2(barcodeData, CmnFns.readDataAdmin(), "WST", 0, global.getInventoryCD());
//        if (statusGetCustt != 1) {
//            ReturnPosition(barcodeData);
//        }
//        else {
//            if (expiredDate != null) {
//
//                ReturnPosition(barcodeData);
//
//            } else {
                // l???y t???t c??? h???n `s??? d???ng trong database ra
                final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallValueinventory(pro_code,vitritu);


                if (expired_date.size() >= 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(InventoryScanCode.this);
                    builder.setTitle("Ch???n H???n S??? D???ng - Ng??y Nh???p Kho - Batch Number");

                    final ArrayList<String> exp_date = new ArrayList<>();
                    for (int i = 0; i < expired_date.size(); i++) {
                        exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM() + " - " + expired_date.get(i).getSTOCKIN_DATE_TAM()
                                + " - " + expired_date.get(i).getBATCH_NUMBER_TAM());                    }

                    // chuy???n ?????i exp_date th??nh m???ng chu???i String
                    String[] mStringArray = new String[exp_date.size()];
                    mStringArray = exp_date.toArray(mStringArray);

                    final String[] mString = mStringArray;
                    builder.setItems(mString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String expDate = mString[which];
                            vitri = which;
                            String product_code = expired_date.get(vitri).getPRODUCT_CODE_TAM();
                            pro_cd = expired_date.get(vitri).getPRODUCT_CD_TAM();
                            fromCd = expired_date.get(vitri).getWAREHOUSE_POSITION_CD_TAM();
                            dialog.dismiss(); // Close Dialog
                            if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
                                if (expDate != "") {

                                    expDateTemp2 = expDate; //TEST
                                    String chuoi[] = expDateTemp2.split(" - ");
                                    if (chuoi[0].equals("Kh??c")){
                                        Intent intent = new Intent(InventoryScanCode.this, SelectPropertiesProductActivity.class);
                                        intent.putExtra("typeScan", "scan_from_inventory");
                                        intent.putExtra("btn1", barcodeData);
                                        intent.putExtra("stockin", "");
                                        intent.putExtra("total_shelf_life", "0");
                                        intent.putExtra("shelf_life_type", "");
                                        intent.putExtra("pro_code", pro_code);
                                        intent.putExtra("pro_name", pro_name);
                                        intent.putExtra("min_rem_shelf_life", "0");
                                        intent.putExtra("returnposition", position);
                                        intent.putExtra("id_unique_IVT", id_unique_IVT);
                                        intent.putExtra("returnCD", product_cd);
                                        intent.putExtra("returnStock", stock);
                                        intent.putExtra("fromCd", fromCd);
                                        DatabaseHelper.getInstance().deleteallExp_date();
                                        DatabaseHelper.getInstance().deleteallEa_Unit();
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                    if (!checkBoxGetDVT.isChecked()) {
                                        ReturnProduct(barcodeData, chuoi[0], chuoi[1] ,chuoi[2]);
                                        //ReturnProduct(barcodeData,expDateTemp2,"");
                                    } else {
                                        ShowDialogUnit(barcodeData, chuoi[0], chuoi[1] ,chuoi[2]);
                                    }

                                }
                                // Do some thing....
                                // For example: Call method of MainActivity.
                                Toast.makeText(InventoryScanCode.this, "You select: " + expDate,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Checkproduct_Code();
                            }

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
//                else if (expired_date.size() == 1) {
//                    String expDatetemp = "" , batch_number = "", product_code = "" , stockin_date = "";
//                    try {
//                        expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
//                        stockin_date = expired_date.get(0).getSTOCKIN_DATE_TAM();
//                        batch_number = expired_date.get(0).getBATCH_NUMBER_TAM();
//                        product_code = expired_date.get(0).getPRODUCT_CODE_TAM();
//                        pro_cd = expired_date.get(0).getPRODUCT_CD_TAM();
//                        fromCd = expired_date.get(0).getWAREHOUSE_POSITION_CD_TAM();
//                    } catch (Exception e) {
//
//                    }
//                    if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
////                            String chuoi[] = expDatetemp.split(" - ");
//                        if (!checkBoxGetDVT.isChecked()) {
//                            ReturnProduct(barcodeData, expDatetemp, stockin_date ,batch_number);
//                        } else {
//                            ShowDialogUnit(barcodeData, expDatetemp, stockin_date ,batch_number);
//                        }
//                    }else{
//                        Checkproduct_Code();
//                    }
//
//                }
                else {
                    Toast.makeText(InventoryScanCode.this, "Kh??ng T??m Th???y S???n Ph???m ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InventoryScanCode.this, InventoryListProduct.class);
//                    intent.putExtra("inventory", "333");
//                    intent.putExtra("id_unique_IVT", id_unique_IVT);
                    startActivity(intent);
                    finish();
                }

//            }
//        }

    }
    private void Checkproduct_Code(){
        Intent intentt = new Intent(getApplication(), InventoryListProduct.class);
        intentt.putExtra("key", "1");
        startActivity(intentt);
    }

    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), InventoryListProduct.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);
        intentt.putExtra("pro_cd", pro_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("inventory", "333");
        intentt.putExtra("id_unique_IVT", id_unique_IVT);
        // truy???n qua cho ListQRCode ????? x??? l?? from - to
        intentt.putExtra("expdate", expiredDate);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("stockin_date", stockinDate);

        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp, String stockinDateShow, String batch_number) {
        // m???c ?????nh ????n v??? l?? 1
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1",pro_code);
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), InventoryListProduct.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);
        intentt.putExtra("pro_cd", pro_cd);
        intentt.putExtra("vitritu", vitritu);
        intentt.putExtra("fromCd", fromCd);

        intentt.putExtra("id_unique_IVT", id_unique_IVT);
        intentt.putExtra("exp_date", expDatetemp);
        intentt.putExtra("inventory", "333");
        intentt.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        if (stockinDate == null) {
            intentt.putExtra("stockin_date", stockinDateShow);

        } else {
            intentt.putExtra("stockin_date", stockinDate);
        }
        intentt.putExtra("batch_number", batch_number);


        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();
    }


    private void ShowDialogUnit(final String barcode, final String expDateTemp2, final String stockinDateShow,final String batch_number) {
        // kh??ng m???c ?????nh ????n v??? ph???i ch???n
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

        AlertDialog.Builder builderDVT = new AlertDialog.Builder(InventoryScanCode.this);
        builderDVT.setTitle("CH???N ????N V??? T??NH");
        builderDVT.setCancelable(false);
        builderDVT.setItems(mString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(InventoryScanCode.this, mString[which], Toast.LENGTH_LONG).show();
                Intent intentt = new Intent(getApplication(), InventoryListProduct.class);
                intentt.putExtra("btn1", barcode);
                intentt.putExtra("pro_code", pro_code);
                intentt.putExtra("pro_name", pro_name);
                intentt.putExtra("pro_cd", pro_cd);
                intentt.putExtra("vitritu", vitritu);
                intentt.putExtra("fromCd", fromCd);

                intentt.putExtra("batch_number", batch_number);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("id_unique_IVT", id_unique_IVT);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                if (stockinDate == null) {
                    intentt.putExtra("stockin_date", stockinDateShow);

                } else {
                    intentt.putExtra("stockin_date", stockinDate);
                }

                intentt.putExtra("inventory", "333");
                // truy???n qua cho ListQRcode ????? add v??o text HSD
                intentt.putExtra("exp_date", expDateTemp2);
                intentt.putExtra("ea_unit", mString[which]);
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
}

package com.FiveSGroup.TMS.TransferUnit;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
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

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.FiveSGroup.TMS.Warehouse.Product_S_P;
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

public class TransferUnitQrcode extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView surfaceView;
private CodeScanner mCodeScanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    boolean check = false;
    Intent intent;
    String position = "";
    String product_cd = "";
    String pro_code = "";
    String pro_name = "";
    String stock = "";
    String expiredDate = " ";
    String expiredTemp = "";
    String ea_unit_position = " ";
    String stockinDate = "";
    String checkToFinish = "";
    String id_unique_LD = "";
    TextView textViewTitle;
    String pro_cd = "";
    //biến để test hiển thị dialog đơn vị tính
    private String expDateTemp2 = "";
    View viewScan;
    Button buttonBack, btnSend;
    private EditText edtBarcode;
    int setting = 0 ;

    private boolean isUp;

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
        setting = sharedPreff.getInt("checkedRadioButtonId", 0);
        try {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
                setContentView(R.layout.layout_qrcode);
                init();
                if (setting == 2131296697) {

                } else {
                    if (ContextCompat.checkSelfPermission(TransferUnitQrcode.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(TransferUnitQrcode.this, new String[]{Manifest.permission.CAMERA}, 123);
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
        if (setting == 2131296697) {
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

        getDataFromIntent();
        check = true;
        isUp = false;

        if (!(position == null)) {
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setChecked(false);
            if (position.equals("1")) {
                textViewTitle.setText("QUÉT VỊ TRÍ TỪ");
            } else if (position.equals("2")) {
                textViewTitle.setText("QUÉT VỊ TRÍ ĐẾN");
            }
        } else {
            checkBoxGetDVT.setVisibility(View.VISIBLE);
            checkBoxGetLPN.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setChecked(false);
            checkBoxGetDVT.setChecked(true);
            checkBoxGetDVT.setClickable(false);
            textViewTitle.setText("QUÉT MÃ - CHUYỂN ĐVT");
        }

        buttonBack.setOnClickListener(this);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CmnFns.hideSoftKeyboard(TransferUnitQrcode.this);
                } catch (Exception e) {

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);
            }
        });

        setCheckBox();
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
                        Toast.makeText(TransferUnitQrcode.this, result.getText(), Toast.LENGTH_SHORT).show();
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
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        textViewTitle = findViewById(R.id.tvTitle);
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
        // ea_unit_position nhận từ adapter để xử lí việc cập nhật product theo đơn vị
        ea_unit_position = intent.getStringExtra("ea_unit_position");
        // position được truyền qua để định danh nó được bấm from hay to
        position = intent.getStringExtra("position");
        // được tuyền qua từ adapter
        product_cd = intent.getStringExtra("product_cd");
        // được truyền qua từ adapter
        stock = intent.getStringExtra("stock");
        id_unique_LD = intent.getStringExtra("id_unique_LD");

        // stockindate truyền từ adapter để xử lí from - to
        stockinDate = intent.getStringExtra("stockin_date");
        // expiredDate truyền từ adapter để xử lí from - to

        expiredDate = intent.getStringExtra("experied_letdown");
    }


    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(TransferUnitQrcode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(TransferUnitQrcode.this, new
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
                Log.d("TAG", "receiveDetections: " + barcodes);

                if (barcodes.size() != 0) {
                    if (check == true) {
                        check = false;
                        Log.d("double", String.valueOf(barcodes.size()));
                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    barcodeData = barcodes.valueAt(0).displayValue;
                                    Toast.makeText(TransferUnitQrcode.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                    Log.e("barcode2", "" + barcodeData);

                                    if (barcodeData != null) {
                                        barcodeData = barcodeData.replace("\n", "");
                                        edtBarcode.setText(barcodeData);
                                        GetData(barcodeData);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(TransferUnitQrcode.this, "Vui Lòng Thử Lại", Toast.LENGTH_LONG).show();
                                    Log.d("#777: ", e.getMessage());
                                    Intent intent = new Intent(TransferUnitQrcode.this, TransferUnitActivity.class);
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
                Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("id_unique_LD", id_unique_LD);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("transfer_unit", "333");
                intentt.putExtra("barcodeData", "barcodeData");

                // truyền qua cho ListQRCode để xử lí from - to
                intentt.putExtra("expdate", expiredDate);
                intentt.putExtra("stockin_date", stockinDate);

                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nhận từ 2 nút", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();


            } else {
                Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("transfer_unit", "333");
                intentt.putExtra("id_unique_LD", id_unique_LD);
                startActivity(intentt);
                finish();
            }

        } else {
            DatabaseHelper.getInstance().deleteallProduct_S_P();

            int statusGetcode = new CmnFns().getProduct_code(barcodeData);
            if (statusGetcode != 1) {
                ReturnPosition(barcodeData);
            } else {
                final ArrayList<Product_S_P> product_s_ps = DatabaseHelper.getInstance().getallValueSP();
                if (product_s_ps.size() > 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TransferUnitQrcode.this);
                    builder.setTitle("Mã Sản Phẩm - Tên Sản Phẩm");

                    final ArrayList<String> product_code = new ArrayList<>();
                    for (int i = 0; i < product_s_ps.size(); i++) {
                        product_code.add(product_s_ps.get(i).getPRODUCT_CODE() + " - " + product_s_ps.get(i).getPRODUCT_NAME());
                    }
                    // chuyển đổi exp_date thành mảng chuỗi String
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
                    alertDialog.show();

                }else if(product_s_ps.size() == 1){
                    pro_code = product_s_ps.get(0).getPRODUCT_CODE();
                    getinformation(barcodeData);
                }else{
                    Toast.makeText(TransferUnitQrcode.this, "Mã Barcode Không Có Trong Hệ Thống", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TransferUnitQrcode.this, TransferUnitActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void getinformation(final String barcodeData) {
        int statusGetCustt = new CmnFns().getDataFromSeverWithBatch2(barcodeData, CmnFns.readDataAdmin(), "WOI", 0, "");
        // expiredDate nhận giá trị từ adapter để xử lí position
//        if (statusGetCustt != 1) {
//            ReturnPosition(barcodeData);
//        }
//        else {
//            if (expiredDate != null) {
//
//                ReturnPosition(barcodeData);
//
//            } else {
                try {
                    // lấy tất cả hạn sử dụng trong database ra
                    final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallValue2(pro_code);

                    // chia expiredDate ra các trường hợp để quyết định có show dialog hay không

                    if (expired_date.size() > 1) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(TransferUnitQrcode.this);
                        builder.setTitle("Chọn Hạn Sử Dụng - Ngày Nhập Kho - Batch Number");

                        final ArrayList<String> exp_date = new ArrayList<>();
                        for (int i = 0; i < expired_date.size(); i++) {
                            exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM() + " - " + expired_date.get(i).getSTOCKIN_DATE_TAM()
                                    + " - " + expired_date.get(i).getBATCH_NUMBER_TAM());

                            //exp_date.add(expired_date.get(i).getSTOCKIN_DATE_TAM());

                        }

                        // chuyển đổi exp_date thành mảng chuỗi String
                        String[] mStringArray = new String[exp_date.size()];
                        mStringArray = exp_date.toArray(mStringArray);

                        final String[] mString = mStringArray;
                        builder.setItems(mString, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String expDate = mString[which];

                                int vitri = which;
                                String product_code = expired_date.get(vitri).getPRODUCT_CODE_TAM();
                                pro_cd = expired_date.get(vitri).getPRODUCT_CD_TAM();
                                dialog.dismiss(); // Close Dialog
                                if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
                                    if (expDate != "") {
                                        // expDateTemp2 lấy giá trị HSD được người dùng chọn
                                        expDateTemp2 = expDate;
                                        String chuoi[] = expDateTemp2.split(" - ");
                                        if (chuoi[0].equals("Khác")) {
                                            Intent intent = new Intent(TransferUnitQrcode.this, SelectPropertiesProductActivity.class);
                                            intent.putExtra("typeScan", "scan_from_letdown");
                                            intent.putExtra("btn1", barcodeData);
                                            intent.putExtra("returnposition", position);
                                            intent.putExtra("pro_code", pro_code);
                                            intent.putExtra("pro_name", pro_name);
                                            intent.putExtra("returnCD", product_cd);
                                            intent.putExtra("id_unique_LD", id_unique_LD);
                                            intent.putExtra("returnStock", stock);
                                            DatabaseHelper.getInstance().deleteallExp_date();
                                            DatabaseHelper.getInstance().deleteallEa_Unit();
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                        if (!checkBoxGetDVT.isChecked()) {
                                            ReturnProduct(barcodeData, chuoi[0], chuoi[1] ,chuoi[2]);
                                        } else {
                                            ShowDialogUnit(barcodeData, chuoi[0], chuoi[1], chuoi[2]);
                                        }

                                    }
                                    // Do some thing....
                                    // For example: Call method of MainActivity.
                                    Toast.makeText(TransferUnitQrcode.this, "You select: " + expDate,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Checkproduct_Code();
                                }



                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if (expired_date.size() == 1) {
                        String expDatetemp = "" , batch_number = "", product_code = "" , stockin_date = "";
                        try {
                            expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
                            stockin_date = expired_date.get(0).getSTOCKIN_DATE_TAM();
                            batch_number = expired_date.get(0).getBATCH_NUMBER_TAM();
                            product_code = expired_date.get(0).getPRODUCT_CODE_TAM();
                            pro_cd = expired_date.get(0).getPRODUCT_CD_TAM();
                        } catch (Exception e) {

                        }
                        if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
//                            String chuoi[] = expDatetemp.split(" - ");
                            if (!checkBoxGetDVT.isChecked()) {
                                ReturnProduct(barcodeData, expDatetemp, stockin_date ,batch_number);
                            } else {
                                ShowDialogUnit(barcodeData, expDatetemp, stockin_date ,batch_number);
                            }
                        }else{
                            Checkproduct_Code();
                        }

                    } else {
                        Toast.makeText(TransferUnitQrcode.this, "Sản Phẩm Không Có Trong Kho", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TransferUnitQrcode.this, TransferUnitActivity.class);
//                        intent.putExtra("btn1", barcodeData);
//                        intent.putExtra("returnposition", position);
//                        intent.putExtra("return_ea_unit_position", ea_unit_position);
//                        intent.putExtra("returnCD", product_cd);
//                        intent.putExtra("returnStock", stock);
//                        intent.putExtra("transfer_unit", "333");
//                        intent.putExtra("id_unique_LD", id_unique_LD);
//
//                        // truyền qua cho letdowmQrCode để xử lí from - to
//
//                        intent.putExtra("expdate", expiredDate);
                        startActivity(intent);
                        finish();

                    }
                } catch (Exception e) {
                    Toast.makeText(TransferUnitQrcode.this, "Vui Lòng Thử Lại", Toast.LENGTH_LONG).show();
                    Log.d("#778:", e.getMessage());
                }

//            }
//        }
    }
    private void Checkproduct_Code(){
        Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
        intentt.putExtra("key", "1");
        startActivity(intentt);
    }


    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("stockin_date", stockinDate);
        intentt.putExtra("transfer_unit", "333");
        intentt.putExtra("id_unique_LD", id_unique_LD);
        // truyền qua cho letdowmQrCode để xử lí from - to

        intentt.putExtra("expdate", expiredDate);

        intentt.putExtra("pro_cd", pro_cd);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);


        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        //editor.commit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp, String stockinDateShow, String batch_number) {

        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1",pro_code);
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);
        intentt.putExtra("pro_cd", pro_cd);

        intentt.putExtra("returnStock", stock);
        intentt.putExtra("exp_date", expDatetemp);
        intentt.putExtra("stock_in", stockinDate);
        intentt.putExtra("id_unique_LD", id_unique_LD);
        if (stockinDate == null) {
            intentt.putExtra("stockin_date", stockinDateShow);

        } else {
            intentt.putExtra("stockin_date", stockinDate);
        }

        intentt.putExtra("transfer_unit", "333");
        if (ea_unit_tams.size() > 0) {
            intentt.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());
        }
        intentt.putExtra("batch_number", batch_number);


        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        //editor.commit();
        finish();
    }


    private void ShowDialogUnit(final String barcode, final String expDateTemp2, final String stockinDateShow,final String batch_number) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "2",pro_code);

        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        final ArrayList<String> ea_unit = new ArrayList<>();
        for (int i = 0; i < ea_unit_tams.size(); i++) {
            ea_unit.add(ea_unit_tams.get(i).getEA_UNIT_TAM());
        }

        // chuyển đổi exp_date thành mảng chuỗi String
        String[] mStringArray = new String[ea_unit.size()];
        mStringArray = ea_unit.toArray(mStringArray);
        final String[] mString = mStringArray;


        AlertDialog.Builder builderDVT = new AlertDialog.Builder(TransferUnitQrcode.this);
        builderDVT.setTitle("CHỌN ĐƠN VỊ TÍNH");
        builderDVT.setCancelable(false);
        builderDVT.setItems(mString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(TransferUnitQrcode.this, mString[which], Toast.LENGTH_LONG).show();
                Intent intentt = new Intent(getApplication(), TransferUnitActivity.class);
                intentt.putExtra("btn1", barcode);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("batch_number", batch_number);
                intentt.putExtra("pro_code", pro_code);
                intentt.putExtra("pro_name", pro_name);
                intentt.putExtra("pro_cd", pro_cd);

                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("transfer_unit", "333");
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("stock_in", stockinDate);
                intentt.putExtra("id_unique_LD", id_unique_LD);
                // truyền qua cho ListQRcode để add vào text HSD
                intentt.putExtra("exp_date", expDateTemp2);
                intentt.putExtra("ea_unit", mString[which]);
                if (stockinDate == null) {
                    intentt.putExtra("stockin_date", stockinDateShow);

                } else {
                    intentt.putExtra("stockin_date", stockinDate);
                }

                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
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
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonQRBack:
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(TransferUnitQrcode.this, TransferUnitActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }
}

package com.FiveSGroup.TMS.Warehouse;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.global;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class Qrcode extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView surfaceView;
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
    String product_cd = "";
    String stock = "";
    String stock_in = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "";
    TextView textViewTitle;
    //biến để test hiển thị dialog đơn vị tính
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;;
    String checkToFinish = "";
    String exp_date;
    String stockin_date;
    String ea_unit;
    String total_shelf_life = "";
    String shelf_life_type = "";
    String min_rem_shelf_life = "";



    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_load_camera);
        init();
        check = true;
        getDataFromIntent();

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


    private void init() {
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        btnSend = findViewById(R.id.btnSend);
        edtBarcode = findViewById(R.id.edtBarcode);
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        textViewTitle = findViewById(R.id.tvTitle);
        textViewTitle.setText("QUÉT MÃ - NHẬP KHO");
        buttonBack = findViewById(R.id.buttonQRBack);
        try {
            initialiseDetectorsAndSources();

        } catch (Exception e) {

        }
    }

    private void getDataFromIntent() {
        intent = getIntent();
        checkToFinish = intent.getStringExtra("check_to_finish_at_list");
        ea_unit_position = intent.getStringExtra("ea_unit_position");
        // position được truyền qua để định danh nó được bấm from hay to
        position = intent.getStringExtra("position");
        // được tuyền qua từ adapter
        product_cd = intent.getStringExtra("product_cd");
        // được truyền qua từ adapter
        stock = intent.getStringExtra("stock");

        exp_date = intent.getStringExtra("exp_date");
        stockin_date = intent.getStringExtra("stockin_date");
        ea_unit = intent.getStringExtra("ea_unit");

        // expiredDate truyền từ adapter để xử lí from - to
        expiredDate = intent.getStringExtra("c");
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
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setChecked(false);
            textViewTitle.setText("QUÉT MÃ - NHẬP KHO");
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
                                        edtBarcode.setText(barcodeData);
                                        GetData(barcodeData);
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(Qrcode.this, "Vui Lòng Thử Lại 1", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Qrcode.this, SelectPropertiesProductActivity.class);
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

                // truyền qua cho ListQRCode để xử lí from - to
                intentt.putExtra("exp_date", exp_date);
                intentt.putExtra("stockin_date", stockin_date);
                intentt.putExtra("ea_unit", ea_unit);

                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nhận từ 2 nút", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();
            } else {
                int statusGetCustt = new CmnFns().getExpDateFromServer(global.getAdminCode(),barcodeData, global.getStockReceiptCd());
                if (statusGetCustt != 1) {
                    ReturnPosition(barcodeData);
                } else {
                    if (expiredDate != null) {

                        ReturnPosition(barcodeData);

                    } else {
                        // lấy tất cả hạn `sử dụng trong database ra
                        final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallExp_date();
                        for (int i = 0; i < expired_date.size(); i++) {
                                                            total_shelf_life = expired_date.get(i).getTOTAL_SHELF_LIFE();
                            shelf_life_type = expired_date.get(i).getSHELF_LIFE_TYPE();
                            min_rem_shelf_life = expired_date.get(i).getMIN_REM_SHELF_LIFE();
                                                        }


//                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(Qrcode.this);
//                                                        builder.setTitle("Chọn Hạn Sử Dụng - Ngày Nhập Kho");
//
//                                                        final ArrayList<String> exp_date = new ArrayList<>();
//                                                        for (int i = 0; i < expired_date.size(); i++) {
//                                                            exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM());
//                                                        }
//
//                                                        // chuyển đổi exp_date thành mảng chuỗi String
//                                                        String[] mStringArray = new String[exp_date.size()];
//                                                        mStringArray = exp_date.toArray(mStringArray);
//
//                                                        final String[] mString = mStringArray;
//                                                        builder.setItems(mString, new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                String expDate = mString[which];
//
//                                                                dialog.dismiss(); // Close Dialog
//
//                                                                if (expDate != "") {
//                                                                    expDateTemp2 = expDate; //TEST
//                                                                    String chuoi[] = expDateTemp2.split(" - ");
//                                                                    if (!checkBoxGetDVT.isChecked()) {
//                                                                        ReturnProduct(barcodeData, chuoi[0], chuoi[1]);
//                                                                        //ReturnProduct(barcodeData,expDateTemp2,"");
//                                                                    } else {
//                                                                        ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);
//                                                                    }
//
//                                                                }
//                                                                // Do some thing....
//                                                                // For example: Call method of MainActivity.
//                                                                Toast.makeText(Qrcode.this, "You select: " + expDate,
//                                                                        Toast.LENGTH_LONG).show();
//                                                            }
//                                                        });
//                                                        AlertDialog alertDialog = builder.create();
//                                                        alertDialog.show();
//                                                    } else if (expired_date.size() == 1) {
//                                                        String expDatetemp = "";
//                                                        try {
//                                                            expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
//                                                        } catch (Exception e) {
//
//                                                        }
//                                                        String chuoi[] = expDatetemp.split(" - ");
//
//                                                        if (!checkBoxGetDVT.isChecked()) {
//                                                            ReturnProduct(barcodeData, chuoi[0], chuoi[1]);
////                                                        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcodeData);
////                                                        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();
////                                                        ReturnProduct(barcodeData, expDatetemp, ea_unit_tams.get(0).getEA_UNIT_TAM());
//                                                        } else {
//                                                            ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);
//                                                        }

                        Intent intent = new Intent(Qrcode.this, SelectPropertiesProductActivity.class);
                        intent.putExtra("typeScan", "scan_from_stock_in");
                        intent.putExtra("btn1", barcodeData);
                        intent.putExtra("stock_in", "333");
                        intent.putExtra("total_shelf_life", total_shelf_life);
                        intent.putExtra("shelf_life_type", shelf_life_type);
                        intent.putExtra("min_rem_shelf_life", min_rem_shelf_life);
                        intent.putExtra("returnposition", position);
                        intent.putExtra("returnCD", product_cd);
                        intent.putExtra("returnStock", stock);
                        DatabaseHelper.getInstance().deleteallExp_date();
                        DatabaseHelper.getInstance().deleteallEa_Unit();
                        startActivity(intent);
                    }
                }
            }

        }
    }

//    private void GetData(final String barcodeData){
//
//        if (checkBoxGetLPN.isChecked()) {
//            if (expiredDate != null) {
//                Intent intentt = new Intent(getApplication(), ListQrcode.class);
//                intentt.putExtra("lpn", "444");
//                intentt.putExtra("btn1", barcodeData);
//                intentt.putExtra("returnposition", position);
//                intentt.putExtra("return_ea_unit_position", ea_unit_position);
//                intentt.putExtra("returnCD", product_cd);
//                intentt.putExtra("returnStock", stock);
//                intentt.putExtra("put_away", "333");
//
//                // truyền qua cho ListQRCode để xử lí from - to
//                intentt.putExtra("expdate", expiredDate);
//                intentt.putExtra("stockin_date", stockinDate);
//
//                Log.e("barcodeData1", "" + barcodeData);
//                Log.e("nhận từ 2 nút", "" + position);
//                startActivity(intentt);
//                DatabaseHelper.getInstance().deleteallExp_date();
//                DatabaseHelper.getInstance().deleteallEa_Unit();
//                //editor.commit();
//                finish();
//
//
//            } else {
//                Intent intentt = new Intent(Qrcode.this, ListQrcode.class);
//                intentt.putExtra("lpn", "444");
//                intentt.putExtra("btn1", barcodeData);
//                intentt.putExtra("put_away", "333");
//                startActivity(intentt);
//                finish();
//            }
//
//        } else {
//            int statusGetCustt = new CmnFns().getExpDateFromServer(barcodeData, global.getStockReceiptCd());
//            if (statusGetCustt != 1) {
//                ReturnPosition(barcodeData);
//            } else {
//                if (expiredDate != null) {
//
//                    ReturnPosition(barcodeData);
//
//                } else {
//                    // lấy tất cả hạn `sử dụng trong database ra
//                    final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallExp_date();
//
//
//                    if (expired_date.size() > 1) {
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(Qrcode.this);
//                        builder.setTitle("Chọn Hạn Sử Dụng - Ngày Nhập Kho");
//
//                        final ArrayList<String> exp_date = new ArrayList<>();
//                        for (int i = 0; i < expired_date.size(); i++) {
//                            exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM());
//                        }
//
//                        // chuyển đổi exp_date thành mảng chuỗi String
//                        String[] mStringArray = new String[exp_date.size()];
//                        mStringArray = exp_date.toArray(mStringArray);
//
//                        final String[] mString = mStringArray;
//                        builder.setItems(mString, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String expDate = mString[which];
//
//                                dialog.dismiss(); // Close Dialog
//
//                                if (expDate != "") {
//                                    expDateTemp2 = expDate; //TEST
//                                    String chuoi[] = expDateTemp2.split(" - ");
//                                    if (!checkBoxGetDVT.isChecked()) {
//                                        ReturnProduct(barcodeData, chuoi[0], chuoi[1]);
//                                        //ReturnProduct(barcodeData,expDateTemp2,"");
//                                    } else {
//                                        ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);
//                                    }
//
//                                }
//                                // Do some thing....
//                                // For example: Call method of MainActivity.
//                                Toast.makeText(Qrcode.this, "You select: " + expDate,
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    } else if (expired_date.size() == 1) {
//                        String expDatetemp = "";
//                        try {
//                            expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
//                        } catch (Exception e) {
//
//                        }
//                        String chuoi[] = expDatetemp.split(" - ");
//
//                        if (!checkBoxGetDVT.isChecked()) {
//                            ReturnProduct(barcodeData, chuoi[0], chuoi[1]);
//                        } else {
//                            ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);
//                        }
//
//                    } else {
//                        Toast.makeText(Qrcode.this, "Vui Lòng Thử Lại", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Qrcode.this, ListQrcode.class);
//                        intent.putExtra("btn1", barcodeData);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                }
//            }
//        }
//    }
    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), ListQrcode.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("stock_in", "333");
        // truyền qua cho ListQRCode để xử lí from - to
//        intentt.putExtra("expdate", expiredDate);
//        intentt.putExtra("stockin_date", stockinDate);
        intentt.putExtra("exp_date", exp_date);
        intentt.putExtra("stockin_date", stockin_date);
        intentt.putExtra("ea_unit", exp_date);

        Log.e("barcodeData1", "" + barcodeData);
        Log.e("nhận từ 2 nút", "" + position);
        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp, String stockinDateShow) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1");
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), ListQrcode.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("exp_date", expDatetemp);
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
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "2");

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

        // chuyển đổi exp_date thành mảng chuỗi String
        String[] mStringArray = new String[ea_unit.size()];
        mStringArray = ea_unit.toArray(mStringArray);
        final String[] mString = mStringArray;

        AlertDialog.Builder builderDVT = new AlertDialog.Builder(Qrcode.this);
        builderDVT.setTitle("CHỌN ĐƠN VỊ TÍNH");
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
                // truyền qua cho ListQRcode để add vào text HSD
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
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
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

package com.FiveSGroup.TMS.StockOut.OD;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
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
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
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

import com.FiveSGroup.TMS.R;
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

public class Qrcode_Stock_Out_OD_Finish extends AppCompatActivity {
    private SurfaceView surfaceView;
    private CodeScanner mCodeScanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText ,tvOD;
    private String barcodeData;
    Button btndone;
    boolean check = false;
    String position = "" , LPN ="";
    String lpn_stokout_od = "";
    String positionOD = "";
    String checkToFinish = "" , id_unique_IVT = "";
    TextView textViewTitle;
    //biến để test hiển thị dialog đơn vị tính
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;
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
        sharedPreferencess.edit().remove("vitritu").commit();
        SharedPreferences sharedPreff = this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        setting = sharedPreff.getString("checked", "");
        try {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
                setContentView(R.layout.layout_qrcode_od);
                init();
                getDataFromIntent();
                if (setting.equals("HoneyWell")) {

                } else {
                    if (ContextCompat.checkSelfPermission(Qrcode_Stock_Out_OD_Finish.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(Qrcode_Stock_Out_OD_Finish.this, new String[]{Manifest.permission.CAMERA}, 123);
                    } else {
                        startScanning();
                    }
                }

            }else {
                setContentView(R.layout.activity_load_camera_od);
                init();
                getDataFromIntent();
                initialiseDetectorsAndSources();

            }
        } catch (Exception e) {
            Log.d("error_od",""+ e.toString());
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

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(Qrcode_Stock_Out_OD_Finish.this, Home_Stockout_OD.class);
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
                    CmnFns.hideSoftKeyboard(Qrcode_Stock_Out_OD_Finish.this);
                } catch (Exception e) {

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = new CmnFns().synchronizeData_With_Message(tvOD.getText().toString(),"OD_WSO");
                Toast.makeText(getApplicationContext(),""+ result,Toast.LENGTH_LONG);
                if (result.equals("1")) {
                    ShowSuccessMessage("Lưu thành công");
                } else {
                    ShowSuccessMessage(result);
                }
            }
        });


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(Qrcode_Stock_Out_OD_Finish.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(Qrcode_Stock_Out_OD_Finish.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
        if(message.equals("Lưu thành công")){
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper.getInstance().deleteProduct_Stockout_OD();
                    dialog.dismiss();
                    Intent intentToHomeQRActivity = new Intent(Qrcode_Stock_Out_OD_Finish.this, Home_Stockout_OD.class);
                    startActivity(intentToHomeQRActivity);
                    finish();
                }
            });
        }else{
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
        }
        dialog.show();
    }

    private void GetData(final String barcodeData) {
        positionOD = barcodeData;
        global.setPosition_CD(positionOD);
        Intent intent = new Intent(Qrcode_Stock_Out_OD_Finish.this, Qrcode_Stock_Out_OD.class);
        intent.putExtra("lpn_stokout_od",positionOD);
        startActivity(intent);
        finish();

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
                        positionOD = result.getText();
                        global.setPosition_CD(positionOD);
                        Intent intent = new Intent(Qrcode_Stock_Out_OD_Finish.this,Qrcode_Stock_Out_OD.class);
                        intent.putExtra("lpn_stokout_od",positionOD);
                        startActivity(intent);
                        finish();
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
        Intent intent = getIntent();

        lpn_stokout_od = intent.getStringExtra("lpn_stokout_od");
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        btnSend = findViewById(R.id.btnSend);
        edtBarcode = findViewById(R.id.edtBarcode);
        btndone = findViewById(R.id.btndone);
        btndone.setVisibility(View.VISIBLE);
        tvOD = findViewById(R.id.tvOD);
        if(lpn_stokout_od.equals(null) || lpn_stokout_od.equals("")){

        }else{
            tvOD.setText(lpn_stokout_od);
        }
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        checkBoxGetLPN.setVisibility(View.GONE);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        textViewTitle = findViewById(R.id.tvTitle);

        textViewTitle.setText("QUÉT MÃ - XUẤT KHO LPN OD");
        buttonBack = findViewById(R.id.buttonQRBack);

    }



    private void getDataFromIntent() {
        checkBoxGetDVT.setVisibility(View.GONE);
        checkBoxGetLPN.setVisibility(View.GONE);
        tvOD.setVisibility(View.VISIBLE);

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
                    if (ActivityCompat.checkSelfPermission(Qrcode_Stock_Out_OD_Finish.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Qrcode_Stock_Out_OD_Finish.this, new
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
                                    positionOD = barcodeData;
                                    global.setPosition_CD(positionOD);
                                    Intent intent = new Intent(Qrcode_Stock_Out_OD_Finish.this,Qrcode_Stock_Out_OD.class);
                                    intent.putExtra("lpn_stokout_od",positionOD);
                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    Toast.makeText(Qrcode_Stock_Out_OD_Finish.this, "Vui Lòng Thử Lại", Toast.LENGTH_LONG).show();
                                }


                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        });
                    }


                }
            }
        });
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

    }
}

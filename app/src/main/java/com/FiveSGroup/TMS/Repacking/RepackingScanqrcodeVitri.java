package com.FiveSGroup.TMS.Repacking;

import android.Manifest;
import android.content.Context;
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

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import java.io.IOException;

public class RepackingScanqrcodeVitri extends AppCompatActivity {
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
    String pro_cd = "";
    String product_cd = "";
    String stock = "";
    String pro_code = "";
    String pro_name = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "";
    String checkToFinish = "" , id_unique_IVT = "";
    TextView textViewTitle;
    //bi???n ????? test hi???n th??? dialog ????n v??? t??nh
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;
    String vitritu = "";
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
        SharedPreferences sharedPreferencess = getSharedPreferences("vitrituinrepacking", Context.MODE_PRIVATE);
        sharedPreferencess.edit().remove("vitritu").commit();
        sharedPreferencess.edit().remove("Warehouse_Position_CD").commit();
        SharedPreferences sharedPreff = this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        setting = sharedPreff.getString("checked", "");
        try {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)) {
                setContentView(R.layout.layout_qrcode);
                init();
                getDataFromIntent();
                if (setting.equals("HoneyWell")) {

                } else {
                    if (ContextCompat.checkSelfPermission(RepackingScanqrcodeVitri.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(RepackingScanqrcodeVitri.this, new String[]{Manifest.permission.CAMERA}, 123);
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
                    Intent intent = new Intent(RepackingScanqrcodeVitri.this, RepackingListProduct.class);
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
                    CmnFns.hideSoftKeyboard(RepackingScanqrcodeVitri.this);
                } catch (Exception e) {

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);

            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    private void GetData(final String barcodeData) {
        String statusGetcode = new CmnFns().checkPosition(barcodeData);
        if(!statusGetcode.equals("error")){
            Toast.makeText(RepackingScanqrcodeVitri.this, barcodeData, Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("vitrituinrepacking", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("vitritu", barcodeData);
            editor.putString("Warehouse_Position_CD", statusGetcode);
            editor.apply();
            Intent intentt = new Intent(getApplication(), RepackingScanCode.class);
            intentt.putExtra("vitritu", barcodeData);
            startActivity(intentt);
            finish();
        }else{
            Toast.makeText(RepackingScanqrcodeVitri.this, statusGetcode , Toast.LENGTH_LONG).show();
        }

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
                        String statusGetcode = new CmnFns().checkPosition(result.getText());
                        if(!statusGetcode.equals("error")){
                            Toast.makeText(RepackingScanqrcodeVitri.this, result.getText(), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("vitrituinrepacking", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("vitritu", result.getText());
                            editor.putString("Warehouse_Position_CD", statusGetcode);
                            editor.apply();
                            Intent intentt = new Intent(getApplication(), RepackingScanCode.class);
                            intentt.putExtra("vitritu", result.getText());
                            startActivity(intentt);
                            finish();
                        }else{
                            Toast.makeText(RepackingScanqrcodeVitri.this, "Kh??ng t??m th???y v??? tr?? trong kho" , Toast.LENGTH_LONG).show();
                        }

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
        textViewTitle.setText("QU??T M?? - V??? TR?? REPACKING");
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
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.VISIBLE);
            checkBoxGetLPN.setChecked(false);
            if (position.equals("1")) {
                textViewTitle.setText("QU??T V??? TR?? T???");
            } else if (position.equals("2")) {
                textViewTitle.setText("QU??T V??? TR?? ?????N");
            }
        } else {
            checkBoxGetDVT.setVisibility(View.GONE);
            checkBoxGetLPN.setVisibility(View.GONE);
            checkBoxGetDVT.setChecked(true);
            checkBoxGetLPN.setChecked(false);
            textViewTitle.setText("QU??T M?? - V??? TR?? KI???M T???N");
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
                    if (ActivityCompat.checkSelfPermission(RepackingScanqrcodeVitri.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(RepackingScanqrcodeVitri.this, new
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
                                    String statusGetcode = new CmnFns().checkPosition(barcodeData);
                                    if(!statusGetcode.equals("error")){
                                        Toast.makeText(RepackingScanqrcodeVitri.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                        Log.e("barcode2", "" + barcodeData);

                                        if (barcodeData != null) {
                                            barcodeData = barcodeData.replace("\n","");
                                            edtBarcode.setText(barcodeData);
                                            Toast.makeText(RepackingScanqrcodeVitri.this, barcodeData, Toast.LENGTH_SHORT).show();
                                            SharedPreferences sharedPreferences = getSharedPreferences("vitrituinrepacking", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("vitritu", barcodeData);
                                            editor.putString("Warehouse_Position_CD", statusGetcode);
                                            editor.apply();
                                            Intent intentt = new Intent(getApplication(), RepackingScanCode.class);
                                            intentt.putExtra("vitritu", barcodeData);
                                            startActivity(intentt);
                                            finish();

                                        }
                                    }else{
                                        Toast.makeText(RepackingScanqrcodeVitri.this, statusGetcode , Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(RepackingScanqrcodeVitri.this, "Vui L??ng Th??? L???i", Toast.LENGTH_LONG).show();
                                    Log.d("#777", e.getMessage());
                                    Intent intent = new Intent(RepackingScanqrcodeVitri.this, RepackingListProduct.class);
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

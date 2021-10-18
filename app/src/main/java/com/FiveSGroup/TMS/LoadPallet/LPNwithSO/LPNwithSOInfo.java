package com.FiveSGroup.TMS.LoadPallet.LPNwithSO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LPN.LPNProduct;
import com.FiveSGroup.TMS.LPN.LPNProductAdapter;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletQRCode;
import com.FiveSGroup.TMS.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.Hashtable;

public class LPNwithSOInfo extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewLPNProduct;
    private LPNProductAdapter adapter;
    private ArrayList<LPNProduct> lpnProducts;
    private TextView tvTitle, tvTitleBarcode, tvLPNBarcodeImage;
    private String lpnCode = "", order_code = "";
    private Button buttonBack , btnSuggest , buttonPutToPallet;
    private ImageView imageViewBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpn_so_info);
        init();
        getLPNCode();
        generateLPNBarcode(lpnCode);
        getDataProductOfLPN(lpnCode);
    }

    private void generateLPNBarcode(String lpnCode) {
        try {

//            Typeface font = Typeface.createFromAsset(this.getAssets(),
//                    "fonts/LibreBarcode128Text-Regular.ttf");
//            tvLPNBarcodeImage.setTypeface(font);
//
//            // generate barcode string
////            EAN13CodeBuilder bb = new EAN13CodeBuilder(lpnCode);
//            tvLPNBarcodeImage.setText(lpnCode);

            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(lpnCode, BarcodeFormat.CODE_128,200, 50, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            imageViewBarcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getDataProductOfLPN(String lpnCode) {

        DatabaseHelper.getInstance().deleteLPNProduct();
        int status = new CmnFns().synchronizeGetProductOfLPN(lpnCode, CmnFns.readDataAdmin(), "LPN", 1,"");
        if (status == -1) {
            lpnProducts.clear();
            adapter = new LPNProductAdapter(this, R.layout.product_of_lpn_item, lpnProducts);
            listViewLPNProduct.setAdapter(adapter);
        } else {
            lpnProducts = DatabaseHelper.getInstance().getAllLPNProduct();
            adapter = new LPNProductAdapter(this, R.layout.product_of_lpn_item, lpnProducts);
            listViewLPNProduct.setAdapter(adapter);
        }

    }

    private void getLPNCode() {
        Intent intent = getIntent();
        lpnCode = intent.getStringExtra("LPN_CODE");
        order_code = intent.getStringExtra("order_code");
        tvTitleBarcode.setText("Mã LPN: " + lpnCode);
    }

    private void init() {
        tvTitleBarcode = findViewById(R.id.tvTitleLPNCode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        btnSuggest = findViewById(R.id.btnSuggest);
        btnSuggest.setOnClickListener(this);
        buttonPutToPallet = findViewById(R.id.buttonPutToPallet);
        buttonPutToPallet.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitleLPNProduct);
        listViewLPNProduct = findViewById(R.id.listViewLPNProduct);
        lpnProducts = new ArrayList<>();
        adapter = new LPNProductAdapter(this, R.layout.product_of_lpn_item, lpnProducts);
        listViewLPNProduct.setAdapter(adapter);
        imageViewBarcode = findViewById(R.id.imgLPNBarcodeImage);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                finish();
                break;
            case R.id.btnSuggest:
                Intent intentt = new Intent(LPNwithSOInfo.this, LPNwithSOSuggest.class);
                intentt.putExtra("order_code",order_code);
                intentt.putExtra("lpn_code",lpnCode);
                SharedPreferences sharedPreferencess = getSharedPreferences("lpn_code", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorr = sharedPreferencess.edit();
                editorr.putString("lpn_code", lpnCode);
                editorr.apply();
                startActivity(intentt);
                break;
            case R.id.buttonPutToPallet:
                Intent intent = new Intent(LPNwithSOInfo.this, LoadPalletActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("lpn_code", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lpn_code", lpnCode);
                editor.apply();
                startActivity(intent);
                break;
        }
    }
}
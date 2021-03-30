package com.FiveSGroup.TMS.AddBarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class ListProductBarcodeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvBarcodeProduct;
    Button btnBack;
    ProductBarcodeAdapter adapter;
    ArrayList<ProductBarcode> productBarcodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_barcode);

        init();
    }

    private void init() {
        lvBarcodeProduct = findViewById(R.id.lvProductBarcode);
        btnBack = findViewById(R.id.buttonBack);
        productBarcodes = new ArrayList<>();
        test();
        adapter = new ProductBarcodeAdapter(this, R.layout.item_product_barcode_layout, productBarcodes);
        lvBarcodeProduct.setAdapter(adapter);
        lvBarcodeProduct.setOnItemClickListener(this);
    }

    private void test() {
        productBarcodes.add(new ProductBarcode("88126", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88127", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88128", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88129", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88120", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88121", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88122", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88123", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88124", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88125", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88126", "SDT", "8935015203226"));
        productBarcodes.add(new ProductBarcode("88126", "SDT", "8935015203226"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ListProductBarcodeActivity.this, ScanAddProductBarcode.class);
        intent.putExtra("productCode", productBarcodes.get(position).getCode());
        intent.putExtra("productBarcode", productBarcodes.get(position).getBarcode());
        startActivity(intent);
    }
}
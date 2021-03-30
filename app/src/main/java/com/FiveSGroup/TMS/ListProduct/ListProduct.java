package com.FiveSGroup.TMS.ListProduct;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.LetDown.LetDownAdapter;
import com.FiveSGroup.TMS.LetDown.ProductLetDown;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.StockOut.Product_StockOut;
import com.FiveSGroup.TMS.StockOut.StockoutAdapter;
import com.FiveSGroup.TMS.StockTransfer.Product_StockTransfer;
import com.FiveSGroup.TMS.StockTransfer.StockTransferAdapter;

import java.util.ArrayList;

public class ListProduct extends AppCompatActivity {
    Button buttonBack, btnSynchronized, buttonBackListLetDown;
    ImageButton btnScanBarcode;
    RecyclerView listViewProduct;
    TextView tvTitle;
    ArrayList<ProductLetDown> letDowns;
    ArrayList<Product_StockTransfer> stockTransfers;
    ArrayList<Product_StockOut> Stockout;
    LetDownAdapter letDownAdapter;
    StockTransferAdapter stockTransferAdapter;
    StockoutAdapter StockoutListAdapter;

    String value1 = "",
            positonReceive = "",
            productCd = "",
            stock = "",
            expDate = "",
            expDate1 = "",
            let_down = "",
            stock_transfer = "",
            stock_out = "",
            ea_unit = "",
            ea_unit_position = "",
            stockinDate = "",
            fromLetDownSuggestionsActivity = "",
            lpn = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qrcode);


    }
}

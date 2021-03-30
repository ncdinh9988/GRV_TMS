package com.FiveSGroup.TMS.LetDown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class LetDownSuggestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewProductSuggetions;
    private LetDownProductSuggestionsAdapter adapter;
    private ArrayList<LetDownProductSuggest> letDownProductSuggests;
    private Button buttonBack,  buttonShowList;
    private ImageButton buttonScanBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_let_down_suggestions);

        init();
        getLetDownProductSuggestion();
    }

    private void getLetDownProductSuggestion() {
        DatabaseHelper.getInstance().deleteLetDownSuggest();
        int status = new CmnFns().synchronizeGetProductLetDownSuggest(CmnFns.readDataAdmin());
        if (status == -1){
            letDownProductSuggests.clear();
            adapter = new LetDownProductSuggestionsAdapter(this, R.layout.letdown_product_suggestions_item, letDownProductSuggests);
            listViewProductSuggetions.setAdapter(adapter);
        }else {
            letDownProductSuggests = DatabaseHelper.getInstance().getAllLetDownProductSuggest();
            adapter = new LetDownProductSuggestionsAdapter(this, R.layout.letdown_product_suggestions_item, letDownProductSuggests);
            listViewProductSuggetions.setAdapter(adapter);
        }
    }

    private void init() {
        listViewProductSuggetions = findViewById(R.id.listViewLetDownSuggestion);
        buttonBack = findViewById(R.id.buttonBack);
        buttonScanBarcode = findViewById(R.id.buttonScanBarCode);
        buttonShowList = findViewById(R.id.buttonShowList);
        buttonBack.setOnClickListener(this);
        buttonShowList.setOnClickListener(this);
        buttonScanBarcode.setOnClickListener(this);
        letDownProductSuggests = new ArrayList<>();
        //test list danh sách mẫu
        adapter = new LetDownProductSuggestionsAdapter(LetDownSuggestionsActivity.this, R.layout.letdown_product_suggestions_item, letDownProductSuggests);
        listViewProductSuggetions.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                Intent intentBack = new Intent(this, MainWareHouseActivity.class);
                startActivity(intentBack);
                finish();
                break;
            case R.id.buttonScanBarCode:
                Intent intentScanBarCode = new Intent(this, LetDownQrCodeActivity.class);
                startActivity(intentScanBarCode);
                break;
            case R.id.buttonShowList:
                Intent intentShowList = new Intent(this, LetDownActivity.class);
                intentShowList.putExtra("fromLetDownSuggestionsActivity", "1");
                startActivity(intentShowList);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
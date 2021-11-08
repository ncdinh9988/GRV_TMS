package com.FiveSGroup.TMS;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.MainMenu.MenuItemAdpater;
import com.FiveSGroup.TMS.MainMenu.MenuItemObject;
import com.FiveSGroup.TMS.MainMenu.Setting;
import com.FiveSGroup.TMS.MainMenu.SpaceItem;

import java.util.ArrayList;

public class MainShipper extends AppCompatActivity {

    RecyclerView rvCategory;
    ArrayList<MenuItemObject> arrItem;
    MenuItemShipperAdapter adpater;
    TextView tvSale, tvVersion;
    LinearLayout layout_name_sale ;
    ImageButton imgsetting ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ware_house);
        rvCategory = findViewById(R.id.rvCategory);
        tvSale = findViewById(R.id.tvSale);
        layout_name_sale = findViewById(R.id.layout_name_sale);
        layout_name_sale.setVisibility(View.GONE);
        tvVersion = findViewById(R.id.tvVersion);
        imgsetting = findViewById(R.id.imgsetting);
        String version;
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = pInfo.versionName;
            tvVersion.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        prepareData();
        if(CmnFns.isCheckAdmin()){
            tvSale.setText(CmnFns.readDataAdmin());
        }else if(CmnFns.isCheckAdmin()){
            tvSale.setText(CmnFns.readDataShipper());
        }

        imgsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);

            }
        });

        adpater = new MenuItemShipperAdapter(this, arrItem);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        rvCategory.addItemDecoration(new SpaceItem(spanCount, spacing, includeEdge));
        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setAdapter(adpater);
        adpater.notifyDataSetChanged();
    }

    private void prepareData(){
        arrItem = new ArrayList<>();
        String name[] = {"Kéo Container", "Giao Hàng" ,"Thêm Khách Hàng"
        };
        int images[] = {R.drawable.ic_lpn, R.drawable.ic_nhap_kho, R.drawable.ic_nhap_kho
        };
        String Lock_Wh_Adjustment = DatabaseHelper.getInstance().getParamByKey("LOCK_WH_Adjustment").getValue();
        for(int i = 0; i < name.length; i ++){
            MenuItemObject object = new MenuItemObject();
            object.setNameItem(name[i]);
            object.setImageItem(images[i]);
            if (Lock_Wh_Adjustment.equals("1")){
                if(i==9){

                }else{
                    arrItem.add(object);
                }
            }else{
                arrItem.add(object);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}

package com.FiveSGroup.TMS.TransferQR.Home;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.MainMenu.MenuItemObject;
import com.FiveSGroup.TMS.MainMenu.SpaceItem;
import com.FiveSGroup.TMS.PickList.NewWareHouseActivity;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class Home_PhanloaiHH extends AppCompatActivity {

    RecyclerView rvCategory;
    ArrayList<MenuItemObject> arrItem;
    Arr_Adapter_Transfer_Posting adpater;
    TextView tvSale, tvVersion;
    Button btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_phanloaihh);
        rvCategory = findViewById(R.id.rvCategory);
        tvSale = findViewById(R.id.tvSale);
        tvVersion = findViewById(R.id.tvVersion);
        btnback = findViewById(R.id.btnback);



        String version;
//        try {
//            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
//            version = pInfo.versionName;
//            tvVersion.setText("Version: " + version);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        prepareData();
        if(CmnFns.isCheckAdmin()){
            tvSale.setText(CmnFns.readDataAdmin());
        }else if(CmnFns.isCheckAdmin()){
            tvSale.setText(CmnFns.readDataShipper());
        }

        adpater = new Arr_Adapter_Transfer_Posting(this, arrItem);
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
        String name[] = {"Lấy Hàng", "Chuyển Mã"};
        int images[] = {R.drawable.ic_lpn, R.drawable.ic_nhap_kho};
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
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_PhanloaiHH.this, MainWareHouseActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}

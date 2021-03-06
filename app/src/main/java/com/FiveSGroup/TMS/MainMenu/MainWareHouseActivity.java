package com.FiveSGroup.TMS.MainMenu;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;

public class MainWareHouseActivity extends AppCompatActivity {

    RecyclerView rvCategory;
    ArrayList<MenuItemObject> arrItem;
    MenuItemAdpater adpater;
    TextView tvSale, tvVersion;
    ImageButton imgsetting ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ware_house);
        rvCategory = findViewById(R.id.rvCategory);
        tvSale = findViewById(R.id.tvSale);
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
                Intent intent = new Intent(getApplicationContext(),Setting.class);
                startActivity(intent);

            }
        });

        adpater = new MenuItemAdpater(this, arrItem);
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
        String name[] = {"LPN", "Nh???p Kho", "Put Away", "Let Down", "Chuy???n V??? Tr??", "Master Pick" ,"PickList",
                "Xu???t Kho", "Ki???m T???n", "Ch???nh Kho" , "G??? S???n Ph???m",
//                "Tr??? H??ng" ,
                "Xu???t H???y","Chuy???n ??VT"
               , "PO Return","Ph??n Lo???i HH" , "Ki???m ?????nh" ,"Ki???m Tra Xe" ,"OD"
//                ,"Repacking"
        };
        int images[] = {R.drawable.ic_lpn, R.drawable.ic_nhap_kho, R.drawable.ic_putaway, R.drawable.ic_letdown,
                R.drawable.ic_chuyen_vi_tri,  R.drawable.ic_master_pick , R.drawable.ic_picklist, R.drawable.ic_xuat_kho,
                R.drawable.ic_kiem_ton, R.drawable.ic_chinh_kho , R.drawable.ic_go_san_pham,
//                R.drawable.ic_tra_hang ,
                R.drawable.ic_xuat_kho, R.drawable.ic_go_san_pham
                , R.drawable.ic_chuyen_vi_tri ,R.drawable.ic_nhap_kho, R.drawable.ic_xuat_kho , R.drawable.ic_chinh_kho
                , R.drawable.ic_go_san_pham
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
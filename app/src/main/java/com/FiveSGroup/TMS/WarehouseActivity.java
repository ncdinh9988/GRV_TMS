package com.FiveSGroup.TMS;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.FiveSGroup.TMS.LetDown.LetDownQrCodeActivity;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.StockTransfer.Qrcode_StockTransfer;
import com.FiveSGroup.TMS.StockOut.Qrcode_Stock_Out;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Warehouse.Home_Ware;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WarehouseActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isLoaded = false;

    WebView webViewPickList;
    FrameLayout frameLayout;
    BottomNavigationView bottom_warehouse;
    LinearLayout layout_more;
    boolean isChooseMore = false;
    Animation animation;
    LinearLayout layout_putaway, layout_letdown, layout_export, layout_location;
    ImageView imgtransport, imgletdown, imgexport, imglocation;
    Menu menu;
    MenuItem itemMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        // webViewPickList = findViewById(R.id.webViewWarehouse);

        init();

        //webViewPickList.setVisibility(View.VISIBLE);
        // bottom_warehouse.setVisibility(View.VISIBLE);

        //frameLayout.setVisibility(View.VISIBLE);

        loadFragment(new Home_Ware());
        //  addEvents(urlPickList);
    }

    private void init(){
        frameLayout = findViewById(R.id.frame);
        bottom_warehouse = findViewById(R.id.bottom_warehouse);
        bottom_warehouse.setOnNavigationItemSelectedListener(navListener);
        layout_more = findViewById(R.id.layout_more);
        layout_putaway = findViewById(R.id.layout_put_away);
        layout_letdown = findViewById(R.id.layout_let_down);
        layout_export = findViewById(R.id.layout_export_ware);
        layout_location = findViewById(R.id.layout_location);
        imgtransport = findViewById(R.id.imgtransport);
        imgletdown = findViewById(R.id.imgletdown);
        imgexport = findViewById(R.id.imgexport);
        imglocation = findViewById(R.id.imglocation);
        menu = bottom_warehouse.getMenu();

        itemMore = menu.findItem(R.id.navigation_More);


        layout_putaway.setOnClickListener(this);
        layout_letdown.setOnClickListener(this);
        layout_export.setOnClickListener(this);
        layout_location.setOnClickListener(this);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                    Fragment selecFragment = null;


                    switch (menu.getItemId()) {
                        case R.id.navigation_warehouse:
                            layout_more.setVisibility(View.GONE);
                            itemMore.setIcon(R.drawable.ic_baseline_add_24);
                            isChooseMore = false;

                            selecFragment = new Home_Ware();
                            break;
                        case R.id.navigation_PO:
                            layout_more.setVisibility(View.GONE);
                            itemMore.setIcon(R.drawable.ic_baseline_add_24);

                            bottom_warehouse.setVisibility(View.GONE);
                            //selecFragment = new Home_QRcode();
                            Intent intent = new Intent(WarehouseActivity.this, HomeQRActivity.class);
                            startActivity(intent);
                            finish();
                            isChooseMore = false;

                            break;
                        case R.id.navigation_More:
                            if (isChooseMore == false) {
                                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.move_from_right);
                                layout_more.startAnimation(animation);
                                layout_more.setVisibility(View.VISIBLE);
                                menu.setIcon(R.drawable.ic_baseline_arrow_forward_ios_24);

                                isChooseMore = true;
                            } else {
                                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.move);
                                layout_more.startAnimation(animation);
                                layout_more.setVisibility(View.GONE);
                                menu.setIcon(R.drawable.ic_baseline_add_24);
                                noChoose();
                                isChooseMore = false;
                                noChoose();
                            }

                            break;


                    }
                    return loadFragment(selecFragment);

                }
            };

    private boolean loadFragment(Fragment fragment) {
        // switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_put_away:
                noChoose();
                imgtransport.setImageResource(R.drawable.put_away_choose);
                Intent intentt = new Intent(WarehouseActivity.this, Qrcode_PutAway.class);
                startActivity(intentt);

                break;
            case R.id.layout_let_down:
                noChoose();
                imgletdown.setImageResource(R.drawable.letdown_choose);
                Intent intent1 = new Intent(WarehouseActivity.this, LetDownQrCodeActivity.class);
                startActivity(intent1);
//                Intent intent1 = new Intent(WarehouseActivity.this, QrCodeLetDownActivity.class);
//                startActivity(intent1);

                break;
            case R.id.layout_export_ware:
                noChoose();
                imgexport.setImageResource(R.drawable.export_ware_choose);
                Intent intent2 = new Intent(WarehouseActivity.this, Qrcode_Stock_Out.class);
                startActivity(intent2);
                break;
            case R.id.layout_location:
                noChoose();
                imglocation.setImageResource(R.drawable.letdown_choose);
                Intent intent3 = new Intent(WarehouseActivity.this, Qrcode_StockTransfer.class);
                startActivity(intent3);

                break;
        }
    }
    private void noChoose(){
        imgtransport.setImageResource(R.drawable.transport);
        imgletdown.setImageResource(R.drawable.change_loction);
        imgexport.setImageResource(R.drawable.export_ware);
        imglocation.setImageResource(R.drawable.change_loction);
    }
}

package com.FiveSGroup.TMS.PickList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.Inventory.InventoryHome;
import com.FiveSGroup.TMS.LPN.LPNActivity;
import com.FiveSGroup.TMS.LetDown.LetDownSuggestionsActivity;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.StockOut.Home_Stockout;
import com.FiveSGroup.TMS.StockTransfer.Qrcode_StockTransfer;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Warehouse_Adjustment;
import com.FiveSGroup.TMS.global;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class NewWareHouseActivity extends AppCompatActivity implements View.OnClickListener {


    FloatingActionsMenu actionMenu;
    FloatingActionButton actionButton;
    FloatingActionButton floating_put_away, floating_letdown, floating_export_ware, floating_change_location, floating_PickList, floating_PO, floating_LPN, floating_Warehouse_Adj, floating_Inventory;
    WebView webViewPickList;
    Button btnBack;
    private ProgressBar progressBar;
    private Button btnScan, btnback, btnShow , btnlpn;
    String urlPickListt = "";
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ware_house);


        init();

        urlPickListt = DatabaseHelper.getInstance().getParamByKey("URL_PickByOrder").getValue();

       // urlPickListt = DatabaseHelper.getInstance().getParamByKey("URL_Stockin").getValue();;

        if(CmnFns.isNetworkAvailable()) {
            String urlPickList =  urlPickListt + "?USER_PICKLIST=" + CmnFns.readDataAdmin();
            addEvents(urlPickList);
        }else{
            LayoutInflater factory = LayoutInflater.from(this);
            View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
            final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 64);
            dialog.getWindow().setBackgroundDrawable(inset);
            dialog.setView(layout_cus);

            Button btnClose = layout_cus.findViewById(R.id.btnHuy);
            TextView textView = layout_cus.findViewById(R.id.tvText);


            textView.setText("Vui lòng bật kết nối mạng");
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        RefeshData();

    }


    private void init(){
        actionMenu = findViewById(R.id.menu_floating);
        actionButton = new FloatingActionButton(getBaseContext());
        floating_put_away = findViewById(R.id.floating_put_away);
        floating_letdown = findViewById(R.id.floating_letdown);
        floating_export_ware = findViewById(R.id.floating_export_ware);
        floating_change_location = findViewById(R.id.floating_change_location);
        floating_PickList = findViewById(R.id.floating_pickList);
        floating_PO = findViewById(R.id.floating_PO);
        floating_LPN = findViewById(R.id.floating_LPN);
        floating_Inventory = findViewById(R.id.floating_inventory);
        floating_Warehouse_Adj = findViewById(R.id.floating_warehouse_adjustment);
        btnScan = (Button) findViewById(R.id.btn1);
        btnback = findViewById(R.id.btnback);
        btnShow = findViewById(R.id.btnShow);
        btnlpn = findViewById(R.id.btnlpn);
        refreshLayout = findViewById(R.id.swipeRefesh);
        btnScan.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnlpn.setOnClickListener(this);

        floating_Warehouse_Adj.setOnClickListener(this);
        floating_Inventory.setOnClickListener(this);
        floating_put_away.setOnClickListener(this);
        floating_letdown.setOnClickListener(this);
        floating_export_ware.setOnClickListener(this);
        floating_change_location.setOnClickListener(this);
        floating_PickList.setOnClickListener(this);
        floating_PO.setOnClickListener(this);
        floating_LPN.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnback.setOnClickListener(this);

        webViewPickList = findViewById(R.id.wvContent_warehome);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void RefeshData() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String urlPickList =  urlPickListt + "?USER_PICKLIST=" +CmnFns.readDataAdmin();
                addEvents(urlPickList);
                refreshLayout.setRefreshing(false);

            }
        });

    }

    private void addEvents(String url) {
        //String url = "https://voucherpg.com/Phase2/LOGINv2.aspx";

        if (CmnFns.isNetworkAvailable()) {
            webViewPickList.getSettings().setJavaScriptEnabled(true);
            webViewPickList.getSettings().setUseWideViewPort(true);
            webViewPickList.getSettings().setLoadWithOverviewMode(true);
            webViewPickList.canGoBack();
            webViewPickList.getSettings().setSupportZoom(true);
            webViewPickList.canGoForward();
            webViewPickList.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webViewPickList.setInitialScale(1);
            webViewPickList.loadUrl(url);
            webViewPickList.setWebViewClient(new MyBrowser());
            webViewPickList.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    //Required functionality here
                    return super.onJsAlert(view, url, message, result);
                }


                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);

                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        try {
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                        }

                    }
                }
            });
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest wRequest) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(wRequest.getUrl().toString());
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (url.contains("WarehousePickListOrderForAppItem.aspx?PickListOrderCD")) {

                String chuoi[] = url.split("=");
                String code = chuoi[1];
                String chuoi2[] = code.split("&");
                String code2 = chuoi2[0];
                global.setPickListCD(code2);
                // Toast.makeText(HomeQRActivity.this, code+"", Toast.LENGTH_SHORT).show();

                btnScan.setVisibility(View.VISIBLE);
                btnShow.setVisibility(View.VISIBLE);
                btnlpn.setVisibility(View.VISIBLE);
                btnback.setVisibility(View.GONE);
                actionMenu.setVisibility(View.GONE);
                SharedPreferences sharedPreferences = getSharedPreferences("whpicklist", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("picklist", code);
                editor.apply();

            } else {
                btnShow.setVisibility(View.GONE);
                btnScan.setVisibility(View.GONE);
                btnlpn.setVisibility(View.GONE);
                btnback.setVisibility(View.VISIBLE);
                actionMenu.setVisibility(View.GONE);
                SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                SharedPreferences sharedPreferences = getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
            }
            progressBar.setVisibility(View.GONE);

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_LPN:
                Intent intentLPN = new Intent(NewWareHouseActivity.this, LPNActivity.class);
                startActivity(intentLPN);
                break;
            case R.id.floating_pickList:

                break;
            case R.id.floating_warehouse_adjustment:

                Intent intent_ajm = new Intent(NewWareHouseActivity.this, Warehouse_Adjustment.class);
                startActivity(intent_ajm);
                // finish();
                break;
            case R.id.floating_inventory:

                Intent intent_ivt = new Intent(NewWareHouseActivity.this, InventoryHome.class);
                startActivity(intent_ivt);
                // finish();
                break;
            case R.id.floating_PO:

                Intent intent = new Intent(NewWareHouseActivity.this, HomeQRActivity.class);
                startActivity(intent);
               // finish();
                break;
            case R.id.floating_put_away:
                Intent intentt = new Intent(NewWareHouseActivity.this, Qrcode_PutAway.class);
                startActivity(intentt);
                //finish();
                break;
            case R.id.floating_letdown:
                Intent intent1 = new Intent(NewWareHouseActivity.this, LetDownSuggestionsActivity.class);
                startActivity(intent1);
                //finish();
                break;
            case R.id.floating_export_ware:
                Intent intent2 = new Intent(NewWareHouseActivity.this, Home_Stockout.class);

                startActivity(intent2);
               // finish();
                break;
            case R.id.floating_change_location:
                Intent intent3 = new Intent(NewWareHouseActivity.this, Qrcode_StockTransfer.class);
                startActivity(intent3);
               // finish();
                break;
            case R.id.btn1:
                Intent intent4 = new Intent(NewWareHouseActivity.this, PickListQrCode.class);
                startActivity(intent4);
                break;

            case R.id.btnShow:
                Intent intent5 = new Intent(NewWareHouseActivity.this, ListPickList.class);
                startActivity(intent5);
                break;
            case R.id.btnlpn:
                Intent intent6 = new Intent(NewWareHouseActivity.this, LPNActivity.class);
                startActivity(intent6);
                break;
            case R.id.btnback:
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = this.
                getSharedPreferences("value", Activity.MODE_PRIVATE);
        String s = prefs.getString("lastUrl", "");
        if (!s.equals("")) {
            webViewPickList.loadUrl(s);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = this.
                getSharedPreferences("value", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("lastUrl", webViewPickList.getUrl());
        edit.commit();   // can use edit.apply() but in this case commit is better
    }
}
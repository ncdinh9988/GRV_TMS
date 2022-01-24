package com.FiveSGroup.TMS.ListOD;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
import com.FiveSGroup.TMS.ValueEventbus;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.Wv_ShowResultQrode;
import com.FiveSGroup.TMS.global;

import org.greenrobot.eventbus.EventBus;

public class HomeOD extends AppCompatActivity {
    private WebView mWebview;
    private Button btn1, btnLpn, btn3, btnback, btnOB , btnchuyendvt;
    LinearLayout layout;
    private ProgressDialog progressSyncProgram;
    String value1 = "", value2 = "";
    SharedPreferences sharedPref;
    String urlOD = "" , valuewarehouse = "";
    ValueEventbus eventbus;
    static String value = "";
    String value3 = "";
    private TextView tvTitle;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_to_compose);
        progressBar = findViewById(R.id.progressbar);
        refreshLayout = findViewById(R.id.swipeRefesh);

        progressBar.setMax(100);


//
//        Log.e("new3","là : " + value3);
//        Log.e("new4","là : " + value4);

        mWebview = (WebView) findViewById(R.id.webview);
        btn1 = (Button) findViewById(R.id.btn1);
        btnchuyendvt = (Button) findViewById(R.id.btnchuyendvt) ;
        btnback = findViewById(R.id.btnback);
        tvTitle = findViewById(R.id.tvTitle);
        btnOB = findViewById(R.id.btnOB);
        btnLpn = findViewById(R.id.btnlpn);
        layout = findViewById(R.id.layout);
        urlOD = DatabaseHelper.getInstance().getParamByKey("URL_OutboundDelivery").getValue();

        btnchuyendvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeOD.this, TransferUnitQrcode.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebview.reload();
            }
        });

        btnLpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeOD.this, LPNOD.class);
                intent.putExtra("lpn_od","");
                startActivity(intent);
                EventBus.getDefault().postSticky(new CheckEventbus());

            }
        });


        btnOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeOD.this, OutboundOD.class);
                intent.putExtra("outbound_od","");
                startActivity(intent);
                EventBus.getDefault().postSticky(new CheckEventbus());

            }
        });


        addEvents(urlOD+"?USER_CODE="+CmnFns.readDataAdmin());
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(HomeOD.this, MainWareHouseActivity.class);
                startActivity(intenttt);
                finish();
            }
        });

        RefeshData();



    }

    private void RefeshData() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addEvents(urlOD+"?USER_CODE="+CmnFns.readDataAdmin());
                refreshLayout.setRefreshing(false);

            }
        });

    }
    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(HomeOD.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(HomeOD.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);
        dialog.setCancelable(false);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);
        btnClose.setText("OK");


        textView.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addEvents(String url) {
        if (CmnFns.isNetworkAvailable()) {
            mWebview.addJavascriptInterface(new HomeOD.JavaScriptInterface(HomeOD.this), "Android");
            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.canGoBack();
            mWebview.getSettings().setSupportZoom(true);
            mWebview.canGoForward();
            mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebview.setInitialScale(1);
            mWebview.loadUrl(url);

            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    progressBar.setVisibility(View.VISIBLE);
                }
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed(); // Ignore SSL certificate errors
                }

                public void onPageFinished(WebView view, String url) {
                    SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                    value3 = sharedPref.getString("btn1", "");


                    //Here you want to use .loadUrl again
                    //on the webview object and pass in
                    //"javascript:<your javaScript function"

                    // mWebview.loadUrl("javascript:ResusltBarCode('"+value3+"')");
                    //mWebview.loadUrl("javascript:ResusltBarCode('"+value3+"','"+value4+"')");
                    ;//if passing in an object. Mapping may need to take place
                    Log.e("urljavascript", "đã chạy dc");

                    //Toast.makeText(HomeQRActivity.this, url+"", Toast.LENGTH_LONG).show();
                    if (url.contains("OutboundDeliveryListForAppItem") && url.contains("POSITION_CD")) {
                        try {
                            String chuoin[] = url.split("=");
                            String coden = chuoin[2];
                            String chuoi0[] = url.split("=");
                            String code0 = chuoi0[3];
                            if(code0 != null && code0 != ""){
                                String chuoi[] = url.split("=");
                                String code = chuoi[2];
                                String chuoi2[] = code.split("&");
                                String obdl = chuoi2[0];
                                String chuoi3[] = code.split("&");
                                String pick = chuoi3[1];
                                String position_cd = "";
                                global.setOutbound_Delivery_CD(obdl);
                                if(pick.contains("POSITION_CD")){
                                    String chuoi4[] = url.split("=");
                                    position_cd = chuoi4[3];
                                }
                                if(position_cd != null && position_cd != ""){
                                    global.setPosition_CD(position_cd);

                                    String result2 = new CmnFns().Check_OD_Have_LPN(obdl);
                                    if(result2.equals("1")){
                                        Intent intenttt = new Intent(HomeOD.this, ListPickPositionOD.class);
                                        DatabaseHelper.getInstance().deleteProduct_OD();
                                        String result = new CmnFns().Suggest_Product_For_OD_With_Position(CmnFns.readDataAdmin(), obdl, position_cd);
                                        intenttt.putExtra("POSITION_CD" , position_cd);
                                        startActivity(intenttt);
                                    }else{
                                        ShowSuccessMessage(result2);
                                    }


                                }
                            }
                            tvTitle.setText("Thông Tin Chi Tiết OD");
                            btn1.setVisibility(View.GONE);
                            btnOB.setVisibility(View.VISIBLE);
                            btnLpn.setVisibility(View.VISIBLE);
                            btnchuyendvt.setVisibility(View.VISIBLE);
                            btnback.setVisibility(View.GONE);

                        }catch (Exception e){
                            Log.d("LOG Eror",e.toString());
                        }

                    }else if(url.contains("OutboundDeliveryListForAppItem")) {

                        String chuoi[] = url.split("=");
                        String code = chuoi[2];
                        String chuoi2[] = code.split("&");
                        String obdl = chuoi2[0];
                        global.setOutbound_Delivery_CD(obdl);
                        global.setLinkOD(url);

                        tvTitle.setText("Thông Tin Chi Tiết OD");
                        btn1.setVisibility(View.GONE);
                        btnOB.setVisibility(View.VISIBLE);
                        btnLpn.setVisibility(View.VISIBLE);
                        btnchuyendvt.setVisibility(View.VISIBLE);
                        btnback.setVisibility(View.GONE);

                        SharedPreferences sharedPreferences = getSharedPreferences("linkOD", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("linkOD", url);
                        editor.apply();

                    }
                    else {
                        btnOB.setVisibility(View.GONE);
                        btn1.setVisibility(View.VISIBLE);
                        btnLpn.setVisibility(View.GONE);
                        btnchuyendvt.setVisibility(View.GONE);
                        btnback.setVisibility(View.VISIBLE);
                        SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
//                        SharedPreferences sharedPreferences = getSharedPreferences("masterpick", Context.MODE_PRIVATE);
//                        sharedPreferences.edit().clear().apply();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            });

            mWebview.setWebChromeClient(new WebChromeClient() {
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

    public class JavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = this.getSharedPreferences("linkOD", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = this.
                getSharedPreferences("linkOD", Activity.MODE_PRIVATE);
        String s = prefs.getString("linkOD", "");
        if (!s.equals("")) {
            mWebview.loadUrl(s);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        SharedPreferences prefs = this.
//                getSharedPreferences("name_master_pick", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putString("lastUrl", mWebview.getUrl());
//        edit.commit();   // can use edit.apply() but in this case commit is better
    }

    @Override
    public void onBackPressed() {

    }
}


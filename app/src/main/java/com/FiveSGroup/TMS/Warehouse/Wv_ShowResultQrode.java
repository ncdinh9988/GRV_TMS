package com.FiveSGroup.TMS.Warehouse;

import android.app.AlertDialog;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LetDown.LetDownQrCodeActivity;
import com.FiveSGroup.TMS.PutAway.Qrcode_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.StockOut.Qrcode_Stock_Out;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;

public class Wv_ShowResultQrode  extends AppCompatActivity {

    WebView webViewPickList;
    private ProgressBar progressBar;
    String urlPickListt = "";
    int result_WSO  ;
    String type_WSO = "";
    int result_WPA  ;
    String type_WPA = "";
    int result_WOI  ;
    String type_WOI = "";
    int result_WLD  ;
    String type_WLD = "";
//    int result_WPL  ;
//    String type_WPL = "";

    String pageBack = "";
    Button buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wv_showresult_update_qrcode);

        webViewPickList = findViewById(R.id.wvContent_warehome);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        buttonBack = findViewById(R.id.buttonBack);

        final Intent intent = getIntent();
        result_WSO = intent.getIntExtra("result_WSO",0);
        type_WSO = intent.getStringExtra("type_WSO");
        result_WPA = intent.getIntExtra("result_WPA",0);
        type_WPA = intent.getStringExtra("type_WPA");
        result_WOI = intent.getIntExtra("result_WOI",0);
        type_WOI = intent.getStringExtra("type_WOI");
        result_WLD = intent.getIntExtra("result_WLD",0);
        type_WLD = intent.getStringExtra("type_WLD");
//        result_WPL = intent.getIntExtra("result_WPL",0);
//        type_WPL = intent.getStringExtra("type_WPL");

        urlPickListt = DatabaseHelper.getInstance().getParamByKey("URL_PreviewStockDetailForApp").getValue();
        if (CmnFns.isNetworkAvailable()) {
            if(type_WSO!=null && type_WSO.equals("WSO")){
                String urlPickList =  urlPickListt + "?CD=" + result_WSO + "&TYPE=" + type_WSO;
                addEvents(urlPickList);
                pageBack = type_WSO;
            }else if(type_WPA!=null && type_WPA.equals("WPA")){
                String urlPickList =  urlPickListt + "?CD=" + result_WPA + "&TYPE=" + type_WPA;
                addEvents(urlPickList);
                pageBack = type_WPA;
            }else if(type_WOI!=null && type_WOI.equals("WOI")){
                String urlPickList =  urlPickListt + "?CD=" + result_WOI + "&TYPE=" + type_WOI;
                addEvents(urlPickList);
                pageBack = type_WOI;
            }else if(type_WLD!=null && type_WLD.equals("WLD")){
                String urlPickList =  urlPickListt + "?CD=" + result_WLD + "&TYPE=" + type_WLD;
                addEvents(urlPickList);
                pageBack = type_WLD;
            }
//            else if(type_WPL!=null && type_WPL.equals("WPL")){
//                String urlPickList =  urlPickListt + "?CD=" + result_WPL + "&TYPE=" + type_WPL;
//                addEvents(urlPickList);
//                pageBack = type_WPL;
//            }
        }else{
            LayoutInflater factory = LayoutInflater.from(getApplicationContext());
            View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
            final AlertDialog dialog = new AlertDialog.Builder(getApplicationContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageBack){
                    case "WSO":
                        Intent intentWSO = new Intent(Wv_ShowResultQrode.this, Qrcode_Stock_Out.class);
                        startActivity(intentWSO);
                        finish();
                        break;
                    case "WPA":
                        Intent intentWPA = new Intent(Wv_ShowResultQrode.this, Qrcode_PutAway.class);
                        startActivity(intentWPA);
                        finish();
                        break;
                    case "WOI":
                        Intent intentWOI = new Intent(Wv_ShowResultQrode.this, ListStockTransfer.class);
                        intentWOI.putExtra("turn_off_alert", "123");
                        startActivity(intentWOI);
                        finish();
                        break;
                    case "WLD":
                        Intent intentWLD= new Intent(Wv_ShowResultQrode.this, LetDownQrCodeActivity.class);
                        startActivity(intentWLD);
                        finish();
                        break;
//                    case "WPL":
//                        Intent intentWPL= new Intent(Wv_ShowResultQrode.this, PickListQrCode.class);
//                        startActivity(intentWPL);
//                        finish();
//                        break;
                    default:
                        finish();
                        break;
                }
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

            progressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {

    }

}

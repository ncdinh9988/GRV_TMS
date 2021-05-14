package com.FiveSGroup.TMS.Warehouse;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.FiveSGroup.TMS.AddCustomerFragment.FCustomerAddNewEdit;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

public class Home_Ware extends Fragment {
    WebView webViewPickList;
    private ProgressBar progressBar;
    String urlPickListt = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_ware_house,container,false);
        webViewPickList = view.findViewById(R.id.wvContent_warehome);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        urlPickListt = DatabaseHelper.getInstance().getParamByKey("URL_PickList").getValue();
        if(CmnFns.isNetworkAvailable()) {
            String urlPickList =  urlPickListt + "?USER_PICKLIST=" + CmnFns.readDataAdmin();
            addEvents(urlPickList);
        }else{
            LayoutInflater factory = LayoutInflater.from(getActivity());
            View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

        return view;

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
}

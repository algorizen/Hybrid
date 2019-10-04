package com.example.jsbridge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText editText;
    private Button showBtn;
    private Button refreshBtn;
    private MainActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        editText = findViewById(R.id.editText);
        showBtn = findViewById(R.id.showBtn);
        refreshBtn = findViewById(R.id.refreshBtn);

        webView.loadUrl("http://192.168.1.9:8080?timestamp=" + new Date().getTime());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new NativeBridge(this), "NativeBridge");

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = editText.getText().toString();
                self.showWebDialog(inputValue);
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://192.168.1.9:8080?timestamp=" + new Date().getTime());
            }
        });
    }

    private void showWebDialog(String text) {
        String jsCode = String.format("window.showWebDialog('%s')", text);
        webView.evaluateJavascript(jsCode, null);
    }

    class NativeBridge {
        private Context ctx;
        NativeBridge(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showNativeDialog(String text) {
            new AlertDialog.Builder(ctx).setMessage(text).create().show();
        }
    }
}

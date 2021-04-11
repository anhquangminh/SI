package com.foxconn.main;

import com.bgxt.gallerydemo.R;
import com.bgxt.gallerydemo.R.layout;
import com.bgxt.gallerydemo.R.menu;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TableauActivity extends Activity {

	private WebView showWV;
	
	@SuppressLint("SetJavaScriptEnabled") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tableau);
		
		showWV = (WebView) findViewById(R.id.showWV);
		showWV.getSettings().setJavaScriptEnabled(true);
		// 设置是否可缩放
		showWV.getSettings().setBuiltInZoomControls(true);
		showWV.getSettings().setLoadWithOverviewMode(true);
		showWV.requestFocus();
		showWV.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}			
		});
		showWV.loadUrl("http://10.120.213.235/views/VI__1/VI_?:embed=y&:display_count=no");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && showWV.canGoBack()) {       
        	showWV.goBack();       
            return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }
}

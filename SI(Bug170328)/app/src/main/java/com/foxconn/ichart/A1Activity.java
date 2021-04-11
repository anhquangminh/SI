package com.foxconn.ichart;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bgxt.gallerydemo.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;

public class A1Activity extends Activity {
	
	WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.a1);
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);  
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setSupportZoom(true);
		// 设置是否可缩放
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.requestFocus();
		wv.loadUrl("file:///android_asset/mianji_chart.html");
		
	}
	public void backBtn(View view){
		this.finish();
	}
	
	//模拟获取远程数据 这里可以是联网到服务端获取数据
	private String getRemoteData(){
		 try {  
	            JSONObject object1 = new JSONObject();  
	            object1.put("name", "北京");  
	            object1.put("color", "#1f7e92");  
	            Random random = new Random();
	            //js中的数组类型要使用JSONArray对象
	            JSONArray jadata= new JSONArray();  
	            for(int i=0;i<12;i++){
	            	jadata.put(random.nextInt(40));
	            }
	            object1.put("value", jadata);    
	            JSONArray jsonArray = new JSONArray();  
	            jsonArray.put(object1);  
	            return jsonArray.toString();  
	        } catch (JSONException e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	}
	
	public void updateBtn(View view){
		
		wv.loadUrl("javascript:setContentInfo('"+getRemoteData()+"')");  
	}
}

package com.foxconn.ichart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bgxt.gallerydemo.*;
import com.foxconn.webservice.DBUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.EditText;

public class A2Activity extends Activity {
	
	private WebView wv;
	private EditText SN;
	private EditText startTime;
	private EditText endTime;
	private String strStartTime;
	private String strEndTime;
	private String strSN;
	private String strGroupValue;
	private DBUtil dbUtil;
      
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.a1);
				
		dbUtil = new DBUtil();
		Intent intent = this.getIntent();
		strSN = intent.getStringExtra("SN");
		strGroupValue = intent.getStringExtra("groupValue");
		initControls();
		
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);  
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setSupportZoom(true);
		// 设置是否可缩放
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.requestFocus();
		wv.loadUrl("file:///android_asset/zhuzhuang_chart.html");
	}
	public void backBtn(View view){
		this.finish();
	}
	
	//初始化控件，获取数据
	public void initControls(){
		
		SN = (EditText) findViewById(R.id.txtSN);
		startTime = (EditText) findViewById(R.id.txtStartTime);
		endTime = (EditText) findViewById(R.id.txtEndTime);
		
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());     
		String date = sDateFormat.format(new java.util.Date());
		
		startTime.setText(date);
		endTime.setText(date);
		
		strStartTime = startTime.getText().toString().trim();
		strEndTime = endTime.getText().toString().trim();
		SN.setText(strSN);
		
	}
	//模拟获取远程数据这里可以是联网到服务端获取数据
	private String getRemoteData(){
		 try { 			 
				//连接Webservice查询数据库，获取作业员工号权限信息
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				strSN = "2189009649";strStartTime="2015/07/08 00:00:00";strEndTime="2015/07/09 00:00:00";
				//String strData = strSN + "<>" + strStartTime +"<>"+ strEndTime +"<>"+ strGroupValue;
				//"2189009649", "2015/07/08 00:00:00", "2015/07/09 00:00:00"
				String strData = strSN + "@" + strStartTime +"@"+ strEndTime;
				list = dbUtil.VIReNum(strData);

				if(list == null || list.isEmpty()){
					return null;
				}
				JSONObject object1 = new JSONObject();
				JSONObject object2 = new JSONObject();
				JSONObject object3 = new JSONObject();
				JSONObject object4 = new JSONObject();
				JSONObject object5 = new JSONObject();
				
				switch(list.size()){			
				case 1://有一条数据的情况
					object1.put("name", "NULL");  
					object1.put("color", "#b5bcc5");  
					object1.put("value", 0);  
					 
					object2.put("name", "NULL");  
					object2.put("color", "#b5bcc5");  
					object2.put("value", 0);  
					  
					object3.put("name", "NULL");  
					object3.put("color", "#b5bcc5");  
					object3.put("value", 0);  
					  
					object4.put("name", "NULL");  
					object4.put("color", "#b5bcc5");  
					object4.put("value", 0);  
					 
					object5.put("name", "NULL");  
					object5.put("color", "#b5bcc5");  
					object5.put("value", 0);
					break;
				case 2:
					object1.put("name", list.get(1).get("CODE2").toString());  
					object1.put("color", "#b5bcc5");  
					object1.put("value", Integer.parseInt(list.get(1).get("COUNT").toString()));  
					 
					object2.put("name", "NULL");  
					object2.put("color", "#b5bcc5");  
					object2.put("value", 0);  
					  
					object3.put("name", "NULL");  
					object3.put("color", "#b5bcc5");  
					object3.put("value", 0);  
					  
					object4.put("name", "NULL");  
					object4.put("color", "#b5bcc5");  
					object4.put("value", 0);  
					 
					object5.put("name", "NULL");  
					object5.put("color", "#b5bcc5");  
					object5.put("value", 0);
					break;
				case 3:
					object1.put("name", list.get(1).get("CODE2").toString());  
					object1.put("color", "#b5bcc5");  
					object1.put("value", Integer.parseInt(list.get(1).get("COUNT").toString()));  
					 
					object2.put("name", list.get(2).get("CODE2").toString());  
					object2.put("color", "#b5bcc5");  
					object2.put("value", Integer.parseInt(list.get(2).get("COUNT").toString()));  
					  
					object3.put("name", "NULL");  
					object3.put("color", "#b5bcc5");  
					object3.put("value", 0);  
					  
					object4.put("name", "NULL");  
					object4.put("color", "#b5bcc5");  
					object4.put("value", 0);  
					 
					object5.put("name", "NULL");  
					object5.put("color", "#b5bcc5");  
					object5.put("value", 0);
					break;
				case 4:
					object1.put("name", list.get(1).get("CODE2").toString());  
					object1.put("color", "#b5bcc5");  
					object1.put("value", Integer.parseInt(list.get(1).get("COUNT").toString()));  
					 
					object2.put("name", list.get(2).get("CODE2").toString());  
					object2.put("color", "#b5bcc5");  
					object2.put("value", Integer.parseInt(list.get(2).get("COUNT").toString()));  
					  
					object3.put("name", list.get(3).get("CODE2").toString());  
					object3.put("color", "#b5bcc5");  
					object3.put("value", Integer.parseInt(list.get(3).get("COUNT").toString()));  
					  
					object4.put("name", "NULL");  
					object4.put("color", "#b5bcc5");  
					object4.put("value", 0);  
					 
					object5.put("name", "NULL");  
					object5.put("color", "#b5bcc5");  
					object5.put("value", 0);
					break;
				case 5:
					object1.put("name", list.get(1).get("CODE2").toString());  
					object1.put("color", "#b5bcc5");  
					object1.put("value", Integer.parseInt(list.get(1).get("COUNT").toString()));  
					 
					object2.put("name", list.get(2).get("CODE2").toString());  
					object2.put("color", "#b5bcc5");  
					object2.put("value", Integer.parseInt(list.get(2).get("COUNT").toString()));  
					  
					object3.put("name", list.get(3).get("CODE2").toString());  
					object3.put("color", "#b5bcc5");  
					object3.put("value", Integer.parseInt(list.get(3).get("COUNT").toString()));  
					  
					object4.put("name", list.get(4).get("CODE2").toString());  
					object4.put("color", "#b5bcc5");  
					object4.put("value", Integer.parseInt(list.get(4).get("COUNT").toString()));  
					 
					object5.put("name", "NULL");  
					object5.put("color", "#b5bcc5");  
					object5.put("value", 0);
					break;
				case 6:
					object1.put("name", list.get(1).get("CODE2").toString());  
					object1.put("color", "#b5bcc5");  
					object1.put("value", Integer.parseInt(list.get(1).get("COUNT").toString()));  
					 
					object2.put("name", list.get(2).get("CODE2").toString());  
					object2.put("color", "#b5bcc5");  
					object2.put("value", Integer.parseInt(list.get(2).get("COUNT").toString()));  
					  
					object3.put("name", list.get(3).get("CODE2").toString());  
					object3.put("color", "#b5bcc5");  
					object3.put("value", Integer.parseInt(list.get(3).get("COUNT").toString()));  
					  
					object4.put("name", list.get(4).get("CODE2").toString());  
					object4.put("color", "#b5bcc5");  
					object4.put("value", Integer.parseInt(list.get(4).get("COUNT").toString()));  
					 
					object5.put("name", list.get(4).get("CODE2").toString());  
					object5.put("color", "#b5bcc5");  
					object5.put("value", Integer.parseInt(list.get(4).get("COUNT").toString()));
					break;
				default:					
					break;
				}
			 	  	           	            	            	            
	            JSONArray jsonArray = new JSONArray();  
	            jsonArray.put(object1);  
	            jsonArray.put(object2);  
	            jsonArray.put(object3);  
	            jsonArray.put(object4);  
	            jsonArray.put(object5);  
  
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

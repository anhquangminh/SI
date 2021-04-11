package com.foxconn.main;

import com.bgxt.gallerydemo.R;
import com.foxconn.main.MainActivity.passBtnListener;
import com.foxconn.updatesoft.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingActivity extends Activity{

	public ImageView exit; 
	public ImageView updatesoft; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_view);
		
		exit = (ImageView)this.findViewById(R.id.exit_img);
		exit.setOnClickListener(new exitListener());
		
		updatesoft = (ImageView)this.findViewById(R.id.update_img);
		updatesoft.setOnClickListener(new updatesoftListener());
	}

	public void backBtn(View view){
		//Intent intent = new Intent(SettingActivity.this,MainActivity.class );
		//SettingActivity.this.startActivity(intent);
		//SettingActivity.this.finish();
		this.finish();
	}
	public class exitListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			
			new AlertDialog.Builder(SettingActivity.this)
			.setIcon(android.R.drawable.btn_dialog)
			.setTitle("温馨提示")
			.setMessage("您确定要退出么？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						
							dialog.dismiss();
							SettingActivity.this.finish();
						}
					})
			.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							
							dialog.dismiss();
						}
					}).show();// show很关键
		}		
	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class updatesoftListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			
			UpdateManager manager = new UpdateManager(SettingActivity.this);
			// 检查更新
			manager.checkUpdate();
		}		
	}
}

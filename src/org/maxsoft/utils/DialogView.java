package org.maxsoft.utils;

import org.maxsoft.pm.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.provider.Settings;

public class DialogView {
	/**
	 * @function 到系统设置界面的对话框
	 * @param clazz
	 */
	public static void to_setting_dialog(final Activity clazz){
		Builder dialog = new AlertDialog.Builder(clazz);
		dialog.setTitle(R.string.quit_title);
		dialog.setIcon(R.drawable.dialogicon);
		dialog.setMessage(R.string.tip_open_network);
		dialog.setPositiveButton(R.string.confirm, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);  
					clazz.startActivity(intent);
				}
			});
		dialog.create();        
		dialog.show();
	}
	
	/**
	 * @function 退出程序的对话框
	 * @param clazz
	 */
	public static void quit_app_dialog(Activity clazz){
		AlertDialog.Builder builder = new Builder(clazz);  
        builder.setMessage(R.string.quit_tip);  
        builder.setIcon(R.drawable.dialogicon);
        builder.setTitle(R.string.quit_title);  
        builder.setPositiveButton(R.string.confirm, 
        new android.content.DialogInterface.OnClickListener() {  
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();  
				android.os.Process.killProcess(android.os.Process.myPid());  
			}  
        });  
        builder.setNegativeButton(R.string.cancle,  
        new android.content.DialogInterface.OnClickListener() {  
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();  
			}  
        });  
        
        builder.create().show();  
	}
}

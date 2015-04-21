package org.maxsoft.pm.activity.base;

import org.maxsoft.utils.DialogView;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @function 通用基类
 * @author MaxSoft
 * @date 2015-03-14
 * @version 1.0.0
 */
public class ActivityBase extends ActionBarActivity{
	@Override  
	public boolean dispatchTouchEvent(MotionEvent ev) {  
	    if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
	        View v = getCurrentFocus();  
	        if (isShouldHideInput(v, ev)) {  
	            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	            if (imm != null) {  
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
	            }  
	        }  
	        return super.dispatchTouchEvent(ev);  
	    } else if(ev.getAction() == MotionEvent.BUTTON_BACK) {
	    	Toast.makeText(this, "C", Toast.LENGTH_SHORT).show();
	    }
	    // 必不可少，否则所有的组件都不会有TouchEvent了  
	    if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev);  
	}  
	
	/**
	 * @function 是否要隐藏软键盘
	 * @param v 输入框View
	 * @param event 当前的触屏事件
	 * @return Boolean值  (是否隐藏软件)
	 */
	public  boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null && (v instanceof EditText)) {  
	        int[] leftTop = { 0, 0 };  
	        //获取输入框当前的location位置  
	        v.getLocationInWindow(leftTop);  
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX()>left && event.getX()<right && event.getY()>top && event.getY()<bottom){  
	            // 点击的是输入框区域，保留点击EditText的事件  
	            return false;  
	        } else {  
	            return true;  
	        }  
	    }  
	    return false;  
	} 
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            DialogView.quit_app_dialog(this); 
            return true;  
        }  
        return true;  
    }  
}

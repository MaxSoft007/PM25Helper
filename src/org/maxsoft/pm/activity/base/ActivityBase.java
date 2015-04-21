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
 * @function ͨ�û���
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
	    // �ز����٣��������е������������TouchEvent��  
	    if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev);  
	}  
	
	/**
	 * @function �Ƿ�Ҫ���������
	 * @param v �����View
	 * @param event ��ǰ�Ĵ����¼�
	 * @return Booleanֵ  (�Ƿ��������)
	 */
	public  boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null && (v instanceof EditText)) {  
	        int[] leftTop = { 0, 0 };  
	        //��ȡ�����ǰ��locationλ��  
	        v.getLocationInWindow(leftTop);  
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX()>left && event.getX()<right && event.getY()>top && event.getY()<bottom){  
	            // ���������������򣬱������EditText���¼�  
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

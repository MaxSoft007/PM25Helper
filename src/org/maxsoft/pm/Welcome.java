package org.maxsoft.pm;

import org.maxsoft.update.model.UpdateManager;
import org.maxsoft.utils.DialogView;
import org.maxsoft.utils.PhoneStatusUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
/**
 * @function WelcomeActivity
 * @author MaxSoft
 * @date 2015-03-14
 * @version 1.0.0
 */
public class Welcome extends Activity{
	private Button startBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Welcome.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		boolean openNetwork = PhoneStatusUtils.isOpenNetwork(getWindow().getContext());
		if(!openNetwork){
			Toast.makeText(getWindow().getContext(), getWindow().getContext().getString(R.string.open_network_tip), Toast.LENGTH_LONG).show();
		}
		UpdateManager manager = new UpdateManager(Welcome.this);
		manager.checkUpdate();
	}
	
	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				if(!PhoneStatusUtils.isOpenNetwork(v.getContext())){
					DialogView.to_setting_dialog(Welcome.this);
				}else{
					Intent intent = new Intent( Welcome.this, PM25Helper.class);
					Welcome.this.startActivity(intent);
					Welcome.this.finish();
				}
			}
		}
	};
}

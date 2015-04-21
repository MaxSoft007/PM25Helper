package org.maxsoft.pm;


import org.maxsoft.pm.activity.base.ActivityBase;
import org.maxsoft.pm.asynctask.UpdatePM25InfoAsyncTask;
import org.maxsoft.update.ParseXmlService;
import org.maxsoft.utils.Constant;
import org.maxsoft.utils.DialogView;
import org.maxsoft.utils.PhoneStatusUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @function PM25Activity
 * @author MaxSoft
 * @date 2015-03-14
 * @version 1.0.0
 */
public class PM25Helper extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UpdatePM25InfoAsyncTask readPM25InfoAsyncTask = null;
		PM25Helper.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pm_25_helper);
		final EditText edit_city = (EditText) findViewById(R.id.edit_city);
		Button query = (Button) findViewById(R.id.query);
		
		Button current = (Button) findViewById(R.id.current);
		
		final TextView[] weathers = new TextView[4];
		weathers[0] = (TextView) findViewById(R.id.date_time_now);
		weathers[1] = (TextView) findViewById(R.id.date_time_first);
		weathers[2] = (TextView) findViewById(R.id.date_time_second);
		weathers[3] = (TextView) findViewById(R.id.date_time_three);
		final TextView[] lifeIndexs = new TextView[12];
		lifeIndexs[0] = (TextView) findViewById(R.id.dress_index);
		lifeIndexs[1] = (TextView) findViewById(R.id.dress_index_des);
		lifeIndexs[2] = (TextView) findViewById(R.id.car_index);
		lifeIndexs[3] = (TextView) findViewById(R.id.car_index_des);
		lifeIndexs[4] = (TextView) findViewById(R.id.travel_index);
		lifeIndexs[5] = (TextView) findViewById(R.id.travel_index_des);
		lifeIndexs[6] = (TextView) findViewById(R.id.cold_index);
		lifeIndexs[7] = (TextView) findViewById(R.id.cold_index_des);
		lifeIndexs[8] = (TextView) findViewById(R.id.sport_index);
		lifeIndexs[9] = (TextView) findViewById(R.id.sport_index_des);
		lifeIndexs[10] = (TextView) findViewById(R.id.uv_index);
		lifeIndexs[11] = (TextView) findViewById(R.id.uv_index_des);

		final TextView[] pm25s = new TextView[2];
		pm25s[0] = (TextView) findViewById(R.id.city_name);
		pm25s[1] = (TextView) findViewById(R.id.pm25);

		readPM25InfoAsyncTask = new UpdatePM25InfoAsyncTask(this.getWindow().getContext() ,pm25s,weathers,lifeIndexs);
		readPM25InfoAsyncTask.execute(Constant.locationInfo(this.getWindow().getCurrentFocus()));
		
		current.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!PhoneStatusUtils.isOpenNetwork(v.getContext())){
					DialogView.to_setting_dialog(PM25Helper.this);
				}else{
					UpdatePM25InfoAsyncTask readPM25InfoAsyncTask = new UpdatePM25InfoAsyncTask(v.getContext() ,pm25s,weathers,lifeIndexs);
					readPM25InfoAsyncTask.execute(Constant.locationInfo(v));
					edit_city.setText("");
				}
			}
		});
		
		query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!PhoneStatusUtils.isOpenNetwork(v.getContext())){
					DialogView.to_setting_dialog(PM25Helper.this);
				}else{
					if(edit_city.getText()==null || edit_city.getText().toString().trim().length()==0){
						ParseXmlService parseXmlService = new ParseXmlService();
						int versionCode = parseXmlService.getVersionCode(PM25Helper.this, "org.maxsoft.pm");
						Toast.makeText(v.getContext(), "ÇëÌîÐ´³ÇÊÐÃû³Æ£¡" + versionCode, Toast.LENGTH_LONG).show();
					}else{
						UpdatePM25InfoAsyncTask readPM25InfoAsyncTask = new UpdatePM25InfoAsyncTask(v.getContext(),pm25s,weathers,lifeIndexs);
						readPM25InfoAsyncTask.execute(edit_city.getText().toString());
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pm25_helper, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

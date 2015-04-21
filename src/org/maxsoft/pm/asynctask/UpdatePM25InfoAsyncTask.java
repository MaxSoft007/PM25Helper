package org.maxsoft.pm.asynctask;

import org.maxsoft.pm.pojo.LifeIndex;
import org.maxsoft.pm.pojo.PM25;
import org.maxsoft.pm.pojo.Result;
import org.maxsoft.pm.pojo.WeatherInfo;
import org.maxsoft.utils.Constant;
import org.maxsoft.utils.ReadPM25Info;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import android.widget.TextView;
import android.widget.Toast;

public class UpdatePM25InfoAsyncTask extends AsyncTask<String, Integer, Result> {
	private Context context = null;
	ReadPM25Info readPM25Info = null;
	private TextView[] lifeIndexs = null;
	private TextView[] weathers = null;
	private TextView[] pm25s = null;
	public UpdatePM25InfoAsyncTask(Context context,TextView[] pm25s,TextView[] weathers,TextView[] lifeIndexs) {
		this.context = context;
		this.pm25s = pm25s;
		this.weathers = weathers;
		this.lifeIndexs = lifeIndexs;
	}
	ProgressDialog mProgressDialog = null;
	/**
	 * ������UI�߳��У��ڵ���doInBackground()֮ǰִ��
	 */
	@Override
	protected void onPreExecute() {
		mProgressDialog = ProgressDialog.show(context, "��ȴ�", "���ڸ�����Ϣ...", true);
	}

	/**
	 * ��̨���еķ������������з�UI�̣߳�����ִ�к�ʱ�ķ���
	 */
	@Override
	protected Result doInBackground(String... params) {
		Result result = null;
		try {
			readPM25Info = new ReadPM25Info(params[0]);
			result = readPM25Info.ReadFromInternet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ������ui�߳��У���doInBackground()ִ����Ϻ�ִ��
	 */
	@Override
	protected void onPostExecute(Result result) {
		mProgressDialog.cancel();
		if(result.getStatus().equals("success") && result.getError()==0){
			PM25 pm25 = result.getResults()[0];
			pm25s[0].setText(pm25.getCurrentCity());
			
			if(pm25.getPm25()!=null && pm25.getPm25().length() > 0){
				long value = Integer.valueOf(pm25.getPm25()) ;
				String[] pmLevel = Constant.getPMLevel(value);
				pm25s[1].setText(pmLevel[0]);
				pm25s[1].setTextColor(Color.parseColor(pmLevel[1]));
			}

			WeatherInfo[] weather_data = pm25.getWeather_data();
			for(int i=0;i<weathers.length;i++){
				weathers[i].setText(weather_data[i].getDate() + " | " + weather_data[i].getWeather()  + " | " +  weather_data[i].getTemperature() + " | " + weather_data[i].getWind());
			}
			LifeIndex[] index = pm25.getIndex();
			if(index != null &&  index.length > 0){
				for(int i=0;i<lifeIndexs.length;i++){
					if(i%2==0){
						lifeIndexs[i].setText(index[i/2].getTipt() + "��" + index[i/2].getZs());
					}else if(i%2==1){
						lifeIndexs[i].setText("        "+index[i/2].getDes());
					}
				}
			}else{
				for(int i=0;i<lifeIndexs.length;i++){
					if(i%2==0){
						lifeIndexs[i].setText("��");
					}else if(i%2==1){
						lifeIndexs[i].setText("�õ�����֧������ָ����ѯ��");
					}
				}
			}
		}else{
			Toast.makeText(context, "�����ѯ��ȷ�ĳ������ƣ�", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ��publishProgress()�������Ժ�ִ�У�publishProgress()���ڸ��½���
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		
	}

}

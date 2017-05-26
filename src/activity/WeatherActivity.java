package activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import model.Weather;
import util.HttpRequestListener;
import util.HttpUtil;
import util.LogUtil;
import util.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;

public class WeatherActivity extends Activity implements OnClickListener{
	private static final String TagString  = "WeatherActivity";
	/**
	 * ����������������������Ű沼��
	 */
	private LinearLayout weatherInfoLayout = null;
	/**
	 * ��ʾ������
	 */
	private TextView cityName = null;
	/**
	 * ��ʾ����ʱ��
	 */
	private TextView publishText = null;
	/**
	 * ��ʾ��ʼ�¶�
	 */
	private TextView temp1 = null;
	/**
	 * ��ʾ��ֹ�¶�
	 */
	private TextView temp2 = null;
	/**
	 * �����¶�֮��ļ����
	 */
	private TextView tempMargin = null;
	/**
	 * ������ʾ����������Ϣ
	 */
	private TextView weatherDesp = null;
	/**
	* �л����а�ť
	*/
	private Button switchCity;
	/**
	* ����������ť
	*/
	private Button refreshWeather;
	/**
	* ������ʾ��ǰ����
	*/
	private TextView currentDateText;
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.show_weather);
		// ��ʼ�����ؼ�
		 weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info);
		 cityName = (TextView) findViewById(R.id.city_name);
		 publishText = (TextView) findViewById(R.id.publish_text);
		 /**
		  * ������ʾ����������Ϣ
		  */
		 weatherDesp = (TextView) findViewById(R.id.weather_desp);
		 temp1 = (TextView) findViewById(R.id.temp1);
		 temp2 = (TextView) findViewById(R.id.temp2);
		 cityName.setVisibility(View.INVISIBLE);
		 weatherInfoLayout.setVisibility(View.INVISIBLE);
		 //currentDateText = (TextView) findViewById(R.id.current_date);
		 switchCity = (Button) findViewById(R.id.switch_city);
		 refreshWeather = (Button) findViewById(R.id.refresh_weather);
		 String countyName = getIntent().getStringExtra("county_name");
		 if (!TextUtils.isEmpty(countyName)) {
			 HttpUtil.requestHttp(HttpUtil.countyToSinaWeatherAddress(countyName), new HttpRequestListener() {

				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFinish(String response, Context context) {
					Weather.parseXMLWithPull(response, context);
				}
				 
			 }, WeatherActivity.this);
		 }
		 showWeather();
		 switchCity.setOnClickListener(this);
		 refreshWeather.setOnClickListener(this);
 	 }
	private void showWeather() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
		LogUtil.d(TagString + " showWeather() :" , sp.getString("city", " ") + sp.getString("udatetime", " ") + sp.getString("status1", " "));
		cityName.setText(sp.getString("city", " "));
		publishText.setText(sp.getString("udatetime", " "));
		weatherDesp.setText(sp.getString("status1", " "));
		temp1.setText(sp.getString("temperature1", " "));
		temp2.setText(sp.getString("temperature2", " "));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityName.setVisibility(View.VISIBLE);
	}
	public static void saveWeatherInfo(final Weather weather,final Context context) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy��m��d��", Locale.CHINA);
		
		Thread t = new Thread(new Runnable () {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/**
				 * getSharedPreference(),��һ�ִ�Context�еõ���������Ҫ������������һ��ָ�����ơ��ڶ���ָ��
				 * MODE_PRIVATE,MODE_PREFERENCE,����Activity��getSharedPreference(),���ֻ��Ҫ�ƶ��򿪷���
				 * ���ڶ������������������ͨ��PreferenceManager�õ�
				 */
				SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
				editor.putBoolean("selected_city", true);
				editor.putString("city", weather.getCity());
				editor.putString("direction1", weather.getDirection1());
				editor.putString("direction2", weather.getDirection2());
				editor.putString("chy_shuoming", weather.getChy_shuoming());
				editor.putString("power1", weather.getPower1());
				editor.putString("gm_s", weather.getGm_s());
				editor.putString("power2", weather.getPower2());
				editor.putString("status1", weather.getStatus1());
				editor.putString("status2", weather.getStatus2());
				editor.putString("temperature1", weather.getTemperature1());
				editor.putString("temperature2", weather.getTemperature2());
				editor.putString("udatetime", weather.getUdatetime());
				editor.putString("yd_s", weather.getYd_s());
				editor.commit();
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
				LogUtil.d(TagString + " saveWeatherInfo( :" , sp.getString("city", " ") + sp.getString("udatetime", " ") + sp.getString("status1", " "));
			}
		});
		t.start();
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.switch_city) {
			SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putBoolean("selected_city", false);
			editor.commit();
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.refresh_weather) {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
			String cityName = sp.getString("city", "");
			if (TextUtils.isEmpty(cityName)) {
				Toast.makeText(this, "������Ϊ�գ�����ʹ��refresh����", Toast.LENGTH_SHORT).show();
			} else {
				HttpUtil.requestHttp(HttpUtil.countyToSinaWeatherAddress(cityName), new HttpRequestListener() {

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFinish(String response, Context context) {
						Weather.parseXMLWithPull(response, context);
					}
					 
				 }, WeatherActivity.this);
				showWeather();
			}
		}
 		
	}
}

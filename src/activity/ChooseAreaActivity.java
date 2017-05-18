package activity;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.HttpRequestListener;
import util.HttpUtil;
import util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;

import db.CoolWeatherDB;

public class ChooseAreaActivity extends Activity{
	private List<String> dataList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private ListView listView;
	private TextView textView;
	private List<Province> provincesList;
	private List<City> citiesList;
	private List<County> countiesList;
	/**
	 * 显示对话框
	 */
	private ProgressDialog progressDialog;
	/**
	 * 这里构造很精妙，包括查询的时候通过selecetedProvince,selectedCity等
	 * 在onBackPresss()中，也可以通过currentLevel,来选择即将执行queryCities()，queryCounties(),queryProvinces()
	 * 选中的级别,为了辨别现在是在哪张表上，以此来改变显示ListView
	 */
	private int currentLevel;
	private final int provinceLevel = 0;
	private final int cityLevel = 1;
	private final int countyLevel = 2;
	private Province selectedProvince;
	private City selectedCity;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView)findViewById(R.id.list_view);
		textView = (TextView)findViewById(R.id.text_title);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == provinceLevel) {
					selectedProvince = provincesList.get(position);
					queryCities();
				} else if (currentLevel == cityLevel) {
					selectedCity = citiesList.get(position);
					queryCounties();
				}
			}
			
		});
		queryProvinces();
	}
	/**
	 * 查询全国所有的省，优先从数据库查询，如果查询不到就去服务器查询
	 */
	private void queryProvinces() {
		provincesList = coolWeatherDB.loadProvinces();
		if (provincesList.size() > 0) {
			dataList.clear();
			for (Province p : provincesList) {
				dataList.add(p.getProvinceName());
			}
			//adpter构造的时候时使用了dataList这个参数的，如今这个参数发生了改变
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText("中国");
			currentLevel = provinceLevel;
		} else {
			queryFromServer(null, "province");
		}
	}
	private void queryCities() {
		citiesList = coolWeatherDB.loadCities(selectedProvince.getProvinceId());
		if (citiesList.size() > 0) {
			dataList.clear();
			for (City c : citiesList) {
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedProvince.getProvinceName());
			currentLevel = cityLevel;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");	
		}
	}
	private void queryCounties() {
		countiesList = coolWeatherDB.loadCounties(selectedCity.getCityId());
		if (countiesList.size() > 0) {
			dataList.clear();
			for (County c : countiesList) {
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLevel = countyLevel;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}
	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		//申请网络，需要网络地址，并且使用对调方法
		HttpUtil.requestHttp(address, new HttpRequestListener() {

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
					
				});
			}

			@Override
			public void onFinish(String response) {
					// TODO Auto-generated method stub
					boolean result = false;
					if ("province".equals(type)) {
						result = Utility.handleProvincesResponse(coolWeatherDB,
						response);
					} else if ("city".equals(type)) {
						result = Utility.handleCitiesResponse(coolWeatherDB,
						response, selectedProvince.getProvinceId());
					} else if ("county".equals(type)) {
						result = Utility.handleCountiesResponse(coolWeatherDB,
						response, selectedCity.getCityId());
					}
					if (result) {
						// 通过runOnUiThread()方法回到主线程处理逻辑
						//如果数据库中找不到数据，就使用网络从服务器中获得数据，之后再
						//调用之前的方法，从数据库中获得数据，如果数据库加载错误，就继续
						//从网络中获得数据
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								closeProgressDialog();
								if ("province".equals(type)) {
									queryProvinces();
								} else if ("city".equals(type)) {
									queryCities();
								} else if ("county".equals(type)) {
									queryCounties();
								}
							}
						});
					}
			}
		});
	}
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(true);
		}
		progressDialog.show();
	}
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	@Override
	public void onBackPressed() {
		if (currentLevel == countyLevel) {
			queryCities();
		} else if (currentLevel == cityLevel) {
			queryProvinces();
		} else {
			finish();
		}
	}
}

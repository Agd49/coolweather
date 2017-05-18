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
	 * ��ʾ�Ի���
	 */
	private ProgressDialog progressDialog;
	/**
	 * ���ﹹ��ܾ��������ѯ��ʱ��ͨ��selecetedProvince,selectedCity��
	 * ��onBackPresss()�У�Ҳ����ͨ��currentLevel,��ѡ�񼴽�ִ��queryCities()��queryCounties(),queryProvinces()
	 * ѡ�еļ���,Ϊ�˱�������������ű��ϣ��Դ����ı���ʾListView
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
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�������ѯ������ȥ��������ѯ
	 */
	private void queryProvinces() {
		provincesList = coolWeatherDB.loadProvinces();
		if (provincesList.size() > 0) {
			dataList.clear();
			for (Province p : provincesList) {
				dataList.add(p.getProvinceName());
			}
			//adpter�����ʱ��ʱʹ����dataList��������ģ����������������˸ı�
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText("�й�");
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
		//�������磬��Ҫ�����ַ������ʹ�öԵ�����
		HttpUtil.requestHttp(address, new HttpRequestListener() {

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
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
						// ͨ��runOnUiThread()�����ص����̴߳����߼�
						//������ݿ����Ҳ������ݣ���ʹ������ӷ������л�����ݣ�֮����
						//����֮ǰ�ķ����������ݿ��л�����ݣ�������ݿ���ش��󣬾ͼ���
						//�������л������
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
			progressDialog.setMessage("���ڼ���...");
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

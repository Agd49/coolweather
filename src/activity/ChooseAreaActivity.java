package activity;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.HttpRequestListener;
import util.HttpUtil;
import util.LogUtil;
import util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
	private final String TagString = "ChooseAreaActivity";
	private List<String> dataList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private ListView listView;
	private TextView textView;
	private List<Province> provincesList;
	private List<City> citiesList;
	private List<County> countiesList;
	/**
	 * 閿熸枻鎷风ず閿熺殕浼欐嫹閿熸枻鎷�
	 */
	private ProgressDialog progressDialog;
	/**
	 * 閿熸枻鎷烽敓鏂ゆ瀯閿熸枻鎷疯姭閿熸枻鎷风澘顒婃嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺獤顖ゆ嫹閿熺粸鎲嬫嫹閿熼叺顭掓嫹閿熺氮elecetedProvince,selectedCity閿熸枻鎷�
	 * 閿熸枻鎷穙nBackPresss()閿熷彨锝忔嫹涔熼敓鏂ゆ嫹閿熸枻鎷烽�氶敓鏂ゆ嫹currentLevel,閿熸枻鎷烽�夐敓浠婂嵆鏂ゆ嫹鎵ч敓鏂ゆ嫹queryCities()閿熸枻鎷穛ueryCounties(),queryProvinces()
	 * 閫夐敓鍙殑纭锋嫹閿熸枻鎷�,涓洪敓鍓挎唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风枱閿熸枻鎷峰笇閿熸枻鎷锋簮閿熸枻鎷烽敓鏂ゆ嫹璋嬮敓鏂ゆ嫹閿熺粸缍stView
	 */
	private int currentLevel;
	private final int provinceLevel = 0;
	private final int cityLevel = 1;
	private final int countyLevel = 2;
	private boolean isHandleInTxt = false;
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
//		if (!isHandleInTxt) {
//			Utility.handleProvinceInTxt(coolWeatherDB, this);
//			Utility.handleCityInTxt(coolWeatherDB, this);
//			Utility.handleCountyInTxt(coolWeatherDB, this);
//			isHandleInTxt = true;
//		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == provinceLevel) {
					selectedProvince = provincesList.get(position);
					queryCities();
				} else if (currentLevel == cityLevel) {
//					for (City c: citiesList) {
//						LogUtil.e(TagString, c.toString());
//					}
					selectedCity = citiesList.get(position);
					queryCounties();
				}
			}
			
		});
		queryProvinces();
	}
	
	protected void onStart() {
        super.onStart();
        
    }
	
	/**
	 * 閿熸枻鎷疯鍏ㄩ敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷风渷閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ杈炬嫹閿熸枻鎷烽敓鎹峰尅鎷烽敓绐栴垽鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璇㈤敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鍘婚敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯
	 */
	private void queryProvinces() {
		provincesList = coolWeatherDB.loadProvinces();
		if (provincesList.size() > 0) {
			dataList.clear();
			for (Province p : provincesList) {
				dataList.add(p.getProvinceName());
			}
			//adpter閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎲嬫嫹閿熺粸绗旂櫢鎷烽敓鏂ゆ嫹閿熺禌ataList閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹妯￠敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风儊璋嬮敓锟�
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText("中国");
			currentLevel = provinceLevel;
		} else {
			LogUtil.d(TagString, "得到的省序列为0");
			Utility.handleProvinceInTxt(coolWeatherDB, this);
		}
//		else {
//			queryFromServer(null, "province");
//		}
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
			LogUtil.d(TagString, "得到城市序列为0");
			Utility.handleCityInTxt(coolWeatherDB, this);
		}
//		else {
//			queryFromServer(selectedProvince.getProvinceCode(), "city");	
//		}
	}
	private void queryCounties() {
		LogUtil.d(TagString, selectedCity.getCityCode());
		countiesList = coolWeatherDB.loadCounties(Integer.parseInt(selectedCity.getCityCode()));
		if (countiesList.size() > 0) {
			dataList.clear();
			for (County c : countiesList) {
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLevel = countyLevel;
		}  else {
			LogUtil.d(TagString	, "得到的县序列为0");
			Utility.handleCountyInTxt(coolWeatherDB, this);
			countiesList = coolWeatherDB.loadCounties(Integer.parseInt(selectedCity.getCityCode()));
			for (County c : countiesList) {
				LogUtil.d(TagString, c.getCountyName());
			}
		}
//		else {
//			queryFromServer(selectedCity.getCityCode(), "county");
//		}
	}
	private void queryFromTxt (String type) {
		if (type.equals("city")) {
			Utility.handleCityInTxt(coolWeatherDB, this);
			queryCities();
		} else if (type.equals("county")) {
			Utility.handleCountyInTxt(coolWeatherDB, this);
			queryCounties();
		} else {
			Utility.handleProvinceInTxt(coolWeatherDB, this);
			queryProvinces();
		}
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

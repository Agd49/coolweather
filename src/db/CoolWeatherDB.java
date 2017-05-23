package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.LogUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/*
 * ����Ӧ���е��������ݿ⿪�������е������
 */
public class CoolWeatherDB {
	private SQLiteDatabase db;
	private static int VERSION = 1;
	private final String TAGString = "CoolWeatherDB"; 
	private static final String DB_NAME = "cool_weather";
	private static CoolWeatherDB coolWeatherDB;
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context,
				 DB_NAME, null, VERSION);
		db = coolWeatherOpenHelper.getWritableDatabase();
	}
	public static CoolWeatherDB getInstance(Context context) {
		//�ж��Ƿ���Ҫ����ͬ�������
		if (coolWeatherDB == null) {
			synchronized(CoolWeatherDB.class) {
				//ȷ������ͬ���������߳���ֻ��һ���ܹ��½�����
				if (coolWeatherDB == null) {
					coolWeatherDB = new CoolWeatherDB(context);
				}
			}
		}
		return coolWeatherDB;
	}
	public boolean saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
			return true;
		}
		return false;
	}
	public boolean saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
			return true;
		}
		return false;
	}
	public boolean saveCounty(County county) {
			if (county != null) {
				ContentValues values = new ContentValues();
				values.put("county_name", county.getCountyName());
				values.put("county_code", county.getCountyCode());
				values.put("city_id", county.getCityId());
				db.insert("County", null, values);
				return true;
			}
			return false;
	}
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		//Cursor�Ǹ�ÿһ�����ݿ��й�ϵ�ģ�����û�й��췽������ͨ��db�õ�
		//db��ѯ���±��е����ݣ����Ե�һ�����������ݿ�����ű�������ֹ�����
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do{
				Province province = new Province();
				province.setProvinceId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	/**
	 * �������к�ʡ��ͬ������ʡʱ��Ҫ�����е�ʡ�ݾ���ӡ��������������������Ҫ�Ѷ�Ӧ��ʡ��
	 * �ĳ�����������
	 * @return
	 */
	public List<City> loadCities(int provinceId) {
		LogUtil.d(TAGString, "loadCities( " + provinceId);
		List<City> list = new ArrayList<City>();
		//selectionһ��ռλ��?����ôselectionArgs String[]����Ҳֻ��һ��Ԫ��
		Cursor cursor = db.query("City", null, "province_id = ?", new String[] {String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while(cursor.moveToNext());
		}
		return list;
	}
	public List<County> loadCounties(int cityId) {
		LogUtil.d(TAGString, "loadCounties( " + String.valueOf(cityId));
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
//		//ʹ�����ַ�ʽ�ᳬ���߽�
//		for (cursor.moveToFirst();!cursor.isLast();cursor.moveToNext()) {
//			
//		}
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setCountyId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCityId(cityId);
				list.add(county);
			} while(cursor.moveToNext());
		}
		return list;
	}
}

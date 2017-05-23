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
 * 将该应用中的所有数据库开发都集中到这个类
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
		//判断是否需要进入同步代码块
		if (coolWeatherDB == null) {
			synchronized(CoolWeatherDB.class) {
				//确保进入同步代码块的线程中只有一个能够新建对象
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
		//Cursor是跟每一个数据库有关系的，所有没有构造方法而是通过db得到
		//db查询的事表中的内容，所以第一个数据是数据库的哪张表，这里出现过错误
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
	 * 搜索城市和省不同，搜索省时需要把所有的省份均打印出来，而搜索城市是需要把对应的省份
	 * 的城市搜索出来
	 * @return
	 */
	public List<City> loadCities(int provinceId) {
		LogUtil.d(TAGString, "loadCities( " + provinceId);
		List<City> list = new ArrayList<City>();
		//selection一个占位符?，那么selectionArgs String[]这里也只有一个元素
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
//		//使用这种方式会超出边界
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

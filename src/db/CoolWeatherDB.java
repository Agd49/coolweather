package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
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
			db.insert(DB_NAME, null, values);
			return true;
		}
		return false;
	}
	public boolean saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			db.insert(DB_NAME, null, values);
			return true;
		}
		return false;
	}
	public boolean saveCounty(County county) {
			if (county != null) {
				ContentValues values = new ContentValues();
				values.put("county_name", county.getCountyName());
				values.put("county_code", county.getCountyCode());
				db.insert(DB_NAME, null, values);
				return true;
			}
			return false;
	}
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		//Cursor是跟每一个数据库有关系的，所有没有构造方法而是通过db得到
		Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
		for (cursor.moveToFirst();!cursor.isLast();cursor.moveToNext()) {
			Province province = new Province();
			province.setProvinceId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		}
		return list;
	}
	public List<City> loadCities() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				list.add(city);
			} while(cursor.moveToNext());
		}
		return list;
	}
	public List<County> loadCounties() {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
		for (cursor.moveToFirst();!cursor.isLast();cursor.moveToNext()) {
			County county = new County();
			county.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
			county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
			list.add(county);
		}
		return list;
	}
}

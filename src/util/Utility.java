package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import model.City;
import model.County;
import model.Province;
import android.app.Activity;
import android.util.Log;
import db.CoolWeatherDB;

public class Utility {
	private static final String filename_city = "city.txt";
	private static final String filename_province = "province.txt";
	private static final String filename_county = "county.txt";
	private static final String TAGString = "Utility";
	public static boolean handleProvinceInTxt(final CoolWeatherDB coolWeatherDB, final Activity activity){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InputStream is = activity.getAssets().open(filename_province);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null) {
						String[] array = line.split("\\|");
						Province province = new Province();
						province.setProvinceCode(array[0]);
						province.setProvinceName(array[1]);
						//Log.e(TAGString, province.toString());
						if (!coolWeatherDB.saveProvince(province)) {
							Log.d("Utility", "¥Ê¥¢ °∑›∑¢œ÷¥ÌŒÛ");
						}
						
					}
					is.close();
					br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		return false;
	}
	public static boolean handleCityInTxt(final CoolWeatherDB coolWeatherDB, final Activity activity) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InputStream is = activity.getAssets().open(filename_city);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null) {
						String[] array = line.split("\\|");
						City city = new City();
						city.setCityCode(array[0]);
						city.setCityName(array[1]);
						city.setProvinceId(Integer.valueOf(array[2]));
						//Log.e(TAGString, city.toString());
						if (!coolWeatherDB.saveCity(city)) {
							Log.d("Utility", "city ¥Ê¥¢ error!");
						}
					}
					is.close();
					br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		return false;
	}
	public static boolean handleCountyInTxt(final CoolWeatherDB coolWeatherDB, final Activity activity) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InputStream is = activity.getAssets().open(filename_county);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null) {
						String[] array = line.split("\\|");
						County county = new County();
						county.setCountyCode(array[0]);
						county.setCountyName(array[1]);
						county.setCityId(Integer.valueOf(array[2]));
						//Log.e(TAGString, county.toString());
						if (!coolWeatherDB.saveCounty(county)) {
							Log.d("Utility","county¥Ê¥¢¥ÌŒÛ");
						}
						
					}
					is.close();
					br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		return false;
	}
}

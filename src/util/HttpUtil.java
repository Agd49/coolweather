package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import activity.ChooseAreaActivity;
import android.content.Context;

import model.Weather;


public class HttpUtil {
	public static void requestHttp(final String address, final HttpRequestListener listener, final Context context) {
		new Thread (new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection httpURLConnection = null;
				StringBuffer sb ;
				try {
					URL url = new URL(address);
					httpURLConnection = (HttpURLConnection)url.openConnection();
					httpURLConnection.setDoInput(true);
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setConnectTimeout(8000);
					httpURLConnection.setReadTimeout(8000);
					httpURLConnection.setRequestMethod("GET");
					InputStream inputStream = httpURLConnection.getInputStream();
					BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(inputStream));
					String temp = "init";
					temp = bufferedReader.readLine();
					//System.out.println(temp);
					sb = new StringBuffer();
					while (temp != null) {
						sb.append(temp);
						temp = bufferedReader.readLine();
						//System.out.println(temp);
					}
					//System.out.println(sb.toString());
					LogUtil.d("HttpUtil.class GET", sb.toString());
					if (listener != null) {
						//listener.onFinish(sb.toString(), ChooseAreaActivity.getInstance());
						listener.onFinish(sb.toString(), context);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (httpURLConnection != null) {
						httpURLConnection.disconnect();
					}
				}
			}
			
		}).start();	
	}
	/**
	 * 传入县的名字，得到可以向新浪天气接口申请的address
	 * @param countyName
	 */
	public static String countyToSinaWeatherAddress(String countyName) {
		String ret = null;
		String encodeCountyName = null;
		try {
			encodeCountyName = URLEncoder.encode(countyName, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret = "http://php.weather.sina.com.cn/xml.php?city="
				+ encodeCountyName + "&password=DJOYnieT8234jlsK&day=0";
		return ret;
	}
	public static void main(String []args) {
		String address = countyToSinaWeatherAddress("永宁");
//		HttpUtil.requestHttp(address, new HttpRequestListener() {
//
//			@Override
//			public void onError(Exception e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onFinish(String response) {
//				// TODO Auto-generated method stub
//				Weather.parseXMLWithPull(response);
//			}
//			
//		});		
	}
}
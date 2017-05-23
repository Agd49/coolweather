package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUtil {
	public static void requestHttp(final String address, final HttpRequestListener listener) {
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
					System.out.println(sb.toString());
					//LogUtil.d("HttpUtil.class GET", sb.toString());
					if (listener != null) {
						listener.onFinish(sb.toString());
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
	public static void main(String []args) {
		HttpUtil.requestHttp("http://www.weather.com.cn/data/cityinfo/101170102.html", new HttpRequestListener() {

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
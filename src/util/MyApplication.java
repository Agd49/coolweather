package util;

import android.content.Context;
import android.app.Application;

public class MyApplication extends Application{
	//��Ϊ�����Context����static������ʹ�ã�����Ҫ����Ϊstatic
	private static Context context;
	@Override
    public void onCreate() {
    	context = getApplicationContext();
    }
	public static Context getContext() {
		return context;
	}
}

package util;

import android.content.Context;
import android.app.Application;

public class MyApplication extends Application{
	//因为这里的Context会在static方法中使用，所以要申明为static
	private static Context context;
	@Override
    public void onCreate() {
    	context = getApplicationContext();
    }
	public static Context getContext() {
		return context;
	}
}

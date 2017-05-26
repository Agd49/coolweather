package util;

import android.content.Context;

public interface HttpRequestListener {
		public void onError(Exception e);
		public void onFinish(String response, Context context);
}

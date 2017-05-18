package util;

public interface HttpRequestListener {
		public void onError(Exception e);
		public void onFinish(String response);
}

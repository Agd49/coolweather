package model;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import util.LogUtil;
import util.MyApplication;
import activity.WeatherActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Weather {
	private String city = "";
	private String status1 = "";
	private String temperature1 = "";
	private String direction1 = "";
	private String power1 = "";
	private String status2 = "";
	private String temperature2 = "";
	private String direction2 = "";
	private String power2 = "";
	private String chy_shuoming = "";
	private String gm_s = "";
	private String yd_s = "";
	private String udatetime = "";
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStatus1() {
		return status1;
	}
	public void setStatus1(String status1) {
		this.status1 = status1;
	}
	public String getTemperature1() {
		return temperature1;
	}
	public void setTemperature1(String temperature1) {
		this.temperature1 = temperature1;
	}
	public String getDirection1() {
		return direction1;
	}
	public void setDirection1(String direction1) {
		this.direction1 = direction1;
	}
	public String getPower1() {
		return power1;
	}
	public void setPower1(String power1) {
		this.power1 = power1;
	}
	public String getStatus2() {
		return status2;
	}
	public void setStatus2(String status2) {
		this.status2 = status2;
	}
	public String getTemperature2() {
		return temperature2;
	}
	public void setTemperature2(String temperature2) {
		this.temperature2 = temperature2;
	}
	public String getDirection2() {
		return direction2;
	}
	public void setDirection2(String direction2) {
		this.direction2 = direction2;
	}
	public String getPower2() {
		return power2;
	}
	public void setPower2(String power2) {
		this.power2 = power2;
	}
	public String getChy_shuoming() {
		return chy_shuoming;
	}
	public void setChy_shuoming(String chy_shuoming) {
		this.chy_shuoming = chy_shuoming;
	}
	public String getGm_s() {
		return gm_s;
	}
	public void setGm_s(String gm_s) {
		this.gm_s = gm_s;
	}
	public String getYd_s() {
		return yd_s;
	}
	public void setYd_s(String yd_s) {
		this.yd_s = yd_s;
	}
	public String getUdatetime() {
		return udatetime;
	}
	public void setUdatetime(String udatetime) {
		this.udatetime = udatetime;
	} 
	@Override
	public String toString() {
		return city + ",������죺" + status1
				+ ",����¶�" + temperature1 + ",����¶�" + temperature2 + ", ����"
				+ direction1 + ", �缶" + power1  + "ҹ��: ����" + direction2 + ", �缶" + power2
				+ ", ���鴩��: " + chy_shuoming + ", ��ðԤ��: " + gm_s + ", �˶�����: " + yd_s
				+ "����ʱ��: " + udatetime;
	}
	public static Weather parseXMLWithPull(String xmlData, Context context) {
		Weather weather = null;
		/**
		 * ���û����һ��Ļ���û������Ϊ����ֻ��õ�һ����ָ��
		 */
		weather = new Weather();
		try {
			/**
			 * ����Ҫ���һ��XMLPullParserFactoryʵ��rsource not found,������kxml2.jar������������
			 * ������������������ʱ�򣬻���ʾ
			 */
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			/**
			 * ͨ��XMLPullParserFactoryʵ�����xmlPullParserʵ��
			 */
			XmlPullParser xmlPullParser = factory.newPullParser();
			/**
			 * ��Ҫ������xml������xmlPullParser��setInput()������
			 */
			xmlPullParser.setInput(new StringReader(xmlData));
			/**
			 * �õ���ǰ�Ľ����¼�
			 */
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
					// ��ʼ����ĳ�����
					case XmlPullParser.START_TAG: {
						if ("city".equals(nodeName)) {
							weather.city = xmlPullParser.nextText();
						} else if ("status1".equals(nodeName)) {
							weather.status1 = xmlPullParser.nextText();
						} else if ("direction1".equals(nodeName)) {
							weather.direction1 = xmlPullParser.nextText();
						} else if ("power1".equals(nodeName)) {
							weather.power1 = xmlPullParser.nextText();
						} else if ("status2".equals(nodeName)) {
							weather.status2 = xmlPullParser.nextText();
						} else if ("temperature2".equals(nodeName)) {
							weather.temperature2 = xmlPullParser.nextText();
						} else if ("direction2".equals(nodeName)) {
							weather.direction2 = xmlPullParser.nextText();
						} else if ("power2".equals(nodeName)) {
							weather.power2 = xmlPullParser.nextText();
						} else if ("chy_shuoming".equals(nodeName)) {
							weather.chy_shuoming = xmlPullParser.nextText();
						} else if ("gm_s".equals(nodeName)) {
							weather.gm_s = xmlPullParser.nextText();
						} else if ("yd_s".equals(nodeName)) {
							weather.yd_s = xmlPullParser.nextText();
						} else if ("udatetime".equals(nodeName)) {
							weather.udatetime = xmlPullParser.nextText();
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("Weather".equals(nodeName)) {
							LogUtil.d("Weather.java", "weather is: " + weather.toString());
							//System.out.println(weather.toString());
							WeatherActivity.saveWeatherInfo(weather, context);
						}
						break;
					}
					default:
						break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
		e.printStackTrace();
		}
		return weather;
	}
//	public static void main(String[] args ) {
//		String xmlData = new String();
//		xmlData = "<!-- published at 2017-05-25 00:18:13 --><Profiles><Weather><city>����</city><status1>��</status1><status2>��</status2><figure1>qing</figure1><figure2>qing</figure2><direction1>�޳�������</direction1><direction2>�޳�������</direction2><power1>��3</power1><power2>��3</power2><temperature1>29</temperature1><temperature2>13</temperature2><ssd>7</ssd><tgd1>26</tgd1><tgd2>26</tgd2><zwx>4</zwx><ktk>4</ktk><pollution>3</pollution><xcz>1</xcz><zho></zho><diy></diy><fas></fas><chy>3</chy><zho_shuoming>����</zho_shuoming><diy_shuoming>����</diy_shuoming><fas_shuoming>����</fas_shuoming><chy_shuoming>���㱡�����㱡�����������㡢��֯������������T����������װ��ţ�����㡢������װ�����ͼп�</chy_shuoming><pollution_l>���</pollution_l><zwx_l>ǿ</zwx_l><ssd_l>ƫ��</ssd_l><fas_l>����</fas_l><zho_l>����</zho_l><chy_l>������</chy_l><ktk_l>����Ҫ����</ktk_l><xcz_l>�ǳ�����</xcz_l><diy_l>����</diy_l><pollution_s>�Կ�����Ⱦ����ɢ������Ӱ��</pollution_s><zwx_s>������ǿ</zwx_s><ssd_s>����ƫ�ȣ��ʵ��������º��Կɴﵽ�Ƚ����ʵĳ̶ȡ�</ssd_s><ktk_s>����Ҫ�����յ�</ktk_s><xcz_s>ϴ��������δ��4����û�н�ˮ����硢ɳ������������ʱ�䳤���ǳ�����ϴ��</xcz_s><gm>1</gm><gm_l>�ͷ���</gm_l><gm_s>�����¶Ƚϸߣ�Ҫ�����ʱ���ڿյ������������Ŀյ�����</gm_s><yd>4</yd><yd_l>��̫����</yd_l><yd_s>�������ȣ���̫���˻����˶���</yd_s><savedate_weather>2017-05-25</savedate_weather><savedate_life>2017-05-25</savedate_life><savedate_zhishu>2017-05-25</savedate_zhishu><udatetime>2017-05-24 17:10:00</udatetime></Weather></Profiles>";
//		Weather.parseXMLWithPull(xmlData);
//	}
}

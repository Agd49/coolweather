package model;

public class City {
	private int cityId;
	private String cityName;
	private String cityCode;
	private int Province;
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public int getProvince() {
		return Province;
	}
	public void setProvince(int province) {
		Province = province;
	}
	
}

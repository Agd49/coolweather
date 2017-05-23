package model;

public class City {
	private int cityId;
	private String cityName;
	private String cityCode;
	private int ProvinceId;
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
	public int getProvinceId() {
		return ProvinceId;
	}
	public void setProvinceId(int provinceId) {
		this.ProvinceId = provinceId;
	}
	public String toString() {
		return String.valueOf(cityId) + " " + cityCode + " " + cityName + " " + String.valueOf(ProvinceId);
	}
}

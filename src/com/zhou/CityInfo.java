package com.zhou;

public class CityInfo {
	public String name;
	public int cityIndex;
	public double coordx;
	public double coordy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCityIndex() {
		return cityIndex;
	}

	public void setCityIndex(int cityIndex) {
		this.cityIndex = cityIndex;
	}

	public double getCoordx() {
		return coordx;
	}

	public void setCoordx(double coordx) {
		this.coordx = coordx;
	}

	public double getCoordy() {
		return coordy;
	}

	public void setCoordy(double coordy) {
		this.coordy = coordy;
	}

	public double getCityDis(CityInfo c1) {
		return Math.sqrt((c1.coordx - this.coordx) * (c1.coordx - this.coordx)
				+ (c1.coordy - this.coordy) * (c1.coordy - this.coordy));
	}
}

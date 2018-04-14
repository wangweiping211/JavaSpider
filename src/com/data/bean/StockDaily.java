package com.data.bean;

public class StockDaily {
	String saveTime;
	public String getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}

	String stockID;
	String stockName;
	float beginPrice;
	float lowPrice;
	float highPrice;
	float closePrice;
	int counts;
	int mount;
	float exchangeRite;

	public float getExchangeRite() {
		return exchangeRite;
	}

	public void setExchangeRite(float exchangeRite) {
		this.exchangeRite = exchangeRite;
	}

	public String getStockID() {
		return stockID;
	}

	public void setStockID(String stockID) {
		this.stockID = stockID;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public float getBeginPrice() {
		return beginPrice;
	}

	public void setBeginPrice(float beginPrice) {
		this.beginPrice = beginPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(float closePrice) {
		this.closePrice = closePrice;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public int getMount() {
		return mount;
	}

	public void setMount(int mount) {
		this.mount = mount;
	}
}

/**
 * 
 */
package com.data.bean;

/**
 * @author Administrator
 * 
 */
public class StockBeanRich {
	
	String stockID;
	String stockName;
	String category;
	String area;
	String openTime;
	public String getFloatStock() {
		return floatStock;
	}

	public void setFloatStock(String floatStock) {
		this.floatStock = floatStock;
	}

	public String getAllStock() {
		return allStock;
	}

	public void setAllStock(String allStock) {
		this.allStock = allStock;
	}

	String floatStock;
	String allStock;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

}

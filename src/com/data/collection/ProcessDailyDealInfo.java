package com.data.collection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.data.bean.StockBeanRich;
import com.data.bean.StockDaily;
import com.data.dao.GetAllStockInfoDAOImpl;

public class ProcessDailyDealInfo {
	// 84b6e655fe1e154b275dce8459e8afa3
	String httpUrl = "http://apis.baidu.com/apistore/stockservice/stock";
	String httpArg = "";
	String stockCode = "";
	GetAllStockInfoDAOImpl getAllStockInfoDAOImpl = null;

	public void processmain() {
		String jsonResult = request(httpUrl, httpArg);
		saveDailyInfo(jsonResult, stockCode);
	}

	ProcessDailyDealInfo(String stockCode,
			GetAllStockInfoDAOImpl getAllStockInfoDAOImpl) {
		String temp = null;
		String httpArgBase = "stockid=";
		this.stockCode = stockCode;
		if (stockCode.startsWith("6")) {
			temp = "sh" + stockCode + "&list=1";

		} else {
			temp = "sz" + stockCode + "&list=1";
		}
		httpArg = httpArgBase + temp;
		this.getAllStockInfoDAOImpl = getAllStockInfoDAOImpl;
	}

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("apikey",
					"84b6e655fe1e154b275dce8459e8afa3");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void saveDailyInfo(String info, String stockID) {
		List<StockBeanRich> listStock = getAllStockInfoDAOImpl
				.queryStockByID(stockID);
		JSONObject jsAll = JSONObject.fromObject(info);
		JSONObject stockInfoList = JSONObject.fromObject(jsAll
				.getString("retData"));
		Object stockInfoObj = JSONArray.fromObject(
				stockInfoList.getString("stockinfo")).get(0);
		JSONObject stockInfo = JSONObject.fromObject(stockInfoObj);
		System.out.println(stockInfo.getString("name"));
		StockDaily stockDaily = new StockDaily();
		stockDaily.setStockID(stockID);
		stockDaily.setSaveTime(stockInfo.getString("date"));
		stockDaily.setBeginPrice(Float.parseFloat(stockInfo
				.getString("OpenningPrice")));
		stockDaily
				.setHighPrice(Float.parseFloat(stockInfo.getString("hPrice")));
		stockDaily.setLowPrice(Float.parseFloat(stockInfo.getString("lPrice")));
		stockDaily.setClosePrice(Float.parseFloat(stockInfo
				.getString("currentPrice")));
		stockDaily.setCounts((int) Float.parseFloat(stockInfo
				.getString("totalNumber")));
		stockDaily.setMount((int) Float.parseFloat(stockInfo
				.getString("turnover")));
		// Name
		if (listStock != null && listStock.size() > 0) {
			stockDaily.setStockName(listStock.get(0).getStockName());
			float flowCount = Float
					.parseFloat(listStock.get(0).getFloatStock());
			float rite = stockDaily.getCounts() / (flowCount * 10000);
			stockDaily.setExchangeRite(rite);
		}
		if (stockDaily.getHighPrice() != stockDaily.getLowPrice()) {
			getAllStockInfoDAOImpl.saveStockDaily(stockDaily);
		}
		// ªª ÷¬ 
	}

}

package com.data.collection;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.data.bean.Stock5PC;
import com.data.dao.GetAllStockInfoDAOImpl;

public class CollectDataBase extends Thread{
	public int rate = 3; // 涨跌比率
	String stockCode;
	String stockName;
	public float price = 0;
	float changePrice = 0;
	public float changeRatePer = 0;
	public float turnOverPer = 0;
	int  count = 0;
	public int  mount = 0;
	public float sel1P = 0;
	public int sel1C = 0;
	public float sel2P = 0;
	public int sel2C = 0;
	public float sel3P = 0;
	public int sel3C = 0;
	public float sel4P = 0;
	public int sel4C = 0;
	public float sel5P = 0;
	public int sel5C = 0;
	
	public int buy1C = 0;
	public float buy1P = 0;
	public float buy2P = 0;
	public int buy2C = 0;
	public float buy3P = 0;
	public int buy3C = 0;
	public float buy4P = 0;
	public int buy4C = 0;
	public float buy5P = 0;
	public int buy5C = 0;
	String currentTime;
	String baseURL = "http://dp.sina.cn/dpool/stock_new/v2/wdpk.php?code=";
	String endURL = "&vt=4";
	String fullURL = null;
	GetAllStockInfoDAOImpl getAllStockInfoDAOImpl = null;
//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		String url = "http://dp.sina.cn/dpool/stock_new/v2/wdpk.php?code=sz000514&vt=4";
//		Document doc = Jsoup.connect(url).get();
//		Elements elements = doc.getElementsContainingOwnText("卖五价:");
//		String str = elements.get(0).text();
//		System.out.println(elements.get(0).text());
//		setDate(str);
//	}
	
	CollectDataBase (String stockCode,String stockName,GetAllStockInfoDAOImpl getAllStockInfoDAOImpl) {
		String temp = null;
		this.stockCode = stockCode;
		if(stockCode.startsWith("6")) {
			temp = "sh" + stockCode;
			
		} else {
			temp = "sz" + stockCode;
		}
		fullURL = baseURL + temp + endURL;
		this.stockName = stockName;
		this.getAllStockInfoDAOImpl = getAllStockInfoDAOImpl;
	}
	
	public void pullData() throws IOException{
		try{
		Document doc = Jsoup.connect(fullURL).get();
		Elements elements = doc.getElementsContainingOwnText("卖五价:");
		String str = elements.get(0).text();
		setDate(str);
		} catch (Exception e) {
		}
	}
	
	public void run(){
		try {
			pullData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  int setDate(String strData) {		
		int ps = strData.indexOf("当前价:");
		int pe = strData.indexOf("30秒刷新");
		String str1 = strData.substring(ps, pe);
		//System.out.println(str1);
		//int priceLength ="当前价:".length();
		String strPrivice = str1.substring(4, str1.indexOf("涨跌额:"));
		//当前价:
		try{
		if(!strPrivice.trim().contains("停牌")) {
		price = Float.parseFloat(strPrivice.trim());
		//System.out.println("当前价：" + price);
		String strChangePrice = str1.substring(str1.indexOf("涨跌额:")+4, str1.indexOf("涨跌幅:"));
		//涨跌额
		changePrice = Float.parseFloat(strChangePrice.trim());
		//System.out.println("涨跌额:" + changePrice);
		//涨跌幅
		
		String temp1 = str1.substring(str1.indexOf("涨跌幅:")+4, str1.indexOf("换手率:"));
		changeRatePer = Float.parseFloat(temp1.substring(0, temp1.indexOf("%")))/100;
		//System.out.println("涨跌幅:" + changeRatePer);
		//换手率
		temp1 = str1.substring(str1.indexOf("换手率:")+4, str1.indexOf("成交量:"));
		turnOverPer = Float.parseFloat(temp1.substring(0, temp1.indexOf("%")))/100;
		//System.out.println("换手率:" + turnOverPer);
		//成交量
		String dealMount = str1.substring(str1.indexOf("成交量:")+4, str1.indexOf("股"));
		mount = Integer.parseInt(dealMount.replace(",", ""));
		//System.out.println("成交量:" + mount);
		//成交额
		String dealCount = str1.substring(str1.indexOf("成交额:")+4, str1.indexOf("元"));
		count = Integer.parseInt(dealCount.replace(",", ""));
		//System.out.println("成交额:" + count);
		//更新时间:
		String currentTimeShort = str1.substring(str1.indexOf("更新时间:")+5, str1.indexOf("五档盘口"));
		Calendar c = Calendar.getInstance();
		currentTime = String.valueOf(c.get(Calendar.YEAR)) + "-" + currentTimeShort.trim();
		//System.out.println("更新时间:" + currentTime);
		//卖五价
		String sel5PStr = str1.substring(str1.indexOf("卖五价:"), str1.length());
		sel5P = Float.parseFloat(sel5PStr.substring(sel5PStr.indexOf("卖五价:")+4, sel5PStr.indexOf("量:")));
		//System.out.println("卖五价:" + sel5P);
		//卖五量
		sel5C = Integer.parseInt(sel5PStr.substring(sel5PStr.indexOf("量:")+2, sel5PStr.indexOf("卖四价:")).trim());
		//System.out.println("卖五量:" + sel5M);
		//卖四价
		String sel4PStr = str1.substring(str1.indexOf("卖四价:"), str1.length());
		sel4P = Float.parseFloat(sel4PStr.substring(sel4PStr.indexOf("卖四价:")+4, sel4PStr.indexOf("量:")));
		//System.out.println("卖四价:" + sel4P);
		//卖四量
		sel4C = Integer.parseInt(sel4PStr.substring(sel4PStr.indexOf("量:")+2, sel4PStr.indexOf("卖三价:")).trim());
		//System.out.println("卖四量:" + sel4M);
		//卖三价
		String sel3PStr = str1.substring(str1.indexOf("卖三价:"), str1.length());
		sel3P = Float.parseFloat(sel3PStr.substring(sel3PStr.indexOf("卖三价:")+4, sel3PStr.indexOf("量:")));
		//System.out.println("卖三价:" + sel3P);
		//卖三量
		sel3C = Integer.parseInt(sel3PStr.substring(sel3PStr.indexOf("量:")+2, sel3PStr.indexOf("卖二价:")).trim());
		//System.out.println("卖三量:" + sel3M);
		//卖二价
		String sel2PStr = str1.substring(str1.indexOf("卖二价:"), str1.length());
		sel2P = Float.parseFloat(sel2PStr.substring(sel2PStr.indexOf("卖二价:")+4, sel2PStr.indexOf("量:")));
		//System.out.println("卖二价:" + sel2P);
		//卖二量
		sel2C = Integer.parseInt(sel2PStr.substring(sel2PStr.indexOf("量:")+2, sel2PStr.indexOf("卖一价:")).trim());
		//System.out.println("卖二量:" + sel2M);
		//卖一价
		String sel1PStr = str1.substring(str1.indexOf("卖一价:"), str1.length());
		sel1P = Float.parseFloat(sel1PStr.substring(sel1PStr.indexOf("卖一价:")+4, sel1PStr.indexOf("量:")));
		//System.out.println("卖一价:" + sel1P);
		//卖一量
		sel1C = Integer.parseInt(sel1PStr.substring(sel1PStr.indexOf("量:")+2, sel1PStr.indexOf("买一价:")).trim());
		//System.out.println("卖一量:" + sel1M);
		
		//买一价
		String buy1PStr = str1.substring(str1.indexOf("买一价:"), str1.length());
		buy1P = Float.parseFloat(buy1PStr.substring(buy1PStr.indexOf("买一价:")+4, buy1PStr.indexOf("量:")));
		//System.out.println("买一价:" + buy1P);
		//买一量
		buy1C = Integer.parseInt(buy1PStr.substring(buy1PStr.indexOf("量:")+2, buy1PStr.indexOf("买二价:")).trim());
		//System.out.println("买一量:" + buy1M);
		
		//买二价
		String buy2PStr = str1.substring(str1.indexOf("买二价:"), str1.length());
		buy2P = Float.parseFloat(buy2PStr.substring(buy2PStr.indexOf("买二价:")+4, buy2PStr.indexOf("量:")));
		//System.out.println("买二价:" + buy2P);
		//买二量
		buy2C = Integer.parseInt(buy2PStr.substring(buy2PStr.indexOf("量:")+2, buy2PStr.indexOf("买三价:")).trim());
		//System.out.println("买二量:" + buy2M);
		
		//买三价
		String buy3PStr = str1.substring(str1.indexOf("买三价:"), str1.length());
		buy3P = Float.parseFloat(buy3PStr.substring(buy3PStr.indexOf("买三价:")+4, buy3PStr.indexOf("量:")));
		//System.out.println("买三价:" + buy3P);
		//买三量
		buy3C = Integer.parseInt(buy3PStr.substring(buy3PStr.indexOf("量:")+2, buy3PStr.indexOf("买四价:")).trim());
		//System.out.println("买三量:" + buy3M);
		
		//买四价
		String buy4PStr = str1.substring(str1.indexOf("买四价:"), str1.length());
		buy4P = Float.parseFloat(buy4PStr.substring(buy4PStr.indexOf("买四价:")+4, buy4PStr.indexOf("量:")));
		//System.out.println("买四价:" + buy4P);
		//买四量
		buy4C = Integer.parseInt(buy4PStr.substring(buy4PStr.indexOf("量:")+2, buy4PStr.indexOf("买五价:")).trim());
		//System.out.println("买四量:" + buy4M);
		
		//买五价
		String buy5PStr = str1.substring(str1.indexOf("买五价:"), str1.length());
		buy5P = Float.parseFloat(buy5PStr.substring(buy5PStr.indexOf("买五价:")+4, buy5PStr.indexOf("量:")));
		//System.out.println("买五价:" + buy5P);
		//买五量
		buy5C = Integer.parseInt(buy5PStr.substring(buy5PStr.indexOf("量:")+2, buy5PStr.length()).trim());
		//System.out.println("买五量:" + buy5M);
		float sellAll = sel1C + sel2C + sel3C + sel4C + sel5C;
		float buyAll = buy1C + buy2C + buy3C + buy4C + buy5C;
		if(sellAll == 0 || buyAll == 0){
			return 0;
		}
		if(sellAll * rate <= buyAll && buyAll > sellAll){
			long ps1 = System.currentTimeMillis();
			System.out.println(new Date(ps1)+"正在处理 买单 : " + stockCode);
			System.out.println(fullURL);
			saveStock5PC(1);
			return 1;}
		if(sellAll  >= buyAll * rate && buyAll < sellAll){
			long ps1 = System.currentTimeMillis();
			System.out.println(new Date(ps1)+"正在处理 卖单: " + stockCode);
			System.out.println(fullURL);
			saveStock5PC(-1);
			return -1;
		}
		}
		
		} catch (Exception e) {
			return 0;
		}
		
		return 0;
	}
	
	private void saveStock5PC(int direction){
		Stock5PC stock5PC = new Stock5PC();
		stock5PC.setStockID(stockCode);
		stock5PC.setStockName(stockName);
		stock5PC.setDirection(direction);
		stock5PC.setSaveTime(currentTime);
		stock5PC.setSel1P(sel1P);
		stock5PC.setSel2P(sel2P);
		stock5PC.setSel3P(sel3P);
		stock5PC.setSel4P(sel4P);
		stock5PC.setSel5P(sel5P);
		stock5PC.setSel1C(sel1C);
		stock5PC.setSel2C(sel2C);
		stock5PC.setSel3C(sel3C);
		stock5PC.setSel4C(sel4C);
		stock5PC.setSel5C(sel5C);
		stock5PC.setBuy1C(buy1C);
		stock5PC.setBuy2C(buy2C);
		stock5PC.setBuy3C(buy3C);
		stock5PC.setBuy4C(buy4C);
		stock5PC.setBuy5C(buy5C);
		stock5PC.setBuy1P(buy1P);
		stock5PC.setBuy2P(buy2P);
		stock5PC.setBuy3P(buy3P);
		stock5PC.setBuy4P(buy4P);
		stock5PC.setBuy5P(buy5P);
		try{
			getAllStockInfoDAOImpl.saveStock5PC(stock5PC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取所有的股票基础信息
	}

}

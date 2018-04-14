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
	public int rate = 3; // �ǵ�����
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
//		Elements elements = doc.getElementsContainingOwnText("�����:");
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
		Elements elements = doc.getElementsContainingOwnText("�����:");
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
		int ps = strData.indexOf("��ǰ��:");
		int pe = strData.indexOf("30��ˢ��");
		String str1 = strData.substring(ps, pe);
		//System.out.println(str1);
		//int priceLength ="��ǰ��:".length();
		String strPrivice = str1.substring(4, str1.indexOf("�ǵ���:"));
		//��ǰ��:
		try{
		if(!strPrivice.trim().contains("ͣ��")) {
		price = Float.parseFloat(strPrivice.trim());
		//System.out.println("��ǰ�ۣ�" + price);
		String strChangePrice = str1.substring(str1.indexOf("�ǵ���:")+4, str1.indexOf("�ǵ���:"));
		//�ǵ���
		changePrice = Float.parseFloat(strChangePrice.trim());
		//System.out.println("�ǵ���:" + changePrice);
		//�ǵ���
		
		String temp1 = str1.substring(str1.indexOf("�ǵ���:")+4, str1.indexOf("������:"));
		changeRatePer = Float.parseFloat(temp1.substring(0, temp1.indexOf("%")))/100;
		//System.out.println("�ǵ���:" + changeRatePer);
		//������
		temp1 = str1.substring(str1.indexOf("������:")+4, str1.indexOf("�ɽ���:"));
		turnOverPer = Float.parseFloat(temp1.substring(0, temp1.indexOf("%")))/100;
		//System.out.println("������:" + turnOverPer);
		//�ɽ���
		String dealMount = str1.substring(str1.indexOf("�ɽ���:")+4, str1.indexOf("��"));
		mount = Integer.parseInt(dealMount.replace(",", ""));
		//System.out.println("�ɽ���:" + mount);
		//�ɽ���
		String dealCount = str1.substring(str1.indexOf("�ɽ���:")+4, str1.indexOf("Ԫ"));
		count = Integer.parseInt(dealCount.replace(",", ""));
		//System.out.println("�ɽ���:" + count);
		//����ʱ��:
		String currentTimeShort = str1.substring(str1.indexOf("����ʱ��:")+5, str1.indexOf("�嵵�̿�"));
		Calendar c = Calendar.getInstance();
		currentTime = String.valueOf(c.get(Calendar.YEAR)) + "-" + currentTimeShort.trim();
		//System.out.println("����ʱ��:" + currentTime);
		//�����
		String sel5PStr = str1.substring(str1.indexOf("�����:"), str1.length());
		sel5P = Float.parseFloat(sel5PStr.substring(sel5PStr.indexOf("�����:")+4, sel5PStr.indexOf("��:")));
		//System.out.println("�����:" + sel5P);
		//������
		sel5C = Integer.parseInt(sel5PStr.substring(sel5PStr.indexOf("��:")+2, sel5PStr.indexOf("���ļ�:")).trim());
		//System.out.println("������:" + sel5M);
		//���ļ�
		String sel4PStr = str1.substring(str1.indexOf("���ļ�:"), str1.length());
		sel4P = Float.parseFloat(sel4PStr.substring(sel4PStr.indexOf("���ļ�:")+4, sel4PStr.indexOf("��:")));
		//System.out.println("���ļ�:" + sel4P);
		//������
		sel4C = Integer.parseInt(sel4PStr.substring(sel4PStr.indexOf("��:")+2, sel4PStr.indexOf("������:")).trim());
		//System.out.println("������:" + sel4M);
		//������
		String sel3PStr = str1.substring(str1.indexOf("������:"), str1.length());
		sel3P = Float.parseFloat(sel3PStr.substring(sel3PStr.indexOf("������:")+4, sel3PStr.indexOf("��:")));
		//System.out.println("������:" + sel3P);
		//������
		sel3C = Integer.parseInt(sel3PStr.substring(sel3PStr.indexOf("��:")+2, sel3PStr.indexOf("������:")).trim());
		//System.out.println("������:" + sel3M);
		//������
		String sel2PStr = str1.substring(str1.indexOf("������:"), str1.length());
		sel2P = Float.parseFloat(sel2PStr.substring(sel2PStr.indexOf("������:")+4, sel2PStr.indexOf("��:")));
		//System.out.println("������:" + sel2P);
		//������
		sel2C = Integer.parseInt(sel2PStr.substring(sel2PStr.indexOf("��:")+2, sel2PStr.indexOf("��һ��:")).trim());
		//System.out.println("������:" + sel2M);
		//��һ��
		String sel1PStr = str1.substring(str1.indexOf("��һ��:"), str1.length());
		sel1P = Float.parseFloat(sel1PStr.substring(sel1PStr.indexOf("��һ��:")+4, sel1PStr.indexOf("��:")));
		//System.out.println("��һ��:" + sel1P);
		//��һ��
		sel1C = Integer.parseInt(sel1PStr.substring(sel1PStr.indexOf("��:")+2, sel1PStr.indexOf("��һ��:")).trim());
		//System.out.println("��һ��:" + sel1M);
		
		//��һ��
		String buy1PStr = str1.substring(str1.indexOf("��һ��:"), str1.length());
		buy1P = Float.parseFloat(buy1PStr.substring(buy1PStr.indexOf("��һ��:")+4, buy1PStr.indexOf("��:")));
		//System.out.println("��һ��:" + buy1P);
		//��һ��
		buy1C = Integer.parseInt(buy1PStr.substring(buy1PStr.indexOf("��:")+2, buy1PStr.indexOf("�����:")).trim());
		//System.out.println("��һ��:" + buy1M);
		
		//�����
		String buy2PStr = str1.substring(str1.indexOf("�����:"), str1.length());
		buy2P = Float.parseFloat(buy2PStr.substring(buy2PStr.indexOf("�����:")+4, buy2PStr.indexOf("��:")));
		//System.out.println("�����:" + buy2P);
		//�����
		buy2C = Integer.parseInt(buy2PStr.substring(buy2PStr.indexOf("��:")+2, buy2PStr.indexOf("������:")).trim());
		//System.out.println("�����:" + buy2M);
		
		//������
		String buy3PStr = str1.substring(str1.indexOf("������:"), str1.length());
		buy3P = Float.parseFloat(buy3PStr.substring(buy3PStr.indexOf("������:")+4, buy3PStr.indexOf("��:")));
		//System.out.println("������:" + buy3P);
		//������
		buy3C = Integer.parseInt(buy3PStr.substring(buy3PStr.indexOf("��:")+2, buy3PStr.indexOf("���ļ�:")).trim());
		//System.out.println("������:" + buy3M);
		
		//���ļ�
		String buy4PStr = str1.substring(str1.indexOf("���ļ�:"), str1.length());
		buy4P = Float.parseFloat(buy4PStr.substring(buy4PStr.indexOf("���ļ�:")+4, buy4PStr.indexOf("��:")));
		//System.out.println("���ļ�:" + buy4P);
		//������
		buy4C = Integer.parseInt(buy4PStr.substring(buy4PStr.indexOf("��:")+2, buy4PStr.indexOf("�����:")).trim());
		//System.out.println("������:" + buy4M);
		
		//�����
		String buy5PStr = str1.substring(str1.indexOf("�����:"), str1.length());
		buy5P = Float.parseFloat(buy5PStr.substring(buy5PStr.indexOf("�����:")+4, buy5PStr.indexOf("��:")));
		//System.out.println("�����:" + buy5P);
		//������
		buy5C = Integer.parseInt(buy5PStr.substring(buy5PStr.indexOf("��:")+2, buy5PStr.length()).trim());
		//System.out.println("������:" + buy5M);
		float sellAll = sel1C + sel2C + sel3C + sel4C + sel5C;
		float buyAll = buy1C + buy2C + buy3C + buy4C + buy5C;
		if(sellAll == 0 || buyAll == 0){
			return 0;
		}
		if(sellAll * rate <= buyAll && buyAll > sellAll){
			long ps1 = System.currentTimeMillis();
			System.out.println(new Date(ps1)+"���ڴ��� �� : " + stockCode);
			System.out.println(fullURL);
			saveStock5PC(1);
			return 1;}
		if(sellAll  >= buyAll * rate && buyAll < sellAll){
			long ps1 = System.currentTimeMillis();
			System.out.println(new Date(ps1)+"���ڴ��� ����: " + stockCode);
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
		// ��ȡ���еĹ�Ʊ������Ϣ
	}

}

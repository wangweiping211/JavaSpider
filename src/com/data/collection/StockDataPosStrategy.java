package com.data.collection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.data.bean.Stock5PC;
import com.data.bean.StockBean;
import com.data.bean.StockRate;
import com.data.dao.GetAllStockInfoDAOImpl;

public class StockDataPosStrategy {

	private GetAllStockInfoDAOImpl getAllStockInfoDAOImpl;

	public GetAllStockInfoDAOImpl getGetAllStockInfoDAOImpl() {
		return getAllStockInfoDAOImpl;
	}

	public void setGetAllStockInfoDAOImpl(
			GetAllStockInfoDAOImpl getAllStockInfoDAOImpl) {
		System.out.println("Test begin: setGetAllStockInfoDAOImpl");
		this.getAllStockInfoDAOImpl = getAllStockInfoDAOImpl;
	}

	public void processMain() {
		System.out.println("��ʼ����");
		List<Stock5PC> stock5PCList = getAllStockInfoDAOImpl.getStock5PC();
		Stock5PC perStock = null;
		int count = 1;
		for (int i = 0; i < stock5PCList.size(); i++) {
			Stock5PC currentStock = stock5PCList.get(i);

			if (perStock != null && currentStock != null
					&& perStock.getStockID().equals(currentStock.getStockID())) {
				if (currentStock.getDirection() == perStock.getDirection()) {
					System.out.println("��ʼ����" + perStock.getStockID());
					count = count + 1;
					processSeriesDirect(perStock,count);
				} else {
					count = 1;
				}
			} else {
				count = 1;
			}

			perStock = currentStock;
		}
		System.out.println("�������");

	}
	
	
	public void processDailyMain() {
		System.out.println("��ʼ����....");
		List<StockBean> ls= getAllStockInfoDAOImpl.selectAllQuartJob();
		if(ls == null || ls.size() == 0) {
			return;
		}
		for(int i=0;i < ls.size();i++) {
			ProcessDailyDealInfo dailyDeal = new ProcessDailyDealInfo(ls.get(i).getStockID(),getAllStockInfoDAOImpl);
			try{
				System.out.println("��ʼ����: "+ls.get(i).getStockID());
				dailyDeal.processmain();
			} catch(Exception e) {
				e.fillInStackTrace();
			}
		}
		System.out.println("�������");

	}

	// ���������㷨
	public void processSeriesDirect(Stock5PC currentStock, int count) {
		int baseRate = 1;
		String des = null;
		StockRate stockRate = new StockRate();
		stockRate.setStockID(currentStock.getStockID());
		stockRate.setSaveTime(currentStock.getSaveTime());
		stockRate.setStockName(currentStock.getStockName());
		if (currentStock.getDirection() == 1) {
			baseRate = 2;
			des = "����";
		} else {
			baseRate = 1;
			des = "����";
		}
		// ����3,5,8,13,21,34,55
		switch (count) {
		case 3: {
			stockRate.setDescribes(des + "����3");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 5: {
			stockRate.setDescribes(des + "����5");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 8: {
			stockRate.setDescribes(des + "����8");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 13: {
			stockRate.setDescribes(des + "����13");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 21: {
			stockRate.setDescribes(des + "����21");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 34: {
			stockRate.setDescribes(des + "����34");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		case 55: {
			stockRate.setDescribes(des + "����55");
			stockRate.setPoints(baseRate);
			getAllStockInfoDAOImpl.saveStockRate(stockRate);
			break;
		}
		}
	}
	
	public void processWebData() {
		long ms = System.currentTimeMillis();
		System.out.println(new Date(ms) + "JOB��ʼִ��~");
		
		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minutes = date.get(Calendar.MINUTE);
		System.out.println("Hour = " + hour);

		//9��35֮ǰ���ռ�����
		if(hour == 9 && minutes < 30) { 
			return;
		}
		if(hour == 11 && minutes > 30) {
			return;
		}				
		
		if (getAllStockInfoDAOImpl == null) {
			WebApplicationContext wac = ContextLoader
					.getCurrentWebApplicationContext();
			getAllStockInfoDAOImpl = (GetAllStockInfoDAOImpl) wac
					.getBean("getAllStockInfoDAOImpl");
		}
		List<StockBean> list = getAllStockInfoDAOImpl.selectAllQuartJob();
		ExecutorService exec = Executors.newFixedThreadPool(100);
		for (int i = 0; i < list.size(); i++) {
			StockBean stockBean = list.get(i);
				Runnable process = new CollectDealDetailBase(stockBean.getStockID(), stockBean
						.getStockName(), getAllStockInfoDAOImpl);
				exec.execute(process);
		}
		exec.shutdown();
	}
}

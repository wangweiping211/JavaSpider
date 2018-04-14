package com.data.collection;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.data.bean.StockBean;
import com.data.dao.GetAllStockInfoDAOImpl;

public class FivePositionScheduler_BAK extends QuartzJobBean {

	private GetAllStockInfoDAOImpl getAllStockInfoDAOImpl;

	public GetAllStockInfoDAOImpl getGetAllStockInfoDAOImpl() {
		return getAllStockInfoDAOImpl;
	}

	public void setGetAllStockInfoDAOImpl(
			GetAllStockInfoDAOImpl getAllStockInfoDAOImpl) {
		this.getAllStockInfoDAOImpl = getAllStockInfoDAOImpl;
	}

	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		long ms = System.currentTimeMillis();
		System.out.println(new Date(ms) + "JOB开始执行~");
		
		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minutes = date.get(Calendar.MINUTE);
		System.out.println("Hour = " + hour);

		//9点35之前不收集数据
		if(hour == 9 && minutes < 35) { 
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
		for (int i = 0; i < list.size(); i++) {
			StockBean stockBean = list.get(i);
			try {
				process(stockBean);
				Thread.currentThread().sleep(5);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
			}
		}
		System.out.println(list.size());
	}

	protected void process(StockBean bean) throws IOException {
		CollectDataBase coBase = new CollectDataBase(bean.getStockID(), bean
				.getStockName(), getAllStockInfoDAOImpl);
		coBase.start();
	}
}

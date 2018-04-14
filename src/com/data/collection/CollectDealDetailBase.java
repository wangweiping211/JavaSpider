package com.data.collection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.data.bean.Stock5PC;
import com.data.dao.GetAllStockInfoDAOImpl;

public class CollectDealDetailBase extends Thread{
	public int rate = 3; // 涨跌比率
	String stockCode;
	String stockName;
	
	String currentTime;
	String baseURL = "http://dp.sina.cn/dpool/stock_new/v2/cjmx.php?code=";
	String endURL = "&page=1&vt=4";
	String fullURL = null;
	GetAllStockInfoDAOImpl getAllStockInfoDAOImpl = null;
	public  void pullWebData() throws IOException {;
		Document doc = Jsoup.connect(fullURL).get();
		Elements elements = doc.getElementsContainingOwnText("性质");
		String str = elements.get(0).text();

		String filepath = ctreateFile(stockCode);
		processDate(str,filepath);
	}
	
	public  String ctreateFile(String stockCode) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String currentDate = df.format(new Date()); 
		//http://dp.sina.cn/dpool/stock_new/v2/cjmx.php?timer=30&code=sh600891&page=1
		String path = "D:\\DealDetail";
		String year = currentDate.substring(0, 4);
		String month = currentDate.substring(4,6);
		String fullPath = path + File.separator + stockCode + File.separator + year + File.separator + month;
		File folder = new File(fullPath);
		
		if(!folder.exists()  && !folder.isDirectory())      
		{       
		    folder.mkdirs();    
		}
		File file = new File(fullPath + File.separator + currentDate+".txt");
		if(!file.exists()){
			file.createNewFile();
		}
		return file.getPath();
	}
	
	private  void processDate(String dataDetail,String filePath) throws IOException{
		int startIndex = dataDetail.indexOf("性质");
		int endIndex = dataDetail.indexOf("下页");
		String preData = dataDetail.substring(startIndex+"性质".length(), endIndex);
		List<String> list = new ArrayList<String>();
		while(getIndex(preData) != -1){
			int place = getIndex(preData);
			String putDetail =  preData.substring(0, place);
			list.add(putDetail);
			preData = preData.substring(place,preData.length());
		}
		String lastLine = readLastLine(new File(filePath),null).trim();
       
		BufferedWriter br = new BufferedWriter(new FileWriter(filePath,true));
		if(!"".equalsIgnoreCase(lastLine.trim()) && list.size() > 0 ) br.write("\r\n");
		List<String> inputString = deleteDuplicateDate(list,lastLine);
		for(int i= 0; i < inputString.size();i++) {
			if( i != inputString.size()-1 ) {
				br.write(inputString.get(i)+"\r\n");
			} else {
				br.write(inputString.get(i));
			}
		}
		br.flush();
		br.close();
	}
	
	private  List<String> deleteDuplicateDate(List<String> input,String line){
		List<String> outString = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String lastTime = "00:00:00";
		if(line.length() > 9) {
			lastTime = line.substring(0,8);
		}

		for(int i=input.size()-1; i >= 0;i--) {
			String time = input.get(i).substring(1,9);
			try {
				if(df.parse(lastTime).getTime() < df.parse(time).getTime()){
					outString.add(input.get(i));
				} 
				if(df.parse(lastTime).getTime() == df.parse(time).getTime()) {
					if(!line.trim().equals(input.get(i).trim())){
						outString.add(input.get(i));
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return outString;
	}
	
	private static int getIndex(String preData) {
		int a = preData.indexOf("买盘");
		int b = preData.indexOf("卖盘");
		int c = preData.indexOf("中性盘");
		int d = preData.indexOf("--");
		if(a == -1 && b== -1 && c== -1) return -1;
		int minIndex = 99999;
		if(a > -1 ) minIndex = a + "买盘".length();
		if(b > -1 && b < minIndex)  minIndex = b + "卖盘".length();
		if(c > -1 && c < minIndex)  minIndex = c + "中性盘".length();
		if(d > -1 && d < minIndex)  minIndex = d+ "--".length();
		if(minIndex == 99999) minIndex = -1;
		return minIndex;
	}
	
	public static String readLastLine(File file, String charset) throws IOException {  
		  if (!file.exists() || file.isDirectory() || !file.canRead()) {  
		    return null;  
		  }  
		  RandomAccessFile raf = null;  
		  try {  
		    raf = new RandomAccessFile(file, "r");  
		    long len = raf.length();  
		    if (len == 0L) {  
		      return "";  
		    } else {  
		      long pos = len - 1;  
		      while (pos > 0) {  
		        pos--;  
		        raf.seek(pos);  
		        if (raf.readByte() == '\n') {  
		          break;  
		        }  
		      }  
		      if (pos == 0) {  
		        raf.seek(0);  
		      }  
		      byte[] bytes = new byte[(int) (len - pos)];  
		      raf.read(bytes);  
		      if (charset == null) {  
		        return new String(bytes);  
		      } else {  
		        return new String(bytes, charset);  
		      }  
		    }  
		  } catch (FileNotFoundException e) {  
		  } finally {  
		    if (raf != null) {  
		      try {  
		        raf.close();  
		      } catch (Exception e2) {  
		      }  
		    }  
		  }  
		  return null;  
		}
	
	CollectDealDetailBase (String stockCode,String stockName,GetAllStockInfoDAOImpl getAllStockInfoDAOImpl) {
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
	

	
	public void run(){
		try {
			pullWebData();
		} catch (Exception e) {
			System.out.println(fullURL);
			e.printStackTrace();
		}
	}
}

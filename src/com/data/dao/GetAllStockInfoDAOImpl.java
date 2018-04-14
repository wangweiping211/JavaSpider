package com.data.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.data.bean.Stock5PC;
import com.data.bean.StockBean;
import com.data.bean.StockBeanRich;
import com.data.bean.StockDaily;
import com.data.bean.StockRate;

public class GetAllStockInfoDAOImpl {
	
	
	/**
	 * 定时任务的DAO
	 * 
	 * @author wang
	 * 
	 */
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		System.out.println("开始注入---GetAllStockInfoDAOImpl");
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * 查找所有的定时任务
	 * 
	 * @return
	 */
	public List<StockBean> selectAllQuartJob() {
		String sql = "SELECT stockID,stockName,category,area,openDate from stockbase where openDate <> '0000-00-00 00:00:00';";
		// 获取所有的股票基础信息
		if(namedParameterJdbcTemplate == null) {
			System.out.println("注入失败");
		}
		List<StockBean> list = namedParameterJdbcTemplate.query(sql,
				new HashMap<String, String>(),
				new BeanPropertyRowMapper<StockBean>(StockBean.class));
		//List list = namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, String>());
		return list;
	}
	
	public int saveStock5PC(Stock5PC stock5PC){
		String sql = "insert into stock5PC(stockID,stockName,direction,saveTime,sel1P,sel2P,sel3P,sel4P,sel5P,sel1C,sel2C,sel3C,sel4C,sel5C,buy1P,buy2P,buy3P,buy4P,buy5P,buy1C,buy2C,buy3C,buy4C,buy5C)"
			+ "values(:stockID,:stockName,:direction,:saveTime,:sel1P,:sel2P,:sel3P,:sel4P,:sel5P,:sel1C,:sel2C,:sel3C,:sel4C,:sel5C,:buy1P,:buy2P,:buy3P,:buy4P,:buy5P,:buy1C,:buy2C,:buy3C,:buy4C,:buy5C)";
		
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(stock5PC);
		return namedParameterJdbcTemplate.update(sql, paramSource);
	}
	
	/**
	 * 查找所有的股票代码
	 * 
	 * @return
	 */
	public List<Stock5PC> getStock5PC() {
		String sql = "SELECT stockID,stockName,direction,saveTime,sel1P,sel2P,sel3P,sel4P,sel5P,sel1C,sel2C,sel3C,sel4C,sel5C,buy1P,buy2P,buy3P,buy4P,buy5P,buy1C,buy2C,buy3C,buy4C,buy5C from stock5PC where saveTime > :saveTime ORDER BY stockID";
		// 获取所有的股票基础信息
		if(namedParameterJdbcTemplate == null) {
			System.out.println("注入失败");
		}
		Map<String, String> map = new HashMap<String, String>();
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Process date:"+df.format(date.getTime()));
		map.put("saveTime", df.format(date.getTime()));
		List<Stock5PC> list = namedParameterJdbcTemplate.query(sql,
				map,
				new BeanPropertyRowMapper<Stock5PC>(Stock5PC.class));
		//List list = namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, String>());
		return list;
	}
	
	/*
	 * 设置股票积分
	 */
	public int saveStockRate(StockRate stockRate){
		String sql = "insert into stockRate(stockID,stockName,saveTime,describes,points)"
			+ "values(:stockID,:stockName,:saveTime,:describes,:points)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(stockRate);
		return namedParameterJdbcTemplate.update(sql, paramSource);
	}
	
	/**
	 * 查找单个股票的信息
	 * 
	 * @return
	 */
	public List<StockBeanRich> queryStockByID(String stockID) {
		String sql = "SELECT stockID,stockName,category,area,openDate,floatStock,allStock from stockbaserich where stockID = :stockID;";
		// 获取所有的股票基础信息
		if(namedParameterJdbcTemplate == null) {
			System.out.println("注入失败");
		}
		HashMap<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("stockID", stockID);
		List<StockBeanRich> list = namedParameterJdbcTemplate.query(sql,
				queryMap,
				new BeanPropertyRowMapper<StockBeanRich>(StockBeanRich.class));
		//List list = namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, String>());
		return list;
	}
	
	
	/*
	 * 设置股票日K线数据
	 */
	public int saveStockDaily(StockDaily stockDaily){
		String sql = "insert into stockDaily(stockID,stockName,beginPrice,lowPrice,highPrice,closePrice,counts,mount,saveTime,exchangeRite)"
			+ "values(:stockID,:stockName,:beginPrice,:lowPrice,:highPrice,:closePrice,:counts,:mount,:saveTime,:exchangeRite)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(stockDaily);
		return namedParameterJdbcTemplate.update(sql, paramSource);
	}
}

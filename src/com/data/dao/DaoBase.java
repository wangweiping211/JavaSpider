/**
 * 
 */
package com.data.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


public abstract class DaoBase extends JdbcDaoSupport {
	private DataSourceTransactionManager transactionManager;

	public DataSourceTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(
			DataSourceTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

}


package com.taotu51.topclient.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taotu51.topclient.dao.CachedItemsDataDAO;
import com.taotu51.topclient.model.CachedItemsData;



public class CachedItemsDataService {
	private CachedItemsDataDAO cachedItemsDataDAO;
	
	@Transactional(readOnly = false)
	public void add(CachedItemsData cachedItemsData) {
		cachedItemsDataDAO.add(cachedItemsData);	
	}

	@Transactional(readOnly = false)
	public void update(CachedItemsData cachedItemsData) {
		cachedItemsDataDAO.update(cachedItemsData);
	}

	@Transactional(readOnly = true)
	public List<CachedItemsData> get(String numiid, String userNick) {
		return cachedItemsDataDAO.get(numiid,userNick);
	}

	@Transactional(readOnly = false)
	public void delete(String numiid, String userNick) {
		cachedItemsDataDAO.delete(numiid,userNick);
	}
	
	public CachedItemsDataDAO getCachedItemsDataDAO() {
		return cachedItemsDataDAO;
	}

	public void setCachedItemsDataDAO(CachedItemsDataDAO cachedItemsDataDAO) {
		this.cachedItemsDataDAO = cachedItemsDataDAO;
	}


}

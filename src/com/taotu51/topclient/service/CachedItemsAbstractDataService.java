package com.taotu51.topclient.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taotu51.topclient.dao.CachedItemsAbstractDataDAO;
import com.taotu51.topclient.model.CachedItemsAbstractData;



public class CachedItemsAbstractDataService {
	private CachedItemsAbstractDataDAO cachedItemsAbstractDataDAO;
	
	@Transactional(readOnly = false)
	public void add(CachedItemsAbstractData cachedItemsAbstractData) {
		cachedItemsAbstractDataDAO.add(cachedItemsAbstractData);	
	}

	@Transactional(readOnly = false)
	public void update(CachedItemsAbstractData cachedItemsAbstractData) {
		cachedItemsAbstractDataDAO.update(cachedItemsAbstractData);
	}

	@Transactional(readOnly = true)
	public List<CachedItemsAbstractData> get(String numiid) {
		return cachedItemsAbstractDataDAO.get(numiid);
	}

	public CachedItemsAbstractDataDAO getCachedItemsAbstractDataDAO() {
		return cachedItemsAbstractDataDAO;
	}

	public void setCachedItemsAbstractDataDAO(CachedItemsAbstractDataDAO cachedItemsAbstractDataDAO) {
		this.cachedItemsAbstractDataDAO = cachedItemsAbstractDataDAO;
	}


}

package com.taotu51.topclient.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taotu51.topclient.dao.CachedItemsDAO;
import com.taotu51.topclient.model.CachedItems;



public class CachedItemsService {
	private CachedItemsDAO cachedItemsDAO;
	
	@Transactional(readOnly = false)
	public void add(CachedItems CachedItems) {
		cachedItemsDAO.add(CachedItems);
		
	}

	@Transactional(readOnly = false)
	public void update(CachedItems cachedItems) {
		cachedItemsDAO.update(cachedItems);
	}

	@Transactional(readOnly = true)
	public List<CachedItems> get(String leafNodeCid, String userNick) {
		return cachedItemsDAO.get(leafNodeCid, userNick);
	}

	@Transactional(readOnly = true)
	public List<String> list() {
		return cachedItemsDAO.list();
	}
	
	public CachedItemsDAO getCachedItemsDAO() {
		return cachedItemsDAO;
	}

	public void setCachedItemsDAO(CachedItemsDAO cachedItemsDAO) {
		this.cachedItemsDAO = cachedItemsDAO;
	}	
}

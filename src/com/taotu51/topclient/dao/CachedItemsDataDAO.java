package com.taotu51.topclient.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.taotu51.topclient.model.CachedItemsData;


public class CachedItemsDataDAO {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(CachedItemsData cachedItemsData) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItemsData);		
	}
	
	public List<CachedItemsData> get(String numiid, String userNick) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("from CachedItemsData u where u.numiid = '" + numiid + "' and u.userNick = '" + userNick + "'");
		@SuppressWarnings("unchecked")
		List<CachedItemsData> CachedItemsDataList = query.list();
		return CachedItemsDataList;
	}

	public void delete(String numiid, String userNick) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("delete from CachedItemsData u where u.numiid = '" + numiid + "' and u.userNick = '" + userNick + "'");
		query.executeUpdate();
	}
	
	public void update(CachedItemsData cachedItemsData) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItemsData);
	}

}

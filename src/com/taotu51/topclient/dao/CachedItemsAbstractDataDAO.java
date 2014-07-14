package com.taotu51.topclient.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.taotu51.topclient.model.CachedItemsAbstractData;


public class CachedItemsAbstractDataDAO {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(CachedItemsAbstractData cachedItemsAbstractData) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItemsAbstractData);		
	}
	
	public List<CachedItemsAbstractData> get(String numiid) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("from CachedItemsAbstractData u where u.numiid = '" + numiid +"'");
		@SuppressWarnings("unchecked")
		List<CachedItemsAbstractData> CachedItemsAbstractDataList = query.list();
		return CachedItemsAbstractDataList;
	}

	public void delete(String numiid) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("delete from CachedItemsAbstractData u where u.numiid = '" + numiid +"'");
		query.executeUpdate();
	}
	
	public void update(CachedItemsAbstractData cachedItemsAbstractData) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItemsAbstractData);
	}

}

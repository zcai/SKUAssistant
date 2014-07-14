package com.taotu51.topclient.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.taotu51.topclient.model.CachedItems;


public class CachedItemsDAO {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(CachedItems cachedItems) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItems);		
	}
	
	public List<CachedItems> get(String leafNodeCid, String userNick) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("from CachedItems u where u.leafNodeCid = '" + leafNodeCid + "' and u.userNick = '" + userNick + "'");
		@SuppressWarnings("unchecked")
		List<CachedItems> userInfo = query.list();
		return userInfo;
	}

	public List<String> list() {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("select leafNodeCid from CachedItems u");
		@SuppressWarnings("unchecked")
		List<String> userInfo = query.list();
		return userInfo;
	}
	
	public void delete(String leafNodeCid) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("delete from CachedItems u where u.leafNodeCid = '" + leafNodeCid +"'");
		query.executeUpdate();
	}
	
	public void update(CachedItems cachedItems) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cachedItems);
	}

}

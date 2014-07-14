package com.taotu51.topclient.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.taotu51.topclient.dao.IUserInfoDAO;
import com.taotu51.topclient.model.UserInfo;


public class UserInfoDAO implements IUserInfoDAO {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<UserInfo> search(String userNick) {
		Session session = getSessionFactory().getCurrentSession();//transaction enabled
		Query query = session.createQuery("from UserInfo u where u.userNick = '" + userNick +"'");
		@SuppressWarnings("unchecked")
		List<UserInfo> userInfo = query.list();
		return userInfo;
	}
	
	@Override
	public void updateUserInfo(UserInfo userInfo) {
		getSessionFactory().getCurrentSession().saveOrUpdate(userInfo);
	}

}

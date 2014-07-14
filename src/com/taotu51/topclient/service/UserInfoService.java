package com.taotu51.topclient.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taotu51.topclient.dao.IUserInfoDAO;
import com.taotu51.topclient.model.UserInfo;


public class UserInfoService {
	IUserInfoDAO userInfoDAO;

	@Transactional(readOnly = true)
	public List<UserInfo> search(String userNick) {
		return userInfoDAO.search(userNick);
	}

	@Transactional(readOnly = false)
	public void save(UserInfo userInfo) {
		userInfoDAO.updateUserInfo(userInfo);
	}
	
	@Transactional(readOnly = false)
	public void updateUserInfo(UserInfo userInfo) {
		userInfoDAO.updateUserInfo(userInfo);
	}
	
	public IUserInfoDAO getUserInfoDAO() {
		return userInfoDAO;
	}

	public void setUserInfoDAO(IUserInfoDAO userInfoDAO) {
		this.userInfoDAO = userInfoDAO;
	}
	
	
}

package com.taotu51.topclient.dao;

import java.util.List;

import com.taotu51.topclient.model.UserInfo;


public interface IUserInfoDAO {
	public List<UserInfo> search(String userNick);
	public void updateUserInfo(UserInfo userInfo);

}

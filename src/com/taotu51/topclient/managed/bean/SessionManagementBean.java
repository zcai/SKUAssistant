package com.taotu51.topclient.managed.bean;

import java.io.Serializable;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="sessionManagementBean")
@SessionScoped
public class SessionManagementBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 448037505530491275L;
	private String sessionKey;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}

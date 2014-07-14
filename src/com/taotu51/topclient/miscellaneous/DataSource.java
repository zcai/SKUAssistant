package com.taotu51.topclient.miscellaneous;

import java.io.Serializable;

public class DataSource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3559856599246188473L;
	private String url;
	private String appkey;
	private String secret;
	private String sessionKey;
	private String nick;//“¡¬Â–°œÛ
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	
}

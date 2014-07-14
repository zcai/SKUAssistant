package com.taotu51.topclient.managed.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.taobao.api.internal.util.WebUtils;
import com.taotu51.topclient.miscellaneous.DataSource;


@ManagedBean
@ViewScoped
public class UserAuthorizationBean implements Serializable {

	private static final long serialVersionUID = 1517695889742278926L;

	@ManagedProperty(value = "#{userData}")
	private DataSource dataSource;
	
	private String initValue = "Loading data...";
	
	//@PostConstruct
	//public void init() {
	public void init(ComponentSystemEvent cse) {
		
		HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String code = request.getParameter("code");
		//curl -i -d "code=xMuBtklMeuxr0AujfjOZ3lht1&grant_type=authorization_code&client_id=12304977&client_secret=9288fb99669a14b6a9bf28e49b270f98&redirect_uri=http://www.oauth.net/2/ " https://oauth.taobao.com/token
		Map<String, String> param = new HashMap<String, String>();
		param.put("grant_type", "authorization_code");
		param.put("code", code);
		param.put("client_id", dataSource.getAppkey());
		param.put("client_secret", dataSource.getSecret());
		param.put("redirect_uri", "http://www.taotu51.com:8080/SKUAssistant/pages/index.jsf");
		param.put("view", "web");
		param.put("state", "");
		//String tbPostSessionUrl = "https://oauth.tbsandbox.com/token";//sandbox 
		String tbPostSessionUrl = "https://oauth.taobao.com/token";//access_tokenªÒ»°µÿ÷∑
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
		String responseTxt = null;
		try {
			responseTxt = WebUtils.doPost(tbPostSessionUrl, param, 3000, 3000);
		} catch (IOException e) {
			try {
				//replace client_id
				response.sendRedirect("https://oauth.taobao.com/authorize?response_type=code&client_id=217036xx&redirect_uri=http://www.taotu51.com:8080/SKUAssistant/pages/default.jsf");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
			JSONObject json = (JSONObject) JSONSerializer.toJSON(responseTxt);
			String sessionKey = json.getString("access_token");
			String nick = json.getString("taobao_user_nick");
			System.out.println(json.getString("taobao_user_nick"));
			System.out.println(json.getString("access_token"));
			try {
				response.sendRedirect("index.jsf?sessionKey="+sessionKey+"&nick="+nick);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public String getInitValue() {
		return initValue;
	}
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}
	
}

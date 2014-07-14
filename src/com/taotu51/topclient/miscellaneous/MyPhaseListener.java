package com.taotu51.topclient.miscellaneous;

import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.taotu51.topclient.managed.bean.SessionManagementBean;


public class MyPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 6957886252293748173L;

	@Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // Do your job here which should run right before the RENDER_RESPONSE.
    	//System.out.println("before phase==============================");
    	/*
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	Map<String, String> headers = externalContext.getRequestHeaderMap();
    	System.out.println(headers.get("X-Requested-With"));
    	boolean ajax = "XMLHttpRequest".equals(headers.get("X-Requested-With"));
    	*/
    	
    	
    	//HttpServletRequest httpRequest = (HttpServletRequest) event.getFacesContext().getExternalContext().getRequest();
    	
    	ExternalContext externalContext = event.getFacesContext().getExternalContext();
    	Map<String, String> headers = externalContext.getRequestHeaderMap();
    	String requestType = headers.get("X-Requested-With");//primefaces ajax call only, for jsf ajax call it is null, the below code needs modification????????
    	//System.out.println("request type========="+requestType);
    	boolean isAjaxCall = "XMLHttpRequest".equalsIgnoreCase(requestType);
    	
    	//String urlstr = httpRequest.getRequestURI().toString();
    	//System.out.println("******urlstr*********"+urlstr);
    	FacesContext context = FacesContext.getCurrentInstance();
    	SessionManagementBean sessionManagementBean = (SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagementBean}", SessionManagementBean.class);

    	//System.out.println("session key is========="+sessionManagementBean.getSessionKey());
    	if ((isAjaxCall) && (sessionManagementBean.getSessionKey() == null)) {
		try {
			UIViewRoot view = event.getFacesContext().getApplication().getViewHandler().createView(event.getFacesContext(), ""); 
			event.getFacesContext().setViewRoot(view);
			//System.out.println("xxxxxxxxxxxxxxxxxxxx");
	        ExternalContext extContext = event.getFacesContext().getExternalContext();
	        try {
	            extContext.redirect("https://oauth.taobao.com/authorize?response_type=code&client_id=21703655&redirect_uri=http://www.taotu51.com:8080/SKUAssistant/pages/index.jsf");
	            //extContext.redirect("https://oauth.tbsandbox.com/authorize?response_type=code&client_id=1021703655&redirect_uri=http://www.taotu51.com:8080/SKUAssistant/pages/default.jsf");
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
    	}
/*
    	if (isAjaxCall) {
    		//System.out.println(sessionManagementBean.getSessionKey());
        	//if (sessionManagementBean.getSessionKey() == null ) {
        		try {
        			UIViewRoot view = event.getFacesContext().getApplication().getViewHandler().createView(event.getFacesContext(), ""); 
        			event.getFacesContext().setViewRoot(view); 
        			System.out.println("xxxxxxxxxxxxxxxxxxxx");
        	        ExternalContext extContext = event.getFacesContext().getExternalContext();
        	        try {
        	            //extContext.redirect("https://oauth.taobao.com/authorize?response_type=token&client_id=21589200");
        	        } catch (Exception e) {
        	        	e.printStackTrace();
        	        }

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
        	//}
    	}
*/ 
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        // Do your job here which should run right after the RENDER_RESPONSE.
    	//System.out.println("after phase==============================");
    }

}
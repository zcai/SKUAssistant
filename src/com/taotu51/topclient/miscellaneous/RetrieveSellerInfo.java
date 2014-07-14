package com.taotu51.topclient.miscellaneous;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.response.UserSellerGetResponse;

public class RetrieveSellerInfo {
	public SellerInfo getSellerInfo(DataSource dataSource) {
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		UserSellerGetRequest req=new UserSellerGetRequest();
		req.setFields("user_id,nick,sex,seller_credit,type,has_more_pic,item_img_num,item_img_size,prop_img_num,prop_img_size,auto_repost,promoted_type,status,alipay_bind,consumer_protection,avatar,liangpin,sign_food_seller_promise,has_shop,is_lightning_consignment,has_sub_stock,is_golden_seller");
		String jsonTxt = "";
		SellerInfo sellerInfo = null;
		try {
			UserSellerGetResponse response = client.execute(req, dataSource.getSessionKey());
			jsonTxt = response.getBody();
			int count = 1;
			while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
				response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				if (count <= 0) {
					break;
				}
				count--;
				Thread.sleep(2000);
			}
			if (jsonTxt == null) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			else if (jsonTxt.contains("error")) {
		        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取卖家信息失败，请重试!");  
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	sellerInfo = new SellerInfo();
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt ); 
			    JSONObject sellerInfoObject = json.getJSONObject("user_seller_get_response").getJSONObject("user");
			    sellerInfo.setItemImgNum(sellerInfoObject.getInt("item_img_num"));
			    Integer [] tmparray = new Integer[sellerInfoObject.getInt("item_img_num")];
			    for(int j=0;j<tmparray.length;j++) {
			    	tmparray[j] = j;
			    }
			    sellerInfo.setItemImgNumList(Arrays.asList(tmparray));
			    sellerInfo.setItemImgSize(sellerInfoObject.getString("item_img_size"));
			    sellerInfo.setPropImgNum(sellerInfoObject.getInt("prop_img_num"));
			    sellerInfo.setPropImgSize(sellerInfoObject.getString("prop_img_size"));
			    System.out.println("response body from getSellerInfo ---------"+jsonTxt);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sellerInfo;
	}
	
	public class SellerInfo {
		private int itemImgNum;
		private List<Integer> itemImgNumList;
		private String itemImgSize;
		private int propImgNum;
		private String propImgSize;
		public int getItemImgNum() {
			return itemImgNum;
		}
		public void setItemImgNum(int itemImgNum) {
			this.itemImgNum = itemImgNum;
		}
		public String getItemImgSize() {
			return itemImgSize;
		}
		public void setItemImgSize(String itemImgSize) {
			this.itemImgSize = itemImgSize;
		}
		public int getPropImgNum() {
			return propImgNum;
		}
		public void setPropImgNum(int propImgNum) {
			this.propImgNum = propImgNum;
		}
		public String getPropImgSize() {
			return propImgSize;
		}
		public void setPropImgSize(String propImgSize) {
			this.propImgSize = propImgSize;
		}
		public List<Integer> getItemImgNumList() {
			return itemImgNumList;
		}
		public void setItemImgNumList(List<Integer> itemImgNumList) {
			this.itemImgNumList = itemImgNumList;
		}
		
	}
}

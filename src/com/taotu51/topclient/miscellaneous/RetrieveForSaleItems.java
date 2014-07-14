package com.taotu51.topclient.miscellaneous;

    import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

    import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

    import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemsListGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsListGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taotu51.topclient.miscellaneous.RetrieveItem.ItemData;


    public class RetrieveForSaleItems implements Serializable {

	private static final long serialVersionUID = -6936493254460834040L;

	public ForSaleItemsData retrieveForSaleItems(DataSource dataSource, String leafcid, String[] progressData, FacesMessage msg){ 
		progressData[0] = "开始获取在售商品信息...";
		System.out.println("retrieveForSaleItems begin");
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
		//req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id");
		req.setFields("num_iid,title,pic_url,num,price");
		req.setSellerCids(leafcid);
		//System.out.println(leafcid+"--------------------leafcid");
		req.setPageSize(200L);
		ForSaleItemsData forSaleItemsData = new ForSaleItemsData();
		Map<String,String> numiidToTitle = new HashMap<String,String>();
		Map<String,Double> numiidToPrice = new HashMap<String,Double>();
		Map<String,Integer> numiidToItemNum = new HashMap<String,Integer>();
		Map<String,String> numiidToPicUrl = new HashMap<String,String>();
		List<String> numiids = new ArrayList<String>();
		String jsonTxt = null;
		ItemsOnsaleGetResponse response = null;
		int retry = 1;
		try {
			//System.out.println(dataSource.getAppkey()+"---"+dataSource.getSecret()+"---"+dataSource.getSessionKey());
			response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			int count = 1;
			
			while ((response == null)||(jsonTxt.contains("error"))) {
				progressData[retry++] = "获取在售商品信息失败，第"+retry+"重试中...";
				response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				if (count <= 0) {
					break;
				}
				count--;
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ((response == null)||(jsonTxt == null)) {
			/*
              FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试!!"); 
              FacesContext.getCurrentInstance().addMessage(null, msg);
		      RequestContext.getCurrentInstance().update("items_data:messages");
		    */
		      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		      msg.setSummary("连接淘宝错误，请重试!");
              return forSaleItemsData;
		}
		else if (jsonTxt.contains("error")) {
			/*
              FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取商品信息失败，请重试!!"); 
              FacesContext.getCurrentInstance().addMessage(null, msg);
		      RequestContext.getCurrentInstance().update("items_data:messages");
		      */
		      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		      msg.setSummary("获取商品信息失败，请重试!");
              return forSaleItemsData;    
		}
		else if (jsonTxt.contains("session-expired")){
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			/*
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取商品信息失败，请重试!!"); 
			FacesContext.getCurrentInstance().addMessage(null, msg); 
			RequestContext.getCurrentInstance().update("items_data:messages");	
		    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		    msg.setSummary("获取商品信息失败，请重试!");
			*/
		}
		System.out.println("retrieveForSaleItems-------------"+jsonTxt);

      //System.out.println("response body from retrieveForSaleItems==============="+jsonTxt+"====================");
      
      if (jsonTxt.contains("items_onsale_get_response")){
    	  if (jsonTxt.contains("\"total_results\":0")){
    		  return null;
    	  }
    	  JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
    	  JSONArray items_array = json.getJSONObject("items_onsale_get_response").getJSONObject("items").getJSONArray("item");
          //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
     
    	  for (int i=0;i<items_array.size();i++){
    		  JSONObject jo = (JSONObject)items_array.get(i);
    		  String title = jo.getString("title");
    		  String numiid = jo.getString("num_iid");
    		  Double itemPrice = jo.getDouble("price");
    		  String pic_url = jo.getString("pic_url");
    		  int num = jo.getInt("num");
    		  numiids.add(numiid);
    		  numiidToTitle.put(numiid, title);
    		  numiidToPrice.put(numiid,itemPrice);
    		  numiidToItemNum.put(numiid, new Integer(num));
    		  numiidToPicUrl.put(numiid, pic_url);      
    	  }
		progressData[retry++] = "一共有"+numiids.size()+"个商品";
		progressData[retry++] = "开始获取商品详细信息";

    	  Map<String,ItemData> tmpNumiidToItemData = new HashMap<String,ItemData>();
    	  StringBuilder numiidsString = new StringBuilder();
    	  int round = 0;
    	  //for (String numiid : numiids) {
    	  for (int j=0;j<numiids.size();j++) {
    		  numiidsString.append(numiids.get(j)+",");
    		  if (((j+1) % 20 == 0) || (j == numiids.size()-1)) {
    			  round = (j+1)/20;
    			  if ((j+1) % 20 != 0) {
    				  round++;
    			  }
    			  //System.out.println("j================="+j+"=============numiids size=="+numiids.size()+"====round=="+round);
    			  //taobao.items.list.get
    			  TaobaoClient clientItemsList=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
    			  ItemsListGetRequest reqItemsList=new ItemsListGetRequest();
    			  //reqItemsList.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
    			  //reqItemsList.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
    			  reqItemsList.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,pic_url,num,price,has_showcase,approve_status,property_alias,item_img,prop_img,sku");
    			  reqItemsList.setNumIids(numiidsString.toString());
    			  String jsonTxtItemsList = null;
    			  ItemsListGetResponse responseItemsList = null;
    			  try {
    				  responseItemsList = clientItemsList.execute(reqItemsList, dataSource.getSessionKey());
    				  jsonTxtItemsList = responseItemsList.getBody();
    				  System.out.println("ItemsListGetResponse=="+jsonTxtItemsList);
    				  int count = 1;
    				  while ((jsonTxtItemsList == null)||(jsonTxtItemsList.contains("error"))) {
    					  responseItemsList = clientItemsList.execute(reqItemsList, dataSource.getSessionKey());
    					  jsonTxtItemsList = responseItemsList.getBody();
    					  if (count <= 0) {
    						  break;
    					  }
    					  count--;
    					  Thread.sleep(1000);
    				  }
    			  } catch (Exception e) {
    				  e.printStackTrace();
    			  }
    			  if ((responseItemsList == null)||(jsonTxtItemsList == null)) {
    				  //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试!!"); 
    				  //FacesContext.getCurrentInstance().addMessage(null, msg);
    			  }
    			  else if (jsonTxtItemsList.contains("error")) {
    				  //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取商品信息失败，请重试!!"); 
    				  //FacesContext.getCurrentInstance().addMessage(null, msg);
    			  }
    			  else if (jsonTxtItemsList.contains("items_list_get_response")) {
    				  //System.out.println(jsonTxtItemsList);
    				  JSONObject jsonItemsList = (JSONObject) JSONSerializer.toJSON(jsonTxtItemsList);
    				  JSONArray itemArray = jsonItemsList.getJSONObject("items_list_get_response").getJSONObject("items").getJSONArray("item");
    				  RetrieveItem retrieveItem = new RetrieveItem();
    				  ItemData itemData = null;
    				  for (int i=0;i<itemArray.size();i++) {
    					  JSONObject joItem = (JSONObject)itemArray.get(i);  
    					  String tmptitle = numiidToTitle.get(numiids.get(i+20*(round-1)));
    					  
    					  /* do not show messages since it is very fast without checking image in RetrieveItem.java
    					  if (progressData[9] == null || progressData[9].equals("")) {
    						  progressData[9] = "<font color='blue'>"+tmptitle+"</font>";
    					  }
    					  else {
    						  for (int k=1;k<10;k++) {
    							  progressData[k-1] = progressData[k];
    						  }
    						  progressData[9] = "<font color='blue'>"+tmptitle+"</font>";
    					  }
    					  */
    					  itemData = retrieveItem.retrieveItem(dataSource, joItem.toString(), progressData);
    					  itemData.setTitle(numiidToTitle.get(numiids.get(i+20*(round-1))));
    					  itemData.setItemPrice(numiidToPrice.get(numiids.get(i+20*(round-1))));
    					  itemData.setItemNum(numiidToItemNum.get(numiids.get(i+20*(round-1))));
    					  itemData.setItemPicUrl(numiidToPicUrl.get(numiids.get(i+20*(round-1))));
    					  tmpNumiidToItemData.put(numiids.get(i+20*(round-1)), itemData);
    					  //System.out.println("======="+numiids.get(i)+"============");
    				  }
    				  //System.out.println(numiids.size()+"size1========================");
    			  }
    			  else {
    				  //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取商品信息失败，请重试!!"); 
    				  //FacesContext.getCurrentInstance().addMessage(null, msg);    				  
    			  }
    			  numiidsString = new StringBuilder();
    		  }
    	  }
    	  forSaleItemsData.setNumiidToItemData(tmpNumiidToItemData);
    	  forSaleItemsData.setNumiids(numiids);
      }
      System.out.println("retrieveForSaleItems end");
      return forSaleItemsData;
	}

     

     public ForSaleItemsData retrieveForSaleItems(DataSource dataSource, List<String> leafcids, String [] progressData){
      
      ForSaleItemsData forSaleItemsData = new ForSaleItemsData();
      /*
      HashMap<String,String> tmpNumiidToTitle = new HashMap<String,String>();
      HashMap<String,String> tmpNumiidToItemPicUrl = new HashMap<String,String>();
      HashMap<String,Double> tmpNumiidToItemPrice = new HashMap<String,Double>();
      HashMap<String,Integer> tmpNumiidToItemNum = new HashMap<String,Integer>();
      HashMap<String,Boolean> tmpNumiidToHasSku = new HashMap<String,Boolean>();
      RetrieveItem tmpRetrieveItem = new RetrieveItem();
      Map<String,ItemData> tmpNumiidToItemData = new HashMap<String,ItemData>();
      Map<String, String> tmpNumiidToitemImgPosition = new HashMap<String,String>();
      for (int i=0;i<leafcids.size();i++) {
       System.out.println("leafcid:---------------------"+leafcids.get(i));
       TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
       ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
       //req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id");
       req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_showcase,delist_time,postage_id,seller_cids");
       req.setSellerCids(leafcids.get(i));
       req.setPageSize(200L);
       String jsonTxt = "";
       try {
        System.out.println(dataSource.getAppkey()+"---"+dataSource.getSecret()+"---"+dataSource.getSessionKey());
        ItemsOnsaleGetResponse response = client.execute(req , dataSource.getSessionKey());
        jsonTxt = response.getBody();
        int count = 1;
        while (jsonTxt.contains("error")) {
         response = client.execute(req , dataSource.getSessionKey());
         jsonTxt = response.getBody();
         if (count < 0) {
          break;
         }
         count--;
         Thread.sleep(1000);     
        }
        if (jsonTxt.contains("session-expired")){
         FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsp");
        }
              else {
               FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功获取商品信息!"); 
               FacesContext.getCurrentInstance().addMessage(null, msg);          
              }
        System.out.println("retrieveForSaleItems-------------"+jsonTxt);
       } catch (Exception e) {
              FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取销售商品失败，请重试!!"); 
              FacesContext.getCurrentInstance().addMessage(null, msg); 
        //e.printStackTrace();
       }
       //System.out.println("response body from retrieveForSaleItems==============="+jsonTxt+"====================");
          JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
          JSONArray items_array = json.getJSONObject("items_onsale_get_response").getJSONObject("items").getJSONArray("item");
          //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

       for (int j=0;j<items_array.size();j++){
           JSONObject jo = (JSONObject)items_array.get(j);
           //System.out.println("**************"+jo.getString("title"));
           if (progressData[9] == null || progressData[9].equals("")) {
            progressData[9] = "<font color='red'>"+jo.getString("title")+"</font>";
           }
           else {
            for (int k=1;k<10;k++) {
             progressData[k-1] = progressData[k];
            }
            progressData[9] = "<font color='red'>"+jo.getString("title")+"</font>";
           }
           if (jo.getString("has_showcase").equals("true")) {
            //System.out.println("=================================has showcase true");
            String title = jo.getString("title");
            
            //System.out.println(title+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            long numiid = jo.getLong("num_iid");
            ItemData tmpItemData = tmpRetrieveItem.retrieveItem(dataSource, numiid, progressData);
            tmpNumiidToItemData.put(String.valueOf(numiid), tmpItemData);
            tmpNumiidToitemImgPosition.put(String.valueOf(numiid), "0");//by default, show the item_img at position 0
            Double itemPrice = jo.getDouble("price");
            tmpNumiidToTitle.put(Long.toString(numiid), title);
            String pic_url = jo.getString("pic_url");
            int num = jo.getInt("num");
         if (CheckRemoteFile.exists(pic_url)) {
          //System.out.println("file exists------------");
          tmpNumiidToItemPicUrl.put(Long.toString(numiid), pic_url);
         }
         else {
          tmpNumiidToItemPicUrl.put(Long.toString(numiid), null);
         }
            //tmpNumiidToItemPicUrl.put(Long.toString(numiid), pic_url);
            tmpNumiidToItemPrice.put(Long.toString(numiid), itemPrice);
            tmpNumiidToItemNum.put(Long.toString(numiid), new Integer(num));
            //System.out.println(Long.toString(numiid)+"*****"+num);
            
            //need to consider tmpItemData==null
            if (tmpItemData != null) {
             tmpNumiidToHasSku.put(Long.toString(numiid), tmpItemData.isItemHasSku());
            }
            else {
             tmpNumiidToHasSku.put(Long.toString(numiid), null);
            }

           }
          }
         
          forSaleItemsData.setNumiidToTitle(tmpNumiidToTitle);
          forSaleItemsData.setNumiidToItemPicUrl(tmpNumiidToItemPicUrl);
          forSaleItemsData.setNumiidToItemPrice(tmpNumiidToItemPrice);
          forSaleItemsData.setNumiidToItemNum(tmpNumiidToItemNum);
          forSaleItemsData.setTitles(new ArrayList<String>(forSaleItemsData.getTitleToNumiid().keySet()));
          forSaleItemsData.setNumiids(new ArrayList<String>(forSaleItemsData.getNumiidToTitle().keySet()));  
          forSaleItemsData.setNumiidToHasSku(tmpNumiidToHasSku);
          forSaleItemsData.setNumiidToItemData(tmpNumiidToItemData);
          forSaleItemsData.setNumiidToitemImgPosition(tmpNumiidToitemImgPosition);
         
          forSaleItemsData.setColors(null);
          //System.out.println(forSaleItemsData.getNumiids()+"-----------getNumiids()--------");
          //////////////////////
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功获取商品信息!"); 
             FacesContext.getCurrentInstance().addMessage(null, msg);
      }
      */
      return forSaleItemsData;
     }

    }
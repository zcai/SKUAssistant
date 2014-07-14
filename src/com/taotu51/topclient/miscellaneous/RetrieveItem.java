package com.taotu51.topclient.miscellaneous;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class RetrieveItem implements Serializable {
	private static final long serialVersionUID = 269279301070510758L;

	public ItemData retrieveItem(DataSource dataSource, String jsonTxtItem, String[] progressData) {		
	    System.out.println("retrieve itemData begin");
	    System.out.println(jsonTxtItem);
		//System.out.println(jsonTxtItem);
	    ItemData itemData = new ItemData();
	    //JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxtItem);
	    JsonNode json = null;
		try {
			json = new ObjectMapper().readTree(jsonTxtItem);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    /*
	    itemData.setDetail_url(json.getString("detail_url"));
	    itemData.setPropertyAlias(json.getString("property_alias"));
	    itemData.setHasShowcase(json.getBoolean("has_showcase"));
	    itemData.setApproveStatus(json.getString("approve_status"));
	    itemData.setCid(json.getLong("cid"));
	    */
	    itemData.setDetail_url(json.get("detail_url").asText());
	    itemData.setPropertyAlias(json.get("property_alias").asText());
	    itemData.setHasShowcase(json.get("has_showcase").asBoolean());
	    itemData.setApproveStatus(json.get("approve_status").asText());
	    itemData.setCid(json.get("cid").asLong());
	    
	    //System.out.println(itemoj.getString("property_alias")+"*********************");
	    if (itemData.getPropertyAlias() != null && ! itemData.getPropertyAlias().equals("")) {
	    	String [] propertyAliasParts = itemData.getPropertyAlias().split(";");
	    	Map<String,String> tmpPropertyToAlias = new HashMap<String,String>();
	    	for (int k=0;k<propertyAliasParts.length;k++) {
	    		String [] tmpParts = propertyAliasParts[k].split(":");
	    		tmpPropertyToAlias.put(tmpParts[0]+":"+tmpParts[1], tmpParts[2]);
	    	}
	    	itemData.setPropertyToAlias(tmpPropertyToAlias);
	    }	
	    	    
	    String[] tmpItemImgs = new String[5];
	    Set<String> tmpUsedItemImgPositions = new HashSet<String>();
	    Map<String, String> tmpItemImgPositionToID = new HashMap<String,String>();
	    String [] tmpItemImgIDs = new String[5];//by default there are 5 image spots available
	    //try {//multiple item_img 
	    //try catch is expensive, use if instead
	    if (jsonTxtItem.indexOf("\"item_img\":[") != -1) {
	    	/*
	    	JSONArray itemImgArray = json.getJSONObject("item_imgs").getJSONArray("item_img");
	    	for (int i=0;i<itemImgArray.size();i++) {
	    		JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
	    		if (CheckRemoteFile.exists(tmpobj.getString("url"))) {
	    			tmpItemImgs[i] = tmpobj.getString("url");
	    		}
	    		else {
	    			tmpItemImgs[i] = "badimg";
	    		}
	    		tmpItemImgPositionToID.put(tmpobj.getString("position"), tmpobj.getString("id"));
	    		tmpItemImgIDs[i] = tmpobj.getString("id");
	    		tmpUsedItemImgPositions.add(tmpobj.getString("position"));
	    	}
	    	*/
	    	JsonNode itemImgArray = json.get("item_imgs").get("item_img");
	    	if (itemImgArray.isArray()) {
	    		int i = 0;
        		for (JsonNode imgNode : itemImgArray) {
        			/* do not check image
    	    		if (CheckRemoteFile.exists(imgNode.get("url").asText())) {
    	    			tmpItemImgs[i] = imgNode.get("url").asText();
    	    		}
    	    		else {
    	    			tmpItemImgs[i] = "badimg";
    	    		}
    	    		*/
	    			tmpItemImgs[i] = imgNode.get("url").asText();

    	    		tmpItemImgPositionToID.put(imgNode.get("position").asText(), imgNode.get("id").asText());
    	    		tmpItemImgIDs[i] = imgNode.get("id").asText();
    	    		tmpUsedItemImgPositions.add(imgNode.get("position").asText());
    	    		i++;
        		}
	    	}
	    }
	    else {
	    	if (jsonTxtItem.indexOf("\"item_img\":{") != -1) {
	    		/*
	    		JSONObject itemImgsoj = json.getJSONObject("item_imgs").getJSONObject("item_img");
	    		if (CheckRemoteFile.exists(itemImgsoj.getString("url"))) {
	    			tmpItemImgs[0] = itemImgsoj.getString("url");
	    		}
	    		else {
	    			tmpItemImgs[0] = "badimg";
	    		}
	    		tmpItemImgPositionToID.put(itemImgsoj.getString("position"), itemImgsoj.getString("id"));
	    		tmpUsedItemImgPositions.add(itemImgsoj.getString("position"));
	    		*/
	    		JsonNode itemImgsoj = json.get("item_imgs").get("item_img");
	    		/*
	    		if (CheckRemoteFile.exists(itemImgsoj.get("url").asText())) {
	    			tmpItemImgs[0] = itemImgsoj.get("url").asText();
	    		}
	    		else {
	    			tmpItemImgs[0] = "badimg";
	    		}
	    		*/
    			tmpItemImgs[0] = itemImgsoj.get("url").asText();

	    		tmpItemImgPositionToID.put(itemImgsoj.get("position").asText(), itemImgsoj.get("id").asText());
	    		tmpUsedItemImgPositions.add(itemImgsoj.get("position").asText());
	    	}
	    	else {
	    		//System.out.println("no item img------------------");
	    	}
	    }
	    itemData.setItemImgs(tmpItemImgs);
	    itemData.setItemImgPositionToID(tmpItemImgPositionToID);
	    itemData.setItemImgIDs(tmpItemImgIDs);
	    itemData.setUsedItemImgPositions(tmpUsedItemImgPositions);
	    //System.out.println("itemimgs-----------------"+itemData.getItemImgs());
	    	    
	    ///
	    HashMap<String,ArrayList<String>> tmpPidVid1ToPidVid2 = new HashMap<String,ArrayList<String>>();
		HashMap<String,String> tmpPidVid1ToName = new HashMap<String,String>();
		HashMap<String,String> tmpPidVid2ToName = new HashMap<String,String>();
		Map<String,Integer> tmpPidVidToQuantity = new HashMap<String,Integer>();
		HashMap<String,String> tmpPidVid1ToPrice = new HashMap<String,String>();
		HashMap<String,String> tmpProp_img_propertiesToUrl = new HashMap<String,String>();
		HashMap<String,Integer> tmpPropertiesToQuantity = new HashMap<String,Integer>();//only for items with sku
		HashMap<String,Long> tmpSkuPropertiesToSkuid = new HashMap<String,Long>();
		HashMap<Long,String> tmpSkuidToSkuProperties = new HashMap<Long,String>();
		//try {//multiple <sku>
		/*
    	if (jsonTxtItem.indexOf("\"sku\":[") != -1) {
			itemData.setItemHasSku(true);
		    
	    	//if multiple <sku> then multiple or one <prop_img>
	    	//if one <sku> then one <prop_img> ???
	    	//if 0 <sku> then 0 <sku>
		    
			JSONArray sku_array= json.getJSONObject("skus").getJSONArray("sku");
		    for (int i=0;i<sku_array.size();i++){
		        JSONObject jo = (JSONObject)sku_array.get(i);
		    	String properties = jo.getString("properties");//sku properties
		    	long skuid = jo.getLong("sku_id");
		    	int quantity = jo.getInt("quantity");
		        String propertiesName = jo.getString("properties_name");
		    	String [] skuPidVidsWithName = propertiesName.split(";");//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)	    		    															 //21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
		    	
		    	String [] skuPidVids = properties.split(";");
		    	String skuPidVid1 = "";
		    	String skuPidVid2 = "";
		    	String [] skuPidVid1WithName;
	    		//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
	    		//treat pidVid2(1627207:28320:颜色分类:白色;) as pidVid1, pidVid1+pidVid3(21684:6536025:套餐种类:官方标配;+1627732:3266779:套餐:套餐一) as pidVid2
	    		//since each property image is for pidVid2 in this case. 
		    	if (skuPidVids.length == 3) {//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
		    		skuPidVid1 = skuPidVids[1];
		    		skuPidVid2 = skuPidVids[0]+";"+skuPidVids[2];
		    		skuPidVid1WithName = skuPidVidsWithName[1].split(":");
		    		//itemData.setItemHasThreeSku(true);
		    		itemData.setItemHasTwoSku(true);//treat it as two sku
		    		String [] skuPidVid2WithName_first = skuPidVidsWithName[0].split(":");
		    		String [] skuPidVid2WithName_second = skuPidVidsWithName[2].split(":");
		    		tmpPidVid2ToName.put(skuPidVid2WithName_first[0]+":"+skuPidVid2WithName_first[1]+";"+skuPidVid2WithName_second[0]+":"+skuPidVid2WithName_second[1],skuPidVid2WithName_first[3]+":"+skuPidVid2WithName_second[3]);//pay attention to the format
		    		
		    		//change the order o properties
		    		String [] property_parts = properties.split(";");
		    		tmpPropertiesToQuantity.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2] + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+ ";");
		    	}
		    	else if (skuPidVids.length == 2) {
		    		//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)
		    		skuPidVid1 = skuPidVids[0];
		    		skuPidVid2 = skuPidVids[1];
		    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
		    		itemData.setItemHasTwoSku(true);
		    		String [] skuPidVid2WithName = skuPidVidsWithName[1].split(":");
		    		tmpPidVid2ToName.put(skuPidVid2WithName[0]+":"+skuPidVid2WithName[1],skuPidVid2WithName[3]);//do not include "尺码" in the size name
		    	
					tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
		    	}
		    	else {//skuPidVids.length == 1
		    		skuPidVid1 = skuPidVids[0];
		    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
		    		
					tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
		    	}
		    	tmpPidVid1ToName.put(skuPidVid1WithName[0]+":"+skuPidVid1WithName[1],skuPidVid1WithName[3]);//do not include "颜色" in the color name
		    	
		        if (tmpPidVid1ToPidVid2.get(skuPidVid1) == null) {
		        	tmpPidVid1ToPidVid2.put(skuPidVid1, new ArrayList<String>());
		        }
		        tmpPidVid1ToPidVid2.get(skuPidVid1).add(skuPidVid2);
		    	tmpPidVidToQuantity.put(properties, jo.getInt("quantity"));
		    	
		    	String price = jo.getString("price");
		    	tmpPidVid1ToPrice.put(skuPidVid1, price);//store the price of the last sku since many skus may have the same color
		    }
		    */
		
    	if (jsonTxtItem.indexOf("\"sku\":[") != -1) {
			itemData.setItemHasSku(true);
		    
	    	//if multiple <sku> then multiple or one <prop_img>
	    	//if one <sku> then one <prop_img> ???
	    	//if 0 <sku> then 0 <sku>
		    
			JsonNode sku_array= json.get("skus").get("sku");
			if (sku_array.isArray()) {
				int i = 0;
	        	for (JsonNode jo : sku_array) {
	        		//JsonNode jo = sku_array.get(i);
			    	String properties = jo.get("properties").asText();//sku properties
			    	long skuid = jo.get("sku_id").asLong();
			    	int quantity = jo.get("quantity").asInt();
			        String propertiesName = jo.get("properties_name").asText();
			    	String [] skuPidVidsWithName = propertiesName.split(";");//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)	    		    															 //21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
			    	
			    	String [] skuPidVids = properties.split(";");
			    	String skuPidVid1 = "";
			    	String skuPidVid2 = "";
			    	String [] skuPidVid1WithName;
		    		//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
		    		//treat pidVid2(1627207:28320:颜色分类:白色;) as pidVid1, pidVid1+pidVid3(21684:6536025:套餐种类:官方标配;+1627732:3266779:套餐:套餐一) as pidVid2
		    		//since each property image is for pidVid2 in this case. 
			    	if (skuPidVids.length == 3) {//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
			    		skuPidVid1 = skuPidVids[1];
			    		skuPidVid2 = skuPidVids[0]+";"+skuPidVids[2];
			    		skuPidVid1WithName = skuPidVidsWithName[1].split(":");
			    		//itemData.setItemHasThreeSku(true);
			    		itemData.setItemHasTwoSku(true);//treat it as two sku
			    		String [] skuPidVid2WithName_first = skuPidVidsWithName[0].split(":");
			    		String [] skuPidVid2WithName_second = skuPidVidsWithName[2].split(":");
			    		tmpPidVid2ToName.put(skuPidVid2WithName_first[0]+":"+skuPidVid2WithName_first[1]+";"+skuPidVid2WithName_second[0]+":"+skuPidVid2WithName_second[1],skuPidVid2WithName_first[3]+":"+skuPidVid2WithName_second[3]);//pay attention to the format
			    		
			    		//change the order o properties
			    		String [] property_parts = properties.split(";");
			    		tmpPropertiesToQuantity.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+";", new Integer(quantity));
						tmpSkuPropertiesToSkuid.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2] + ";", new Long(skuid));
				    	tmpSkuidToSkuProperties.put(new Long(skuid), property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+ ";");
			    	}
			    	else if (skuPidVids.length == 2) {
			    		//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)
			    		skuPidVid1 = skuPidVids[0];
			    		skuPidVid2 = skuPidVids[1];
			    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
			    		itemData.setItemHasTwoSku(true);
			    		String [] skuPidVid2WithName = skuPidVidsWithName[1].split(":");
			    		tmpPidVid2ToName.put(skuPidVid2WithName[0]+":"+skuPidVid2WithName[1],skuPidVid2WithName[3]);//do not include "尺码" in the size name
			    	
						tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
						tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
				    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
			    	}
			    	else {//skuPidVids.length == 1
			    		skuPidVid1 = skuPidVids[0];
			    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
			    		
						tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
						tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
				    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
			    	}
			    	tmpPidVid1ToName.put(skuPidVid1WithName[0]+":"+skuPidVid1WithName[1],skuPidVid1WithName[3]);//do not include "颜色" in the color name
			    	
			        if (tmpPidVid1ToPidVid2.get(skuPidVid1) == null) {
			        	tmpPidVid1ToPidVid2.put(skuPidVid1, new ArrayList<String>());
			        }
			        tmpPidVid1ToPidVid2.get(skuPidVid1).add(skuPidVid2);
			    	tmpPidVidToQuantity.put(properties, jo.get("quantity").asInt());
			    	
			    	String price = jo.get("price").asText();
			    	tmpPidVid1ToPrice.put(skuPidVid1, price);//store the price of the last sku since many skus may have the same color
			    }
	        	i++;
			}
		    
	    	//try {//multiple <prop_img>
		    if (jsonTxtItem.indexOf("\"prop_img\":[") != -1) {
		    	/*
		    	JSONArray prop_img_array= json.getJSONObject("prop_imgs").getJSONArray("prop_img");
			    for (int i=0;i<prop_img_array.size();i++){
			        JSONObject jo = (JSONObject)prop_img_array.get(i);
			        tmpProp_img_propertiesToUrl.put(jo.getString("properties"),jo.getString("url"));
			    }
			    */
		    	JsonNode prop_img_array= json.get("prop_imgs").get("prop_img");
		    	if (prop_img_array.isArray()) {
		    		int i = 0;
		        	for (JsonNode jo : prop_img_array) {
				    	//JsonNode jo = prop_img_array.get(i);
				        tmpProp_img_propertiesToUrl.put(jo.get("properties").asText(),jo.get("url").asText());
				        i++;
		        	}
			    }
	    	}
		    else {
		    //catch(Exception e1) {
		    	if (jsonTxtItem.indexOf("\"prop_img\":{") != -1) {
		    	//try {// only one <prop_img>
		    		/*
		    		JSONObject prop_img_obj= json.getJSONObject("prop_imgs").getJSONObject("prop_img");
		    		tmpProp_img_propertiesToUrl.put(prop_img_obj.getString("properties"),prop_img_obj.getString("url"));
		    		*/
		    		JsonNode prop_img_obj= json.get("prop_imgs").get("prop_img");
		    		tmpProp_img_propertiesToUrl.put(prop_img_obj.get("properties").asText(),prop_img_obj.get("url").asText());
		    	}
		    	//catch(Exception e) {//some may not have prop_img
		    	else {
		    	}
		    }
	    }
    	else {
	    //catch(Exception e2) {
    		//System.out.println("e2 output=================================");
    		//e2.printStackTrace();
	    	//try {//one <sku>		    	
	    	if (jsonTxtItem.indexOf("\"sku\":{") != -1) {
	    		itemData.setItemHasSku(true);
		    	//String num_iid = json.getString("num_iid");//use numiid instead of pidvid
		    	String num_iid = json.get("num_iid").asText();//use numiid instead of pidvid
		    	tmpPidVid1ToPidVid2.put(num_iid, new ArrayList<String>());	
		    	/*
		    	JSONObject onlyOneSku = json.getJSONObject("skus").getJSONObject("sku");
				String properties = onlyOneSku.getString("properties");//sku properties
				long skuid = onlyOneSku.getLong("sku_id");
				int quantity = onlyOneSku.getInt("quantity");
				String propertiesName = onlyOneSku.getString("properties_name");
				*/
		    	JsonNode onlyOneSku = json.get("skus").get("sku");
				String properties = onlyOneSku.get("properties").asText();//sku properties
				long skuid = onlyOneSku.get("sku_id").asLong();
				int quantity = onlyOneSku.get("quantity").asInt();
				String propertiesName = onlyOneSku.get("properties_name").asText();		    	
		    	
				String [] skuPidVidsWithName = propertiesName.split(";");//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)
				                                                         //or only one sku property "properties_name":"1627207:28327:颜色分类:酒红色"
								    	
				String [] skuPidVids = properties.split(";");
		    	String skuPidVid1;
		    	String skuPidVid2 = "";
		    	String [] skuPidVid1WithName;
		    	if (skuPidVids.length == 3) {//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
		    		skuPidVid1 = skuPidVids[1];
		    		skuPidVid2 = skuPidVids[0]+";"+skuPidVids[2];
		    		skuPidVid1WithName = skuPidVidsWithName[1].split(":");
		    		//itemData.setItemHasThreeSku(true);
		    		itemData.setItemHasTwoSku(true);//treat it as two sku
		    		String [] skuPidVid2WithName_first = skuPidVidsWithName[0].split(":");
		    		String [] skuPidVid2WithName_second = skuPidVidsWithName[2].split(":");
		    		tmpPidVid2ToName.put(skuPidVid2WithName_first[0]+":"+skuPidVid2WithName_first[1]+";"+skuPidVid2WithName_second[0]+":"+skuPidVid2WithName_second[1],skuPidVid2WithName_first[3]+":"+skuPidVid2WithName_second[3]);//pay attention to the format
					
		    		//change the order o properties
		    		String [] property_parts = properties.split(";");
		    		tmpPropertiesToQuantity.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(property_parts[1] + ";"+property_parts[0]+";"+property_parts[2] + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), property_parts[1] + ";"+property_parts[0]+";"+property_parts[2]+ ";");
		    	}
		    	else if (skuPidVids.length == 2) {
		    		//1627207:3232484:颜色:天蓝色;1627778:3267949:尺码:175/96(L)
		    		skuPidVid1 = skuPidVids[0];
		    		skuPidVid2 = skuPidVids[1];
		    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
		    		itemData.setItemHasTwoSku(true);
		    		String [] skuPidVid2WithName = skuPidVidsWithName[1].split(":");
		    		tmpPidVid2ToName.put(skuPidVid2WithName[0]+":"+skuPidVid2WithName[1],skuPidVid2WithName[3]);//do not include "尺码" in the size name
					tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
		    	}
		    	else {//skuPidVids.length == 1
		    		skuPidVid1 = skuPidVids[0];
		    		skuPidVid1WithName = skuPidVidsWithName[0].split(":");
					tmpPropertiesToQuantity.put(properties + ";", new Integer(quantity));
					tmpSkuPropertiesToSkuid.put(properties + ";", new Long(skuid));
			    	tmpSkuidToSkuProperties.put(new Long(skuid), properties+ ";");
		    	}
		    	tmpPidVid1ToName.put(skuPidVid1WithName[0]+":"+skuPidVid1WithName[1],skuPidVid1WithName[3]);//do not include "颜色" in the color name
				if (tmpPidVid1ToPidVid2.get(skuPidVid1) == null) {
					tmpPidVid1ToPidVid2.put(skuPidVid1, new ArrayList<String>());
				}
				tmpPidVid1ToPidVid2.get(skuPidVid1).add(skuPidVid2);

		    	if (progressData[9] == null || progressData[9].equals("")) {
		    		progressData[9] = skuPidVid1WithName[3];
		    	}
		    	else {
		    		for (int k=1;k<10;k++) {
		    			progressData[k-1] = progressData[k];
		    		}
		    		progressData[9] = skuPidVid1WithName[3];
		    	}
		    	
				//tmpPidVidToQuantity.put(properties, onlyOneSku.getInt("quantity"));
				tmpPidVidToQuantity.put(properties, onlyOneSku.get("quantity").asInt());
				
				//String price = onlyOneSku.getString("price");
				String price = onlyOneSku.get("price").asText();
				tmpPidVid1ToPrice.put(skuPidVid1, price);//store the price of the last sku since many skus may have the same color	    	
	    	
		    	//try {//multiple <prop_img>
	    		if (jsonTxtItem.indexOf("\"prop_img\":[") != -1) {
	    			/*
			    	JSONArray prop_img_array= json.getJSONObject("prop_imgs").getJSONArray("prop_img");
				    for (int i=0;i<prop_img_array.size();i++){
				        JSONObject jo = (JSONObject)prop_img_array.get(i);
				        tmpProp_img_propertiesToUrl.put(jo.getString("properties"),jo.getString("url"));
				    }
				    */
	    			
	    			JsonNode prop_img_array= json.get("prop_imgs").get("prop_img");
	    			if (prop_img_array.isArray()) {
		        		for (JsonNode propimgNode : prop_img_array) {
		        			tmpProp_img_propertiesToUrl.put(propimgNode.get("properties").asText(),propimgNode.get("url").asText());
		        		}
		        	}
				    
		    	}
			    //catch(Exception e1) {
			    else {
			    	if (jsonTxtItem.indexOf("\"prop_img\":{") != -1) {
			    	//try {// only one <prop_img>
			    		/*
			    		JSONObject prop_img_obj= json.getJSONObject("prop_imgs").getJSONObject("prop_img");
			    		tmpProp_img_propertiesToUrl.put(prop_img_obj.getString("properties"),prop_img_obj.getString("url"));
			    		*/
			    		JsonNode prop_img_obj= json.get("prop_imgs").get("prop_img");
			    		tmpProp_img_propertiesToUrl.put(prop_img_obj.get("properties").asText(),prop_img_obj.get("url").asText());
			    	}
				    //catch(Exception e) {//some may not have prop_img
				    else {
			    	}
			    }
	    	}
	    	//catch(Exception e3) {//no sku
	    	else {
	    		//System.out.println("e3 output=================================");
	    		//e3.printStackTrace();
	    		itemData.setItemHasSku(false);
	    		//JSONObject noSku = json.getJSONObject("item_get_response").getJSONObject("item");
		    	//String num_iid = json.getString("num_iid");//use numiid instead of pidvid
		    	String num_iid = json.get("num_iid").asText();//use numiid instead of pidvid
		    	tmpPidVid1ToPidVid2.put(num_iid, new ArrayList<String>());

		    	String url = null;
		    	//try {
		    	if (jsonTxtItem.indexOf("\"item_img\":[") != -1) {
		    		/*
		    		JSONArray item_img_array = json.getJSONObject("item_imgs").getJSONArray("item_img");
		    		JSONObject jo = (JSONObject)item_img_array.get(0);
		    		url = jo.getString("url");
		    		*/
		    		JsonNode item_img_array = json.get("item_imgs").get("item_img");
		    		JsonNode jo = item_img_array.get(0);
		    		url = jo.get("url").asText();
		    	}
		    	//catch(Exception e4) {
		    	else if (jsonTxtItem.indexOf("\"item_img\":{") != -1) {
		    		//System.out.println(json.toString());
		    		/*
		    		JSONObject item_img_obj = json.getJSONObject("item_imgs").getJSONObject("item_img");
		    		url = item_img_obj.getString("url");
		    		*/
		    		
		    		JsonNode item_img_obj = json.get("item_imgs").get("item_img");
		    		url = item_img_obj.get("url").asText();
		    	}
		    	
		    	//tmpProp_img_propertiesToUrl.put(json.getString("num_iid"),url);
		    	tmpProp_img_propertiesToUrl.put(json.get("num_iid").asText(),url);
	    	    itemData.setPropToImg(tmpProp_img_propertiesToUrl);
	    	}
	    }
	    //finally {
    	itemData.setPropToImg(tmpProp_img_propertiesToUrl);
    	itemData.setPropertiesToQuantity(tmpPropertiesToQuantity);
    	itemData.setSkuPropertiesToSkuid(tmpSkuPropertiesToSkuid);
    	itemData.setSkuidToSkuProperties(tmpSkuidToSkuProperties);
	    //}

	    Iterator<String> it = tmpPidVid1ToPidVid2.keySet().iterator();
	    while (it.hasNext()) {
	    	tmpPidVid1ToPidVid2.get(it.next()).add("new");//add placeholder for new sku
	    }
	    itemData.setPidVid1ToPidVid2(tmpPidVid1ToPidVid2);
	    List<String> tmpPidVid1s = new ArrayList<String>();
	    tmpPidVid1s.addAll(tmpPidVid1ToPidVid2.keySet());
	    tmpPidVid1s.add("new");//add a placeholder for new item
	    itemData.setPidVid1s(tmpPidVid1s);
	    itemData.setPidVid1ToName(tmpPidVid1ToName);
	    itemData.setPidVid1ToPrice(tmpPidVid1ToPrice);
	    itemData.setPidVid2ToName(tmpPidVid2ToName);
	    itemData.setPidVidToQuantity(tmpPidVidToQuantity);
	    
	    ///
	    /* move bolow part to RetrieveItemProps.java
	    long cid = json.getLong("cid");
	    delay this to retrieve when needed
	    retrieveItemProps(dataSource,itemData,cid); 
	    
		Map<String, String> allPidVidToNameForPid1 = new HashMap<String,String>();//to add a new sku such as an item of a new color, exclude the existing ones, such as the same color
		Map<String, String> allPidVidToNameForPid2 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
		Map<String, String> allPidVidToNameForPid3 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
		Map<String, Map<String,String>> tmpAvailablePidVid2ForPidVid1 = new HashMap<String,Map<String,String>>();//to add a new sku, such as a new size for an existing sku
		Map<String, Map<String,String>> tmpAvailablePidVid3ForPidVid1 = new HashMap<String,Map<String,String>>();//to add a new sku, such as a new size for an existing sku
		Object[] pidVid1List = itemData.pidVid1ToPidVid2.keySet().toArray();
		String [] pid1_vid1 = pidVid1List[0].toString().split(":");
    	allPidVidToNameForPid1 = itemData.getItemPropsPidToPidVidToName().get(pid1_vid1[0]);//all pidvid to name in itemprops for pid1

		Iterator<Entry<String, ArrayList<String>>> pidVid1ToPidVid2Entry = itemData.pidVid1ToPidVid2.entrySet().iterator();
	    while (pidVid1ToPidVid2Entry.hasNext()) {
	        Entry<String, ArrayList<String>> pairs = pidVid1ToPidVid2Entry.next();
	        String pidVid1 = pairs.getKey();
	        //System.out.println("*******************pidvid1*"+pidVid1);
	        //System.out.println("*******************allPidVidToNameForPid1*"+allPidVidToNameForPid1.keySet());
	        if (allPidVidToNameForPid1 != null) {//some items may not have pidvid1 property, in this case, numiid was used instead of pidvid1, and allPidVidToNameForPid1 is null
	        	allPidVidToNameForPid1.remove(pidVid1);
	        }
	        
	        List<String> pidVid2 = pairs.getValue();
	        String [] pid2_vid2 = pidVid2.get(0).split(":");
        	allPidVidToNameForPid2 = itemData.getItemPropsPidToPidVidToName().get(pid2_vid2[0]);//all pidvid to name in itemprops for pid2
        	itemData.setAllPidVidToNameForPid2(itemData.getItemPropsPidToPidVidToName2().get(pid2_vid2[0]));

	        if (allPidVidToNameForPid2 == null) {
	        	allPidVidToNameForPid2 = new HashMap<String,String>();
	        }
	        Map<String, String> availablePidVid2 = new HashMap<String, String>();
	        	
	        Set<String> existingPidVid2 = new HashSet<String>();
		       for (int j=0;j<pidVid2.size();j++){
		       	if (allPidVidToNameForPid2.containsKey(pidVid2.get(j))){
		       		existingPidVid2.add(pidVid2.get(j));
		       	}
	        }
			Iterator<Entry<String, String>> availablePidVid2OfPidVid1Entry = allPidVidToNameForPid2.entrySet().iterator();
			while (availablePidVid2OfPidVid1Entry.hasNext()) {
				Entry<String, String> pairs2 = availablePidVid2OfPidVid1Entry.next();
				if (!existingPidVid2.contains(pairs2.getKey())) {
					availablePidVid2.put(pairs2.getKey(), pairs2.getValue());
				}
			}
			tmpAvailablePidVid2ForPidVid1.put(pidVid1, availablePidVid2);
			////////////////////////////////////////////////////
			
			//System.out.println("*********************************************"+pidVid2.get(0)+"***"+pidVid2.get(1)+"***"+pidVid2.get(2));
			//21684:6536025;1627732:3266779***21684:6536025;1627732:3266781***new
			if (pidVid2.get(0).split(";").length == 2) {//21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
				//pid2_vid2[1].split(";")[1] //官方标配;1627207
				//System.out.println(pid2_vid2[1].split(";")[1]+"------------");
				allPidVidToNameForPid3 = itemData.getItemPropsPidToPidVidToName().get(pid2_vid2[1].split(";")[1]);//all pidvid to name in itemprops for pid3
	        	itemData.setAllPidVidToNameForPid3(itemData.getItemPropsPidToPidVidToName3().get(pid2_vid2[1].split(";")[1]));

		        if (allPidVidToNameForPid3 == null) {
		        	allPidVidToNameForPid3 = new HashMap<String,String>();
		        }
		        Map<String, String> availablePidVid3 = new HashMap<String, String>();

				Iterator<Entry<String, String>> availablePidVid3OfPidVid1Entry = allPidVidToNameForPid3.entrySet().iterator();
				while (availablePidVid3OfPidVid1Entry.hasNext()) {
					Entry<String, String> pairs3 = availablePidVid3OfPidVid1Entry.next();
					availablePidVid3.put(pairs3.getKey(), pairs3.getValue());
				}
				tmpAvailablePidVid3ForPidVid1.put(pidVid1, availablePidVid3);	
				//System.out.println(allPidVidToNameForPid3.keySet()+"-----------");
			}
			////////////////////////////////////////////////////
		}
	    itemData.setAllPidVidToNameForPid1(allPidVidToNameForPid1);
	    itemData.setAvailablePidVid2ForPidVid1(tmpAvailablePidVid2ForPidVid1);
	    //System.out.println("filtered2ColorToSizePidVidToName--"+filtered2ColorToSizePidVidToName.keySet());
	    
	    itemData.setAllPidVidToNameForPid3(allPidVidToNameForPid3);
	    itemData.setAvailablePidVid3ForPidVid1(tmpAvailablePidVid3ForPidVid1);
	    */
	    System.out.println("retrieve itemData end");
	    return itemData;
	}


	public class ItemData implements Serializable {
		private static final long serialVersionUID = -5793035914588799515L;
		private String desc;
		private String title;
		private String pic_url;
		private String propertyAlias;
		private String detail_url;
		private long cid;
		private Map<Long,String> itemCidToProps = new HashMap<Long,String>();
		private Map<String,String> propertyToAlias = new HashMap<String,String>();
		private HashMap<String,String> propToImg;
		private HashMap<String,Long> skuPropertiesToSkuid;
		private HashMap<Long,String> skuidToSkuProperties;
		private HashMap<String,ArrayList<String>> pidVid1ToPidVid2;
		private HashMap<String,String> pidVid1ToName;
		private HashMap<String,String> pidVid2ToName;
		private Map<String,Integer> pidVidToQuantity;
		private HashMap<String,String> pidVid1ToPrice;
		private List<String> pidVid1s;
		private Map<String,Integer> propertiesToQuantity;//only for items with sku
		private Map<String,String> itemPropsPidToName3 = new HashMap<String,String>();//pidvid -> name(vid's name)
		private Map<String,Map<String,String>> itemPropsPidToPidVidToName3 = new HashMap<String,Map<String,String>>();//pid -> pidvid -> name(vid's name)
		private Map<String,String> itemPropsPidToName2 = new HashMap<String,String>();//pidvid -> name(vid's name)
		private Map<String,Map<String,String>> itemPropsPidToPidVidToName2 = new HashMap<String,Map<String,String>>();//pid -> pidvid -> name(vid's name)
		private Map<String,String> itemPropsPidToName = new HashMap<String,String>();//pidvid -> name(vid's name)
		private Map<String,Map<String,String>> itemPropsPidToPidVidToName = new HashMap<String,Map<String,String>>();//pid -> pidvid -> name(vid's name)
		private Map<String,String> itemPropsPidVidToName = new HashMap<String,String>();//pid -> name(pid's name)
		private Map<String,Map<String,String>> availablePidVid2ForPidVid1 = new HashMap<String,Map<String,String>>();//such as the available sizes for a particular color
		private Map<String,Map<String,String>> availablePidVid3ForPidVid1 = new HashMap<String,Map<String,String>>();//such as the available sizes for a particular color
		private String[] itemImgs = new String[5];
		private Map<String, String> itemImgPositionToID;//current id of item_img in item_imgs
		private String [] itemImgIDs;//itemimg position is optional, do not use the above line. use this one instead
		private boolean itemHasSku = false;
		private boolean itemHasTwoSku = false;//some have two sku, such as color and size, some only have one sku
		private boolean itemHasThreeSku = false;//some have three sku, 21684:6536025:套餐种类:官方标配;1627207:28320:颜色分类:白色;1627732:3266779:套餐:套餐一
		private boolean hasShowcase = false;
		private String approveStatus;
		private Set<String> usedItemImgPositions;
		private Map<String, String> allPidVidToNameForPid1 = new HashMap<String,String>();//to add a new sku such as an item of a new color, exclude the existing ones, such as the same color
		private Map<String, String> allPidVidToNameForPid2 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
		private Map<String, String> allPidVidToNameForPid3 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
		
		
		//data from retrieveItemData
		private String itemPicUrl;
		private double itemPrice;
		private int itemNum;
		private HashMap<String,String> numiidToTitle = new HashMap<String,String>();
		private boolean hasSku;
		private List<String> colors;
		private List<String> titles;
		private int itemImgPosition;//current position of item_img in item_imgs
		
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getPic_url() {
			return pic_url;
		}
		public void setPic_url(String pic_url) {
			this.pic_url = pic_url;
		}
		
		public String getPropertyAlias() {
			return propertyAlias;
		}
		public void setPropertyAlias(String propertyAlias) {
			this.propertyAlias = propertyAlias;
		}
		public Map<String, String> getPropertyToAlias() {
			return propertyToAlias;
		}
		public void setPropertyToAlias(Map<String, String> propertyToAlias) {
			this.propertyToAlias = propertyToAlias;
		}
		public HashMap<String, String> getPropToImg() {
			return propToImg;
		}
		public void setPropToImg(HashMap<String, String> propToImg) {
			this.propToImg = propToImg;
		}
		public HashMap<String, Long> getSkuPropertiesToSkuid() {
			return skuPropertiesToSkuid;
		}
		public void setSkuPropertiesToSkuid(HashMap<String, Long> skuPropertiesToSkuid) {
			this.skuPropertiesToSkuid = skuPropertiesToSkuid;
		}
		
		public HashMap<Long, String> getSkuidToSkuProperties() {
			return skuidToSkuProperties;
		}
		public void setSkuidToSkuProperties(HashMap<Long, String> skuidToSkuProperties) {
			this.skuidToSkuProperties = skuidToSkuProperties;
		}
		public HashMap<String, ArrayList<String>> getPidVid1ToPidVid2() {
			return pidVid1ToPidVid2;
		}
		public void setPidVid1ToPidVid2(
				HashMap<String, ArrayList<String>> pidVid1ToPidVid2) {
			this.pidVid1ToPidVid2 = pidVid1ToPidVid2;
		}

		public HashMap<String, String> getPidVid1ToName() {
			return pidVid1ToName;
		}
		public void setPidVid1ToName(HashMap<String, String> pidVid1ToName) {
			this.pidVid1ToName = pidVid1ToName;
		}
		public HashMap<String, String> getPidVid2ToName() {
			return pidVid2ToName;
		}
		public void setPidVid2ToName(HashMap<String, String> pidVid2ToName) {
			this.pidVid2ToName = pidVid2ToName;
		}

		public Map<String, Integer> getPidVidToQuantity() {
			return pidVidToQuantity;
		}
		public void setPidVidToQuantity(Map<String, Integer> pidVidToQuantity) {
			this.pidVidToQuantity = pidVidToQuantity;
		}
		public HashMap<String, String> getPidVid1ToPrice() {
			return pidVid1ToPrice;
		}
		public void setPidVid1ToPrice(HashMap<String, String> pidVid1ToPrice) {
			this.pidVid1ToPrice = pidVid1ToPrice;
		}
		public List<String> getPidVid1s() {
			return pidVid1s;
		}
		public void setPidVid1s(List<String> pidVid1s) {
			this.pidVid1s = pidVid1s;
		}
		public String getDetail_url() {
			return detail_url;
		}
		public void setDetail_url(String detail_url) {
			this.detail_url = detail_url;
		}
		public Map<String, Integer> getPropertiesToQuantity() {
			return propertiesToQuantity;
		}
		public void setPropertiesToQuantity(
				Map<String, Integer> propertiesToQuantity) {
			this.propertiesToQuantity = propertiesToQuantity;
		}

		public Map<String, Map<String, String>> getItemPropsPidToPidVidToName() {
			return itemPropsPidToPidVidToName;
		}
		public void setItemPropsPidToPidVidToName(
				Map<String, Map<String, String>> itemPropsPidToPidVidToName) {
			this.itemPropsPidToPidVidToName = itemPropsPidToPidVidToName;
		}
		public Map<String, String> getItemPropsPidVidToName() {
			return itemPropsPidVidToName;
		}
		public void setItemPropsPidVidToName(
				Map<String, String> itemPropsPidVidToName) {
			this.itemPropsPidVidToName = itemPropsPidVidToName;
		}
		public Map<String, String> getItemPropsPidToName() {
			return itemPropsPidToName;
		}
		public void setItemPropsPidToName(Map<String, String> itemPropsPidToName) {
			this.itemPropsPidToName = itemPropsPidToName;
		}
		public Map<String, Map<String, String>> getAvailablePidVid2ForPidVid1() {
			return availablePidVid2ForPidVid1;
		}
		public void setAvailablePidVid2ForPidVid1(
				Map<String, Map<String, String>> availablePidVid2ForPidVid1) {
			this.availablePidVid2ForPidVid1 = availablePidVid2ForPidVid1;
		}

		public String[] getItemImgs() {
			return itemImgs;
		}
		public void setItemImgs(String[] itemImgs) {
			this.itemImgs = itemImgs;
		}
		public Map<String, String> getItemImgPositionToID() {
			return itemImgPositionToID;
		}
		public void setItemImgPositionToID(Map<String, String> itemImgPositionToID) {
			this.itemImgPositionToID = itemImgPositionToID;
		}
		public boolean isItemHasSku() {
			return itemHasSku;
		}
		public void setItemHasSku(boolean itemHasSku) {
			this.itemHasSku = itemHasSku;
		}
		
		public boolean isItemHasTwoSku() {
			return itemHasTwoSku;
		}
		public void setItemHasTwoSku(boolean itemHasTwoSku) {
			this.itemHasTwoSku = itemHasTwoSku;
		}
		
		public boolean isItemHasThreeSku() {
			return itemHasThreeSku;
		}
		public void setItemHasThreeSku(boolean itemHasThreeSku) {
			this.itemHasThreeSku = itemHasThreeSku;
		}
		public boolean isHasShowcase() {
			return hasShowcase;
		}
		public void setHasShowcase(boolean hasShowcase) {
			this.hasShowcase = hasShowcase;
		}
		
		public String getApproveStatus() {
			return approveStatus;
		}
		public void setApproveStatus(String approveStatus) {
			this.approveStatus = approveStatus;
		}
		public Set<String> getUsedItemImgPositions() {
			return usedItemImgPositions;
		}
		public void setUsedItemImgPositions(Set<String> usedItemImgPositions) {
			this.usedItemImgPositions = usedItemImgPositions;
		}

		public String[] getItemImgIDs() {
			return itemImgIDs;
		}
		public void setItemImgIDs(String[] itemImgIDs) {
			this.itemImgIDs = itemImgIDs;
		}
		public Map<String, String> getItemPropsPidToName2() {
			return itemPropsPidToName2;
		}
		public void setItemPropsPidToName2(Map<String, String> itemPropsPidToName2) {
			this.itemPropsPidToName2 = itemPropsPidToName2;
		}
		public Map<String, Map<String, String>> getItemPropsPidToPidVidToName2() {
			return itemPropsPidToPidVidToName2;
		}
		public void setItemPropsPidToPidVidToName2(
				Map<String, Map<String, String>> itemPropsPidToPidVidToName2) {
			this.itemPropsPidToPidVidToName2 = itemPropsPidToPidVidToName2;
		}
		public Map<String, String> getAllPidVidToNameForPid1() {
			return allPidVidToNameForPid1;
		}
		public void setAllPidVidToNameForPid1(Map<String, String> allPidVidToNameForPid1) {
			this.allPidVidToNameForPid1 = allPidVidToNameForPid1;
		}
		public Map<String, String> getAllPidVidToNameForPid2() {
			return allPidVidToNameForPid2;
		}
		public void setAllPidVidToNameForPid2(Map<String, String> allPidVidToNameForPid2) {
			this.allPidVidToNameForPid2 = allPidVidToNameForPid2;
		}
		public HashMap<String, String> getNumiidToTitle() {
			return numiidToTitle;
		}
		public void setNumiidToTitle(HashMap<String, String> numiidToTitle) {
			this.numiidToTitle = numiidToTitle;
		}
		public List<String> getColors() {
			return colors;
		}
		public void setColors(List<String> colors) {
			this.colors = colors;
		}
		public List<String> getTitles() {
			return titles;
		}
		public void setTitles(List<String> titles) {
			this.titles = titles;
		}
		public String getItemPicUrl() {
			return itemPicUrl;
		}
		public void setItemPicUrl(String itemPicUrl) {
			this.itemPicUrl = itemPicUrl;
		}
		public double getItemPrice() {
			return itemPrice;
		}
		public void setItemPrice(double itemPrice) {
			this.itemPrice = itemPrice;
		}
		public int getItemNum() {
			return itemNum;
		}
		public void setItemNum(int itemNum) {
			this.itemNum = itemNum;
		}
		public boolean isHasSku() {
			return hasSku;
		}
		public void setHasSku(boolean hasSku) {
			this.hasSku = hasSku;
		}
		public int getItemImgPosition() {
			return itemImgPosition;
		}
		public void setItemImgPosition(int itemImgPosition) {
			this.itemImgPosition = itemImgPosition;
		}
		public Map<String, String> getItemPropsPidToName3() {
			return itemPropsPidToName3;
		}
		public void setItemPropsPidToName3(Map<String, String> itemPropsPidToName3) {
			this.itemPropsPidToName3 = itemPropsPidToName3;
		}
		public Map<String, Map<String, String>> getItemPropsPidToPidVidToName3() {
			return itemPropsPidToPidVidToName3;
		}
		public void setItemPropsPidToPidVidToName3(
				Map<String, Map<String, String>> itemPropsPidToPidVidToName3) {
			this.itemPropsPidToPidVidToName3 = itemPropsPidToPidVidToName3;
		}
		public Map<String, Map<String, String>> getAvailablePidVid3ForPidVid1() {
			return availablePidVid3ForPidVid1;
		}
		public void setAvailablePidVid3ForPidVid1(
				Map<String, Map<String, String>> availablePidVid3ForPidVid1) {
			this.availablePidVid3ForPidVid1 = availablePidVid3ForPidVid1;
		}
		public Map<String, String> getAllPidVidToNameForPid3() {
			return allPidVidToNameForPid3;
		}
		public void setAllPidVidToNameForPid3(Map<String, String> allPidVidToNameForPid3) {
			this.allPidVidToNameForPid3 = allPidVidToNameForPid3;
		}
		public long getCid() {
			return cid;
		}
		public void setCid(long cid) {
			this.cid = cid;
		}
		public Map<Long, String> getItemCidToProps() {
			return itemCidToProps;
		}
		public void setItemCidToProps(Map<Long, String> itemCidToProps) {
			this.itemCidToProps = itemCidToProps;
		}
	}
}

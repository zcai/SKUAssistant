package com.taotu51.topclient.miscellaneous;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class SellerCatsDropDownMenu implements Serializable {

	private static final long serialVersionUID = 3282685021738041203L;
	private SellerCatsMenuData sellerCatsMenuData = new SellerCatsMenuData();

	public SellerCatsMenuData createSellerCatsDropDownMenu(DataSource dataSource) {
		/*
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		SellercatsListGetRequest req=new SellercatsListGetRequest();
		req.setNick(dataSource.getNick());
		SellercatsListGetResponse response = null;
		try {
			response = client.execute(req);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String test = response.getBody();
		*/
		String test = "{\"sellercats_list_get_response\":{\"seller_cats\":{\"seller_cat\":[{\"cid\":410119623,\"name\":\"test\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410119624,\"name\":\"subtest\",\"parent_cid\":410119623,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410119720,\"name\":\"AF\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":410120610,\"name\":\"subaf\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410120611,\"name\":\"subaf2\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":410120612,\"name\":\"subaf3\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":410119957,\"name\":\"∆‰À˚∆∑≈∆\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":410119721,\"name\":\"HCO\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"}]}}}";
	    JSONObject json = (JSONObject) JSONSerializer.toJSON( test ); 
		Map<String,List<String>> catsParentToLeaf = new HashMap<String,List<String>>();
		Map<String,String> sellerCatsCidToName = new HashMap<String,String>();
		List<String> tmpLeafCidList = new ArrayList<String>();
	    JSONArray jsonarray= json.getJSONObject("sellercats_list_get_response").getJSONObject("seller_cats").getJSONArray("seller_cat");//sandbox
	    for (int i=0;i<jsonarray.size();i++){
	        JSONObject jo = (JSONObject)jsonarray.get(i);
	        String cat_parent_cid = jo.getString("parent_cid");
	        String cat_name = jo.getString("name");
	        String cat_cid = jo.getString("cid");
	        sellerCatsCidToName.put(cat_cid, cat_name);
	        if (cat_parent_cid.equals("0")) {
	        	catsParentToLeaf.put(cat_cid, new ArrayList<String>());
	        }else {
	        	catsParentToLeaf.get(cat_parent_cid).add(cat_cid);
	        	tmpLeafCidList.add(cat_cid);
	        }
	    }
	    sellerCatsMenuData.setCatsParentToLeaf(catsParentToLeaf);
	    sellerCatsMenuData.setSellerCatsCidToName(sellerCatsCidToName);
	    sellerCatsMenuData.setLeafCidList(tmpLeafCidList);
	    return sellerCatsMenuData;
	}
	
	public class SellerCatsMenuData implements Serializable {
		private static final long serialVersionUID = 933828992607932853L;
		private Map<String,List<String>> catsParentToLeaf = new HashMap<String,List<String>>();
		private Map<String,String> sellerCatsCidToName = new HashMap<String,String>();
		private List<String> parentLevel = new ArrayList<String>();
		private List<String> leafCidList = new ArrayList<String>();

		public Map<String, List<String>> getCatsParentToLeaf() {
			return catsParentToLeaf;
		}

		public void setCatsParentToLeaf(Map<String, List<String>> catsParentToLeaf) {
			this.catsParentToLeaf = catsParentToLeaf;
		}
		public Map<String, String> getSellerCatsCidToName() {
			return sellerCatsCidToName;
		}
		public void setSellerCatsCidToName(Map<String, String> sellerCatsCidToName) {
			this.sellerCatsCidToName = sellerCatsCidToName;
		}

		public List<String> getParentLevel() {
			List<String> tmpList = new ArrayList<String>();
			tmpList.addAll(catsParentToLeaf.keySet());
			return tmpList;
		}

		public void setParentLevel(List<String> parentLevel) {
			
			this.parentLevel = parentLevel;
		}

		public List<String> getLeafCidList() {
			return leafCidList;
		}

		public void setLeafCidList(List<String> leafCidList) {
			this.leafCidList = leafCidList;
		}		
	}
}

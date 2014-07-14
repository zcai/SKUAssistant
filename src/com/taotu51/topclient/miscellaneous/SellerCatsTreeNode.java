package com.taotu51.topclient.miscellaneous;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.response.SellercatsListGetResponse;

public class SellerCatsTreeNode implements Serializable {

	private static final long serialVersionUID = 3282685021738041203L;

	public List<String> createTreeNode(TreeNode root, String cid, DataSource dataSource) {
		List<String> leafCidList = null;
		
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		SellercatsListGetRequest req=new SellercatsListGetRequest();
		req.setNick(dataSource.getNick());
		SellercatsListGetResponse response = null;
		String jsonText = null;
		try {
			response = client.execute(req);
			int count = 1;
			while (response == null) {
				response = client.execute(req);
				if (count <= 0) {
			        break;
				}
				count--;
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return leafCidList;
		}
		jsonText = response.getBody();

		
		//jsonText = "{\"sellercats_list_get_response\":{\"seller_cats\":{\"seller_cat\":[{\"cid\":410119623,\"name\":\"test\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410119624,\"name\":\"subtest\",\"parent_cid\":410119623,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410119720,\"name\":\"AF\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":410120610,\"name\":\"subaf\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":410120611,\"name\":\"subaf2\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":410120612,\"name\":\"subaf3\",\"parent_cid\":410119720,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":410119957,\"name\":\"ÆäËûÆ·ÅÆ\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":410119721,\"name\":\"HCO\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"}]}}}";	     
	    //jsonText = "{\"sellercats_list_get_response\":{\"seller_cats\":{\"seller_cat\":[{\"cid\":453095607,\"name\":\"AFÇéÂÂÌ×\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":243123784,\"name\":\"AF ÄÐ×°\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":243123785,\"name\":\"¶ÌÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":243123786,\"name\":\"³¤ÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":243123787,\"name\":\"³¤¿ã/ÎÀ¿ã\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":243123788,\"name\":\"³ÄÒÂ\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"},{\"cid\":243123789,\"name\":\"Ã«ÒÂ\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":5,\"type\":\"manual_type\"},{\"cid\":243123791,\"name\":\"ÓðÈÞÒÂ\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":6,\"type\":\"manual_type\"},{\"cid\":243123792,\"name\":\"¶Ì¿ã/ÐÝÏÐ¿ã\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":7,\"type\":\"manual_type\"},{\"cid\":243123793,\"name\":\"ÎÀÒÂ/Ã«Ã«ÎÀÒÂ\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":8,\"type\":\"manual_type\"},{\"cid\":243123794,\"name\":\"Âí¼Ð\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":9,\"type\":\"manual_type\"},{\"cid\":457027127,\"name\":\"ÄÐ¼Ð¿Ë\",\"parent_cid\":243123784,\"pic_url\":\"\",\"sort_order\":10,\"type\":\"manual_type\"},{\"cid\":243123795,\"name\":\"AF Å®×°\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":243123796,\"name\":\"¶ÌÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":243123797,\"name\":\"³¤ÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":243123798,\"name\":\"¶Ì¿ã\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":243123799,\"name\":\"³¤¿ã/ÎÀ¿ã\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"},{\"cid\":243123800,\"name\":\"³ÄÒÂ\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":5,\"type\":\"manual_type\"},{\"cid\":243123801,\"name\":\"Ã«ÒÂ\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":6,\"type\":\"manual_type\"},{\"cid\":243123803,\"name\":\"Âí¼Ð\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":7,\"type\":\"manual_type\"},{\"cid\":243123804,\"name\":\"ÎÀÒÂ/Ã«Ã«ÎÀÒÂ\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":8,\"type\":\"manual_type\"},{\"cid\":243123805,\"name\":\"ÓðÈÞÒÂ\",\"parent_cid\":243123795,\"pic_url\":\"\",\"sort_order\":9,\"type\":\"manual_type\"},{\"cid\":243123806,\"name\":\"POLO  ÄÐ×°\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"},{\"cid\":243123808,\"name\":\"³¤ÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123806,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":243123809,\"name\":\"³ÄÒÂ\",\"parent_cid\":243123806,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":243123810,\"name\":\"ÎÀÒÂ\",\"parent_cid\":243123806,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":456261706,\"name\":\"¶ÌÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123806,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"},{\"cid\":457980087,\"name\":\"Ã«ÒÂ\",\"parent_cid\":243123806,\"pic_url\":\"\",\"sort_order\":5,\"type\":\"manual_type\"},{\"cid\":243123813,\"name\":\"POLO  Å®×°\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":5,\"type\":\"manual_type\"},{\"cid\":243123814,\"name\":\"³¤ÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123813,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":243123816,\"name\":\"³ÄÒÂ\",\"parent_cid\":243123813,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":243123817,\"name\":\"ÎÀÒÂ/ÍâÌ×\",\"parent_cid\":243123813,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":243123819,\"name\":\"Å®È¹\",\"parent_cid\":243123813,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"},{\"cid\":456261707,\"name\":\"¶ÌÐäTÐô  Ô²Áì/·­Áì\",\"parent_cid\":243123813,\"pic_url\":\"\",\"sort_order\":5,\"type\":\"manual_type\"}]}}}";
		System.out.println(jsonText);
		if (jsonText == null) {
			return null;
		}
		//jsonText = "{\"sellercats_list_get_response\":{\"seller_cats\":{\"seller_cat\":[{\"cid\":410119623,\"name\":\"ÒÂ·þÐ¬Íà\",\"parent_cid\":0,\"sort_order\":1},{\"cid\":410119623,\"name\":\"Abercrombie Fitch\",\"parent_cid\":410119623}]}}}";
	    //jsonText = "{\"sellercats_list_get_response\":{\"seller_cats\":{\"seller_cat\":[{\"cid\":634426549,\"name\":\"AFÄÐ×°\",\"parent_cid\":0,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":634426550,\"name\":\"AFÎÀÒÂ\",\"parent_cid\":634426549,\"pic_url\":\"\",\"sort_order\":1,\"type\":\"manual_type\"},{\"cid\":634426551,\"name\":\"AFÎÀ¿ã\",\"parent_cid\":634426549,\"pic_url\":\"\",\"sort_order\":2,\"type\":\"manual_type\"},{\"cid\":634426552,\"name\":\"AFÓðÈÞÒÂ\",\"parent_cid\":634426549,\"pic_url\":\"\",\"sort_order\":3,\"type\":\"manual_type\"},{\"cid\":634426553,\"name\":\"AFÂí¼×\",\"parent_cid\":634426549,\"pic_url\":\"\",\"sort_order\":4,\"type\":\"manual_type\"}]}}}";
		if (jsonText.contains("sellercats_list_get_response")) {
			leafCidList = new ArrayList<String>();
	    	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonText);
	    	JSONArray jsonarray= json.getJSONObject("sellercats_list_get_response").getJSONObject("seller_cats").getJSONArray("seller_cat");//sandbox
		    for (int i=0;i<jsonarray.size();i++) {
		        JSONObject jo = (JSONObject)jsonarray.get(i);
		        String cat_parent_cid = jo.getString("parent_cid");
		        String cat_name = jo.getString("name");
		        //System.out.println(cat_name+"------------------");
		        String cat_cid = jo.getString("cid");
		        if (cat_parent_cid.equals("0")) {
		        	new DefaultTreeNode(new TreeNodeData(cat_cid, cat_name, true), root);
		        }else {
		        	//System.out.println("cat_parent_cid:"+cat_parent_cid);
		        	//System.out.println("cat_cid:"+cat_cid);
		        	List<TreeNode> children = root.getChildren();
		        	//System.out.println("children.size:"+children.size());
		        	for (TreeNode child : children) {
	        			//System.out.println("child.getData"+child.getData());
		        		if (((TreeNodeData)child.getData()).getCid().equals(cat_parent_cid)) {
		        			//System.out.println("cat_cid"+cat_cid);
		        			new DefaultTreeNode(new TreeNodeData(cat_cid, cat_name, false), child);
		        			leafCidList.add(cat_cid);
		        		}
		        	}
		        }
		    }
		    //the above code sets the first level as parents, but some of them may not have subitems
		    //so the one without subitems as non-parents
		    List<TreeNode> children = root.getChildren();
		    for (TreeNode child : children) {
		    	if (child.getChildCount() == 0) {
		    		((TreeNodeData)child.getData()).setIs_parent(false);
		    	}
		    }
	    }

	    return leafCidList;
	}
	
	public class TreeNodeData implements Serializable {
		private static final long serialVersionUID = 933828992607932853L;
		private String cid;
		private String name;
		private boolean is_parent;
		
		public TreeNodeData (String cid, String name, boolean is_parent) {
			this.cid = cid;
			this.name = name;
			this.is_parent = is_parent;
		}
		
		public String getCid() {
			return cid;
		}
		public void setCid(String cid) {
			this.cid = cid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isIs_parent() {
			return is_parent;
		}
		public void setIs_parent(boolean is_parent) {
			this.is_parent = is_parent;
		}
	}
}

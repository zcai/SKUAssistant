package com.taotu51.topclient.miscellaneous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItempropsGetRequest;
import com.taobao.api.response.ItempropsGetResponse;
import com.taotu51.topclient.miscellaneous.RetrieveItem.ItemData;


public class RetrieveItemProps {
	//taobao.itemprops.get
		public static void retrieveItemProps(DataSource dataSource, ItemData itemData) {
			String jsonTxt = null;
			if ((itemData.getItemCidToProps().get(new Long(itemData.getCid())) == null) || (itemData.getItemCidToProps().get(new Long(itemData.getCid())).equals(""))) {
				TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
				ItempropsGetRequest req=new ItempropsGetRequest();
				req.setFields("pid,name,must,multi,prop_values");
				req.setCid(itemData.getCid());
				
				try {
					ItempropsGetResponse response = client.execute(req);
					jsonTxt = response.getBody();
					int count = 1;
					while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
						response = client.execute(req);
						jsonTxt = response.getBody();
						if (count <= 0) {
							break;
						}
						count--;	
						Thread.sleep(1000);
					}
					if (jsonTxt == null) {
						
					}
					else if (jsonTxt.contains("error")) {
						
					}
					itemData.getItemCidToProps().put(new Long(itemData.getCid()), jsonTxt);
				} catch (Exception e) {
					itemData.getItemCidToProps().put(new Long(itemData.getCid()), "");
					//e.printStackTrace();
				}
			}
			else {
				jsonTxt = itemData.getItemCidToProps().get(new Long(itemData.getCid()));
			}
			if (jsonTxt.contains("itemprops_get_response")) {
				System.out.println("response body from retrieveItemProps ---------------- "+jsonTxt);
			    JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt ); 
			    JSONArray item_prop_array = json.getJSONObject("itemprops_get_response").getJSONObject("item_props").getJSONArray("item_prop");
				Map<String,Map<String,String>> tmpItemPropsPidToPidVidToName = new HashMap<String,Map<String,String>>();//pid -> pidvid -> name(vid's name)
				Map<String,String> tmpItemPropsPidToName = new HashMap<String,String>();//pid -> name(pid's name)
				
				Map<String,String> tmpItemPropsPidToName2 = new HashMap<String,String>();//pid -> name(pid's name)
				Map<String,Map<String,String>> tmpItemPropsPidToPidVidToName2 = new HashMap<String,Map<String,String>>();//pid -> pidvid -> name(vid's name)
		
				for (int i=0;i<item_prop_array.size();i++){
			    	JSONObject jo = (JSONObject)item_prop_array.get(i);
			    		String pid = jo.getString("pid");
			    		String pidName = jo.getString("name");
			    		//System.out.println("pid------"+pid);
			    		//try {
			    		if (jo.toString().indexOf("\"prop_value\":[") != -1) {
				    	    JSONArray prop_value_array = jo.getJSONObject("prop_values").getJSONArray("prop_value");
				    		Map<String,String> tmpItemPropsPidVidToName = new HashMap<String,String>();//pidvid -> name(vid's name)
				    		Map<String,String> tmpItemPropsPidVidToName2 = new HashMap<String,String>();//pidvid -> name(vid's name)
				    	    for (int j=0;j<prop_value_array.size();j++){
				    	    	JSONObject jovidname = (JSONObject)prop_value_array.get(j);
				    	    	String vid = jovidname.getString("vid");
				    	    	//System.out.println("vid"+vid);
				    	    	String vidName = jovidname.getString("name");
				    	    	tmpItemPropsPidVidToName.put(pid+":"+vid, vidName);
				    	    	tmpItemPropsPidVidToName2.put(pid+":"+vid, vidName);
				    	    }
				    	    tmpItemPropsPidToName.put(pid, pidName);
				    	    tmpItemPropsPidToPidVidToName.put(pid,tmpItemPropsPidVidToName);
				    	    
				    	    tmpItemPropsPidToName2.put(pid, pidName);
				    	    tmpItemPropsPidToPidVidToName2.put(pid,tmpItemPropsPidVidToName2);
				    	}
			    		else {
			    		//catch(Exception e) {
			    			//try {//one prop_value
			    			if (jsonTxt.indexOf("\"prop_value\":{") != -1) {
				    			JSONObject prop_value = jo.getJSONObject("prop_values").getJSONObject("prop_value");
					    		Map<String,String> tmpItemPropsPidVidToName = new HashMap<String,String>();//pidvid -> name(vid's name)
					    	    JSONObject jovidname = prop_value;
					    	    String vid = jovidname.getString("vid");
					    	    //System.out.println("vid"+vid);
					    	    String vidName = jovidname.getString("name");
					    	    tmpItemPropsPidVidToName.put(pid+":"+vid, vidName);
					    	    tmpItemPropsPidToName.put(pid, pidName);
					    	    tmpItemPropsPidToPidVidToName.put(pid,tmpItemPropsPidVidToName);
					    	    tmpItemPropsPidToPidVidToName2.put(pid,tmpItemPropsPidVidToName);
					    	    tmpItemPropsPidToName2.put(pid, pidName);
				    	    }
			    			//catch(Exception e2) {//no prop_values
			    				
			    			//}
			    		}
			    }
			    itemData.setItemPropsPidToName(tmpItemPropsPidToName);//prepare values for adding new sku under the same pidVid1
			    itemData.setItemPropsPidToPidVidToName(tmpItemPropsPidToPidVidToName);
			    
			    itemData.setItemPropsPidToName2(tmpItemPropsPidToName2);//prepare values for adding new item with different sku pidVid1
			    itemData.setItemPropsPidToPidVidToName2(tmpItemPropsPidToPidVidToName2);
			}
			///////
			Map<String, String> allPidVidToNameForPid1 = new HashMap<String,String>();//to add a new sku such as an item of a new color, exclude the existing ones, such as the same color
			Map<String, String> allPidVidToNameForPid2 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
			Map<String, String> allPidVidToNameForPid3 = new HashMap<String,String>();//to add a new sku, such as a new size for an existing sku
			Map<String, Map<String,String>> tmpAvailablePidVid2ForPidVid1 = new HashMap<String,Map<String,String>>();//to add a new sku, such as a new size for an existing sku
			Map<String, Map<String,String>> tmpAvailablePidVid3ForPidVid1 = new HashMap<String,Map<String,String>>();//to add a new sku, such as a new size for an existing sku
			Object[] pidVid1List = itemData.getPidVid1ToPidVid2().keySet().toArray();
			String [] pid1_vid1 = pidVid1List[0].toString().split(":");
	    	allPidVidToNameForPid1 = itemData.getItemPropsPidToPidVidToName().get(pid1_vid1[0]);//all pidvid to name in itemprops for pid1

			Iterator<Entry<String, ArrayList<String>>> pidVid1ToPidVid2Entry = itemData.getPidVid1ToPidVid2().entrySet().iterator();
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
		}
}

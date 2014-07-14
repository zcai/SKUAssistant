package com.taotu51.topclient.miscellaneous;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taotu51.topclient.miscellaneous.RetrieveItem.ItemData;


public class ForSaleItemsData implements Serializable {
	private static final long serialVersionUID = -3513183650965730187L;
	private List<String> numiids;
	private transient Map<String,ItemData> numiidToItemData = new HashMap<String,ItemData>();

	public List<String> getNumiids() {
		return numiids;
	}
	public void setNumiids(List<String> numiids) {
		this.numiids = numiids;
	}
	public Map<String, ItemData> getNumiidToItemData() {
		return numiidToItemData;
	}
	public void setNumiidToItemData(Map<String, ItemData> numiidToItemData) {
		this.numiidToItemData = numiidToItemData;
	}
}


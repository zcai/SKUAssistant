package com.taotu51.topclient.miscellaneous;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ItemAbstractData implements Serializable {
	private static final long serialVersionUID = -3399715684858296722L;
	private HashMap<String,String> numiidToTitle = new HashMap<String,String>();
	private HashMap<String,String> numiidToItemPicUrl = new HashMap<String,String>();
	private HashMap<String,Double> numiidToItemPrice = new HashMap<String,Double>();
	private HashMap<String,Integer> numiidToItemNum = new HashMap<String,Integer>();
	private HashMap<String,String> titleToNumiid = new HashMap<String,String>();
	private HashMap<String,Boolean> numiidToHasSku = new HashMap<String,Boolean>();
	private List<String> colors;
	private List<String> titles;
	private Map<String, String> numiidToitemImgPosition;//current position of item_img in item_imgs
	public HashMap<String, String> getNumiidToTitle() {
		return numiidToTitle;
	}
	public void setNumiidToTitle(HashMap<String, String> numiidToTitle) {
		this.numiidToTitle = numiidToTitle;
	}
	public HashMap<String, String> getNumiidToItemPicUrl() {
		return numiidToItemPicUrl;
	}
	public void setNumiidToItemPicUrl(HashMap<String, String> numiidToItemPicUrl) {
		this.numiidToItemPicUrl = numiidToItemPicUrl;
	}
	public HashMap<String, Double> getNumiidToItemPrice() {
		return numiidToItemPrice;
	}
	public void setNumiidToItemPrice(HashMap<String, Double> numiidToItemPrice) {
		this.numiidToItemPrice = numiidToItemPrice;
	}
	public HashMap<String, Integer> getNumiidToItemNum() {
		return numiidToItemNum;
	}
	public void setNumiidToItemNum(HashMap<String, Integer> numiidToItemNum) {
		this.numiidToItemNum = numiidToItemNum;
	}
	public HashMap<String, String> getTitleToNumiid() {
		return titleToNumiid;
	}
	public void setTitleToNumiid(HashMap<String, String> titleToNumiid) {
		this.titleToNumiid = titleToNumiid;
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

	public HashMap<String, Boolean> getNumiidToHasSku() {
		//System.out.println(numiidToHasSku.keySet());
		//System.out.println(numiidToHasSku.values());
		return numiidToHasSku;
	}
	public void setNumiidToHasSku(HashMap<String, Boolean> numiidToHasSku) {
		this.numiidToHasSku = numiidToHasSku;
	}

	public Map<String, String> getNumiidToitemImgPosition() {
		return numiidToitemImgPosition;
	}
	public void setNumiidToitemImgPosition(
			Map<String, String> numiidToitemImgPosition) {
		this.numiidToitemImgPosition = numiidToitemImgPosition;
	}
}
package com.taotu51.topclient.miscellaneous;

public class ImgsToBeWaterMarked {
	private String [] imgIDs;//if to be watermarked images are mainPic or additionalPic or both
	private String [] urls;//image url in taobao
	private String [] properties;//if to be watermarked images are propPic
	public String[] getImgIDs() {
		return imgIDs;
	}
	public void setImgIDs(String[] imgIDs) {
		this.imgIDs = imgIDs;
	}
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	public String[] getProperties() {
		return properties;
	}
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
	
	
}

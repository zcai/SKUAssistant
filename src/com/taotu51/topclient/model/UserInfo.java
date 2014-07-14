package com.taotu51.topclient.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userInfo")
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 80853223946776412L;
	//private String userID;
	private String firstName;
	private String lastName;
	private String email;
	private String telephone;
	private String address;
	private String userNick;
	//private String sessionKey;
	private boolean picWaterMarkCheckbox;
	private String waterMarkImg1;
	//private String waterMarkImg2;
	//private String waterMarkImg3;
	//private String selectedWaterMarkImg;
	private boolean textWaterMarkCheckbox;
	private String textWaterMarkContent;
	private String textWaterMarkContentColor;
	private String textWaterMarkContentFont;
	private int textWaterMarkContentSize;
	private int waterMarkPosition;
	/*
	@Id
	@Column(name="userID", unique = true, nullable = false)
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	*/

	@Id
	@Column(name="user_nick", unique = false, nullable = true)
	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	/*
	@Column(name="session_key", unique = true, nullable = false)
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	*/
	
	@Column(name="first_name", unique = false, nullable = true)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="last_name", unique = false, nullable = true)
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
	@Column(name="email", unique = true, nullable = true)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}

	@Column(name="telephone", unique = false, nullable = true)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name="address", unique = false, nullable = true)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="water_mark_img1", unique = false, nullable = true)
	public String getWaterMarkImg1() {
		return waterMarkImg1;
	}

	public void setWaterMarkImg1(String waterMarkImg1) {
		this.waterMarkImg1 = waterMarkImg1;
	}
/*
	@Column(name="water_mark_img2", unique = false, nullable = true)
	public String getWaterMarkImg2() {
		return waterMarkImg2;
	}

	public void setWaterMarkImg2(String waterMarkImg2) {
		this.waterMarkImg2 = waterMarkImg2;
	}

	@Column(name="water_mark_img3", unique = false, nullable = true)
	public String getWaterMarkImg3() {
		return waterMarkImg3;
	}

	public void setWaterMarkImg3(String waterMarkImg3) {
		this.waterMarkImg3 = waterMarkImg3;
	}

	@Column(name="selected_water_mark_img", unique = false, nullable = true)
	public String getSelectedWaterMarkImg() {
		return selectedWaterMarkImg;
	}

	public void setSelectedWaterMarkImg(String selectedWaterMarkImg) {
		this.selectedWaterMarkImg = selectedWaterMarkImg;
	}
*/
	@Column(name="pic_water_mark_checkbox", unique = false, nullable = true, columnDefinition = "BIT")
	public boolean isPicWaterMarkCheckbox() {
		return picWaterMarkCheckbox;
	}

	public void setPicWaterMarkCheckbox(boolean picWaterMarkCheckbox) {
		this.picWaterMarkCheckbox = picWaterMarkCheckbox;
	}

	@Column(name="text_water_mark_checkbox", unique = false, nullable = true, columnDefinition = "BIT")
	public boolean isTextWaterMarkCheckbox() {
		return textWaterMarkCheckbox;
	}

	public void setTextWaterMarkCheckbox(boolean textWaterMarkCheckbox) {
		this.textWaterMarkCheckbox = textWaterMarkCheckbox;
	}

	@Column(name="text_water_mark_content", unique = false, nullable = true)
	public String getTextWaterMarkContent() {
		return textWaterMarkContent;
	}

	public void setTextWaterMarkContent(String textWaterMarkContent) {
		this.textWaterMarkContent = textWaterMarkContent;
	}

	@Column(name="text_water_mark_content_color", unique = false, nullable = true)
	public String getTextWaterMarkContentColor() {
		return textWaterMarkContentColor;
	}

	public void setTextWaterMarkContentColor(String textWaterMarkContentColor) {
		this.textWaterMarkContentColor = textWaterMarkContentColor;
	}

	@Column(name="text_water_mark_content_font", unique = false, nullable = true)
	public String getTextWaterMarkContentFont() {
		return textWaterMarkContentFont;
	}

	public void setTextWaterMarkContentFont(String textWaterMarkContentFont) {
		this.textWaterMarkContentFont = textWaterMarkContentFont;
	}

	@Column(name="text_water_mark_content_size", unique = false, nullable = true)
	public int getTextWaterMarkContentSize() {
		return textWaterMarkContentSize;
	}

	public void setTextWaterMarkContentSize(int textWaterMarkContentSize) {
		this.textWaterMarkContentSize = textWaterMarkContentSize;
	}

	@Column(name="water_mark_position", unique = false, nullable = true)
	public int getWaterMarkPosition() {
		return waterMarkPosition;
	}

	public void setWaterMarkPosition(int waterMarkPosition) {
		this.waterMarkPosition = waterMarkPosition;
	}
	
	
}
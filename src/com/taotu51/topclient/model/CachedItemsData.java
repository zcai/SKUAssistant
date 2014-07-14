package com.taotu51.topclient.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
	@Entity
	@Table(name="cachedItemsData")
	public class CachedItemsData implements Serializable {

		private static final long serialVersionUID = -2064952730536029247L;
		private String numiid;
		private String userNick;
		private byte [] itemsDetailData;

		@Id
		@Column(name="numiid", unique = true, nullable = false)
		public String getNumiid() {
			return numiid;
		}

		public void setNumiid(String numiid) {
			this.numiid = numiid;
		}

		@Column(name="user_nick", unique = false, nullable = false)
		public String getUserNick() {
			return userNick;
		}

		public void setUserNick(String userNick) {
			this.userNick = userNick;
		}
		
		@Column(name="items_detail_data", unique = true, nullable = false, columnDefinition = "longblob")
		public byte[] getItemsDetailData() {
			return itemsDetailData;
		}

		public void setItemsDetailData(byte[] itemsDetailData) {
			this.itemsDetailData = itemsDetailData;
		}
	}

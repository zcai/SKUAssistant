package com.taotu51.topclient.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
	@Entity
	@Table(name="cachedItems")
	public class CachedItems implements Serializable {
		private static final long serialVersionUID = 5188047270649088496L;
		private String leafNodeCid;//seller cats leaf node cid, corresponding to treenode menu
		//private String sessionKey;
		private String userNick;//foreign key in UserInfo
		private byte [] itemsData;

		@Id
		@Column(name="leaf_node_cid", unique = true, nullable = false)
		public String getLeafNodeCid() {
			return leafNodeCid;
		}

		public void setLeafNodeCid(String leafNodeCid) {
			this.leafNodeCid = leafNodeCid;
		}

		@Column(name="user_nick", unique = false, nullable = false)
		public String getUserNick() {
			return userNick;
		}

		public void setUserNick(String userNick) {
			this.userNick = userNick;
		}

		@Column(name="items_data", unique = false, nullable = false, columnDefinition = "longblob")
		public byte[] getItemsData() {
			return itemsData;
		}

		public void setItemsData(byte[] itemsData) {
			this.itemsData = itemsData;
		}
			
	}

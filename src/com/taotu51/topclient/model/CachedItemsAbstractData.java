package com.taotu51.topclient.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
	@Entity
	@Table(name="cachedItemsAbstractData")
	public class CachedItemsAbstractData implements Serializable {

		private static final long serialVersionUID = -2064952730536029247L;
		private String numiid;
		private byte [] itemsAbstractData;

		@Id
		@Column(name="numiid", unique = true, nullable = false)
		public String getNumiid() {
			return numiid;
		}

		public void setNumiid(String numiid) {
			this.numiid = numiid;
		}

		@Column(name="items_abstract_data", unique = true, nullable = false, columnDefinition = "longblob")
		public byte[] getItemsAbstractData() {
			return itemsAbstractData;
		}

		public void setItemsAbstractData(byte[] itemsAbstractData) {
			this.itemsAbstractData = itemsAbstractData;
		}
	}

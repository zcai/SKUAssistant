package com.taotu51.topclient.managed.bean;


import java.io.Serializable;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.TreeNode;
import org.primefaces.model.DefaultTreeNode;

import com.taotu51.topclient.miscellaneous.DataSource;
import com.taotu51.topclient.miscellaneous.RetrieveItem;
import com.taotu51.topclient.miscellaneous.SellerCatsTreeNode;
import com.taotu51.topclient.miscellaneous.RetrieveItem.ItemData;







@ManagedBean(name="test2Bean")
@ViewScoped
public class Test2Bean implements Serializable {
	
	private static final long serialVersionUID = 7911739142723603954L;
	@ManagedProperty(value = "#{userData}")
	private DataSource dataSource;


	
	private TreeNode treeNode; //use tree structure to show seller cats 
	private final String PARENT_CID = "0";

	RetrieveItem.ItemData itemData;

	Map<String,ItemData> numiidToItemData = new HashMap<String,ItemData>();

	private String sessionKey;//to be passed from callback parameter
	String createdFileName;//uploaded image file name	

	
	public Test2Bean() {//constructor

	}


	
	//a method annotated with @PostConstruct to perform actions directly after construction and dependency injection
	@PostConstruct
	public void init() {

		//dataSource.setNick("伊洛小象");
		//dataSource.setNick("af时代广场");
		//dataSource.setNick("xiaoxue0316");
		dataSource.setNick("af代购站");
		//dataSource.setNick("shoping520_2009");
		//dataSource.setNick("sandbox_cai");
		System.out.println(dataSource.getUrl());
		System.out.println(dataSource.getAppkey());
		System.out.println(dataSource.getSecret());
		System.out.println(dataSource.getNick());
		//sessionKey = "6201d17dc908f0e183e9d551bdfebba1625650b77b0b8fc61508127";//xiaoxue0316
		//sessionKey = "6201d095b3fda71f6ZZ313e21baa7c4883435f5fa73101862592574";//伊洛小象
		sessionKey = "6102828835511b7425ce2cfd30e4148a1163138412e39453600301501";//sandbox_cai

		dataSource.setSessionKey(sessionKey);

			SellerCatsTreeNode sellerCatsTreeNode = new SellerCatsTreeNode();
			treeNode = new DefaultTreeNode(sellerCatsTreeNode.new TreeNodeData("0","root",true), null);
			List<String> leafCids = sellerCatsTreeNode.createTreeNode(treeNode,PARENT_CID,dataSource);

	    System.out.println("initializing finished--------------");
	}

	public TreeNode getTreeNode() {
		return treeNode;
	}



	public void setTreeNode(TreeNode treeNode) {
		this.treeNode = treeNode;
	}



	public void doNothing() {
		System.out.println("doNothing called-------------");
	}

	public DataSource getDataSource() {
		return dataSource;
	}



	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

}
					
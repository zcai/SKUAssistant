package com.taotu51.topclient.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.TreeNode;

@ManagedBean(name="importCatsTreeBean")
@ViewScoped
public class BuildCatsTree implements Serializable {

	private static final long serialVersionUID = 6274849870231568701L;
	private TreeNode root;
	public BuildCatsTree() {
		try {
	       FileInputStream fileIn = new FileInputStream("C:\\Users\\rezca\\Desktop\\catsTree.er");
	       ObjectInputStream in = new ObjectInputStream(fileIn);
	       root = (TreeNode) in.readObject();
	       in.close();
	       fileIn.close();
	    }
		catch(Exception i){
		}
	}
	
	public void doSomething() {
		
	}
	public TreeNode getRoot() {
		System.out.println("xxxxxxxxxxxx");
		return root;
	}
}

package com.taotu51.topclient.managed.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.apache.commons.io.FileUtils;

@ManagedBean(name="recycledImgBean")
@SessionScoped
public class RecycledImgBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1967298758655311383L;
	File catalinaBase;
	private String userNick;
	List<String> recycledImgs;
	
	public void imgRecycle(ComponentSystemEvent cse) {

	    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
	    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
	    if (!dir.exists()) {
	    	dir.mkdirs();
	    }
	    System.out.println("============="+dir.toString());
	    setRecycledImgs(listFilesForFolder(dir));
	    System.out.println(recycledImgs.size()+"------------recycledImgs size---------------");
	}

	private List<String> listFilesForFolder(File folder) {
		List<String> fileNames = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println("recycled imgs file names:"+fileEntry.getName());
	            fileNames.add(fileEntry.getName());
	        }
	    }
	    System.out.println(fileNames.size()+"-----------fileNames size----------------");
	    return fileNames;
	}
	public void emptyRecycleBin() {
	    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
	    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
	    try {
			FileUtils.cleanDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功删除回收站中的所有图片！");  
        FacesContext.getCurrentInstance().addMessage(null, msg); 
	}

	public File getCatalinaBase() {
		return catalinaBase;
	}

	public void setCatalinaBase(File catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	
	public List<String> getRecycledImgs() {
	    System.out.println(recycledImgs.size()+"-----------getRecycledImgs size----------------");
		return recycledImgs;
	}

	public void setRecycledImgs(List<String> recycledImgs) {
	    System.out.println(recycledImgs.size()+"-----------setRecycledImgs size----------------");
		this.recycledImgs = recycledImgs;
	}
	
}

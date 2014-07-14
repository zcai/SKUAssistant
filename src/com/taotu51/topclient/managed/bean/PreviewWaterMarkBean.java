package com.taotu51.topclient.managed.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.exception.FileUploadException;

@ManagedBean
@SessionScoped
public class PreviewWaterMarkBean implements Serializable {
	private static final long serialVersionUID = -4842235619395405317L;
	private byte [] waterMarkedImgData;
    public void paint(OutputStream stream, Object object) throws IOException {
    	System.out.println("paint called---------------------");
        try{
        	stream.write(waterMarkedImgData);
        }
        catch(FileUploadException e){
            //e.printStackTrace();
        }
        stream.close();
    }
    
    @PostConstruct
    public void init() {
	    File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
	    System.out.println("catalinaBase is=================="+catalinaBase);
	    waterMarkedImgData = imgToByteArray(new File(catalinaBase, "webapps/SKUAssistant/images/watermarkpreview.jpg"));
    }
	public byte[] getWaterMarkedImgData() {
		return waterMarkedImgData;
	}
	public void setWaterMarkedImgData(byte[] waterMarkedImgData) {
		this.waterMarkedImgData = waterMarkedImgData;
	}
	private byte[] imgToByteArray(File imgFile) {
		System.out.println("imgToByteArray input file name=="+imgFile.toString());
        FileInputStream fis;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(imgFile);
	        //create FileInputStream which obtains input bytes from a file in a file system
	        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.
	        byte[] buf = new byte[1024];
	        try {
	            for (int readNum; (readNum = fis.read(buf)) != -1;) {
	                //Writes to this byte array output stream
	                bos.write(buf, 0, readNum); 
	                //System.out.println("read " + readNum + " bytes,");
	            }
	        } catch (IOException ex) {
	        	ex.printStackTrace();
	        }
	        fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        byte[] bytes = bos.toByteArray();
		return bytes;
	}
}

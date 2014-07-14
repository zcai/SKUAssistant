package com.taotu51.topclient.managed.bean;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
 


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
 


import org.primefaces.event.SlideEndEvent;
import org.richfaces.exception.FileUploadException;

@ManagedBean
@SessionScoped
public class EditImgBean implements Serializable {

    private static final long serialVersionUID = 2931291282497149959L;
    private String imgUrlStr;
    private byte [] imgContent;
    private boolean editImgAction = false;
    private int valueAll = 50;
    private int valueAllChange;
    private int valueR = 50;
    private int valueRChange;
    private int valueG = 50;
    private int valueGChange;
    private int valueB = 50;
    private int valueBChange;
    private String which;
    private byte [] imgContentCopy;
    public void paint(OutputStream stream, Object object) throws IOException {
        try{
            //stream.write(imgToByteArray(new File("C:\\Users\\rezca\\Desktop\\img\\cloud3.jpg")));
            //System.out.println("imgUrlStr==="+imgUrlStr);
            if (editImgAction) {
                stream.write(imgContent);
                editImgAction = false;
            }
            else {
            	if (imgUrlStr != null) {
	                imgContent = remoteImgToByteArray(imgUrlStr);
	                stream.write(imgContent);
	                valueAll = 50;
	                valueR = 50;
	                valueG = 50;
	                valueB = 50;
	                imgContentCopy = new byte[imgContent.length];
	                for (int i=0;i<imgContent.length;i++) {
	                	imgContentCopy[i] = imgContent[i];
	                }
            	}
            }
        }
        catch(FileUploadException e){
            e.printStackTrace();
        }
        stream.close();
    }
    
    public String adjustBrightness(SlideEndEvent event) {
        System.out.println(event.getValue());
        System.out.println(valueAllChange);
        which = (String)event.getComponent().getAttributes().get("which");
        if (which.equals("All")) {
            setValueAll(event.getValue());
            if (valueAllChange < 0) {
            	byte [] tmp = new byte[imgContentCopy.length];
                for (int i=0;i<imgContentCopy.length;i++) {
                	tmp[i] = imgContentCopy[i];
                }
                setImgContent(tmp);
            }
        }
        else if (which.equals("R")) {
            setValueR(event.getValue());
            if (valueGChange < 0) {
            	byte [] tmpR = new byte[imgContentCopy.length];
                for (int i=0;i<imgContentCopy.length;i++) {
                	tmpR[i] = imgContentCopy[i];
                }
                setImgContent(tmpR);
            }
        }
        else if (which.equals("G")) {
            setValueG(event.getValue());
            if (valueGChange < 0) {
            	byte [] tmpG = new byte[imgContentCopy.length];
                for (int i=0;i<imgContentCopy.length;i++) {
                	tmpG[i] = imgContentCopy[i];
                }
                setImgContent(tmpG);
            }
        }
        else if (which.equals("B")) {
            setValueB(event.getValue());
            if (valueBChange < 0) {
            	byte [] tmpB = new byte[imgContentCopy.length];
                for (int i=0;i<imgContentCopy.length;i++) {
                	tmpB[i] = imgContentCopy[i];
                }
                setImgContent(tmpB);
            }
        }
        InputStream is = new ByteArrayInputStream(imgContent);
        System.out.println(which);
        editImgAction = true;
        float brightenFactor = 1.0f;

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RescaleOp op = null;
        if (which.equals("All")) {
            op = new RescaleOp(brightenFactor, valueAllChange, null);
        }
        else if (which.equals("R")) {
            op = new RescaleOp(new float[]{brightenFactor,1.0f,1.0f}, new float[]{valueRChange,0,0}, null);
        }
        else if (which.equals("G")) {
            op = new RescaleOp(new float[]{1.0f,brightenFactor,1.0f}, new float[]{0,valueGChange,0}, null);
        }
        else if (which.equals("B")) {
            op = new RescaleOp(new float[]{1.0f,1.0f,brightenFactor}, new float[]{0,0,valueBChange}, null);
        }
        
        bufferedImage = op.filter(bufferedImage, bufferedImage);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgContent = baos.toByteArray();
        return null;
    }
    
    public String adjustBrightnessR() {
        editImgAction = true;
        float brightenFactor = 1.2f;

        InputStream is = new ByteArrayInputStream(imgContent);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RescaleOp op = new RescaleOp(new float[]{brightenFactor,1.0f,1.0f}, new float[]{0,0,0}, null);
        bufferedImage = op.filter(bufferedImage, bufferedImage);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgContent = baos.toByteArray();
        return null;
    }

    
    
    private byte[] imgToByteArray(File imgFile) {        
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
                    System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
    
    public byte[] remoteImgToByteArray(String urlStr) {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        URL url;
        InputStream is = null;
        try {
            url = new URL(urlStr);
            is = url.openStream ();
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                bais.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
          e.printStackTrace ();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("length================="+bais.toByteArray().length);
        return bais.toByteArray();
    }

    public void reset() {
        valueAll = 50;
        valueR = 50;
        valueG = 50;
        valueB = 50;
    	byte [] tmp = new byte[imgContentCopy.length];
        for (int i=0;i<imgContentCopy.length;i++) {
        	tmp[i] = imgContentCopy[i];
        }
        setImgContent(tmp);
    }
    public String getImgUrlStr() {
        return imgUrlStr;
    }

    public void setImgUrlStr(String imgUrlStr) {
        this.imgUrlStr = imgUrlStr;
        System.out.println("setImgUrlStr called======"+imgUrlStr);
    }

    public byte[] getImgContent() {
        return imgContent;
    }

    public void setImgContent(byte[] imgContent) {
        this.imgContent = imgContent;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public int getValueAll() {
        return valueAll;
    }

    public void setValueAll(int valueAll) {
        valueAllChange = (valueAll - this.valueAll)/2;
        this.valueAll = valueAll;
    }

    public int getValueR() {
        return valueR;
    }

    public void setValueR(int valueR) {
        valueRChange = (valueR - this.valueR)/2;
        this.valueR = valueR;
    }

    public int getValueG() {
        return valueG;
    }

    public void setValueG(int valueG) {
        valueGChange = (valueG - this.valueG)/2;
        this.valueG = valueG;
    }

    public int getValueB() {
        return valueB;
    }

    public void setValueB(int valueB) {
        valueBChange = (valueB - this.valueB)/2;
        this.valueB = valueB;
    }

    public String getWhich() {
        return which;
    }

    public void setWhich(String which) {
        System.out.println(which+"----------------");
        this.which = which;
    }
    
    
}
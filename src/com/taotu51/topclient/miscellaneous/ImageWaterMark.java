package com.taotu51.topclient.miscellaneous;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageWaterMark {
	public static byte[] addWaterMark(byte[] imageData, String waterMarkPicOrText, String waterMarkType, String textWaterMarkContentColor, String userNick, int waterMarkFontSize, int waterMarkPosition) {
		if ((waterMarkPicOrText == null) || (waterMarkPicOrText.equals(""))) {
			return imageData;
		}
		//double RATIO = 0.57;//340/600, 340 is the width of notexist.png, 600 is the width of 
		//images of generated. When previewing watermark, the ratio should be taken into account to show the proper size of watermark
		
		//File origFile = new File("C:\\Users\\rezca\\Desktop\\img\\cloud.gif");
	    //ImageIcon image = new ImageIcon(origFile.getPath());
	    ImageIcon image = new ImageIcon(imageData);
	    int imageWidth = image.getIconWidth();
	    int imageHeight = image.getIconHeight();
	    // create BufferedImage object of same width and height as of original image
	    BufferedImage bufferedImage = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);
	
	    // create graphics object and add original image to it
	    Graphics graphics = bufferedImage.getGraphics();
	    graphics.drawImage(image.getImage(), 0, 0, null);
	
	    if (waterMarkType.equals("text")) {
		    //add text watermark
		    // set font for the watermark text
		    graphics.setFont(new Font("Arial", Font.BOLD, waterMarkFontSize));
		    //graphics.setColor(Color.black);
		    
		    Color color = hex2Rgb("#"+textWaterMarkContentColor);
		    graphics.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),80));
		    //unicode characters for (c) is \u00a9
		    //String watermarkText = "\u00a9 52hco.taobao.com";
		    String watermarkText = waterMarkPicOrText;
		    //get width of the string of a specific size
		    int strWidth = graphics.getFontMetrics().stringWidth(watermarkText);
		    // add the watermark text
		    System.out.println(imageWidth+"---"+strWidth+"--");
		    if (waterMarkPosition == 1) {
		    	graphics.drawString(watermarkText, 0, 1*imageHeight/6);
		    }
		    else if (waterMarkPosition == 4) {
		    	graphics.drawString(watermarkText, 0, 1*imageHeight/2);
		    }		    
		    else if (waterMarkPosition == 7) {
		    	graphics.drawString(watermarkText, 0, 5*imageHeight/6);
		    }	
		    else if (waterMarkPosition == 2) {
		    	graphics.drawString(watermarkText, imageWidth/2-strWidth/2, 1*imageHeight/6);
		    }			    
		    else if (waterMarkPosition == 5) {
		    	graphics.drawString(watermarkText, imageWidth/2-strWidth/2, 1*imageHeight/2);
		    }	
		    else if (waterMarkPosition == 8) {
		    	graphics.drawString(watermarkText, imageWidth/2-strWidth/2, 5*imageHeight/6);
		    }
		    else if (waterMarkPosition == 3) {
		    	graphics.drawString(watermarkText, imageWidth-strWidth, 1*imageHeight/6);
		    }
		    else if (waterMarkPosition == 6) {
		    	graphics.drawString(watermarkText, imageWidth-strWidth, 1*imageHeight/2);
		    }
		    else if (waterMarkPosition == 9) {
		    	graphics.drawString(watermarkText, imageWidth-strWidth, 5*imageHeight/6);
		    }
	    }
	    
	    else if (waterMarkType.equals("pic")) {
	    
		    //add image watermark
		    //File waterMarkFile = new File("C:\\Users\\rezca\\Desktop\\img\\minus.PNG");
		    //ImageIcon waterMark = new ImageIcon(waterMarkFile.getPath());
		    File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
		    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/waterMarkImg");
		    //System.out.println(dir+"/"+new File(waterMarkPicOrText).getPath());
		    ImageIcon waterMark = new ImageIcon(dir+"/"+userNick+"/"+waterMarkPicOrText);
		    System.out.println("ImageIcon===="+dir+"/"+userNick+"/"+waterMarkPicOrText);
		    int waterMarkWidth = waterMark.getIconWidth();
		    int waterMarkHeight = waterMark.getIconHeight();
		
		    // create graphics object and add original image to it
	    	//graphics.drawImage(waterMark.getImage(), imageWidth-waterMarkWidth, (imageHeight-waterMarkHeight)*2/3, null);
		    if (waterMarkPosition == 1) {
		    	graphics.drawImage(waterMark.getImage(), 0, (imageHeight)*1/6, null);
		    }
		    else if (waterMarkPosition == 4) {
		    	graphics.drawImage(waterMark.getImage(), 0, (imageHeight)*1/2, null);
		    }
		    else if (waterMarkPosition == 7) {
		    	graphics.drawImage(waterMark.getImage(), 0, (imageHeight)*5/6, null);
		    }		    
		    if (waterMarkPosition == 2) {
		    	graphics.drawImage(waterMark.getImage(), (imageWidth-waterMarkWidth)/2, (imageHeight)*1/6, null);
		    }
		    if (waterMarkPosition == 5) {
		    	graphics.drawImage(waterMark.getImage(), (imageWidth-waterMarkWidth)/2, (imageHeight-waterMarkHeight)/2, null);
		    }
		    if (waterMarkPosition == 8) {
		    	graphics.drawImage(waterMark.getImage(), (imageWidth-waterMarkWidth)/2, (imageHeight)*5/6, null);
		    }
		    if (waterMarkPosition == 3) {
		    	graphics.drawImage(waterMark.getImage(), imageWidth-waterMarkWidth, (imageHeight)*1/6, null);
		    }		    
		    if (waterMarkPosition == 6) {
		    	graphics.drawImage(waterMark.getImage(), imageWidth-waterMarkWidth, (imageHeight-waterMarkHeight)/2, null);
		    }
		    if (waterMarkPosition == 9) {
		    	graphics.drawImage(waterMark.getImage(), imageWidth-waterMarkWidth, (imageHeight)*5/6, null);
		    }
		    graphics.dispose();
	    }
	    /*
	    File newFile = new File("C:\\Users\\rezca\\Desktop\\img\\cloudwatermark.jpg");
	    try {
	          ImageIO.write(bufferedImage, "jpg", newFile);
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	    */
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
		    ImageIO.write(bufferedImage, "jpg", baos );
		    baos.flush();
		    baos.close();
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	    
	    byte[] imageInByte = baos.toByteArray();
	    return imageInByte;
	}
	
	/**
	 * 
	 * @param colorStr e.g. "#FFFFFF"
	 * @return 
	 */
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
}

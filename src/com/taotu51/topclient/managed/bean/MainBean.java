package com.taotu51.topclient.managed.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.UploadedFile;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.ArticleUserSubscribe;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemImgDeleteRequest;
import com.taobao.api.request.ItemImgUploadRequest;
import com.taobao.api.request.ItemPropimgDeleteRequest;
import com.taobao.api.request.ItemPropimgUploadRequest;
import com.taobao.api.request.ItemQuantityUpdateRequest;
import com.taobao.api.request.ItemSkuAddRequest;
import com.taobao.api.request.ItemUpdateRequest;
import com.taobao.api.request.ShopRemainshowcaseGetRequest;
import com.taobao.api.request.SkusQuantityUpdateRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemImgDeleteResponse;
import com.taobao.api.response.ItemImgUploadResponse;
import com.taobao.api.response.ItemPropimgDeleteResponse;
import com.taobao.api.response.ItemPropimgUploadResponse;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.taobao.api.response.ItemSkuAddResponse;
import com.taobao.api.response.ItemUpdateResponse;
import com.taobao.api.response.ShopRemainshowcaseGetResponse;
import com.taobao.api.response.SkusQuantityUpdateResponse;
import com.taobao.api.response.VasSubscribeGetResponse;
import com.taotu51.topclient.miscellaneous.DataSource;
import com.taotu51.topclient.miscellaneous.ForSaleItemsData;
import com.taotu51.topclient.miscellaneous.ImageResizer;
import com.taotu51.topclient.miscellaneous.ImageWaterMark;
import com.taotu51.topclient.miscellaneous.ImgsToBeWaterMarked;
import com.taotu51.topclient.miscellaneous.RetrieveDataFromTaobao;
import com.taotu51.topclient.miscellaneous.RetrieveForSaleItems;
import com.taotu51.topclient.miscellaneous.RetrieveItem;
import com.taotu51.topclient.miscellaneous.RetrieveItemProps;
import com.taotu51.topclient.miscellaneous.RetrieveSellerInfo;
import com.taotu51.topclient.miscellaneous.SellerCatsTreeNode;
import com.taotu51.topclient.miscellaneous.RetrieveItem.ItemData;
import com.taotu51.topclient.miscellaneous.RetrieveSellerInfo.SellerInfo;
import com.taotu51.topclient.miscellaneous.SellerCatsDropDownMenu.SellerCatsMenuData;
import com.taotu51.topclient.model.CachedItems;
import com.taotu51.topclient.model.CachedItemsData;
import com.taotu51.topclient.model.UserInfo;
import com.taotu51.topclient.service.CachedItemsDataService;
import com.taotu51.topclient.service.CachedItemsService;
import com.taotu51.topclient.service.UserInfoService;



@ManagedBean(name="mainBean")
@ViewScoped
public class MainBean implements Serializable {
	
	private static final long serialVersionUID = 7911739142723603954L;
	@ManagedProperty(value = "#{userData}")
	private DataSource dataSource;
	
	@ManagedProperty(value = "#{sessionManagementBean}")
	private SessionManagementBean sessionManagementBean;

	@ManagedProperty(value = "#{editImgBean}")
	private EditImgBean editImgBean;

	@ManagedProperty(value = "#{recycledImgBean}")
	private RecycledImgBean recycledImgBean;
	
	@ManagedProperty(value = "#{previewWaterMarkBean}")
	private PreviewWaterMarkBean previewWaterMarkBean;
	
	@ManagedProperty(value = "#{userInfoService}")
	private UserInfoService userInfoService;

	@ManagedProperty(value = "#{cachedItemsService}")
	private CachedItemsService cachedItemsService;

	private Set<String> cachedItemsCidSet;
	@ManagedProperty(value = "#{cachedItemsDataService}")
	private CachedItemsDataService cachedItemsDataService;

	
	private TreeNode treeNode; //use tree structure to show seller cats 
	private SellerCatsMenuData sellerCatsMenuData;//use menu to show seller cats
	private final String PARENT_CID = "0";
	
	private String cid;
	private long numiid;
	private ForSaleItemsData forSaleItemsData;
	RetrieveItem.ItemData itemData;
	
	private long quantity = 1;
	private long defaultQuantityChange = 1;
	private String price;
	private long num;//item num
	private String title;//item title
	
	private String skuChange;//sku number change, plus or minus
	private String skuProperties;
	
	private String detail_url;
	
	private String imageFileName;
	private byte[] imageFileContent;
	
	private boolean showAjaxIndicator = false;

	private String pidVid1;//correspond to color of cloth
	private String pidVid2;//correspond to size of cloth
	private String pidVid3;
	private String pidVid1Alias;
	private String pidVid2Alias;
	private String pidVid3Alias;
	
	private String uploadedPropImgUrl;//url of the uploaded property image
	private String propImgUrl;//url of the property image in taobao
	
	private String newItemImgUrl;//url of the uploaded item image
	private String [] updateImg = new String[3];//use this instead, include url,success or failure,etc of the uploaded item image

	private Map<String,String> availablePidVid2ForEachPidVid1 = new HashMap<String,String>();//new sizes which can be added
	Map<String,ItemData> numiidToItemData = new HashMap<String,ItemData>();
	private SellerInfo sellerInfo;
	
	private String currentItemImgId;
	private String currentItemSmallImgId;
	private String currentItemImgPosition = "0";
	
	private String isRecommended;
	private String approveStatus;
	
	private int imgIndex = 0;
	private String skuBatchUpdateStr;
	private List<String> leafCidList;//user cats leaf cid
	
	private RetrieveForSaleItems retrieveForSaleItems;
	private String[] progressData = new String[10];
	private String waterMark;
	private String waterMarkType;//pic or text
	
	private UserInfo userInfo;
	
	private String selectedWaterMarkImg;//do not save this one to database because 'commandlink' submit will overwrite it to empty value
	private String selectedWaterMarkImgToBeSaved;//save it to database
	private String toBeChangedWaterMarkImg;//onmouseover event will pass the image id to this variable
	private boolean picWaterMark;
	private boolean textWaterMark;	
	private String sessionKey;//to be passed from callback parameter
	String createdFileName;//uploaded image file name	
	//private String imgToBeWaterMarkedtmp = "mainPic";//选择打水印的图片（主图，附图，主图和附图，属性图，全部）
	//private String imgToBeWaterMarked = "mainPic";//选择打水印的图片（主图，附图，主图和附图，属性图，全部）	
	private String imgToBeWaterMarkedtmp = "mainAndAdditionalPic";//update：主图 = 主图 + 附图，所有的5张小图都当作主图
	private String imgToBeWaterMarked = "mainAndAdditionalPic";//
	private String [] waterMarkedImgs;//update poll when adding watermark to images
	private String waterMarkCompleted;	
	private File catalinaBase;	
	private String whatIsEdited;//当前正在编辑的图片：mainPic, additionalPic or propPic
	private int waterMarkFontSize = 30;
	private int waterMarkPosition = 9;	
	private String updateItemResult = "failure";//or success
	private String userNick;
	private String field;//title, price, stock, image, etc.
	
	public MainBean() {//constructor

	}

	public void retrieveForSaleItems() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
		msg.setSummary("success");
		
		progressData = new String[10];
		imgIndex = 0;
		String leafnodecid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("leafnodecid");
		//<a4j:param name="param1" value="#{node.cid}" assignTo="#{mainBean.cid}" />  the assignTo executes after action has been called, so use the above line instead
		//System.out.println("leafnodecid-----:"+leafnodecid);
		setCid(leafnodecid);		
		retrieveForSaleItems = new RetrieveForSaleItems();
		ForSaleItemsData forSaleItemsData;
		List<CachedItems> cachedItemsList = cachedItemsService.get(leafnodecid, userNick);
		if ((cachedItemsList != null) && (cachedItemsList.size() > 0)) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(cachedItemsList.get(0).getItemsData());
				ObjectInputStream ois = new ObjectInputStream(bais);
				forSaleItemsData = (ForSaleItemsData) ois.readObject();
			    setForSaleItemsData(forSaleItemsData);
			    ois.close();
			    bais.close();			    
			    List<String> numiids = forSaleItemsData.getNumiids();
				Map<String,ItemData> numiidToItemDatatmp = new HashMap<String,ItemData>();
			    for (int i = 0;i < numiids.size();i++) {
			    	List<CachedItemsData> itemDataList = cachedItemsDataService.get(numiids.get(i),userNick);
			    	if (itemDataList.size() > 0) {
						ByteArrayInputStream bais2 = new ByteArrayInputStream(itemDataList.get(0).getItemsDetailData());
						ObjectInputStream ois2 = new ObjectInputStream(bais2);
						ItemData itemDatatmp = (ItemData) ois2.readObject();
						numiidToItemDatatmp.put(numiids.get(i), itemDatatmp);
					    ois2.close();
					    bais2.close();
			    	}
			    	else {
			    		//System.out.println(numiids.get(i)+"no data--------------");
			    	}
			    }
			    setNumiidToItemData(numiidToItemDatatmp);			    
		    } catch (Exception e) {
				e.printStackTrace();
			}			
		}
		else {
	        RequestContext reqContext = RequestContext.getCurrentInstance();  //get your hands on request context
	        setShowAjaxIndicator(true);
	        reqContext.update("ajax_indicator_form:ajaximg");
			forSaleItemsData = retrieveForSaleItems.retrieveForSaleItems(dataSource,cid,progressData,msg);				
			if (forSaleItemsData == null) {
			    msg.setSeverity(FacesMessage.SEVERITY_INFO);
			    msg.setSummary("此目录中没有商品!");
			    FacesContext.getCurrentInstance().addMessage(null, msg);
		        RequestContext.getCurrentInstance().update("showMesage:messages");
				return;
			}
			if (!msg.getSummary().equals("success")) {
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			setForSaleItemsData(forSaleItemsData);
			setNumiidToItemData(forSaleItemsData.getNumiidToItemData());
			progressData = null;
	        setShowAjaxIndicator(false);
			try {
				//write into cachedItems table
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(forSaleItemsData);
				CachedItems cachedItems = new CachedItems();
				cachedItems.setLeafNodeCid(cid);
				cachedItems.setUserNick(userNick);
				cachedItems.setItemsData(baos.toByteArray());
				cachedItemsService.add(cachedItems);
				oos.close();
				baos.close();
				
				//write into cachedItemsData table
				Iterator<String> numiids = numiidToItemData.keySet().iterator();
				while (numiids.hasNext()) {
					String numiid = numiids.next().toString();
					ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
					ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
					oos2.writeObject(numiidToItemData.get(numiid));
					CachedItemsData cachedItemsData = new CachedItemsData();
					cachedItemsData.setNumiid(numiid);
					cachedItemsData.setUserNick(userNick);
					cachedItemsData.setItemsDetailData(baos2.toByteArray());
					cachedItemsDataService.add(cachedItemsData);
					oos2.close();
					baos2.close();				
				}

		    } catch (Exception e) {
				e.printStackTrace();
			}
		}
		progressData = null;
		RequestContext.getCurrentInstance().update("showMesage:messages");
		
	}

	public void retrieveForSaleItemsUpdate() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
		msg.setSummary("success");

		/*
		progressData = new String[10];
		imgIndex = 0;
		String leafnodecid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("leafnodecid");
		//<a4j:param name="param1" value="#{node.cid}" assignTo="#{mainBean.cid}" />  the assignTo executes after action has been called, so use the above line instead
		System.out.println("leafnodecid-----:"+leafnodecid);
		setCid(leafnodecid);		
		retrieveForSaleItems = new RetrieveForSaleItems();
		ForSaleItemsData forSaleItemsData;
		*/
		progressData = new String[10];
		imgIndex = 0;
		String leafnodecid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("leafnodecid");
		//System.out.println("leafnodecid-----:"+leafnodecid);
		setCid(leafnodecid);		
		retrieveForSaleItems = new RetrieveForSaleItems();
		ForSaleItemsData forSaleItemsData;

        RequestContext reqContext = RequestContext.getCurrentInstance();  //get your hands on request context
        setShowAjaxIndicator(true);
        reqContext.update("ajax_indicator_form:ajaximg");

		//System.out.println("retrieveForSaleItemsUpdate called begin");
		forSaleItemsData = retrieveForSaleItems.retrieveForSaleItems(dataSource,cid,progressData,msg);			
		if (forSaleItemsData == null) {
		    msg.setSeverity(FacesMessage.SEVERITY_INFO);
		    msg.setSummary("此目录中没有商品!");			
			FacesContext.getCurrentInstance().addMessage(null, msg);
	        RequestContext.getCurrentInstance().update("showMesage:messages");
			return;
		}
		if (!msg.getSummary().equals("success")) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}		
		setForSaleItemsData(forSaleItemsData);
		setNumiidToItemData(forSaleItemsData.getNumiidToItemData());
        setShowAjaxIndicator(false);
        
		//delete cached data first
		List<CachedItems> cachedItemsList = cachedItemsService.get(leafnodecid, userNick);
		if ((cachedItemsList != null) && (cachedItemsList.size() > 0)) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(cachedItemsList.get(0).getItemsData());
				ObjectInputStream ois = new ObjectInputStream(bais);
				ForSaleItemsData forSaleItemsDataDelete = (ForSaleItemsData) ois.readObject();				    
			    List<String> numiids = forSaleItemsDataDelete.getNumiids();
			    for (int i = 0;i < numiids.size();i++) {
					cachedItemsDataService.delete(numiids.get(i),userNick);
			    }
		    } catch (Exception e) {
				e.printStackTrace();
			}			
		}	
		
		try {
			//////////////////////////////////////////////
			//write into cachedItems table
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(forSaleItemsData);
			CachedItems cachedItems = new CachedItems();
			cachedItems.setLeafNodeCid(cid);
			cachedItems.setUserNick(userNick);
			cachedItems.setItemsData(baos.toByteArray());
			cachedItemsService.add(cachedItems);
			oos.close();
			baos.close();
			
			//write into cachedItemsData table
			Iterator<String> numiids = numiidToItemData.keySet().iterator();
			while (numiids.hasNext()) {
				String numiid = numiids.next().toString();
				ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
				ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
				oos2.writeObject(numiidToItemData.get(numiid));
				CachedItemsData cachedItemsData = new CachedItemsData();
				cachedItemsData.setNumiid(numiid);
				cachedItemsData.setUserNick(userNick);
				cachedItemsData.setItemsDetailData(baos2.toByteArray());
				cachedItemsDataService.add(cachedItemsData);
				oos2.close();
				baos2.close();				
			}

	    } catch (Exception e) {
			e.printStackTrace();
		}
		progressData = null;
		RequestContext.getCurrentInstance().update("showMesage:messages");
	}
	
	/*
	public void retrieveItem(long numiid) {
		System.out.println("retrieveItem called---------------numiid="+numiid);
		setNumiid(numiid);
		RetrieveItem retrieveItem = new RetrieveItem();
		RetrieveItem.ItemData itemData = retrieveItem.retrieveItem(dataSource,numiid,progressData);
		setItemData(itemData);
	}
	*/
	
	public FacesMessage updateItem(String numiid, String field) {
			updateItemResult = "failure";
			//System.out.println("updateItem called----------"+numiid+"---"+field);		
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemUpdateRequest  req=new ItemUpdateRequest();
			req.setNumIid(Long.parseLong(numiid));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
			itemData = numiidToItemData.get(String.valueOf(numiid));
			if(field.equals("price")) {
				req.setPrice(price);
				msg.setSummary("成功修改价格！");
			}
			if(field.equals("num")) {
				req.setNum(num);
				msg.setSummary("成功修改库存！");
			}	
			if(field.equals("title")) {
				req.setTitle(title);
				msg.setSummary("成功修改标题！");
			}			
			if(field.equals("image")) {
				//System.out.println("image----------");
				FileItem fileItem = new FileItem(imageFileName,imageFileContent);
				req.setImage(fileItem);
				msg.setSummary("成功替换商品主图！");
			
				try {
					//get the original image and save it before updating
					TaobaoClient clientSave=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
					ItemGetRequest reqSave=new ItemGetRequest();
					//req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
					reqSave.setFields("title,item_img");
					reqSave.setNumIid(Long.parseLong(numiid));
					ItemGetResponse responseSave = clientSave.execute(reqSave, dataSource.getSessionKey());
					String jsonTxtSave = responseSave.getBody();
					JSONObject jsonSave = (JSONObject) JSONSerializer.toJSON(jsonTxtSave);
					JSONObject tmpobjSave = (JSONObject)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img").get(0);
					String imgUrlSave = tmpobjSave.getString("url");
					String [] urlParts = imgUrlSave.split("\\.");
					byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
				    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
				    //System.out.println(catalinaBase.toString()+"===============================");
				    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
				    if (!dir.exists()) {
				    	dir.mkdirs();
				    }
				    FileOutputStream fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
					fos.write(imgSave);
					fos.close();
					//////////////
				}
				catch(Exception e) {
					//if not successfully save the img, do not continue 
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary("保存主图失败,请重试!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return msg;
				}
			}
			try {
				ItemUpdateResponse response = client.execute(req, dataSource.getSessionKey());
				System.out.println(response.getBody());
				int count = 1;
				while ((response.getBody() == null)||(response.getBody().contains("error"))) {
					response = client.execute(req, dataSource.getSessionKey());
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);				
				}
				if (response.getBody() == null) {
					updateImg[2] = "nochange";
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary("连接淘宝错误，请重试！");
					if(!field.equals("image")) {
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
				}
				else if ((response.getBody().contains("error")) || (response.getBody().contains("ERROR"))) {
					updateImg[2] = "nochange";
		        	JSONObject json = (JSONObject) JSONSerializer.toJSON(response.getBody());
		        	if (response.getBody().contains("sub_msg")) {
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
		        	}
		        	else if (response.getBody().contains("msg")) {
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						msg.setSummary(json.getJSONObject("error_response").getString("msg"));
		        	}
		        	else {
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						msg.setSummary("修改失败，请重试！");
		        	}
					if(!field.equals("image")) {
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
				}
				else {
					updateItemResult = "success";
					//whether success or failure, need to update updateItemResult
					//so put the following line at the end of this method
					//RequestContext.getCurrentInstance().update("updateItemForm:updateItemResult");
					if(field.equals("title")) {
						itemData.setTitle(this.title);
						serializeItemData(String.valueOf(numiid),itemData);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					else if(field.equals("price")) {
						itemData.setItemPrice(Double.valueOf(this.price));
						serializeItemData(String.valueOf(numiid),itemData);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					else if(field.equals("num")) {
						itemData.setItemNum((int)this.num);
						serializeItemData(String.valueOf(numiid),itemData);
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					//if update image, need to setNewItemImgUrl to show the new image
					else if(field.equals("image")) {
						TaobaoClient client2=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
						ItemGetRequest req2=new ItemGetRequest();
						req2.setFields("item_img");
						req2.setNumIid(Long.parseLong(numiid));
						
						try {
							String jsonTxt2 = "";
							ItemGetResponse response2 = client2.execute(req2, dataSource.getSessionKey());
							jsonTxt2 = response2.getBody();
							int count2 = 1;
							while ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
								response2 = client2.execute(req2, dataSource.getSessionKey());
								if (count2 <= 0) {
									break;
								}
								count2--;
								Thread.sleep(2000);				
							}

							if ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
								//updateImg[0] = null;							
								updateImg[2] = "nodisplay"; //image updated but retrieve updated image url error,should let users know this, such as show a special image for this information
							}
							else {
								JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt2); 
								JSONObject itemoj= json2.getJSONObject("item_get_response").getJSONObject("item");
								JSONArray itemImgArray = itemoj.getJSONObject("item_imgs").getJSONArray("item_img");
								for (int i=0;i<itemImgArray.size();i++) {
									JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
									if (tmpobj.getInt("id") == 0) {
										setNewItemImgUrl(tmpobj.getString("url"));
										updateImg[0] = tmpobj.getString("url");
										updateImg[2] = "display";
										itemData.getItemImgs()[0] = tmpobj.getString("url");
									}
									else {
										//////////////after update mainPic, all the other 4 additional image ids will be changed, so need to update them in local database
										//////////////////////////very important. tested in sandbox, but not in formal enviroment 
										//////////////////////////it is a good practice to update as much information as possible in itemData
										//////////////////////////
										itemData.getItemImgs()[i] = tmpobj.getString("url");
										itemData.getItemImgIDs()[i] = tmpobj.getString("id");
									}
								}
								//update local database
								//forSaleItemsData.getNumiidToItemPrice().put(String.valueOf(numiid), Double.valueOf(price));
								itemData.setItemPicUrl(updateImg[0]);
								serializeItemData(String.valueOf(numiid),itemData);
								///
							}
							RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
						}
						catch(ApiException e2) {
							updateImg[2] = "nodisplay";
						}
					}
				}
			} catch (Exception e) {
				updateImg[2] = "nochange";
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				msg.setSummary("连接淘宝超时，请重试！");
				if(!field.equals("image")) {
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				e.printStackTrace();
			}
			RequestContext.getCurrentInstance().update("updateItemForm:updateItemResult");
			return msg;
		}


	
	private void serializeForSaleItemsData(String leafCid, String userNick, ForSaleItemsData forSaleItemsData) {
		CachedItems cachedItemstmp = new CachedItems();					
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(forSaleItemsData);
			cachedItemstmp.setLeafNodeCid(cid);
			cachedItemstmp.setUserNick(userNick);
			cachedItemstmp.setItemsData(baos.toByteArray());
			cachedItemsService.update(cachedItemstmp);
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void serializeItemData(String numiid, ItemData itemData) {
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		ObjectOutputStream oos2;
		try {
			oos2 = new ObjectOutputStream(baos2);
			oos2.writeObject(itemData);
			CachedItemsData cachedItemsData = new CachedItemsData();
			cachedItemsData.setNumiid(numiid);
			cachedItemsData.setUserNick(userNick);
			cachedItemsData.setItemsDetailData(baos2.toByteArray());
			cachedItemsDataService.update(cachedItemsData);
			oos2.close();
			baos2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
    public void replaceItemImgFileUploadlistener(FileUploadEvent event) throws Exception {
    	//System.out.println("replaceItemImgFileUploadlistener called---------------");    	
        UploadedFile file = event.getFile();
        setImageFileName(file.getFileName());
        //setImageFileContent(file.getContents());
        //setImageFileContent(ImageResizer.resize(file.getInputstream()));//reduce the size of image
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(ImageResizer.resize(file.getInputstream()), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),userNick,waterMarkFontSize,waterMarkPosition);
        setImageFileContent(waterMarkedImgData);//reduce the size of image and add watermark
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
		if (currentItemImgId.equals("0")) {
			msg = updateItem(String.valueOf(numiid), "image");//item main img can only be replaced using taobao.item.update API
		}
		else {
			if(replaceItemImg(msg)) {
				setNewItemImgUrl(file.getFileName());
				msg.setSummary("成功替换商品图片！");
			}
			else {
				//error message set in method replaceItemImg(msg)
				//msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "替换商品图片失败，请重试！");
				//FacesContext.getCurrentInstance().addMessage(null, msg);				
			}
		}
        RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void replaceItemImgSetParams() {
    	//System.out.println("replaceItemImgSetParams called----------------");
		String numiid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		this.numiid = Long.parseLong(numiid_passed);    	
		String imgid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("current_itemimg_id");
		String imgIndex_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("img_index");
		String currentItemSmallImgId_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("globalsmallitemimg_id");
		
		this.imgIndex = Integer.parseInt(imgIndex_passed);
		this.currentItemImgId = imgid_passed;
		this.currentItemSmallImgId = currentItemSmallImgId_passed;
		updateImg[1] = currentItemSmallImgId;
    }
	private boolean replaceItemImg(FacesMessage msg) {
		//System.out.println("before replacingitemimg");
		boolean booleanResult = true;
		//delete the img first
		//before deletion,should make sure the image exists in taobao. 
		//if the data in my site is not consistent with taobao, deletion
		//will execute normally, but actually the image does not exist in taobao
		//need to take this into account. more to be done!!!!!!!!!!!
		//System.out.println("replaceItemImg called----------------------------");
		String [] item_image_ids = itemData.getItemImgIDs();
		//System.out.println(imgIndex+"+++++++++++++++++++++");
		//System.out.println(item_image_ids[imgIndex]+"-------------");
		//System.out.println(item_image_ids[imgIndex]+"image id passed to saveImg(), idex is");
		boolean imgExist = saveImg(item_image_ids[imgIndex]);//saveImg return boolean (the image exists or not)
		
		//////////////////////////////////////////
		if (imgExist) {
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemImgDeleteRequest req=new ItemImgDeleteRequest();
			//req.setId(Long.parseLong(currentItemImgId));
	
			req.setId(Long.parseLong(item_image_ids[imgIndex]));//get img id by index instead
			req.setNumIid(numiid);
			String jsonTxt = null;
			try {
				ItemImgDeleteResponse response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				//System.out.println("delete response========="+jsonTxt);
				int count = 1;
				while ((response == null)||(jsonTxt.contains("error"))||(jsonTxt.contains("ERROR"))) {
					response = client.execute(req , dataSource.getSessionKey());
					jsonTxt = response.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				/*
				updateImg[2] = "nochange";
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				msg.setSummary("删除图片时连接淘宝错误，请重试！");
				e.printStackTrace();
				booleanResult = false;
				return booleanResult;//if delete error, you cannot add, so skip the add by returnning false
				*/
			}				
				if (jsonTxt == null) {
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary("删除图片时连接淘宝错误，请重试！");
			        booleanResult = false;
			        updateImg[2] = "nochange";
			        return booleanResult;
				}
				else if (jsonTxt.contains("error")) {
					JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
					booleanResult = false;
					updateImg[2] = "nochange";
					return booleanResult;
				}
				else if (jsonTxt.contains("ERROR")) {
					msg.setSummary("连接淘宝错误，请重试！");
					updateImg[2] = "nochange";
					return booleanResult;
				}
				//remove image id from the List itemData.getItemImgIDs()
				else {
					item_image_ids[imgIndex] = null;
					itemData.getItemImgs()[imgIndex] = null;
					itemData.getItemImgIDs()[imgIndex] = null;
					updateImg[0] = null;
					updateImg[2] = "nodisplay";
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
				}

			
			//add new img
			ItemImgUploadRequest  reqUpload = new ItemImgUploadRequest();
			reqUpload.setNumIid(numiid);
			//req2.setPosition(0L);
		
			FileItem fileItem = new FileItem(imageFileName,imageFileContent);
			reqUpload.setImage(fileItem);
			String jsonTxtUpload = null;
			JSONObject jsonUpload = null;
			try {
				ItemImgUploadResponse response = client.execute(reqUpload, dataSource.getSessionKey());
				jsonTxtUpload = response.getBody();
				//System.out.println("response body from addItemImg(String current_itemimg_pos) **************************************"+jsonTxt);
				jsonUpload = (JSONObject) JSONSerializer.toJSON( jsonTxtUpload );
				
				int count = 1;
				while ((response == null)||(jsonTxtUpload.contains("error"))) {
					response = client.execute(reqUpload, dataSource.getSessionKey());
					jsonTxtUpload = response.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				/*
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				msg.setSummary("添加图片时连接淘宝错误，请重试！");
				e.printStackTrace();
				booleanResult = false;
				*/
			}				
				if (jsonTxtUpload == null) {
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary("成功删除原图,但添加新图片时连接淘宝错误，请重试！");
			        booleanResult = false;
				}
				else if (jsonTxtUpload.contains("error")) {
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary(jsonUpload.getJSONObject("error_response").getString("sub_msg"));
					booleanResult = false;
				}
				else if (jsonTxtUpload.contains("item_img_upload_response")) {
					JSONObject jsonObj = jsonUpload.getJSONObject("item_img_upload_response").getJSONObject("item_img");				
					//add a new image id to itemData.getItemImgIDs()
					String added_image_id = jsonObj.getString("id");
	
					item_image_ids[imgIndex] = added_image_id;
					itemData.getItemImgIDs()[imgIndex] = added_image_id;
					String url = (String)jsonUpload.getJSONObject("item_img_upload_response").getJSONObject("item_img").getString("url");
					setNewItemImgUrl(url);
					updateImg[0] = url;
					updateImg[2] = "display";
					itemData.getItemImgs()[imgIndex] = url;
					
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
				}
				else {
					//未知错误
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					msg.setSummary("连接淘宝错误，请重试！");
			        booleanResult = false;
				}

		}
		else {
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("此商品图片不存在,请点击目录更新！");			
	        RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
	        updateImg[2] = "nochange";
	        booleanResult = false;
		}
		return booleanResult;
	}

	
	public void deleteItemImg() {
		//System.out.println("deleteItemImg called----------------------------");
		String numiid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		String imgid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("current_itemimg_id");
		String imgIndex_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("img_index");
		this.imgIndex = Integer.parseInt(imgIndex_passed);
		setNumiid(Long.parseLong(numiid_passed));
		String currentItemSmallImgId_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("globalsmallitemimg_id");
		this.currentItemSmallImgId = currentItemSmallImgId_passed;
		//System.out.println(currentItemSmallImgId+"************");
		updateImg[1] = currentItemSmallImgId;
		

		//System.out.println("numiid is--"+numiid_passed+"---------image id is--"+imgid_passed);
		String [] item_image_ids = itemData.getItemImgIDs();
		//req.setId(Long.parseLong(imgid_passed));
		//System.out.println("=====imgIndex_passed===="+imgIndex_passed);
		//if cached data not consistent with taobao, delete will execute normally, but it does not actually delete image from taobao. need to take this into account. more work to be done!!!!!!!!!!
		boolean imgExist = saveImg(item_image_ids[imgIndex]);//saveImg return boolean (the image exists or not)

		//////////////////////////////////////////
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "");
		if (imgExist) {
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemImgDeleteRequest req=new ItemImgDeleteRequest();
			req.setId(Long.parseLong(item_image_ids[imgIndex]));
			req.setNumIid(Long.parseLong(numiid_passed));
			String jsonTxt = "";
			
			try {
				ItemImgDeleteResponse response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				System.out.println(jsonTxt);
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
				int count = 1;
				while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
					response = client.execute(req , dataSource.getSessionKey());
					jsonTxt = response.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
				if (jsonTxt == null) {
					msg.setSummary("删除图片出错，请重试！");
					updateImg[2] = "nochange";
				}
				else if (jsonTxt.contains("error")) {
					msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
					updateImg[2] = "nochange";
				}
				else {
					msg.setSeverity(FacesMessage.SEVERITY_INFO);
					msg.setSummary("成功删除商品图片！");
					//msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功删除商品图片！");
					//remove image id from the List itemData.getItemImgIDs()
					String deleted_image_id = json.getJSONObject("item_img_delete_response").getJSONObject("item_img").getString("id");
					item_image_ids[Integer.parseInt(imgIndex_passed)] = null;
					itemData.getItemImgs()[imgIndex] = null;
					itemData.getItemImgIDs()[imgIndex] = null;
					//System.out.println("deleted index-------"+Integer.parseInt(imgIndex_passed));
					updateImg[0] = "nodisplay";
					updateImg[2] = "nodisplay";
			        RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
				}
	
			} catch (Exception e) {
				updateImg[2] = "nochange";
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				msg.setSummary("连接淘宝超时，请重试！");
				e.printStackTrace();
			}
		}
		else {//the image to be deleted not exist
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("此商品图片不存在,请点击目录更新！");
			//msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "此商品图片不存在,请点击目录更新！");
	        RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
	        updateImg[2] = "nochange";
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	
	
    public void addItemImgSetParams() {
    	//System.out.println("addItemImgSetParams called----------------");
		String numiid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		String imgIndex_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("img_index");
		String currentItemSmallImgId_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("globalsmallitemimg_id");
		this.imgIndex = Integer.parseInt(imgIndex_passed);
		this.numiid = Long.parseLong(numiid_passed);  
		String imgid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("current_itemimg_id");
		this.currentItemImgId = imgid_passed;
		this.currentItemSmallImgId = currentItemSmallImgId_passed;
		//System.out.println("numiid is: "+numiid+"---------------");
    }
    public void addItemImgFileUploadlistener(FileUploadEvent event) throws Exception {
    	//System.out.println("addItemImgFileUploadlistener called---------------");    	
    	updateImg[1] = currentItemSmallImgId;
    	UploadedFile file = event.getFile();
        setImageFileName(file.getFileName());
        //setImageFileContent(file.getContents());
        //setImageFileContent(ImageResizer.resize(file.getInputstream()));//reduce the size of image
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(ImageResizer.resize(file.getInputstream()), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),userNick,waterMarkFontSize,waterMarkPosition);
        setImageFileContent(waterMarkedImgData);//reduce the size of image and add watermark

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
		if (currentItemImgId.equals("0")) {
			updateItem(String.valueOf(numiid), "image");//item main img can only be replaced using taobao.item.update API
		}
		else {
			if (addItemImg(msg)) {
				msg.setSummary("成功添加商品图片！");			
			}
			else {
				//FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "添加商品图片失败，请重试！");
				//FacesContext.getCurrentInstance().addMessage(null, msg);				
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);				
        RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
    }
    
	private boolean addItemImg(FacesMessage msg) {
		boolean booleanResult = true;
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemImgUploadRequest  req=new ItemImgUploadRequest ();
		req.setNumIid(numiid);
		//req.setPosition(0L);
		for(long i=0;i<sellerInfo.getItemImgNum();i++) {
			if (!itemData.getUsedItemImgPositions().contains(String.valueOf(i))) {
				req.setPosition(i);
				break;
			}
		}
		
		FileItem fileItem = new FileItem(imageFileName,imageFileContent);
		req.setImage(fileItem);
		String jsonTxt = "";
		try {
			ItemImgUploadResponse response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			int count = 1;
			while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
				response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				if (count <= 0) {
					break;
				}
				count--;
				Thread.sleep(2000);
			}
			System.out.println("response body from addItemImg **************************************"+jsonTxt);
			JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
			if (jsonTxt == null) {
				msg.setSummary("连接淘宝错误，请重试！");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				booleanResult = false;
				updateImg[2] = "nochange";
			}
			else if (jsonTxt.contains("error")) {
				msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
	        	booleanResult = false;
	        	updateImg[2] = "nochange";
			}
			else{
				//add a new image id to itemData.getItemImgIDs()
				JSONObject jsonObj = json.getJSONObject("item_img_upload_response").getJSONObject("item_img");
				String added_image_id = jsonObj.getString("id");
				String [] item_image_ids = itemData.getItemImgIDs();
				item_image_ids[imgIndex] = added_image_id;
				
				//set new image to be shown on the front end
				setNewItemImgUrl(jsonObj.getString("url"));
				updateImg[0] = jsonObj.getString("url");			
				updateImg[2] = "display";
				itemData.getItemImgs()[imgIndex] = jsonObj.getString("url");
				itemData.getItemImgIDs()[imgIndex] = added_image_id;
				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("连接淘宝超时，请重试！");
			booleanResult = false;
			updateImg[2] = "nochange";
		}
		return booleanResult;
	}
    public void updateItemPropImgFileUploadlistener(FileUploadEvent event) throws Exception {
    	//System.out.println("updateItemPropImgFileUploadlistener called---------------");    	
        UploadedFile file = event.getFile();
        setImageFileName(file.getFileName());
        //setImageFileContent(file.getContents());
        //setImageFileContent(ImageResizer.resize(file.getInputstream()));//reduce the size of image
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(ImageResizer.resize(file.getInputstream()), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),userNick,waterMarkFontSize,waterMarkPosition);
        setImageFileContent(waterMarkedImgData);//reduce the size of image and add watermark
        addOrUpdateItemPropImg();
        //retrieveItem();
    }
	private String addOrUpdateItemPropImg() {
		String updatedImgUrl = null;
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemPropimgUploadRequest req=new ItemPropimgUploadRequest();
		req.setNumIid(numiid);
		req.setProperties(pidVid1);
		//System.out.println("numiid------"+numiid+"pidVid1----------"+pidVid1);
		FileItem fileItem = new FileItem(imageFileName,imageFileContent);
		req.setImage(fileItem);
		String jsonTxt = "";
		FacesMessage msg = null;
		long prop_img_id = 0;
		try {
			//if it is add, do not need to save or delete
			//save a copy before uploading
			TaobaoClient clientSave=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemGetRequest reqSave=new ItemGetRequest();
			//req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
			reqSave.setFields("title,prop_img");
			reqSave.setNumIid(numiid);
			ItemGetResponse responseSave = clientSave.execute(reqSave, dataSource.getSessionKey());
			String jsonTxtSave = responseSave.getBody();
			System.out.println(jsonTxtSave);
			boolean imgExist = false;
			JSONObject jsonSave = (JSONObject) JSONSerializer.toJSON(jsonTxtSave);
			JSONArray propImgArray = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("prop_imgs").getJSONArray("prop_img");
			String properties = null;
			String url = null;
			for (int i=0;i<propImgArray.size();i++) {
				JSONObject tmpobj = (JSONObject)propImgArray.get(i);				
				url = tmpobj.getString("url");
				prop_img_id = tmpobj.getLong("id");
				properties = tmpobj.getString("properties");
				if (properties.equals(pidVid1)) {
					imgExist = true;
					break;
				}
			}
			if (imgExist) {	
				String [] urlParts = url.split("\\.");
				byte [] imgSave = editImgBean.remoteImgToByteArray(url);
			    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
			    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
			    if (!dir.exists()) {
			    	dir.mkdirs();
			    }
			    FileOutputStream fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
				fos.write(imgSave);
				fos.close();
			
				//////////////save done
				///////////delete original prop_img
				TaobaoClient clientDelete=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
				ItemPropimgDeleteRequest reqDelete=new ItemPropimgDeleteRequest();
				reqDelete.setId(prop_img_id);
				reqDelete.setNumIid(numiid);
				try {
					ItemPropimgDeleteResponse responseDelete = clientDelete.execute(reqDelete , dataSource.getSessionKey());
					itemData.getPropToImg().remove(pidVid1);
					System.out.println(responseDelete.getBody());
				} catch (ApiException e) {
					e.printStackTrace();
				}
			}
			/////upload new prop_img
			ItemPropimgUploadResponse response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println(jsonTxt);
			if (jsonTxt.contains("error")) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
			}
			else {
				//{"item_propimg_upload_response":{"prop_img":{"created":"2013-10-03 23:16:30","id":31010166085,"url":"http:\/\/img03.tbsandbox.com\/bao\/uploaded\/i3\/3600301501\/T2hkWyXghcXXXXXXXX_!!3600301501.jpg"}}}
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
				setUploadedPropImgUrl(json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url"));
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功更新商品属性图！");
				itemData.getPropToImg().put(pidVid1, json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url"));
				updatedImgUrl = json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url");
				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
			}
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//System.out.println("response body from addOrUpdateItemPropImg **************************************"+jsonTxt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedImgUrl;
	}
 
	public String updateItemQuantity(long numiid, String skuChange) { 
		//clearMessages();
		//System.out.println("updateItemQuantity-------------------is called");
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemQuantityUpdateRequest req=new ItemQuantityUpdateRequest();
		req.setNumIid(numiid);
		//req.setQuantity(quantity);
		req.setType(2L);//increment
		//System.out.println("numiid---:"+numiid);
		//System.out.println("quantity---:"+quantity);
		itemData = numiidToItemData.get(String.valueOf(numiid));
		int num =  itemData.getItemNum();
		//System.out.println("item number is---:"+num);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "更新库存失败，请重试!");  
		
		if (skuChange.equals("plus")){
			//req.setSkuidQuantities(skuPropertiesToSkuid.get(skuProperties).toString()+":1");
			//req.setQuantity(num+quantity);
			req.setQuantity(quantity);
			//System.out.println("plus");
	        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功增加库存!");  

		}
		else {//minus
			//req.setSkuidQuantities(skuPropertiesToSkuid.get(skuProperties).toString()+":-1");
			//req.setQuantity(num+quantity);
			req.setQuantity(-quantity);
			//System.out.println("minus");
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功减少库存!");
		}
		String jsonTxt = "";
		try {
			ItemQuantityUpdateResponse response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			//System.out.println(req);
			System.out.println(jsonTxt);
	        if (jsonTxt.contains("error")) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);

	        	msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	if (skuChange.equals("plus")){
	        		itemData.setItemNum(itemData.getItemNum()+(int)quantity);
	        	}
	        	else {
	        		itemData.setItemNum(itemData.getItemNum()-(int)quantity);
	        	}
	        	//update local database
				serializeItemData(String.valueOf(numiid),itemData);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
		} catch (ApiException e) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "更新库存失败，请重试!");
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
			e.printStackTrace();
		}		
		System.out.println(jsonTxt);
		//updateItemDescForSkuQuantityChange();
    	return null;
	}
	
	public String updateSkuQuantity() { 
		//clearMessages();
		//System.out.println("updateSkuQuantity-------------------is called");
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		SkusQuantityUpdateRequest req=new SkusQuantityUpdateRequest();
		req.setNumIid(numiid);
		req.setType(2L);//increment
		//System.out.println("skuProperties is "+ skuProperties);
		if (skuChange.equals("plus")){
			//req.setSkuidQuantities(skuPropertiesToSkuid.get(skuProperties).toString()+":1");
			req.setSkuidQuantities(itemData.getSkuPropertiesToSkuid().get(skuProperties).toString()+":"+quantity);
		}
		else {//minus
			//req.setSkuidQuantities(skuPropertiesToSkuid.get(skuProperties).toString()+":-1");
			req.setSkuidQuantities(itemData.getSkuPropertiesToSkuid().get(skuProperties).toString()+":-"+quantity);
		}
		String jsonTxt = "";
		try {
			SkusQuantityUpdateResponse response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println(jsonTxt);
	        if (jsonTxt.contains("error")) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
	        	FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	FacesMessage msg;
	        	if (skuChange.equals("plus")){
	        		//System.out.println("skuProperties is---"+skuProperties);
	        		//System.out.println(itemData.getPropertiesToQuantity().keySet());
	        		itemData.getPropertiesToQuantity().put(skuProperties, itemData.getPropertiesToQuantity().get(skuProperties)+(int)quantity);
	        		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功增加库存!");  
	        	}
	        	else {
	        		itemData.getPropertiesToQuantity().put(skuProperties, itemData.getPropertiesToQuantity().get(skuProperties)-(int)quantity);
	        		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功减少库存!");
	        	}
				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
	        	FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
		} catch (ApiException e) {
			// TODO Auto-generated catch block
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "修改库存失败，请重试!");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
			e.printStackTrace();
		}		
		System.out.println(jsonTxt);
		//updateItemDescForSkuQuantityChange();
    	return null;
	}
	
	public void setSkuNumBatchUpdateByAjax() {
		String skuBatchUpdateStr_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sku_batch_update_str");
		this.skuBatchUpdateStr = skuBatchUpdateStr_passed;
		//System.out.println("setSkuNumBatchUpdateByAjax called---"+skuBatchUpdateStr);
	}
	
	public String updateSkuQuantityInBatch() {
		//System.out.println("updateSkuQuantityInBatch-------------------is called");
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		SkusQuantityUpdateRequest req=new SkusQuantityUpdateRequest();
		req.setNumIid(numiid);
		req.setType(2L);//increment
		req.setSkuidQuantities(skuBatchUpdateStr);

		String jsonTxt = "";
		try {
			SkusQuantityUpdateResponse response = client.execute(req, dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println(jsonTxt);
			//{"skus_quantity_update_response":{"item":{"iid":"2000038123656","modified":"2013-10-03 03:16:56","num":95,"num_iid":2000038123656,"skus":{"sku":[{"modified":"2013-10-03 03:16:56","quantity":17,"sku_id":30007221065},{"modified":"2013-10-03 03:16:56","quantity":11,"sku_id":30007221068},{"modified":"2013-10-03 03:16:56","quantity":3,"sku_id":30007233337},{"modified":"2013-10-03 03:16:56","quantity":8,"sku_id":30007234741},{"modified":"2013-10-03 03:16:56","quantity":5,"sku_id":30007234744},{"modified":"2013-10-03 03:16:56","quantity":2,"sku_id":30007234747},{"modified":"2013-10-03 03:16:56","quantity":4,"sku_id":30007234750},{"modified":"2013-10-03 03:16:56","quantity":2,"sku_id":30007236613}]}}}}
	        if (jsonTxt.contains("error")) {
		        //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "更新库存失败，请重试!");  
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
	        	FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	try {
		        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
			    	JSONArray sku_array= json.getJSONObject("skus_quantity_update_response").getJSONObject("item").getJSONObject("skus").getJSONArray("sku");
			    	for (int i=0;i<sku_array.size();i++) {
			    		JSONObject jo = (JSONObject)sku_array.get(i);
			    		String skuPropertiesTmp = itemData.getSkuidToSkuProperties().get(jo.getLong("sku_id"));
			    		//System.out.println(skuPropertiesTmp+"--------------------"+jo.getInt("quantity"));
			    		itemData.getPropertiesToQuantity().put(skuPropertiesTmp, new Integer(jo.getInt("quantity")));
			    	}
	        	}//only one sku
	        	catch (Exception ex) {
		        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		        	JSONObject jo= json.getJSONObject("skus_quantity_update_response").getJSONObject("item").getJSONObject("skus").getJSONObject("sku");
			    	String skuPropertiesTmp = itemData.getSkuidToSkuProperties().get(jo.getLong("sku_id"));
			    	//System.out.println(skuPropertiesTmp+"--------------------"+jo.getInt("quantity"));
			    	itemData.getPropertiesToQuantity().put(skuPropertiesTmp, new Integer(jo.getInt("quantity")));
	        	}
		    	
	        	FacesMessage msg;
	        	if (skuChange.equals("plus")){
	        		//itemData.getPropertiesToQuantity().put(skuProperties, itemData.getPropertiesToQuantity().get(skuProperties)+(int)quantity);
	        		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功增加库存!");  
	        	}
	        	else {
	        		//itemData.getPropertiesToQuantity().put(skuProperties, itemData.getPropertiesToQuantity().get(skuProperties)-(int)quantity);
	        		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功减少库存!");
	        	}
	        	
				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
	        	FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
		} catch (ApiException e) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "修改库存失败，请重试!");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
			e.printStackTrace();
		}		
		System.out.println(jsonTxt);
		//updateItemDescForSkuQuantityChange();
    	return null;
	}
	
	public String doNothing() {
		System.out.println("doNothing called-----------");
		return null;
	}

	public String editImg() {
		//System.out.println("editImg() called------------");
		if(currentItemImgId.equals("0")) {
			setWhatIsEdited("mainPic");
		}
		else {
			setWhatIsEdited("additionalPic");
		}
		updateImg[1] = currentItemSmallImgId;
		editImgBean.setImgUrlStr(itemData.getItemImgs()[Integer.valueOf(currentItemImgPosition)]);
		//reset slider
		editImgBean.setValueAll(50);
		editImgBean.setValueR(50);
		editImgBean.setValueG(50);
		editImgBean.setValueB(50);
		return null;
	}

	public String editPropImg() {
		//System.out.println("editPropImg() called------------");
		String propImgUrl_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propImgUrl");
		String pidVid1_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid1");
		setWhatIsEdited("propPic");
		setPidVid1(pidVid1_passed);
		editImgBean.setImgUrlStr(propImgUrl_passed);
		//reset slider
		editImgBean.setValueAll(50);
		editImgBean.setValueR(50);
		editImgBean.setValueG(50);
		editImgBean.setValueB(50);
		return null;
	}
/*	
	public String showItemDetail(long numiid) {
		//reset to default image for publishing item image
		isImgUploaded = false;
		createdFileName = null;
		
		setNumiid(numiid);
		setItemData(numiidToItemData.get(String.valueOf(numiid)));
		String itemCidToProps = itemData.getItemCidToProps().get(new Long(itemData.getCid()));
		RetrieveItemProps.retrieveItemProps(dataSource, itemData);
		if ((itemCidToProps == null) || (itemCidToProps.equals(""))){
			serializeItemData(String.valueOf(numiid),itemData);
		}
		return null;
	}
*/
	public String showItemDetail() {
		//reset to default image for publishing item image
		isImgUploaded = false;
		createdFileName = null;
		setItemData(numiidToItemData.get(String.valueOf(numiid)));
		String itemCidToProps = itemData.getItemCidToProps().get(new Long(itemData.getCid()));
		RetrieveItemProps.retrieveItemProps(dataSource, itemData);
		if ((itemCidToProps == null) || (itemCidToProps.equals(""))){
			serializeItemData(String.valueOf(numiid),itemData);
		}
		return null;
	}	
	/*
	public String showItemDetail() {
		String numiid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		setNumiid(Long.valueOf(numiid_passed));
		setItemData(numiidToItemData.get(String.valueOf(numiid)));
		String itemCidToProps = itemData.getItemCidToProps().get(new Long(itemData.getCid()));
		RetrieveItemProps.retrieveItemProps(dataSource, itemData);
		if ((itemCidToProps == null) || (itemCidToProps.equals(""))){
			serializeItemData(String.valueOf(numiid),itemData);
		}
		return null;
	}
	*/
	
	public void getItemDetailUrl() {
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemGetRequest req=new ItemGetRequest();
		req.setFields("detail_url");
		req.setNumIid(numiid);
		String jsonTxt = "";
		try {
			ItemGetResponse response = client.execute(req, dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println("response body from retrieveItem ---------"+jsonTxt);
	        if (jsonTxt.contains("error")) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
		        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功获取商品信息!");  
		        FacesContext.getCurrentInstance().addMessage(null, msg);	        	
	        }
		} catch (ApiException e) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "获取商品信息失败，请重试!");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
			e.printStackTrace();
		}
		JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
		JSONObject itemoj= json.getJSONObject("item_get_response").getJSONObject("item");
		setDetail_url(itemoj.getString("detail_url"));

	    /*
	    try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(detail_url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

	}
	
	public String addNewPidVid2Sku() {//under the same color category, add new size
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemSkuAddRequest req=new ItemSkuAddRequest();
		req.setNumIid(numiid);
		//System.out.println(numiid+"-----"+pidVid1+"---"+pidVid2+"--"+quantity+"--"+price);
		boolean threesku = false;
		if ((pidVid3 == null) || (pidVid3.equals("")) || (pidVid3.equals("null"))){
			req.setProperties(pidVid1+";"+pidVid2);
		}
		else {
			req.setProperties(pidVid2+";"+pidVid1+";"+pidVid3);
			pidVid3 = null;
			threesku = true;
		}
		if (pidVid2Alias == null || pidVid2Alias.equals("")) {
		}
		else {
			//updateItemSizeAlias();
		}
		req.setQuantity(quantity);
		req.setPrice(price);
		String jsonTxt = "";
		try {
			ItemSkuAddResponse response = client.execute(req, dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println(jsonTxt);
			//{"item_sku_add_response":{"sku":{"created":"2013-10-16 22:14:34","iid":"2000038123656","num_iid":2000038123656,"sku_id":31050533928}}}
        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        if (jsonTxt.contains("error")) {
	        	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	//System.out.println(itemData.getPidVid2ToName().keySet());
	        	//System.out.println(itemData.getPidVid2ToName().keySet());
	        	//System.out.println("pidvid2 name="+itemData.getAvailablePidVid2ForPidVid1().get(pidVid1).get(pidVid2));
	        	if (pidVid2Alias == null && pidVid2Alias.equals("")) {
	        		itemData.getPropertyToAlias().put(pidVid2, pidVid2Alias);
	        	}
	        	
	        	String skuId = json.getJSONObject("item_sku_add_response").getJSONObject("sku").getString("sku_id");
				if (threesku) {
		        	itemData.getPidVid2ToName().put(pidVid2+";"+pidVid3,itemData.getAvailablePidVid3ForPidVid1().get(pidVid1).get(pidVid3));
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).remove("new");
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).add(pidVid2+";"+pidVid3);
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).add("new");
		        	itemData.getPropertiesToQuantity().put(pidVid1+";"+pidVid2+";"+pidVid3+";", new Integer((int)quantity));
		        	//itemData.getAvailablePidVid3ForPidVid1().get(pidVid1).remove(pidVid3);
		        	//System.out.println("***"+itemData.getAvailablePidVid2ForPidVid1().get(pidVid1).keySet());
		        	
					itemData.getSkuPropertiesToSkuid().put(pidVid1+";"+pidVid2+";"+pidVid3 + ";", Long.parseLong(skuId));
					itemData.getSkuidToSkuProperties().put(Long.parseLong(skuId), pidVid1+";"+pidVid2+";"+pidVid3 + ";");					
				}
				else {
		        	itemData.getPidVid2ToName().put(pidVid2,itemData.getAvailablePidVid2ForPidVid1().get(pidVid1).get(pidVid2));
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).remove("new");
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).add(pidVid2);
		        	itemData.getPidVid1ToPidVid2().get(pidVid1).add("new");
		        	itemData.getPropertiesToQuantity().put(pidVid1+";"+pidVid2+";", new Integer((int)quantity));
		        	itemData.getAvailablePidVid2ForPidVid1().get(pidVid1).remove(pidVid2);
		        	//System.out.println("***"+itemData.getAvailablePidVid2ForPidVid1().get(pidVid1).keySet());
		        	
					itemData.getSkuPropertiesToSkuid().put(pidVid1+";"+pidVid2 + ";", Long.parseLong(skuId));
					itemData.getSkuidToSkuProperties().put(Long.parseLong(skuId), pidVid1+";"+pidVid2 + ";");
				}

				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功添加商品!");  
	        	FacesContext.getCurrentInstance().addMessage(null, msg); 
	        }
		} catch (ApiException e) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "添加商品失败,请重试!");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
			e.printStackTrace();
		}
		System.out.println("response body from addNewSizeSku------------------"+jsonTxt);
		//updateItemDescForSkuChangeOfExistingItem();
		return null;
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
	
	private boolean isImgUploaded = false;
	private StreamedContent  uploadedImg;
    public void fileUploadlistener(FileUploadEvent event) throws Exception {
    	//System.out.println("fileUploadlistener called---------------");
        UploadedFile file = event.getFile();
        setImageFileName(file.getFileName());
        //setImageFileContent(file.getContents());
        //setImageFileContent(ImageResizer.resize(file.getInputstream()));//reduce the size of image
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(ImageResizer.resize(file.getInputstream()), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),userNick,waterMarkFontSize,waterMarkPosition);
        setImageFileContent(waterMarkedImgData);//reduce the size of image and add watermark
        
        saveFile(file, "tmp", "tmpimage");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功上传图片：" + file.getFileName() + ".");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        isImgUploaded = true;
    }
    
	public void setAddNewPidVid1SkuParams() {
		String price_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("price");
		setPrice(price_passed);
		String quantity_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("quantity");
		setQuantity(Integer.parseInt(quantity_passed));
		String pidVid1_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid1");
		setPidVid1(pidVid1_passed);
		String pidVid1Alias_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid1Alias");
		setPidVid1Alias(pidVid1Alias_passed);
		String pidVid2_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid2");
		setPidVid2(pidVid2_passed);
		String pidVid2Alias_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid2Alias");
		setPidVid2Alias(pidVid2Alias_passed);
	}
	
	public String addNewPidVid1Sku() {//add a new item with color, size and other properties
		//clearMessages();
		if (isImgUploaded == false) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "请选择宝贝图片!");  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 	
	        return null;
		}
		//isImgUploaded = false;
		if (!uploadPropImg()) {//upload propimg first
			return null;
		}
		
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemSkuAddRequest req=new ItemSkuAddRequest();
		req.setNumIid(numiid);
		if ((pidVid2 == null) || (pidVid2.equals("null"))){//may be a 'null' string from javascript
			req.setProperties(pidVid1);
			pidVid2 = null;
		}
		else {
			req.setProperties(pidVid1+";"+pidVid2);
		}
		if (pidVid1Alias == null || pidVid1Alias.equals("")) {
		}
		else {
			//updateItemColorAlias();			
		}
		if (pidVid2Alias == null || pidVid2Alias.equals("")) {
		}
		else {
			//updateItemSizeAlias();
		}
		//req.setProperties("1627207:3232484");//test
		req.setQuantity(quantity);
		req.setPrice(price);
		String jsonTxt = "";
		try {
			ItemSkuAddResponse response = client.execute(req , dataSource.getSessionKey());
			jsonTxt = response.getBody();
			System.out.println("====================="+jsonTxt);
			//{"item_sku_add_response":{"sku":{"created":"2013-10-04 02:08:43","iid":"2000038352852","num_iid":2000038352852,"sku_id":31050313094}}}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "发布宝贝遇到异常，请稍候重试！");
			if (jsonTxt == null) {
        		///////need to work on this. sometime successsful but jsonTxt == null
				msg.setSummary("发布宝贝遇到异常，请稍候重试！");
			}
			else if (jsonTxt.contains("error") || jsonTxt.contains("ERROR")) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	if (jsonTxt.contains("sub_msg")) {
	        		msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
	        	}
	        	else if (jsonTxt.contains("sub_code")) {
	        		msg.setSummary(json.getJSONObject("error_response").getString("sub_code"));
	        	}
	        	else {
	        		msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "发布宝贝遇到异常，请稍候重试！");
	        	}
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		    }
	        else {
	        	itemData.getPidVid1s().remove(itemData.getPidVid1s().size()-1);//remove the placeholder "new"
	        	itemData.getPidVid1s().add(pidVid1);
	        	itemData.getPidVid1s().add("new");//add a place holder
	        	itemData.getPidVid1ToPrice().put(pidVid1, price);
	        	itemData.getPidVid1ToName().put(pidVid1, itemData.getAllPidVidToNameForPid1().get(pidVid1));//update item detail title: color, (colorAlias to be done)
	        	itemData.getAllPidVidToNameForPid1().remove(pidVid1);//update publish item part: available pidVid1s, such as color
	        	itemData.getAvailablePidVid2ForPidVid1().put(pidVid1, itemData.getAllPidVidToNameForPid2());//update "add new pidvid2" part for the new pidvid1
	        	itemData.getPropToImg().put(pidVid1, getUploadedPropImgUrl());
	        	setUploadedPropImgUrl(null);
	        	//itemData.getAvailablePidVid2ForPidVid1().remove();//need to remove added one
	        	if ((pidVid2 == null) || (pidVid2.equals(""))) {
					itemData.getPropertiesToQuantity().put(pidVid1+";", new Integer((int)quantity));
				}
				else {
					itemData.getPropertiesToQuantity().put(pidVid1+";"+pidVid2+";", new Integer((int)quantity));
				}
				ArrayList<String> tmplist = new ArrayList<String>();
	        	tmplist.add(pidVid2);
	        	tmplist.add("new");//placeholder for add new pidVid2(size) sku
	        	itemData.getPidVid1ToPidVid2().put(pidVid1, tmplist);
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
				String skuId = json.getJSONObject("item_sku_add_response").getJSONObject("sku").getString("sku_id");

				//System.out.println(pidVid1+";"+pidVid2+"------------------");
				if ((pidVid2 == null) || (pidVid2.equals(""))) {
					itemData.getSkuPropertiesToSkuid().put(pidVid1+";", Long.parseLong(skuId));
					itemData.getSkuidToSkuProperties().put(Long.parseLong(skuId), pidVid1+";");
				}
				else {
					itemData.getSkuPropertiesToSkuid().put(pidVid1+";"+pidVid2 + ";", Long.parseLong(skuId));
					itemData.getSkuidToSkuProperties().put(Long.parseLong(skuId), pidVid1+";"+pidVid2 + ";");					
				}
				//update local database
				serializeItemData(String.valueOf(numiid),itemData);
				//more to be updated
		        msg.setSummary("成功发布宝贝！");
		        FacesContext.getCurrentInstance().addMessage(null, msg); 	
		        //reset the image to be shown
		        isImgUploaded = false;
		        createdFileName = null;
	        }
		} catch (ApiException e) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "添加商品失败,请重试！");  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean uploadPropImg() {
		/////upload new prop_img
		boolean booleanResult = true;
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemPropimgUploadRequest req=new ItemPropimgUploadRequest();
		req.setNumIid(numiid);
		req.setProperties(pidVid1);
		FileItem fileItem = new FileItem(imageFileName,imageFileContent);
		req.setImage(fileItem);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "");  

		try {
			//System.out.println(fileItem.getContent().length+"==================");
			ItemPropimgUploadResponse response = client.execute(req , dataSource.getSessionKey());
			String jsonTxt = response.getBody();
			System.out.println("uploadPropImg() fsonTxt=============="+jsonTxt);
			int count = 1;
			while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
				response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				if (count <= 0) {
					break;
				}
				count--;
				Thread.sleep(2000);
			}
			//{"error_response":{"code":530,"msg":"Remote service error","sub_code":"isv.missing-parameter:image","sub_msg":"图片为空！"}}
			if (jsonTxt == null) {
				booleanResult = false;
				msg.setSummary("连接淘宝错误，请重试！");
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        booleanResult = false;
			}
			else if (jsonTxt.contains("error") || jsonTxt.contains("ERROR")){
				booleanResult = false;
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
	        	if (jsonTxt.contains("sub_msg")) {
					msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
	        	}
	        	else if (jsonTxt.contains("sub_code")) {
					msg.setSummary(json.getJSONObject("error_response").getString("sub_code"));
	        	}
	        	else {
					msg.setSummary("连接淘宝错误，请重试！");
	        	}
				FacesContext.getCurrentInstance().addMessage(null, msg);
		        booleanResult = false;
			}
			else {
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
				setUploadedPropImgUrl(json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url"));
				itemData.getPropToImg().put(pidVid1, getUploadedPropImgUrl());
				serializeItemData(String.valueOf(numiid),itemData);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			booleanResult = false;
		}
		return booleanResult;
	}
	public void setWaterMarkTypeListener(String whichChecked) {
		if (whichChecked.equals("textWaterMark")) {
			if (userInfo.isTextWaterMarkCheckbox()) {
				userInfo.setTextWaterMarkCheckbox(true);
				userInfo.setPicWaterMarkCheckbox(false);
			}
		}
		if (whichChecked.equals("picWaterMark")) {
			if (userInfo.isPicWaterMarkCheckbox()) {
				userInfo.setPicWaterMarkCheckbox(true);
				userInfo.setTextWaterMarkCheckbox(false);
			}
		}
	}
	/*
	public void chooseWaterMarkImgListener(String imgIdNumber) {
		selectedWaterMarkImgToBeSaved = imgIdNumber;
		userInfo.setSelectedWaterMarkImg(selectedWaterMarkImgToBeSaved);
	}
	*/
	
	public void setToBeChangedWaterMarkImg() {
		String waterMarkImgID_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("waterMarkImgID");
		toBeChangedWaterMarkImg = waterMarkImgID_passed;
	}
	
	public void waterMarkImgFileUploadlistener(FileUploadEvent event) throws Exception {
	    UploadedFile file = event.getFile();
	    setImageFileName(file.getFileName());
	    setImageFileContent(file.getContents());
	    
	    saveFile(file, "waterMarkImg", null);
	    
	    userInfo.setWaterMarkImg1(imageFileName);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功添加水印图片！");  
	    FacesContext.getCurrentInstance().addMessage(null, msg);
	}	

	public void textWaterMarkContentColorChangeListener(ValueChangeEvent event) {
		userInfo.setTextWaterMarkContentColor(event.getNewValue().toString());
	}
	
	public void updateUserInfo() {
		//setSelectedWaterMarkImg(userInfo.getSelectedWaterMarkImg());
		if (userInfo.isPicWaterMarkCheckbox()) {
			setWaterMarkType("pic");
			setWaterMark(userInfo.getWaterMarkImg1());
		}
		else if (userInfo.isTextWaterMarkCheckbox()) {
			setWaterMarkType("text");
			setWaterMark(userInfo.getTextWaterMarkContent());
		}
		else {
			setWaterMark(null);
		}
		userInfo.setTextWaterMarkContentSize(waterMarkFontSize);
		userInfo.setWaterMarkPosition(waterMarkPosition);
		userInfoService.updateUserInfo(userInfo);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功更新水印设置信息！");  
        FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
	
	public void retrieveRecommendedItems() {
		progressData = new String[10];
		RetrieveForSaleItems retrieveForSaleItems = new RetrieveForSaleItems();
		ForSaleItemsData forSaleItemsData;
		forSaleItemsData = retrieveForSaleItems.retrieveForSaleItems(dataSource, leafCidList, progressData);
		setForSaleItemsData(forSaleItemsData);
		setNumiidToItemData(forSaleItemsData.getNumiidToItemData());
	}
	
	public TreeNode getTreeNode() {//should not do expensive operation in get, use @PostConstruct
		//SellerCatsTreeNode sellerCatsTreeNode = new SellerCatsTreeNode();
		//root = new DefaultTreeNode(sellerCatsTreeNode.new TreeNodeData("0","root",true), null);
		//sellerCatsTreeNode.createTreeNode(root,PARENT_CID,dataSource);
		return treeNode;
	}
	
	//a method annotated with @PostConstruct to perform actions directly after construction and dependency injection
	@PostConstruct
	public void init() {
		
		/*
		SellerCatsDropDownMenu sellerCatsDropDownMenu = new SellerCatsDropDownMenu();
		setSellerCatsMenuData(sellerCatsDropDownMenu.createSellerCatsDropDownMenu(dataSource));
		setLeafCidList(sellerCatsMenuData.getLeafCidList());
		*/
		HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.userNick = request.getParameter("nick");
		this.sessionKey = request.getParameter("sessionKey");
		dataSource.setNick(userNick);
		dataSource.setSessionKey(sessionKey);
		sessionManagementBean.setSessionKey(dataSource.getSessionKey());

			SellerCatsTreeNode sellerCatsTreeNode = new SellerCatsTreeNode();
			treeNode = new DefaultTreeNode(sellerCatsTreeNode.new TreeNodeData("0","root",true), null);
			List<String> leafCids = sellerCatsTreeNode.createTreeNode(treeNode,PARENT_CID,dataSource);
			if (leafCids != null) {
				setLeafCidList(leafCids);
			}
			else {
		        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误,请刷新重试！");  
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        RequestContext.getCurrentInstance().update("showMesage:messages");
				return;
			}

			RetrieveSellerInfo retriveSellerInfo = new RetrieveSellerInfo();
			SellerInfo sellerInfo = retriveSellerInfo.getSellerInfo(dataSource);
			if (sellerInfo != null) {
				setSellerInfo(sellerInfo);
			}
			else {
		        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误,请刷新重试！");  
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        RequestContext.getCurrentInstance().update("showMesage:messages");
				return;
			}
			
			List<UserInfo> userInfoList = userInfoService.search(userNick);
			if ((userInfoList != null) && (userInfoList.size() > 0)) {
				setUserInfo(userInfoList.get(0));
				//setSelectedWaterMarkImg(userInfo.getSelectedWaterMarkImg());
				if (userInfo.isPicWaterMarkCheckbox()) {
					setWaterMarkType("pic");
					setWaterMark(userInfo.getWaterMarkImg1());
					if (userInfo.getWaterMarkPosition() != 0) {
						this.waterMarkPosition = userInfo.getWaterMarkPosition();
					}
				}
				else if (userInfo.isTextWaterMarkCheckbox()) {
					setWaterMarkType("text");
					setWaterMark(userInfo.getTextWaterMarkContent());
					setWaterMarkFontSize(userInfo.getTextWaterMarkContentSize());
					if (userInfo.getWaterMarkPosition() != 0) {
						this.waterMarkPosition = userInfo.getWaterMarkPosition();
					}
				}
				else {
					setWaterMark(null);
					if (userInfo.getWaterMarkPosition() != 0) {
						this.waterMarkPosition = userInfo.getWaterMarkPosition();
					}
				}
			}
			else {
				userInfo = new UserInfo();
				userInfo.setUserNick(userNick);
				userInfo.setTextWaterMarkCheckbox(false);
				userInfo.setPicWaterMarkCheckbox(false);
				userInfo.setTextWaterMarkContentColor("000000");//default text color black
				userInfo.setTextWaterMarkContentSize(waterMarkFontSize);
				setUserInfo(userInfo);
			}
		    //////////////////////////////////////////////////////
		    //when not run within eclipse, comment the following line out
			previewWaterMark();
			
		
		setCachedItemsCidSet(new HashSet<String>(cachedItemsService.list()));
		

	    this.catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
	    imageFileContent = imgToByteArray(new File(catalinaBase, "webapps/SKUAssistant/images/notexist.png"));
	    uploadedImg = new DefaultStreamedContent(new ByteArrayInputStream(imgToByteArray(new File(catalinaBase, "webapps/SKUAssistant/images/notexist.png"))));
	    
	    recycledImgBean.setUserNick(userNick);
	    recycledImgBean.setCatalinaBase(catalinaBase);
	    ///////////////////user subscribe information
		/*
		TaobaoClient client=new DefaultTaobaoClient(url, appkey, secret);
		VasSubscribeGetRequest req=new VasSubscribeGetRequest();
		req.setNick("fads");
		req.setArticleCode("afads");//get articleCode from my.open.taobao.com
		VasSubscribeGetResponse response = client.execute(req);
		*/
	    
	    /*
	    VasSubscribeGetResponse response = RetrieveDataFromTaobao.vasSubscribeGet(dataSource);
	    List<ArticleUserSubscribe> subscribes = response.getArticleUserSubscribes();
	    for (ArticleUserSubscribe subscribe : subscribes) {
	    	subscribe.getDeadline();
	    	subscribe.getItemCode();
	    }
	    */
	    System.out.println("initializing finished--------------");
	}


	/*
	 * poll not working, use "<a href...." instead
	public void updateItems(String leafnodecid) {
		retrieveForSaleItemsUpdate();
	}
	*/
	public SellerCatsMenuData getSellerCatsMenuData() {
		return sellerCatsMenuData;
	}
	public void setSellerCatsMenuData(SellerCatsMenuData sellerCatsMenuData) {
		this.sellerCatsMenuData = sellerCatsMenuData;
	}

	public String getCid() {
		System.out.println("getCid called: "+cid);
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public long getNumiid() {
		return numiid;
	}
	
	public void setNumiid(long numiid) {
		this.numiid = numiid;
	}

	public void setCurrentItemImgPositionAndID() {
		String current_itemigm_position_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("current_itemimg_position");
		String current_itemigm_id_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("current_itemimg_id");
		String img_index_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("img_index");
		setImgIndex(Integer.valueOf(img_index_passed));
		//System.out.println("setCurrentItemImgPositionAndID called----------"+current_itemigm_position_passed);
		//setNumiid();//do not use this.use the following instead
		String numiid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		this.numiid = Long.parseLong(numiid_passed);
		String smallimgid_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("globalsmallitemimg_id");
		this.currentItemSmallImgId = smallimgid_passed;
		ItemData itemDataObj = numiidToItemData.get(String.valueOf(numiid));
		setItemData(itemDataObj);
		if (itemDataObj == null) {
			//System.out.println("xxxxxxxxxxx");
		}
		setCurrentItemImgId(current_itemigm_id_passed);
		setCurrentItemImgPosition(current_itemigm_position_passed);
	}
	
	public String saveEditedImg() {
		/////////////////////////////////////save main image
		if (whatIsEdited.equals("mainPic")) {
			//need to save a copy of the original image first
			
			///////////////////////////////
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemUpdateRequest  req=new ItemUpdateRequest();
			req.setNumIid(numiid);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功修改商品主图！");
			itemData = numiidToItemData.get(String.valueOf(numiid));
	
			//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
			//System.out.println(editImgBean.getImgContent().length+"======================...numiid=="+numiid);
			String [] urlParts = editImgBean.getImgUrlStr().split("\\.");
			//FileItem fileItem = new FileItem("tmpname.jpg",editImgBean.getImgContent());
			FileItem fileItem = new FileItem(editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1],editImgBean.getImgContent());
			req.setImage(fileItem);
			
			try {
				ItemUpdateResponse response = client.execute(req, dataSource.getSessionKey());
				System.out.println(response.getBody());
				int count = 1;
				while ((response.getBody() == null)||(response.getBody().contains("error"))) {
					response = client.execute(req, dataSource.getSessionKey());
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);				
				}
				if (response.getBody() == null) {
					updateImg[2] = "nochange";
			        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试！");  
			        FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				else if (response.getBody().contains("error")) {
					updateImg[2] = "nochange";
					//msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "修改失败，请重试！");
		        	JSONObject json = (JSONObject) JSONSerializer.toJSON(response.getBody());
		        	msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				else {	
						//if update image, need to setNewItemImgUrl to show the new image
						TaobaoClient client2=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
						ItemGetRequest req2=new ItemGetRequest();
						req2.setFields("item_img");
						req2.setNumIid(numiid);
						
						try {
							String jsonTxt2 = "";
							ItemGetResponse response2 = client2.execute(req2, dataSource.getSessionKey());
							jsonTxt2 = response2.getBody();
							int count2 = 1;
							while ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
								response2 = client2.execute(req2, dataSource.getSessionKey());
								if (count2 <= 0) {
									break;
								}
								count2--;
								Thread.sleep(2000);				
							}
	
							if ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
								//updateImg[0] = null;							
								updateImg[2] = "nodisplay"; //image updated but retrieve updated image url error,should let users know this, such as show a special image for this information
							}
							else {
								JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt2); 
								JSONObject itemoj= json2.getJSONObject("item_get_response").getJSONObject("item");
								JSONArray itemImgArray = itemoj.getJSONObject("item_imgs").getJSONArray("item_img");
								for (int i=0;i<itemImgArray.size();i++) {
									JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
									if (tmpobj.getInt("id") == 0) {
										setNewItemImgUrl(tmpobj.getString("url"));
										updateImg[0] = tmpobj.getString("url");
										updateImg[2] = "display";
										itemData.getItemImgs()[0] = tmpobj.getString("url");
										itemData.getItemImgIDs()[0] = "0";
										//System.out.println("------"+newItemImgUrl);
										editImgBean.setImgUrlStr(newItemImgUrl);
										//break;
									}
									else {
										//////////////after update mainPic, all the other 4 additional image ids will be changed, so need to update them in local database
										//////////////////////////very important. tested in sandbox, but not in formal enviroment 
										//////////////////////////it is a good practice to update as much information as possible in itemData
										//////////////////////////
										itemData.getItemImgs()[i] = tmpobj.getString("url");
										itemData.getItemImgIDs()[i] = tmpobj.getString("id");
									}
								}
								//update local database
								//forSaleItemsData.getNumiidToItemPrice().put(String.valueOf(numiid), Double.valueOf(price));
								itemData.setItemPicUrl(updateImg[0]);
								serializeItemData(String.valueOf(numiid),itemData);
								FacesContext.getCurrentInstance().addMessage(null, msg);
								//RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
								//do this in xhtml
							}
						}
						catch(ApiException e2) {
							FacesContext.getCurrentInstance().addMessage(null, msg);	
							updateImg[2] = "nodisplay";
						}
					}
	
			} catch (Exception e) {
				updateImg[2] = "nochange";
				FacesContext.getCurrentInstance().addMessage(null, msg);
				e.printStackTrace();
			}
		}
		/////////////////////////////////////////////////////////////////
		///////附图
		else if (whatIsEdited.equals("additionalPic")) {
			String [] item_image_ids = itemData.getItemImgIDs();
			//System.out.println(imgIndex+"+++++++++++++++++++++");
			//System.out.println(item_image_ids[imgIndex]+"-------------");

			//req.setId(Long.parseLong(currentItemImgId));
			//save a copy of the original image in local
			//get the original image and save it before deletion

			boolean imgExist = saveImg(item_image_ids[imgIndex]);
			if(imgExist) {
				TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
				ItemImgDeleteRequest req=new ItemImgDeleteRequest();
				req.setId(Long.parseLong(item_image_ids[imgIndex]));//get img id by index instead
				req.setNumIid(numiid);
				try {
					ItemImgDeleteResponse response = client.execute(req , dataSource.getSessionKey());
					String jsonTxt = response.getBody();
					System.out.println("delete response========="+jsonTxt);
					int count = 1;
					while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
						response = client.execute(req , dataSource.getSessionKey());
						jsonTxt = response.getBody();
						if (count <= 0) {
							break;
						}
						count--;
						Thread.sleep(2000);
					}
					if (jsonTxt == null) {
				        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试！");  
				        FacesContext.getCurrentInstance().addMessage(null, msg);
				        updateImg[2] = "nochange";
					}
					else if (jsonTxt.contains("error")) {
						JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
						FacesContext.getCurrentInstance().addMessage(null, msg);
						updateImg[2] = "nochange";
					}
					//remove image id from the List itemData.getItemImgIDs()
					else {
						item_image_ids[imgIndex] = null;
						itemData.getItemImgs()[imgIndex] = null;
						itemData.getItemImgIDs()[imgIndex] = null;
						updateImg[0] = null;
						updateImg[2] = "nodisplay";
						//update local database
						serializeItemData(String.valueOf(numiid),itemData);
	
					}
				} catch (Exception e) {
					updateImg[2] = "nochange";
					e.printStackTrace();
					return null;//if delete error, you cannot add, so skip the add by returnning false
				}
				
				//add new img
				ItemImgUploadRequest  req2=new ItemImgUploadRequest();
				req2.setNumIid(numiid);
				//req2.setPosition(0L);
			
				//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
				String [] urlParts = editImgBean.getImgUrlStr().split("\\.");
				//FileItem fileItem = new FileItem("tmpname.jpg",editImgBean.getImgContent());
				FileItem fileItem = new FileItem(editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1],editImgBean.getImgContent());
				req2.setImage(fileItem);
				try {
					ItemImgUploadResponse response = client.execute(req2 , dataSource.getSessionKey());
					String jsonTxt = response.getBody();
					System.out.println("response body from addItemImg(String current_itemimg_pos) **************************************"+jsonTxt);
					JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
					
					int count = 1;
					while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
						response = client.execute(req2 , dataSource.getSessionKey());
						jsonTxt = response.getBody();
						if (count <= 0) {
							break;
						}
						count--;
						Thread.sleep(2000);
					}
					if (jsonTxt == null) {
				        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝错误，请重试！");  
				        FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					else if (jsonTxt.contains("error")) {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					else{
						JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt);
						JSONObject jsonimgObj = json2.getJSONObject("item_img_upload_response").getJSONObject("item_img");				
						//add a new image id to itemData.getItemImgIDs()
						String added_image_id = jsonimgObj.getString("id");
		
						item_image_ids[imgIndex] = added_image_id;
						itemData.getItemImgIDs()[imgIndex]= added_image_id;
						setNewItemImgUrl(jsonimgObj.getString("url"));
						updateImg[0] = jsonimgObj.getString("url");
						updateImg[2] = "display";
						itemData.getItemImgs()[imgIndex] = jsonimgObj.getString("url");
						editImgBean.setImgUrlStr(jsonimgObj.getString("url"));
						//update local database
						serializeItemData(String.valueOf(numiid),itemData);
				        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功保存图片！");  
				        FacesContext.getCurrentInstance().addMessage(null, msg);
						//RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
				        //do this in xhtml
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "此商品图片不存在,请点击目录更新！");
				FacesContext.getCurrentInstance().addMessage(null, msg);			
		        //RequestContext.getCurrentInstance().update("newItemImg:new_item_img");//show the new img
		        updateImg[2] = "nochange";
			}
		}
		////////属性图 propImg
		else if (whatIsEdited.equals("propPic")) {
			//FileItem fileItem = new FileItem("tmpname.jpg",editImgBean.getImgContent());			
			String [] urlParts = editImgBean.getImgUrlStr().split("\\.");
			setImageFileName(editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
			setImageFileContent(editImgBean.getImgContent());
			String updatedImgUrl = addOrUpdateItemPropImg();
			editImgBean.setImgUrlStr(updatedImgUrl);
		}
		return null;
	}
	

	
	public String getCurrentItemImgId() {
		return currentItemImgId;
	}

	public void setCurrentItemImgId(String currentItemImgId) {
		//System.out.println("setCurrentItemImgId---------"+currentItemImgId);
		this.currentItemImgId = currentItemImgId;
	}

	public String getCurrentItemImgPosition() {
		return currentItemImgPosition;
	}

	public void setCurrentItemImgPosition(String currentItemImgPosition) {
		//System.out.println("setCurrentItemImgPosition---------"+currentItemImgPosition);
		this.currentItemImgPosition = currentItemImgPosition;
	}

	public ForSaleItemsData getForSaleItemsData() {
		return forSaleItemsData;
	}

	public void setForSaleItemsData(ForSaleItemsData forSaleItemsData) {
		this.forSaleItemsData = forSaleItemsData;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public RetrieveItem.ItemData getItemData() {
		return itemData;
	}

	public void setItemData(RetrieveItem.ItemData itemData) {
		this.itemData = itemData;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
	public long getDefaultQuantityChange() {
		return defaultQuantityChange;
	}

	public void setDefaultQuantityChange(long defaultQuantityChange) {
		this.defaultQuantityChange = defaultQuantityChange;
	}

	public void setQuantityByAjax() {
		String quantity_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("num_of_stock_change");
		this.quantity = Long.parseLong(quantity_passed);
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSkuChange() {
		return skuChange;
	}

	public void setSkuChange(String skuChange) {
		this.skuChange = skuChange;
	}

	public String getSkuProperties() {
		return skuProperties;
	}

	public void setSkuProperties(String skuProperties) {
		this.skuProperties = skuProperties;
	}

	public String getDetail_url() {
		return detail_url;
	}

	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public byte[] getImageFileContent() {
		return imageFileContent;
	}

	public void setImageFileContent(byte[] imageFileContent) {
		this.imageFileContent = imageFileContent;
	}

	public boolean getShowAjaxIndicator() {
		return showAjaxIndicator;
	}

	public void setShowAjaxIndicator(boolean showAjaxIndicator) {
		this.showAjaxIndicator = showAjaxIndicator;
	}

	public String getPidVid1() {
		return pidVid1;
	}

	public void setPidVid1() {
		String pidVid1_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pidVid1");
		this.pidVid1 = pidVid1_passed;
	}
	public void setPidVid1(String pidVid1) {
		this.pidVid1 = pidVid1;
	}

	public String getPidVid2() {
		return pidVid2;
	}

	public void setPidVid2(String pidVid2) {
		this.pidVid2 = pidVid2;
	}
	
	public String getPidVid3() {
		return pidVid3;
	}

	public void setPidVid3(String pidVid3) {
		this.pidVid3 = pidVid3;
	}


	public String getPidVid1Alias() {
		return pidVid1Alias;
	}

	public void setPidVid1Alias(String pidVid1Alias) {
		this.pidVid1Alias = pidVid1Alias;
	}

	public String getPidVid2Alias() {
		return pidVid2Alias;
	}

	public void setPidVid2Alias(String pidVid2Alias) {
		this.pidVid2Alias = pidVid2Alias;
	}
	
	public String getPidVid3Alias() {
		return pidVid3Alias;
	}

	public void setPidVid3Alias(String pidVid3Alias) {
		this.pidVid3Alias = pidVid3Alias;
	}

	public Map<String, String> getAvailablePidVid2ForEachPidVid1() {
		return availablePidVid2ForEachPidVid1;
	}

	public void setAvailablePidVid2ForEachPidVid1(
			Map<String, String> availablePidVid2ForEachPidVid1) {
		this.availablePidVid2ForEachPidVid1 = availablePidVid2ForEachPidVid1;
	}

	public String getUploadedPropImgUrl() {
		return uploadedPropImgUrl;
	}

	public void setUploadedPropImgUrl(String uploadedPropImgUrl) {
		this.uploadedPropImgUrl = uploadedPropImgUrl;
	}

	public String getNewItemImgUrl() {
		return newItemImgUrl;
	}

	public void setNewItemImgUrl(String newItemImgUrl) {
		this.newItemImgUrl = newItemImgUrl;
	}

	
	public String[] getUpdateImg() {
		return updateImg;
	}

	public void setUpdateImg(String[] updateImg) {
		this.updateImg = updateImg;
	}

	public SellerInfo getSellerInfo() {
		return sellerInfo;
	}

	public void setSellerInfo(SellerInfo sellerInfo) {
		this.sellerInfo = sellerInfo;
	}

	public Map<String, ItemData> getNumiidToItemData() {
		return numiidToItemData;
	}

	public void setNumiidToItemData(Map<String, ItemData> numiidToItemData) {
		this.numiidToItemData = numiidToItemData;
	}
	
	//make sure the following two method execute in order
	private boolean isSetParamForRecomCalled = false;
	public void setParamForRecom() {
		String isRecommended_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isRecommended");
		setIsRecommended(isRecommended_passed);
		isSetParamForRecomCalled = true;
	}

	public String recommendItem() {
		FacesMessage msg;
		//////////check remaining showcase number
		TaobaoClient clientShowCase=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ShopRemainshowcaseGetRequest reqShowCase=new ShopRemainshowcaseGetRequest();
		ShopRemainshowcaseGetResponse responseShowCase = null;
		try {
			int count = 1;
			responseShowCase = clientShowCase.execute(reqShowCase, sessionKey);
			while (responseShowCase == null) {
				responseShowCase = clientShowCase.execute(reqShowCase, sessionKey);
				if (count <= 0) {
			        break;
				}
				count--;
				Thread.sleep(2000);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (responseShowCase == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "连接淘宝超时，请重试！");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		int all_count = 0;
		int remain_count = 0;
		int used_count = 0;

		System.out.println(responseShowCase.getBody());
		//{"shop_remainshowcase_get_response":{"shop":{"all_count":5,"remain_count":0,"used_count":5}}}
		if (responseShowCase.getBody().contains("remain_count")) {
        	JSONObject jsonShowCase = (JSONObject) JSONSerializer.toJSON(responseShowCase.getBody());
        	all_count = jsonShowCase.getJSONObject("shop_remainshowcase_get_response").getJSONObject("shop").getInt("all_count");
        	remain_count = jsonShowCase.getJSONObject("shop_remainshowcase_get_response").getJSONObject("shop").getInt("remain_count");
        	used_count = jsonShowCase.getJSONObject("shop_remainshowcase_get_response").getJSONObject("shop").getInt("used_count");

		}
		while (isSetParamForRecomCalled == false) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isSetParamForRecomCalled == true) {
				break;
			}
		}
		isSetParamForRecomCalled = false;
		boolean flag;
		//if (isRecommended.equals("yes")) {
		if (isRecommended == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "操作失败，请重试！");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		else if (isRecommended.contains("notrecommend")) {
			//do recommendation
			if (remain_count-1 >= 0) {
				flag = true;
				if (responseShowCase.getBody().contains("remain_count")) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功推荐商品！还有"+(remain_count-1)+"个橱窗位。");
				}
				else {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功推荐商品！");
				}
			}
			else {
				flag = false;
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "没有剩余的橱窗位,不能推荐。");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("failure", "failure");//do not change button
        		return null;
			}
		}
		else {
			//cancel recommendation
			flag = false;
			if (responseShowCase.getBody().contains("remain_count")) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功取消橱窗推荐！还有"+(remain_count+1)+"个橱窗位。");
			}
			else {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功取消橱窗推荐！");
			}
		}
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());

		ItemUpdateRequest req=new ItemUpdateRequest();
		req.setNumIid(numiid);
		req.setHasShowcase(flag);
		itemData = numiidToItemData.get(String.valueOf(numiid));
		try {
			ItemUpdateResponse response = client.execute(req , dataSource.getSessionKey());
			if (response.getBody().contains("error")) {
				//msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "橱窗操作出错，请重试！");
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(response.getBody());
	        	msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("failure", "failure");
			}
			else {
				//itemData.setHasShowcase(flag);//cannot do this, which will change the UI render and make the comandlink button not to work
				boolean tmpHasShowCase = itemData.isHasShowcase();
				itemData.setHasShowcase(flag);
				serializeItemData(String.valueOf(numiid),itemData);
				itemData.setHasShowcase(tmpHasShowCase);
	        	if ((remain_count <= 0) && (flag == true)){
	        		msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "没有剩余的橱窗！");
					FacesContext.getCurrentInstance().addMessage(null, msg);
	        		return null;
	        	}
	        	else {
	        		FacesContext.getCurrentInstance().addMessage(null, msg);
	        	}
			}
			System.out.println(response.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "橱窗操作出错，请重试！");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().addCallbackParam("failure", "failure");
		}
		return null;
		
	}

	//make sure the following two method execute in order
	private boolean isSetParamForApproveStatusCalled = false;
	public void setParamForApproveStatus() {
		String approveStatus_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("approveStatus");
		setApproveStatus(approveStatus_passed);
		isSetParamForApproveStatusCalled = true;
	}
	
	public String approveStatusChange() {
		while (isSetParamForApproveStatusCalled == false) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isSetParamForApproveStatusCalled == true) {
				break;
			}
		}
		isSetParamForApproveStatusCalled = false;
		String status;
		FacesMessage msg = null;
		//if (approveStatus.equals("onsale")) {
		if (approveStatus == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "操作失败，请重试！");
			return null;
		}
		else if (approveStatus.contains("instock")) {
			//put on sale
			status = "onsale";
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功上架商品！");
		}
		else {
			//put in stock
			status = "instock";
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功下架商品！");
		}
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());

		ItemUpdateRequest req=new ItemUpdateRequest();
		req.setNumIid(numiid);
		req.setApproveStatus(status);
		itemData = numiidToItemData.get(String.valueOf(numiid));
		try {
			ItemUpdateResponse response = client.execute(req , dataSource.getSessionKey());
			System.out.println(response.getBody());
			if ((response.getBody() == null) || (response.getBody().contains("error"))) {
	        	JSONObject json = (JSONObject) JSONSerializer.toJSON(response.getBody());
	        	msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().addCallbackParam("failure", "failure");
			}
			else {
				//itemData.setApproveStatus(status);//cannot do this, which will change the UI render and make the comandlink button not to work
				String tmpStatus = new String(itemData.getApproveStatus());
				itemData.setApproveStatus(status);
				serializeItemData(String.valueOf(numiid),itemData);
				itemData.setApproveStatus(tmpStatus);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} 
		catch (ApiException e) {
			e.printStackTrace();
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "上下架操作出错，请重试！");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().addCallbackParam("failure", "failure");
		}
		return null;
		
	}
	
	public void idleListener() {  
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,   
	                                        "Your session is closed", "You have been idle for at least 5 seconds"));  
	         
	        //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	        //invalidate session 
	        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().invalidate();
	        try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("https://oauth.taobao.com/authorize?response_type=token&client_id=21589200");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}  
	
	/*
	public void activeListener() {  
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,  
	                                    "Welcome Back", "That's a long coffee break!"));  
	} 
	*/ 

	public void showProgress() {
		
	}
	public String getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(String isRecommended) {
		this.isRecommended = isRecommended;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public int getImgIndex() {
		return imgIndex;
	}

	public void setImgIndex(int imgIndex) {
		this.imgIndex = imgIndex;
	}

	public String getSkuBatchUpdateStr() {
		return skuBatchUpdateStr;
	}

	public void setSkuBatchUpdateStr(String skuBatchUpdateStr) {
		this.skuBatchUpdateStr = skuBatchUpdateStr;
	}

	public List<String> getLeafCidList() {
		return leafCidList;
	}

	public void setLeafCidList(List<String> leafCidList) {
		this.leafCidList = leafCidList;
	}
	

	public String getWaterMark() {
		return waterMark;
	}

	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}

	public String getWaterMarkType() {
		return waterMarkType;
	}

	public void setWaterMarkType(String waterMarkType) {
		this.waterMarkType =  waterMarkType;
	}
	
	public String getProgressDataString() {
		StringBuilder strb = new StringBuilder();
		if (progressData != null) {
		for(int i=0;i<10;i++) {
			if (progressData[i] != null) {
				strb.append(progressData[i]);
				strb.append("<br/>");
			}
		}
		}
		return strb.toString();
	}

	public RetrieveForSaleItems getRetrieveForSaleItems() {
		return retrieveForSaleItems;
	}

	public void setRetrieveForSaleItems(RetrieveForSaleItems retrieveForSaleItems) {
		this.retrieveForSaleItems = retrieveForSaleItems;
	}

	public StreamedContent getUploadedImg() {
		//return uploadedImg;
        //get uploadedImg
        FacesContext context = FacesContext.getCurrentInstance();
        /*
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
        	uploadedImg = new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
        	uploadedImg = new DefaultStreamedContent(new ByteArrayInputStream(getImageFileContent()));
        }
        */
    	uploadedImg = new DefaultStreamedContent(new ByteArrayInputStream(getImageFileContent()));
       return uploadedImg;
	}

	public void setUploadedImg(StreamedContent uploadedImg) {
		this.uploadedImg = uploadedImg;
	}

	public SessionManagementBean getSessionManagementBean() {
		return sessionManagementBean;
	}

	public void setSessionManagementBean(SessionManagementBean sessionManagementBean) {
		this.sessionManagementBean = sessionManagementBean;
	}

	public UserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getSelectedWaterMarkImg() {
		return selectedWaterMarkImg;
	}

	public void setSelectedWaterMarkImg(String selectedWaterMarkImg) {
		this.selectedWaterMarkImg = selectedWaterMarkImg;
	}

	public boolean isPicWaterMark() {
		return picWaterMark;
	}

	public void setPicWaterMark(boolean picWaterMark) {
		this.picWaterMark = picWaterMark;
	}

	public boolean isTextWaterMark() {
		return textWaterMark;
	}

	public void setTextWaterMark(boolean textWaterMark) {
		this.textWaterMark = textWaterMark;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getCreatedFileName() {
		return createdFileName;
	}

	public void setCreatedFileName(String createdFileName) {
		this.createdFileName = createdFileName;
	}

	public CachedItemsService getCachedItemsService() {
		return cachedItemsService;
	}

	public void setCachedItemsService(CachedItemsService cachedItemsService) {
		this.cachedItemsService = cachedItemsService;
	}

	public CachedItemsDataService getCachedItemsDataService() {
		return cachedItemsDataService;
	}

	public void setCachedItemsDataService(CachedItemsDataService cachedItemsDataService) {
		this.cachedItemsDataService = cachedItemsDataService;
	}

	public EditImgBean getEditImgBean() {
		return editImgBean;
	}

	public void setEditImgBean(EditImgBean editImgBean) {
		this.editImgBean = editImgBean;
	}

	public Set<String> getCachedItemsCidSet() {
		return cachedItemsCidSet;
	}

	public void setCachedItemsCidSet(Set<String> cachedItemsCidSet) {
		this.cachedItemsCidSet = cachedItemsCidSet;
	}



	public File getCatalinaBase() {
		return catalinaBase;
	}

	public void setCatalinaBase(File catalinaBase) {
		this.catalinaBase = catalinaBase;
	}
	
	private boolean saveImg(String imgID) {
		//get the original image and save it before deletion
		TaobaoClient clientSave=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemGetRequest reqSave=new ItemGetRequest();
		//req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
		reqSave.setFields("title,item_img");
		reqSave.setNumIid(numiid);
		ItemGetResponse responseSave = null;
		try {
			responseSave = clientSave.execute(reqSave, dataSource.getSessionKey());
		} catch (ApiException e1) {
			e1.printStackTrace();
		}
		String jsonTxtSave = responseSave.getBody();
		boolean imgExist = false;
		JSONObject jsonSave = (JSONObject) JSONSerializer.toJSON(jsonTxtSave);
		JSONArray itemImgArray = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img");
		String imgUrlSave = null;
		for (int i=0;i<itemImgArray.size();i++) {
			JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
			if (tmpobj.getString("id").equals(imgID)) {
				imgUrlSave = tmpobj.getString("url");
				imgExist = true;
				break;
			}		
		}
		if (imgExist) {
			String [] urlParts = imgUrlSave.split("\\.");
			byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
		    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
		    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
		    if (!dir.exists()) {
		    	dir.mkdirs();
		    }
		    FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
				fos.write(imgSave);
				fos.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return imgExist;
	}

	public String getImgToBeWaterMarkedtmp() {
		return imgToBeWaterMarkedtmp;
	}

	public void setImgToBeWaterMarkedtmp(String imgToBeWaterMarkedtmp) {
		this.imgToBeWaterMarkedtmp = imgToBeWaterMarkedtmp;
	}

	public void setImgToBeWaterMarked(String imgToBeWaterMarked) {
		this.imgToBeWaterMarked = imgToBeWaterMarked;
	}
	public void picSelectValueChanged(AjaxBehaviorEvent event) {
		setImgToBeWaterMarked(imgToBeWaterMarkedtmp);
	}
	
	public String addWaterMark() {
		setWaterMarkedImgs(null);
		if (waterMark == null) {
	        //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "请设置水印!!");  
	        //FacesContext.getCurrentInstance().addMessage(null, msg);
	        waterMarkCompleted = "请设置水印！";
			try {
				Thread.sleep(2000);//wait 2 sesonds for the poll to update
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		String numiid_pased = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numiid");
		setNumiid(Long.valueOf(numiid_pased)); 		
		setNumiid(numiid);
		setItemData(numiidToItemData.get(String.valueOf(numiid)));
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "添加水印出错，请重试！");  
		try {
	        if(addWaterMarkImgs(imgToBeWaterMarked,msg)) {
				/*
				for (int i=0;i<waterMarkedImgs.length;i++) {
					System.out.println(waterMarkedImgs[i]+"--------------waterMarkedImgs--------"+i);
				}
				*/
				try {
					Thread.sleep(2000);//wait 2 sesonds for the poll to update
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	
		        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "成功添加水印！");  
				//waterMarkCompleted = "以上图片成功加水印！";
			}
			else {
			}
		}
		catch(Exception e) {
			
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);			
		//imgToBeWaterMarked = "mainPic";//reset
		//imgToBeWaterMarkedtmp = "mainPic";//reset
		imgToBeWaterMarked = "mainAndAdditionalPic";//reset
		imgToBeWaterMarkedtmp = "mainAndAdditionalPic";//reset
		waterMarkedImgs = null;//reset
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private boolean addWaterMarkImgs(String forWhat, FacesMessage msg) {
		boolean booleanResult = false;
		waterMarkCompleted = null;
		if (forWhat.equals("mainPic")) {			
			booleanResult = addWaterMarkForMainPic(saveCopyOfImg(forWhat), msg);
		}
		else if (forWhat.equals("additionalPic")) {			
			booleanResult = addWaterMarkForAdditionalPic(saveCopyOfImg(forWhat), msg);
		}
		else if (forWhat.equals("mainAndAdditionalPic")) {			
			booleanResult = addWaterMarkForMainAndAdditionalPic(saveCopyOfImg(forWhat), msg);
		}
		else if (forWhat.equals("propPic")) {			
			booleanResult = addWaterMarkForPropPic(saveCopyOfImg(forWhat));
		}
		else if (forWhat.equals("allPic")) {			
			booleanResult = addWaterMarkForAllPic(saveCopyOfImg(forWhat));
		}
		return booleanResult;
	}
	

	public String [] getWaterMarkedImgs() {
		return waterMarkedImgs;
	}

	public void setWaterMarkedImgs(String [] waterMarkedImgs) {
		this.waterMarkedImgs = waterMarkedImgs;
	}

	public String getWaterMarkCompleted() {
		return waterMarkCompleted;
	}

	public void setWaterMarkCompleted(String waterMarkCompleted) {
		this.waterMarkCompleted = waterMarkCompleted;
	}

	public String getWhatIsEdited() {
		return whatIsEdited;
	}

	public void setWhatIsEdited(String whatIsEdited) {
		this.whatIsEdited = whatIsEdited;
	}

	public String getPropImgUrl() {
		return propImgUrl;
	}

	public void setPropImgUrl(String propImgUrl) {
		this.propImgUrl = propImgUrl;
	}

	private void saveFile(UploadedFile file, String location, String fileName) {
	    File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
	    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/"+location+"/"+userNick);
	    String[] fileNameParts = file.getFileName().split("\\.");
	    
	    if (fileName != null) {
	    	createdFileName =  fileName + "." + fileNameParts[1];
	    }
	    else {
	    	createdFileName = file.getFileName();
	    }
	    if (!dir.exists()) {
	    	dir.mkdirs();
	    }
	    try {
		    FileOutputStream fos = new FileOutputStream(dir.getPath().toString()+"/"+createdFileName);
		    byte[] buffer = new byte[1024];
		    int bytesRead = 0;
		    InputStream inputStream = file.getInputstream();
		    while((bytesRead = inputStream.read(buffer)) > 0) {
		    	fos.write(buffer,0,bytesRead);
		    	fos.flush();
		    }		    
		    fos.close();
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	private ImgsToBeWaterMarked saveCopyOfImg(String forWhat) {//save a copy of image before updating
		ImgsToBeWaterMarked imgsToBeWaterMarked = new ImgsToBeWaterMarked();
		TaobaoClient clientSave=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemGetRequest reqSave=new ItemGetRequest();
		//req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
		if ((forWhat.equals("mainPic")) || forWhat.equals("additionalPic") || forWhat.equals("mainAndAdditionalPic")) {
			reqSave.setFields("title,item_img");
		}
		else {
			reqSave.setFields("title,prop_img");
		}
		reqSave.setNumIid(numiid);
		ItemGetResponse responseSave = null;
		try {
			responseSave = clientSave.execute(reqSave, dataSource.getSessionKey());
		} catch (ApiException e) {
			e.printStackTrace();
		}
		String jsonTxtSave = responseSave.getBody();
		System.out.println(jsonTxtSave);
		if (jsonTxtSave.contains("item_get_response")) {
			JSONObject jsonSave = (JSONObject) JSONSerializer.toJSON(jsonTxtSave);
			if (forWhat.equals("mainPic")) {
				JSONObject tmpobjSave = (JSONObject)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img").get(0);
				String imgUrlSave = tmpobjSave.getString("url");
				waterMarkedImgs = new String[1];
				String [] urlstmp = new String[1];
				urlstmp[0] = imgUrlSave;
				String [] urlParts = imgUrlSave.split("\\.");
				byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
			    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
			    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
			    if (!dir.exists()) {
			    	dir.mkdirs();
			    }
			    FileOutputStream fos;
				try {
					fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
					fos.write(imgSave);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				imgsToBeWaterMarked.setUrls(urlstmp);
			}
			else if (forWhat.equals("additionalPic")) {
				JSONArray itemImgArray = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img");
				String imgUrlSave = null;
				waterMarkedImgs = new String[itemImgArray.size()-1];//do not include the main image
				String [] imgIDstmp = new String[itemImgArray.size()-1];
				String [] urlstmp = new String[itemImgArray.size()-1];	
				int k = 0;
				for (int i=0;i<itemImgArray.size();i++) {
					JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
					String imgID = tmpobj.getString("id");
					if (!imgID.equals("0")) {
						if (i > 4) {
							break;
						}
						imgIDstmp[k] = imgID;
						/////////////save a copy of the image
						imgUrlSave = tmpobj.getString("url");
						urlstmp[k] = imgUrlSave;
						k++;
						//System.out.println(imgID+"-----===--------");
						String [] urlParts = imgUrlSave.split("\\.");
						byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
					    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
					    //System.out.println(catalinaBase.toString()+"===============================");
					    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
					    if (!dir.exists()) {
					    	dir.mkdirs();
					    }
					    FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
							fos.write(imgSave);
							fos.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				imgsToBeWaterMarked.setImgIDs(imgIDstmp);
				imgsToBeWaterMarked.setUrls(urlstmp);
			}
			else if (forWhat.equals("mainAndAdditionalPic")) {			
				JSONArray itemImgArray = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img");
				//System.out.println(itemImgArray.size()+"-******save main and additional******************************");
				String imgUrlSave = null;
				waterMarkedImgs = new String[itemImgArray.size()];
				String [] imgIDstmp = new String[itemImgArray.size()];
				String [] urlstmp = new String[itemImgArray.size()];			
				for (int i=0;i<itemImgArray.size();i++) {
					JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
						imgIDstmp[i] = tmpobj.getString("id");
						imgUrlSave = tmpobj.getString("url");
						urlstmp[i] = imgUrlSave;
						String [] urlParts = imgUrlSave.split("\\.");
						byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
					    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
					    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
					    if (!dir.exists()) {
					    	dir.mkdirs();
					    }
					    FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
							fos.write(imgSave);
							fos.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
				}
				imgsToBeWaterMarked.setImgIDs(imgIDstmp);
				imgsToBeWaterMarked.setUrls(urlstmp);			
			}
			else if (forWhat.equals("propPic")) {
				JSONArray propImgArray = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("prop_imgs").getJSONArray("prop_img");
				String imgUrlSave = null;
				waterMarkedImgs = new String[propImgArray.size()];
				String [] imgIDstmp = new String[propImgArray.size()];
				String [] urlstmp = new String[propImgArray.size()];
				String [] propertiestmp = new String[propImgArray.size()];
				for (int i=0;i<propImgArray.size();i++) {
					JSONObject tmpobj = (JSONObject)propImgArray.get(i);				
					imgIDstmp[i] = tmpobj.getString("id");
					propertiestmp[i] = tmpobj.getString("properties");
					imgUrlSave = tmpobj.getString("url");
					urlstmp[i] = imgUrlSave;
					String [] urlParts = imgUrlSave.split("\\.");
					byte [] imgSave = editImgBean.remoteImgToByteArray(imgUrlSave);
				    //File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
				    File dir = new File(catalinaBase, "webapps/SKUAssistant/data/recycle/"+userNick);
				    if (!dir.exists()) {
				    	dir.mkdirs();
				    }
				    FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(dir.getPath().toString()+"/"+editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1]);
						fos.write(imgSave);
						fos.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				imgsToBeWaterMarked.setImgIDs(imgIDstmp);
				imgsToBeWaterMarked.setUrls(urlstmp);
				imgsToBeWaterMarked.setProperties(propertiestmp);
			}
		}
		return imgsToBeWaterMarked;
	}

	private boolean addWaterMarkForMainPic(ImgsToBeWaterMarked imgsToBeWaterMarked, FacesMessage msg) {//save waterMarked mainPic
		/*
		/////////////////////////////////////////////////
		TaobaoClient clientSave=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemGetRequest reqSave=new ItemGetRequest();
		reqSave.setFields("item_img");
		reqSave.setNumIid(numiid);
		ItemGetResponse responseSave = null;
		try {
			responseSave = clientSave.execute(reqSave, dataSource.getSessionKey());
		} catch (ApiException e1) {
			e1.printStackTrace();
		}
		String jsonTxtSave = responseSave.getBody();
		JSONObject jsonSave = (JSONObject) JSONSerializer.toJSON(jsonTxtSave);
		JSONArray itemImgArrayx = (JSONArray)jsonSave.getJSONObject("item_get_response").getJSONObject("item").getJSONObject("item_imgs").getJSONArray("item_img");
		for (int i=0;i<itemImgArrayx.size();i++) {
			JSONObject tmpobj = (JSONObject)itemImgArrayx.get(i);
			System.out.println("before adding watermark mainPic, get imgID from taobao>>>>>>>>>>>>>>>test>>>>>>>>"+tmpobj.getString("id"));
		}
		////////////////////////////////////////////////
		 
		 */
		//////////////add watermark to main image
		boolean booleanResult = false;
		//System.out.println(imgsToBeWaterMarked.getUrls().length+"===length============url0====="+imgsToBeWaterMarked.getUrls()[0]);
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		ItemUpdateRequest req=new ItemUpdateRequest();
		req.setNumIid(numiid);
		//itemData = numiidToItemData.get(String.valueOf(numiid));//do not need this line. itemData already set and may have changed by other operations. this line will reset it and cause some problem for subsequent operations
		//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(editImgBean.remoteImgToByteArray(imgsToBeWaterMarked.getUrls()[0]), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),sessionKey,waterMarkFontSize,waterMarkPosition);
        String [] urlParts = imgsToBeWaterMarked.getUrls()[0].split("\\.");
        FileItem fileItem = new FileItem(editImgBean.getTimeStamp()+"."+urlParts[urlParts.length-1],waterMarkedImgData);			
		req.setImage(fileItem);
		ItemUpdateResponse response = null;
		try {
			response = client.execute(req, dataSource.getSessionKey());
			int count = 1;
			while ((response.getBody() == null)||(response.getBody().contains("error"))) {
				response = client.execute(req, dataSource.getSessionKey());
				if (count <= 0) {
					break;
				}
				count--;
				Thread.sleep(2000);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		if (response.getBody() == null) {
		}
		else if (response.getBody().contains("error")) {
        	JSONObject json = (JSONObject) JSONSerializer.toJSON(response.getBody());
        	//System.out.println("------------------error----------------------");
        	msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
			//msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json.getJSONObject("error_response").getString("sub_msg"));
		}
		else {
			TaobaoClient client2=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemGetRequest req2=new ItemGetRequest();
			req2.setFields("item_img");
			req2.setNumIid(numiid);			
			try {
				String jsonTxt2 = "";
				ItemGetResponse response2 = client2.execute(req2, dataSource.getSessionKey());
				jsonTxt2 = response2.getBody();
				int count2 = 1;
				while ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
					response2 = client2.execute(req2, dataSource.getSessionKey());
					if (count2 <= 0) {
						break;
					}
					count2--;
					Thread.sleep(2000);				
				}
				JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt2);
				if ((jsonTxt2 == null)||(jsonTxt2.contains("error"))) {
					//msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", json2.getJSONObject("error_response").getString("sub_msg"));
		        	msg.setSummary("连接淘宝错误，请重试！");//this needs to be change, since successfully added  watermark, but failed to show the watermark, need to concatenate the messages,such as successfully added but failed to show ?????????????????
				}
				else {
					 
					JSONObject itemoj= json2.getJSONObject("item_get_response").getJSONObject("item");
					JSONArray itemImgArray = itemoj.getJSONObject("item_imgs").getJSONArray("item_img");
					for (int i=0;i<itemImgArray.size();i++) {
						JSONObject tmpobj = (JSONObject)itemImgArray.get(i);
						if (tmpobj.getInt("id") == 0) {
							itemData.getItemImgs()[0] = tmpobj.getString("url");
							itemData.getItemImgIDs()[0] = "0";//nessecery to add this line??????
							waterMarkedImgs[0] = tmpobj.getString("url");
							//break;
						}
						else {
							//////////////after update mainPic, all the other 4 additional image ids will be changed, so need to update them in local database
							//////////////////////////very important. tested in sandbox, but not in formal enviroment 
							//////////////////////////
							//////////////////////////
							itemData.getItemImgs()[i] = tmpobj.getString("url");
							itemData.getItemImgIDs()[i] = tmpobj.getString("id");
						}
					}

					//update local database
					//forSaleItemsData.getNumiidToItemPrice().put(String.valueOf(numiid), Double.valueOf(price));
					itemData.setItemPicUrl(updateImg[0]);
					serializeItemData(String.valueOf(numiid),itemData);
					booleanResult = true;
					
					//////////////after update mainPic, all the other 4 additional image ids will be changed, so need to update them in local database
				}
			}
			catch(Exception e2) {

			}
		}
		return booleanResult;
	}
	
	private boolean addWaterMarkForAdditionalPic(ImgsToBeWaterMarked imgsToBeWaterMarked, FacesMessage msg) {
		boolean booleanResult = false;
		String [] imgIDstmp = imgsToBeWaterMarked.getImgIDs();
		String [] urlstmp = imgsToBeWaterMarked.getUrls();
		for (int j=0;j<imgIDstmp.length;j++) {
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemImgDeleteRequest req=new ItemImgDeleteRequest();
			req.setId(Long.parseLong(imgIDstmp[j]));//get img id by index instead
			req.setNumIid(numiid);
			try {
				ItemImgDeleteResponse response = client.execute(req , dataSource.getSessionKey());
				String jsonTxt = response.getBody();
				System.out.println("delete response========="+jsonTxt);
				int count = 1;
				while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
					response = client.execute(req , dataSource.getSessionKey());
					jsonTxt = response.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
				if (jsonTxt == null) {
		        	msg.setSummary("连接淘宝错误，请重试！");
				}
				else if (jsonTxt.contains("error")) {
					JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		        	msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
				}
				//remove image id from the List itemData.getItemImgIDs()
				else {
					itemData.getItemImgs()[j+1] = null;
					itemData.getItemImgIDs()[j+1] = null;
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			//add new img
			ItemImgUploadRequest  req2=new ItemImgUploadRequest();
			req2.setNumIid(numiid);
			//req2.setPosition(0L);
			
			//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
			byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(editImgBean.remoteImgToByteArray(urlstmp[j]), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),sessionKey,waterMarkFontSize,waterMarkPosition);
			String [] parts = urlstmp[j].split("\\.");
		    FileItem fileItem = new FileItem(imgIDstmp[j]+"."+parts[parts.length-1],waterMarkedImgData);
			req2.setImage(fileItem);
			try {
				ItemImgUploadResponse response = client.execute(req2 , dataSource.getSessionKey());
				String jsonTxt = response.getBody();
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
				
				int count = 1;
				while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
					response = client.execute(req2 , dataSource.getSessionKey());
					jsonTxt = response.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
				if (jsonTxt == null) {
		        	msg.setSummary("连接淘宝错误，请重试！");
				}
				else if (jsonTxt.contains("error")) {
		        	msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
				}
				else{
					JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt);
					JSONObject jsonimgObj = json2.getJSONObject("item_img_upload_response").getJSONObject("item_img");				
					//add a new image id to itemData.getItemImgIDs()						
					setNewItemImgUrl(jsonimgObj.getString("url"));
					itemData.getItemImgs()[j+1] = jsonimgObj.getString("url");
					itemData.getItemImgIDs()[j+1] = jsonimgObj.getString("id");
					waterMarkedImgs[j] = getNewItemImgUrl();
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
					booleanResult = true;
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return booleanResult;
	}

	private boolean addWaterMarkForMainAndAdditionalPic(ImgsToBeWaterMarked imgsToBeWaterMarked, FacesMessage msg) {
		boolean booleanResult = false;
		//for mainPic, must put this after finishing additionalPic, otherwise there is a problem
		//this is because changing mainPic will also change the other 4 additional image ids
		//and the to be water marked image ids have be set before channing mainPic. need to make
		//some changes if put the following line after finishing additionalPic
		//addWaterMarkForMainPic(imgsToBeWaterMarked);
	
		///////////////////////////////////////////////////////////////////////
		String [] imgIDstmp = imgsToBeWaterMarked.getImgIDs();
		String [] urlstmp = imgsToBeWaterMarked.getUrls();
		for (int j=1;j<imgIDstmp.length;j++) {//for additionalPic
			TaobaoClient clientAdditional=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemImgDeleteRequest reqDelete=new ItemImgDeleteRequest();
			reqDelete.setId(Long.parseLong(imgIDstmp[j]));//get img id by index instead
			reqDelete.setNumIid(numiid);
			try {
				ItemImgDeleteResponse responseDelete = clientAdditional.execute(reqDelete, dataSource.getSessionKey());
				String jsonTxt = responseDelete.getBody();
				int count = 1;
				while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
					responseDelete = clientAdditional.execute(reqDelete, dataSource.getSessionKey());
					jsonTxt = responseDelete.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
				if (jsonTxt == null) {
		        	msg.setSummary("连接淘宝错误，请重试！");
				}
				else if (jsonTxt.contains("error")) {
					JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
		        	msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
				}
				//remove image id from the List itemData.getItemImgIDs()
				else {
					itemData.getItemImgs()[j] = null;
					itemData.getItemImgIDs()[j] = null;

					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			//add new img
			ItemImgUploadRequest reqUpload=new ItemImgUploadRequest();
			reqUpload.setNumIid(numiid);
			//req2.setPosition(0L);
			
			//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
			byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(editImgBean.remoteImgToByteArray(urlstmp[j]), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),sessionKey,waterMarkFontSize,waterMarkPosition);
			String [] parts = urlstmp[j].split("\\.");
		    FileItem fileItem = new FileItem(imgIDstmp[j]+"."+parts[parts.length-1],waterMarkedImgData);
			reqUpload.setImage(fileItem);
			try {
				ItemImgUploadResponse responseUpload = clientAdditional.execute(reqUpload, dataSource.getSessionKey());
				String jsonTxt = responseUpload.getBody();
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
				
				int count = 1;
				while ((jsonTxt == null)||(jsonTxt.contains("error"))) {
					responseUpload = clientAdditional.execute(reqUpload, dataSource.getSessionKey());
					jsonTxt = responseUpload.getBody();
					if (count <= 0) {
						break;
					}
					count--;
					Thread.sleep(2000);
				}
				if (jsonTxt == null) {
		        	msg.setSummary("连接淘宝错误，请重试！");
				}
				else if (jsonTxt.contains("error")) {
		        	msg.setSummary(json.getJSONObject("error_response").getString("sub_msg"));
				}
				else{
					JSONObject json2 = (JSONObject) JSONSerializer.toJSON(jsonTxt);
					JSONObject jsonimgObj = json2.getJSONObject("item_img_upload_response").getJSONObject("item_img");				
					//add a new image id to itemData.getItemImgIDs()						
					setNewItemImgUrl(jsonimgObj.getString("url"));
					itemData.getItemImgs()[j] = jsonimgObj.getString("url");
					itemData.getItemImgIDs()[j] = jsonimgObj.getString("id");
					waterMarkedImgs[j] = getNewItemImgUrl();
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
					booleanResult = true;
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}		
		addWaterMarkForMainPic(imgsToBeWaterMarked, msg);//for mainPic
		return booleanResult;
	}
	
	
	private boolean addWaterMarkForPropPic(ImgsToBeWaterMarked imgsToBeWaterMarked) {
		boolean booleanResult = false;
		String [] imgIDstmp = imgsToBeWaterMarked.getImgIDs();
		String [] urlstmp = imgsToBeWaterMarked.getUrls();
		String [] propertiestmp = imgsToBeWaterMarked.getProperties();
		for (int j=0;j<waterMarkedImgs.length;j++) {
			TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
			ItemPropimgUploadRequest req=new ItemPropimgUploadRequest();
			req.setNumIid(numiid);
			req.setProperties(propertiestmp[j]);
			//FileItem fileItem = new FileItem(imageFileName,imageFileContent);
	        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(editImgBean.remoteImgToByteArray(urlstmp[j]), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),sessionKey,waterMarkFontSize,waterMarkPosition);
			String [] parts = urlstmp[j].split("\\.");
	        FileItem fileItem = new FileItem(imgIDstmp[j]+"."+parts[parts.length-1],waterMarkedImgData);
			
			req.setImage(fileItem);
			String jsonTxt = "";
			try {
				///////////delete original prop_img
				TaobaoClient clientDelete=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
				ItemPropimgDeleteRequest reqDelete=new ItemPropimgDeleteRequest();
				reqDelete.setId(Long.valueOf(imgIDstmp[j]));
				reqDelete.setNumIid(numiid);
				try {
					ItemPropimgDeleteResponse responseDelete = clientDelete.execute(reqDelete , dataSource.getSessionKey());
					itemData.getPropToImg().remove(pidVid1);
					System.out.println(responseDelete.getBody());
				} catch (ApiException e) {
					e.printStackTrace();
				}
				
				ItemPropimgUploadResponse response = client.execute(req , dataSource.getSessionKey());
				jsonTxt = response.getBody();
				System.out.println(jsonTxt);
				if (jsonTxt.contains("error")) {
		        	JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);
				}
				else {
					//{"item_propimg_upload_response":{"prop_img":{"created":"2013-10-03 23:16:30","id":31010166085,"url":"http:\/\/img03.tbsandbox.com\/bao\/uploaded\/i3\/3600301501\/T2hkWyXghcXXXXXXXX_!!3600301501.jpg"}}}
					JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
					setUploadedPropImgUrl(json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url"));
					itemData.getPropToImg().put(propertiestmp[j], json.getJSONObject("item_propimg_upload_response").getJSONObject("prop_img").getString("url"));
					waterMarkedImgs[j] = getUploadedPropImgUrl();
					//update local database
					serializeItemData(String.valueOf(numiid),itemData);
					booleanResult = true;
				}
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}	
		return booleanResult;
	}
	
	private boolean addWaterMarkForAllPic(ImgsToBeWaterMarked imgsToBeWaterMarked) {
		boolean booleanResult = false;
		return booleanResult;
	}

	public int getWaterMarkFontSize() {
		return waterMarkFontSize;
	}

	public void setWaterMarkFontSize(int waterMarkFontSize) {
		this.waterMarkFontSize = waterMarkFontSize;
	}
	
	public void setWatermarkPosition() {
		String waterMarkPosition_passed = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("waterMarkPosition");
		this.waterMarkPosition = Integer.valueOf(waterMarkPosition_passed);
	}
	
	public int getWaterMarkPosition() {
		return this.waterMarkPosition;
	}
	
	public void previewWaterMark() {
		if (userInfo == null) {
			return;
		}
		
		if (userInfo.isPicWaterMarkCheckbox()) {
			setWaterMarkType("pic");
			setWaterMark(userInfo.getWaterMarkImg1());
		}
		else if (userInfo.isTextWaterMarkCheckbox()) {
			setWaterMarkType("text");
			setWaterMark(userInfo.getTextWaterMarkContent());
		}
	    File defaultFile = new File(catalinaBase, "webapps/SKUAssistant/images/watermarkpreview.jpg");
        byte[] waterMarkedImgData = ImageWaterMark.addWaterMark(ImageResizer.resize(imgToByteArray(defaultFile)), waterMark, waterMarkType, userInfo.getTextWaterMarkContentColor(),userNick,waterMarkFontSize,waterMarkPosition);
        previewWaterMarkBean.setWaterMarkedImgData(waterMarkedImgData);
	}

	public PreviewWaterMarkBean getPreviewWaterMarkBean() {
		return previewWaterMarkBean;
	}

	public void setPreviewWaterMarkBean(PreviewWaterMarkBean previewWaterMarkBean) {
		this.previewWaterMarkBean = previewWaterMarkBean;
	}

	public String getUpdateItemResult() {
		return updateItemResult;
	}

	public void setUpdateItemResult(String updateItemResult) {
		this.updateItemResult = updateItemResult;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public RecycledImgBean getRecycledImgBean() {
		return recycledImgBean;
	}

	public void setRecycledImgBean(RecycledImgBean recycledImgBean) {
		this.recycledImgBean = recycledImgBean;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
					
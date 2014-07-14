	//the following line make javascript work well in jsf and tells XML validator it is just a block of text and it should not worry about it. For example, less than symbol is not allowed in jsf
	//<![CDATA[
	function getScrollTop(){
    if(typeof pageYOffset!= 'undefined'){
        //most browsers except IE before #9
        return pageYOffset;
    }
    else{
        var B= document.body; //IE 'quirks'
        var D= document.documentElement; //IE with doctype
        D= (D.clientHeight)? D: B;
        return D.scrollTop;
    }
}
	           
	   	//window.onscroll = autoScroll();
		function autoScroll() {
			//var top = window.pageYOffset || document.documentElement.scrollTop
			var top = getScrollTop();
			//alert(top)
			var left = window.pageXOffset || document.documentElement.scrollLeft		
			//alert(top);
			//window.scrollBy(left,top);
			document.getElementById("cats_panel").style.left = (left+0)+"px";
			document.getElementById("cats_panel").style.top = (top+0)+"px";
		}
		function getElementPosition(elem) {
			var top = 0;
			var left = 0;
 			while( elem != null ) {
 				top += elem.offsetTop;
 				left += elem.offsetLeft;
 				elem = elem.offsetParent;
 			}
 			return {top: top,left: left};
		}
		function positionComponent(e) {
			var pos = getMousePosition(e);
			//alert(pos);
			document.getElementById("indicator").style.left = pos[0]+"px";
			document.getElementById("indicator").style.top = pos[1]+"px";			
		}
		
		function treeNodeLink(param) {
			//alert(param);
			retrieveForSaleItems(param);
			//alert("xxx")
		}
		function treeNodeLink2(param) {
			//alert(param);
			myJsFunction2(param);
		}		
		
		function addWaterMark(param) {
			//alert(param);
			addWaterMarkJsFunction(param);
		}
		function redirectToUrl() {
			//alert(document.getElementById("items_data:hidden_value").value);
			window.open(document.getElementById("items_data:hidden_value").value);
		}
		
		function editItem(item_price, hidden_price) {
			//alert(item_price);

			var elem_price = document.getElementById(item_price);
			var elem_hidden = document.getElementById(hidden_price);
			//alert(elem_price.id)
			//alert(elem_hidden.id)
			
			//alert(elem_price.innerHTML)
			elem_hidden.value=elem_price.innerHTML;
			elem_hidden.style.display = "block";
			//elem_price.innerHTML="";
			var top = 0;
			var left = 0;
 			while( elem_price != null ) {
 				top += elem_price.offsetTop;
 				left += elem_price.offsetLeft;
 				elem_price = elem_price.offsetParent;
 			}
			//alert(left)
			//alert(top)
			
			elem_hidden.style.left = left+"px";
			elem_hidden.style.top = top+"px";
			
			//move ajax indicator to correct position
			var indicatorElem = document.getElementById("indicator");
			indicatorElem.style.left = left+"px";
			indicatorElem.style.top = top+"px";
		}
		
		function hideEdit(item_price, hidden_price) {
			//alert(item_price)
			//alert(hidden_price)
			if(document.getElementById("updateItemForm:updateItemResult").innerHTML == "success") {
				document.getElementById(item_price).innerHTML = document.getElementById(hidden_price).value;
				document.getElementById(hidden_price).style.display = "none";
			}
			else {
				document.getElementById(hidden_price).style.display = "none";				
			}
		}

		var globalimgid;//imgid in taobao, not html id
		var globalsmallitemimg;//imgid in taobao, not html id. if the id is 0, it is the main img, which cannot be deleted and can only be replaced
		var globalselectedimgelemid;//only after clicking on a small item img, add,delete and replace icons will show
		var globalimgindex;//image index in the List itemData.getItemImgIDs()
		function showBigImg(smallimgID,row,pos,numiid,imgid) {//on click of small image event
			//alert(imgid)
			globalimgid = imgid;
			globalimgindex = pos;
			//alert(globalimgid)
			//alert(smallimgID)
			//alert(numiid+"-"+imgid)
			var bigItemImg = document.getElementById('items_data:datagrid:'+row+':item_img');
			globalselectedimgelemid = 'items_data:datagrid:'+row+':item_img';
			if (bigItemImg == null) {
				bigItemImg = document.getElementById('items_data:datagrid:'+row+':item_img2');
				if (bigItemImg == null) {
					bigItemImg = document.getElementById('items_data:datagrid:'+row+':item_img3');
					globalselectedimgelemid = 'items_data:datagrid:'+row+':item_img3';
				}
				else {
					globalselectedimgelemid = 'items_data:datagrid:'+row+':item_img2';
				}
				

			}
			var smallItemImg = document.getElementById(smallimgID);
			globalsmallitemimg = smallItemImg;
			//alert(bigItemImg+"----"+smallItemImg)
			bigItemImg.src = smallItemImg.src;
			//alert("xxxxxx")
			var imgposition = pos;
			
			setCurrentItemImgPositionAndID(imgposition,numiid,imgid,globalimgindex,smallimgID);			
		}
		
		var numiid;
		var itemImg;
		function itemImgOnMouseOver(base_id, item_imgid, numiid_passed) {//mouse on big item image
			numiid = numiid_passed;
			//alert(item_image)
			var elem_img = document.getElementById(base_id+":"+item_imgid);
			var itemImgtmp = elem_img;//save the element for later use to show the new image
			var icon_width = 45;
			var top = 0;
			var left = 0;
			var right = 0;
 			while( elem_img != null ) {
 				top += elem_img.offsetTop;
 				left += elem_img.offsetLeft;
 				elem_img = elem_img.offsetParent;
 			}
 			var elem_img_id = base_id+":"+item_imgid;
 			//alert(elem_img_id)
 			//alert(globalselectedimgelemid)
 			if (elem_img_id == globalselectedimgelemid) {
	 			if ((itemImgtmp.src.indexOf('/images/notexist.png') == -1) && (itemImgtmp.src.indexOf('/images/badimg.png') == -1)){	
			 		var elem_add = document.getElementById(base_id+":replaceItemImgid");
			 		elem_add.style.left = left + "px";
			 		elem_add.style.top = top+"px";
			 		elem_add.style.display = "block";
			 		if (globalimgid != '0') {//item main img cannot be deleted
				 		var elem_delete = document.getElementById(base_id+":deleteItemImgid");
				 		elem_delete.style.left = left + icon_width + "px";
				 		elem_delete.style.top = top+"px";
				 		elem_delete.style.display = "block"; 				 						 		
			 		}
			 		
			 		var elem_delete = document.getElementById(base_id+":editItemImgid");
			 		elem_delete.style.left = left + icon_width*2 + "px";
			 		elem_delete.style.top = top+"px";
			 		elem_delete.style.display = "block"; 
	 			}
	 			else if (itemImgtmp.src.indexOf('/images/notexist.png') != -1) {
	 	 			var elem_edit = document.getElementById(base_id+":addItemImgid");
	 	 			elem_edit.style.left = left + "px";
	 	 			elem_edit.style.top = top+"px";
	 	 			elem_edit.style.display = "block"; 
	 	 			/*
			 		var elem_delete = document.getElementById(base_id+":editItemImgid");
			 		elem_delete.style.left = left + icon_width*2 + "px";
			 		elem_delete.style.top = top+"px";
			 		elem_delete.style.display = "block";
			 		*/
	 			}
	 			else if (itemImgtmp.src.indexOf('/images/badimg.png') != -1) {
	 	 			var elem_edit = document.getElementById(base_id+":replaceItemImgid");
	 	 			elem_edit.style.left = left + "px";
	 	 			elem_edit.style.top = top+"px";
	 	 			elem_edit.style.display = "block";
	 	 			if (globalimgid != '0') {//item main img cannot be deleted
				 		var elem_delete = document.getElementById(base_id+":deleteItemImgid");
				 		elem_delete.style.left = left + icon_width + "px";
				 		elem_delete.style.top = top+"px";
				 		elem_delete.style.display = "block"; 	
	 	 			}
	 	 			
	 	 			/*
			 		var elem_delete = document.getElementById(base_id+":editItemImgid");
			 		elem_delete.style.left = left + icon_width*2 + "px";
			 		elem_delete.style.top = top+"px";
			 		elem_delete.style.display = "block";
			 		*/
	 			}
 			}
		}
		
		var pidVid1;
		function itemPropImgOnMouseOver(item_image, pid1_passed, vid1_passed, editOrReplaceIconImgID) {
			//alert(editOrReplaceIconImgID);
			//alert(item_image)
			pidVid1 = pid1_passed+":"+vid1_passed;
			//alert(item_image)
			var elem_img = document.getElementById(item_image);
			var top = 0;
			var left = 0;
 			while( elem_img != null ) {
 				top += elem_img.offsetTop;
 				left += elem_img.offsetLeft;
 				elem_img = elem_img.offsetParent;
 			}
 			//var elem_edit = document.getElementById("replaceItemPropImgid");
 			if (editOrReplaceIconImgID.indexOf('replaceItemPropImgid') != -1) {
	 			var elem_edit = document.getElementById(editOrReplaceIconImgID);
	 			elem_edit.style.left = left+"px";
	 			elem_edit.style.top = top+"px";
	 			elem_edit.style.display = "block";
 			}
 			if (editOrReplaceIconImgID.indexOf('editItemPropImgid') != -1) {
	 			var elem_edit = document.getElementById(editOrReplaceIconImgID);
	 			elem_edit.style.left = left+40+"px";
	 			elem_edit.style.top = top+"px";
	 			elem_edit.style.display = "block";
 			} 			
		}

		var globalposition;
		var globalwidth;
		function replaceItemImage(itemImgID,itemImgPanelContentID) {//replace current item img,click on replace icon
			itemImg = document.getElementById(itemImgID);//make the item img saved
			if (itemImg == null) {
				itemImg = document.getElementById(itemImgID.replace("item_img","item_img2"));
				if (itemImg == null) {
					itemImg = document.getElementById(itemImgID.replace("item_img","item_img3"));
				}
			}
			var panelContentElem = document.getElementById(itemImgPanelContentID);
			globalposition = getElementPosition(panelContentElem);
			globalwidth = panelContentElem.offsetWidth;

			if (globalimgid != '0') {
			callReplaceItemImg(numiid, globalimgid, globalimgindex, globalsmallitemimg.id);
			//alert(numiid)
			//alert(globalimgid)
			document.getElementById("hidden_itemimg_uploader_form:hidden_fileuploader_input").click();//use double click instead because click on fileUpload is not working for IE
			}
			else {
				callReplaceMainItemImg(numiid, globalimgid, globalimgindex, globalsmallitemimg.id);
				document.getElementById("hidden_main_itemimg_uploader_form:hidden_fileuploader_input").click();
			}
			
		}
		
		function replaceItemPropImgage() {
			//alert(pidVid1)
			setPidVid1Function(pidVid1);
			document.getElementById("one_item_data:hidden_prop_img_fileuploader_input").click();
		}
		
		function deleteItemImage(itemImgID,itemImgPanelContentID) {//click on delete icon
			//alert(numiid+"="+globalimgid)
			//alert(itemImgID) //items_data:datagrid:#{row}:item_img
			itemImg = document.getElementById(itemImgID);//make the item img saved
			if (itemImg == null) {
				itemImg = document.getElementById(itemImgID.replace("item_img","item_img2"));
			}
			
			var panelContentElem = document.getElementById(itemImgPanelContentID);
			var position = getElementPosition(panelContentElem);
			var width = panelContentElem.offsetWidth;
			var indicatorElem = document.getElementById("indicator");
			indicatorElem.style.left = position.left+width/2-33+"px";//33*2=66 is the ajax indicator image width
			indicatorElem.style.top = position.top+70+"px";
			indicatorElem.style.display = "block";	
			//alert(globalimgid)
			callDeleteItemImg(numiid,globalimgid,globalimgindex, globalsmallitemimg.id);
			//alert(globalimgid)
			
		}
		function afterItemImgDelete() {
			//alert("xxxxxx")
			/*
			var status = document.getElementById('newItemImg:status').innerHTML;
			if (status != "nochange") {
				itemImg.src = "../images/notexist.png";
				globalsmallitemimg.src = "../images/notexist.png";
				var indicatorElem = document.getElementById("indicator");
				indicatorElem.style.display = "none";
			}
			*/
			var parts = document.getElementById('newItemImg:smallImgId').innerHTML.split(":");			
			var bigItemImg = document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+':item_img');
			var status = document.getElementById('newItemImg:status').innerHTML;
			var smallImgId = document.getElementById('newItemImg:smallImgId').innerHTML;
			if (status == "nochange") {
			}
			else if (status == "nodisplay") {
				//globalsmallitemimg.src = "../images/notexist.png";
				if (bigItemImg == null) {
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img2").src = "../images/notexist.png";
				}
				else {
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img").src = "../images/notexist.png";
				}
				document.getElementById(smallImgId).src = "../images/notexist.png";
			}
			hideAjaxIndicator();			
		}
		
		function addItemImage(itemImgID,itemImgPanelContentID) {
			itemImg = document.getElementById(itemImgID);//make the item img saved
			if (itemImg == null) {
				itemImg = document.getElementById(itemImgID.replace("item_img","item_img2"));
			}
			var panelContentElem = document.getElementById(itemImgPanelContentID);
			globalposition = getElementPosition(panelContentElem);
			globalwidth = panelContentElem.offsetWidth;

			//alert(globalimgid)
			callAddItemImg(numiid,globalimgid,globalimgindex, globalsmallitemimg.id);
			//alert("xxxxxxxx")
			document.getElementById("hidden_itemimg_uploader_add_form:hidden_fileuploader_input").click();
		}
		function showAjaxIndicatorEvent(e) {
			var pos = getMousePosition(e);
			//alert(pos)
			var indicatorElem = document.getElementById("indicator");
			indicatorElem.style.left = pos[0]+"px";
			indicatorElem.style.top = pos[1]+"px";
			indicatorElem.style.display = "block";

		}
		function showAjaxIndicator() {
			//alert("xxxxxxx")
			//alert(globalposition)
			var indicatorElem = document.getElementById("indicator");
			//alert(indicatorElem)
			indicatorElem.style.left = globalposition.left+globalwidth/2-33+"px";
			indicatorElem.style.top = globalposition.top+70+"px";
			indicatorElem.style.display = "block";	
			//alert("xxxxxx")
		}
		
		function showAjaxIndicator() {
			//alert("xxxxxxx")
			//alert(globalposition)
			var indicatorElem = document.getElementById("indicator");
			//alert(indicatorElem)
			indicatorElem.style.left = globalposition.left+globalwidth/2-33+"px";
			indicatorElem.style.top = globalposition.top+70+"px";
			indicatorElem.style.display = "block";	
			//alert("xxxxxx")
		}
		function showIcon(elemID) {
			if (globalimgid == '0') {//mouse on the main item image,do not show delete icon,only show replace icon
				if (elemID.indexOf('delete') != -1) {
					document.getElementById(elemID).style.display = "none";
				}
				else {
					document.getElementById(elemID).style.display = "block";
				}
			}
			else {
				document.getElementById(elemID).style.display = "block";
			}
		}
		function hideIcon(elemID) {
			//alert("xxx")
			document.getElementById(elemID).style.display = "none";
		}		
		function changeCursor(elemID) {
			document.getElementById(elemID).style.cursor = "pointer";
		}
		
		function makevisible(cur,which){
	        if (which==0){
				cur.style.opacity = 1;			
	            cur.filters.alpha.opacity=100;/* For IE */
			}
	        else{
				cur.style.opacity = 0.4;
	            cur.filters.alpha.opacity=40;/* For IE */
			}
	    }
		
		function showSameSizeSku(e){
			var pos = getMousePosition(e);
			//alert(pos);
			//var obj = document.getElementById('newSizeSku');
			document.getElementById('sameSizeSku').style.left=pos[0]+'px'; 
			//alert(document.getElementById('newSizeSku').style.left);
			//alert(obj.style.left);
			
			document.getElementById('sameSizeSku').style.top=pos[1]+'px';
			//alert(document.getElementById('newSizeSku').style.top);
			//alert(obj.style.top);
			document.getElementById('sameSizeSku').style.display = "block";
		}
		function showNewSizeSku(e){
			var pos = getMousePosition(e);
			//alert(pos);
			//var obj = document.getElementById('newSizeSku');
			document.getElementById('newSizeSku').style.left=pos[0]+'px'; 
			//alert(document.getElementById('newSizeSku').style.left);
			//alert(obj.style.left);
			
			document.getElementById('newSizeSku').style.top=pos[1]+'px';
			//alert(document.getElementById('newSizeSku').style.top);
			//alert(obj.style.top);
			document.getElementById('newSizeSku').style.display = "block";
		}
		function hideSameSizeSku() {
			document.getElementById('sameSizeSku').style.display = "none";
		}
		function hideNewSizeSku() {
			document.getElementById('newSizeSku').style.display = "none";
		}
		function getMousePosition(e) {
			posx = 0;posy = 0; 
			if (!e) var e = window.event; 
			if (e.pageX || e.pageY){ 
			posx = e.pageX;posy = e.pageY; 
			} 
			else if (e.clientX || e.clientY){ 
			posx = e.clientX;posy = e.clientY; 
			} 
		    var pos=Array(posx,posy);
		    //alert(pos);
			return pos;
		}
		
	    function scrollPage() {
	    	if (navigator.userAgent.indexOf('Chrome') != -1){
	    		window.location.href='#anchorPanel';
	    	}
	    	else {
	        	//window.location.hash='anchorPanel'; //not working in IE or FireFox
	        	
	    		//var pos = getElementPosition(document.getElementById('anchorPanel'));
	        	//alert(pos.top+"==="+pos.left)
	        	//window.scrollTo(-pos.top, 0);
	    		
	        	document.getElementById('anchorPanel').scrollIntoView();

	    	}
	    }
	    
		
		function showNewItemImg() {//after replacing/adding/deleting an item img
			//alert(itemImg)
			//document.getElementById('newItemImg').style.left = itemImg.style.left+"px";
			//document.getElementById('newItemImg').style.top = itemImg.style.top+"px";
			//alert(document.getElementById('newItemImg:img'))
			var newImgSrc = document.getElementById('newItemImg:img').src;
			//alert(newImgSrc)
			//alert(document.getElementById('newItemImg:img')+ "  " + newImgSrc)
			//itemImg.src = newImgSrc; //do not use this line to get the itemImg, because
			//when user select an image, another imgItem may be clicked, so the img will be set to a wrong position
			//use the following lines to get the itemImg id from the small image id
			//alert(itemImg.id)  items_data:datagrid:2:item_img
			//alert(globalsmallitemimg.id)  items_data:datagrid:2:all_item_imgs:1:item_img_small
			
			//var parts = globalsmallitemimg.id.split(":");
			//alert(document.getElementById('newItemImg:smallImgId').innerHTML)
			var parts = document.getElementById('newItemImg:smallImgId').innerHTML.split(":");
			//alert(document.getElementById('newItemImg:smallImgId').innerHTML)
			//alert(parts[0]+":"+parts[1]+":"+parts[2]+":item_img")
			
			var bigItemImg = document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+':item_img');
			var status = document.getElementById('newItemImg:status').innerHTML;
			var smallImgId = document.getElementById('newItemImg:smallImgId').innerHTML;
			
			if (status == "nochange") {
			}
			else if (status == "nodisplay") {
				//globalsmallitemimg.src = "../images/notexist.png";
				if (bigItemImg == null) {
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img2").src = "../images/notexist.png";
				}
				else {
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img").src = "../images/notexist.png";
				}
				document.getElementById(smallImgId).src = "../images/notexist.png";
			}
			else if (status == "display") {
				if (bigItemImg == null) {
					//update big image
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img2").src = newImgSrc;
				}
				else {
					document.getElementById(parts[0]+":"+parts[1]+":"+parts[2]+":item_img").src = newImgSrc;
				}
				
				//show item small img
				//alert(smallImgId)
				document.getElementById(smallImgId).src = newImgSrc;
			}
			hideAjaxIndicator();
		}

		function showNewItemImgID() {//after replacing/adding an item img
			//alert(itemImg)
			//document.getElementById('newItemImg').style.left = itemImg.style.left+"px";
			//document.getElementById('newItemImg').style.top = itemImg.style.top+"px";
			var newImgIDValue = document.getElementById('newItemImgID:imgID').value;
			//alert(newImgIDValue)
			//itemImg.src = newImgSrc; //do not use this line to get the itemImg, because
			//when user select an image, another imgItem may be clicked, so the img will be set to a wrong position
			//use the following lines to get the itemImg id from the small image id
			//alert(itemImg.id)  items_data:datagrid:2:item_img
			//alert(globalsmallitemimg.id)  items_data:datagrid:2:all_item_imgs:1:item_img_small
			//alert(globalsmallitemimg.value)
			globalsmallitemimg.value = newImgIDValue;
			//alert(globalsmallitemimg.value)
			
			//hide ajax indicator
			var indicatorElem = document.getElementById("indicator");
			indicatorElem.style.display = "none";
		}
		
		function hideItem() {
			document.getElementById('one_item_data').style.display = "none";
		}
		function showItem() {
			document.getElementById('one_item_data').style.display = "block";
		}
		

		function recommendItemCaller(flag,numiid) {
			//alert(numiid);
			recommendItem(flag,numiid);
		}
		
		function afterRecommendOperation(elemid,args) {
			if (args.failure == "failure") {
				//alert(args.failure)
			}
			else {
			//alert(elemid)
			var recomelem = document.getElementById(elemid);
			var elem_class = recomelem.getAttribute("class");
			var elem_title = recomelem.getAttribute("title");
			//alert("unchanged class--"+elem_class)
			//alert(elemid)
			//alert(elem_title)
			//alert(recomelem.innerHTML)
			if (elem_class.indexOf("notrecommend") == -1) {
				var changed_class = elem_class.replace("recommend","notrecommend");
				//alert(changed_class)
				recomelem.setAttribute("class",changed_class);
				//recomelem.setAttribute("title","点击推荐");
				recomelem.setAttribute("title","\u70B9\u51FB\u63A8\u8350");
				//alert("changed_class---"+changed_class)
				//recomelem.innerHTML = '未推荐';
				//use this web site to convert Chinese to unicode: choose Hexadecimal NCRsSelect http://rishida.net/tools/conversion/
				recomelem.innerHTML = '&#x672A;&#x63A8;&#x8350;';//use unicode to avoid unreadable character
			}
			else {
				var changed_class = elem_class.replace("notrecommend","recommend");
				recomelem.setAttribute("class",changed_class);
				//alert("changed_class---"+changed_class)
				//recomelem.setAttribute("title","点击取消推荐");
				//use this web site to convert Chinese to unicode: choose JavaScript escapes http://rishida.net/tools/conversion/
				recomelem.setAttribute("title","\u70B9\u51FB\u53D6\u6D88\u63A8\u8350");
				//recomelem.innerHTML = '推荐中';	
				recomelem.innerHTML = '&#x63A8;&#x8350;&#x4E2D;';
			}
			}
		}

		function afterChangeApproveStatus(elemid,args) {
			//alert(args)
			//alert(args.failure)
			if (args.failure == "failure") {
				//alert(args.failure)
			}
			else {
			var recomelem = document.getElementById(elemid);		
			var elem_class = recomelem.getAttribute("class");
			if (elem_class.indexOf("instock") == -1) {
				//alert(elem_class)
				var changed_class = elem_class.replace("onsale","instock");
				//alert(changed_class)
				recomelem.setAttribute("class",changed_class);
				//recomelem.setAttribute("title","点击下架");
				recomelem.setAttribute("title","\u70B9\u51FB\u4E0A\u67B6");
				//alert(changed_class)
				//recomelem.innerHTML = '库存';
				//use this web site to convert Chinese to unicode: choose Hexadecimal NCRsSelect http://rishida.net/tools/conversion/
				recomelem.innerHTML = '&#x5E93;&#x5B58;';//use unicode to avoid unreadable character
			}
			else {
				var changed_class = elem_class.replace("instock","onsale");
				recomelem.setAttribute("class",changed_class);
				recomelem.setAttribute("title","\u70B9\u51FB\u4E0B\u67B6");
				//recomelem.setAttribute("title","点击上架");
				//recomelem.innerHTML = '在售';	
				recomelem.innerHTML = '&#x5728;&#x552E;';
			}
			}
			
		}		
		function setStockChangeValue(stockChangeInputID) {
			//alert(document.getElementById(stockChangeInputID).value)
			callSetNumOfstockIncrease(document.getElementById(stockChangeInputID).value);
		}
		
		function setSkuStockChangeValue(skuStockChangeInputID) {
			//alert(document.getElementById(skuStockChangeInputID).value)
			callSetNumOfSkuStockChange(document.getElementById(skuStockChangeInputID).value);
		}		
		
		var inputElem;
		function setSkuBatchUpdate(nthItem,numOfPidVid2,skuChange) {
			//alert(nthItem+"====="+numOfPidVid2)
			var skuId2Num = "";
			//one_item_data:onenumiidgrid:6:pidVid2:5:numOfSkuStockChange
			for (var i=0;i < eval(numOfPidVid2 - 1);i++) {
				inputElem = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":pidVid2:"+i+":numOfSkuStockChange");
				skuId2Num += inputElem.title;
				skuId2Num += ":";
				if (skuChange == "plus") {
					skuId2Num += inputElem.value;
				}
				else {
					skuId2Num += "-" + inputElem.value;
				}
				skuId2Num += ";";
			}
			//alert(skuId2Num)
			callSetSkuNumBatchUpdate(skuId2Num);
		}
		
		function setAddNewPidVid1Sku(nthItem) {
			//alert(nthItem)
			//one_item_data:onenumiidgrid:8:custom_new_size
			var price = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":add_new_price").value;
			var quantity = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":add_new_quantity").value;
			var pidVid1 = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":add_new_pidVid1_input").value;
			var pidVid1Alias = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":custom_new_pidVid1").value;
			//alert(pidVid1)
			if (document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":add_new_pidVid2_input") != null) {
				var pidVid2 = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":add_new_pidVid2_input").value;
				var pidVid1Alia2 = document.getElementById("one_item_data:onenumiidgrid:"+nthItem+":custom_new_pidVid2").value;
				callSetAddNewPidVid1SkuParams(price,quantity,pidVid1,pidVid1Alias,pidVid2,pidVid1Alia2);
			}
			else {
				callSetAddNewPidVid1SkuParams(price,quantity,pidVid1,pidVid1Alias,null,null);
			}
		}
		
		function retrieveRecommendedItems() {
			callRetrieveRecommendedItems();
		}
		
		function hideNewSizeSku() {
			document.getElementById('newSizeSku').style.display = "none";
		}
		
		var globalpanelElementID;
		function setPropImgPanelElm(panelElementID) {//when click on "添加...", save the panel element
			globalpanelElementID = panelElementID;
			//alert(globalpanelElementID)
		}
		
		
		function showAjaxIndicator2() {
			var panelContentElem = document.getElementById(globalpanelElementID);
			var position = getElementPosition(panelContentElem);
			var width = panelContentElem.offsetWidth;
			var indicatorElem = document.getElementById("indicator");
			indicatorElem.style.left = position.left+width/2-33+"px";//33*2=66 is the ajax indicator image width
			indicatorElem.style.top = position.top+90+"px";
			indicatorElem.style.display = "block";	
		}
		
		function hideAjaxIndicator() {
			document.getElementById("indicator").style.display = "none";
		}
		
		function hideEditPen(elemID) {
			//alert(elemID)
			document.getElementById(elemID).style.display = "none";
		}
		
		function hideCatsPanel(elemID) {
			//alert(elemID)
			document.getElementById(elemID).style.display = "none";
		}
		function showCatsPanel(elemID) {
			document.getElementById(elemID).style.display = "block";
		}
		
		function recomChange(elemID) {
			//alert("recomChange--"+elemID)
			//alert("recomChange--------------"+document.getElementById(elemID).getAttribute("class"))
			recomChangeCalled(document.getElementById(elemID).getAttribute("class"));
			//alert("recomChange--"+elemID)
		}
		
		function approveStatusChange(elemID) {
			//alert("approveStatusChange"+elemID)
			approveStatusChangeCalled(document.getElementById(elemID).getAttribute("class"));
		}
		
		function uploadImgForPublishItem(elemID) {
			document.getElementById(elemID).click();
			//one_item_data:onenumiidgrid:19:fileuploadlistener_input
		}
		
		var globalWaterMarkImgID;
		function configWaterMark(elemID) {
			//alert(elemID)
			globalWaterMarkImgID = elemID.split(":")[1];
			//alert(globalWaterMarkImgID)
			var waterMarkImgElmPos = getElementPosition(document.getElementById(elemID));
			//alert(waterMarkImgElmPos.left)
			document.getElementById('waterMarkForm:replaceWaterMark').style.left = waterMarkImgElmPos.left-200+"px";
			document.getElementById('waterMarkForm:replaceWaterMark').style.top = waterMarkImgElmPos.top-100+"px";
			document.getElementById('waterMarkForm:replaceWaterMark').style.display = 'block';
		}
		
		function replaceWaterMarkImg() {
			callToBeChangedWaterMarkImg(globalWaterMarkImgID);
			document.getElementById('waterMarkFileUploadForm:waterMark_fileuploader_input').click();
		}
		
		var innerHTMLContent;
		function saveImgStart() {
			innerHTMLContent = document.getElementById('editImgForm:imgelement').innerHTML;
			//document.getElementById('editImgForm:imgelement').innerHTML='正在保存图片。。。';
			document.getElementById('editImgForm:imgelement').innerHTML='&#27491;&#22312;&#20445;&#23384;&#22270;&#29255;&#12290;&#12290;&#12290;';
		}
		function saveImgComplete() {
			document.getElementById('editImgForm:imgelement').innerHTML=innerHTMLContent;
		}
		
		function editPropImg(propImgUrl,pidVid1) {
			//alert(propImgUrl)
			//alert(pidVid1)
			editPropImgJsFunction(propImgUrl,pidVid1);
		}
		function editImgJS() {
			//alert("xxx")
			callEditImg();
		}
		
		function resetBackGroundColor() {
			document.getElementById('pos1').style.background="#D5D5D5";
			document.getElementById('pos2').style.background="#D5D5D5";
			document.getElementById('pos3').style.background="#D5D5D5";
			document.getElementById('pos4').style.background="#D5D5D5";
			document.getElementById('pos5').style.background="#D5D5D5";
			document.getElementById('pos6').style.background="#D5D5D5";
			document.getElementById('pos7').style.background="#D5D5D5";
			document.getElementById('pos8').style.background="#D5D5D5";
			document.getElementById('pos9').style.background="#D5D5D5";			
		}
		function watermarkPosition(position) {
			resetBackGroundColor();
			callWatermarkPosition(position);
			
		}
		function setSelectedWaterMark() {//when clicking on 设置水印, set the background of selected watermark position
			resetBackGroundColor();
			var selectedPos = 'pos'+document.getElementById('waterMarkForm:waterMarkPosition').innerHTML;
			//alert(selectedPos)
			document.getElementById(selectedPos).style.background="blue";
			
		}
		/*
		function showItemDetailJS(numiid) {
			showItemDetailCalled(numiid);
		}
		*/
	//]]>
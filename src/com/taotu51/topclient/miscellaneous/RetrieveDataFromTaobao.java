package com.taotu51.topclient.miscellaneous;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.VasSubscribeGetRequest;
import com.taobao.api.response.VasSubscribeGetResponse;

public class RetrieveDataFromTaobao {
	public static VasSubscribeGetResponse vasSubscribeGet(DataSource dataSource) {
		TaobaoClient client=new DefaultTaobaoClient(dataSource.getUrl(), dataSource.getAppkey(), dataSource.getSecret());
		VasSubscribeGetRequest request = new VasSubscribeGetRequest();
		request.setNick(dataSource.getNick());
		request.setArticleCode("afads");//get articleCode from my.open.taobao.com
		VasSubscribeGetResponse response = null;
		try {
			response = client.execute(request);
			/*
			if (response.isSuccess()) {
				return response;
			} else {
			}
			*/
		} catch (ApiException e) {
		}
		return response;
	}
}

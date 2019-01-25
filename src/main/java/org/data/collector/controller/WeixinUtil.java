package org.data.collector.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WeixinUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeixinUtil.class);

	@Autowired
	RestTemplate rest;

	@Autowired
	Cache cache;
	String accessToken = null;

	String accessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=8888888882729708&corpsecret=cD3t48UL5f2Udgda4344JMKui6fSwo8zf5Zm_lhFvoJ0";
	String publishUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

	String messageTemplate0;// = "{   \"touser\" : \"aaa\",   \"toparty\" : \"mytest|PartyID2\",   \"totag\" : \"TagID1 | TagID2\",   \"msgtype\" : \"text\",   \"agentid\" : 1000002,   \"text\" : {       \"content\" : \"";
	String messageTemplate1 = "\"   },   \"safe\":0}";

	String messageTemplate0Foramt = "{   \"touser\" : \"%s\",   \"toparty\" : \"%s\",   \"totag\" : \"%s\",   \"msgtype\" : \"text\",   \"agentid\" : 1000002,   \"text\" : {       \"content\" : \"";

	public void sendMessage(String message) {
		sendMessage("default user","","",message);
	}
	
	public void sendMessage(String touser, String toparty, String totag, String message) {
		messageTemplate0=String.format(messageTemplate0Foramt, touser,toparty,totag);
//		
		int i = 3;
		while (i-- > 0) {

			int tokenRetry = 3;
			while (accessToken == null && tokenRetry-- > 0)
				flushToken();

			if (accessToken == null) {
				LOGGER.info("=== get token failed after retry 3 times.");
				break;
			}

			StringBuilder messageJson = new StringBuilder();
			messageJson.append(messageTemplate0);
			messageJson.append(message);
			messageJson.append(messageTemplate1);

			StringBuilder url = new StringBuilder();
			url.append(publishUrl);
			url.append(accessToken);

			LOGGER.info(url.toString());
			LOGGER.info(messageJson.toString());
			ResponseEntity<ResponseJson> entity = rest.postForEntity(url.toString(), messageJson.toString(), ResponseJson.class);
//			LOGGER.info("return--" + entity.getBody());
			ResponseJson resToken = entity.getBody();
			if (resToken.getErrcode() == 0) {
				LOGGER.info("Send suc!");
				break;
			} else {
				if (resToken.getErrcode() == 42001) //token is not null but it has expired.
					flushToken();
			}
		}
	}

	public boolean flushToken() {
		ResponseEntity<ResponseJson> entity = rest.getForEntity(accessTokenUrl, ResponseJson.class);
		ResponseJson resToken = entity.getBody();
		LOGGER.info("=== Get token result:" + resToken);
		if (resToken.getErrcode() == 0) {			
			accessToken = resToken.getAccess_token();
			return accessToken != null;
		} else {
			return false;
		}
	}

	static class ResponseJson {

		private int errcode;
		private String errmsg;
		private String access_token;
		private int expires_in;
		private String invaliduser;

		public void setErrcode(int errcode) {
			this.errcode = errcode;
		}

		public int getErrcode() {
			return errcode;
		}

		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}

		public String getErrmsg() {
			return errmsg;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setExpires_in(int expires_in) {
			this.expires_in = expires_in;
		}

		public int getExpires_in() {
			return expires_in;
		}

		public void setInvaliduser(String invaliduser) {
			this.invaliduser = invaliduser;
		}

		public String getInvaliduser() {
			return invaliduser;
		}

	}
}

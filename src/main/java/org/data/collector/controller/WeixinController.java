package org.data.collector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeixinController {

	private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);
	@Autowired
	WeixinUtil weixin;

	@RequestMapping(value = "/weixin/{custContent}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String send(@PathVariable("custContent") String desc,@RequestParam(value="touser",defaultValue="") String touser,@RequestParam(value="toparty",defaultValue="") String toparty,@RequestParam(value="totag",defaultValue="") String totag, @RequestBody String jsonStr) {

//		HttpHeaders responseHeaders = new HttpHeaders();
		logger.info("name=" + desc);
		logger.info("touser=" + touser);
		logger.info("Content=" + jsonStr);
		if (jsonStr != null && jsonStr.length() > 0)
			weixin.sendMessage(touser,toparty,totag, desc + ":" + jsonStr.replace("\"", "\\\""));
		return "name=" + desc + "\r\n" + jsonStr;
	}


	@RequestMapping(value = "/weixin/test/{indexName}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String test(@PathVariable("indexName") String name, @RequestBody String jsonStr) {

		HttpHeaders responseHeaders = new HttpHeaders();
		
		logger.info("name="+name);
		logger.info("Content="+jsonStr);
		weixin.sendMessage("","","","name="+name+"\r\n"+jsonStr);
		return "name="+name+"\r\n"+jsonStr;
	}

}

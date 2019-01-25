package org.data.collector.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;

import org.data.collector.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CollectorController {
	//bin/kafka-topics.sh --list --zookeeper ip:2181
	//bin/kafka-topics.sh --create --zookeeper  ip:2181 --partitions 1 --replication-factor 1 --topic data-platform-topic
	String defaultTopic="data-platform-topic";
	private static final Logger logger = LoggerFactory.getLogger(CollectorController.class);

	@Autowired
	private KafkaTemplate kafkaTemplate;

	Map<String, String> routeMapping = new HashMap<String, String>();
	
	@Autowired
	PropertiesConfig propConfig;
	
	@PostConstruct
	void init(){
		routeMapping=propConfig.getRouteMapping();
		logger.info(routeMapping.toString());
	}
	
	@RequestMapping(value = "/api/v1/collector/{productName}/{platformName}/{logType}", headers = "Content-Type=application/json", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> receive(@PathVariable("productName") String productName,@PathVariable("platformName") String platformName,@PathVariable("logType") String logType,@PathParam("withId") boolean withId, @RequestBody String jsonStr) {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		String urlkey=productName+platformName+logType;
		String topic=routeMapping.get(urlkey);
		if(topic==null){
			return new ResponseEntity<String>("invalid url:"+urlkey, responseHeaders, HttpStatus.BAD_REQUEST );
		}
			
		if(withId){
			topic+="-with-id";
		}
		logger.info("Send to topic:"+ topic);
//		responseHeaders.set("MyResponseHeader", "MyValue");
		if (isValid(jsonStr)) {
			kafkaTemplate.send( topic, jsonStr);
			return new ResponseEntity<String>("suc", responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Not valid json!", responseHeaders, HttpStatus.BAD_REQUEST );
		}

	}

	private boolean isValid(String jsonStr) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonStr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

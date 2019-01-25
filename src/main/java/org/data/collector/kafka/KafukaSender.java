package org.data.collector.kafka;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class KafukaSender {
	public static Logger logger = LoggerFactory.getLogger(KafukaSender.class);

	@Autowired
	private KafkaTemplate kafkaTemplate;
	@Autowired
	private KafkaTemplate<String, String> template;
	private final CountDownLatch latch = new CountDownLatch(3);

	public ListenableFuture<SendResult<String, String>>  sendMessage(String s) {
		logger.info("Send:"+s);
		return this.template.sendDefault(s);
	}
}
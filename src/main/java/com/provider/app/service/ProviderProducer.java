package com.provider.app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigurationProperties(prefix = "producer-config")
@Service
public class ProviderProducer {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ProviderProducer.class);
	private String topicProviderToGateway;// "ProviderToGatewayTopic";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		logger.info(String.format("#### -> Provider Producer (Kafkfa), message -> %s", message));
		this.kafkaTemplate.send(topicProviderToGateway, message);
		logger.info("Message sent");
	}

	public String getTopicProviderToGateway() {
		return topicProviderToGateway;
	}

	public void setTopicProviderToGateway(String topicProviderToGateway) {
		this.topicProviderToGateway = topicProviderToGateway;
	}
	
	

}

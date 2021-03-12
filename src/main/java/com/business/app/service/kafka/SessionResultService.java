package com.business.app.service.kafka;

import com.business.app.service.pojo.SessionResultPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionResultService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.topic.name}")
	private String topicName;

	public void sendDataToKafkaTopic(SessionResultPojo sessionResultPojo) {
		Map<String, Object> headers = new HashMap<>();
		headers.put(KafkaHeaders.TOPIC, topicName);
		kafkaTemplate.send(new GenericMessage<SessionResultPojo>(sessionResultPojo, headers));
	}
}

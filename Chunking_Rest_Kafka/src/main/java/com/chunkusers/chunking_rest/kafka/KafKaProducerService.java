package com.chunkusers.chunking_rest.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class KafKaProducerService
{
    private static final Logger logger = LoggerFactory.getLogger(KafKaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ByteBuffer message)
    {
        this.kafkaTemplate.send(AppConstants.TOPIC_NAME, new String(message.array()));
    }
}
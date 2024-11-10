package com.example.redisstreamconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class StreamConsumer implements StreamListener<String, ObjectRecord<String,String>> {

    private static Logger log = LoggerFactory.getLogger(StreamConsumer.class);


    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        System.out.println(" 1. Consumer Consumed" + message.getValue());
        log.info("Consumed");

    }
}

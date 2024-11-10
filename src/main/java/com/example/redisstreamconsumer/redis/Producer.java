package com.example.redisstreamconsumer.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Producer {

    @Value("${stream.key}")
    private String streamKey;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Producer(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //@Scheduled(fixedRateString= "${publish.rate}")
    public void publishEvent(String request){
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                .ofObject(request)
                .withStreamKey(streamKey);
        this.redisTemplate
                .opsForStream()
                .add(record);
        atomicInteger.incrementAndGet();
    }
}
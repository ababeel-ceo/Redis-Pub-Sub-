package com.example.redisstreamconsumer.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Value("${stream.key}")
    private String streamKey;

    @Value("${host.redis}")
    private String redisHost;

    @Value("${port.redis}")
    private int redisPort;
    @Lazy
    private final StreamListener<String, ObjectRecord<String, String>> streamListener;

    public RedisConfiguration(@Lazy StreamListener<String, ObjectRecord<String, String>> streamListener) {
        this.streamListener = streamListener;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        return new JedisConnectionFactory(configuration);
    }


    @Bean
    public Subscription subscription() throws UnknownHostException {
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(String.class)
                .build();
        var listenerContainer = StreamMessageListenerContainer
                .create(jedisConnectionFactory(), options);
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(streamKey, InetAddress.getLocalHost().getHostName()),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                streamListener);
        listenerContainer.start();
        return subscription;
    }




    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
        return redisTemplate;

    }
}

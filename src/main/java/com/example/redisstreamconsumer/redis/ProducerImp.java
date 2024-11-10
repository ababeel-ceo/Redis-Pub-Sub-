package com.example.redisstreamconsumer.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerImp {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private Producer producer;

    @RequestMapping(value = "/publisher",method = RequestMethod.POST)
    public ResponseEntity<Void> publish(@RequestBody String message) {

        try {
//            log.info("publishing >>" + message);
            producer.publishEvent(message.toString());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

package com.rama.reactive.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Service
@Log4j2
public class CacheServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private ReactiveRedisOperations<String, String> studentOps;

    @Autowired
    private ObjectMapper mapper;

    public <T>Mono<T> findCacheByKey(String key, Class<T> clazz){
        return studentOps.opsForValue()
                .get(key)
                .map(value -> {
                    try {
                        return mapper.readValue(value, clazz);
                    } catch (IOException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .onErrorResume(throwable -> {
                    LOGGER.error("Error on findCacheByKey. key = {}, error = {}", key, throwable.getMessage());
                    return Mono.error(throwable);
                });
    }

    public <T> Mono<Boolean> createCache(String key, T value, long ttl) {
        return Mono.just(value)
                .map(data -> this.toString(data, value))
                .flatMap(s -> studentOps.opsForValue().set(key, s, Duration.ofSeconds(ttl)))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error on createCache. key = {}, error = {}", key, throwable.getMessage());
                    return Mono.error(throwable);
                });
    }

    public <T> Mono<Boolean> createCachePersistent(String key, T value) {
        return Mono.just(value)
                .map(data -> this.toString(data, value))
                .flatMap(s -> studentOps.opsForValue().set(key, s))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error on createCache. key = {}, error = {}", key, throwable.getMessage());
                    return Mono.error(throwable);
                });
    }

    public Mono<Long> deleteCache(String key) {
        return studentOps.delete(key);
    }

    private <T> String toString(T data, T value) {
        try {
            return data instanceof String ? (String) data : mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw Exceptions.propagate(e);
        }
    }
}

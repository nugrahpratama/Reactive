package com.rama.reactive.Configuration;

import com.rama.reactive.Model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    ReactiveRedisOperations<String, Student> redisOperations(ReactiveRedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<Student> serializer = new Jackson2JsonRedisSerializer<>(Student.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Student> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Student> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}

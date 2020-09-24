package com.rama.reactive.Loader;

import com.rama.reactive.Model.Student;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class StudentLoader {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Student> studentOps;

    public StudentLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Student> studentOps){
        this.factory = factory;
        this.studentOps = studentOps;
    }

    @PostConstruct
    public void loadData() {
        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
                Flux.just("Rama", "Dani", "Test")
                        .map(name -> new Student(UUID.randomUUID().toString().substring(0, 5), name, "123456"))
                        .flatMap(student -> studentOps.opsForValue().set(student.getId(), student)))
                .thenMany(studentOps.keys("*")
                        .flatMap(studentOps.opsForValue()::get))
                .subscribe(System.out::println);
    }

}

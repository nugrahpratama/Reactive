package com.rama.reactive.Service;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rama.reactive.Model.Student;
import com.rama.reactive.Repository.StudentRepo;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@AllArgsConstructor
public class StudentService {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private StudentRepo studentRepo;
    private KafkaTemplate<String, String> kafkaTemplate;

    public Flux<Student> getAll() {
        kafkaTemplate.send("getAllStudent", "get all student data");
        return studentRepo.findAll().switchIfEmpty(Flux.empty());
    }

    public Mono<Student> getById(final String id) {
        return studentRepo.findById(id);
    }

    public Mono update(final String id, final Student student) {
        return studentRepo.save(student);
    }

    public Mono<Student> save(final Student student)  {
        try {
            kafkaTemplate.send("getAllStudent", mapper.writeValueAsString(student));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Mono.just(student);
    }

    public Mono delete(final String id) {
        final Mono<Student> dbStudent = getById(id);
        if (Objects.isNull(dbStudent)) {
            return Mono.empty();
        }
        return getById(id)
                .flatMap(student -> studentRepo.delete(student));
    }

}

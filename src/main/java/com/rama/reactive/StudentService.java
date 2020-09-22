package com.rama.reactive;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rama.reactive.Student;
import com.rama.reactive.Repository.StudentRepo;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@AllArgsConstructor
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public Flux<Student> getAll() {
        return studentRepo.findAll().switchIfEmpty(Flux.empty());
    }

    public Mono<Student> getById(final String id) {
        return studentRepo.findById(id);
    }

    public Mono update(final String id, final Student student) {
        return studentRepo.save(student);
    }

    public Mono save(final Student student) {
        return studentRepo.save(student);
    }

    public Mono delete(final String id) {
        final Mono<Student> dbStudent = getById(id);
        if (Objects.isNull(dbStudent)) {
            return Mono.empty();
        }
        return getById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull).flatMap(studentToBeDeleted -> studentRepo
                .delete(studentToBeDeleted).then(Mono.just(studentToBeDeleted)));
    }

}

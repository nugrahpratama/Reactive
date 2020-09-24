package com.rama.reactive.Controller;

import com.rama.reactive.Model.Student;
import com.rama.reactive.Repository.StudentRepo;
import com.rama.reactive.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@AllArgsConstructor
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    private StudentRepo studentRepo;

    private final ReactiveRedisOperations<String, Student> studentOps;

    @GetMapping
    public Flux<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("{id}")
    public Mono<Student> getById(@PathVariable("id") final String id) {
//        return studentService.getById(id);
        return studentOps.opsForValue().get(id);
    }

    @PutMapping("{id}")
    public Mono updateById(@PathVariable("id") final String id, @RequestBody final Student student) {
        return studentService.update(id, student);
    }

    @PostMapping
    public Mono<Student> save(@RequestBody final Student student) {
        return studentService.save(student).subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping("{id}")
    public Mono delete(@PathVariable final String id) {
        return studentService.delete(id);
    }

}

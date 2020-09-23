package com.rama.reactive.Controller;

import com.rama.reactive.Model.Student;
import com.rama.reactive.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Flux<Student> getAll() {
        return studentService.getAll();
    }


    @GetMapping("{id}")
    public Mono<Student> getById(@PathVariable("id") final String id) {
        return studentService.getById(id);
    }

    @PutMapping("{id}")
    public Mono updateById(@PathVariable("id") final String id, @RequestBody final Student student) {
        return studentService.update(id, student);
    }

    @PostMapping
    public Mono save(@RequestBody final Student student) {
        return studentService.save(student);
    }

    @DeleteMapping("{id}")
    public Mono delete(@PathVariable final String id) {
        return studentService.delete(id);
    }

}

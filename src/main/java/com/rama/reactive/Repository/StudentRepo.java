package com.rama.reactive.Repository;

import com.rama.reactive.Model.Student;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Repository
public interface StudentRepo extends ReactiveMongoRepository<Student, String> {
}

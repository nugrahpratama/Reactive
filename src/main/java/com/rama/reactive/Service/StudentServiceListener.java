package com.rama.reactive.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rama.reactive.Model.Student;
import com.rama.reactive.Repository.StudentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceListener {
    @Autowired
    private StudentRepo studentRepo;
    private static ObjectMapper mapper = new ObjectMapper();
    private final Logger LOG = LoggerFactory.getLogger(StudentServiceListener.class);

    @KafkaListener(topics = "getAllStudent")
    void listenGetSaveStudent(String message) throws JsonProcessingException {
        Student student = mapper.readValue(message, Student.class);
        student = studentRepo.save(student).block();
        LOG.info("Listener [{}]", student);
    }

}

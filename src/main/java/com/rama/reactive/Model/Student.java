package com.rama.reactive.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@Document
@NoArgsConstructor
public class Student {

    @Id
    private String id;
    private String firstName;
    private String lastName;

}

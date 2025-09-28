package com.example.review.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Component
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeToret {
    @Id
    String id;
    String name;
}

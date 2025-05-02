package com.ravi.meroJob.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "jobs")
@Data       //getters, setters, toString, equals, hashcode
public class Job {

    @Id
    private ObjectId jobId;

    private LocalDateTime dateCreated;

    private String jobName;
    private String jobDescription;
    private String jobLocation;
    private int yearsOfExperience;
    private List<String> skills = new ArrayList<>();
}

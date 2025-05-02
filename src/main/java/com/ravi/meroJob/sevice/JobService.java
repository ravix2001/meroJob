package com.ravi.meroJob.sevice;

import com.ravi.meroJob.entity.Job;
import com.ravi.meroJob.repository.JobRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getById(ObjectId jobId){
        return jobRepository.findById(jobId);
    }

    public void saveJob(Job job){
        job.setDateCreated(LocalDateTime.now());
        jobRepository.save(job);
    }

    public boolean deleteJob(ObjectId jobId){
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            jobRepository.deleteById(jobId);
            return true;  // Job found and deleted
        }
        return false;
    }

}

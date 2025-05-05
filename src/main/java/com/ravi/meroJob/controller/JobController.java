package com.ravi.meroJob.controller;

import com.ravi.meroJob.entity.Job;
import com.ravi.meroJob.service.JobService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5173")     // to enable Cross-Origin Resource Sharing (CORS) for the specified origin. It allows the front-end application (in this case, running on http://localhost:5173) to make HTTP requests to the Spring Boot back-end without getting blocked by the browser’s same-origin policy.
//@RestController       // Used for REST APIs.
@Controller      // Used in traditional MVC (Model-View-Controller) web applications.
@RequestMapping
public class JobController {

    @Autowired
    JobService jobService;

//    Used in traditional MVC (Model-View-Controller) web applications.

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("message", "Hello User");
        return "index"; // Corresponds to src/main/resources/templates/index.html
    }

    @GetMapping("/jobs/view")
    public String viewJobs(Model model) {
        List<Job> allJobs = jobService.getAllJobs();
        model.addAttribute("jobs", allJobs);
        return "jobs";
    }

    @GetMapping("/jobs/new")
    public String showPostJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "post-job";
    }

    @PostMapping("/jobs")
    public String submitJob(@ModelAttribute Job job) {
        jobService.saveJob(job);
        return "redirect:/jobs/view";
    }

//    Used for REST APIs.

    @GetMapping("/health-check")        // The class uses the `@Controller` annotation, which is primarily used for returning **views** (like HTML files), not raw responses like `"OK"`. Since `JobController` returns a simple string instead of a view, it should use the `@RestController` annotation.
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getAll(){
        List<Job> allJobs = jobService.getAllJobs();
        if(allJobs != null && !allJobs.isEmpty()) {
            return new ResponseEntity<>(allJobs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("job/id/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable String jobId) {
        try{
            ObjectId id = new ObjectId(jobId); // Validate and convert here
            Optional<Job> job = jobService.getById(id);
            if(job.isPresent()) {
                return new ResponseEntity<>(job, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid Job ID format.", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("post-job")
    public ResponseEntity<?> createJob(@RequestBody Job job){
        try {
            jobService.saveJob(job);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("job/id/{jobId}")
    public ResponseEntity<?> updateJobById(@PathVariable String jobId, @RequestBody Job job) {
        try {
            ObjectId id = new ObjectId(jobId);
            Optional<Job> old = jobService.getById(id);
            if (old.isPresent()) {
                Job updatedJob = old.get();
                updatedJob.setJobName(job.getJobName());
                updatedJob.setJobDescription(job.getJobDescription());
                updatedJob.setJobLocation(job.getJobLocation());
                updatedJob.setYearsOfExperience(job.getYearsOfExperience());
                updatedJob.setSkills(job.getSkills());

                jobService.saveJob(updatedJob);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            // Handle invalid ObjectId format
            return new ResponseEntity<>("Invalid Job ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("job/id/{jobId}")
    public ResponseEntity<?> deleteJobById(@PathVariable String jobId){
        try{
            ObjectId id = new ObjectId(jobId);
            boolean deleted = jobService.deleteJob(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid Job ID format.", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
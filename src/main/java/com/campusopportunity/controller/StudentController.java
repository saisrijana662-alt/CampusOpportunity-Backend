package com.campusopportunity.controller;

import com.campusopportunity.dto.LoginRequest;
import com.campusopportunity.entity.Student;
import com.campusopportunity.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public Student register(@RequestBody Student student) {
        return studentService.register(student);
    }

    @PostMapping("/login")
    public Student login(@RequestBody LoginRequest request) {
        return studentService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }
}

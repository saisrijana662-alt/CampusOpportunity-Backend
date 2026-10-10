package com.campusopportunity.service;

import com.campusopportunity.config.ApiException;
import com.campusopportunity.entity.Student;
import com.campusopportunity.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student register(Student student) {
        if (isBlank(student.getName()) || isBlank(student.getEmail()) || isBlank(student.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
        }
        student.setEmail(student.getEmail().trim().toLowerCase());
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student login(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        Student student = studentRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!student.getPassword().equals(password)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return student;
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    // Email and password are NOT changed here.
    public Student update(Long id, Student updated) {
        Student student = getById(id);
        student.setName(updated.getName());
        student.setBranch(updated.getBranch());
        student.setCgpa(updated.getCgpa());
        student.setGraduationYear(updated.getGraduationYear());
        student.setSkills(updated.getSkills());
        student.setInterests(updated.getInterests());
        student.setBacklogs(updated.getBacklogs());
        return studentRepository.save(student);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

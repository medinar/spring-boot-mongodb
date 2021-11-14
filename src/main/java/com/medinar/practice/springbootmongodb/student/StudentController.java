package com.medinar.practice.springbootmongodb.student;

import com.medinar.practice.springbootmongodb.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> fetchAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public void addStudent(@RequestBody Student student) throws BadRequestException {
        studentService.addStudent(student);
    }
}

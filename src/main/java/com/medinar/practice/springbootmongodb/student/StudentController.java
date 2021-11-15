package com.medinar.practice.springbootmongodb.student;

import com.medinar.practice.springbootmongodb.student.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> fetchAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return new ResponseEntity<>(students, NO_CONTENT);
        } else {
            return new ResponseEntity<>(students, OK);
        }
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) throws BadRequestException {
        try {
            Student _student = studentService.addStudent(student);
            return new ResponseEntity<>(_student, CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return new ResponseEntity<>(OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable String studentId) {
        try {
            Student _student = studentService.updateStudent(student, studentId);
            return new ResponseEntity<>(_student, OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }

    }

}

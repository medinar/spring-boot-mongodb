package com.medinar.practice.springbootmongodb.student;

import com.medinar.practice.springbootmongodb.student.exception.BadRequestException;
import com.medinar.practice.springbootmongodb.student.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) throws BadRequestException {
        Boolean emailExists = studentRepository.existsByEmail(student.getEmail());
        if (emailExists) {
            throw new BadRequestException(String.format(
                    "Email `%s` already exist",
                    student.getEmail()
            ));
        }
        studentRepository.insert(student);
    }

    public void deleteStudent(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException(String.format(
                    "Student with id %s does not exists",
                    studentId
            ));
        }
        studentRepository.deleteById(studentId);
    }
}

package com.medinar.practice.springbootmongodb.student;

import com.medinar.practice.springbootmongodb.student.exception.BadRequestException;
import com.medinar.practice.springbootmongodb.student.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public void updateStudent(Student student, String studentId) {
        Student _student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("student with id %s does not exists", studentId)
                ));

        if (student.getFirstName() != null
                && student.getFirstName().length() > 0 &&
                !Objects.equals(_student.getFirstName(), student.getFirstName())
        ) {
            _student.setFirstName(student.getFirstName());
        }

        if (student.getLastName() != null
                && student.getLastName().length() > 0 &&
                !Objects.equals(_student.getLastName(), student.getLastName())
        ) {
            _student.setLastName(student.getLastName());
        }

        if (student.getEmail() != null &&
                student.getEmail().length() > 0 &&
                !Objects.equals(_student.getEmail(), student.getEmail())
        ) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new IllegalStateException("email taken");
            }
            _student.setEmail(student.getEmail());
        }

        if (student.getGender() != null &&
                student.getEmail().length() > 0 &&
                !Objects.equals(_student.getGender(), student.getGender())
        ) {
            _student.setGender(student.getGender());
        }

        if (student.getFavouriteSubjects() != null && !student.getFavouriteSubjects().isEmpty()) {
            _student.setFavouriteSubjects(student.getFavouriteSubjects());
        }

        if (student.getTotalSpentInBooks() != null &&
                !student.getTotalSpentInBooks().equals(_student.getTotalSpentInBooks())
        ) {
            _student.setTotalSpentInBooks(student.getTotalSpentInBooks());
        }

        if (student.getCreated() != null &&
                !student.getCreated().equals(_student.getCreated())
        ) {
            _student.setCreated(student.getCreated());
        }
        studentRepository.save(_student);
    }
}

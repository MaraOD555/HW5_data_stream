package ru.hogwarts.school.HW5_data_stream.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.HW5_data_stream.model.Student;
import ru.hogwarts.school.HW5_data_stream.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }
    @GetMapping("{id}")
    public ResponseEntity<Student> findStudentInfo (@PathVariable long id){
        Student student = studentService.findStudent(id);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }
    @PutMapping
    public ResponseEntity<Student> editStudentInfo (@RequestBody Student student){
        Student studentForEdit = studentService.editStudent(student);
        if(studentForEdit == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentForEdit);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudentInfo (@PathVariable long id){
        studentService.deleteStudents(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "age")
    public ResponseEntity<Collection<Student>> findStudentByAge (@RequestParam(required = false) int age){
        if(age > 0){
            return ResponseEntity.ok(studentService.findByAge(age));
        }
       return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> findByAgeBetween (@RequestParam int from,
                                                                 @RequestParam int to){
        return ResponseEntity.ok(studentService.findByAgeBetween(from, to));
    }
}

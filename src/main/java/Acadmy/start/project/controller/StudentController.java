package Acadmy.start.project.controller;

import Acadmy.start.project.dto.StudentDTO;
import Acadmy.start.project.dto.StudentFilterDTO;
import Acadmy.start.project.dto.StudentFormDTO;
import Acadmy.start.project.dto.StudentGridDTO;
import Acadmy.start.project.generic.GenericController;
import Acadmy.start.project.model.Student;
import Acadmy.start.project.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController extends GenericController<Student, StudentFilterDTO, StudentGridDTO, StudentFormDTO, Long> {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        super(studentService);
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> getAllList() {
        return ResponseEntity.ok(studentService.getAll());
    }
}
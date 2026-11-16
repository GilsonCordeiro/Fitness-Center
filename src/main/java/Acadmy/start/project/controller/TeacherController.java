package Acadmy.start.project.controller;

import Acadmy.start.project.dto.TeacherDTO;
import Acadmy.start.project.dto.TeacherFilterDTO;
import Acadmy.start.project.dto.TeacherFormDTO;
import Acadmy.start.project.dto.TeacherGridDTO;
import Acadmy.start.project.generic.GenericController;
import Acadmy.start.project.model.Teacher;
import Acadmy.start.project.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController extends GenericController<Teacher, TeacherFilterDTO, TeacherGridDTO, TeacherFormDTO, Long> {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        super(teacherService);
        this.teacherService = teacherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherDTO>> getAllList() {
        return ResponseEntity.ok(teacherService.getAll());
    }
}
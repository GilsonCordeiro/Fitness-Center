package Acadmy.start.project.controller;

import Acadmy.start.project.dto.ClassesDTO;
import Acadmy.start.project.dto.ClassesFilterDTO;
import Acadmy.start.project.dto.ClassesFormDTO;
import Acadmy.start.project.dto.ClassesGridDTO;
import Acadmy.start.project.generic.GenericController;
import Acadmy.start.project.model.Classes;
import Acadmy.start.project.service.ClassesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassesController extends GenericController<Classes, ClassesFilterDTO, ClassesGridDTO, ClassesFormDTO, Long> {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        super(classesService);
        this.classesService = classesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClassesDTO>> getAllList() {
        return ResponseEntity.ok(classesService.getAll());
    }
}
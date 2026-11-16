package Acadmy.start.project.mapper;

import Acadmy.start.project.dto.ClassesDTO;
import Acadmy.start.project.dto.ClassesFormDTO;
import Acadmy.start.project.dto.ClassesGridDTO;
import Acadmy.start.project.model.Classes;
import Acadmy.start.project.model.Student;

import java.util.HashSet;
import java.util.stream.Collectors;

public final class ClassesMapper {

    private ClassesMapper() {
    }

    public static Classes toEntity(ClassesFormDTO form) {
        if (form == null) return null;
        Classes entity = new Classes();
        entity.setId(form.getId());
        entity.setTitle(form.getTitle());
        entity.setDescription(form.getDescription());
        entity.setInstructor(form.getInstructor());
        entity.setDurationMinutes(form.getDurationMinutes());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        entity.setStudents(new HashSet<>());
        return entity;
    }

    public static ClassesFormDTO toForm(Classes entity) {
        if (entity == null) return null;
        ClassesFormDTO form = new ClassesFormDTO();
        form.setId(entity.getId());
        form.setTitle(entity.getTitle());
        form.setDescription(entity.getDescription());
        form.setInstructor(entity.getInstructor());
        form.setDurationMinutes(entity.getDurationMinutes());
        form.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        if (entity.getStudents() != null) {
            form.setStudentIds(entity.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet()));
        }
        form.setIsActive(entity.getIsActive());
        return form;
    }

    public static ClassesGridDTO toGrid(Classes entity) {
        if (entity == null) return null;
        ClassesGridDTO grid = new ClassesGridDTO();
        grid.setId(entity.getId());
        grid.setTitle(entity.getTitle());
        grid.setDescription(entity.getDescription());
        grid.setInstructor(entity.getInstructor());
        grid.setDurationMinutes(entity.getDurationMinutes());
        grid.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        grid.setTeacherName(entity.getTeacher() != null ? entity.getTeacher().getName() : null);
        grid.setIsActive(entity.getIsActive());
        return grid;
    }

    public static ClassesDTO toDTO(Classes entity) {
        if (entity == null) return null;
        ClassesDTO dto = new ClassesDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setInstructor(entity.getInstructor());
        dto.setDurationMinutes(entity.getDurationMinutes());
        dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        dto.setTeacherName(entity.getTeacher() != null ? entity.getTeacher().getName() : null);
        if (entity.getStudents() != null) {
            dto.setStudentIds(entity.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet()));
        }
        dto.setIsActive(entity.getIsActive());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
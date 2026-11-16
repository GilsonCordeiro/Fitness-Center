package Acadmy.start.project.mapper;

import Acadmy.start.project.dto.StudentDTO;
import Acadmy.start.project.dto.StudentFormDTO;
import Acadmy.start.project.dto.StudentGridDTO;
import Acadmy.start.project.model.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static Student toEntity(StudentFormDTO form) {
        if (form == null) return null;
        Student entity = new Student();
        entity.setId(form.getId());
        entity.setName(form.getName());
        entity.setEmail(form.getEmail());
        entity.setEnrollmentNumber(form.getEnrollmentNumber());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        return entity;
    }

    public static StudentFormDTO toForm(Student entity) {
        if (entity == null) return null;
        StudentFormDTO form = new StudentFormDTO();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setEmail(entity.getEmail());
        form.setEnrollmentNumber(entity.getEnrollmentNumber());
        form.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        form.setIsActive(entity.getIsActive());
        return form;
    }

    public static StudentGridDTO toGrid(Student entity) {
        if (entity == null) return null;
        StudentGridDTO grid = new StudentGridDTO();
        grid.setId(entity.getId());
        grid.setName(entity.getName());
        grid.setEmail(entity.getEmail());
        grid.setEnrollmentNumber(entity.getEnrollmentNumber());
        grid.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        grid.setTeacherName(entity.getTeacher() != null ? entity.getTeacher().getName() : null);
        grid.setIsActive(entity.getIsActive());
        return grid;
    }

    public static StudentDTO toDTO(Student entity) {
        if (entity == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setEnrollmentNumber(entity.getEnrollmentNumber());
        dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        dto.setTeacherName(entity.getTeacher() != null ? entity.getTeacher().getName() : null);
        dto.setIsActive(entity.getIsActive());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
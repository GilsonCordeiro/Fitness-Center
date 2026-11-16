package Acadmy.start.project.mapper;

import Acadmy.start.project.dto.TeacherDTO;
import Acadmy.start.project.dto.TeacherFormDTO;
import Acadmy.start.project.dto.TeacherGridDTO;
import Acadmy.start.project.model.Teacher;

public final class TeacherMapper {

    private TeacherMapper() {
    }

    public static Teacher toEntity(TeacherFormDTO form) {
        if (form == null) return null;
        Teacher entity = new Teacher();
        entity.setId(form.getId());
        entity.setName(form.getName());
        entity.setEmail(form.getEmail());
        entity.setSpecialty(form.getSpecialty());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        return entity;
    }

    public static TeacherFormDTO toForm(Teacher entity) {
        if (entity == null) return null;
        TeacherFormDTO form = new TeacherFormDTO();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setEmail(entity.getEmail());
        form.setSpecialty(entity.getSpecialty());
        form.setIsActive(entity.getIsActive());
        return form;
    }

    public static TeacherGridDTO toGrid(Teacher entity) {
        if (entity == null) return null;
        TeacherGridDTO grid = new TeacherGridDTO();
        grid.setId(entity.getId());
        grid.setName(entity.getName());
        grid.setEmail(entity.getEmail());
        grid.setSpecialty(entity.getSpecialty());
        grid.setIsActive(entity.getIsActive());
        return grid;
    }

    public static TeacherDTO toDTO(Teacher entity) {
        if (entity == null) return null;
        TeacherDTO dto = new TeacherDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setSpecialty(entity.getSpecialty());
        dto.setIsActive(entity.getIsActive());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
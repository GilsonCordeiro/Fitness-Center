package Acadmy.start.project.dto;

import lombok.Data;

@Data
public class TeacherFilterDTO {

    private String name;
    private String email;
    private String specialty;
    private Boolean isActive;
}
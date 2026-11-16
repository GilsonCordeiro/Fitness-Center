package Acadmy.start.project.dto;

import lombok.Data;

@Data
public class StudentFilterDTO {

    private String name;
    private String email;
    private Long teacherId;
    private Boolean isActive;
}
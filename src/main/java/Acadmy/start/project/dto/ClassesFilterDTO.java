package Acadmy.start.project.dto;

import lombok.Data;

@Data
public class ClassesFilterDTO {

    private String title;
    private String instructor;
    private Long teacherId;
    private Boolean isActive;
}
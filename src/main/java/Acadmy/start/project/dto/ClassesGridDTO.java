package Acadmy.start.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesGridDTO {

    private Long id;
    private String title;
    private String description;
    private String instructor;
    private Integer durationMinutes;
    private Long teacherId;
    private String teacherName;
    private Boolean isActive;
}
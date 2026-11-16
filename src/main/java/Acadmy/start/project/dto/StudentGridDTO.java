package Acadmy.start.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGridDTO {

    private Long id;
    private String name;
    private String email;
    private String enrollmentNumber;
    private Long teacherId;
    private String teacherName;
    private Boolean isActive;
}
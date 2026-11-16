package Acadmy.start.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesDTO {

    private Long id;
    private String title;
    private String description;
    private String instructor;
    private Integer durationMinutes;
    private Long teacherId;
    private String teacherName;
    private Set<Long> studentIds;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
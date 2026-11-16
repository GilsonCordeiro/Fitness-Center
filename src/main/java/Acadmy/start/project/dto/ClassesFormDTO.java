package Acadmy.start.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesFormDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be at most 150 characters")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Size(max = 150, message = "Instructor must be at most 150 characters")
    private String instructor;

    private Integer durationMinutes;

    private Long teacherId;

    private Set<Long> studentIds;

    private Boolean isActive;
}
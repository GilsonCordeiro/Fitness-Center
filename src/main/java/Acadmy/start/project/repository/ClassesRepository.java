package Acadmy.start.project.repository;

import Acadmy.start.project.generic.repository.GenericEntityDeletedRepository;
import Acadmy.start.project.model.Classes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassesRepository extends GenericEntityDeletedRepository<Classes, Long> {

    @Query("SELECT c FROM Classes c " +
            "LEFT JOIN c.teacher t " +
            "WHERE c.isDeleted = false " +
            "AND (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:instructor IS NULL OR LOWER(c.instructor) LIKE LOWER(CONCAT('%', :instructor, '%'))) " +
            "AND (:teacherId IS NULL OR t.id = :teacherId) " +
            "AND (:isActive IS NULL OR c.isActive = :isActive)")
    Page<Classes> findAllByFilter(@Param("title") String title,
                                  @Param("instructor") String instructor,
                                  @Param("teacherId") Long teacherId,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);

    @Query("SELECT c.id FROM Classes c " +
            "WHERE (:id IS NULL OR c.id <> :id) " +
            "AND UPPER(c.title) = UPPER(:title) " +
            "AND c.isDeleted = false")
    Optional<Long> findIdByTitle(@Param("id") Long id, @Param("title") String title);

    @Query("SELECT c FROM Classes c WHERE UPPER(c.title) = UPPER(:title)")
    Optional<Classes> findByTitle(@Param("title") String title);
}
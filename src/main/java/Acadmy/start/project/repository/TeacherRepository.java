package Acadmy.start.project.repository;

import Acadmy.start.project.generic.repository.GenericEntityDeletedRepository;
import Acadmy.start.project.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends GenericEntityDeletedRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t " +
            "WHERE t.isDeleted = false " +
            "AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:email IS NULL OR LOWER(t.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:specialty IS NULL OR LOWER(t.specialty) LIKE LOWER(CONCAT('%', :specialty, '%'))) " +
            "AND (:isActive IS NULL OR t.isActive = :isActive)")
    Page<Teacher> findAllByFilter(@Param("name") String name,
                                  @Param("email") String email,
                                  @Param("specialty") String specialty,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);

    @Query("SELECT t.id FROM Teacher t WHERE (:id IS NULL OR t.id <> :id) AND UPPER(t.email) = UPPER(:email) AND t.isDeleted = false")
    Optional<Long> findIdByEmail(@Param("id") Long id, @Param("email") String email);

    @Query("SELECT t FROM Teacher t WHERE UPPER(t.email) = UPPER(:email)")
    Optional<Teacher> findByEmail(@Param("email") String email);
}
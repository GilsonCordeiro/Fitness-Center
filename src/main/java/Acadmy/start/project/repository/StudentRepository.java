package Acadmy.start.project.repository;

import Acadmy.start.project.generic.repository.GenericEntityDeletedRepository;
import Acadmy.start.project.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends GenericEntityDeletedRepository<Student, Long> {

    @Query("SELECT s FROM Student s " +
            "LEFT JOIN s.teacher t " +
            "WHERE s.isDeleted = false " +
            "AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:teacherId IS NULL OR t.id = :teacherId) " +
            "AND (:isActive IS NULL OR s.isActive = :isActive)")
    Page<Student> findAllByFilter(@Param("name") String name,
                                  @Param("email") String email,
                                  @Param("teacherId") Long teacherId,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);

    @Query("SELECT s.id FROM Student s WHERE (:id IS NULL OR s.id <> :id) AND UPPER(s.email) = UPPER(:email) AND s.isDeleted = false")
    Optional<Long> findIdByEmail(@Param("id") Long id, @Param("email") String email);

    @Query("SELECT s FROM Student s WHERE UPPER(s.email) = UPPER(:email)")
    Optional<Student> findByEmail(@Param("email") String email);
}
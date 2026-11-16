package Acadmy.start.project.service;

import Acadmy.start.project.dto.StudentDTO;
import Acadmy.start.project.dto.StudentFilterDTO;
import Acadmy.start.project.dto.StudentFormDTO;
import Acadmy.start.project.dto.StudentGridDTO;
import Acadmy.start.project.exceptions.BusinessException;
import Acadmy.start.project.generic.service.GenericService;
import Acadmy.start.project.mapper.StudentMapper;
import Acadmy.start.project.model.Student;
import Acadmy.start.project.model.Teacher;
import Acadmy.start.project.repository.StudentRepository;
import Acadmy.start.project.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService extends GenericService<Student, StudentFilterDTO, StudentGridDTO, StudentFormDTO, Long> {

    private final StudentRepository repository;
    private final TeacherRepository teacherRepository;

    public StudentService(StudentRepository repository, TeacherRepository teacherRepository) {
        super(repository, "Student");
        this.repository = repository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<StudentDTO> getAll() {
        return repository.findAllByIsDeletedFalse().stream()
                .map(StudentMapper::toDTO)
                .toList();
    }

    @Override
    protected Student toEntity(StudentFormDTO form) {
        Student entity = StudentMapper.toEntity(form);
        applyTeacher(entity, form.getTeacherId());
        return entity;
    }

    @Override
    protected StudentGridDTO toGrid(Student entity) {
        return StudentMapper.toGrid(entity);
    }

    @Override
    protected StudentFormDTO toForm(Student entity) {
        return StudentMapper.toForm(entity);
    }

    @Override
    protected void validarCreateHeSave(StudentFormDTO form) {
        repository.findIdByEmail(form.getId(), form.getEmail())
                .ifPresent(id -> {
                    throw new BusinessException(String.format(
                            "A student with email '%s' already exists.", form.getEmail()));
                });
        if (form.getTeacherId() != null) {
            teacherRepository.findByIdAndIsDeletedFalse(form.getTeacherId())
                    .orElseThrow(() -> new BusinessException("Teacher not found with id: " + form.getTeacherId()));
        }
    }

    @Override
    protected Page<StudentGridDTO> findAllByFilter(StudentFilterDTO filter, Pageable pageable) {
        return repository.findAllByFilter(
                        filter.getName(),
                        filter.getEmail(),
                        filter.getTeacherId(),
                        filter.getIsActive(),
                        pageable)
                .map(StudentMapper::toGrid);
    }

    @Override
    @Transactional
    public StudentFormDTO save(StudentFormDTO form) {
        return tryReactivateDeleted(form).orElseGet(() -> super.save(form));
    }

    @Override
    @Transactional
    public StudentFormDTO update(Long id, StudentFormDTO form) {
        StudentFormDTO updated = super.update(id, form);
        // Ensure teacher relation is persisted correctly after generic update
        Student entity = repository.findById(id).orElseThrow();
        applyTeacher(entity, form.getTeacherId());
        repository.save(entity);
        return StudentMapper.toForm(entity);
    }

    private void applyTeacher(Student entity, Long teacherId) {
        if (teacherId == null) {
            entity.setTeacher(null);
        } else {
            Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(teacherId)
                    .orElseThrow(() -> new BusinessException("Teacher not found with id: " + teacherId));
            entity.setTeacher(teacher);
        }
    }

    private Optional<StudentFormDTO> tryReactivateDeleted(StudentFormDTO form) {
        if (form.getId() != null || form.getEmail() == null) return Optional.empty();
        return repository.findByEmail(form.getEmail())
                .filter(s -> Boolean.TRUE.equals(s.getIsDeleted()))
                .map(entity -> reactivate(form, entity));
    }

    private StudentFormDTO reactivate(StudentFormDTO form, Student entity) {
        entity.setName(form.getName());
        entity.setEmail(form.getEmail());
        entity.setEnrollmentNumber(form.getEnrollmentNumber());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        entity.setIsDeleted(false);
        applyTeacher(entity, form.getTeacherId());
        return StudentMapper.toForm(repository.save(entity));
    }
}
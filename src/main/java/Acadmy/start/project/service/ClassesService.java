package Acadmy.start.project.service;

import Acadmy.start.project.dto.ClassesDTO;
import Acadmy.start.project.dto.ClassesFilterDTO;
import Acadmy.start.project.dto.ClassesFormDTO;
import Acadmy.start.project.dto.ClassesGridDTO;
import Acadmy.start.project.exceptions.BusinessException;
import Acadmy.start.project.generic.service.GenericService;
import Acadmy.start.project.mapper.ClassesMapper;
import Acadmy.start.project.model.Classes;
import Acadmy.start.project.model.Student;
import Acadmy.start.project.model.Teacher;
import Acadmy.start.project.repository.ClassesRepository;
import Acadmy.start.project.repository.StudentRepository;
import Acadmy.start.project.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClassesService extends GenericService<Classes, ClassesFilterDTO, ClassesGridDTO, ClassesFormDTO, Long> {

    private final ClassesRepository repository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public ClassesService(ClassesRepository repository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        super(repository, "Class");
        this.repository = repository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<ClassesDTO> getAll() {
        return repository.findAllByIsDeletedFalse().stream()
                .map(ClassesMapper::toDTO)
                .toList();
    }

    @Override
    protected Classes toEntity(ClassesFormDTO form) {
        Classes entity = ClassesMapper.toEntity(form);
        applyRelations(entity, form);
        return entity;
    }

    @Override
    protected ClassesGridDTO toGrid(Classes entity) {
        return ClassesMapper.toGrid(entity);
    }

    @Override
    protected ClassesFormDTO toForm(Classes entity) {
        return ClassesMapper.toForm(entity);
    }

    @Override
    protected void validarCreateHeSave(ClassesFormDTO form) {
        repository.findIdByTitle(form.getId(), form.getTitle())
                .ifPresent(id -> {
                    throw new BusinessException(String.format(
                            "A class with title '%s' already exists.", form.getTitle()));
                });
        if (form.getTeacherId() != null) {
            teacherRepository.findByIdAndIsDeletedFalse(form.getTeacherId())
                    .orElseThrow(() -> new BusinessException("Teacher not found with id: " + form.getTeacherId()));
        }
        validateStudents(form.getStudentIds());
    }

    @Override
    protected Page<ClassesGridDTO> findAllByFilter(ClassesFilterDTO filter, Pageable pageable) {
        return repository.findAllByFilter(
                        filter.getTitle(),
                        filter.getInstructor(),
                        filter.getTeacherId(),
                        filter.getIsActive(),
                        pageable)
                .map(ClassesMapper::toGrid);
    }

    @Override
    @Transactional
    public ClassesFormDTO save(ClassesFormDTO form) {
        return tryReactivateDeleted(form).orElseGet(() -> {
            ClassesFormDTO saved = super.save(form);
            // ensure relations persisted (students set)
            Classes entity = repository.findById(saved.getId()).orElseThrow();
            applyRelations(entity, form);
            repository.save(entity);
            return ClassesMapper.toForm(entity);
        });
    }

    @Override
    @Transactional
    public ClassesFormDTO update(Long id, ClassesFormDTO form) {
        ClassesFormDTO updated = super.update(id, form);
        Classes entity = repository.findById(id).orElseThrow();
        applyRelations(entity, form);
        repository.save(entity);
        return ClassesMapper.toForm(entity);
    }

    private void applyRelations(Classes entity, ClassesFormDTO form) {
        // Teacher
        if (form.getTeacherId() == null) {
            entity.setTeacher(null);
        } else {
            Teacher teacher = teacherRepository.findByIdAndIsDeletedFalse(form.getTeacherId())
                    .orElseThrow(() -> new BusinessException("Teacher not found with id: " + form.getTeacherId()));
            entity.setTeacher(teacher);
            // auto-fill instructor name from teacher if not provided
            if (entity.getInstructor() == null || entity.getInstructor().isBlank()) {
                entity.setInstructor(teacher.getName());
            }
        }
        // Students
        if (form.getStudentIds() == null || form.getStudentIds().isEmpty()) {
            entity.setStudents(new HashSet<>());
        } else {
            Set<Student> students = new HashSet<>(studentRepository.findAllById(form.getStudentIds()));
            // filter deleted
            students.removeIf(s -> Boolean.TRUE.equals(s.getIsDeleted()));
            if (students.size() != form.getStudentIds().size()) {
                throw new BusinessException("One or more students were not found.");
            }
            entity.setStudents(students);
        }
    }

    private void validateStudents(Set<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) return;
        Set<Student> students = new HashSet<>(studentRepository.findAllById(studentIds));
        students.removeIf(s -> Boolean.TRUE.equals(s.getIsDeleted()));
        if (students.size() != studentIds.size()) {
            throw new BusinessException("One or more students were not found.");
        }
    }

    private Optional<ClassesFormDTO> tryReactivateDeleted(ClassesFormDTO form) {
        if (form.getId() != null || form.getTitle() == null) return Optional.empty();
        return repository.findByTitle(form.getTitle())
                .filter(c -> Boolean.TRUE.equals(c.getIsDeleted()))
                .map(entity -> reactivate(form, entity));
    }

    private ClassesFormDTO reactivate(ClassesFormDTO form, Classes entity) {
        entity.setTitle(form.getTitle());
        entity.setDescription(form.getDescription());
        entity.setInstructor(form.getInstructor());
        entity.setDurationMinutes(form.getDurationMinutes());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        entity.setIsDeleted(false);
        applyRelations(entity, form);
        return ClassesMapper.toForm(repository.save(entity));
    }
}
package Acadmy.start.project.service;

import Acadmy.start.project.dto.TeacherDTO;
import Acadmy.start.project.dto.TeacherFilterDTO;
import Acadmy.start.project.dto.TeacherFormDTO;
import Acadmy.start.project.dto.TeacherGridDTO;
import Acadmy.start.project.exceptions.BusinessException;
import Acadmy.start.project.generic.service.GenericService;
import Acadmy.start.project.mapper.TeacherMapper;
import Acadmy.start.project.model.Teacher;
import Acadmy.start.project.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService extends GenericService<Teacher, TeacherFilterDTO, TeacherGridDTO, TeacherFormDTO, Long> {

    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        super(repository, "Teacher");
        this.repository = repository;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<TeacherDTO> getAll() {
        return repository.findAllByIsDeletedFalse().stream()
                .map(TeacherMapper::toDTO)
                .toList();
    }

    @Override
    protected Teacher toEntity(TeacherFormDTO form) {
        return TeacherMapper.toEntity(form);
    }

    @Override
    protected TeacherGridDTO toGrid(Teacher entity) {
        return TeacherMapper.toGrid(entity);
    }

    @Override
    protected TeacherFormDTO toForm(Teacher entity) {
        return TeacherMapper.toForm(entity);
    }

    @Override
    protected void validarCreateHeSave(TeacherFormDTO form) {
        repository.findIdByEmail(form.getId(), form.getEmail())
                .ifPresent(id -> {
                    throw new BusinessException(String.format(
                            "A teacher with email '%s' already exists.", form.getEmail()));
                });
    }

    @Override
    protected Page<TeacherGridDTO> findAllByFilter(TeacherFilterDTO filter, Pageable pageable) {
        return repository.findAllByFilter(
                        filter.getName(),
                        filter.getEmail(),
                        filter.getSpecialty(),
                        filter.getIsActive(),
                        pageable)
                .map(TeacherMapper::toGrid);
    }

    @Override
    @Transactional
    public TeacherFormDTO save(TeacherFormDTO form) {
        return tryReactivateDeleted(form).orElseGet(() -> super.save(form));
    }

    private Optional<TeacherFormDTO> tryReactivateDeleted(TeacherFormDTO form) {
        if (form.getId() != null || form.getEmail() == null) return Optional.empty();
        return repository.findByEmail(form.getEmail())
                .filter(t -> Boolean.TRUE.equals(t.getIsDeleted()))
                .map(entity -> reactivate(form, entity));
    }

    private TeacherFormDTO reactivate(TeacherFormDTO form, Teacher entity) {
        entity.setName(form.getName());
        entity.setEmail(form.getEmail());
        entity.setSpecialty(form.getSpecialty());
        entity.setIsActive(form.getIsActive() != null ? form.getIsActive() : true);
        entity.setIsDeleted(false);
        return TeacherMapper.toForm(repository.save(entity));
    }
}
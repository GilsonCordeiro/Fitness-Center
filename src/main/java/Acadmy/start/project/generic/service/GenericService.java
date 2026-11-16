package Acadmy.start.project.generic.service;

import Acadmy.start.project.exceptions.BusinessException;
import Acadmy.start.project.generic.interfaces.IActiveTable;
import Acadmy.start.project.generic.interfaces.IDeletedTable;
import Acadmy.start.project.generic.repository.GenericEntityDeletedRepository;
import Acadmy.start.project.util.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public abstract class GenericService<E, I, G, F, K> {

    protected final JpaRepository<E, K> repositoryGeneric;
    private final String entityNameGeneric;

    protected GenericService(JpaRepository<E, K> repositoryGeneric, String entity) {
        this.repositoryGeneric = repositoryGeneric;
        this.entityNameGeneric = entity;
    }

    protected abstract E toEntity(F form);

    protected abstract G toGrid(E entidade);

    protected abstract F toForm(E entidade);

    protected abstract void validarCreateHeSave(F form);

    protected abstract Page<G> findAllByFilter(I filtro, Pageable pageable);

    @Transactional(readOnly = true)
    public Page<G> filtrar(I filtro, Pageable pageable) {
        try {
            Page<G> result = this.findAllByFilter(filtro, pageable);
            log.info("Filtro retornou {} elementos.", result.getTotalElements());
            return result;
        } catch (Exception ex) {
            log.error("Erro ao filtrar: {}", entityNameGeneric + " " + ex.getMessage(), ex);
            throw new BusinessException(ExceptionConstants.ERRO_AO_FILTRAR_ENTIDADE, entityNameGeneric);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        if (repositoryGeneric instanceof GenericEntityDeletedRepository<?, ?>) {
            GenericEntityDeletedRepository<E, K> genericEntityDeletedRepository =
                    (GenericEntityDeletedRepository<E, K>) repositoryGeneric;
            return genericEntityDeletedRepository.findAllByIsDeletedFalse();
        }

        return this.repositoryGeneric.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public Page<G> findAll(Pageable pageable) {
        log.info("Iniciando consulta paginada");
        try {
            final Page<E> entities;

            if (repositoryGeneric instanceof GenericEntityDeletedRepository<?, ?>) {
                GenericEntityDeletedRepository<E, K> genericEntityDeletedRepository =
                        (GenericEntityDeletedRepository<E, K>) repositoryGeneric;
                entities = genericEntityDeletedRepository.findAllByIsDeletedFalse(pageable);
            } else {
                entities = this.repositoryGeneric.findAll(pageable);
            }

            return entities.map(this::toGrid);
        } catch (Exception ex) {
            log.error("Erro ao listar: {}", entityNameGeneric.concat(" ").concat(ex.getMessage()), ex);
            throw new BusinessException(ExceptionConstants.ERRO_AO_LISTAR_TODOS, entityNameGeneric);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public E getEntidadeById(K id) {
        return repositoryGeneric.findById(id)
                .orElseThrow(() -> {
                    log.warn("Entidade com ID {} nao encontrada.", id);
                    return new BusinessException(entityNameGeneric + " com id: %d nao encontrada.", id);
                });
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public F findById(K id) {
        E entity;

        if (repositoryGeneric instanceof GenericEntityDeletedRepository<?, ?>) {
            GenericEntityDeletedRepository<E, K> genericEntityDeletedRepository =
                    (GenericEntityDeletedRepository<E, K>) repositoryGeneric;
            entity = genericEntityDeletedRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> {
                        log.warn("Entidade com ID {} nao encontrada.", id);
                        return new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id);
                    });
        } else {
            entity = repositoryGeneric.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Entidade com ID {} nao encontrada.", id);
                        return new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id);
                    });
        }

        return this.toForm(entity);
    }

    @Transactional
    public E saveT(E entity) {
        return this.repositoryGeneric.save(entity);
    }

    @Transactional
    public F save(F form) {
        this.validarCreateHeSave(form);
        try {
            final E entity = this.toEntity(form);
            return this.toForm(this.repositoryGeneric.save(entity));
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Erro ao salvar: {}", entityNameGeneric + " " + ex.getMessage(), ex);
            throw new BusinessException(ExceptionConstants.ERRO_AO_SALVAR_ENTIDADE, entityNameGeneric);
        }
    }

    @Transactional
    public F update(K id, F form) {
        if (!repositoryGeneric.existsById(id)) {
            log.warn("Erro ao atualizar {} com ID {} nao encontrada.", entityNameGeneric, id);
            throw new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id);
        }

        this.validarCreateHeSave(form);

        final E existingEntity = this.repositoryGeneric.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id));

        try {
            final E entity = this.toEntity(form);
            return this.toForm(this.repositoryGeneric.save(this.getDadosUpdate(existingEntity, entity)));
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Erro ao atualizar: {}", entityNameGeneric + " " + ex.getMessage(), ex);
            throw new BusinessException(ExceptionConstants.ERRO_AO_ATUALIZAR_ENTIDADE, entityNameGeneric, id);
        }
    }

    private E getDadosUpdate(E existingEntity, E updatedEntity) {
        for (Field field : updatedEntity.getClass().getDeclaredFields()) {
            if (!field.getName().equals("id") && !field.getName().equals("updatedAt") && !field.getName().equals("createdAt")) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(updatedEntity);

                    if (value != null) {
                        field.set(existingEntity, value);
                    }
                } catch (IllegalAccessException exception) {
                    log.error(exception.toString());
                }
            }
        }

        return existingEntity;
    }

    @Transactional
    public void deleteById(K id) {
        Optional<E> entityOptional = this.repositoryGeneric.findById(id);

        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();

            if (entity instanceof IDeletedTable) {
                IDeletedTable deletedTable = (IDeletedTable) entity;
                deletedTable.setIsDeleted(true);
                this.repositoryGeneric.save(entity);
                return;
            }

            this.repositoryGeneric.deleteById(id);
            return;
        }

        throw new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id);
    }

    @Transactional
    public F activeDeactiveEntity(K id, Boolean flag) {
        Optional<E> entityOptional = this.repositoryGeneric.findById(id);

        if (entityOptional.isPresent()) {
            E entity = entityOptional.get();

            if (entity instanceof IActiveTable) {
                IActiveTable activeTable = (IActiveTable) entity;
                activeTable.setIsActive(flag);
                return this.toForm(this.repositoryGeneric.save(entity));
            }

            return null;
        }

        throw new BusinessException(ExceptionConstants.ERRO_ENTIDADE_NAO_ENCONTRADA, entityNameGeneric, id);
    }
}
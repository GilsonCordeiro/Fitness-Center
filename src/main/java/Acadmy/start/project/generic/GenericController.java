package Acadmy.start.project.generic;

import Acadmy.start.project.generic.service.GenericService;
import Acadmy.start.project.util.CustomPageable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class GenericController<E, I, G, F, K> {

    protected final GenericService<E, I, G, F, K> genericService;

    protected GenericController(GenericService<E, I, G, F, K> genericService) {
        this.genericService = genericService;
    }

    @PostMapping("/filtrar")
    public ResponseEntity<Page<G>> filtrar(@RequestBody I filtro, CustomPageable pageable) {
        return ResponseEntity.ok(genericService.filtrar(filtro, pageable.toPageable()));
    }

    public ResponseEntity<Page<G>> getAll(CustomPageable pageable) {
        return ResponseEntity.ok(this.genericService.findAll(pageable.toPageable()));
    }

    @GetMapping
    public ResponseEntity<Page<G>> getAll(@RequestParam(defaultValue = "0") int pageNumber,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "asc") String sortDirection,
                                          HttpServletRequest httpServletRequest) {
        Sort sort = Sort.by(Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return ResponseEntity.ok(this.genericService.findAll(pageable));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<F> getById(@PathVariable K id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(this.genericService.findById(id));
    }

    @PostMapping
    public ResponseEntity<F> create(@Valid @RequestBody F dto) {
        return ResponseEntity.ok(this.genericService.save(dto));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<F> update(@PathVariable K id, @Valid @RequestBody F form, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(this.genericService.update(id, form));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable K id) {
        this.genericService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id:\\d+}/active-deactive/{flag}")
    public ResponseEntity<F> toggleActiveDeactive(@PathVariable K id, @PathVariable Boolean flag) {
        return ResponseEntity.ok(this.genericService.activeDeactiveEntity(id, flag));
    }
}
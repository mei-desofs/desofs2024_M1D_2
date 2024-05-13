package backend.service;

import backend.service.dto.RoleDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.Role}.
 */
public interface RoleService {
    /**
     * Save a role.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RoleDTO> save(RoleDTO roleDTO);

    /**
     * Updates a role.
     *
     * @param roleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RoleDTO> update(RoleDTO roleDTO);

    /**
     * Partially updates a role.
     *
     * @param roleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RoleDTO> partialUpdate(RoleDTO roleDTO);

    /**
     * Get all the roles.
     *
     * @return the list of entities.
     */
    Flux<RoleDTO> findAll();

    /**
     * Get all the RoleDTO where RoleUser is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<RoleDTO> findAllWhereRoleUserIsNull();

    /**
     * Returns the number of roles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" role.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RoleDTO> findOne(Long id);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

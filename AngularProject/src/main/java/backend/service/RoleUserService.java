package backend.service;

import backend.service.dto.RoleUserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.RoleUser}.
 */
public interface RoleUserService {
    /**
     * Save a roleUser.
     *
     * @param roleUserDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RoleUserDTO> save(RoleUserDTO roleUserDTO);

    /**
     * Updates a roleUser.
     *
     * @param roleUserDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RoleUserDTO> update(RoleUserDTO roleUserDTO);

    /**
     * Partially updates a roleUser.
     *
     * @param roleUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RoleUserDTO> partialUpdate(RoleUserDTO roleUserDTO);

    /**
     * Get all the roleUsers.
     *
     * @return the list of entities.
     */
    Flux<RoleUserDTO> findAll();

    /**
     * Returns the number of roleUsers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" roleUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RoleUserDTO> findOne(Long id);

    /**
     * Delete the "id" roleUser.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

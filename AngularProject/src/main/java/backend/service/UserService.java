package backend.service;

import backend.service.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.User}.
 */
public interface UserService {
    /**
     * Save a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserDTO> save(UserDTO userDTO);

    /**
     * Updates a user.
     *
     * @param userDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserDTO> update(UserDTO userDTO);

    /**
     * Partially updates a user.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserDTO> partialUpdate(UserDTO userDTO);

    /**
     * Get all the users.
     *
     * @return the list of entities.
     */
    Flux<UserDTO> findAll();

    /**
     * Get all the UserDTO where RoleUser is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<UserDTO> findAllWhereRoleUserIsNull();

    /**
     * Returns the number of users available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserDTO> findOne(Long id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Get the user by email.
     *
     * @param email the email of the entity.
     * @return the entity.
     */
    Mono<UserDTO> findByEmail(String email);
}

package com.isep.desofs.service;

import com.isep.desofs.service.dto.RoleUserDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.isep.desofs.domain.RoleUser}.
 */
public interface RoleUserService {
    /**
     * Save a roleUser.
     *
     * @param roleUserDTO the entity to save.
     * @return the persisted entity.
     */
    RoleUserDTO save(RoleUserDTO roleUserDTO);

    /**
     * Updates a roleUser.
     *
     * @param roleUserDTO the entity to update.
     * @return the persisted entity.
     */
    RoleUserDTO update(RoleUserDTO roleUserDTO);

    /**
     * Partially updates a roleUser.
     *
     * @param roleUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoleUserDTO> partialUpdate(RoleUserDTO roleUserDTO);

    /**
     * Get all the roleUsers.
     *
     * @return the list of entities.
     */
    List<RoleUserDTO> findAll();

    /**
     * Get the "id" roleUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleUserDTO> findOne(Long id);

    /**
     * Delete the "id" roleUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

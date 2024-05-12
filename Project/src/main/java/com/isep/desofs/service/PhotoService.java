package com.isep.desofs.service;

import com.isep.desofs.service.dto.PhotoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.isep.desofs.domain.Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    PhotoDTO save(PhotoDTO photoDTO);

    /**
     * Updates a photo.
     *
     * @param photoDTO the entity to update.
     * @return the persisted entity.
     */
    PhotoDTO update(PhotoDTO photoDTO);

    /**
     * Partially updates a photo.
     *
     * @param photoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PhotoDTO> partialUpdate(PhotoDTO photoDTO);

    /**
     * Get all the photos.
     *
     * @return the list of entities.
     */
    List<PhotoDTO> findAll();

    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PhotoDTO> findOne(Long id);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

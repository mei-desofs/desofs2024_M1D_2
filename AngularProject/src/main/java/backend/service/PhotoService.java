package backend.service;

import backend.service.dto.PhotoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PhotoDTO> save(PhotoDTO photoDTO);

    /**
     * Updates a photo.
     *
     * @param photoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PhotoDTO> update(PhotoDTO photoDTO);

    /**
     * Partially updates a photo.
     *
     * @param photoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PhotoDTO> partialUpdate(PhotoDTO photoDTO);

    /**
     * Get all the photos.
     *
     * @return the list of entities.
     */
    Flux<PhotoDTO> findAll();

    /**
     * Returns the number of photos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PhotoDTO> findOne(Long id);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

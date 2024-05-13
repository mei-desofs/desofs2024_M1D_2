package backend.service;

import backend.service.dto.PortfolioDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.Portfolio}.
 */
public interface PortfolioService {
    /**
     * Save a portfolio.
     *
     * @param portfolioDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PortfolioDTO> save(PortfolioDTO portfolioDTO);

    /**
     * Updates a portfolio.
     *
     * @param portfolioDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PortfolioDTO> update(PortfolioDTO portfolioDTO);

    /**
     * Partially updates a portfolio.
     *
     * @param portfolioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PortfolioDTO> partialUpdate(PortfolioDTO portfolioDTO);

    /**
     * Get all the portfolios.
     *
     * @return the list of entities.
     */
    Flux<PortfolioDTO> findAll();

    /**
     * Returns the number of portfolios available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" portfolio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PortfolioDTO> findOne(Long id);

    /**
     * Delete the "id" portfolio.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

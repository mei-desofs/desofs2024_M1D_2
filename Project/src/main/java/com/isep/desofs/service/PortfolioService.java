package com.isep.desofs.service;

import com.isep.desofs.service.dto.PortfolioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.isep.desofs.domain.Portfolio}.
 */
public interface PortfolioService {
    /**
     * Save a portfolio.
     *
     * @param portfolioDTO the entity to save.
     * @return the persisted entity.
     */
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    /**
     * Updates a portfolio.
     *
     * @param portfolioDTO the entity to update.
     * @return the persisted entity.
     */
    PortfolioDTO update(PortfolioDTO portfolioDTO);

    /**
     * Partially updates a portfolio.
     *
     * @param portfolioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PortfolioDTO> partialUpdate(PortfolioDTO portfolioDTO);

    /**
     * Get all the portfolios.
     *
     * @return the list of entities.
     */
    List<PortfolioDTO> findAll();

    /**
     * Get the "id" portfolio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PortfolioDTO> findOne(Long id);

    /**
     * Delete the "id" portfolio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

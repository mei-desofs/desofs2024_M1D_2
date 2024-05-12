package com.isep.desofs.web.rest;

import com.isep.desofs.repository.PortfolioRepository;
import com.isep.desofs.service.PortfolioService;
import com.isep.desofs.service.dto.PortfolioDTO;
import com.isep.desofs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.isep.desofs.domain.Portfolio}.
 */
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioResource {

    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

    private static final String ENTITY_NAME = "portfolio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortfolioService portfolioService;

    private final PortfolioRepository portfolioRepository;

    public PortfolioResource(PortfolioService portfolioService, PortfolioRepository portfolioRepository) {
        this.portfolioService = portfolioService;
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * {@code POST  /portfolios} : Create a new portfolio.
     *
     * @param portfolioDTO the portfolioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portfolioDTO, or with status {@code 400 (Bad Request)} if the portfolio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PortfolioDTO> createPortfolio(@RequestBody PortfolioDTO portfolioDTO) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolioDTO);
        if (portfolioDTO.getId() != null) {
            throw new BadRequestAlertException("A new portfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        portfolioDTO = portfolioService.save(portfolioDTO);
        return ResponseEntity.created(new URI("/api/portfolios/" + portfolioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, portfolioDTO.getId().toString()))
            .body(portfolioDTO);
    }

    /**
     * {@code PUT  /portfolios/:id} : Updates an existing portfolio.
     *
     * @param id the id of the portfolioDTO to save.
     * @param portfolioDTO the portfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioDTO,
     * or with status {@code 400 (Bad Request)} if the portfolioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PortfolioDTO portfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}, {}", id, portfolioDTO);
        if (portfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        portfolioDTO = portfolioService.update(portfolioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portfolioDTO.getId().toString()))
            .body(portfolioDTO);
    }

    /**
     * {@code PATCH  /portfolios/:id} : Partial updates given fields of an existing portfolio, field will ignore if it is null
     *
     * @param id the id of the portfolioDTO to save.
     * @param portfolioDTO the portfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioDTO,
     * or with status {@code 400 (Bad Request)} if the portfolioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the portfolioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the portfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PortfolioDTO> partialUpdatePortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PortfolioDTO portfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Portfolio partially : {}, {}", id, portfolioDTO);
        if (portfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PortfolioDTO> result = portfolioService.partialUpdate(portfolioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portfolioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /portfolios} : get all the portfolios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portfolios in body.
     */
    @GetMapping("")
    public List<PortfolioDTO> getAllPortfolios() {
        log.debug("REST request to get all Portfolios");
        return portfolioService.findAll();
    }

    /**
     * {@code GET  /portfolios/:id} : get the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portfolioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable("id") Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        Optional<PortfolioDTO> portfolioDTO = portfolioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(portfolioDTO);
    }

    /**
     * {@code DELETE  /portfolios/:id} : delete the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable("id") Long id) {
        log.debug("REST request to delete Portfolio : {}", id);
        portfolioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

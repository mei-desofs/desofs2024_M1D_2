package backend.web.rest;

import backend.repository.ReceiptRepository;
import backend.service.ReceiptService;
import backend.service.dto.ReceiptDTO;
import backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link backend.domain.Receipt}.
 */
@RestController
@RequestMapping("/api/receipts")
public class ReceiptResource {

    private final Logger log = LoggerFactory.getLogger(ReceiptResource.class);

    private static final String ENTITY_NAME = "receipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiptService receiptService;

    private final ReceiptRepository receiptRepository;

    public ReceiptResource(ReceiptService receiptService, ReceiptRepository receiptRepository) {
        this.receiptService = receiptService;
        this.receiptRepository = receiptRepository;
    }

    /**
     * {@code POST  /receipts} : Create a new receipt.
     *
     * @param receiptDTO the receiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiptDTO, or with status {@code 400 (Bad Request)} if the receipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReceiptDTO>> createReceipt(@RequestBody ReceiptDTO receiptDTO) throws URISyntaxException {
        log.debug("REST request to save Receipt : {}", receiptDTO);
        if (receiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new receipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return receiptService
            .save(receiptDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/receipts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /receipts/:id} : Updates an existing receipt.
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ReceiptDTO>> updateReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Receipt : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return receiptRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return receiptService
                    .update(receiptDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /receipts/:id} : Partial updates given fields of an existing receipt, field will ignore if it is null
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receiptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReceiptDTO>> partialUpdateReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Receipt partially : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return receiptRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReceiptDTO> result = receiptService.partialUpdate(receiptDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /receipts} : get all the receipts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receipts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReceiptDTO>> getAllReceipts(@RequestParam(name = "filter", required = false) String filter) {
        if ("payment-is-null".equals(filter)) {
            log.debug("REST request to get all Receipts where payment is null");
            return receiptService.findAllWherePaymentIsNull().collectList();
        }
        log.debug("REST request to get all Receipts");
        return receiptService.findAll().collectList();
    }

    /**
     * {@code GET  /receipts} : get all the receipts as a stream.
     * @return the {@link Flux} of receipts.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReceiptDTO> getAllReceiptsAsStream() {
        log.debug("REST request to get all Receipts as a stream");
        return receiptService.findAll();
    }

    /**
     * {@code GET  /receipts/:id} : get the "id" receipt.
     *
     * @param id the id of the receiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReceiptDTO>> getReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to get Receipt : {}", id);
        Mono<ReceiptDTO> receiptDTO = receiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receiptDTO);
    }

    /**
     * {@code DELETE  /receipts/:id} : delete the "id" receipt.
     *
     * @param id the id of the receiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to delete Receipt : {}", id);
        return receiptService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

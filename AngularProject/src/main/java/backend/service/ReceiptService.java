package backend.service;

import backend.service.dto.ReceiptDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.Receipt}.
 */
public interface ReceiptService {
    /**
     * Save a receipt.
     *
     * @param receiptDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ReceiptDTO> save(ReceiptDTO receiptDTO);

    /**
     * Updates a receipt.
     *
     * @param receiptDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ReceiptDTO> update(ReceiptDTO receiptDTO);

    /**
     * Partially updates a receipt.
     *
     * @param receiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO);

    /**
     * Get all the receipts.
     *
     * @return the list of entities.
     */
    Flux<ReceiptDTO> findAll();

    /**
     * Get all the ReceiptDTO where Payment is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<ReceiptDTO> findAllWherePaymentIsNull();

    /**
     * Returns the number of receipts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" receipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ReceiptDTO> findOne(Long id);

    /**
     * Delete the "id" receipt.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

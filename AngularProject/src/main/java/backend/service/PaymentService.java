package backend.service;

import backend.service.dto.PaymentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link backend.domain.Payment}.
 */
public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentDTO> save(PaymentDTO paymentDTO);

    /**
     * Updates a payment.
     *
     * @param paymentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentDTO> update(PaymentDTO paymentDTO);

    /**
     * Partially updates a payment.
     *
     * @param paymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentDTO> partialUpdate(PaymentDTO paymentDTO);

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    Flux<PaymentDTO> findAll();

    /**
     * Get all the PaymentDTO where Cart is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentDTO> findAllWhereCartIsNull();

    /**
     * Returns the number of payments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentDTO> findOne(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}

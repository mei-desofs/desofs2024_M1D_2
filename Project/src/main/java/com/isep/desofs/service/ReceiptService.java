package com.isep.desofs.service;

import com.isep.desofs.service.dto.ReceiptDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.isep.desofs.domain.Receipt}.
 */
public interface ReceiptService {
    /**
     * Save a receipt.
     *
     * @param receiptDTO the entity to save.
     * @return the persisted entity.
     */
    ReceiptDTO save(ReceiptDTO receiptDTO);

    /**
     * Updates a receipt.
     *
     * @param receiptDTO the entity to update.
     * @return the persisted entity.
     */
    ReceiptDTO update(ReceiptDTO receiptDTO);

    /**
     * Partially updates a receipt.
     *
     * @param receiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO);

    /**
     * Get all the receipts.
     *
     * @return the list of entities.
     */
    List<ReceiptDTO> findAll();

    /**
     * Get all the ReceiptDTO where Payment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ReceiptDTO> findAllWherePaymentIsNull();

    /**
     * Get the "id" receipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReceiptDTO> findOne(Long id);

    /**
     * Delete the "id" receipt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.isep.desofs.service.impl;

import com.isep.desofs.domain.Receipt;
import com.isep.desofs.repository.ReceiptRepository;
import com.isep.desofs.service.ReceiptService;
import com.isep.desofs.service.dto.ReceiptDTO;
import com.isep.desofs.service.mapper.ReceiptMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.isep.desofs.domain.Receipt}.
 */
@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

    private final Logger log = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        log.debug("Request to save Receipt : {}", receiptDTO);
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(receipt);
    }

    @Override
    public ReceiptDTO update(ReceiptDTO receiptDTO) {
        log.debug("Request to update Receipt : {}", receiptDTO);
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(receipt);
    }

    @Override
    public Optional<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO) {
        log.debug("Request to partially update Receipt : {}", receiptDTO);

        return receiptRepository
            .findById(receiptDTO.getId())
            .map(existingReceipt -> {
                receiptMapper.partialUpdate(existingReceipt, receiptDTO);

                return existingReceipt;
            })
            .map(receiptRepository::save)
            .map(receiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDTO> findAll() {
        log.debug("Request to get all Receipts");
        return receiptRepository.findAll().stream().map(receiptMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the receipts where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReceiptDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all receipts where Payment is null");
        return StreamSupport.stream(receiptRepository.findAll().spliterator(), false)
            .filter(receipt -> receipt.getPayment() == null)
            .map(receiptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReceiptDTO> findOne(Long id) {
        log.debug("Request to get Receipt : {}", id);
        return receiptRepository.findById(id).map(receiptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Receipt : {}", id);
        receiptRepository.deleteById(id);
    }
}

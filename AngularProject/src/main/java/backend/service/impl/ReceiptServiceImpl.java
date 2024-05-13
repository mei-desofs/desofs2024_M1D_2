package backend.service.impl;

import backend.repository.ReceiptRepository;
import backend.service.ReceiptService;
import backend.service.dto.ReceiptDTO;
import backend.service.mapper.ReceiptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Receipt}.
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
    public Mono<ReceiptDTO> save(ReceiptDTO receiptDTO) {
        log.debug("Request to save Receipt : {}", receiptDTO);
        return receiptRepository.save(receiptMapper.toEntity(receiptDTO)).map(receiptMapper::toDto);
    }

    @Override
    public Mono<ReceiptDTO> update(ReceiptDTO receiptDTO) {
        log.debug("Request to update Receipt : {}", receiptDTO);
        return receiptRepository.save(receiptMapper.toEntity(receiptDTO)).map(receiptMapper::toDto);
    }

    @Override
    public Mono<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO) {
        log.debug("Request to partially update Receipt : {}", receiptDTO);

        return receiptRepository
            .findById(receiptDTO.getId())
            .map(existingReceipt -> {
                receiptMapper.partialUpdate(existingReceipt, receiptDTO);

                return existingReceipt;
            })
            .flatMap(receiptRepository::save)
            .map(receiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ReceiptDTO> findAll() {
        log.debug("Request to get all Receipts");
        return receiptRepository.findAll().map(receiptMapper::toDto);
    }

    /**
     *  Get all the receipts where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReceiptDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all receipts where Payment is null");
        return receiptRepository.findAllWherePaymentIsNull().map(receiptMapper::toDto);
    }

    public Mono<Long> countAll() {
        return receiptRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ReceiptDTO> findOne(Long id) {
        log.debug("Request to get Receipt : {}", id);
        return receiptRepository.findById(id).map(receiptMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Receipt : {}", id);
        return receiptRepository.deleteById(id);
    }
}

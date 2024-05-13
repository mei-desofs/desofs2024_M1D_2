package backend.service.impl;

import backend.repository.PaymentRepository;
import backend.service.PaymentService;
import backend.service.dto.PaymentDTO;
import backend.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Mono<PaymentDTO> save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        return paymentRepository.save(paymentMapper.toEntity(paymentDTO)).map(paymentMapper::toDto);
    }

    @Override
    public Mono<PaymentDTO> update(PaymentDTO paymentDTO) {
        log.debug("Request to update Payment : {}", paymentDTO);
        return paymentRepository.save(paymentMapper.toEntity(paymentDTO)).map(paymentMapper::toDto);
    }

    @Override
    public Mono<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        log.debug("Request to partially update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);

                return existingPayment;
            })
            .flatMap(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentDTO> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll().map(paymentMapper::toDto);
    }

    /**
     *  Get all the payments where Cart is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentDTO> findAllWhereCartIsNull() {
        log.debug("Request to get all payments where Cart is null");
        return paymentRepository.findAllWhereCartIsNull().map(paymentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        return paymentRepository.deleteById(id);
    }
}

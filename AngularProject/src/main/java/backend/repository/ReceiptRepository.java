package backend.repository;

import backend.domain.Receipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Receipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceiptRepository extends ReactiveCrudRepository<Receipt, Long>, ReceiptRepositoryInternal {
    @Query("SELECT * FROM receipt entity WHERE entity.id not in (select payment_id from payment)")
    Flux<Receipt> findAllWherePaymentIsNull();

    @Override
    <S extends Receipt> Mono<S> save(S entity);

    @Override
    Flux<Receipt> findAll();

    @Override
    Mono<Receipt> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ReceiptRepositoryInternal {
    <S extends Receipt> Mono<S> save(S entity);

    Flux<Receipt> findAllBy(Pageable pageable);

    Flux<Receipt> findAll();

    Mono<Receipt> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Receipt> findAllBy(Pageable pageable, Criteria criteria);
}

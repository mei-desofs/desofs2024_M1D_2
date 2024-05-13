package backend.repository;

import backend.domain.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends ReactiveCrudRepository<Photo, Long>, PhotoRepositoryInternal {
    @Query("SELECT * FROM photo entity WHERE entity.portfolio_id = :id")
    Flux<Photo> findByPortfolio(Long id);

    @Query("SELECT * FROM photo entity WHERE entity.portfolio_id IS NULL")
    Flux<Photo> findAllWherePortfolioIsNull();

    @Query("SELECT * FROM photo entity WHERE entity.cart_id = :id")
    Flux<Photo> findByCart(Long id);

    @Query("SELECT * FROM photo entity WHERE entity.cart_id IS NULL")
    Flux<Photo> findAllWhereCartIsNull();

    @Override
    <S extends Photo> Mono<S> save(S entity);

    @Override
    Flux<Photo> findAll();

    @Override
    Mono<Photo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PhotoRepositoryInternal {
    <S extends Photo> Mono<S> save(S entity);

    Flux<Photo> findAllBy(Pageable pageable);

    Flux<Photo> findAll();

    Mono<Photo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Photo> findAllBy(Pageable pageable, Criteria criteria);
}

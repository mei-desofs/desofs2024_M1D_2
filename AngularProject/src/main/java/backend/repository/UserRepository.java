package backend.repository;

import backend.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long>, UserRepositoryInternal {
    @Query("SELECT * FROM user entity WHERE entity.portfolio_id = :id")
    Flux<User> findByPortfolio(Long id);

    @Query("SELECT * FROM user entity WHERE entity.portfolio_id IS NULL")
    Flux<User> findAllWherePortfolioIsNull();

    @Query("SELECT * FROM user entity WHERE entity.id not in (select role_user_id from role_user)")
    Flux<User> findAllWhereRoleUserIsNull();

    @Query("SELECT * FROM user entity WHERE entity.email = :email")
    Mono<User> findByEmail(String email);

    @Override
    <S extends User> Mono<S> save(S entity);

    @Override
    Flux<User> findAll();

    @Override
    Mono<User> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserRepositoryInternal {
    <S extends User> Mono<S> save(S entity);

    Flux<User> findAllBy(Pageable pageable);

    Flux<User> findAll();

    Mono<User> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<User> findAllBy(Pageable pageable, Criteria criteria);
}

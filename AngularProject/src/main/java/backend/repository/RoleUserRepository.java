package backend.repository;

import backend.domain.RoleUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RoleUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleUserRepository extends ReactiveCrudRepository<RoleUser, Long>, RoleUserRepositoryInternal {
    @Query("SELECT * FROM role_user entity WHERE entity.user_id_id = :id")
    Flux<RoleUser> findByUserId(Long id);

    @Query("SELECT * FROM role_user entity WHERE entity.user_id_id IS NULL")
    Flux<RoleUser> findAllWhereUserIdIsNull();

    @Query("SELECT * FROM role_user entity WHERE entity.role_id_id = :id")
    Flux<RoleUser> findByRoleId(Long id);

    @Query("SELECT * FROM role_user entity WHERE entity.role_id_id IS NULL")
    Flux<RoleUser> findAllWhereRoleIdIsNull();

    @Override
    <S extends RoleUser> Mono<S> save(S entity);

    @Override
    Flux<RoleUser> findAll();

    @Override
    Mono<RoleUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RoleUserRepositoryInternal {
    <S extends RoleUser> Mono<S> save(S entity);

    Flux<RoleUser> findAllBy(Pageable pageable);

    Flux<RoleUser> findAll();

    Mono<RoleUser> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RoleUser> findAllBy(Pageable pageable, Criteria criteria);
}

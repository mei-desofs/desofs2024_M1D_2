package backend.repository;

import backend.domain.User;
import backend.repository.rowmapper.PortfolioRowMapper;
import backend.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the User entity.
 */
@SuppressWarnings("unused")
class UserRepositoryInternalImpl extends SimpleR2dbcRepository<User, Long> implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PortfolioRowMapper portfolioMapper;
    private final UserRowMapper userMapper;

    private static final Table entityTable = Table.aliased("user", EntityManager.ENTITY_ALIAS);
    private static final Table portfolioTable = Table.aliased("portfolio", "portfolio");

    public UserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PortfolioRowMapper portfolioMapper,
        UserRowMapper userMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(User.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.portfolioMapper = portfolioMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Flux<User> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<User> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PortfolioSqlHelper.getColumns(portfolioTable, "portfolio"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(portfolioTable)
            .on(Column.create("portfolio_id", entityTable))
            .equals(Column.create("id", portfolioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, User.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<User> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<User> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private User process(Row row, RowMetadata metadata) {
        User entity = userMapper.apply(row, "e");
        entity.setPortfolio(portfolioMapper.apply(row, "portfolio"));
        return entity;
    }

    @Override
    public <S extends User> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

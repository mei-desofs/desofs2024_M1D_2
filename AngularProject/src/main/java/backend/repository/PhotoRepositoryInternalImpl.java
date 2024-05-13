package backend.repository;

import backend.domain.Photo;
import backend.repository.rowmapper.CartRowMapper;
import backend.repository.rowmapper.PhotoRowMapper;
import backend.repository.rowmapper.PortfolioRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Photo entity.
 */
@SuppressWarnings("unused")
class PhotoRepositoryInternalImpl extends SimpleR2dbcRepository<Photo, Long> implements PhotoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PortfolioRowMapper portfolioMapper;
    private final CartRowMapper cartMapper;
    private final PhotoRowMapper photoMapper;

    private static final Table entityTable = Table.aliased("photo", EntityManager.ENTITY_ALIAS);
    private static final Table portfolioTable = Table.aliased("portfolio", "portfolio");
    private static final Table cartTable = Table.aliased("cart", "cart");

    public PhotoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PortfolioRowMapper portfolioMapper,
        CartRowMapper cartMapper,
        PhotoRowMapper photoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Photo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.portfolioMapper = portfolioMapper;
        this.cartMapper = cartMapper;
        this.photoMapper = photoMapper;
    }

    @Override
    public Flux<Photo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Photo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PhotoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PortfolioSqlHelper.getColumns(portfolioTable, "portfolio"));
        columns.addAll(CartSqlHelper.getColumns(cartTable, "cart"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(portfolioTable)
            .on(Column.create("portfolio_id", entityTable))
            .equals(Column.create("id", portfolioTable))
            .leftOuterJoin(cartTable)
            .on(Column.create("cart_id", entityTable))
            .equals(Column.create("id", cartTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Photo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Photo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Photo> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Photo process(Row row, RowMetadata metadata) {
        Photo entity = photoMapper.apply(row, "e");
        entity.setPortfolio(portfolioMapper.apply(row, "portfolio"));
        entity.setCart(cartMapper.apply(row, "cart"));
        return entity;
    }

    @Override
    public <S extends Photo> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

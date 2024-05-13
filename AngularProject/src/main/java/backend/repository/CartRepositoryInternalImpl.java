package backend.repository;

import backend.domain.Cart;
import backend.repository.rowmapper.CartRowMapper;
import backend.repository.rowmapper.PaymentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Cart entity.
 */
@SuppressWarnings("unused")
class CartRepositoryInternalImpl extends SimpleR2dbcRepository<Cart, Long> implements CartRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentRowMapper paymentMapper;
    private final CartRowMapper cartMapper;

    private static final Table entityTable = Table.aliased("cart", EntityManager.ENTITY_ALIAS);
    private static final Table paymentTable = Table.aliased("payment", "payment");

    public CartRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentRowMapper paymentMapper,
        CartRowMapper cartMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Cart.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymentMapper = paymentMapper;
        this.cartMapper = cartMapper;
    }

    @Override
    public Flux<Cart> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Cart> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CartSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentSqlHelper.getColumns(paymentTable, "payment"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(paymentTable)
            .on(Column.create("payment_id", entityTable))
            .equals(Column.create("id", paymentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Cart.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Cart> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Cart> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Cart process(Row row, RowMetadata metadata) {
        Cart entity = cartMapper.apply(row, "e");
        entity.setPayment(paymentMapper.apply(row, "payment"));
        return entity;
    }

    @Override
    public <S extends Cart> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

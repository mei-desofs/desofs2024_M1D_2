package backend.repository;

import backend.domain.Payment;
import backend.repository.rowmapper.PaymentRowMapper;
import backend.repository.rowmapper.ReceiptRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Payment entity.
 */
@SuppressWarnings("unused")
class PaymentRepositoryInternalImpl extends SimpleR2dbcRepository<Payment, Long> implements PaymentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ReceiptRowMapper receiptMapper;
    private final PaymentRowMapper paymentMapper;

    private static final Table entityTable = Table.aliased("payment", EntityManager.ENTITY_ALIAS);
    private static final Table receiptTable = Table.aliased("receipt", "receipt");

    public PaymentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ReceiptRowMapper receiptMapper,
        PaymentRowMapper paymentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Payment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.receiptMapper = receiptMapper;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Flux<Payment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Payment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PaymentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ReceiptSqlHelper.getColumns(receiptTable, "receipt"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(receiptTable)
            .on(Column.create("receipt_id", entityTable))
            .equals(Column.create("id", receiptTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Payment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Payment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Payment> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Payment process(Row row, RowMetadata metadata) {
        Payment entity = paymentMapper.apply(row, "e");
        entity.setReceipt(receiptMapper.apply(row, "receipt"));
        return entity;
    }

    @Override
    public <S extends Payment> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

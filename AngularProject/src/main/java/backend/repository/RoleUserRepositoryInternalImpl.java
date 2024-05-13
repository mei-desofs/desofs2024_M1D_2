package backend.repository;

import backend.domain.RoleUser;
import backend.repository.rowmapper.RoleRowMapper;
import backend.repository.rowmapper.RoleUserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the RoleUser entity.
 */
@SuppressWarnings("unused")
class RoleUserRepositoryInternalImpl extends SimpleR2dbcRepository<RoleUser, Long> implements RoleUserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final RoleRowMapper roleMapper;
    private final RoleUserRowMapper roleuserMapper;

    private static final Table entityTable = Table.aliased("role_user", EntityManager.ENTITY_ALIAS);
    private static final Table userIdTable = Table.aliased("user", "userId");
    private static final Table roleIdTable = Table.aliased("role", "roleId");

    public RoleUserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        RoleRowMapper roleMapper,
        RoleUserRowMapper roleuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RoleUser.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.roleuserMapper = roleuserMapper;
    }

    @Override
    public Flux<RoleUser> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RoleUser> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RoleUserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userIdTable, "userId"));
        columns.addAll(RoleSqlHelper.getColumns(roleIdTable, "roleId"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userIdTable)
            .on(Column.create("user_id_id", entityTable))
            .equals(Column.create("id", userIdTable))
            .leftOuterJoin(roleIdTable)
            .on(Column.create("role_id_id", entityTable))
            .equals(Column.create("id", roleIdTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RoleUser.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RoleUser> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RoleUser> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private RoleUser process(Row row, RowMetadata metadata) {
        RoleUser entity = roleuserMapper.apply(row, "e");
        entity.setUserId(userMapper.apply(row, "userId"));
        entity.setRoleId(roleMapper.apply(row, "roleId"));
        return entity;
    }

    @Override
    public <S extends RoleUser> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

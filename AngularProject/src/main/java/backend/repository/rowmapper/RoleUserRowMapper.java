package backend.repository.rowmapper;

import backend.domain.RoleUser;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RoleUser}, with proper type conversions.
 */
@Service
public class RoleUserRowMapper implements BiFunction<Row, String, RoleUser> {

    private final ColumnConverter converter;

    public RoleUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RoleUser} stored in the database.
     */
    @Override
    public RoleUser apply(Row row, String prefix) {
        RoleUser entity = new RoleUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUserIdId(converter.fromRow(row, prefix + "_user_id_id", Long.class));
        entity.setRoleIdId(converter.fromRow(row, prefix + "_role_id_id", Long.class));
        return entity;
    }
}

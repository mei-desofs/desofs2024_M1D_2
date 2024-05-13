package backend.repository.rowmapper;

import backend.domain.Role;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Role}, with proper type conversions.
 */
@Service
public class RoleRowMapper implements BiFunction<Row, String, Role> {

    private final ColumnConverter converter;

    public RoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Role} stored in the database.
     */
    @Override
    public Role apply(Row row, String prefix) {
        Role entity = new Role();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameRole(converter.fromRow(row, prefix + "_name_role", String.class));
        return entity;
    }
}

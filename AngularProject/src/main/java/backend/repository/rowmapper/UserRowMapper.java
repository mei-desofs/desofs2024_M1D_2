package backend.repository.rowmapper;

import backend.domain.User;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link User}, with proper type conversions.
 */
@Service
public class UserRowMapper implements BiFunction<Row, String, User> {

    private final ColumnConverter converter;

    public UserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link User} stored in the database.
     */
    @Override
    public User apply(Row row, String prefix) {
        User entity = new User();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setContact(converter.fromRow(row, prefix + "_contact", String.class));
        entity.setProfilePhotoContentType(converter.fromRow(row, prefix + "_profile_photo_content_type", String.class));
        entity.setProfilePhoto(converter.fromRow(row, prefix + "_profile_photo", byte[].class));
        entity.setPortfolioId(converter.fromRow(row, prefix + "_portfolio_id", Long.class));
        return entity;
    }
}

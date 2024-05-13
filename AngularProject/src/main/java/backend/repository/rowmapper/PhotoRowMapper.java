package backend.repository.rowmapper;

import backend.domain.Photo;
import backend.domain.enumeration.PhotoState;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Photo}, with proper type conversions.
 */
@Service
public class PhotoRowMapper implements BiFunction<Row, String, Photo> {

    private final ColumnConverter converter;

    public PhotoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Photo} stored in the database.
     */
    @Override
    public Photo apply(Row row, String prefix) {
        Photo entity = new Photo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPhotoContentType(converter.fromRow(row, prefix + "_photo_content_type", String.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", byte[].class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setState(converter.fromRow(row, prefix + "_state", PhotoState.class));
        entity.setPortfolioId(converter.fromRow(row, prefix + "_portfolio_id", Long.class));
        entity.setCartId(converter.fromRow(row, prefix + "_cart_id", Long.class));
        return entity;
    }
}

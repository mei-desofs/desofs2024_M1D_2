package backend.repository.rowmapper;

import backend.domain.Receipt;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Receipt}, with proper type conversions.
 */
@Service
public class ReceiptRowMapper implements BiFunction<Row, String, Receipt> {

    private final ColumnConverter converter;

    public ReceiptRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Receipt} stored in the database.
     */
    @Override
    public Receipt apply(Row row, String prefix) {
        Receipt entity = new Receipt();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdPayment(converter.fromRow(row, prefix + "_id_payment", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}

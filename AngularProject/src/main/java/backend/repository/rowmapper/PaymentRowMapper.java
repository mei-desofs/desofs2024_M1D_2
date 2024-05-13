package backend.repository.rowmapper;

import backend.domain.Payment;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Payment}, with proper type conversions.
 */
@Service
public class PaymentRowMapper implements BiFunction<Row, String, Payment> {

    private final ColumnConverter converter;

    public PaymentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Payment} stored in the database.
     */
    @Override
    public Payment apply(Row row, String prefix) {
        Payment entity = new Payment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdCart(converter.fromRow(row, prefix + "_id_cart", Long.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setReceiptId(converter.fromRow(row, prefix + "_receipt_id", Long.class));
        return entity;
    }
}

package backend.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PaymentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_cart", table, columnPrefix + "_id_cart"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));

        columns.add(Column.aliased("receipt_id", table, columnPrefix + "_receipt_id"));
        return columns;
    }
}

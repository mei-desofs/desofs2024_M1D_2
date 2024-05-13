package backend.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PhotoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("photo", table, columnPrefix + "_photo"));
        columns.add(Column.aliased("photo_content_type", table, columnPrefix + "_photo_content_type"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("state", table, columnPrefix + "_state"));

        columns.add(Column.aliased("portfolio_id", table, columnPrefix + "_portfolio_id"));
        columns.add(Column.aliased("cart_id", table, columnPrefix + "_cart_id"));
        return columns;
    }
}

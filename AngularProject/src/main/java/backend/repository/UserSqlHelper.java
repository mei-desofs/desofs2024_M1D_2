package backend.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("password", table, columnPrefix + "_password"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));
        columns.add(Column.aliased("contact", table, columnPrefix + "_contact"));
        columns.add(Column.aliased("profile_photo", table, columnPrefix + "_profile_photo"));
        columns.add(Column.aliased("profile_photo_content_type", table, columnPrefix + "_profile_photo_content_type"));

        columns.add(Column.aliased("portfolio_id", table, columnPrefix + "_portfolio_id"));
        return columns;
    }
}

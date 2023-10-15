package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DatabaseMysql {
    private static final DatabaseMysql INSTANCE = new DatabaseMysql();
    private final Connection connection;

    private DatabaseMysql() {
        ResourceBundle resource = ResourceBundle.getBundle("database_mysql");
        String dbUrl = resource.getString("db.url");
        String dbName = resource.getString("db.name");
        String dbUser = resource.getString("db.user");
        String dbPassword = resource.getString("db.password");

        try {
            connection = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseMysql getInstance(){
        return INSTANCE;
    }

    public Connection getConnection(){
        return this.connection;
    }
}

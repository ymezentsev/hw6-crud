package storage;

import org.flywaydb.core.Flyway;

import java.util.ResourceBundle;

public class DatabaseInitService {
    public void initDbMysql (){
        ResourceBundle resource = ResourceBundle.getBundle("database_mysql");
        String dbUrl = resource.getString("db.url");
        String dbName = resource.getString("db.name");
        String dbUser = resource.getString("db.user");
        String dbPassword = resource.getString("db.password");

        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl + dbName, dbUser, dbPassword)
                .load();
        flyway.migrate();
    }

    public void initDbH2 (String connectionUrl){
        Flyway flyway = Flyway.configure()
                .dataSource(connectionUrl, null, null)
                .load();
        flyway.migrate();
    }
}

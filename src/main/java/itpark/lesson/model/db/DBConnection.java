package itpark.lesson.model.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;
    private Logger logger = LoggerFactory.getLogger(DBConnection.class);

    private DBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://83.147.246.87:5432/demo_db_vanya", "demo_db_vanya_user", "12345");
        } catch (Exception e) {
            logger.error("Произошла ошибка при подключении к БД");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private static DBConnection instance = null;

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public void executeQuery() {
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement("select username, title from users u join country c on u.country_id = c.id").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(resultSet);
    }

}
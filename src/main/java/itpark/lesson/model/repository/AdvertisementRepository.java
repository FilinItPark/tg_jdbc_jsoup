package itpark.lesson.model.repository;

import itpark.lesson.model.entity.Advertisement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class AdvertisementRepository {
    private Connection connection;

    public AdvertisementRepository(Connection connection) {
        this.connection = connection;
    }

/*    public List<Advertisement> findAll() throws SQLException {
        List<Advertisement> countries = new ArrayList<>();

        Statement statement = connection.createStatement();
        String sql = "select * from country";

        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            int population = resultSet.getInt("population");

            var country = new Advertisement(id, title, population);
            countries.add(country);
        }

        resultSet.close();
        statement.close();

        return countries;
    }*/

    public void saveAll(List<Advertisement> advertisements) {
        try {
            Statement statement = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("insert into advertisement (title, description, metro_station, uri, region, price) values ");

            for (var advertisement : advertisements) {
                sql.append("(");
                sql.append(advertisement.toString());
                sql.append("),");
            }
            sql.deleteCharAt(sql.length() - 1);

            if (!statement.execute(sql.toString())) {
                System.out.println("чет пошло не так");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Advertisement> getAll() {
        List<Advertisement> advertisementList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "select * from advertisement";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var metroStation = resultSet.getString("metrostation");
                var uri = resultSet.getString("uri");
                var region = resultSet.getString("region");
                var price = resultSet.getString("price");

                var adv = Advertisement.builder().id(id).title(title).description(description).metroStation(metroStation).uri(uri).region(region).price(price).build();
                advertisementList.add(adv);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return advertisementList;
    }
}

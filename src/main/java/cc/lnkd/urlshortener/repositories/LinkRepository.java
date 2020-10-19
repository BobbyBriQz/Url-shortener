package cc.lnkd.urlshortener.repositories;


import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.models.response.LinkResponse;

import java.sql.*;


public class LinkRepository {

    Connection connection;

    public LinkRepository(DBConfig dbConfig){

        //DBConfig is used to get the configuration from the application.properties file
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lnkd_db?"+"user="+dbConfig.getUsername()+"&password="+dbConfig.getPassword()+"&serverTimezone=UTC");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public LinkResponse retrieveURLFromDB(String slug) throws SQLException {

        try {

            String updateQuery = "UPDATE links set visit_count = visit_count + 1 WHERE `slug` = ?";

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, slug);
            int update = updateStatement.executeUpdate();

            String selectQuery = "SELECT id, slug, url, visit_count FROM links WHERE `slug` = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, slug);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int linkId = resultSet.getInt("id");
                String slugCreated = resultSet.getString("slug");
                String url = resultSet.getString("url");
                int visitCount = resultSet.getInt("visit_count");

                resultSet.close();
                connection.close();
                return new LinkResponse(linkId, slugCreated, url, visitCount);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {

            connection.close();
        }


        return null;
    }

    public LinkResponse retrieveURLFromDB(int id) throws SQLException {
        String selectQuery = "SELECT id, slug, url, visit_count FROM links WHERE `id` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            int linkId = resultSet.getInt("id");
            String shortUrl = "lnkd.cc/" + resultSet.getString("slug");
            String url = resultSet.getString("url");
            int visitCount = resultSet.getInt("visit_count");

            resultSet.close();
            connection.close();
            return new LinkResponse(linkId, shortUrl, url, visitCount);
        }

        resultSet.close();
        connection.close();
        return null;
    }

    public int checkURLExistenceInDB(String url) throws SQLException {
        String selectQuery = "SELECT count(url) FROM links WHERE `url` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, url);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            resultSet.close();
            connection.close();
            return resultSet.getInt(1);
        }

        resultSet.close();
        connection.close();
        return 0;
    }

    public int checkSlugExistenceInDB(String slug) throws SQLException {
        String selectQuery = "SELECT count(*) FROM links WHERE `slug` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, slug);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){

            resultSet.close();
            connection.close();
            return resultSet.getInt(1);
        }

        resultSet.close();
        connection.close();
        return 0;
    }

    public int writeURLToDB(String url, String slug) throws SQLException{

        String insertQuery = "INSERT INTO links(url,slug) values(?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, url);
        preparedStatement.setString(2, slug);

        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        int last_insert_id = 0;
        if (rs.next()) {
            last_insert_id = rs.getInt(1);
        }
        return last_insert_id;
    }
}

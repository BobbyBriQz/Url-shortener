package cc.lnkd.urlshortener.repositories;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.LinkResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    Connection connection;

    public UserRepository(DBConfig dbConfig){

        //DBConfig is used to get the configuration from the application.properties file
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lnkd_db?"+"user="+dbConfig.getUsername()+"&password="+dbConfig.getPassword()+"&serverTimezone=UTC");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AuthUser getAuthUserByEmail(String email) throws SQLException {
        String selectQuery = "SELECT email, password, enabled FROM users WHERE `email` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            String roleQuery = "SELECT authority FROM authorities WHERE `email` = ?";

            PreparedStatement roleStatement = connection.prepareStatement(roleQuery);
            roleStatement.setString(1, email);

            ResultSet roleResult = roleStatement.executeQuery();

            String userEmail = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean enabled = resultSet.getBoolean("enabled");
            List<String> roles = new ArrayList<>();

            while (roleResult.next()){
                roles.add(roleResult.getString("role"));
            }

            AuthUser authUser = AuthUser.builder()
                    .email(userEmail)
                    .password(password)
                    .isEnabled(enabled)
                    .roles(roles)
                    .build();

            resultSet.close();
            connection.close();
            return authUser;
        }

        resultSet.close();
        connection.close();
        return null;
    }
}

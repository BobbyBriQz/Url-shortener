package cc.lnkd.urlshortener.repositories;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        String selectQuery = "SELECT id, email, password, enabled FROM users WHERE `email` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            String roleQuery = "SELECT authority FROM authorities WHERE `email` = ?";

            PreparedStatement roleStatement = connection.prepareStatement(roleQuery);
            roleStatement.setString(1, email);

            ResultSet roleResult = roleStatement.executeQuery();

            int userId = resultSet.getInt("id");
            String userEmail = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean enabled = resultSet.getBoolean("enabled");
            List<String> roles = new ArrayList<>();

            while (roleResult.next()){
                roles.add(roleResult.getString("authority"));
            }

            AuthUser authUser = AuthUser.builder()
                    .userId(userId)
                    .email(userEmail)
                    .password(password)
                    .isEnabled(enabled)
                    .roles(roles)
                    .build();

            resultSet.close();
            roleResult.close();
            connection.close();
            return authUser;
        }

        resultSet.close();
        connection.close();
        return null;
    }

    public RegisteredUser register(RegistrationRequest request) throws SQLException {

        String insertQuery = "INSERT INTO users(first_name, last_name, email, password) " +
                "values(?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, request.getFirstName());
        preparedStatement.setString(2, request.getLastName());
        preparedStatement.setString(3, request.getEmail());
        preparedStatement.setString(4, request.getPassword()); //Make Sure it is the same strength as the password Encoder bean

        int id = preparedStatement.executeUpdate();

        String roleInsert = "INSERT INTO authorities(email, authority) " +
                "values(?,?)";

        PreparedStatement roleStatement = connection.prepareStatement(roleInsert);
        roleStatement.setString(1, request.getEmail());
        roleStatement.setString(2, "ROLE_FREE");
        roleStatement.executeUpdate();

        if (id != 0){

            String selectQuery = "SELECT id, email, first_name, last_name, created_at, verified_at, enabled " +
                    "FROM users where `id` = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);

            ResultSet selectResult = selectStatement.executeQuery();

            if(selectResult.next()){
                int userId = selectResult.getInt("id");
                String email = selectResult.getString("email");
                String firstName = selectResult.getString("first_name");
                String lastName = selectResult.getString("last_name");
                String createdAt = selectResult.getString("created_at");;
                String verifiedAt = selectResult.getString("verified_at");
                boolean isEnabled = selectResult.getBoolean("enabled");

                RegisteredUser user = RegisteredUser.builder()
                        .id(userId)
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .createdAt(createdAt)
                        .verifiedAt(verifiedAt)
                        .isEnabled(isEnabled)
                        .build();

                selectStatement.close();
                roleStatement.close();
                preparedStatement.close();
                connection.close();
                return user;
            }
        }


        roleStatement.close();
        preparedStatement.close();
        connection.close();
        return null;
    }

    public RegisteredUser getUserWithEmail(String email) throws SQLException {
        String selectQuery = "SELECT id, email, first_name, last_name, created_at, verified_at, enabled " +
                "FROM users where `email` = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        selectStatement.setString(1, email);

        ResultSet selectResult = selectStatement.executeQuery();

        if(selectResult.next()){
            int userId = selectResult.getInt("id");
            String userEmail = selectResult.getString("email");
            String firstName = selectResult.getString("first_name");
            String lastName = selectResult.getString("last_name");
            String createdAt = selectResult.getString("created_at");;
            String verifiedAt = selectResult.getString("verified_at");
            boolean isEnabled = selectResult.getBoolean("enabled");

            RegisteredUser user = RegisteredUser.builder()
                    .id(userId)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .createdAt(createdAt)
                    .verifiedAt(verifiedAt)
                    .isEnabled(isEnabled)
                    .build();

            selectStatement.close();
            connection.close();
            return user;
        }

        selectStatement.close();
        connection.close();
        return null;
    }

    public int doesEmailAlreadyExist(String email) throws SQLException {
        String selectQuery = "SELECT count(email) as email_count " +
                "FROM users where `email` = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        selectStatement.setString(1, email);

        ResultSet selectResult = selectStatement.executeQuery();

        if(selectResult.next()){
            int emailCount = selectResult.getInt("email_count");

            selectStatement.close();
            connection.close();
            return emailCount;
        }

        selectStatement.close();
        connection.close();
        return 0;
    }
}

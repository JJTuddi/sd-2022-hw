package repository.user;

import controller.Response;
import model.User;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

  private final Connection connection;
  private final RightsRolesRepository rightsRolesRepository;


  public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
    this.connection = connection;
    this.rightsRolesRepository = rightsRolesRepository;
  }

  @Override
  public List<User> findAll() {
    return null;
  }

  @Override
  public User findByUsernameAndPassword(String username, String password) {
    try {
      Statement statement = connection.createStatement();

      String fetchUserSql =
          "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
      ResultSet userResultSet = statement.executeQuery(fetchUserSql);
      userResultSet.next();

      User user = new UserBuilder()
          .setUsername(userResultSet.getString("username"))
          .setPassword(userResultSet.getString("password"))
          .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
          .build();

      return user;
    } catch (SQLException e) {
      System.out.println(e.toString());
    }
    return null;
  }

  @Override
  public boolean save(User user) {
    try {
      PreparedStatement insertUserStatement = connection
          .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      insertUserStatement.setString(1, user.getUsername());
      insertUserStatement.setString(2, user.getPassword());
      insertUserStatement.executeUpdate();

      ResultSet rs = insertUserStatement.getGeneratedKeys();
      rs.next();
      long userId = rs.getLong(1);
      user.setId(userId);

      rightsRolesRepository.addRolesToUser(user, user.getRoles());

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

  }

  @Override
  public void removeAll() {
    try {
      Statement statement = connection.createStatement();
      String sql = "DELETE from user where id >= 0";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteByUsername(String username) {
    try {
      Statement statement = connection.createStatement();
      String sql = "DELETE from user where username = \'" +username+ "\'";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Response<Boolean> existsByUsername(String email) {
    try {
      Statement statement = connection.createStatement();

      String fetchUserSql =
          "Select * from `" + USER + "` where `username`=\'" + email + "\'";
      ResultSet userResultSet = statement.executeQuery(fetchUserSql);
      return new Response<>(userResultSet.next());
    } catch (SQLException e) {
      return new Response<>(singletonList(e.getMessage()));
    }
  }

  @Override
  public void updateByUsername(String email, User u2) {
    try {
      PreparedStatement insertUserStatement = connection
              .prepareStatement("UPDATE user SET username =?, password=? WHERE username = ?", Statement.RETURN_GENERATED_KEYS);
      insertUserStatement.setString(1, u2.getUsername());
      insertUserStatement.setString(2, u2.getPassword());
      insertUserStatement.setString(3, email);
      insertUserStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public User findByUsername(String username) {
    try {
      Statement statement = connection.createStatement();

      String fetchUserSql =
              "Select * from `" + USER + "` where `username`=\'" +username+ "\'";
      ResultSet userResultSet = statement.executeQuery(fetchUserSql);
      userResultSet.next();

      User user = new UserBuilder()
              .setUsername(userResultSet.getString("username"))
              .setPassword(userResultSet.getString("password"))
              .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
              .build();

      return user;
    } catch (SQLException e) {
      System.out.println(e.toString());
    }
    return null;
  }
  }





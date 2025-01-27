package guru.qa.niffler.db.dao;

import static org.springframework.util.Base64Utils.encodeToString;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import io.netty.handler.codec.base64.Base64Encoder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtendWith;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

  private static DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
  private static DataSource userdataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

  @Override
  public int createUser(UserEntity user) {
    int createdRows = 0;
    try (Connection conn = authDs.getConnection()) {

      conn.setAutoCommit(false);

      try (PreparedStatement usersPs = conn.prepareStatement(
          "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS); // ожидаем получить сгенеренный ключ (2 параметр)

          PreparedStatement authorityPs = conn.prepareStatement(
          "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {

        usersPs.setString(1, user.getUsername());
        usersPs.setString(2, pe.encode(user.getPassword()));
        usersPs.setBoolean(3, user.getEnabled());
        usersPs.setBoolean(4, user.getAccountNonExpired());
        usersPs.setBoolean(5, user.getAccountNonLocked());
        usersPs.setBoolean(6, user.getAccountNonExpired());

        createdRows = usersPs.executeUpdate();
        UUID generatedUserId;
        try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            generatedUserId = UUID.fromString(generatedKeys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t obtain id from given ResultSet");
          }
        }

        for (Authority authority: Authority.values()) {
          authorityPs.setObject(1, generatedUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        user.setId(generatedUserId);
        conn.commit();
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return createdRows;
  }

  @Override
  public UserEntity getUserById(UUID userId) {
    UserEntity user = new UserEntity();
    try (Connection conn = authDs.getConnection();
        PreparedStatement userPs = conn.prepareStatement(
          "SELECT * FROM users WHERE id = ?"
      )) {
        userPs.setObject(1, user.getId());
        ResultSet resultSet = userPs.executeQuery();

        if (resultSet.next()) {
          user.setId(resultSet.getObject("id", UUID.class));
          user.setUsername(resultSet.getString("username"));
          user.setPassword(resultSet.getString("password"));
          user.setEnabled(resultSet.getBoolean("enabled"));
          user.setAccountNonExpired(resultSet.getBoolean("account_not_expired"));
          user.setAccountNonLocked(resultSet.getBoolean("account_not_locked"));
          user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserEntity updateUser(UserEntity user) {

    try (Connection conn = authDs.getConnection();
      PreparedStatement usersPs = conn.prepareStatement(
          "UPDATE users SET(password, enabled, account_non_expired, "
              + "account_non_locked, credentials_non_expired) = (?, ?, ?, ?, ?) WHERE id = ?")
    ) {
        usersPs.setString(1, pe.encode(user.getPassword()));
        usersPs.setBoolean(2, user.getEnabled());
        usersPs.setBoolean(3, user.getAccountNonExpired());
        usersPs.setBoolean(4, user.getAccountNonLocked());
        usersPs.setBoolean(5, user.getAccountNonExpired());
        usersPs.setObject(6, user.getId());

        usersPs.executeUpdate();
        return getUserById(user.getId());

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteUserById(UUID userId) {

    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (
          PreparedStatement authPs = conn.prepareStatement(
          "DELETE FROM authorities WHERE user_id = ?");

          PreparedStatement usersPs = conn.prepareStatement(
          "DELETE FROM users WHERE id = ?");
      ) {

        authPs.setObject(1, userId);
        usersPs.setObject(1, userId);
        authPs.executeUpdate();
        usersPs.executeUpdate();
        conn.commit();
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int createUserInUserData(UserEntity user) {
    int createdRows = 0;
    try (Connection conn = userdataDs.getConnection()) {

      try (PreparedStatement usersPs = conn.prepareStatement(
          "INSERT INTO users (username, currency) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
      ) {
        usersPs.setString(1, user.getUsername());
        usersPs.setString(2, CurrencyValues.RUB.name());

        createdRows = usersPs.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return createdRows;
  }

  @Override
  public UserDataEntity getUserdataInUserData(String username) {
    UserDataEntity userdata = new UserDataEntity();
    try (Connection conn = userdataDs.getConnection();
        PreparedStatement userPs = conn.prepareStatement(
            "SELECT * FROM users WHERE username = ?"
        )) {
      userPs.setObject(1, username);
      ResultSet resultSet = userPs.executeQuery();

      if (resultSet.next()) {
        userdata.setId(resultSet.getObject("id", UUID.class));
        userdata.setUsername(resultSet.getString("username"));
        userdata.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userdata.setFirstname(resultSet.getString("firstname"));
        userdata.setSurname(resultSet.getString("surname"));
        userdata.setPhoto(resultSet.getString("photo"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return userdata;
  }

  @Override
  public UserDataEntity updateUserInUserData(UserDataEntity userdata) {

    try (Connection conn = userdataDs.getConnection();
        PreparedStatement userdataPs = conn.prepareStatement(
            "UPDATE users SET (currency, firstname, surname, photo) = (?, ?, ?, ?) WHERE username = ?")) {

      ClassLoader classLoader = getClass().getClassLoader();
      byte[] fileContent = FileUtils.readFileToByteArray(new File(classLoader
          .getResource(userdata.getPhoto())
          .getFile()));
      String encodedString = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent);

      userdataPs.setObject(1, userdata.getCurrency().name());
      userdataPs.setString(2, userdata.getFirstname());
      userdataPs.setString(3, userdata.getSurname());
      userdataPs.setObject(4, encodedString.getBytes());
      userdataPs.setObject(5, userdata.getUsername());

      userdataPs.executeUpdate();
      return getUserdataInUserData(userdata.getUsername());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void deleteUserByIdInUserData(UUID userId) {

    try (Connection connUserdata = userdataDs.getConnection();
        PreparedStatement userdataPS = connUserdata.prepareStatement(
          "DELETE FROM users WHERE id = ?");
      ) {
        userdataPS.setObject(1, userId);
        userdataPS.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteUserByUsernameInUserData(String username) {

    try (Connection connUserdata = userdataDs.getConnection();
        PreparedStatement userdataPS = connUserdata.prepareStatement(
          "DELETE FROM users WHERE username = ?");
      ) {
        userdataPS.setString(1, username);
        userdataPS.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}

package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUser9DAO9Hibernate;
import guru.qa.niffler.db.dao.impl.AuthUser99DAOJdbc;
import guru.qa.niffler.db.dao.impl.AuthUser99DAOSpringJdbc;
import guru.qa.niffler.db.entity.auth.UserEntity;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// Интерфейс отвечает за порядок взаимодействия с БД (любые виды хранения данных, персистенс лэйер)
// развязать реализацию с БД и бизнес логику
public interface AuthUser9DAO {

  static AuthUser9DAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
      return new AuthUser99DAOSpringJdbc();
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUser99DAOJdbc();
    } else {
//      return new AuthUserDAOJdbc();
//      return new AuthUserDAOSpringJdbc();
      return new AuthUser9DAO9Hibernate();
    }
  }

  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  UserEntity createUser(UserEntity user);

  UserEntity getUserById(UUID userId);

  UserEntity updateUser(UserEntity user);

  void deleteUser(UserEntity user);
}

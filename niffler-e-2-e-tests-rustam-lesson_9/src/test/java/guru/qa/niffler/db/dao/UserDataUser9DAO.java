package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUser99DAOJdbc;
import guru.qa.niffler.db.dao.impl.UserDataUser9DAO9Hibernate;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import java.util.UUID;

public interface UserDataUser9DAO {

  static UserDataUser9DAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
//      return new AuthUserDAOHibernate();
      return null;
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUser99DAOJdbc();
    } else {
//      return new AuthUserDAOJdbc();
//      return new AuthUserDAOSpringJdbc();
      return new UserDataUser9DAO9Hibernate();
    }
  }

  UserDataEntity createUserInUserData(UserDataEntity userData);

  UserDataEntity getUserdataInUserData(UserDataEntity userData);

  UserDataEntity getUserdataInUserDataByUserName(String username);

  UserDataEntity updateUserInUserData(UserDataEntity userData);

  void deleteUserByIdInUserData(UUID userId);

  void deleteUserByUsernameInUserData(UserDataEntity userData);
}

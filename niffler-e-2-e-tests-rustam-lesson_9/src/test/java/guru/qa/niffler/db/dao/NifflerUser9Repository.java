package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUser9DAO9Hibernate;
import guru.qa.niffler.db.dao.impl.UserDataUser9DAO9Hibernate;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;

public class NifflerUser9Repository {

  private final AuthUser9DAO authUser9DAO = new AuthUser9DAO9Hibernate();
  private final UserDataUser9DAO userdataUser9DAO = new UserDataUser9DAO9Hibernate();

//  private final AuthUserDAO authUserDAO = new AuthUserDAOSpringJdbc();
//  private final UserDataUserDAO userdataUserDAO = new UserDataUserDAO9Hibernate();

  public void createUserForTest(UserEntity user) {
    authUser9DAO.createUser(user);
    userdataUser9DAO.createUserInUserData(fromAuthUserEntity(user));
  }

  public void removeAfterTest(UserEntity user) {
    UserDataEntity userData = userdataUser9DAO.getUserdataInUserDataByUserName(user.getUsername());
    userdataUser9DAO.deleteUserByUsernameInUserData(userData);
    authUser9DAO.deleteUser(user);
  }

  public UserDataEntity fromAuthUserEntity(UserEntity user) {
    UserDataEntity userData = new UserDataEntity();
    userData.setUsername(user.getUsername());
    userData.setCurrency(CurrencyValues.USD);
    userData.setFirstname("updated_firstname_2");
    userData.setSurname("updated_surname_2");
    userData.setPhoto("photos/photo2.jpeg".getBytes());
    return userData;
  }
}

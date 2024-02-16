package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUser9DAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.Jpa9Service;
import guru.qa.niffler.db.helper.UserdataHelper;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import java.util.UUID;

public class UserDataUser9DAO9Hibernate extends Jpa9Service implements UserDataUser9DAO {

  public UserDataUser9DAO9Hibernate() {
    super(EntityManagerFactoryProvider.INSTANCE.getEntityManagerFactory(ServiceDB.USERDATA).createEntityManager());
  }

  @Override
  public UserDataEntity createUserInUserData(UserDataEntity userData) {
    userData.setPhoto(new UserdataHelper().getEncodingPhoto(userData).getBytes());
    persist(userData);
    return userData;
  }

//  @Override // тоже работает
//  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
//    return em.createQuery("select u from UserDataEntity u where u.username=:username", UserDataEntity.class)
//        .setParameter("username", userData.getUsername())
//        .getSingleResult();
//  }

  @Override
  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
    return find(UserDataEntity.class, userData.getId());
  }

  @Override
  public UserDataEntity getUserdataInUserDataByUserName(String username) {
    return em.createQuery("select u from UserDataEntity u where u.username=:username", UserDataEntity.class)
    .setParameter("username", username)
    .getSingleResult();
  }


  @Override
  public UserDataEntity updateUserInUserData(UserDataEntity userData) {
    return merge(userData);
  }

  @Override
  public void deleteUserByIdInUserData(UUID userId) {

  }

  @Override
  public void deleteUserByUsernameInUserData(UserDataEntity userData) {
    remove(userData);
  }

}

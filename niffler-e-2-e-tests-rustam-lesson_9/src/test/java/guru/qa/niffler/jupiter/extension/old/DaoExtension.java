package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.db.dao.AuthUser9DAO;
import guru.qa.niffler.db.dao.UserDataUser9DAO;
import guru.qa.niffler.jupiter.annotation.old.DAO;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class DaoExtension implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    for (Field field: testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(AuthUser9DAO.class)
          && field.isAnnotationPresent(DAO.class)) {
        field.setAccessible(true);

        field.set(testInstance, AuthUser9DAO.getImpl());
      }
      if (field.getType().isAssignableFrom(UserDataUser9DAO.class)
          && field.isAnnotationPresent(DAO.class)) {
        field.setAccessible(true);

        field.set(testInstance, UserDataUser9DAO.getImpl());
      }

    }
  }
}

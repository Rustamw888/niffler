package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.api.client.DBUser9RestClient;
import guru.qa.niffler.api.client.Register9Client;
import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.context.Session9Context;
import guru.qa.niffler.jupiter.annotation.old.DBUser;
import guru.qa.niffler.model.User9Json;
import io.qameta.allure.AllureId;
import java.util.Objects;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DBUser9Extension implements BeforeEachCallback, ParameterResolver,
    AfterTestExecutionCallback {

  public final static Namespace NAMESPACE = Namespace.create(DBUser9Extension.class);

  private final DBUser9RestClient dbUser9RestClient = new DBUser9RestClient();

  private final Register9Client register9Client = new Register9Client();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final String testId = getTestId(context);
    DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
    if (annotation != null) {
      User9Json registeredUser = doRegister(
          annotation.username(),
          annotation.password(),
          annotation.submitPassword()
      );

      context.getStore(NAMESPACE).put(testId, registeredUser);
    }
  }

  private User9Json doRegister(String username, String password, String submitPassword) {
    register9Client.registerPreRequest();
    register9Client.register(username, password, submitPassword);
    return dbUser9RestClient.getCurrentUser(username);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(User9Json.class);
  }

  @Override
  public User9Json resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    final String testId = getTestId(extensionContext);
    return extensionContext.getStore(NAMESPACE).get(testId, User9Json.class);
  }

  private String getTestId(ExtensionContext context) {
    return Objects.requireNonNull(
        context.getRequiredTestMethod().getAnnotation(AllureId.class)
    ).value();
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    Session9Context.getInstance().release();
    Cookie9Context.getInstance().release();
  }
}

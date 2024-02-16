package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector;
import guru.qa.niffler.model.User9Json;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public abstract class CreateUser9Extension implements BeforeEachCallback, ParameterResolver {

  public static final Namespace NESTED = Namespace.create(GeneratedUser.Selector.NESTED);
  public static final Namespace OUTER = Namespace.create(GeneratedUser.Selector.OUTER);

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Map<GeneratedUser.Selector, GenerateUser> usersForTest = usersForTest(context);
    for (Entry<Selector, GenerateUser> entry : usersForTest.entrySet()) {
      User9Json user = createUserForTest(entry.getValue());
      user.setFriends(createFriendIfPresent(entry.getValue()));
      user.setIncomeInvitations(createIncomeInvitationsIfPresent(entry.getValue()));
      user.setOutcomeInvitations(createOutcomeInvitationsIfPresent(entry.getValue()));
      context.getStore(Namespace.create(entry.getKey())).put(entry.getKey(), user);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(GeneratedUser.class) &&
        parameterContext.getParameter().getType().isAssignableFrom(User9Json.class);
  }

  @Override
  public User9Json resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    GeneratedUser generatedUser = parameterContext.getParameter().getAnnotation(GeneratedUser.class);
    return extensionContext.getStore(Namespace.create(generatedUser.selector()))
        .get(generatedUser.selector(), User9Json.class);
  }

  protected abstract List<User9Json> createFriendIfPresent(GenerateUser annotation);

  protected abstract List<User9Json> createIncomeInvitationsIfPresent(GenerateUser annotation);

  protected abstract List<User9Json> createOutcomeInvitationsIfPresent(GenerateUser annotation);
  protected abstract User9Json createUserForTest(GenerateUser annotation);

  private Map<GeneratedUser.Selector, GenerateUser> usersForTest(ExtensionContext context) {
    Map<GeneratedUser.Selector, GenerateUser> result = new HashMap<>();
    ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
    if (apiLogin != null && apiLogin.user().handleAnnotation()) {
      result.put(Selector.NESTED, apiLogin.user());
    }
    GenerateUser outerUser = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
    if (outerUser != null && outerUser.handleAnnotation()) {
      result.put(Selector.OUTER, outerUser);
    }
    return result;
  }
}

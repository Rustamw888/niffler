package guru.qa.niffler.jupiter.extension.old;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import guru.qa.niffler.api.client.Auth9Client;
import guru.qa.niffler.api.client.DBUser9RestClient;
import guru.qa.niffler.api.client.Register9Client;
import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.context.Session9Context;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.old.ApiRegisterWithRandomUser;
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
import org.openqa.selenium.Cookie;

public class ApiRegisterWithRandomUserExtension implements BeforeEachCallback, AfterTestExecutionCallback,
    ParameterResolver {

  private final static Config CFG = Config.getInstance();
  private final Register9Client register9Client = new Register9Client();
  private final Auth9Client auth9Client = new Auth9Client();
  private final String JSESSIONID = "JSESSIONID";
  public final static Namespace NAMESPACE = Namespace.create(DBUser9Extension.class);
  private final DBUser9RestClient dbUser9RestClient = new DBUser9RestClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final String testId = getTestId(context);
    ApiRegisterWithRandomUser apiRegister = context.getRequiredTestMethod().getAnnotation(ApiRegisterWithRandomUser.class);
    if (apiRegister != null) {
      User9Json registeredUser = new User9Json();
      if (apiRegister.randomUser().handleAnnotation()) {
        registeredUser = doRegister(
            apiRegister.username(),
            apiRegister.password(),
            apiRegister.submitPassword()
        );
        doLogin(
            apiRegister.username(),
            apiRegister.password()
        );
      } else {
        String randomUsername = new Faker().name().username();
        String randomPassword = new Faker().code().gtin8();

        registeredUser = doRegister(
            randomUsername,
            randomPassword,
            randomPassword
        );
        doLogin(
            randomUsername,
            randomPassword
        );
      }

      context.getStore(NAMESPACE).put(testId, registeredUser);
    }
  }

  private User9Json doRegister(String username, String password, String submitPassword) {
    register9Client.registerPreRequest();
    register9Client.register(username, password, submitPassword);
    return dbUser9RestClient.getCurrentUser(username);
  }

  private void doLogin(String username, String password) {
    final Session9Context session9Context = Session9Context.getInstance();
    final Cookie9Context cookie9Context = Cookie9Context.getInstance();
    final String codeVerifier = OauthUtils.generateCodeVerifier();
    final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

    session9Context.setCodeVerifier(codeVerifier);
    session9Context.setCodeChallenge(codeChallenge);

    auth9Client.authorizePreRequest9();
    auth9Client.login9(username, password);
    final String token = auth9Client.getToken9();
    Selenide.open(CFG.getFrontUrl());
    Selenide.sessionStorage().setItem("id_token", token);
    Selenide.sessionStorage().setItem("codeVerifier", session9Context.getCodeVerifier());
    Selenide.sessionStorage().setItem("codeChallenge", session9Context.getCodeChallenge());
    Cookie jsessionCookie = new Cookie(JSESSIONID, cookie9Context.getCookie(JSESSIONID));
    WebDriverRunner.getWebDriver().manage().addCookie(jsessionCookie);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    Session9Context.getInstance().release();
    Cookie9Context.getInstance().release();
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
}

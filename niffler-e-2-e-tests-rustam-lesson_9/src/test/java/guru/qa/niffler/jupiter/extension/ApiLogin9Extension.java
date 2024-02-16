package guru.qa.niffler.jupiter.extension;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.CreateUser9Extension.NESTED;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.client.Auth9Client;
import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.context.Session9Context;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector;
import guru.qa.niffler.model.User9Json;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiLogin9Extension implements BeforeEachCallback, AfterTestExecutionCallback {

  protected static final Config CFG = Config.getInstance();
  private final Auth9Client auth9Client = new Auth9Client();
  private final static String JSESSIONID = "JSESSIONID";

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApiLogin annotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
    if (annotation != null) {
      GenerateUser user = annotation.user();
      if (user.handleAnnotation()) {
        User9Json createdUser = context.getStore(NESTED).get(
            Selector.NESTED,
            User9Json.class
        );
        doLogin(createdUser.getUsername(), createdUser.getPassword());
      } else {
        doLogin(annotation.username(), annotation.password());
      }
    }
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
    open(CFG.getFrontUrl());
    Selenide.sessionStorage().setItem("id_token", token);
    Selenide.sessionStorage().setItem("codeChallenge", session9Context.getCodeChallenge());
    Selenide.sessionStorage().setItem("codeVerifier", session9Context.getCodeVerifier());
    Cookie jsessionCookie = new Cookie(JSESSIONID, cookie9Context.getCookie(JSESSIONID));
    WebDriverRunner.getWebDriver().manage().addCookie(jsessionCookie);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    Session9Context.getInstance().release();
    Cookie9Context.getInstance().release();
  }
}

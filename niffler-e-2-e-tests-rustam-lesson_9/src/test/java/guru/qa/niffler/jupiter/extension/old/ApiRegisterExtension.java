package guru.qa.niffler.jupiter.extension.old;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.client.Auth9Client;
import guru.qa.niffler.api.client.Register9Client;
import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.context.Session9Context;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.old.ApiRegister;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiRegisterExtension implements BeforeEachCallback, AfterTestExecutionCallback {

  private final static Config CFG = Config.getInstance();
  private final Register9Client register9Client = new Register9Client();
  private final Auth9Client auth9Client = new Auth9Client();
  private final String JSESSIONID = "JSESSIONID";

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApiRegister apiRegister = context.getRequiredTestMethod().getAnnotation(ApiRegister.class);
    if (apiRegister != null) {
      doRegister(
          apiRegister.username(),
          apiRegister.password(),
          apiRegister.submitPassword()
      );
      doLogin(
          apiRegister.username(),
          apiRegister.password()
      );
    }
  }

  private void doRegister(String username, String password, String submitPassword) {
    register9Client.registerPreRequest();
    register9Client.register(username, password, submitPassword);
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
}

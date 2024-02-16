package guru.qa.niffler.api.client;

import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.context.Session9Context;
import guru.qa.niffler.api.interceptor.AddCookies9Interceptor;
import guru.qa.niffler.api.interceptor.RecievedCode9Interceptor;
import guru.qa.niffler.api.interceptor.RecievedCookies9Interceptor;
import guru.qa.niffler.api.service.Auth9Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Auth9Client extends BaseRestClient9 {

  public Auth9Client() {
    super(
        CFG.getAuthUrl(),
        true,
        new RecievedCookies9Interceptor(),
        new AddCookies9Interceptor(),
        new RecievedCode9Interceptor()
    );
  }

  private final Auth9Service auth9Service = retrofit.create(Auth9Service.class);

  public void authorizePreRequest9() {
    try {
      auth9Service.authorize(
          "code",
          "client",
          "openid",
          CFG.getFrontUrl() + "/authorized",
          Session9Context.getInstance().getCodeChallenge(),
          "S256"
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void login9(String username, String password) {
    try {
      auth9Service.login(
          Cookie9Context.getInstance().getFormattedCookie("JSESSIONID"),
          Cookie9Context.getInstance().getFormattedCookie("XSRF-TOKEN"),
          Cookie9Context.getInstance().getCookie("XSRF-TOKEN"),
          username,
          password
      ).execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public String getToken9() {
    Session9Context session9Context = Session9Context.getInstance();
    try {
      return auth9Service.token(
          "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
          "client",
          CFG.getFrontUrl() + "/authorized",
          "authorization_code",
          session9Context.getCode(),
          session9Context.getCodeVerifier()
      ).execute().body().get("id_token").asText();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

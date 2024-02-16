package guru.qa.niffler.api.client;

import guru.qa.niffler.api.context.Cookie9Context;
import guru.qa.niffler.api.interceptor.AddCookies9Interceptor;
import guru.qa.niffler.api.interceptor.RecievedCode9Interceptor;
import guru.qa.niffler.api.interceptor.RecievedCookies9Interceptor;
import guru.qa.niffler.api.service.Register9Service;
import java.io.IOException;

public class Register9Client extends BaseRestClient9 {

  public Register9Client() {
    super(
        CFG.getAuthUrl(),
        true,
        new RecievedCookies9Interceptor(),
        new AddCookies9Interceptor(),
        new RecievedCode9Interceptor()
    );
  }

  private final Register9Service register9Service = retrofit.create(Register9Service.class);

  public void registerPreRequest() {
    try {
      register9Service.requestRegisterForm().execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void register(String username, String password, String passwordSubmit) {
    try {
      register9Service.register(
          username,
          password,
          passwordSubmit,
          Cookie9Context.getInstance().getCookie("XSRF-TOKEN")
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

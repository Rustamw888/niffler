package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.Cookie9Context;
import java.io.IOException;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RecievedCookies9Interceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    List<String> setCookies = response.headers("Set-Cookie");

    if (!setCookies.isEmpty()) {
      for (String cookie: setCookies) {
        String[] cookies = cookie.split(";");
        for (String item: cookies) {
          if (item.contains("XSRF-TOKEN") || item.contains("JSESSIONID")) {
            String[] res = item.split("=");
            if (res.length == 2) {
              System.out.println("#### OLD COOKIE JSESSIONID: " + Cookie9Context.getInstance().getCookie("JSESSIONID"));
              System.out.println("#### OLD COOKIE XSRF-TOKEN: " + Cookie9Context.getInstance().getCookie("XSRF-TOKEN"));
              System.out.println("#### NEW COOKIE: " + res[0] + "=" + res[1]);
              Cookie9Context.getInstance().setCookie(res[0], res[1]);
            } else {
              Cookie9Context.getInstance().removeCookie(res[0]);
            }
          }
        }
      }

    }
    return response;
  }
}

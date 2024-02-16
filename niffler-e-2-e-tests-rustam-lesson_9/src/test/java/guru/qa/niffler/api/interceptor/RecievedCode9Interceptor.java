package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.Session9Context;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

public class RecievedCode9Interceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    String location = response.header("Location");
    if (location != null && location.contains("code=")) {
      Session9Context.getInstance().setCode(StringUtils.substringAfter(location, "code="));
    }
    return response;
  }
}

package guru.qa.niffler.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.context.CookieHolder;
import guru.qa.niffler.api.context.SessionStorageHolder;
import guru.qa.niffler.api.interceptor.AddCookiesReqInterceptor;
import guru.qa.niffler.api.interceptor.ExtractCodeFromRespInterceptor;
import guru.qa.niffler.api.interceptor.ReceivedCookieRespInterceptor;
import guru.qa.niffler.config.Config;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class NifflerAuthClient {

  private static final Config CFG = Config.getInstance();

  private static final OkHttpClient httpClient = new OkHttpClient.Builder()
      .followRedirects(true)
      .addNetworkInterceptor(new ReceivedCookieRespInterceptor())
      .addNetworkInterceptor(new AddCookiesReqInterceptor())
      .addNetworkInterceptor(new ExtractCodeFromRespInterceptor())
      .build();

  private final Retrofit retrofit = new Retrofit.Builder()
      .client(httpClient)
      .addConverterFactory(JacksonConverterFactory.create())
      .baseUrl(CFG.getAuthUrl())
      .build();

  private final NifflerAuthApi nifflerAuthApi = retrofit.create(NifflerAuthApi.class);

  public void authorize() throws Exception {
    SessionStorageHolder.getInstance().init();
    nifflerAuthApi.authorize(
        "code",
        "client",
        "openid",
        CFG.getFrontUrl() + "authorized",
        SessionStorageHolder.getInstance().getCodeChallenge(),
        "S256"
    ).execute();
  }

  public Response<Void> login(String username, String password) throws Exception {
    return nifflerAuthApi.login(
        CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
        CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
        CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
        username,
        password
    ).execute();
  }


  public JsonNode getToken() throws Exception {
    String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
    return nifflerAuthApi.getToken(
        basic,
        "client",
        CFG.getFrontUrl() + "authorized",
        "authorization_code",
        SessionStorageHolder.getInstance().getCode(),
        SessionStorageHolder.getInstance().getCodeVerifier()
    ).execute().body();
  }

  public Response<Void> register(String username, String password) throws Exception {
    return nifflerAuthApi.register(
        CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
        CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
        CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
        username,
        password,
        password
    ).execute();
  }
}

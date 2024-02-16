package guru.qa.niffler.api.client;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class BaseRestClient9 {

  protected static final Config CFG = Config.getInstance();
  protected final String serviceBaseUrl;
  protected final OkHttpClient httpClient;
  protected final Retrofit retrofit;

  public BaseRestClient9(String serviceBaseUrl) {
    this(serviceBaseUrl, false, null);
  }

//  public BaseRestClient7(String serviceBaseUrl) {
//    this.serviceBaseUrl = serviceBaseUrl;
//    this.httpClient = new OkHttpClient.Builder()
//        .build();
//    this.retrofit = new Retrofit.Builder()
//        .client(httpClient)
//        .baseUrl(serviceBaseUrl)
//        .addConverterFactory(JacksonConverterFactory.create())
//        .build();
//  }

  public BaseRestClient9(String serviceBaseUrl, boolean followRedirect) {
    this(serviceBaseUrl, followRedirect, null);
  }

  public BaseRestClient9(String serviceBaseUrl, boolean followRedirect, Interceptor... interceptors) {
    this.serviceBaseUrl = serviceBaseUrl;
    Builder builder = new Builder()
        .followRedirects(followRedirect);

    if (interceptors != null) {
      for (Interceptor interceptor: interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }

    builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
    this.httpClient = builder.build();

    this.retrofit = new Retrofit.Builder()
        .client(httpClient)
        .baseUrl(serviceBaseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }
}

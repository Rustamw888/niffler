package guru.qa.niffler.api.context;

import java.util.HashMap;
import java.util.Map;

public class Cookie9Context {

  private final Map<String, String> storage;
  private static final ThreadLocal<Cookie9Context> INSTANCE = ThreadLocal.withInitial(
      Cookie9Context::new);

  private Cookie9Context() {
    storage = new HashMap<>();
  }

  public static Cookie9Context getInstance() {
    return INSTANCE.get();
  }

  public void setCookie(String key, String cookie) {
    storage.put(key, cookie);
  }

  public void removeCookie(String key) {
    storage.remove(key);
  }

  public String getCookie(String key) {
    return storage.get(key);
  }

  public String getFormattedCookie(String key) {
    return key + "=" + getCookie(key);
  }

  public void release() {
    storage.clear();
  }
}

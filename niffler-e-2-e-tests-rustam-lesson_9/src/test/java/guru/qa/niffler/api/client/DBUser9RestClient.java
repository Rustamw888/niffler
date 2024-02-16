package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.DBUser9Api;
import guru.qa.niffler.model.User9Json;
import java.io.IOException;
import javax.annotation.Nonnull;

public class DBUser9RestClient extends BaseRestClient9 {

  public DBUser9RestClient() {
    super(CFG.getUserdataUrl());
  }

  private final DBUser9Api dBUser9Api = retrofit.create(DBUser9Api.class);

  public @Nonnull User9Json getCurrentUser(String username) {
    try {
      return dBUser9Api.getCurrentUser(username).execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

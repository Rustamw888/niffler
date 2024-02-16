package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.Spend9Service;
import guru.qa.niffler.model.Spend9Json;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class Spend9RestClient extends BaseRestClient9 {

  public Spend9RestClient() {
    super(CFG.getSpendUrl());
  }

  private final Spend9Service spend9Service = retrofit.create(Spend9Service.class);

  public @Nonnull Spend9Json addSpend(Spend9Json spend) {
    try {
      return spend9Service.addSpend(spend).execute().body();
    } catch (IOException e) {
      Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
      return null;
    }
  }
}

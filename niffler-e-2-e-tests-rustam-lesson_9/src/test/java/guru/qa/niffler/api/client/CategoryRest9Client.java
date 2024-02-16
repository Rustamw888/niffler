package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.Category9Service;
import guru.qa.niffler.model.Category9Json;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class CategoryRest9Client extends BaseRestClient9 {

  public CategoryRest9Client() {
    super(CFG.getCategoryUrl());
  }

  private final Category9Service category9Service = retrofit.create(Category9Service.class);

  public @Nonnull Category9Json addCategory(Category9Json category) {
    try {
      return category9Service.addCategory(category).execute().body();
    } catch (IOException e) {
      Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
      return null;
    }
  }
}

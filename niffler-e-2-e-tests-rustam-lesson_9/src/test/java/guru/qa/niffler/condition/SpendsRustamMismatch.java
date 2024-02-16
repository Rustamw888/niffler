package guru.qa.niffler.condition;

import static java.lang.System.lineSeparator;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.Spend9Json;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendsRustamMismatch extends UIAssertionError {
  public SpendsRustamMismatch(CollectionSource collection,
      List<Spend9Json> expectedSpends, List<Spend9Json> actualSpends,
      @Nullable String explanation, long timeoutMs) {
    super(
        collection.driver(),
        "Spending mismatch" +
            lineSeparator() + "Actual: " + actualSpends +
            lineSeparator() + "Expected: " + expectedSpends +
            (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
            lineSeparator() + "Collection: " + collection.description(),
        expectedSpends, actualSpends,
        timeoutMs);
  }
}

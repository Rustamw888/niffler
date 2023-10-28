package guru.qa.niffler.condition;

import static java.lang.System.lineSeparator;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendsRustamMismatch extends UIAssertionError {
  public SpendsRustamMismatch(CollectionSource collection,
      List<SpendJson> expectedSpends, List<SpendJson> actualSpends,
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

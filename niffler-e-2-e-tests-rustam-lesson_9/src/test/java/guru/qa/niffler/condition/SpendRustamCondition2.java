package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.Spend9Json;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.openqa.selenium.WebElement;

public class SpendRustamCondition2 {

  public static CollectionCondition spends(Spend9Json... expectedSpends) {
    return new CollectionCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        return super.check(driver, elements);
      }

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult,
          @Nullable Exception cause, long timeoutMs) {
        super.fail(collection, lastCheckResult, cause, timeoutMs);
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }
}

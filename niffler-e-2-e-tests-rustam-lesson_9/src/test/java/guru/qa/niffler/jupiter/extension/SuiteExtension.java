package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public interface SuiteExtension extends BeforeAllCallback {

  @Override
  default void beforeAll(ExtensionContext extensionContext) throws Exception {
    extensionContext.getRoot().getStore(Namespace.GLOBAL)
        .getOrComputeIfAbsent(this.getClass(), k -> {
          beforeAllTests(extensionContext);
          return new CloseableResource() {
            @Override
            public void close() throws Throwable {
              afterAllTests();
            }
          };
        });
  }

  default void beforeAllTests(ExtensionContext extensionContext) {}

  default void afterAllTests() {}
}

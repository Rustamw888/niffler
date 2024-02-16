package guru.qa.niffler.jupiter.extension.old;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public interface OldSuiteExtension extends BeforeAllCallback {

    default void beforeSuite() {};

    default void afterSuite() {};

    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        context.getRoot().getStore(Namespace.GLOBAL)
              .getOrComputeIfAbsent(
                    OldSuiteExtension.class,
                    k -> {
                        beforeSuite();
                        return (CloseableResource) this::afterSuite;
              }
        );
    }

//    @Override
//    default void beforeAll(ExtensionContext context) throws Exception {
//        context.getRoot().getStore(Namespace.GLOBAL).getOrComputeIfAbsent(
//              SuiteExtension.class, new Function<Class<SuiteExtension>, ExtensionContext.Store.CloseableResource>() {
//                  @Override
//                  public ExtensionContext.Store.CloseableResource apply(Class<SuiteExtension> suiteExtensionClass) {
//                      beforeSuite();
//                      return new ExtensionContext.Store.CloseableResource() {
//                          @Override
//                          public void close() throws Throwable {
//                              afterSuite();
//                          }
//                      };
//                  }
//              }
//        );
//    }
}

package guru.qa.niffler.jupiter.annotation.old;

import guru.qa.niffler.jupiter.extension.old.BrowserExtension;
import guru.qa.niffler.jupiter.extension.old.ExampleOldSuiteExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({BrowserExtension.class, ExampleOldSuiteExtension.class})
public @interface WebTest {
}

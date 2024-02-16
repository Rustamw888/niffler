package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ApiLogin9Extension;
import guru.qa.niffler.jupiter.extension.DbCreateUser9Extension;
import guru.qa.niffler.jupiter.extension.JpaExtension;
import guru.qa.niffler.jupiter.extension.old.BrowserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
    DbCreateUser9Extension.class,
    ApiLogin9Extension.class,
    BrowserExtension.class,
    JpaExtension.class
})
public @interface WebTest {
}
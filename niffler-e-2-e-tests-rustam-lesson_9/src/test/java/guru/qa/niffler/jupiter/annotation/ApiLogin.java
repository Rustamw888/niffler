package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ApiLogin9Extension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(ApiLogin9Extension.class)
public @interface ApiLogin {

  String username() default "";

  String password() default "";

  GenerateUser user() default @GenerateUser(handleAnnotation = false);
}

package guru.qa.niffler.jupiter.annotation.old;

import guru.qa.niffler.jupiter.extension.old.GenerateRandomUserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(GenerateRandomUserExtension.class)
public @interface GenerateRandomUser {

  boolean handleAnnotation() default false;
}

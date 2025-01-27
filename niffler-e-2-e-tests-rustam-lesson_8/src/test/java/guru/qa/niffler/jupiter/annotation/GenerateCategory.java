package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.GenerateCategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(GenerateCategoryExtension.class)
public @interface GenerateCategory {

    boolean handleAnnotation() default true;

    String category() default "";

    String username() default "";
}

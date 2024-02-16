package guru.qa.niffler.jupiter.annotation.old;


import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.jupiter.extension.old.Generate9SpendExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(Generate9SpendExtension.class)
public @interface GenerateSpend {

    String username();

    String description();

    String category() default "";

    GenerateCategory randomCategory() default @GenerateCategory(handleAnnotation = false);

    double amount();

    CurrencyValues currency();

}
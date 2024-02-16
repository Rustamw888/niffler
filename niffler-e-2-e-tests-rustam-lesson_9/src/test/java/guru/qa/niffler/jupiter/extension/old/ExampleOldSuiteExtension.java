package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManagerFactory;

public class ExampleOldSuiteExtension implements OldSuiteExtension {

    @Override
    public void beforeSuite() {
        System.out.println("BEFORE SUITE!!!");
    }

    @Override
    public void afterSuite() {

        EntityManagerFactoryProvider.INSTANCE.allStoredEntityManagerFactories()
                .forEach(EntityManagerFactory::close);
        System.out.println("AFTER SUITE!!!");
    }
}

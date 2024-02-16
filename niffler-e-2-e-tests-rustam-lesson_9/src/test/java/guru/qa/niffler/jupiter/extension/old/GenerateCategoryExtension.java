package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.api.client.CategoryRest9Client;
import guru.qa.niffler.jupiter.annotation.old.GenerateCategory;
import guru.qa.niffler.model.Category9Json;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
          .create(GenerateCategoryExtension.class);

    private final CategoryRest9Client categoryRest9Client = new CategoryRest9Client();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateCategory annotation = context.getRequiredTestMethod().getAnnotation(GenerateCategory.class);
        if (annotation != null) {
            Category9Json category = new Category9Json();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());

            Category9Json created = categoryRest9Client.addCategory(category);
            context.getStore(NAMESPACE).put("category", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(Category9Json.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("category", Category9Json.class);
    }

}

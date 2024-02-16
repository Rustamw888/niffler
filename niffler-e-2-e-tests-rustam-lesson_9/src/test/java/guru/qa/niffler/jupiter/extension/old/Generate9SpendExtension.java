package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.api.client.CategoryRest9Client;
import guru.qa.niffler.api.client.Spend9RestClient;
import guru.qa.niffler.jupiter.annotation.old.GenerateSpend;
import guru.qa.niffler.model.Category9Json;
import guru.qa.niffler.model.Spend9Json;
import java.util.Date;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class Generate9SpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE_7 = ExtensionContext.Namespace
          .create(Generate9SpendExtension.class);

    private final Spend9RestClient spend9RestClient = new Spend9RestClient();

    private final CategoryRest9Client categoryRest9Client = new CategoryRest9Client();

//    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
//        .build();
//
//    private final Retrofit retrofit = new Retrofit.Builder()
//        .client(httpClient)
//        .baseUrl("http://127.0.0.1:8093")
//        .addConverterFactory(JacksonConverterFactory.create())
//        .build();
//
//    private final Spend7Service spend7Service = retrofit.create(Spend7Service.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend annotation = context.getRequiredTestMethod().getAnnotation(GenerateSpend.class);
        if (annotation != null) {
            Spend9Json spend = new Spend9Json();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            if (annotation.randomCategory().handleAnnotation()) {
//                CategoryJson randomCategory = new CategoryJson();
//                randomCategory.setCategory(annotation.randomCategory().category());
//                randomCategory.setUsername(annotation.randomCategory().username());
//                spend.setCategory(randomCategory);
                Category9Json category = new Category9Json();
                category.setCategory(annotation.randomCategory().category());
                category.setUsername(annotation.randomCategory().username());

                categoryRest9Client.addCategory(category);

                spend.setCategory(category.getCategory());
            } else {
                spend.setCategory(annotation.category());
            }
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            spend.setAmount(annotation.amount());

            Spend9Json created = spend9RestClient.addSpend(spend);
//            Spend7Json created = spend7Service.addSpend(spend).execute().body();
            context.getStore(NAMESPACE_7).put("spend", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(Spend9Json.class);
    }

    @Override
    public Spend9Json resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE_7).get("spend", Spend9Json.class);
    }
}

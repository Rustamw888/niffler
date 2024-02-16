package guru.qa.niffler.test.web.other;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import guru.qa.niffler.jupiter.annotation.old.ApiRegisterWithRandomUser;
import guru.qa.niffler.jupiter.annotation.old.GenerateCategory;
import guru.qa.niffler.model.Category9Json;
import guru.qa.niffler.model.User9Json;
import guru.qa.niffler.page.component.CategoryRustamComponent;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class CustomCondition9Test extends BaseWebTest {

    final String USERNAME = "test37";
    final String CATEGORY_NAME = "обучение4";
    CategoryRustamComponent categoryRustamComponent = new CategoryRustamComponent();

    @ApiRegisterWithRandomUser(username = USERNAME, password = "12345", submitPassword = "12345")
//    @ApiRegisterWithRandomUser(randomUser = @GenerateRandomUser)
//    @GenerateSpend(
//        username = USERNAME,
//        description = "QA GURU ADVANCED VOL 1",
//        randomCategory = @GenerateCategory(
//            username = USERNAME,
//            category = CATEGORY_NAME
//        ),
//        amount = 52000.00,
//        currency = CurrencyValues.RUB
//    )
    @GenerateCategory(
        username = USERNAME,
        category = CATEGORY_NAME
    )
    @AllureId("60")
    @Test
    void checkLastWeekSpendingWithRandomUserTest(User9Json user9Json, Category9Json category9Json) {
        System.out.println("Это айдишник нового юзера: " + user9Json.getId());

        open(CFG.getFrontUrl() + "/main");

        $("input#react-select-3-input").setValue("обучение").pressEnter();
        $("[placeholder='Set Amount']").setValue("520");
        $("form button.button").click();

//        $(".spendings-table tbody").$$("tr")
//            .find(text(categoryJson.getCategory()))
//            .$$("td").first()
//            .scrollTo()
//            .click();

        categoryRustamComponent.checkTableContainsCategory(category9Json);

//        $$(".button_type_small").find(text("Delete selected"))
//            .click();
//
//        $(".spendings-table tbody")
//            .$$("tr")
//            .shouldHave(CollectionCondition.size(0));
    }

}

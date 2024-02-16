package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.NESTED;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.OUTER;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.model.User9Json;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class FriendsTestLesson9 extends BaseWebTest {

    @ApiLogin(
        user = @GenerateUser(
            incomeInvitations = @IncomeInvitation
        )
    )
    @GenerateUser
    @Test
    @AllureId("21324")
    void incomeInvitationShouldBePresentInTable(
        @GeneratedUser(selector = NESTED) User9Json userForTest,
        @GeneratedUser(selector = OUTER) User9Json another) {
        open(CFG.getFrontUrl() + "/main");
        System.out.println();
    }
}

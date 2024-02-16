package guru.qa.niffler.test.web.other;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import guru.qa.niffler.db.dao.AuthUser9DAO;
import guru.qa.niffler.db.dao.UserDataUser9DAO;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.jupiter.annotation.old.DAO;
import guru.qa.niffler.jupiter.annotation.old.GenerateUser;
import guru.qa.niffler.jupiter.extension.old.DaoExtension;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DaoExtension.class)
public class LoginTestLesson9Part1 extends BaseWebTest {

    @DAO
    private AuthUser9DAO authUser9DAO;
    @DAO
    private UserDataUser9DAO userDataUser9DAO;

    @GenerateUser()
    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity user) {
        open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
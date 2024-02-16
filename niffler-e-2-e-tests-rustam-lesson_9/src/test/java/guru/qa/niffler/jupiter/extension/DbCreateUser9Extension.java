package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.NifflerUser9Repository;
import guru.qa.niffler.db.entity.auth.Authority;
import guru.qa.niffler.db.entity.auth.AuthorityEntity;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.User9Json;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DbCreateUser9Extension extends CreateUser9Extension {

  private final NifflerUser9Repository userRepository = new NifflerUser9Repository();
  private static final String DEFAULT_PASSWORD = "12345";

  @Override
  protected User9Json createUserForTest(GenerateUser annotation) {
    UserEntity authUser = new UserEntity();
    authUser.setUsername(new Faker().name().username());
    authUser.setPassword(DEFAULT_PASSWORD);
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
        .map(a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          ae.setUser(authUser);
          return ae;
        }).toList()));

    userRepository.createUserForTest(authUser);
    User9Json result = User9Json.fromEntity(authUser);
    result.setPassword(DEFAULT_PASSWORD);
    return result;
  }

  @Override
  protected List<User9Json> createFriendIfPresent(GenerateUser annotation) {
    return Collections.emptyList();
  }

  @Override
  protected List<User9Json> createIncomeInvitationsIfPresent(GenerateUser annotation) {
    return Collections.emptyList();
  }

  @Override
  protected List<User9Json> createOutcomeInvitationsIfPresent(GenerateUser annotation) {
    return Collections.emptyList();
  }
}

package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.jupiter.annotation.old.User;
import guru.qa.niffler.jupiter.annotation.old.User.UserType;
import guru.qa.niffler.model.User9Json;
import io.qameta.allure.AllureId;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UserQueueExtensionSeveralNamespaces implements
      BeforeEachCallback,
      AfterTestExecutionCallback,
      ParameterResolver {

    public static Namespace USER_EXTENSION_NAMESPACE_1 = Namespace.create(UserQueueExtensionSeveralNamespaces.class);
    public static Namespace USER_EXTENSION_NAMESPACE_2 = Namespace.create(UserQueueExtensionSeveralNamespaces.class);

    private static Queue<User9Json> USERS_WITH_FRIENDS_QUEUE = new ConcurrentLinkedQueue<>();
    private static Queue<User9Json> USERS_INVITATION_SENT_QUEUE = new ConcurrentLinkedQueue<>();
    private static Queue<User9Json> USERS_INVITATION_RECEIVED_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USERS_WITH_FRIENDS_QUEUE.addAll(
              List.of(userJson("rustam", "12345"), userJson("milki", "12345")));
        USERS_INVITATION_SENT_QUEUE.addAll(
              List.of(userJson("emma", "12345"), userJson("emily", "12345")));
        USERS_INVITATION_RECEIVED_QUEUE.addAll(
              List.of(userJson("anna", "12345"), userJson("bill", "12345")));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Parameter[] testParameters = context.getRequiredTestMethod().getParameters();
        UserType userType = null;
        User9Json user = null;
        int index = 0;
        for (Parameter parameter : testParameters) {
            User desiredUser = parameter.getAnnotation(User.class);
            if (desiredUser != null) {
                userType = desiredUser.userType();

                while (user == null) {
                    switch (userType) {
                        case WITH_FRIENDS -> user = USERS_WITH_FRIENDS_QUEUE.poll();
                        case INVITATION_SENT -> user = USERS_INVITATION_SENT_QUEUE.poll();
                        case INVITATION_RECEIVED -> user = USERS_INVITATION_RECEIVED_QUEUE.poll();
                    }
                }
            }
            if (index == 0) {
                context.getStore(USER_EXTENSION_NAMESPACE_1).put(testId, Map.of(userType, user));
            } else {
                context.getStore(USER_EXTENSION_NAMESPACE_2).put(testId, Map.of(userType, user));
            }

            index++;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<UserType, User9Json> user1 = (Map<UserType, User9Json>) context.getStore(USER_EXTENSION_NAMESPACE_1)
              .get(testId);

        UserType userType = user1.keySet().iterator().next();
        switch (userType) {
            case WITH_FRIENDS -> USERS_WITH_FRIENDS_QUEUE.add(user1.get(userType));
            case INVITATION_SENT -> USERS_INVITATION_SENT_QUEUE.add(user1.get(userType));
            case INVITATION_RECEIVED -> USERS_INVITATION_RECEIVED_QUEUE.add(user1.get(userType));
        }
        Map<UserType, User9Json> user2 = (Map<UserType, User9Json>) context.getStore(USER_EXTENSION_NAMESPACE_1)
              .get(testId);

        UserType userType2 = user1.keySet().iterator().next();
        switch (userType2) {
            case WITH_FRIENDS -> USERS_WITH_FRIENDS_QUEUE.add(user2.get(userType));
            case INVITATION_SENT -> USERS_INVITATION_SENT_QUEUE.add(user2.get(userType));
            case INVITATION_RECEIVED -> USERS_INVITATION_RECEIVED_QUEUE.add(user2.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
          ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(User.class) &&
              parameterContext.getParameter().getType().isAssignableFrom(User9Json.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public User9Json resolveParameter(ParameterContext parameterContext,
          ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        Map<UserType, User9Json> user1 = (Map<UserType, User9Json>) extensionContext.getStore(USER_EXTENSION_NAMESPACE_1)
              .get(testId);
        Map<UserType, User9Json> user2 = (Map<UserType, User9Json>) extensionContext.getStore(USER_EXTENSION_NAMESPACE_2)
              .get(testId);

        List<User9Json> usersList = new ArrayList<>();
        usersList.add(user1.values().iterator().next());
        usersList.add(user2.values().iterator().next());
        for (User9Json user: usersList) {
            return user;
        }
        return null;
    }

    private String getTestId(ExtensionContext context) {
        return Objects
              .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class)
              .value());
    }

    private static User9Json userJson(String userName, String password) {
        User9Json user = new User9Json();
        user.setUsername(userName);
        user.setPassword(password);
        return user;
    }
}
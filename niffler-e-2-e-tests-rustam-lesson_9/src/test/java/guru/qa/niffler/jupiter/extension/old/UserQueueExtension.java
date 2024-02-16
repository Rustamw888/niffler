package guru.qa.niffler.jupiter.extension.old;

import guru.qa.niffler.jupiter.annotation.old.User;
import guru.qa.niffler.jupiter.annotation.old.User.UserType;
import guru.qa.niffler.model.User9Json;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserQueueExtension implements
      BeforeEachCallback,
      AfterTestExecutionCallback,
      ParameterResolver {

    public static Namespace USER_EXTENSION_NAMESPACE = Namespace.create(UserQueueExtension.class);

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
        for (Parameter parameter : testParameters) {
            User desiredUser = parameter.getAnnotation(User.class);
            if (desiredUser != null) {
                UserType userType = desiredUser.userType();

                User9Json user = null;

                while (user == null) {
                    switch (userType) {
                        case WITH_FRIENDS -> user = USERS_WITH_FRIENDS_QUEUE.poll();
                        case INVITATION_SENT -> user = USERS_INVITATION_SENT_QUEUE.poll();
                        case INVITATION_RECEIVED -> user = USERS_INVITATION_RECEIVED_QUEUE.poll();
                    }
                }

                context.getStore(USER_EXTENSION_NAMESPACE).put(testId, Map.of(userType, user));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<UserType, User9Json> user = (Map<UserType, User9Json>) context.getStore(USER_EXTENSION_NAMESPACE)
              .get(testId);

        UserType userType = user.keySet().iterator().next();
        switch (userType) {
            case WITH_FRIENDS -> USERS_WITH_FRIENDS_QUEUE.add(user.get(userType));
            case INVITATION_SENT -> USERS_INVITATION_SENT_QUEUE.add(user.get(userType));
            case INVITATION_RECEIVED -> USERS_INVITATION_RECEIVED_QUEUE.add(user.get(userType));
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
        Map<UserType, User9Json> user = (Map<UserType, User9Json>) extensionContext.getStore(USER_EXTENSION_NAMESPACE)
              .get(testId);
        return user.values().iterator().next();
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

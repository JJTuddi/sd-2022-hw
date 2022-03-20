import controller.LoginController;
import database.JDBConnectionWrapper;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import service.user.UserService;
import service.user.UserServiceMySQL;
import view.LoginView;
import view.UtilityView2;

import java.sql.Connection;

import static database.Constants.Schemas.PRODUCTION;

public class Main {
    public static void main(String[] args) {
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        final AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository,
                rightsRolesRepository);

        final UserService userService = new UserServiceMySQL(userRepository,
                rightsRolesRepository);

        final LoginView loginView = new LoginView();

        final UserValidator userValidator = new UserValidator(userRepository);

        new LoginController(loginView, authenticationService, userValidator);

        loginView.setVisible();

        final UtilityView2 utilityView = new UtilityView2();
    }
}
package core.basesyntax.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    private static final String VALID_LOGIN = "validLogin";
    private static final String VALID_PASSWORD = "validPassword";
    private static final int INVALID_AGE = 17;
    private static final int MIN_VALID_AGE = 18;

    @Mock
    private StorageDao storageDao;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_validUser_ok() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals(user.getLogin(), result.getLogin());
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User("log", VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User(VALID_LOGIN, "pass", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_tooYoung_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, INVALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User(null, VALID_PASSWORD, INVALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_5charsLogin_notOk() {
        User user = new User("valid", VALID_PASSWORD, INVALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_6charsLogin_ok() {
        User user = new User("validL", VALID_PASSWORD, MIN_VALID_AGE);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals("validL", result.getLogin());
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = new User("", VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = new User(VALID_LOGIN, null, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_5charsPass_notOk() {
        User user = new User(VALID_LOGIN, "valid", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_6charsPass_ok() {
        User user = new User(VALID_LOGIN, "validP", MIN_VALID_AGE);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals("validP", result.getPassword());
    }

    @Test
    void register_emptyPass_notOk() {
        User user = new User(VALID_LOGIN, "", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_negativeAge_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, -1);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_18age_ok() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);
        Assert.assertNotNull(result);
        Assert.assertEquals("validLogin", result.getLogin());
    }

    @Test
    void register_loginExist_notOk() {
        User existingUser = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        when(storageDao.get(existingUser.getLogin())).thenReturn(existingUser);
        User newUser = new User(VALID_LOGIN, "newPassword", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }
}

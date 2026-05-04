package core.basesyntax.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private StorageDao storageDao;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        storageDao = Mockito.mock(StorageDao.class);
        registrationService = new RegistrationServiceImpl(storageDao);
    }

    @Test
    void register_nullUser_NotOK() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_validUser_OK() {
        User user = new User("validLogin", "validPassword", 25);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals(user.getLogin(), result.getLogin());
    }

    @Test
    void register_shortLogin_NotOK() {
        User user = new User("log", "validPassword", 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_NotOK() {
        User user = new User("validLogin", "pass", 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_tooYoung_NotOK() {
        User user = new User("validLogin", "validPassword", 17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullUserLogin_NotOK() {
        User user = new User(null, "validPassword", 17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_5charsLogin_NotOK() {
        User user = new User("valid", "validPassword", 17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_6charsLogin_OK() {
        User user = new User("validL", "validPassword", 18);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals("validL", result.getLogin());
    }

    @Test
    void register_emptyLogin_NotOK() {
        User user = new User("", "validPassword", 18);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullUserPassword_notOK() {
        User user = new User("validLogin", null, 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_5charsPass_NotOK() {
        User user = new User("validLogin", "valid", 19);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_6charsPass_OK() {
        User user = new User("validLogin", "validP", 18);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);

        Assert.assertNotNull(result);
        Assert.assertEquals("validP", result.getPassword());
    }

    @Test
    void register_emptyPass_NotOK() {
        User user = new User("validLogin", "", 18);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullUserAge_NotOK() {
        User user = new User("validLogin", "validPassword", null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_negativeAge_NotOK() {
        User user = new User("validLogin", "validPassword", -1);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_18age_OK() {
        User user = new User("validLogin", "validPassword", 18);
        when(storageDao.get(user.getLogin())).thenReturn(null);
        when(storageDao.add(user)).thenReturn(user);

        User result = registrationService.register(user);
        Assert.assertNotNull(result);
        Assert.assertEquals("validLogin", result.getLogin());
    }

    @Test
    void register_loginExist_NotOK() {
        User existingUser = new User("validLogin", "validPassword", 25);
        when(storageDao.get(existingUser.getLogin())).thenReturn(existingUser);
        User newUser = new User("validLogin", "newPassword", 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }


}

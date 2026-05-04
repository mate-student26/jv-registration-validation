package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

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
        User user = new User("", "validPassword", 17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullUserPassword_notOK() {
        User user = new User("validLogin", "", 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullUserAge_NotOK() {
        User user = new User("validLogin", "validPassword", 0);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_loginExist_NotOK() {
        User existingUser = new User("validLogin", "validPassword", 25);
        when(storageDao.get(existingUser.getLogin())).thenReturn(existingUser);
        User newUser = new User("validLogin", "newPassword", 25);
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }
}

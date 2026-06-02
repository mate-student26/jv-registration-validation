package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final String VALID_LOGIN = "validLogin";
    private static final String VALID_PASSWORD = "validPassword";
    private static final int INVALID_AGE = 17;
    private static final int MIN_VALID_AGE = 18;
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        registrationService = new RegistrationServiceImpl(new StorageDaoImpl());
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_validUser_ok() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User("log", VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User(VALID_LOGIN, "pass", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_tooYoung_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, INVALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User(null, VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_5charsLogin_notOk() {
        User user = new User("valid", VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_6charsLogin_ok() {
        User user = new User("validL", VALID_PASSWORD, MIN_VALID_AGE);
        User result = registrationService.register(user);
        assertEquals("validL", result.getLogin());
        assertEquals(1, Storage.people.size());
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = new User("", VALID_PASSWORD, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_nullPassword_notOk() {
        User user = new User(VALID_LOGIN, null, MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_5charsPass_notOk() {
        User user = new User(VALID_LOGIN, "valid", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_6charsPass_ok() {
        User user = new User(VALID_LOGIN, "validP", MIN_VALID_AGE);
        User result = registrationService.register(user);
        assertEquals("validP", result.getPassword());
        assertEquals(1, Storage.people.size());
    }

    @Test
    void register_emptyPass_notOk() {
        User user = new User(VALID_LOGIN, "", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_negativeAge_notOk() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, -1);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_18age_ok() {
        User user = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        User result = registrationService.register(user);
        assertEquals("validLogin", result.getLogin());
        assertEquals(1, Storage.people.size());
    }

    @Test
    void register_existingLogin_notOk() {
        User existingUser = new User(VALID_LOGIN, VALID_PASSWORD, MIN_VALID_AGE);
        Storage.people.add(existingUser);
        User newUser = new User(VALID_LOGIN, "newPassword", MIN_VALID_AGE);
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
        assertEquals(1, Storage.people.size());
    }
}

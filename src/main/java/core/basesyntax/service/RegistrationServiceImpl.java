package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_PWD_LENGTH = 6;
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_AGE = 18;
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User cannot be null");
        }

        if (user.getLogin() == null) {
            throw new RegistrationException("Login can't be null");
        }
        if (user.getPassword() == null) {
            throw new RegistrationException("Password can't be null");
        }
        if (user.getAge() == null) {
            throw new RegistrationException("Age cannot be null");
        }

        if (user.getAge() < MIN_AGE) {
            throw new RegistrationException("User age must be at least " + MIN_AGE
                    + ". Actual age: " + user.getAge());
        }

        if (user.getLogin() == null) {
            throw new RegistrationException("Login cannot be null");
        }

        if (user.getLogin().length() < MIN_LOGIN_LENGTH) {
            throw new RegistrationException("Login must contain at least "
                    + MIN_LOGIN_LENGTH + " characters");

        }

        if (user.getPassword().length() < MIN_PWD_LENGTH) {
            throw new RegistrationException("Password must contain at least "
                    + MIN_PWD_LENGTH + " characters");
        }

        if (user.getPassword() == null) {
            throw new RegistrationException("Password cannot be null");
        }

        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("Login already exist");
        }
        return storageDao.add(user);
    }
}


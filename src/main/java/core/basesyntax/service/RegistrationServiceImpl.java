package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private final int MIN_PASSWORD_LENGTH = 6;
    private final int MIN_LOGIN_LENGTH = 6;
    private final int MIN_AGE = 18;
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User cannot be null");
        }

        if (!(user.getAge() >= MIN_AGE)) {
            throw new RegistrationException("Age must be above 18");
        }

        if (!(user.getLogin().length() >= MIN_LOGIN_LENGTH)) {
            throw new RegistrationException("Login must have 6 or more characters");
        }

        if (!(user.getPassword().length() >= MIN_PASSWORD_LENGTH)) {
            throw new RegistrationException("Password must have 6 or more characters");
        }

        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("Login already exist");
        }
        return storageDao.add(user);
    }
}


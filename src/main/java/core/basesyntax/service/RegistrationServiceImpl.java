package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private final int minPwdLength = 6;
    private final int minLoginLength = 6;
    private final int minAge = 18;
    private final int nullAge = 0;
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user.getLogin().equals("")
                || user.getPassword().equals("")
                || user.getAge().equals(nullAge)) {
            throw new RegistrationException("Username, password and age are mandatory");
        }

        if (user == null) {
            throw new RegistrationException("User cannot be null");
        }

        if (!(user.getAge() >= minAge)) {
            throw new RegistrationException("Age must be above 18");
        }

        if (!(user.getLogin().length() >= minLoginLength)) {
            throw new RegistrationException("Login must have 6 or more characters");
        }

        if (!(user.getPassword().length() >= minPwdLength)) {
            throw new RegistrationException("Password must have 6 or more characters");
        }

        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("Login already exist");
        }
        return storageDao.add(user);
    }
}


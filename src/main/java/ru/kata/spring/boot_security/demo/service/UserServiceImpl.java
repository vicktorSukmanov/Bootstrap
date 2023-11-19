package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public User readUser(long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {

        userRepository.deleteById(id);

    }

    @Override
    @Transactional
    public boolean updateUser(User user) throws EntityNotFoundException {
        User userDB = readUser(user.getId());
        if ((userDB.getUsername()).equals(user.getUsername())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } else if (null != userRepository.findByUsername(user.getUsername())) {
            return false;
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }


}

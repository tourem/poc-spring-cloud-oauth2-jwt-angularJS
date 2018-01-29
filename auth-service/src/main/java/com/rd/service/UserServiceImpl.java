package com.rd.service;

import com.rd.domain.User;
import com.rd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by mtoure on 28/01/2018.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    UserRepository userRepository;

    @Override
    public void create(User user) {
        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setActivated(true);
        userRepository.save(user);
    }

}

package com.github.tkpark.users;

import com.github.tkpark.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public User login(String email, String password) {
        checkNotNull(password, "password must be provided");
        User user = findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Could not found user for " + email));
        user.login(passwordEncoder, password);
        user.afterLoginSuccess();
        userRepository.save(user);
        return user;
    }

    @Transactional
    public SignupResult signup(String name, String email, String password) {
        checkNotNull(name, "name must be provided");
        checkNotNull(email, "email must be provided");
        checkNotNull(password, "password must be provided");

        if(findByEmail(email).isPresent()) {
            throw new NotFoundException("Duplicate email " + email);
        }

        User user = new User(name, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return new SignupResult("Welcome!! " + name);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        checkNotNull(userId, "userId must be provided");

        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        checkNotNull(email, "email must be provided");

        return userRepository.findByEmail(email);
    }

}
